package com.example.demo.repository;

import com.example.demo.entity.CouponEntity;
import com.example.demo.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepo extends JpaRepository<CouponEntity,Integer> {

    public List<CouponEntity> findAll();
    public CouponEntity findById(int id);
    public void deleteById(int id);
}
