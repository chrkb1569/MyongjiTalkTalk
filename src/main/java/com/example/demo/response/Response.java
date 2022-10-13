package com.example.demo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private int code;
    private boolean success;
    private Result result;

    public static Response success() {
        return new Response(0, true, null);
    }

    public static <T> Response success(T data) {
        return new Response(0, true, new Success(data));
    }

    public static Response failure(int code, String message) {
        return new Response(code, false, new Failure(message));
    }
}
