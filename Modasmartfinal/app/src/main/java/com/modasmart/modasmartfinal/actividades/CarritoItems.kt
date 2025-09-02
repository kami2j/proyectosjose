package com.modasmart.modasmartfinal.actividades

data class CarritoItems(
    var id: String = "",  // Asegúrate de que los tipos coincidan con los datos en tu base de datos
    var nombre: String = "",
    var precio: Long = 0  // Cambiado a Long para reflejar el cambio en el tipo de datos
) {
    // Constructor sin argumentos necesario para Firebase
    constructor() : this("", "", 0)
}
