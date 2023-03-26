package service;

import transactions.Shipment;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ShipmentService {

    private HashMap<String, Integer> lPackagesOfLpCount;
    private double accumulatedDiscount;
    private final double MAX_DISCOUNT_FOR_MONTH = 10.00;

    public ShipmentService() {
        lPackagesOfLpCount = new HashMap<>();
        accumulatedDiscount = 0.0;
    }

    public void readInputFile(String path) throws FileNotFoundException {
        File starting = new File(System.getProperty("user.dir"));
        File file = new File(starting, path);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String fileLine = scanner.nextLine();

            Shipment shipment = parseInputLine(fileLine);

            if (shipment == null) {
                System.out.println(fileLine + " Ignored");
                continue;
            }

            double discount = calculateShipmentDiscount(shipment);
            shipment.setDiscount(roundDouble(discount));

            double availableDiscount = Math.min(discount, MAX_DISCOUNT_FOR_MONTH - accumulatedDiscount);
            shipment.setDiscount(roundDouble(availableDiscount));
            accumulatedDiscount += roundDouble(availableDiscount);

            System.out.println(shipment);
        }
    }

    public Shipment parseInputLine(String fileLine) {
        String[] lineFields = fileLine.split(" ");

        if (lineFields.length != 3) {
            return null;
        }

        LocalDate date;
        try {
            date = LocalDate.parse(lineFields[0], DateTimeFormatter.ISO_DATE);
        } catch (Exception e) {
            return null;
        }

        String size = lineFields[1];
        String provider = lineFields[2];

        Shipment shipment = new Shipment(date, size, provider);
        double price = shipment.getPrice();

        if (price == -1.0) {
            return null;
        }
        return shipment;
    }

    public double calculateShipmentDiscount(Shipment shipment) {
        String size = shipment.getSize();
        String provider = shipment.getProvider();
        double price = shipment.getPrice();

        if (size.equals("S")) {
            double lowestPrice = getLowestPriceForSize("S");
            return price - lowestPrice;

        } else if (size.equals("L") && provider.equals("LP")) {

            int count = lPackagesOfLpCount.getOrDefault(getMonthKey(shipment.getDate()), 0);
            if (count < 2) {
                lPackagesOfLpCount.put(getMonthKey(shipment.getDate()), count + 1);
            } else if (count == 2) {
                lPackagesOfLpCount.put(getMonthKey(shipment.getDate()), count + 1);
                return price;
            }
        }
        return 0.0;
    }

    public double roundDouble(double value) {
        return (double) (Math.round(value * 100)) / 100.0;
    }

    public double getLowestPriceForSize(String size) {
        double lowestPrice = Double.MAX_VALUE;
        for (String provider : Arrays.asList("LP", "MR")) {
            double price = new Shipment(null, size, provider).getPrice();
            if (price < lowestPrice) {
                lowestPrice = price;
            }
        }
        return lowestPrice;
    }

    public String getMonthKey(LocalDate date) {
        int year = date.getYear();
        int month = date.getMonthValue();
        return year + "-" + month;
    }

}
