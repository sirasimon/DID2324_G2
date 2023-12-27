package it.polito.did.g2.shopdrop.data

data class UserQuery(val email: String, val password: String, val role: UserRole)
