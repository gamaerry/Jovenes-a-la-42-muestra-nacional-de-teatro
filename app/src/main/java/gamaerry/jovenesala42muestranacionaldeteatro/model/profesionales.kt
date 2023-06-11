package gamaerry.jovenesala42muestranacionaldeteatro.model

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
        ),
        ProfesionalDelTeatro(
            id = 7,
            nombre = "Anahí González",
            especialidades = "Gestión cultural y operación técnica",
            descripcion = "Maestra en estudios literarios por la Universidad de Colima y gestora cultural en programas y festividades culturales estatales y nacionales.",
            estado = "Colima",
            urlImagen = "http://fismat.umich.mx/~lg/profesionales/anahi.jpg"
        ),
        ProfesionalDelTeatro(
            id = 27,
            nombre = "Edward Chan",
            especialidades = "Operación técnica y codireción",
            descripcion = "Personal artístico en el Centro Estatal de Prevención del Delito y Participación Ciudadana y Co director en Epígrafe (Agrupación escénica independiente).",
            estado = "Yucatán",
            urlImagen = "http://fismat.umich.mx/~lg/profesionales/edward.jpg"
        ),
        ProfesionalDelTeatro(
            id = 2,
            nombre = "Marina Hernández",
            especialidades = "Actuación, fotografía y musicalización",
            descripcion = "Becaria nacional por la Facultad de artes UABC, Tijuana. Encargada de fotografía y musicalización de diversas obras de teatro universitario de dicha institución.",
            estado = "Baja california",
            urlImagen = "http://fismat.umich.mx/~lg/profesionales/marina.jpg"
        )
    )
}