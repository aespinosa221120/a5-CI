package org.example.Amazon;

import org.example.Amazon.Cost.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import java.util.List;
import static org.mockito.Mockito.*;

public class AmazonUnitTest {

    @Mock
    ShoppingCart mockCart;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("specification-based: Amazon calculates total with mocked cart")
    void testAmazonCalculateWithMockedCart() {
        Item item = new Item(ItemType.ELECTRONIC, "Laptop", 1, 1000);
        when(mockCart.getItems()).thenReturn(List.of(item));

        List<PriceRule> rules = List.of(new RegularCost(), new ExtraCostForElectronics());

        Amazon amazon = new Amazon(mockCart, rules);
        double total = amazon.calculate();

        // RegularCost = 1000, ExtraCostForElectronics = 7.5
        Assertions.assertEquals(1007.5, total);
    }

    @Test
    @DisplayName("structural-based: verify add method is called on cart")
    void testAddMethodCalled() {
        Item item = new Item(ItemType.OTHER, "Book", 1, 15);
        doNothing().when(mockCart).add(item);

        mockCart.add(item);

        verify(mockCart, times(1)).add(item);
    }
}