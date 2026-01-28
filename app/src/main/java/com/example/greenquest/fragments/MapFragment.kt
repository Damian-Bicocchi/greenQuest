package com.example.greenquest.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.greenquest.R
import com.example.greenquest.apiParameters.Estacion
import com.example.greenquest.databinding.FragmentMapBinding
import com.example.greenquest.ui.menu_principal
import com.example.greenquest.viewmodel.MapViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MapFragment : Fragment() {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MapViewModel by viewModels()
    private var currentRoute: Polyline? = null
    private var locationOverlay: MyLocationNewOverlay? = null

    inner class CustomInfoWindow(mapView: MapView) :
        MarkerInfoWindow(R.layout.map_bubble, mapView) {
        var mSelectedStation: Estacion? = null

        init {
            val btn =
                (mView.findViewById<View>(R.id.bubble_moreinfo)) as Button
            btn.setOnClickListener { _ ->
                activity?.let { activity ->
                    val dialogBuilder = MaterialAlertDialogBuilder(activity)
                    dialogBuilder.setMessage("¿Trazar ruta?")
                        .setPositiveButton("Confirmar") { _, _ ->
                            viewLifecycleOwner.lifecycleScope.launch {
                                val ctx = requireContext()
                                val waypoints = arrayListOf(
                                    locationOverlay?.myLocation,
                                    mSelectedStation?.let {
                                        GeoPoint(it.latitud, it.longitud)
                                    }
                                )

                                val road = fetchRoad(ctx, waypoints)

                                if (road.mStatus != Road.STATUS_OK) {
                                    Toast.makeText(
                                        ctx,
                                        "Error al encontrar ruta.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    drawRoute(road, mapView)
                                }
                            }
                        }
                        .setNegativeButton("Cancelar", null)
                        .create()
                        .show()
                    true
                } ?: false
            }
        }

        override fun onOpen(item: Any?) {
            super.onOpen(item)
            mView.findViewById<View?>(R.id.bubble_moreinfo)!!.visibility =
                View.VISIBLE
            val marker = item as Marker
            mSelectedStation = marker.relatedObject as Estacion?
        }
    }


    private val permissionRequester =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            when {
                isGranted -> onPermissionGranted()

                ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) -> {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Mapa de Estaciones")
                        .setMessage("Para acceder al mapa de las estaciones se requiere que la aplicación tenga acceso a tu ubicación")
                        .setPositiveButton("Dar permisos") { _, _ ->
                            requirePermission()
                        }
                        .setNegativeButton("Volver", null)
                        .create()
                        .show()
                }

                else -> {
                    Toast.makeText(
                        requireContext(),
                        "Se regresará al menú.",
                        Toast.LENGTH_LONG
                    ).show()
                    // Request permision directly and if not allowed return to menu
                    startActivity(Intent(activity, menu_principal::class.java))
                    requireActivity().finish()

                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.obtenerEstaciones()
        if (Configuration.getInstance().userAgentValue == "osmdroid") {
            Configuration.getInstance().userAgentValue = requireActivity().packageName
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            onPermissionGranted()
        } else {
            // This actually shows the system permission dialog
            permissionRequester.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        return binding.root
    }

    override fun onPause() {
        super.onPause()

        locationOverlay?.disableMyLocation()
        locationOverlay?.disableFollowLocation()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            locationOverlay?.disableMyLocation()
            locationOverlay?.disableFollowLocation()
        } else {
            locationOverlay?.enableMyLocation()
            locationOverlay?.enableFollowLocation()
        }
    }

    override fun onStop() {
        super.onStop()
        locationOverlay?.disableMyLocation()
        locationOverlay?.disableFollowLocation()
    }


    override fun onResume() {
        super.onResume()

        locationOverlay?.enableMyLocation()
        locationOverlay?.enableFollowLocation()
    }

    private fun onPermissionGranted() {
        val mapView = binding.mapview
        mapView.setDestroyMode(false)
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setUseDataConnection(true)
        mapView.setMultiTouchControls(true)
        mapView.isClickable = true

        val mapViewController = mapView.controller
        locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), mapView)
        locationOverlay?.setPersonAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        mapView.overlays.add(locationOverlay)
        locationOverlay?.enableMyLocation()
        locationOverlay?.enableFollowLocation()
        mapViewController.setZoom(15.0)

        val poiIcon = ResourcesCompat.getDrawable(resources, R.drawable.poi_marker, null)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stations.collect { stations ->
                    for (station in stations) {
                        val poiMarker = Marker(mapView)
                        poiMarker.relatedObject = station;
                        poiMarker.title = station.nombre
                        poiMarker.snippet = "Estación #${station.id}"
                        poiMarker.position = GeoPoint(station.latitud, station.longitud)
                        poiMarker.icon = poiIcon
                        poiMarker.infoWindow = CustomInfoWindow(mapView)
                        poiMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        mapView.overlays.add(poiMarker)
                    }
                }
            }
        }
    }

    private fun requirePermission() {
        permissionRequester.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private suspend fun fetchRoad(
        ctx: Context,
        waypoints: ArrayList<GeoPoint?>
    ): Road = withContext(Dispatchers.IO) {
        val roadManager: RoadManager = OSRMRoadManager(ctx, ctx.packageName)
        roadManager.getRoad(waypoints)
    }


    private fun drawRoute(road: Road, mapView: MapView) {
        val newRoute: Polyline =
            RoadManager.buildRoadOverlay(
                road,
                R.color.accent_color,
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    6f,
                    resources.displayMetrics
                ) // En teoría esto hace que escale según el DPI.
            )
        currentRoute?.let {
            mapView.overlays.remove(it)
        }
        mapView.overlays.add(newRoute)
        currentRoute = newRoute
    }
}