package it.polito.did.g2.shopdrop.data.users

//data class User(val uid: String, val email: String, val password: String, val name: String, val role: UserRole)

open class User (
    val uid: String,
    val email: String,
    val password: String,
    val name: String,
    val role: UserRole
)

/*
class AdmUser(
    uid: String,
    email: String,
    password: String,
    name: String,
    role: UserRole,
) : User(uid, email, password, name, role)

class CstUser(
    uid: String,
    email: String,
    password: String,
    name: String,
    role: UserRole,
    var orders : Map<String, OrderStateName>? = null,
    var isBanned : Boolean = false
) : User(uid, email, password, name, role)

class CrrUser(
    uid: String,
    email: String,
    password: String,
    name: String,
    role: UserRole,
    var isFree : Boolean = false
) : User(uid, email, password, name, role)
*/