package store.result;

import store.product.Product;
import store.product.ProductRepository;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static store.constants.UtilContants.MEMBERSHIP_DISCOUNT_LIMIT;
import static store.constants.UtilContants.NULL;

public class Result {
    private HashMap<String, Integer> boughtProducts = new LinkedHashMap<>();
    private HashMap<String, Integer> promotions = new LinkedHashMap<>();
    private int total = 0;
    private int nonPromotionTotal = 0;
    private int membershipDiscountRate = 0;
    private int promotionDiscount = 0;
    private int membershipDiscount = 0;
    private int moneyToPay = 0;
    private int totalCount = 0;
    private ProductRepository productRepository;

    public Result(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void Reset() {
        boughtProducts = new LinkedHashMap<>();
        promotions = new LinkedHashMap<>();
        total = 0;
        nonPromotionTotal = 0;
        membershipDiscountRate = 0;
        promotionDiscount = 0;
        membershipDiscount = 0;
        moneyToPay = 0;
        totalCount = 0;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public HashMap<String, Integer> getBoughtProducts() {
        return boughtProducts;
    }

    public HashMap<String, Integer> getPromotions() {
        return promotions;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

    public int getMoneyToPay() {
        return moneyToPay;
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }

    public void updateBoughtProducts(String productName, int count) {
        totalCount += count;
        Product product = productRepository.getItemByKey(productName);
        if (product.getType().equals(NULL)) {
            nonPromotionTotal += (product.getPrice() * count);
        }
        boughtProducts.put(productName, count);
    }

    public void decreaseBoughtProducts(String productName, int count){
        totalCount -= count;
        Product product = productRepository.getItemByKey(productName);
        boughtProducts.put(productName, boughtProducts.get(productName) - count);
    }

    public void updatePromotions(String product, int count) {
        promotions.put(product, count);
        if(promotions.get(product) == 0){
            promotions.remove(product);
        }
    }

    public void updateNonPromotionTotal(String productName, int count){
        Product product = productRepository.getItemByKey(productName);
        nonPromotionTotal += (product.getPrice() * count);
    }

    public void updateRepository() {
        for (String key : boughtProducts.keySet()) {
            Product product = productRepository.getItemByKey(key);
            product.decreaseCount(boughtProducts.get(key));
            productRepository.updateItem(product, key);
        }
    }

    public void setMembershipDiscount() {
        membershipDiscountRate = 30;
    }

    public int getTotal() {
        return total;
    }

    public void calculateResult() {
        calculateTotal();
        calculatePromotionDiscount();
        calculateMembershipDiscount();
        calculateMoneyToPay();
        updateRepository();
    }

    private void calculateTotal() {
        for (String key : boughtProducts.keySet()) {
            Product product = productRepository.getItemByKey(key);
            total += product.getPrice() * boughtProducts.get(key);
        }
    }

    private void calculatePromotionDiscount() {
        for (String key : promotions.keySet()) {
            Product product = productRepository.getItemByKey(key);
            promotionDiscount += product.getPrice() * promotions.get(key);
        }
    }

    private void calculateMembershipDiscount() {
        int discount = (nonPromotionTotal / 100) * membershipDiscountRate;
        if (discount > MEMBERSHIP_DISCOUNT_LIMIT) {
            discount = MEMBERSHIP_DISCOUNT_LIMIT;
        }
        membershipDiscount = discount;
    }

    private void calculateMoneyToPay() {
        moneyToPay = total - promotionDiscount - membershipDiscount;
    }


}
