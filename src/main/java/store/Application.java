package store;

import store.product.ProductRepository;
import store.promotion.PromotionRepository;


public class Application { 
    public static void main(String[] args) {
        ProductRepository productRepository = new ProductRepository();
        PromotionRepository promotionRepository = new PromotionRepository();
        StoreController storeController = new StoreController(productRepository, promotionRepository);
        storeController.run();
    }
}

