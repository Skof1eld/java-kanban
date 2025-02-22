package server;

public enum HttpMethod {
    GET,
    POST,
    DELETE;

    public static HttpMethod fromString(String method) {
        return HttpMethod.valueOf(method.toUpperCase());
    }
}