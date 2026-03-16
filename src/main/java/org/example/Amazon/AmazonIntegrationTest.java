package org.example.Amazon;

import org.example.Amazon.Cost.*;
import org.junit.jupiter.api.*;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AmazonIntegrationTest {

    private Database database;
    private ShoppingCartAdaptor cartAdaptor;
    private List<PriceRule> rules;

    @BeforeEach
    void setup() {
        database = new Database();
        database.resetDatabase(); // Reset DB before each test
        cartAdaptor = new ShoppingCartAdaptor(database);

        rules = List.of(
                new RegularCost(),
                new DeliveryPrice(),
                new ExtraCostForElectronics()
        );
    }

    @Test
    @DisplayName("specification-based: calculate total price for multiple items")
    void testTotalPriceCalculation() {
        Amazon amazon = new Amazon(cartAdaptor, rules);

        cartAdaptor.add(new Item(ItemType.ELECTRONIC, "Laptop", 1, 1000));
        cartAdaptor.add(new Item(ItemType.OTHER, "Book", 2, 20));

        double total = amazon.calculate();

        // RegularCost = 1000 + 40 = 1040
        // DeliveryPrice = 5 (3 items)
        // ExtraCostForElectronics = 7.5
        Assertions.assertEquals(1052.5, total);
    }

    @Test
    @DisplayName("structural-based: database is cleared correctly")
    void testDatabaseReset() {
        cartAdaptor.add(new Item(ItemType.OTHER, "Pen", 3, 2.0));
        database.resetDatabase();
        Assertions.assertEquals(0, cartAdaptor.getItems().size());
    }
}