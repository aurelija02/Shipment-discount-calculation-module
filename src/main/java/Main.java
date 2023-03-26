import service.ShipmentService;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) {

        ShipmentService shipmentService = new ShipmentService();

//  If we are trying to run Main class on Intellij IDEA,
//  we should change the path in readInputFile to this: "/src/main/java/input.txt"

        try {
            shipmentService.readInputFile("/input.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("file does not exist");
        }

    }
}
