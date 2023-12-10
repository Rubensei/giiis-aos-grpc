# Guión
Para ilustar la generación de código y su uso en diferentes lenguajes se implementarán un servidor java y un cliente python que se comunicarán entre sí utilizando gRPC

## Cliente
Primero se implementará el cliente, realizando la conexión sobre el servidor alojado en la ip indicada en la sesión.

Esta implementación se detalla en `python_codegen/README.md`

## Servidor
Una vez implementado el cliente pasamos a implementar el servidor, para poder probar los métodos será necesario volver a configurar los clientes python para utilizar conexión local

La implementación del servidor se detalla en `java_codegen/README.md`