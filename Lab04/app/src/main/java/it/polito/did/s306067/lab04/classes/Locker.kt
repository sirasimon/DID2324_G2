package it.polito.did.s306067.lab04.classes

/**
 * Classe relativa al Locker caratterizzata da:
 * @param ID la stringa univoca relativa del locker che coincide con l'indirizzo MAC del dispositivo
 * @param compartments la lista di scompartimenti di cui dispone il locker
 */
data class Locker(
    val ID : String? = null,
    val compartments : List<Compartment>? = null
)
