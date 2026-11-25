# BigWalk Básico

Aplicación Java de consola, módulos 1 a 3, con datos de ejemplo en memoria.

## Rutas duplicadas

Los nombres se comparan sin mayúsculas, tildes vocálicas ni espacios repetidos.
Registrar una ruta crea un **borrador**. Agregue todos sus tramos y use la opción
5 del módulo 1 para confirmar el itinerario. Dos rutas confirmadas no pueden
tener los mismos extremos y la misma secuencia de nombres normalizados de tramos.
La distancia no identifica una calle: cambiarla no evita la detección.

Los nombres de tramo deben identificar consistentemente la vía o segmento.
No hay coordenadas ni catálogo de segmentos: nombres distintos del mismo camino
no se pueden reconocer automáticamente; caminos distintos deben tener nombres
distintos. El orden y el sentido del recorrido sí importan.

Los borradores permiten construir alternativas con prefijos comunes y calcular
indicadores provisionales. Una confirmación rechazada conserva el borrador tal
como estaba, sin confirmarlo. Las altas, modificaciones y eliminaciones de tramos
de rutas confirmadas validan una lista propuesta antes de tocar datos o IDs.
No se puede dejar una ruta confirmada vacía. Las calificaciones y reportes no
definen identidad del recorrido. Las modificaciones deben pasar por los gestores;
los setters públicos del modelo no son una API validada.

## Ejecución y pruebas

Con un JDK instalado, desde la raíz del proyecto en PowerShell:

```powershell
New-Item -ItemType Directory -Force build | Out-Null
$fuentes = Get-ChildItem src,test -Recurse -Filter *.java | ForEach-Object FullName
javac -encoding UTF-8 -d build $fuentes
java -cp build PruebasBigWalk
java -cp build PruebasDuplicados
java -cp build interfaz.BigWalkApp
```

Verificación de esta revisión: 64 pruebas existentes y 19 de duplicados correctas.
Sin enum ni throw. Sin base de datos ni persistencia entre ejecuciones.
La comparación automática de rutas (módulo 4) sigue pendiente.
