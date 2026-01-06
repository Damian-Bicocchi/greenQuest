package com.example.greenquest

import com.example.greenquest.apiParameters.TipoResiduo

class LogroProvider {
    companion object {

        fun logrosObtenidos(): List<Logro> {
            return logros.filter { it.obtenido }
        }

        fun logrosNoObtenidos(): List<Logro> {
            return logros.filter { !it.obtenido }
        }


        val logros = listOf(
            Logro(
                imagen = R.drawable.carton10,
                nombre = "Cartonero Novato",
                descripcion = "Recicla 10 objetos de cartón",
                obtenido = false
            ),
            Logro(
                imagen = R.drawable.carton50,
                nombre = "Cartonero Avanzado",
                descripcion = "Recicla 50 objetos de cartón",
                obtenido = false
            ),
            Logro(
                imagen = R.drawable.carton100,
                nombre = "Maestro del Cartón",
                descripcion = "Recicla 100 objetos de cartón",
                obtenido = false
            ),
            Logro(
                imagen = R.drawable.plastico10,
                nombre = "Plastico Novato",
                descripcion = "Recicla 10 objetos de plástico",
                obtenido = false
            ),
            Logro(
                imagen = R.drawable.plastico50,
                nombre = "Plastico Avanzado",
                descripcion = "Recicla 50 objetos de plástico",
                obtenido = false
            ),
            Logro(
                imagen = R.drawable.plastico100,
                nombre = "Maestro del Plástico",
                descripcion = "Recicla 100 objetos de plástico",
                obtenido = false
            ),
            Logro(
                imagen = R.drawable.vidrio10,
                nombre = "Vidriero Novato",
                descripcion = "Recicla 10 objetos de vidrio",
                obtenido = false
            ),
            Logro(
                imagen = R.drawable.vidrio50,
                nombre = "Vidriero Avanzado",
                descripcion = "Recicla 50 objetos de vidrio",
                obtenido = false
            ),
            Logro(
                imagen = R.drawable.vidrio100,
                nombre = "Maestro del Vidrio",
                descripcion = "Recicla 100 objetos de vidrio",
                obtenido = false
            ),
            Logro(
                imagen = R.drawable.metal10,
                nombre = "Metálico Novato",
                descripcion = "Recicla 10 objetos de metal",
                obtenido = false
            ),
            Logro(
                imagen = R.drawable.metal50,
                nombre = "Metálico Avanzado",
                descripcion = "Recicla 50 objetos de metal",
                obtenido = false
            ),
            Logro(
                imagen = R.drawable.metal100,
                nombre = "Maestro del Metal",
                descripcion = "Recicla 100 objetos de metal",
                obtenido = false
            ),
            Logro(
                imagen = R.drawable.papel10,
                nombre = "Papelero Novato",
                descripcion = "Recicla 10 objetos de papel",
                obtenido = false
            ),
            Logro(
                imagen = R.drawable.papel50,
                nombre = "Papelero Avanzado",
                descripcion = "Recicla 50 objetos de papel",
                obtenido = false
            ),
            Logro(
                imagen = R.drawable.papel100,
                nombre = "Maestro del Papel",
                descripcion = "Recicla 100 objetos de papel",
                obtenido = false
            )

        )
    }
}