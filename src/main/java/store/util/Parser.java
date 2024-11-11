package store.util;

import store.product.Product;
import store.product.ProductRepository;

import java.text.NumberFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static store.constants.UtilContants.SEPARATOR;
import static store.constants.UtilContants.PRODUCT_NAME_INDEX;
import static store.constants.UtilContants.NULL;
import static store.constants.UtilContants.NOT_IN_STOCK;
import static store.constants.UtilContants.EMPTY;
import static store.constants.UtilContants.PRICE_INDEX;
import static store.constants.UtilContants.MONEY_UNIT;
import static store.constants.UtilContants.ORDINARY_COUNT_INDEX;
import static store.constants.UtilContants.PROMOTION_TYPE_INDEX;
import static store.constants.UtilContants.ONE;
import static store.constants.UtilContants.ZERO;
import static store.constants.UtilContants.UNIT;
import static store.constants.UtilContants.SEPARATOR_DASH;
import static store.constants.UtilContants.REGEX;

public class Parser {

    static public List<String> parseSingleProduct(String product) {
        return new ArrayList<>(List.of(product.split(SEPARATOR)));
    }


    static public String parseMoney(int number) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        return numberFormat.format(number);
    }

    static public List<String> parseToOutput(ProductRepository productRepository) {
        List<String> parsedProducts = new ArrayList<>();
        for (Product product : productRepository.getProducts().values()) {
            String name = product.getName();
            String price = parseMoney(product.getPrice());
            String ordinaryCount = getCount(product.getOrdinaryCount());
            String promotionCount = getCount(product.getPromotionCount());
            String type = product.getType();
            if (!type.equals(NULL)) {
                String total = String.join(" ", name, price, promotionCount, type);
                parsedProducts.add(total);
            }
            String total = String.join(" ", name, price, ordinaryCount);
            parsedProducts.add(total);
        }
        return parsedProducts;
    }

    static private String getCount(int count) {
        if (count == 0) {
            return NOT_IN_STOCK;
        }
        return parseMoney(count) + UNIT;
    }

    private static boolean isNumeric(String str) {
        return str != null && str.matches("\\d+");
    }
    static public List<String> parseToOutput(List<String> products) {
        List<String> parsedProducts = new ArrayList<>();

        for (String product : products) {
            if (!isNumeric(product.split(SEPARATOR)[1])) {
                continue;
            }
            List<String> tokens = List.of(product.split(SEPARATOR));
            int count = countElement(products, tokens.getFirst());
            String parsed = parseSingleLine(tokens);
            String parsedHidden = parseHiddenSingleLine(tokens, count);
            parsedProducts.add(parsed);
            if (!parsedHidden.equals(EMPTY)) {
                parsedProducts.add(parsedHidden);
            }
        }
        return parsedProducts;
    }

    private static int countElement(List<String> products, String name) {
        return (int) products.stream()
            .filter(product -> product.contains(name))
            .count();
    }

    static private String parseSingleLine(List<String> tokens) {
        String name = tokens.get(PRODUCT_NAME_INDEX);
        String price = parseMoney(Integer.parseInt(tokens.get(PRICE_INDEX))) + MONEY_UNIT;
        String quantity = ifQuantityIsZero(Integer.parseInt(tokens.get(ORDINARY_COUNT_INDEX)));
        String promotion = ifTypeIsNull(tokens.get(PROMOTION_TYPE_INDEX));
        return String.join(" ", name, price, quantity, promotion);
    }

    static private String parseHiddenSingleLine(List<String> tokens, int count) {
        if (count != ONE || tokens.get(PROMOTION_TYPE_INDEX).equals(NULL)) {
            return EMPTY;
        }
        String name = tokens.get(PRODUCT_NAME_INDEX);
        String price = parseMoney(Integer.parseInt(tokens.get(PRICE_INDEX))) + MONEY_UNIT;
        String quantity = NOT_IN_STOCK;
        return String.join(" ", name, price, quantity);
    }

    static private String ifQuantityIsZero(int quantity) {
        if (quantity == ZERO) {
            return NOT_IN_STOCK;
        }
        return quantity + UNIT;
    }

    static private String ifTypeIsNull(String type) {
        if (type.equals(NULL)) {
            return "";
        }
        return type;
    }

    static public HashMap<String, Integer> parseUserInput(String input) {
        HashMap<String, Integer> returnMap = new LinkedHashMap<>();
        List<String> items = splitInput(input, SEPARATOR);

        for (String item : items) {
            item = replaceBracket(item);
            List<String> parts = splitInput(item, SEPARATOR_DASH);
            if(!returnMap.containsKey(parts.getFirst())) {
                returnMap.put(parts.getFirst(), Integer.parseInt(parts.getLast()));
            }else if(returnMap.containsKey(parts.getFirst())){
                int num = returnMap.get(parts.getFirst());
                returnMap.put(parts.getFirst(),  num + Integer.parseInt(parts.getLast()));
            }
        }
        return returnMap;
    }

    static public List<String> splitInput(String input, String separator) {
        return List.of(input.split(separator));
    }

    static public String replaceBracket(String item) {
        return item.replaceAll(REGEX, EMPTY);
    }
}
