package gamaerry.jovenesala42muestranacionaldeteatro

import gamaerry.jovenesala42muestranacionaldeteatro.model.ProfesionalDelTeatro

// esta ya no se usa desde que logre importar mi base de datos
// externa, y esta funcion sera borrada en la versión final
fun getProfesionalesDePrueba(): List<ProfesionalDelTeatro> {
    return listOf(
        ProfesionalDelTeatro(
            id = 13,
            nombre = "Luis Gerardo",
            especialidades = "Actuación",
            descripcion = "Actor de teatro en la Compañía teatral foro 4 de la capital michoacana desde el 2017.",
            estado = "Michoacán",
            urlImagen = "http://fismat.umich.mx/~lg/profesionales/luis.jpg"
        ),
        ProfesionalDelTeatro(
            id = 4,
            nombre = "Victor Sosa",
            especialidades = "Actuación, dirección y dramaturgia",
            descripcion = "Estudiante de la facultad de artes, ganador de los Premios Estatales de Literatura Joven 2022 y \"Soltar las amarras\" durante el 2018.",
            estado = "Chihuahua",
            urlImagen = "http://fismat.umich.mx/~lg/profesionales/victor.jpg"
        ),
        ProfesionalDelTeatro(
            id = 3,
            nombre = "Beatriz Cadena",
            especialidades = "Promoción y coordinación cultural",
            descripcion = "Ganadora del Premio Estatal de la Juventud 2020, actualmente se encuentra trabajando los proyectos becados por Arte Ayuda de Coneculta Chiapas.",
            estado = "Chiapas",
            urlImagen = "http://fismat.umich.mx/~lg/profesionales/beatriz.jpg"
        ),
        ProfesionalDelTeatro(
            id = 23,
            nombre = "Fernando Palma",
            especialidades = "Actuación y dirección",
            descripcion = "Actor fundador de Focus Compañía Teatral, y maestro en el CEACH Centro de Enseñanza Artística de la Chontalpa desde el 2019.",
            estado = "Tabasco",
            urlImagen = "http://fismat.umich.mx/~lg/profesionales/fernando.jpg"
        )
    )
}

//lista de todos los filtros
fun getFiltros() = listOf("Estado", "Especialidad", "Muestra")

// especialidades temporales (no definitivas)
fun getEstados() = listOf(
    "Baja california",
    "Chiapas",
    "Chihuahua",
    "Colima",
    "Michoacán",
    "Tabasco",
    "Yucatán",
)

/*//lista de los estados de la republica mexicana
fun getEstados() = listOf(
    "Aguascalientes",
    "Baja California",
    "Baja California Sur",
    "Campeche",
    "Chiapas",
    "Chihuahua",
    "Ciudad de México",
    "Coahuila",
    "Colima",
    "Durango",
    "Estado de México",
    "Guanajuato",
    "Guerrero",
    "Hidalgo",
    "Jalisco",
    "Michoacán",
    "Morelos",
    "Nayarit",
    "Nuevo León",
    "Oaxaca",
    "Puebla",
    "Querétaro",
    "Quintana Roo",
    "San Luis Potosí",
    "Sinaloa",
    "Sonora",
    "Tabasco",
    "Tamaulipas",
    "Tlaxcala",
    "Veracruz",
    "Yucatán",
    "Zacatecas"
)*/

// especialidades temporales (no definitivas)
fun getEspecialidades() = listOf(
    "Actuación",
    "Coordinación cultural",
    "Dirección",
    "Dramaturgia",
    "Fotografía",
    "Gestión cultural",
    "Musicalización",
    "Operación técnica",
    "Promoción",
)
/*//lista de todas las especialidades
fun getEspecialidades() = listOf(
    "Actuación",
    "Coordinación"
    "Danza",
    "Dirección",
    "Dramaturgia",
    "Escenografía",
    "Fotografía",
    "Gestión cultural",
    "Iluminación",
    "Musicalizacion",
    "Producción técnica",
    "Sonido",
    "Vestuario"
)*/

// lista de todas las MNT hasta ahora
fun getMuestras() = listOf("42 MNT en Torreón, Coahuila (2022)")

