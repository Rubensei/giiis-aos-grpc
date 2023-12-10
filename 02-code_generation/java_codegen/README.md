# Generación de código
En java la generación de código se integra con las build tools utilizadas para la compilación del proyecto, en este caso gradle.
Dicha condiguración se puede ver en la sección `protobuf` del fichero `build.gradle`.

Gracias a esta integración, los cambios en el `.proto` se compilaran automáticamente junto con nuestro código java.

# Instanciación del servidor
Las librerías de gRPC nos ofrecen una implementación por defecto del servidor, al cual únicamente será necesario añadir la definición de nuestros servicios.
Esta instanciación se realiza con el siguiente código
```java
int port = 50051;
final Server server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
        .addService(ProtoReflectionService.newInstance())
        .addService(new ExampleServiceImpl())
        .build()
        .start();
server.awaitTermination();
```

# Implementación de métodos
En el servidor la generación de código de grpc nos define una clase base `ExampleServiceBaseImpl` que contiene todo el código de manejo de peticiones y respuestas, siendo necesario únicamente sobreescribir los métodos de los distintos procedimientos remotos definidos en el `.proto`

## GetEcho
En este método hay que retornar como respuesta el mismo mensaje enviado por el cliente. Para mandar un mensaje se utiliza
```java
responseObserver.onNext(message); // En este caso message = request
```

Finalmente, para notificar que la comunicación se ha completado, es necesario llamar al método `onComplete`
```java
responseObserver.onComplete();
```

## GetRandom
En este ejemplo es necesario procesar la petición mediante los métodos generados para el tipo de mensaje.

Podemos observar las siguientes peculiaridades:
 * Únicamente los campos con presencia explícita tendrán métodos `hasXXX`.
 * El campo _min_ tendrá valor 0 cuando no se asigne un valor en cliente
```java
final int numbers = request.hasNumbers() ? request.getNumbers() : 1;
final int min = request.getMin();
final int max = request.hasMax() ? request.getMax() : min+1;
```

Al tratarse de una llamada remote con streaming en servidor, en este caso se podrá llamar tantas veces a `onNext` como sea necesario. 
Una vez se termina de enviar datos es necesario notificar con `onComplete`

Es necesario construir las distintas respuestas haciendo uso del _builder_ generado
```java
for(int i = 0; i < numbers; i++) {
    final float value = new Random().nextFloat(min, max);
    final RandomResponse response = RandomResponse.newBuilder().setValue(value).build();
    responseObserver.onNext(response);
}
responseObserver.onCompleted();
```

## StreamingEcho
Al tratarse de un stream bidireccional será necesario retornar una instancia `StreamObserver<T>`. Esta instancia será utilizada por la implementación de gRPC para observar el stream de mensajes que manda el cliente, por ello se llamará a sus métodos cuando:
 * Cliente manda mensaje -> `onNext`
 * Cliente notifica final de stream -> `onComplete`
 * Cliente notifica error -> `onError`

Dado que estamos implementando un echo que debe responder a los mensajes del cliente con su mismo contenido, esta instancia debe llamar a los métodos correspondientes de `responseObserver`
```java
return new StreamObserver<>() {
    @Override
    public void onNext(EchoMessage value) {
        responseObserver.onNext(value);
    }

    @Override
    public void onError(Throwable t) {
        responseObserver.onError(t);
    }

    @Override
    public void onCompleted() {
        responseObserver.onCompleted();
    }
};
```