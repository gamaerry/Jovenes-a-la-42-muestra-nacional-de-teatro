package gamaerry.jovenesala42muestranacionaldeteatro.data

import gamaerry.jovenesala42muestranacionaldeteatro.data.Especialidad.*

fun getProfesionalesDelTeatro(): List<ProfesionalDelTeatro> {
    return listOf(
        ProfesionalDelTeatro(
            "Luis Gerardo",
            listOf(Actuacion),
            "Actor de teatro con más de cinco años de experiencia.",
            1001L,
            "http://fismat.umich.mx/~lg/profesionales/luis.jpg"
        ),
        ProfesionalDelTeatro(
            "Victor Sosa",
            listOf(Actuacion, Direccion, Dramaturgia),
            "Estudiante de la facultad de artes, ganador de los Premios Estatales de Literatura Joven 2022 y \"Soltar las amarras\" durante el 2018.",
            1002L,
            "http://fismat.umich.mx/~lg/profesionales/victor.jpg"
        ),
        ProfesionalDelTeatro("Beatriz Cadena",
            listOf(Promocion, CoordinacionCultural),
            "Ganadora del Premio Estatal de la Juventud 2020, actualmente se encuentra trabajando los proyectos becados por Arte Ayuda de Coneculta Chiapas.",
            1003L,
            "http://fismat.umich.mx/~lg/profesionales/beatriz.jpg"
        ),
        ProfesionalDelTeatro(
            "Fernando Palma",
            listOf(Actuacion, Direccion),
            "Actor fundador de Focus Compañía Teatral, y maestro en el CEACH Centro de Enseñanza Artística de la Chontalpa desde el 2019.",
            1004L,
            "http://fismat.umich.mx/~lg/profesionales/fernando.jpg"
        ),
        ProfesionalDelTeatro(
            "Anahí González",
            listOf(GestionCultural, OperacionTecnica),
            "Maestra en estudios literarios por la Universidad de Colima y gestora cultural en programas y festividades culturales estatales y nacionales.",
            1005L,
            "http://fismat.umich.mx/~lg/profesionales/anahi.jpg"
        ),
        ProfesionalDelTeatro(
            "Edward Chan",
            listOf(OperacionTecnica, Codirecion),
            "Personal artístico en el Centro Estatal de Prevención del Delito y Participación Ciudadana y Co director en Epígrafe (Agrupación escénica independiente).",
            1006L,
            "http://fismat.umich.mx/~lg/profesionales/edward.jpg"
        ),
        ProfesionalDelTeatro(
            "Marina Hernández",
            listOf(Actuacion, Fotografia, Musicalizacion),
            "Becaria nacional por la Facultad de artes UABC, Tijuana. Encargada de fotografía y musicalización de diversas obras de teatro universitario de dicha institución.",
            1007L,
            "http://fismat.umich.mx/~lg/profesionales/marina.jpg"
        )
    )
}