package com.example.greenquest.fragments

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.greenquest.R
import androidx.core.content.ContextCompat
import java.util.concurrent.ExecutorService
import android.Manifest
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.lifecycle.ViewModelProvider
import com.example.greenquest.database.DatosEscaneo
import com.example.greenquest.viewmodel.EscanearModel
import com.example.greenquest.viewmodel.ScanState
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors


class EscanearFragment : Fragment() {
    private lateinit var cameraExecutor: ExecutorService
    private var cameraProvider: ProcessCameraProvider? = null

    private var camaraIniciada = false

    private var qrAlreadyDetected = false
    private lateinit var previewView: PreviewView
    private lateinit var informativeMessage: TextView
    private lateinit var qrScanner: BarcodeScanner
    private lateinit var escanearModel: EscanearModel

    private var errorToast: Toast? = null
    private var lastErrorMessage: String? = null

    @OptIn(ExperimentalGetImage::class)
    private var isProcessing = false


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                startCamera()
            } else {
                Toast.makeText(requireContext(), "Permiso de cámara requerido", Toast.LENGTH_SHORT).show()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(
            requireContext(),
                Manifest.permission.CAMERA
        ) != PackageManager.PERMISSION_GRANTED
        ){
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }


        cameraExecutor = Executors.newSingleThreadExecutor()
        qrScanner = BarcodeScanning.getClient()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        escanearModel = ViewModelProvider(this)[EscanearModel::class.java]

        if (ContextCompat.checkSelfPermission(
            requireContext(),
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        ){
            startCamera()
        }
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_escanear, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        previewView = view.findViewById(R.id.qr_camara)
        informativeMessage = view.findViewById(R.id.qr_mensaje_error)

        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        qrAlreadyDetected = false
        lastErrorMessage = ""
        startCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraProvider?.unbindAll()
        cameraProvider = null
    }

    override fun onPause() {
        super.onPause()
        camaraIniciada = false
        cameraProvider?.unbindAll()
    }


    private fun startCamera() {
        if (camaraIniciada) return
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            camaraIniciada = true
            cameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .setTargetResolution(android.util.Size(1280, 720))
                .build().also {
                    it.surfaceProvider = previewView.surfaceProvider
                }

            val imageAnalizer = ImageAnalysis.Builder()
                .setTargetResolution(android.util.Size(1280, 720))
                .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, {
                        imageProxy -> processImageProxy(imageProxy)})
                }

            // Deteniene todos los usos de la camara, para evitar mayor uso de recursos
            cameraProvider?.unbindAll()

            cameraProvider?.bindToLifecycle(
                viewLifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA
                , preview,
                imageAnalizer
            )

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    @OptIn(ExperimentalGetImage::class)
    private fun processImageProxy(imageProxy: ImageProxy) {

        if (isProcessing) {
            imageProxy.close()
            return
        }

        val mediaImage = imageProxy.image
        if (mediaImage == null) {
            imageProxy.close()
            return
        }

        isProcessing = true

        val image = InputImage.fromMediaImage(
            mediaImage,
            imageProxy.imageInfo.rotationDegrees
        )

        escanearModel.processImage(image)
            .addOnCompleteListener {
                isProcessing = false
                imageProxy.close()
            }

    }

    private fun observeViewModel() {
        escanearModel.scanState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ScanState.QRDetected -> {
                    // return@observe hace que se salga del lambda pero NO de observeViewModel
                    if (qrAlreadyDetected) return@observe
                    qrAlreadyDetected = true

                    cameraProvider?.unbindAll()

                    val datosEscaneo = DatosEscaneo(
                        tipoResiduo = state.payload.tipo_residuo,
                        puntos = state.payload.puntaje
                    )

                    val fragment = EscaneadoExitoso.newInstance(datosEscaneo = datosEscaneo)

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.frame_container, fragment)
                        .addToBackStack(null)
                        .commit()
                }
                
                is ScanState.HappyError -> {
                    showToastError("¡Oh no! " + state.message)
                }

                is ScanState.Error -> {
                    showToastError("❌\u200B " + state.message)
                }
                is ScanState.QrException -> {
                    showToastError("ERROR FATAL " + state.message)
                }

                ScanState.Idle -> {
                    Unit
                }
            }
        }
    }

    private fun showToastError(message: String) {
        if (message != lastErrorMessage) {
            errorToast?.cancel()

            errorToast = Toast.makeText(
                requireContext(),
                message,
                Toast.LENGTH_LONG
            )
            errorToast?.show()

            lastErrorMessage = message
        }
    }
}