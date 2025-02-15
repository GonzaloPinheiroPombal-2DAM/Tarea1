Cambios añadidos al proyecto

  - Se creó ContactRepository para encapsular el acceso a la base de datos (Room).
  - Se creó WeatherRepository para encapsular el acceso a la API del clima.
  - Se añadieron use cases en el paquete domain:
    - WheatherApiServiceUseCase para obtener los datos del clima.
    - ContactsUseCase para trabajar con la base de datos room.
  - Se actualizó el MainScreenViewModel para utilizar estos casos de uso en lugar de acceder directamente a los datos.
  - Se añadió un botón para eliminar todos lo contactos agregados.
