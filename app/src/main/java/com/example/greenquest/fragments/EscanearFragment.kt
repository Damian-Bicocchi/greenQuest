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
import androidx.lifecycle.lifecycleScope
import com.example.greenquest.ResiduoInfo
import com.example.greenquest.RetrofitInstance
import com.example.greenquest.apiParameters.scanning.ReclamarResiduoRequest
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import kotlinx.serialization.json.Json


class EscanearFragment : Fragment() {
   private lateinit var cameraExecutor: ExecutorService
   private lateinit var previewView: PreviewView
   private lateinit var informativeMessage: TextView
   private lateinit var qrScanner: BarcodeScanner

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_escanear, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        previewView = view.findViewById(R.id.qr_camara)
        informativeMessage = view.findViewById(R.id.qr_mensaje_error)

    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .setTargetResolution(android.util.Size(1280, 720)) // Use android.util.Size
                .build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
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
    private fun processImageProxy(imageProxy: ImageProxy){
        val mediaImage = imageProxy.image
        if (mediaImage != null){
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            qrScanner.process(image)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        viewLifecycleOwner.lifecycleScope.launch {
                            handleQrCode(barcode = barcode)
                        }
                    }
                }
                .addOnFailureListener {
                    informativeMessage.text = "No se pudo detectar correctamente el Qr"
                }
                .addOnCompleteListener {
                    imageProxy.close()
                    Log.d("greenQuestProcessImageProxy", "Cierro")

                }
        }

    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private suspend fun handleQrCode(barcode: Barcode){
        val jsonString : String? = barcode.displayValue

        try {
            // Parsear el JSON
            val productInfo: ResiduoInfo? = jsonString?.let { Json.decodeFromString(it) }
            val idResiduo : String = productInfo?.id_residuo ?: ""

            if (idResiduo.isBlank()) {
                informativeMessage.text = "El QR no pudo ser leído correctamente"
                return
            }
            try {
                val request = ReclamarResiduoRequest(idResiduo = idResiduo)
                val response = RetrofitInstance.api.residuoReclamar(request)

                if (response.isSuccessful) {
                    val body = response.body()
                    when {
                        body?.message != null -> {
                            val datosEscaneo = productInfo?.let {
                                DatosEscaneo(
                                    tipoResiduo = it.tipo_residuo,
                                    puntos = productInfo.puntaje
                                )
                            }
                            val fragment = EscaneadoExitoso.newInstance(datosEscaneo = datosEscaneo!!)
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.frame_container, fragment)
                                .commit()
                        }
                        body?.error != null -> {
                            // Error del servidor (aunque HTTP 200)
                            informativeMessage.text = body.error
                        }
                        else -> {
                            informativeMessage.text = "El servidor no reacciona. Reintente"
                        }
                    }
                } else {
                    informativeMessage.text = "El servidor no reacciona. Reintente"
                }
            } catch (e: Exception) {
                informativeMessage.text = e.toString()
            }
            // Post
        } catch (e: Exception) {
            println("Error al parsear JSON: ${e.message}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }



    companion object {
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
            ).toTypedArray()
    }
}