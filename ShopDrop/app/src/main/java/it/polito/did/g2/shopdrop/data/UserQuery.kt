package it.polito.did.g2.shopdrop.data

/**
 * Classe per l'interrogazione del db per il login.
 * Il parametro **role** può assumere il valore null, in tal caso significa che c'è stato un errore nel processamento della query
 */
class UserQuery(val email: String, val password: String, var role: UserRole? = null, var errType: LOGIN_ERROR_TYPE? = null){
    enum class LOGIN_ERROR_TYPE{
        PASSWORD,
        NOT_FOUND,
        UNKNOWN
    }

    override fun toString(): String{
        return "Query: email = $email, password = $password, role = $role, errType = $errType"
    }
}
