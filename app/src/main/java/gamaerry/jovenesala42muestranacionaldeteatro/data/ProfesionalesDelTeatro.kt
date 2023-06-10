package gamaerry.jovenesala42muestranacionaldeteatro.data

fun getProfesionalesDelTeatro(): List<ProfesionalDelTeatro> {
    return listOf(
        ProfesionalDelTeatro(
            nombre = "Luis Gerardo",
            especialidades = "Actuación",
            descripcion = "Actor de teatro con más de cinco años de experiencia.",
            urlImagen = "http://fismat.umich.mx/~lg/profesionales/luis.jpg"
        ),
        ProfesionalDelTeatro(
            nombre = "Victor Sosa",
            especialidades = "Actuacion, direccion y dramaturgia",
            descripcion = "Estudiante de la facultad de artes, ganador de los Premios Estatales de Literatura Joven 2022 y \"Soltar las amarras\" durante el 2018.",
            urlImagen = "http://fismat.umich.mx/~lg/profesionales/victor.jpg"
        ),
        ProfesionalDelTeatro(
            nombre = "Beatriz Cadena",
            especialidades = "Promoción y coordinación cultural",
            descripcion = "Ganadora del Premio Estatal de la Juventud 2020, actualmente se encuentra trabajando los proyectos becados por Arte Ayuda de Coneculta Chiapas.",
            urlImagen = "http://fismat.umich.mx/~lg/profesionales/beatriz.jpg"
        ),
        ProfesionalDelTeatro(
            nombre = "Fernando Palma",
            especialidades = "Actuación y dirección",
            descripcion = "Actor fundador de Focus Compañía Teatral, y maestro en el CEACH Centro de Enseñanza Artística de la Chontalpa desde el 2019.",
            urlImagen = "http://fismat.umich.mx/~lg/profesionales/fernando.jpg"
        ),
        ProfesionalDelTeatro(
            nombre = "Anahí González",
            especialidades = "Gestion cultural y operación técnica",
            descripcion = "Maestra en estudios literarios por la Universidad de Colima y gestora cultural en programas y festividades culturales estatales y nacionales.",
            urlImagen = "http://fismat.umich.mx/~lg/profesionales/anahi.jpg"
        ),
        ProfesionalDelTeatro(
            nombre = "Edward Chan",
            especialidades = "Operación técnica y codirecion",
            descripcion = "Personal artístico en el Centro Estatal de Prevención del Delito y Participación Ciudadana y Co director en Epígrafe (Agrupación escénica independiente).",
            urlImagen = "http://fismat.umich.mx/~lg/profesionales/edward.jpg"
        ),
        ProfesionalDelTeatro(
            nombre = "Marina Hernández",
            especialidades = "Actuación, fotografía y musicalización",
            descripcion = "Becaria nacional por la Facultad de artes UABC, Tijuana. Encargada de fotografía y musicalización de diversas obras de teatro universitario de dicha institución.",
            urlImagen = "http://fismat.umich.mx/~lg/profesionales/marina.jpg"
        )
    )
}