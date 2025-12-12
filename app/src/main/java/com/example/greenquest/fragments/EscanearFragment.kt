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
import android.util.Log
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
   private lateinit var previewView: PreviewView
   private lateinit var informativeMessage: TextView
   private lateinit var qrScanner: BarcodeScanner
   private lateinit var escanearModel: EscanearModel

    private var errorToast: Toast? = null
    private var lastErrorMessage: String? = null

    @OptIn(ExperimentalGetImage::class)
    private var isProcessing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        )
        { isGranted: Boolean ->
            if (isGranted) {
                startCamera()
                informativeMessage.visibility = View.INVISIBLE
            } else {
                informativeMessage.text = "No se obtuvo el permiso para acceder a la cámara"
                informativeMessage.visibility = View.VISIBLE
            }

        }
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        cameraExecutor = Executors.newSingleThreadExecutor()
        qrScanner = BarcodeScanning.getClient()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        escanearModel = ViewModelProvider(this).get(EscanearModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_escanear, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        previewView = view.findViewById(R.id.qr_camara)
        informativeMessage = view.findViewById(R.id.qr_mensaje_error)

        observeViewModel()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .setTargetResolution(android.util.Size(1280, 720)) // Use android.util.Size
                .build().also {
                    it.surfaceProvider = previewView.surfaceProvider
                }

            val imageAnalizer = ImageAnalysis.Builder()
                .setTargetResolution(android.util.Size(1280, 720))
                .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, { imageProxy -> processImageProxy(imageProxy)})
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageAnalizer
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

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun observeViewModel() {

        escanearModel.scanState.observe(viewLifecycleOwner) { state ->
            when (state) {

                is ScanState.QRDetected -> {
                    // Mover UI, navegar, etc.

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

                is ScanState.Error -> {
                    if (state.message != lastErrorMessage) {
                        errorToast?.cancel()

                        errorToast = Toast.makeText(
                            requireContext(),
                            state.message,
                            Toast.LENGTH_LONG
                        )
                        errorToast?.show()

                        lastErrorMessage = state.message
                    }
                }

                ScanState.Idle -> {
                    Unit
                }
            }
        }
    }
}