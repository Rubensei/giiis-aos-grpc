package es.unex.giiis.aos.grpc.example;


import es.unex.giiis.aos.grpc.example.generated.EchoMessage;
import es.unex.giiis.aos.grpc.example.generated.ExampleServiceGrpc;
import es.unex.giiis.aos.grpc.example.generated.RandomRequest;
import es.unex.giiis.aos.grpc.example.generated.RandomResponse;
import io.grpc.stub.StreamObserver;

import java.util.Random;

public class ExampleServiceImpl extends ExampleServiceGrpc.ExampleServiceImplBase {
    @Override
    public void getEcho(EchoMessage request, StreamObserver<EchoMessage> responseObserver) {
        System.out.println(request.getMessage());
        responseObserver.onNext(request);
        responseObserver.onCompleted();
    }

    @Override
    public void getRandom(RandomRequest request, StreamObserver<RandomResponse> responseObserver) {
        final int numbers = request.hasNumbers() ? request.getNumbers() : 1;
        final int min = request.getMin();
        final int max = request.hasMax() ? request.getMax() : min+1;
        for(int i = 0; i < numbers; i++) {
            final float value = new Random().nextFloat(min, max);
            final RandomResponse response = RandomResponse.newBuilder().setValue(value).build();
            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<EchoMessage> streamingEcho(StreamObserver<EchoMessage> responseObserver) {
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
    }
}
