package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.model.CartResponse;
import com.example.demo.model.Coupon;
import com.example.demo.repository.CouponRepo;
import com.example.demo.response.CouponServiceResponse;
import com.example.demo.service.CouponService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/coupons")
public class CouponController {

    @Autowired
    CouponRepo couponRepo;

    @Autowired
    CouponService couponService;

    @PostMapping(value = "/post",  produces = "application/json")
    @ResponseBody
    public ResponseEntity<Coupon> addCoupons(@RequestBody Coupon coupon) {

        return CouponServiceResponse.executeAndGetResponse(
                () -> couponService.saveCoupons(coupon));
    }

    @GetMapping(value = "/get" ,  produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<Coupon>> getCoupon() throws Exception {
        return CouponServiceResponse.executeAndGetResponse(
                () -> {
                    try {
                        return couponService.getCoupons();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @GetMapping(value = "/{id}" , produces = "application/json")
    @ResponseBody
    public ResponseEntity<Coupon> getCouponById(@PathVariable (value = "id") int id) {
        return CouponServiceResponse.executeAndGetResponse(
                () -> couponService.getCouponById(id));
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> updateCoupon(@RequestBody Coupon coupon,
                                               @PathVariable (value = "id") int id) {


        return CouponServiceResponse.executeAndGetResponse(
                () -> couponService.updateCouponById(coupon,id));
    }

    @DeleteMapping(value = "/{id}" , produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> deleteCoupon(@PathVariable (value = "id") int id) {
        return CouponServiceResponse.executeAndGetResponse(
                () -> couponService.deleteCouponById(id));
    }

    @PostMapping(value =  "/apply-coupon/{id}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<CartResponse> applyCoupon(@RequestBody Cart cart,
                                                    @PathVariable (value = "id") int id) {
       return CouponServiceResponse.executeAndGetResponse(() -> {
           try {
               return couponService.applyCoupon(cart,id);
           } catch (JsonProcessingException e) {
               throw new RuntimeException(e);
           }
       });
    }

    @PostMapping(value = "/applicable-coupon", produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<CartResponse>> applyAllCoupon(@RequestBody Cart cart) {
        return CouponServiceResponse.executeAndGetResponse(() -> {
            try {
                return couponService.applyAllCoupon(cart);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
