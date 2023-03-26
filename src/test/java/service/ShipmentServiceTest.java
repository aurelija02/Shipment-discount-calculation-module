package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import transactions.Shipment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShipmentServiceTest {

    @Mock
    Shipment mockedShipment;

    @InjectMocks
    private ShipmentService shipmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void parseInputLine_shouldReturnShipment_withValidInput() {
        String fileLine = "2022-03-26 S LP";

        Shipment result = shipmentService.parseInputLine(fileLine);

        LocalDate expectedDate = LocalDate.parse("2022-03-26", DateTimeFormatter.ISO_DATE);
        assertEquals(expectedDate, result.getDate());
        assertEquals("S", result.getSize());
        assertEquals("LP", result.getProvider());
        assertEquals(1.50, result.getPrice());

    }
    @Test
    void parseInputLine_shouldReturnNull_withExtraField() {
        String fileLine = "2022-03-26 MM LP Extra";
        Shipment result = shipmentService.parseInputLine(fileLine);
        assertNull(result);
    }
    @Test
    void parseInputLine_shouldReturnNull_withInvalidSizeInput() {
        String fileLine = "2022-03-26 MM LP";
        Shipment result = shipmentService.parseInputLine(fileLine);
        assertNull(result);
    }
    @Test
    void parseInputLine_shouldReturnNull_withInvalidProviderInput() {
        String fileLine = "2022-03-26 M KK";
        Shipment result = shipmentService.parseInputLine(fileLine);
        assertNull(result);
    }
    @Test
    void parseInputLine_shouldReturnNull_withInvalidInput() {
        String inputLine = "2023-04-01 S";
        Shipment resultShipment = shipmentService.parseInputLine(inputLine);
        assertNull(resultShipment);
    }
    @Test
    void getMonthKey_shouldReturnKey_withValidDate(){
        LocalDate date = LocalDate.of(2022, 4, 3);
        String result = shipmentService.getMonthKey(date);

        assertEquals("2022-4", result);
    }
    @Test
    void getLowestPriceForSize_shouldReturnLowestPrice(){
        when(mockedShipment.getPrice()).thenReturn(3.0);

        double result = shipmentService.getLowestPriceForSize("M");
        assertEquals(3.0, result, 0.001);
    }
    @Test
    void roundDouble_shouldReturnFormattedResult(){
        Double result = shipmentService.roundDouble(2.55555);
        assertEquals(2.56, result);
    }
    @Test
    void calculateShipmentDiscount_shouldReturnDiscount_whenSizeS(){
        Shipment shipment = new Shipment(null, "S", "MR");

        double result = shipmentService.calculateShipmentDiscount(shipment);
        assertEquals(0.5, result);
    }
    @Test
    void calculateShipmentDiscount_shouldReturnDiscount_whenSizeLAndProviderLp_andNot3TransactionInMonth(){
        LocalDate date = LocalDate.of(2015, 3, 26);
        String size = "L";
        String provider = "LP";
        Shipment shipment = new Shipment(date, size, provider);

        double expectedDiscount = 0.0;
        double actualDiscount = shipmentService.calculateShipmentDiscount(shipment);
        assertEquals(expectedDiscount, actualDiscount);
    }

}