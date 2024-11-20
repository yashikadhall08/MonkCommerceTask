package com.example.demo.model;

import com.example.demo.utils.MapperUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item {
    private int productId;
    private int quantity;
    private int price;

    @Override
    public String toString() {
        return MapperUtil.toJson(this);
    }
}
