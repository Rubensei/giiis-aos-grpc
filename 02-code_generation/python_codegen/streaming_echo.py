import grpc

import example_pb2_grpc, example_pb2

local_uri = 'localhost:50051'
remote_uri = None  # TODO: Asignar a la uri especificada durante la sesión para realizar la conexión


# This class works as a blocking iterator returning the console input until "exit" is found
class LineIter:
    def __iter__(self):
        return self

    def __next__(self):
        message = input()
        if message != "exit":
            return example_pb2.EchoMessage(message=message)
        raise StopIteration


def main():
    uri = remote_uri if remote_uri is not None else local_uri
    print("Write \"exit\" to stop sending messages")
    with grpc.insecure_channel(uri) as channel:
        stub = example_pb2_grpc.ExampleServiceStub(channel)
        response_stream = stub.StreamingEcho(LineIter())
        for echo_message in response_stream:
            print("Received echo: " + echo_message.message)


if __name__ == "__main__":
    main()
