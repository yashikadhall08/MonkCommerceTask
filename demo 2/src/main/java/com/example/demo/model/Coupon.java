package com.example.demo.model;

import com.example.demo.utils.MapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Coupon {


    private int id;

    private String type;


    private Map<String, Object> details;

    @Override
    public String toString() {
        return MapperUtil.toJson(this);
    }
}
