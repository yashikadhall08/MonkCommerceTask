package com.example.demo.model;

import com.example.demo.utils.MapperUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Cart {
    private List<Item> items;

    @Override
    public String toString() {
        return MapperUtil.toJson(this);
    }
}
