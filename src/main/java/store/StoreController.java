package store;

import store.product.Product;
import store.product.ProductRepository;
import store.promotion.Promotion;
import store.promotion.PromotionRepository;
import store.result.Result;
import store.util.FileReader;
import store.util.Parser;
import store.validator.InputValidator;
import store.view.InputView;
import store.view.OutputView;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.List;

import static store.constants.UtilContants.*;

public class StoreController {

    private ProductRepository productRepository;
    private PromotionRepository promotionRepository;
    private FileReader fileReader = new FileReader();
    private InputView inputView = new InputView();
    private HashMap<String, Integer> userOrder = new LinkedHashMap<>();
    private HashSet<String> lackProduct = new HashSet<>();
    private Result result;
    private boolean isFirst = true;

    public StoreController(ProductRepository productRepository, PromotionRepository promotionRepository) {
        this.productRepository = productRepository;
        this.promotionRepository = promotionRepository;
        result = new Result(productRepository);

    }

    public void run() {
        ifIsFirst();
        requestUserInputAndValidate();
        iterateMap();
        printMembershipDiscount();
        result.calculateResult();
        printResultFirstPart();
        printResultSecondPart();
        printResultThirdPart();
        printAskRepurchase();

    }

    private void ifIsFirst() {
        if (isFirst) {

            printStartAndStock();
            return;
        }

        List<String> parsedProducts = Parser.parseToOutput(productRepository);
        inputView.printStart();
        inputView.printStock(parsedProducts);
        inputView.printEndLine();
    }

    private void printAskRepurchase() {
        if(!isAllEmpty()) {
            boolean validInput = false;
            while (!validInput) {
                try {
                    String userInput = inputView.printAskRepurchase();
                    inputView.printEndLine();
                    InputValidator.validateSimpleAnswer(userInput);
                    handleRepurchase(userInput);
                    validInput = true;
                } catch (IllegalArgumentException e) {
                    OutputView.printError(e.getMessage());
                }
            }
        }
    }

    private void handleRepurchase(String userInput) {
        if (userInput.equals(NO)) {
            return;
        }
        isFirst = false;

        restart();

    }

    private boolean isAllEmpty(){
        for(String name : productRepository.getProducts().keySet()){
            Product product = productRepository.getItemByKey(name);
            if(product.getOrdinaryCount() > 0 || product.getPromotionCount() > 0){
                return false;
            }
        }

        return true;
    }

    private void restart() {
        result.Reset();
        run();
    }

    private void printResultThirdPart() {
        OutputView.printThirdPart();
        OutputView.printThirdPartSpecific(
            String.valueOf(result.getTotalCount()),
            Parser.parseMoney(result.getTotal()),
            Parser.parseMoney(result.getPromotionDiscount()),
            Parser.parseMoney(result.getMembershipDiscount()),
            Parser.parseMoney(result.getMoneyToPay())
        );
    }

    private void printResultSecondPart() {
        OutputView.printSecondPart();
        for (String key : result.getPromotions().keySet()) {
            OutputView.printSecondPartSpecific(key, String.valueOf(result.getPromotions().get(key)));
        }
    }

    private void printResultFirstPart() {
        OutputView.printResultFirstPart();
        for (String key : result.getBoughtProducts().keySet()) {
            List<String> parsedBoughtProduct = parseBoughtProducts(key, result.getBoughtProducts().get(key));
            OutputView.printResultFirstPartSpecific(parsedBoughtProduct);
        }
    }

    private List<String> parseBoughtProducts(String key, int count) {
        String price = Parser.parseMoney(productRepository.getItemByKey(key).getPrice() * count);
        return List.of(key, String.valueOf(count), price);

    }

    private void printMembershipDiscount() {
        boolean validInput = false;
        while (!validInput) {
            try {
                String membershipRequest = inputView.printRequestMembership();
                InputValidator.validateSimpleAnswer(membershipRequest);
                if (membershipRequest.equals(YES)) {
                    result.setMembershipDiscount();
                }
                validInput = true;
            } catch (IllegalArgumentException e) {
                OutputView.printError(e.getMessage());
            }
        }
    }

    private List<String> readPromotions() {
        return fileReader.read(PATH_TO_PROMOTION);
    }

    private List<String> readProducts() {
        return fileReader.read(PATH_TO_PRODUCT);
    }

    private void printStartAndStock() {
        List<String> products = readProducts();
        initProductRepository(products);
        initPromotionRepository();
        List<String> parsedProducts = Parser.parseToOutput(productRepository);
        inputView.printStart();
        inputView.printStock(parsedProducts);
        inputView.printEndLine();
    }

    private void initPromotionRepository() {
        List<String> promotions = readPromotions();

        for (String promotion : promotions) {
            if (!isNumeric(promotion.split(SEPARATOR)[1])) {
                continue;
            }
            Promotion promotion1 = new Promotion(List.of(promotion.split(SEPARATOR)));
            promotionRepository.addPromotion(promotion1, productRepository);
        }
    }
    private static boolean isNumeric(String str) {
        return str != null && str.matches("\\d+");
    }

