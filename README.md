# MonkCommerceTask

Created coupon management system using Spring Boot and coonected the service with h2 database

Schema

CouponEntity - Coupon structure that is stored in db

Model

Coupon - request body for coupon
Cart - request body for cart
Item - embedded item for Cart, since list of items are received in cart
CartResponse - response object after the application of coupons

API Endpoints:
● POST /coupons: Create a new coupon.
● GET /coupons: Retrieve all coupons.
● GET /coupons/{id}: Retrieve a specific coupon by its ID.
● PUT /coupons/{id}: Update a specific coupon by its ID.
● DELETE /coupons/{id}: Delete a specific coupon by its ID.
● POST /applicable-coupons: Fetch all applicable coupons for a given cart and
calculate the total discount that will be applied by each coupon.
● POST /apply-coupon/{id}: Apply a specific coupon to the cart and return the
updated cart with discounted prices for each item.

● CartWise coupon Discount

In this calculated the totalAmount for all the items and check if the totalAmount is greater than or equal to the threshold. If yes, apply the discount to the total Amount.

● Product Wise Discount

The Product-Wise Discount coupon logic applies discounts specifically to the product(s) mentioned in the coupon. Here's how it works:

Targeted Discount Application:

The discount is applied only to the product(s) explicitly specified in the coupon.
This ensures that the promotion affects only the intended items in the cart.
Dynamic Cart Calculation:

Once the coupon is applied, the discounted price of the specified product(s) is calculated.
The total cart amount is updated to reflect the sum of:
The discounted prices of eligible products.
The regular prices of non-eligible products.

Example Scenario:

Coupon: "Get 20% off on Product A."
If a customer adds Product A and Product B to their cart:
Product A: Original Price: 100 → Discounted Price: 80
Product B: Price: 50 (No discount)
Total Cart Amount: 80 (discounted) + 50 = 130
This implementation ensures precise discount handling, improves customer transparency, and provides seamless updates to the cart total.


● Another case for product wise discount

1. We can create different coupons for product wise discount for different products and then apply different coupons to the cart.
2. In the single coupon we can have array of product_id with and corresponding array of discounts and then apply coupons to the cart.

● BxGy (Buy X Get Y) Logic Implementation
The implemented Buy X Get Y (BxGy) logic handles scenarios where customers receive Y quantity of a specific product free upon purchasing another product X a certain number of times. Below is an overview of the implementation:

Free Product (Y) Must Be Added to Cart:

Customers will only receive the free product (Y) if they explicitly add it to their cart.
This ensures flexibility, as users may choose not to claim product Y despite meeting the eligibility criteria.
Eligibility Criteria:

Customers must purchase product X at least a specified number of times to qualify for the free product (Y).
For example, "Buy 2 of Product X and Get 1 of Product Y Free."
Limiting the Free Product Quantity:

Even if the user buys X multiple times, the number of free Y products per order is capped.
The maximum free quantity is configurable in the coupon setup, allowing fine-tuned control over promotional limits.
Key Considerations:

Dynamic Validation: The system checks the cart dynamically to validate eligibility for the free product based on the quantity of X purchased.
Customer Flexibility: Users can opt out of claiming Y, preventing unnecessary additions to their order.
Scalability: The promotion logic is extensible, allowing adjustments for various use cases and configurations.
This approach provides a balance between marketing incentives and customer convenience, ensuring that promotional offers are both effective and user-friendly.


● Other Coupons that can be created 
1. Festive-season - we can provide a cart-wise discount with this coupon but here coupon should have a expiration time
2. NewCustomer - if a customer is shopping for the first time , this coupon can be applied cart wise
3. 50% off Upto 100 coupon - in this a threshold amount for discount (say 100) can be set. Example Total Amount is 1000, if 50% is applied to it discount will be 500, so in this case if discount exceeds threshold amount for discount then minimum of 100 and 500 will be applied

● Sample Request Response
POST /coupons: Create a new coupon.

<img width="1035" alt="Screenshot 2024-11-21 at 3 22 51 AM" src="https://github.com/user-attachments/assets/a61cd8bf-ca2b-4304-b337-887a7b17d97f">


   
