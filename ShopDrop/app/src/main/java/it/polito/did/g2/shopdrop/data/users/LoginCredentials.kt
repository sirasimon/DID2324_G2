package it.polito.did.g2.shopdrop.data.users

data class LoginCredentials(val uid: String, val email: String, val password: String, val role: UserRole)
