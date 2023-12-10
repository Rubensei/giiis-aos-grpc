import grpc

import example_pb2_grpc, example_pb2

local_uri = 'localhost:50051'
remote_uri = None  # TODO: Asignar a la uri especificada durante la sesión para realizar la conexión


def main():
    uri = remote_uri if remote_uri is not None else local_uri
    with grpc.insecure_channel(uri) as channel:
        stub = example_pb2_grpc.ExampleServiceStub(channel)
        response = stub.GetEcho(example_pb2.EchoMessage(message="Hello World!"))
        print(response.message)


if __name__ == "__main__":
    main()
