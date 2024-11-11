package store.constants;

public enum ViewConstants {

    START_MESSAGE("안녕하세요. W편의점입니다."),
    START_SECOND_MESSAGE("현재 보유하고 있는 상품입니다."),
    REQUEST_INPUT_MESSAGE("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])"),
    ASK_MEMBERSHIP_DISCOUNT("멤버십 할인을 받으시겠습니까? (Y/N)"),
    ASK_REPURCHASE("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)"),
    ASK_PROMOTION_NOT_APPLIES("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)"),
    ASK_PROMOTION_ADD("현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)"),
    RESULT_START("==============W 편의점================"),
    RESULT_FIRST_PART("상품명      수량  금액"),
    RESULT_PRODUCT_COUNT_PRICE("%s      %s  %s"),
    RESULT_SECOND("=============증   정==============="),
    RESULT_SECOND_SPECIFIC("%s      %s"),
    RESULT_THIRD("===================================="),
    RESULT_TOTAL("총구매액      %s  %s"),
    RESULT_PROMOTION_DISCOUNT("행사할인         -%s"),
    RESULT_MEMBERSHIP_DISCOUNT("멤버십할인           -%s"),
    RESULT_MONEY_TO_PAY("내실돈             %s"),
    TAB("\t");
    private final String message;

    ViewConstants(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }

}
