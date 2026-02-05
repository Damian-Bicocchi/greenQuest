package com.example.greenquest.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.greenquest.R
import com.example.greenquest.apiParameters.TipoResiduo
import com.example.greenquest.fragments.arguments.OrigenHaciaReporte
import com.example.greenquest.states.reporte.EstadoReporte
import com.example.greenquest.states.reporte.EstadoReporteUI
import com.example.greenquest.viewmodel.ReporteViewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReportarFragment : Fragment() {

    private val args: ReportarFragmentArgs by navArgs()

    private lateinit var origen: OrigenHaciaReporte
    private lateinit var idResiduo: String

    private var fullImageBitmap: Bitmap? = null
    private var thumbnailBitmap: Bitmap? = null

    private lateinit var buttonAbrirCamara: Button
    private lateinit var imgThumbnail: ImageView

    private var currentPhotoPath: String? = null
    private lateinit var outputPhotoUri: Uri

    private lateinit var reporteViewModel: ReporteViewModel


    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            processCameraResult()
        } else {
            Log.d("reporteLogging", "El resultado de la activity fue X")
        }
    }

    private fun startCamera() {
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            Log.e("Camera", "Error creando archivo de imagen", ex)
            null
        }

        photoFile?.also { file ->
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.fileprovider",
                file
            )
            outputPhotoUri = photoURI

            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            takePictureLauncher.launch(takePictureIntent)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss", Locale.getDefault()
        )
            .format(Date())
        val storageDir = requireContext().filesDir

        return File.createTempFile(
            "GREENQUEST_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                startCamera()
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                    showPermissionDialog()
                } else {
                    showPermanentDenialDialog()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        reporteViewModel = ViewModelProvider(this)[ReporteViewModel::class.java]

        origen = args.reporteArgumentos.origenHaciaReporte
        idResiduo = args.reporteArgumentos.idResiduo


        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_reportar,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonEnviarReporte = view.findViewById<Button>(R.id.button_enviar_reporte)

        val selectClasificacion: Spinner = view.findViewById(
            R.id.select_clasificacion_correcta)

        cargarSpinnerCategorias(selectClasificacion)
        observeViewModel()

        selectClasificacion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val seleccion = if (position > 0) {
                    parent.getItemAtPosition(position) as TipoResiduo
                } else {
                    null
                }
                reporteViewModel.seleccionarTipoResiduo(seleccion)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                reporteViewModel.seleccionarTipoResiduo(null)
            }
        }


        imgThumbnail = view.findViewById(R.id.image_thumbnail_reportar)


        buttonAbrirCamara = view.findViewById(R.id.button_abrir_camara_reporte)
        buttonAbrirCamara.setOnClickListener {
            chequearPermisoCamara()
        }


        imgThumbnail.setOnClickListener {
            showFullImage()
        }

        buttonEnviarReporte.setOnClickListener {

            fullImageBitmap?.let {
                bitmap ->
                reporteViewModel.processReport(idResiduo = idResiduo, imageData = bitmap)
            } ?: run {

                mostrarDialogoFallido("Debe tomar primero una foto del residuo")
            }
        }

        val volver = view.findViewById<TextView>(R.id.link_volver_reporte)
        volver.setOnClickListener {
            volverAlOrigenUI()
        }
    }


    private fun processCameraResult() {
        try {
            currentPhotoPath?.let { path ->
                val file = File(path)
                if (file.exists()) {
                    val options = BitmapFactory.Options().apply {
                        inJustDecodeBounds = false
                    }

                    fullImageBitmap = BitmapFactory.decodeFile(path, options)

                    fullImageBitmap?.let { bitmap ->
                        thumbnailBitmap = Bitmap.createScaledBitmap(
                            bitmap,
                            200,
                            (200 * bitmap.height / bitmap.width),
                            true
                        )

                        mostrarThumbnail()

                        Toast.makeText(
                            requireContext(),
                            "Foto tomada",
                            Toast.LENGTH_SHORT
                        ).show()
                        file.delete()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("Camera", "Error procesando el resultado de la cámara $e", e)
        }
    }

    private fun mostrarThumbnail() {
        fullImageBitmap?.let { bitmap ->
            imgThumbnail.setImageBitmap(bitmap)
        }
    }

    private fun showFullImage() {
        fullImageBitmap?.let { bitmap: Bitmap ->
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)

            val imageView = ImageView(requireContext()).apply {
                setImageBitmap(bitmap)
                scaleType = ImageView.ScaleType.FIT_CENTER
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setOnClickListener {
                    dialog.dismiss()
                }
            }

            dialog.setContentView(imageView)
            dialog.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            dialog.show()
        }
    }



    private fun cargarSpinnerCategorias(spinner: Spinner) {
        val categoriasOrdenadas = TipoResiduo.entries.sortedBy { it.name }

        val listaConVacio = mutableListOf<TipoResiduo?>(null).apply {
            addAll(categoriasOrdenadas)
        }

        val adapter = object : ArrayAdapter<TipoResiduo?>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listaConVacio
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: LayoutInflater.from(context)
                    .inflate(
                        android.R.layout.simple_spinner_item,
                        parent,
                        false
                    )

                val textView = view.findViewById<TextView>(android.R.id.text1)
                textView.text = getItem(position)?.name ?: ""
                textView.setTextColor(
                    ContextCompat.getColor(context, R.color.texto_normal)
                )
                return view
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = convertView ?: LayoutInflater.from(context)
                    .inflate(
                        android.R.layout.simple_spinner_dropdown_item,
                        parent,
                        false
                    )

                val textView = view.findViewById<TextView>(android.R.id.text1)
                textView.text = getItem(position)?.name ?: ""
                textView.setTextColor(
                    ContextCompat.getColor(context, R.color.texto_normal)
                )
                return view
            }
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun chequearPermisoCamara() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                startCamera()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                showPermissionDialog()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun showPermissionDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Permiso de cámara requerido")
            .setMessage("Para tomar fotos de residuos, necesitamos acceso a tu cámara. " +
                    "¿Quieres conceder el permiso ahora?")
            .setPositiveButton("SÍ, CONCEDER PERMISO") { _, _ ->
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
            .setNegativeButton("AHORA NO") { _, _ ->
                Toast.makeText(requireContext(),
                    "Puedes conceder el permiso más tarde en Configuración",
                    Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun showPermanentDenialDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Permiso de cámara requerido")
            .setMessage("Has denegado el permiso de cámara permanentemente. " +
                    "Para habilitarlo, ve a Configuración de la aplicación y concede el permiso.")
            .setPositiveButton("IR A CONFIGURACIÓN") { dialog, which ->
                openAppSettings()
            }
            .setNegativeButton("CANCELAR", null)
            .show()
    }

    private fun openAppSettings() {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts(
                "package", requireContext().packageName, null
            )
            intent.data = uri
            startActivity(intent)
        } catch (e: Exception) {
            val intent = Intent(Settings.ACTION_APPLICATION_SETTINGS)
            startActivity(intent)
        }
    }

    private fun volverAlOrigenUI() {
        when (origen) {
            OrigenHaciaReporte.ESCANEAR -> {
                findNavController().navigate(
                    ReportarFragmentDirections
                        .actionReportarFragmentToEscanearFragment())
            }

            OrigenHaciaReporte.ESTADISTICA -> {
                findNavController().navigate(
                    ReportarFragmentDirections
                        .actionReportarFragmentToEstadisticasFragment())
            }

            OrigenHaciaReporte.HISTORIALCOMPLETO -> {
                findNavController().navigate(
                    ReportarFragmentDirections
                        .actionReportarFragmentToHistorialResiduoCompletoFragment())
            }
        }
    }

    private fun observeViewModel() {
        reporteViewModel.reporteUIState.observe(viewLifecycleOwner) { state ->
            when (state) {
                EstadoReporteUI.SinReporte -> {

                    Log.d("reporteLogging","Sin cambios")
                }
                EstadoReporteUI.Reportado -> {
                    mostrarDialogoExitoso()
                }
                is EstadoReporteUI.ReporteFallido -> {
                    mostrarDialogoFallido(state.mensaje)
                }
            }
        }
    }

    private fun mostrarDialogoExitoso() {
        AlertDialog.Builder(requireContext())
            .setTitle("Reporte exitoso")
            .setMessage("El reporte se ha enviado correctamente." +
                    " Muchas gracias por tu colaboración")
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
                reporteViewModel.resetearEstado()
                volverAlOrigenUI()
            }
            .setCancelable(false)
            .show()
    }

    private fun mostrarDialogoFallido(mensaje : String){
        Log.d("reporteLogging", "Mostrar Dialogo Fallido")
        AlertDialog.Builder(requireContext())
            .setTitle("Reporte fallido")
            .setMessage(mensaje)
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
                reporteViewModel.resetearEstado()
            }
            .show()
    }



}

