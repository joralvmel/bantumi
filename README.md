# Bantumi

## Funcionalidades Agregadas

### Reiniciar Partida
- Al pulsar la opción "Reiniciar", se muestra un diálogo de confirmación.
- En caso de respuesta afirmativa, se procede a reiniciar la partida actual.

### Guardar Partida
- Permite guardar la situación actual del tablero.
- Solo se guarda una única partida utilizando el sistema de ficheros del dispositivo.

### Recuperar Partida
- Recupera el estado de una partida guardada, leyendo del dispositivo.
- Si la partida actual ha sido modificada, se solicita confirmación antes de recuperar la partida guardada.

### Guardar Puntuación
- Al finalizar cada partida, se guarda la información necesaria para generar un listado de resultados.
- El listado incluye el nombre del jugador ,fecha y hora de la partida, número de semillas en cada almacén y duración de la partida.
- La información se guarda en una base de datos SQLite.

### Mejores Resultados
- Muestra el histórico con los diez mejores resultados obtenidos, ordenados por el mayor número de semillas obtenido por cualquier jugador.
- Incluye un botón, con confirmación, para eliminar todos los resultados guardados.

### Mejoras adicionales
- Permite modificar el nombre del jugador desde los ajustes del juego.
- Permite modificar el número inicial de semillas desde los ajustes del juego.
- Permite elegir qué jugador hace el primer movimiento mediante un toggle.
- Añade un cronómetro a la partida y guarda el tiempo en la base de datos.