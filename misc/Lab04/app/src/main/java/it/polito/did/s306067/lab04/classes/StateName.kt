package it.polito.did.s306067.lab04.classes

enum class StateName {  //L'ordine è...
    PROCESSING,         //... in preparazione
    SHIPPING,           //... in consegna
    AVAILABLE,          //... disponibile al ritiro
    COLLECTED,          //... stato ritirato
    CANCELLED,          //... stato cancellato
    TIMEOUT             //... stato riportato indietro causa superamento tempo massimo
}