package id.ac.ui.cs.advprog.eshop.enums;

public enum PaymentMethod {
    CASH_ON_DELIVERY("Cash on Delivery"),
    VOUCHER("Voucher"),
    BANK_TRANSFER("Bank Transfer");

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean contains(String method) {
        for (PaymentMethod m : values()) {
            if (m.value.equals(method)) {
                return true;
            }
        }
        return false;
    }
}