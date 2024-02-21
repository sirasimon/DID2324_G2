package it.polito.did.g2.shopdrop.data.users

/**
 * Ruoli che possono essere assunti dagli utenti
 *
 * CST - Customer
 * CRR - Carrier
 * ADM - Admin
 */
enum class UserRole {
    /**
     * Cliente
     */
    CST,
    /**
     * Carrier
     */
    CRR,
    /**
     * Admin
     */
    ADM,
    NUL
}