package es.unex.giiis.aos.grpc.example;

import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.protobuf.services.ProtoReflectionService;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        /* The port on which the server should run */
        int port = 50051;
        final Server server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
                .addService(ProtoReflectionService.newInstance())
                .addService(new ExampleServiceImpl())
                .build()
                .start();
        server.awaitTermination();
    }
}
