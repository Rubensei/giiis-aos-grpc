package es.unex.giiis.aos.grpc.server;

import com.google.protobuf.Any;
import com.google.rpc.ErrorInfo;
import com.google.rpc.Status;
import io.grpc.protobuf.StatusProto;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ErrorUtils {
    public static Throwable buildError(String message, com.google.rpc.Code code, ErrorInfo... errors) {
        return StatusProto.toStatusRuntimeException(Status.newBuilder()
                .setCode(code.getNumber())
                .setMessage(message)
                .addAllDetails(Arrays.stream(errors).map(Any::pack).collect(Collectors.toList()))
                .build());
    }
}
