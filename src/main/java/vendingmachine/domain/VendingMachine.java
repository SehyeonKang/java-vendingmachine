package vendingmachine.domain;

import vendingmachine.dto.SellInfoDto;
import vendingmachine.util.Validator;

import java.util.SortedMap;

public class VendingMachine {

    private final static boolean CHANGE_NEED_FLAG = true;
    private final static boolean CHANGE_NO_NEED_FLAG = false;

    private final CoinBox coinBox;
    private final ProductBox productBox;
    private PaymentManager paymentManager;

    public VendingMachine() {
        coinBox = new CoinBox();
        productBox = new ProductBox();
        paymentManager = new PaymentManager();
    }

    public SortedMap<Integer, Integer> generateCoins(int totalAmount) {
        Validator.validateAmount(totalAmount);
        return coinBox.generateCoins(totalAmount);
    }

    public void addProducts(String productsString) {
        productBox.addProducts(productsString);
    }

    public void addInputAmount(int amount) {
        paymentManager.addInputAmount(amount);
    }

    public SellInfoDto sellProduct(String productName) {
        SellInfoDto sellInfoDto;
        Product product = productBox.findProduct(productName);
        int productPrice = productBox.dispenseProduct(product);
        int inputAmount = paymentManager.reduceInputAmount(productPrice);
        sellInfoDto = new SellInfoDto(inputAmount, CHANGE_NO_NEED_FLAG);

        if (inputAmount < productBox.findMinimumPrice() || productBox.checkSoldOut()) {
            sellInfoDto.setChangeNeedFlag(CHANGE_NEED_FLAG);
        }

        return sellInfoDto;
    }

    public int getInputAmount() {
        return paymentManager.getInputAmount();
    }

    public SortedMap<Integer, Integer> generateChange() {
        return paymentManager.generateChange(coinBox);
    }
}
