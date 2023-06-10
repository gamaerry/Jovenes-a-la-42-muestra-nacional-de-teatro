package gamaerry.jovenesala42muestranacionaldeteatro.data

enum class Especialidad {
    Actuacion {
        override fun toString() = "actuación"
    },
    Direccion {
        override fun toString() = "dirección"
    },
    Dramaturgia {
        override fun toString() = "dramaturgia"
    },
    Promocion {
        override fun toString() = "promoción"
    },
    CoordinacionCultural {
        override fun toString() = "coordinación cultural"
    },
    GestionCultural {
        override fun toString() = "gestión cultural"
    },
    OperacionTecnica {
        override fun toString() = "operación técnica"
    },
    Codirecion {
        override fun toString() = "codirección"
    },
    Fotografia {
        override fun toString() = "fotografía"
    },
    Musicalizacion {
        override fun toString() = "musicalización"
    };

    fun capitalize() = toString().replaceFirstChar { it.uppercaseChar() }
}
