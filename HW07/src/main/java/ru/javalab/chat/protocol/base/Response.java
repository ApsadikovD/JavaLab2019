package ru.javalab.chat.protocol.base;

public class Response<T> {
    private T data;

    private Response(T data) {
        this.data = data;
    }

    public Response() {
    }

    public static <E> Response<E> build(E data) {
        return new Response<>(data);
    }

    public T getData() {
        return data;
    }
}
