package store.promotion;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import camp.nextstep.edu.missionutils.DateTimes;

public class PromotionRepository {
    HashMap<String, Promotion> promotions = new HashMap<>();

    public void addPromotion(Promotion promotion) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(promotion.getStartDate(), formatter);
        LocalDate endDate = LocalDate.parse(promotion.getEndDate(), formatter);
        LocalDate currentDate = DateTimes.now().toLocalDate();
        if (currentDate.isBefore(startDate) || currentDate.isAfter(endDate)) {
            promotion.setBuyCount(Integer.MAX_VALUE);
            promotion.setGetCount(0);
        }
        promotions.put(promotion.getPromotionName(), promotion);
    }

    public Promotion getPromotionByKey(String key) {
        return promotions.get(key);
    }

    public boolean promotionExists(String key) {
        return promotions.containsKey(key);
    }
}
