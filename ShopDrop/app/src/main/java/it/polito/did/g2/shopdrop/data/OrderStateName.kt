package it.polito.did.g2.shopdrop.data

/**
 * Valore che può assumere lo stato di una spedizione:
 *
 * **CREATED** - L'ordine è stato creato
 *
 * **RECEIVED** - L'ordine è stato ricevuto in un punto vendita
 *
 * **CARRIED** - L'ordine è stato preso in carico da un fattorino
 *
 * **AVAILABLE** - L'ordine è pronto per il ritiro
 *
 * **COLLECTED** - L'ordine è stato prelevato dall'utente
 */
enum class OrderStateName {
    CREATED,
    RECEIVED,
    CARRIED,
    AVAILABLE,
    COLLECTED,
    ERROR
}