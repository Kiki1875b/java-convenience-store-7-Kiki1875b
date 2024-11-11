package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.constants.ViewConstants;
import java.util.List;

import static store.constants.UtilContants.END_LINE_SINGLE;
import static store.constants.ViewConstants.START_MESSAGE;
import static store.constants.ViewConstants.REQUEST_INPUT_MESSAGE;
import static store.constants.ViewConstants.ASK_MEMBERSHIP_DISCOUNT;
import static store.constants.ViewConstants.ASK_REPURCHASE;
import static store.constants.ViewConstants.ASK_PROMOTION_NOT_APPLIES;

public class InputView {

    public void printStart() {
        System.out.print(START_MESSAGE.getMessage());
        printEndLine();
    }

    public void printStock(List<String> stocks) {
        for (String stock : stocks) {
            System.out.print("- " + stock);
            printEndLine();
        }
    }

    public void printEndLine(){
        System.out.print(END_LINE_SINGLE);
    }

    public String printRequestInput() {
        System.out.print(REQUEST_INPUT_MESSAGE.getMessage());
        printEndLine();
        return Console.readLine();
    }

    public String printRequestMembership() {
        System.out.print(END_LINE_SINGLE + ASK_MEMBERSHIP_DISCOUNT.getMessage());
        printEndLine();
        return Console.readLine();
    }

    public String printAskRepurchase() {
        System.out.print(END_LINE_SINGLE + ASK_REPURCHASE.getMessage());
        printEndLine();
        return Console.readLine();
    }

    public String printAskPromotionNotApplies(String productName, int count) {
        System.out.print(END_LINE_SINGLE + ASK_PROMOTION_NOT_APPLIES.format(productName, count));
        printEndLine();
        return Console.readLine();
    }

    public String printAskPromotionAdd(String productName, int count) {
        String message = ViewConstants.ASK_PROMOTION_ADD.format(productName, count);
        System.out.print(END_LINE_SINGLE + message);
        printEndLine();
        return Console.readLine();
    }


}
