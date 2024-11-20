package com.example.demo.response;

import com.example.demo.model.Coupon;
import com.example.demo.utils.MapperUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CouponServiceResponse {


    public static <T> ResponseEntity<T> executeAndGetResponse(ServiceExecutor<T> serviceExecutor) {
        long startTime = System.currentTimeMillis();
        ResponseEntity<T> response = responseFrom(serviceExecutor.execute());
        return response;
    }

    private static <T> ResponseEntity<T> responseFrom(T responseFromService) {
        if(responseFromService instanceof ResponseWithHttpCode) {
            HttpStatus httpStatusCode = ((ResponseWithHttpCode) responseFromService).getHttpStatusCode();
            return getResponse(responseFromService, Objects.nonNull(httpStatusCode) ? httpStatusCode : HttpStatus.OK);
        }
        else {
            return getResponse(responseFromService);
        }
    }

    private static <T> ResponseEntity<T> getResponse(final T response, HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus).body(response);
    }

    private static <T> ResponseEntity<T> getResponse(final T response) {
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public String toString() {
        return MapperUtil.toJson(this);
    }


}
