package it.polito.did.g2.shopdrop.data.users

import it.polito.did.g2.shopdrop.data.OrderStateName

//data class User(val uid: String, val email: String, val password: String, val name: String, val role: UserRole)

interface User {
    val uid: String
    val email: String
    val password: String
    val name: String
    val role: UserRole
}

class AdmUser(
    override val uid: String,
    override val email: String,
    override val password: String,
    override val name: String,
    override val role: UserRole,
) : User

class CstUser(
    override val uid: String,
    override val email: String,
    override val password: String,
    override val name: String,
    override val role: UserRole,
    orders : Map<String, OrderStateName>? = null,
    isBanned : Boolean = false
) : User

class CrrUser(
    override val uid: String,
    override val email: String,
    override val password: String,
    override val name: String,
    override val role: UserRole,
    isFree : Boolean = false
) : User