# Inicialización de entorno virtual e instalación de dependencias
```bash
# Creación del entorno virtual
virtualenv .venv
# Linux
source .venv/Scripts/activate
# Windows CMD
.venv\Scripts\activate.bat
# Windows PowerShell
.venv\Scripts\activate
# Instalación de dependencias
pip install -r requirements.txt
```

# Generación de código y ejecución
Con el entorno virtual activo
```bash
python -m grpc_tools.protoc -I../protos --python_out=. --pyi_out=. --grpc_python_out=. ../protos/example.proto
```

Para poder ejecutar uno de los scripts cliente, utilizaremos, con el entorno virtual activo el comando `python my_script.py`

Es necesario volver a generar cada vez que se realizan cambios en el `.proto`

# Implementación de cliente echo.py
Utilizando el canal ya definido en el puerto local por defecto, hay que instanciar el _stub_ del cliente.
Esta clase se encuentra en el archivo generado de grpc `example_pb2_grpc`

```python
stub = example_pb2_grpc.ExampleServiceStub(channel)
```

Una vez instanciado se podrá llamar a los métodos definidos para el servicio en el `.proto`. Para ello será necesario importar el módulo de definición de mensajes `example_pb2` y a través del stub definido con anterioridad llamar al método GetEcho pasando un EchoMessage por parámetros para obtener una respuesta del servidor.
```python
response = stub.GetEcho(example_pb2.EchoMessage(message="Hello World!"))
print(response.message)
```

# Implementación de get_random_rpc.py

En este caso se utiliza un stream unidireccional como resultado de la invocación del método GetRandom del servicio, sobre esta colección iteramos e imprimiremos en pantalla el resultado.
```python
for response in stub.GetRandom(example_pb2.RandomRequest(numbers=10, min=0, max=10)):
    print(response.value)
```
# Implementación de streaming_echo.py
En este caso se utiliza un stream bidireccional en el que el servidor responderá a cada uno de los mensajes que envíe el cliente.

En python el streaming cliente implica que la llamada remote tendrá como parámetro un iterador (`LinesIter`) que debe retornar el tipo de mensaje correspondiente, es por ello necesario añadir la creación de este mensaje en su método `__next__`
```python
return example_pb2.EchoMessage(message=message)
```

Cuando la llamada remota retorna un stream de servidor en python se obtiene un iterador sobre los distintos mensajes recibidos, será por ello necesario iterar sobre ese resultado e imprimir los mensajes
```python
response_stream = stub.StreamingEcho(LineIter())
for echo_message in response_stream:
    print("Received echo: " + echo_message.message)
```