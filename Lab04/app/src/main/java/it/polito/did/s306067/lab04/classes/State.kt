package it.polito.did.s306067.lab04.classes

import it.polito.did.s306067.lab04.StateName
import java.sql.Timestamp

data class State(
    val stateName: StateName? = null,
    val timestamp: Timestamp? = null
)
