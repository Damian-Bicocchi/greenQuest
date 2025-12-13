object ChequeosUsuario {

    fun esValidoUsername(username: String): Boolean {
        val regex = Regex("^[a-zA-Z0-9._-]{3,20}$")

        return regex.matches(username)
    }

    fun esValidoFormatoContraseña(contraseña: String): Boolean {
        val regex = Regex("^(?=.*[0-9])(?=.*[a-z]*)(?=.*[A-Z]).{8,}$")
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