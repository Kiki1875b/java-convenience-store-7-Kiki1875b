package store.product;

import java.util.List;

import static store.constants.ErrorConstants.EXCEEDED_STOCK_QUANTITY;
import static store.constants.UtilContants.*;

public class Product {
    private final String name;
    private final int price;
    private int ordinaryCount = 0;
    private int promotionCount = 0;
    private String type = NULL;

    public Product(List<String> productData) {
        name = productData.get(PRODUCT_NAME_INDEX);
        price = Integer.parseInt(productData.get(PRICE_INDEX));
        type = setType(productData.get(PROMOTION_TYPE_INDEX));
        ordinaryCount = updateOrdinaryCount(productData);
        promotionCount = updatePromotionCount(productData);
    }

    public int updateOrdinaryCount(List<String> productData) {
        if (productData.get(PROMOTION_TYPE_INDEX).equals(NULL)) {
            return Integer.parseInt(productData.get(ORDINARY_COUNT_INDEX));
        }
        return ordinaryCount;
    }

    public int updatePromotionCount(List<String> productData) {
        if (productData.get(PROMOTION_TYPE_INDEX).equals(NULL)) {
            return promotionCount;
        }
        return Integer.parseInt(productData.get(ORDINARY_COUNT_INDEX));
    }

    public void setOrdinaryCount(List<String> productData) {
        if (productData.get(PROMOTION_TYPE_INDEX).equals(NULL)) {
            ordinaryCount = Integer.parseInt(productData.get(ORDINARY_COUNT_INDEX));
        }
    }

    public void setPromotionCount(List<String> productData) {
        if (!productData.get(PROMOTION_TYPE_INDEX).equals(NULL)) {
            promotionCount = Integer.parseInt(productData.get(ORDINARY_COUNT_INDEX));
        }
    }

    public void checkIfEnoughInStock(int count) {
        if (ordinaryCount + promotionCount < count) {
            throw new IllegalArgumentException(EXCEEDED_STOCK_QUANTITY.getMessage());
        }
    }

    public boolean userHasToBringMore(int count, int buy, int get) {
        if(count + get > promotionCount){
            return false;
        }
        if(promotionCount - count - get <= ZERO){
            return false;
        }
        if (count % (buy + get) == buy) {
            return true;
        }
        return false;
    }

    public int unableToGetPromotion(int count, int buy, int get) {
        if (promotionCount - count > ZERO) {
            return 0;
        }
        int promotionGroupSize = buy + get;
        int maxPromotionsByPromotionCount = promotionCount / promotionGroupSize;
        int maxPromotionsByCount = count / promotionGroupSize;
        int numberOfPromotions = Math.min(maxPromotionsByPromotionCount, maxPromotionsByCount);
        int totalPromotionItems = numberOfPromotions * promotionGroupSize;
        int unableToApply = count - totalPromotionItems;
        return Math.max(unableToApply, 0);
    }


    public boolean isPromotionProduct() {
        return !type.equals(NULL);
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getOrdinaryCount() {
        return ordinaryCount;
    }

    public int getPromotionCount() {
        return promotionCount;
    }

    public String getType() {
        return type;
    }

    public void decreaseCount(int count) {
        if (promotionCount >= count) {
            promotionCount -= count;
            return;
        }
        count -= promotionCount;
        promotionCount = 0;
        if (ordinaryCount >= count) {
            ordinaryCount -= count;
            return;
        }
    }

    public void hasEnoughPromotionStock(int count) {

    }

    private String setType(String input) {
        if (!type.equals(NULL)) {
            return type;
        }
        return input;
    }

}
