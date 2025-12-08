object ChequeosUsuario {

    fun esValidoCorreo(correo: String): Boolean {
        val regex = Regex("[a-zA-Z0-9._]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,}")

        return regex.matches(correo)
    }

    fun esValidoFormatoContraseña(contraseña: String): Boolean {
        val regex = Regex("^(?=.*[0-9])[0-9A-Za-z!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]{8,}$")
        return regex.matches(contraseña)
    }

    fun esValidoConfirmarContraseña(contraseña: String, confirmar: String): Boolean {
        return contraseña == confirmar
    }

    fun camposCompletos(vararg campos: String): Boolean {
        for (campo in campos) {
            if (campo.isBlank()) {
                return false
            }
        }
        return true
    }
}