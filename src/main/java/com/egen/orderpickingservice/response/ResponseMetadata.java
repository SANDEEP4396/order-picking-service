package com.egen.orderpickingservice.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Data
@Builder
@RequiredArgsConstructor(staticName = "from")
public class ResponseMetadata {

    int statusCode;
    @NonNull
    String statusMessage;
    @JsonInclude(NON_EMPTY)
    Map<String, Object> tags;

    public ResponseMetadata(int statusCode , String statusMessage, Map<String, Object> tags){
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.tags = tags;
    }

}