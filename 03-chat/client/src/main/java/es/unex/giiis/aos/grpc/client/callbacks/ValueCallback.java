package es.unex.giiis.aos.grpc.client.callbacks;

public interface ValueCallback<T> {
    void onValue(T data);
}
