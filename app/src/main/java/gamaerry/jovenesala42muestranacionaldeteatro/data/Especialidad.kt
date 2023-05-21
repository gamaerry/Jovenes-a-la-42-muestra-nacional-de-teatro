package gamaerry.jovenesala42muestranacionaldeteatro.data

enum class Especialidad {
    Actuacion {
        override fun principal() = "Actuación"
        override fun toString() = "actuación"
    },
    Direccion {
        override fun principal() = "Dirección"
        override fun toString() = "dirección"
    },
    Dramaturgia {
        override fun principal() = "Dramaturgia"
        override fun toString() = "dramaturgia"
    },
    Promocion {
        override fun principal() = "Promoción"
        override fun toString() = "promocion"
    },
    CoordinacionCultural {
        override fun principal() = "Coordinación cultural"
        override fun toString() = "coordinación cultural"
    },
    GestionCultural {
        override fun principal() = "Gestión cultural"
        override fun toString() = "gestión cultural"
    },
    OperacionTecnica {
        override fun principal() = "Operación técnica"
        override fun toString() = "operación técnica"
    },
    Codirecion {
        override fun principal() = "Codirección"
        override fun toString() = "codirección"
    },
    Fotografia {
        override fun principal() = "Fotografíá"
        override fun toString() = "fotografía"
    },
    Musicalizacion {
        override fun principal() = "Musicalización"
        override fun toString() = "musicalización"
    };

    abstract fun principal(): String
}
