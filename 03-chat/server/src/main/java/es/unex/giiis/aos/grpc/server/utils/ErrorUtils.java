package es.unex.giiis.aos.grpc.server.utils;

import com.google.protobuf.Any;
import com.google.rpc.ErrorInfo;
import com.google.rpc.Status;
import io.grpc.StatusException;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.StatusProto;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ErrorUtils {
    public static StatusException buildError(String message, com.google.rpc.Code code, ErrorInfo... errors) {
        return StatusProto.toStatusException(Status.newBuilder()
                .setCode(code.getNumber())
                .setMessage(message)
                .addAllDetails(Arrays.stream(errors).map(Any::pack).collect(Collectors.toList()))
                .build());
    }
}
