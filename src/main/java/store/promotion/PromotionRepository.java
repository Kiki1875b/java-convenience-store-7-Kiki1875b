package store.promotion;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import camp.nextstep.edu.missionutils.DateTimes;
import store.product.Product;
import store.product.ProductRepository;

public class PromotionRepository {
    HashMap<String, Promotion> promotions = new HashMap<>();

    public void addPromotion(Promotion promotion, ProductRepository productRepository) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime startDate = LocalDate.parse(promotion.getStartDate(), formatter).atStartOfDay();
        LocalDateTime endDate = LocalDate.parse(promotion.getEndDate(), formatter).atStartOfDay();


        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("[ERROR] 프로모션 종료일이 시작일보다 빠를 수 없습니다.");
        }

        LocalDateTime currentDate = LocalDate.of(DateTimes.now().getYear(), DateTimes.now().getMonth(), DateTimes.now().getDayOfMonth()).atStartOfDay();
        if (!currentDate.isBefore(startDate) && !currentDate.isAfter(endDate)) {
            promotions.put(promotion.getPromotionName(), promotion);
        }else{
            for(String key : productRepository.getProducts().keySet()){
                Product product = productRepository.getItemByKey(key);
                if(product.getType().equals(promotion.getPromotionName())){
                    int promotions = product.getPromotionCount();
                    product.setPromotionCount(0);
                    product.setOrdinaryCount(product.getOrdinaryCount() + promotions);
                    productRepository.updateItem(product, key);
                }
            }
        }
    }

    public Promotion getPromotionByKey(String key) {
        return promotions.get(key);
    }

    public boolean promotionExists(String key) {
        return promotions.containsKey(key);
    }
}
