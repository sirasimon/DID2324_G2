package it.polito.did.s306067.lab04.classes

import java.sql.Timestamp

data class StateValue(
    val stateName: StateName? = null,
    val timestamp: Timestamp? = null
)
