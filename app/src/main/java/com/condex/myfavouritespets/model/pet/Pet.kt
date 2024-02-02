package com.condex.myfavouritespets.model.pet

data class Pet (
    var id: Int,
    var Nombre:String,
    var tipoPelaje: String,
    var clase: String,
    var foto: String,
    var nivelAmorosidad: NivelAmor,
    var enlacePagWeb: String,
    var favotiro: Boolean
        )