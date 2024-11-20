package com.example.demo.service;

import com.example.demo.entity.CouponEntity;
import com.example.demo.model.*;
import com.example.demo.repository.CouponRepo;
import com.example.demo.utils.Constants;
import com.example.demo.utils.MapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class CouponService {

    public static String COUPON_UPDATE_SUCCESS = "Coupon updated successfully";
    public static String COUPON_UPDATE_FAILED = "Coupon updation failed";

    public static String COUPON_DELETE_SUCCESS = "Coupon deleted successfully";
    public static String COUPON_DELETE_FAILED = "Coupon deletion failed";
    public static String NO_COUPON_WITH_THIS_ID = "Coupon with the given id is not present";
    @Autowired
    private CouponRepo couponRepo;

    public Coupon saveCoupons(Coupon coupon) {
        CouponEntity couponEntity = new CouponEntity();
        couponEntity.setId(coupon.getId());
        couponEntity.setType(coupon.getType());
        couponEntity.setDetails(coupon.getDetails().toString());
        log.info("CouponEntity {}" , couponEntity);
        couponRepo.save(couponEntity);
        return coupon;
    }

    public List<Coupon> getCoupons() throws Exception {
        List<Coupon> allCoupons = new ArrayList<>();

        try {
            List<CouponEntity> couponEntityList = couponRepo.findAll();
            for(CouponEntity couponEntity : couponEntityList) {
                Coupon coupon = createCoupon(couponEntity);
                allCoupons.add(coupon);
            }
        } catch (Exception e) {
            throw new Exception("Exception while fetching the coupon");
        }


        return allCoupons;
    }

    public Coupon getCouponById(int id) {
        Coupon coupon = null;
        try {
            CouponEntity couponEntity = couponRepo.findById(id);
            coupon = createCoupon(couponEntity);

        } catch (Exception e) {
            log.error("Exception while fetching the coupon");
        }

        return coupon;
    }

    public String updateCouponById(Coupon coupon, int id) {
        try {

            CouponEntity couponEntity = couponRepo.findById(id);
            couponEntity.setType(coupon.getType());
            couponEntity.setDetails(coupon.getDetails().toString());
            log.info("CouponEntity {}", couponEntity);
            couponRepo.save(couponEntity);
            return COUPON_UPDATE_SUCCESS;

        } catch (Exception e) {
            log.error("Exception while updating the coupon");
            return COUPON_UPDATE_FAILED;
        }
    }

    public String deleteCouponById(int id) {
        try {
            if(couponRepo.findById(id) != null) {
                couponRepo.deleteById(id);
                return COUPON_DELETE_SUCCESS;
            }
            else {
                return NO_COUPON_WITH_THIS_ID;
            }

        } catch(Exception e) {
            log.error("Exception occured while deleting the coupon");
            return COUPON_DELETE_FAILED;
        }
    }

    public List<CartResponse> applyAllCoupon(Cart cart) throws Exception {
        List<Coupon> allCouponList = getCoupons();
        List<CartResponse> cartWithAllCouponApplied = new ArrayList<>();
        for(Coupon coupon : allCouponList) {
           CartResponse cartResponse =  applyCoupon(cart,coupon.getId());
           cartWithAllCouponApplied.add(cartResponse);
        }
        return cartWithAllCouponApplied;
    }

    public CartResponse applyCoupon(Cart cart, int id) throws JsonProcessingException {
        CartResponse response = null;
        if(couponRepo.findById(id) != null) {

            CouponEntity couponEntity = couponRepo.findById(id);
            Coupon coupon = createCoupon(couponEntity);
            switch (couponEntity.getType()) {
                case "cart-wise":
                    response = applyCartWiseCoupon(cart, coupon);
                    break;
                case "product-wise":
                    response = applyProductWiseCoupon(cart,coupon);
                    break;
                case "bxgy-wise":
                    response = applybxgyWiseCoupon(cart, coupon);
                    break;

                default:
                    log.info("No such Coupon present");
            }
        }

        return response;
    }

    private CartResponse applyCartWiseCoupon(Cart cart, Coupon coupon) {
        int totalAmount = 0;
        for(Item item : cart.getItems()) {

//          int pid =  item.getProductId();
           int q = item.getQuantity();
           int price = q* item.getPrice();
           totalAmount+=price;
        }

        int threshold = Integer.parseInt(coupon.getDetails().get("threshold").toString());
        int discount = Integer.parseInt(coupon.getDetails().get("discount").toString());

        int totalAmountAfterDiscount = totalAmount >= threshold ? totalAmount - (totalAmount*discount)/100 : totalAmount;
        int totalDiscount = totalAmount >= threshold ? (totalAmount*discount)/100 : 0;

        log.info("Totalamount {}" , totalAmountAfterDiscount);

        CartResponse cartResponse = new CartResponse(cart.getItems(), totalAmount, totalDiscount, totalAmountAfterDiscount);
        return cartResponse;

    }

    private CartResponse applyProductWiseCoupon(Cart cart, Coupon coupon) {
        int totalAmount = 0;
        int totalAmountAfterDiscount = 0;
        int discountedProductId = Integer.parseInt(coupon.getDetails().get("product_id").toString());
        int discount = Integer.parseInt(coupon.getDetails().get("discount").toString());

        int totalDiscount = 0;
        for(Item item : cart.getItems()) {
            int q = item.getQuantity();
            int price = q* item.getPrice();
            totalAmount += price;
            if(item.getProductId() == discountedProductId) {
                totalDiscount = (price*discount)/100;
                price = price - (price*discount)/100;
            }
            totalAmountAfterDiscount+=price;
        }

        log.info("Totalamount {}" , totalAmount);

        CartResponse cartResponse = new CartResponse(cart.getItems(), totalAmount, totalDiscount, totalAmountAfterDiscount);
        return cartResponse;


    }

    private CartResponse applybxgyWiseCoupon(Cart cart, Coupon coupon) {
        int totalAmount = 0;
        Map<Integer, Integer> bought = new HashMap<>();
        for(Item item : cart.getItems()) {
            int pid =  item.getProductId();
            int q = item.getQuantity();
            totalAmount+=q* item.getPrice();
            bought.put(pid,q);
        }
        log.info("Cart {}" , cart);
        log.info("Bought {}" , bought);

        Map<Integer,Integer> buyProduct = new HashMap<>();


        List<Object> l = (List<Object>)coupon.getDetails().get("buy_products");

        for(Object o : l) {
            Map<String , Object> map =  MapperUtil.fromJson(o.toString());
            buyProduct.put(Integer.parseInt(map.get("product_id").toString()),
                    Integer.parseInt(map.get("quantity").toString()));
        }

        List<Object> getProductsList = (List<Object>)coupon.getDetails().get("get_products");


        Map<String , Object> map = MapperUtil.fromJson( getProductsList.get(0).toString());
        int quantity1 = Integer.parseInt(map.get("quantity").toString());

        int offer = 0;

        for(int productId : bought.keySet()) {
            int quantityBought = bought.get(productId);
            if(buyProduct.containsKey(productId)) {
                int quantityPerOffer =  buyProduct.get(productId);
                int x = quantityBought/quantityPerOffer;
                offer+=x;

            }
        }
        int repitionLimit = Integer.parseInt(coupon.getDetails().get("repition_limit").toString());

        offer = Math.min(Math.min(repitionLimit,offer), quantity1);

        log.info("Quantity of free product received {}" , offer);

        CartResponse cartResponse = new CartResponse(cart.getItems(), totalAmount, offer, offer);
        return cartResponse;


    }

    private Coupon createCoupon(CouponEntity couponEntity) throws JsonProcessingException {
        Map map = new HashMap<>();
        if(couponEntity.getType().equals(Constants.CART_WISE) ||
                couponEntity.getType().equals(Constants.PRODUCT_WISE)) {
            map = MapperUtil.fromJson(couponEntity.getDetails());
        }
        else {
            map = MapperUtil.parse(couponEntity.getDetails());
        }

       Coupon coupon = new Coupon(couponEntity.getId(), couponEntity.getType(), map);
        return coupon;
    }
}
