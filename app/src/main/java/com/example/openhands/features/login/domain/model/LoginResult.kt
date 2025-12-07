package com.example.openhands.features.login.domain.model

sealed class LoginResult {
    object Success : LoginResult()

    // 1. Añadimos una propiedad `message` opcional a la clase base de fallo.
    sealed class Failure(val message: String? = null) : LoginResult() {
        // 2. Estos objetos ahora heredan el constructor de Failure (con message = null por defecto)
        object EmptyFields : Failure()
        object InvalidCredentials : Failure()

        // 3. Este tipo de fallo sí puede llevar un mensaje personalizado.
        class Unknown(message: String) : Failure(message)
    }
}