    private void initProductRepository(List<String> products) {
        for (String product : products) {
            if (product.startsWith("name")) {
                continue;
            }
            productRepository.addItem(List.of(product.split(SEPARATOR)));
        }
    }

    private void requestUserInputAndValidate() {
        boolean validInput = false;
        while (!validInput) {
            try {
                String userInput = inputView.printRequestInput();
                validate(userInput);
                validInput = true;
            } catch (IllegalArgumentException e) {
                OutputView.printError(e.getMessage());
            }
        }
    }

    private void validate(String userInput) {
        InputValidator.validateInput(userInput);
        userOrder = Parser.parseUserInput(userInput);
        for (String key : userOrder.keySet()) {
            productRepository.validateProductExists(key);
            checkStock(key);
        }
    }

    private void checkStock(String key) {
        Product product = productRepository.getItemByKey(key);
        int orderAmount = userOrder.get(key);
        product.checkIfEnoughInStock(orderAmount);
    }

    private void iterateMap() {
        for (String key : userOrder.keySet()) {
            checkForAddition(key);
            checkIfLacksPromotionQuantity(key);
        }
    }

    private void checkIfLacksPromotionQuantity(String key) {
        Product product = productRepository.getItemByKey(key);
        if (!promotionRepository.promotionExists(product.getType())) {
            return;
        }

        if(product.getPromotionCount() == 0){
            return;
        }

        Promotion promotion = promotionRepository.getPromotionByKey(product.getType());
        int numberOfProductsUnableToGetPromotion = product.unableToGetPromotion(userOrder.get(key), promotion.getBuyCount(), promotion.getGetCount());
        if (numberOfProductsUnableToGetPromotion == ZERO) {
            result.updatePromotions(key, userOrder.get(key) / (promotion.getBuyCount() + promotion.getGetCount()) * promotion.getGetCount());
            return;
        }
        getLacksPromotionReply(key, numberOfProductsUnableToGetPromotion);
    }

    private void getLacksPromotionReply(String key, int number) {
        boolean validInput = false;
        while (!validInput) {
            try {
                String userReply = inputView.printAskPromotionNotApplies(key, number);
                InputValidator.validateSimpleAnswer(userReply);
                processLacksPromotionReply(userReply, key, number);
                validInput = true;
            } catch (IllegalArgumentException e) {
                OutputView.printError(e.getMessage());
            }
        }
    }

    private void processLacksPromotionReply(String userReply, String key, int number) {
        if (userReply.equals(NO)) {
            Promotion promotion = promotionRepository.getPromotionByKey(productRepository.getItemByKey(key).getType());
            userOrder.put(key, userOrder.get(key) - number);
            result.decreaseBoughtProducts(key, number);
            result.updatePromotions(key, (userOrder.get(key) / (promotion.getBuyCount() + promotion.getGetCount()) * promotion.getGetCount()));
            if(result.getBoughtProductSize() == ZERO){
                printAskRepurchase();
            }
            return;
        }
        Promotion promotion = promotionRepository.getPromotionByKey(productRepository.getItemByKey(key).getType());
        lackProduct.add(key);
        result.updateNonPromotionTotal(key, number);
        result.updatePromotions(key, (userOrder.get(key) - number) / (promotion.getBuyCount() + promotion.getGetCount()) * promotion.getGetCount());
    }

    private void checkForAddition(String key) {
        Product product = productRepository.getItemByKey(key);
        if (!promotionRepository.promotionExists(product.getType())) {
            result.updateBoughtProducts(key, userOrder.get(key));
            return;
        }

        Promotion promotion = promotionRepository.getPromotionByKey(product.getType());

        if (product.userHasToBringMore(userOrder.get(key), promotion.getBuyCount(), promotion.getGetCount())) {
            getAdditionReply(key, promotion.getGetCount());
            return;
        }
        result.updateBoughtProducts(key, userOrder.get(key));
    }

    private void getAdditionReply(String key, int promotionGet) {
        String productName = key;
        boolean validInput = false;
        while (!validInput) {
            try {
                String userReply = inputView.printAskPromotionAdd(productName, promotionGet);
                InputValidator.validateSimpleAnswer(userReply);
                processAdditionReply(userReply, key, promotionGet);
                validInput = true;
            } catch (IllegalArgumentException e) {
                OutputView.printError(e.getMessage());
            }
        }
    }

    private void processAdditionReply(String userReply, String key, int count) {
        if (userReply.equals(YES)) {
            userOrder.put(key, userOrder.get(key) + count);
            result.updateBoughtProducts(key, userOrder.get(key));
            return;
        }
        result.updateBoughtProducts(key, userOrder.get(key));
    }
}
