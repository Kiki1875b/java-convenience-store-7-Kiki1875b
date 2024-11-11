package store.promotion;

import java.util.List;

import static store.constants.UtilContants.*;

public class Promotion {
    private String promotionName;
    private int buyCount;
    private int getCount;
    private String startDate;
    private String endDate;

    public Promotion(List<String> promotionData) {
        this.promotionName = promotionData.get(PROMOTION_NAME_INDEX);
        this.buyCount = Integer.parseInt(promotionData.get(PROMOTION_BUY_INDEX));
        this.getCount = Integer.parseInt(promotionData.get(PROMOTION_GET_INDEX));
        this.startDate = promotionData.get(PROMOTION_START_DATE_INDEX);
        this.endDate = promotionData.get(PROMOTION_END_DATE_INDEX);
    }

    public String getPromotionName() {
        return promotionName;
    }

    public int getBuyCount() {
        return buyCount;
    }

    public int getGetCount() {
        return getCount;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setBuyCount(int buyCount) {
        this.buyCount = buyCount;
    }

    public void setGetCount(int getCount) {
        this.getCount = getCount;
    }
}
