package com.egen.orderpickingservice.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@RequiredArgsConstructor(staticName = "from")
public class Response<T> {

    @JsonProperty("meta")
    @NonNull ResponseMetadata meta;

    @JsonProperty("data")
    @JsonInclude(NON_NULL)
    T data;

    public Response(ResponseMetadata meta , T data){
        this.meta = meta;
        this.data = data;
    }
}