package transactions;

import java.time.LocalDate;
import java.util.HashMap;

public class Shipment {

    private LocalDate date;
    private String size;
    private String provider;
    private double price;
    private double discount;

    public Shipment(LocalDate date, String size, String provider) {
        this.date = date;
        this.size = size;
        this.provider = provider;
        this.price = getPrice();
        this.discount = 0.0;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getSize() {
        return size;
    }

    public String getProvider() {
        return provider;
    }

    public double getPrice() {
        HashMap<String, Double> prices = new HashMap<>();
        prices.put("LP-S", 1.50);
        prices.put("LP-M", 4.90);
        prices.put("LP-L", 6.90);
        prices.put("MR-S", 2.00);
        prices.put("MR-M", 3.00);
        prices.put("MR-L", 4.00);
        String key = provider + "-" + size;
        return prices.getOrDefault(key, -1.0);
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getReducedPrice() {
        return Math.max(0.0, price - discount);
    }

    @Override
    public String toString() {
        return date.toString() + " " + size + " " + provider + " " + getReducedPrice() + " " + (discount > 0 ? discount : "-");
    }
}
