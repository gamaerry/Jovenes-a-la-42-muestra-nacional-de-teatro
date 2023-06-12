package gamaerry.jovenesala42muestranacionaldeteatro.model

// esta ya no se usa desde que logre importar mi base de datos
// externa, y este archivo será borrado en la versión final
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