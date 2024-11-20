package com.example.demo.service;

import com.example.demo.entity.CouponEntity;
import com.example.demo.model.Coupon;
import com.example.demo.repository.CouponRepo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.apache.coyote.http11.Constants.a;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@PrepareForTest({CouponService.class})
public class CouponServiceTest {

    @Mock
    private CouponRepo couponRepo;

    @InjectMocks
    private CouponService couponService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addCouponTest() {
        Coupon coupon = createCoupon();
        System.out.println(coupon);
        Coupon res = couponService.saveCoupons(coupon);
        verify(couponRepo, atLeast(1)).save(any());


    }

    @Test
    public void getCouponById() {
        CouponEntity couponEntity = createCouponEntity();
        Coupon res = couponService.getCouponById(1);
        verify(couponRepo, atLeast(1)).findById(1);

    }

    private CouponEntity createCouponEntity() {
        CouponEntity couponEntity = new CouponEntity();
        couponEntity.setId(1);
        couponEntity.setType("cart-wise");
        return couponEntity;
    }

    private Coupon createCoupon() {
        Coupon coupon = new Coupon();
        coupon.setId(1);
        coupon.setType("cart-wise");
        Map<String, Object> map = new HashMap<>();
        map.put("discount" , 10);
        coupon.setDetails(map);
        return coupon;
    }
}
