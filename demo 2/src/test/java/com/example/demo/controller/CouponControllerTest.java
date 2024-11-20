package com.example.demo.controller;

import com.example.demo.repository.CouponRepo;
import com.example.demo.service.CouponService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CouponController.class})
public class CouponControllerTest {

    @Mock
    CouponRepo couponRepo;

    @Mock
    CouponService couponService;

    @InjectMocks
    CouponController couponController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

}
