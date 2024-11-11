package store.product;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static store.constants.ErrorConstants.INVALID_PRODUCT_NAME;
import static store.constants.UtilContants.PRODUCT_NAME_INDEX;

public class ProductRepository {

    private HashMap<String, Product> products = new LinkedHashMap<>();

    public void addItem(List<String> product) {
        if (!checkIfExists(product.get(PRODUCT_NAME_INDEX))) {
            Product newProduct = new Product(product);
            products.put(product.get(PRODUCT_NAME_INDEX), newProduct);
            return;
        }
        updateItem(product);
    }

    public HashMap<String, Product> getProducts() {
        return products;
    }

    public Product getItemByKey(String key) {
        return products.get(key);
    }

    public void validateProductExists(String key) {
        if (!products.containsKey(key)) {
            throw new IllegalArgumentException(INVALID_PRODUCT_NAME.getMessage());
        }
    }

    private boolean checkIfExists(String key) {
        return products.containsKey(key);
    }

    private void updateItem(List<String> product) {
        Product productToUpdate = getItemByKey(product.get(PRODUCT_NAME_INDEX));
        productToUpdate.setOrdinaryCount(product);
        productToUpdate.setPromotionCount(product);
        products.put(productToUpdate.getName(), productToUpdate);
    }

    public void updateItem(Product product, String name) {
        products.put(name, product);
    }
}
