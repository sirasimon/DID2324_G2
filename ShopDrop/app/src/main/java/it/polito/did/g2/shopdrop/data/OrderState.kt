package it.polito.did.g2.shopdrop.data

import java.time.LocalDateTime

class OrderState(
    val state: OrderStateName,
    val timestamp: LocalDateTime
)