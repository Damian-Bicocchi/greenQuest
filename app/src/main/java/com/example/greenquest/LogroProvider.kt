package com.example.greenquest

import androidx.lifecycle.compose.rememberLifecycleOwner
import com.example.greenquest.Logros.Logro
import com.example.greenquest.apiParameters.TipoResiduo
import com.example.greenquest.repository.UsuarioRepository

class LogroProvider {
    companion object {

        fun logrosObtenidos(): List<Logro> {
            return logros.filter { it.obtenido }
        }

        fun logrosNoObtenidos(): List<Logro> {
            return logros.filter { !it.obtenido }
        }

        fun chequearYActualizarLogros(tipoResiduo: TipoResiduo, cantidadReciclada: Int) {
            for(logro in logrosNoObtenidos()) {
                logro.chequearCumplimiento(cantidadReciclada, tipoResiduo)
            }
        }

        val logros = listOf(
            Logro(
                imagen = R.drawable.carton10,
                nombre = "Cartonero Novato",
                descripcion = "Recicla 10 objetos de cartón",
                requisitoCantidad = 10,
                tipoResiduo = TipoResiduo.CARTON,
                obtenido = false
            ),
            Logro(
                imagen = R.drawable.carton50,
                nombre = "Cartonero Avanzado",
                descripcion = "Recicla 50 objetos de cartón",
                requisitoCantidad = 50,
                tipoResiduo = TipoResiduo.CARTON,
                obtenido = false
            ),
            Logro(
                imagen = R.drawable.carton100,
                nombre = "Maestro del Cartón",
                descripcion = "Recicla 100 objetos de cartón",
                requisitoCantidad = 100,
                tipoResiduo = TipoResiduo.CARTON,
                obtenido = false
            ),
            Logro(
                imagen = R.drawable.plastico10,
                nombre = "Plastico Novato",
                descripcion = "Recicla 10 objetos de plástico",
                requisitoCantidad = 10,
                tipoResiduo = TipoResiduo.PLASTICO,
                obtenido = false
            ),
            Logro(
                imagen = R.drawable.plastico50,
                nombre = "Plastico Avanzado",
                descripcion = "Recicla 50 objetos de plástico",
                requisitoCantidad = 50,
                tipoResiduo = TipoResiduo.PLASTICO,
                obtenido = false
            ),
            Logro(
                imagen = R.drawable.plastico100,
                nombre = "Maestro del Plástico",
                descripcion = "Recicla 100 objetos de plástico",
                tipoResiduo = TipoResiduo.PLASTICO,
                requisitoCantidad = 100,
                obtenido = false
            ),
            Logro(
                imagen = R.drawable.vidrio10,
                nombre = "Vidriero Novato",
                descripcion = "Recicla 10 objetos de vidrio",
                tipoResiduo = TipoResiduo.VIDRIO,
                requisitoCantidad = 10,
                obtenido = false
            ),
            Logro(
                imagen = R.drawable.vidrio50,
                nombre = "Vidriero Avanzado",
                descripcion = "Recicla 50 objetos de vidrio",
                tipoResiduo = TipoResiduo.VIDRIO,
                requisitoCantidad = 50,
                obtenido = false
            ),
            Logro(
                imagen = R.drawable.vidrio100,
                nombre = "Maestro del Vidrio",
                descripcion = "Recicla 100 objetos de vidrio",
                tipoResiduo = TipoResiduo.VIDRIO,
                requisitoCantidad = 100,
                obtenido = false
            ),
            Logro(
                imagen = R.drawable.metal10,
                nombre = "Metálico Novato",
                descripcion = "Recicla 10 objetos de metal",
                tipoResiduo = TipoResiduo.METAL,
                requisitoCantidad = 10,
                obtenido = false
            ),
            Logro(
                imagen = R.drawable.metal50,
                nombre = "Metálico Avanzado",
                descripcion = "Recicla 50 objetos de metal",
                tipoResiduo = TipoResiduo.METAL,
                requisitoCantidad = 50,
                obtenido = false
            ),
            Logro(
                imagen = R.drawable.metal100,
                nombre = "Maestro del Metal",
                descripcion = "Recicla 100 objetos de metal",
                tipoResiduo = TipoResiduo.METAL,
                requisitoCantidad = 100,
                obtenido = false
            ),
            Logro(
                imagen = R.drawable.papel10,
                nombre = "Papelero Novato",
                descripcion = "Recicla 10 objetos de papel",
                tipoResiduo = TipoResiduo.PAPEL,
                requisitoCantidad = 10,
                obtenido = false
            ),
            Logro(
                imagen = R.drawable.papel50,
                nombre = "Papelero Avanzado",
                descripcion = "Recicla 50 objetos de papel",
                tipoResiduo = TipoResiduo.PAPEL,
                requisitoCantidad = 10,
                obtenido = false
            ),
            Logro(
                imagen = R.drawable.papel100,
                nombre = "Maestro del Papel",
                descripcion = "Recicla 100 objetos de papel",
                tipoResiduo = TipoResiduo.PAPEL,
                requisitoCantidad = 100,
                obtenido = false
            )

        )
    }
}