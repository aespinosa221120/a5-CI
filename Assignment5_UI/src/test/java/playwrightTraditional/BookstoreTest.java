package playwrightTraditional;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;

public class BookstoreTest {

    @Test
    void testBookstorePurchaseFlow() {

        try (Playwright playwright = Playwright.create()) {

            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(false));

            // Fresh context = clear cache/cookies every run (as required by assignment)
            BrowserContext context = browser.newContext(
                    new Browser.NewContextOptions()
                            .setRecordVideoDir(Paths.get("videos/"))
                            .setRecordVideoSize(1280, 720));

            // Clear cookies and cache to ensure TBD tax state
            context.clearCookies();

            Page page = context.newPage();

            page.navigate("https://depaul.bncollege.com/");
            page.waitForTimeout(4000);

            // ---------- TestCase 1: Bookstore ----------

            page.locator("input[name='text']").first().fill("earbuds");
            page.keyboard().press("Enter");
            page.waitForTimeout(4000);

            // Brand filter
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Brand")).first().click();
            page.waitForTimeout(1000);
            page.locator("text=JBL").first().click();
            page.waitForTimeout(2000);

            // Color filter
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Color")).first().click();
            page.waitForTimeout(1000);
            page.locator("text=Black").first().click();
            page.waitForTimeout(2000);

            // Price filter
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Price")).first().click();
            page.waitForTimeout(1000);
            page.locator("text=Over $50").first().click();
            page.waitForTimeout(4000);

            // Click JBL Quantum product
            page.locator("a:has-text('Quantum')").first().click();
            page.waitForTimeout(6000);

            // AssertThat product name, SKU, price, description exist in DOM
            assertTrue(page.locator("h1.name").count() > 0);
            assertTrue(page.locator("[class*='sku']").count() > 0
                    || page.locator("[id*='sku']").count() > 0);
            assertTrue(page.locator("[class*='price']").count() > 0);
            assertTrue(page.locator("[class*='description']").count() > 0
                    || page.locator("[id*='description']").count() > 0);

            // Add to Cart
            page.locator("button:has-text('Add to Cart')").first().click();
            page.waitForTimeout(5000);

            // AssertThat cart icon shows 1 Item
            assertTrue(page.locator("text=1 Item").first().isVisible());

            // Click Cart icon (upper right)
            page.locator("a[href='/cart']").first().click();
            page.waitForTimeout(6000);

            // ---------- TestCase 2: Your Shopping Cart ----------

            // AssertThat on cart page
            assertTrue(page.locator("text=Your Shopping Cart").count() > 0);

            // AssertThat product name, quantity, and price
            assertTrue(page.locator("text=JBL").count() > 0);
            assertTrue(page.locator("input[value='1']").count() > 0);
            assertTrue(page.locator("text=164.98").count() > 0);

            // Select FAST In-Store Pickup
            page.locator("text=FAST In-Store Pickup").first().click();
            page.waitForTimeout(4000);

            // AssertThat sidebar: subtotal, handling, taxes (TBD), estimated total
            assertTrue(page.locator("text=Subtotal").count() > 0);
            assertTrue(page.locator("text=Handling").count() > 0);
            assertTrue(page.locator("text=TBD").count() > 0);
            assertTrue(page.locator("text=164.98").count() > 0);
            assertTrue(page.locator("text=2.00").count() > 0);
            assertTrue(page.locator("text=166.98").count() > 0);

            // Enter promo code TEST and click Apply
            page.locator("input[name='promoCode']").first().click();
            page.keyboard().type("TEST");
            page.waitForTimeout(500);
            page.locator("button:has-text('Apply')").first().click();
            page.waitForTimeout(2000);

            // AssertThat promo code rejection message
            assertTrue(
                page.locator("text=invalid").count() > 0
                || page.locator("text=not valid").count() > 0
                || page.locator("[class*='error']").count() > 0
                || page.locator("[class*='alert']").count() > 0
            );

            // Click PROCEED TO CHECKOUT
            page.locator("button:has-text('PROCEED TO CHECKOUT')").first().click();
            page.waitForTimeout(4000);

            // ---------- TestCase 3: Create Account ----------

            // AssertThat "Create Account" label is present
            assertTrue(page.locator("text=Create Account").count() > 0);

            // Select Proceed as Guest
            page.locator("text=Proceed as Guest").first().click();
            page.waitForTimeout(4000);

            // ---------- TestCase 4: Contact Information ----------

            // AssertThat on Contact Information page
            assertTrue(page.locator("text=Contact Information").count() > 0);

            // First Name
            page.locator("input[name='firstName']").first().click();
            page.waitForTimeout(300);
            page.keyboard().type("Test");
            page.waitForTimeout(300);

            // Last Name
            page.locator("input[name='lastName']").first().click();
            page.waitForTimeout(300);
            page.keyboard().type("User");
            page.waitForTimeout(300);

            // Email - try selectors in order
            if (page.locator("input[id='emailId']").count() > 0) {
                page.locator("input[id='emailId']").first().click();
            } else if (page.locator("input[name='email']").count() > 0) {
                page.locator("input[name='email']").first().click();
            } else {
                page.locator("input[type='email']").first().click();
            }
            page.waitForTimeout(300);
            page.keyboard().type("test@test.com");
            page.waitForTimeout(300);

            // Phone
            page.locator("input[type='tel']").first().click();
            page.waitForTimeout(300);
            page.keyboard().type("3125551111");
            page.waitForTimeout(2000);

            // AssertThat sidebar: subtotal, handling, taxes (TBD), estimated total
            assertTrue(page.locator("text=Subtotal").count() > 0);
            assertTrue(page.locator("text=Handling").count() > 0);
            assertTrue(page.locator("text=TBD").count() > 0);
            assertTrue(page.locator("text=164.98").count() > 0);
            assertTrue(page.locator("text=2.00").count() > 0);
            assertTrue(page.locator("text=166.98").count() > 0);

            // Click CONTINUE
            page.locator("button:has-text('CONTINUE')").first().click();
            page.waitForTimeout(6000);

            // ---------- TestCase 5: Pickup Information ----------

            // AssertThat contact info: name, email, phone
            assertTrue(page.locator("text=Test").count() > 0);
            assertTrue(page.locator("text=test@test.com").count() > 0);
            assertTrue(page.locator("text=3125551111").count() > 0);

            // AssertThat pickup location
            assertTrue(page.locator("text=Pick Up").count() > 0);
            assertTrue(page.locator("text=DePaul University Loop Campus").count() > 0);

            // AssertThat pickup person
            assertTrue(page.locator("text=I'll pick them up").count() > 0);

            // AssertThat sidebar: subtotal, handling, taxes (TBD), estimated total
            assertTrue(page.locator("text=Subtotal").count() > 0);
            assertTrue(page.locator("text=Handling").count() > 0);
            assertTrue(page.locator("text=TBD").count() > 0);
            assertTrue(page.locator("text=164.98").count() > 0);
            assertTrue(page.locator("text=2.00").count() > 0);
            assertTrue(page.locator("text=166.98").count() > 0);

            // AssertThat pickup item
            assertTrue(page.locator("text=JBL").count() > 0);

            // Click CONTINUE
            page.waitForTimeout(2000);
            page.locator("button:has-text('CONTINUE')").first().click();
            page.waitForTimeout(4000);

            // ---------- TestCase 6: Payment Information ----------

            // AssertThat sidebar: subtotal, handling, taxes, total
            assertTrue(page.locator("text=Payment").count() > 0);
            assertTrue(page.locator("text=Subtotal").count() > 0);
            assertTrue(page.locator("text=Handling").count() > 0);
            assertTrue(page.locator("text=164.98").count() > 0);
            assertTrue(page.locator("text=2.00").count() > 0);
            assertTrue(page.locator("text=167.68").count() > 0);

            // AssertThat pickup item
            assertTrue(page.locator("text=JBL").count() > 0);

            // Click BACK TO CART
            page.waitForTimeout(2000);
            page.locator("text=BACK TO CART").first().click();
            page.waitForTimeout(6000);

            // ---------- TestCase 7: Your Shopping Cart - Delete ----------

            // Click Remove via JS (may be styled as icon)
            page.evaluate(
                "() => { " +
                "  const btns = Array.from(document.querySelectorAll('button, a')); " +
                "  const btn = btns.find(b => b.textContent.trim().toUpperCase().includes('REMOVE')); " +
                "  if (btn) btn.click(); " +
                "}"
            );
            page.waitForTimeout(5000);

            // Navigate to cart to confirm empty state
            page.navigate("https://depaul.bncollege.com/cart");
            page.waitForTimeout(5000);

            // AssertThat cart is empty
            String pageText = (String) page.evaluate("() => document.body.innerText");
            assertTrue(
                pageText.toLowerCase().contains("empty")
                || pageText.toLowerCase().contains("no item")
                || pageText.toLowerCase().contains("0 item")
                || page.locator("[class*='empty']").count() > 0
            );

            context.close();
            browser.close();
        }
    }
}