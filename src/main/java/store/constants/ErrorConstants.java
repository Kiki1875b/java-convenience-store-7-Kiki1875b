package store.constants;

public enum ErrorConstants {

    ERROR_MESSAGE_START("[ERROR] "),
    INVALID_INPUT_FORMAT("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    INVALID_PRODUCT_NAME(" 존재하지 않는 상품입니다. 다시 입력해 주세요."),
    INVALID_ETC("잘못된 입력입니다. 다시 입력해 주세요."),
    EXCEEDED_STOCK_QUANTITY("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");

    public final String message;

    ErrorConstants(String message) {
        this.message = message;
    }

    public String getMessage() {
        return ERROR_MESSAGE_START.message + message;
    }
}
