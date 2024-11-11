package store.view;


import java.util.List;

import static store.constants.UtilContants.*;
import static store.constants.ViewConstants.RESULT_START;
import static store.constants.ViewConstants.RESULT_FIRST_PART;
import static store.constants.ViewConstants.RESULT_PRODUCT_COUNT_PRICE;
import static store.constants.ViewConstants.RESULT_SECOND;
import static store.constants.ViewConstants.RESULT_SECOND_SPECIFIC;
import static store.constants.ViewConstants.RESULT_THIRD;
import static store.constants.ViewConstants.RESULT_TOTAL;
import static store.constants.ViewConstants.RESULT_PROMOTION_DISCOUNT;
import static store.constants.ViewConstants.RESULT_MEMBERSHIP_DISCOUNT;
import static store.constants.ViewConstants.RESULT_MONEY_TO_PAY;


public class OutputView {
    public static void printError(String errorMessage) {
        System.out.println(errorMessage);
    }

    public static void printResultFirstPart() {
        System.out.println(END_LINE_SINGLE + RESULT_START.getMessage());
        System.out.println(RESULT_FIRST_PART.getMessage());
    }

    public static void printResultFirstPartSpecific(List<String> singleResult) {

        String price = singleResult.get(FIRST);
        String count = singleResult.get(SECOND);
        if(count.equals(String.valueOf(ZERO))){
            return;
        }
        String money = singleResult.get(THIRD);
        System.out.println(RESULT_PRODUCT_COUNT_PRICE.format(price, count, money));
    }

    public static void printSecondPart() {
        System.out.println(RESULT_SECOND.getMessage());
    }

    public static void printSecondPartSpecific(String item, String count) {
        System.out.println(RESULT_SECOND_SPECIFIC.format(item, count));
    }

    public static void printThirdPart() {
        System.out.println(RESULT_THIRD.getMessage());
    }

    public static void printThirdPartSpecific(String count, String total, String promotion, String membership, String toPay) {
        System.out.println(RESULT_TOTAL.format(count, total));
        System.out.println(RESULT_PROMOTION_DISCOUNT.format(promotion));
        System.out.println(RESULT_MEMBERSHIP_DISCOUNT.format(membership));
        System.out.println(RESULT_MONEY_TO_PAY.format(toPay));

    }
}
