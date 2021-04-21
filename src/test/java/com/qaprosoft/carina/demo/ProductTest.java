package com.qaprosoft.carina.demo;

import java.lang.invoke.MethodHandles;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.qaprosoft.carina.core.foundation.AbstractTest;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.utils.ownership.MethodOwner;
import com.qaprosoft.carina.demo.gui.components.ProductItem;
import com.qaprosoft.carina.demo.gui.pages.AddressPage;
import com.qaprosoft.carina.demo.gui.pages.ConfirmedOrderPage;
import com.qaprosoft.carina.demo.gui.pages.HomePage;
import com.qaprosoft.carina.demo.gui.pages.LoginPage;
import com.qaprosoft.carina.demo.gui.pages.OrderPage;
import com.qaprosoft.carina.demo.gui.pages.OrderSummaryPage;
import com.qaprosoft.carina.demo.gui.pages.PaymentPage;
import com.qaprosoft.carina.demo.gui.pages.ProductPage;
import com.qaprosoft.carina.demo.gui.pages.ShippingPage;

public class ProductTest extends AbstractTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@Test
    @MethodOwner(owner = "Tsekhanovich")
    public void testProduct() throws InterruptedException {
		HomePage homePage = new HomePage(getDriver());
		homePage.open();
		Assert.assertTrue(homePage.isPageOpened(), "Base page is not opened!");
		
		List<ProductItem> products = homePage.getProducts();
		Assert.assertFalse(CollectionUtils.isEmpty(products), "No products found!");
		
		int rand = (int)(Math.random() * products.size());
		ProductPage productPage = products.get(rand).openProductPage();
		Assert.assertTrue(productPage.isPageOpened(), "Product page is not opened!");
		
		productPage.selectRandomColor();
		productPage.selectRandomSize();
		productPage.addProduct();
		Assert.assertTrue(productPage.getOkIcon().isVisible(), "Product has not been added");
		
		OrderPage orderPage = productPage.clickProceedButton();
		Assert.assertTrue(orderPage.isPageOpened(), "Order page is not opened!");
		
		LoginPage loginPage = orderPage.clickProceedButton();
		Assert.assertTrue(loginPage.isPageOpened(), "Login page is not opened!");
		
		loginPage.getLoginItem().typeEmail(R.TESTDATA.get("test_login_value"));
        loginPage.getLoginItem().typePassword(R.TESTDATA.get("test_password_value"));
        AddressPage addressPage = loginPage.getLoginItem().confirmOrderLogin();
        Assert.assertTrue(addressPage.isPageOpened(), "Address page is not opened!");
        
        ShippingPage shippingPage = addressPage.clickProceedButton();
        Assert.assertTrue(shippingPage.isPageOpened(), "Shipping page is not opened!");
        
        PaymentPage paymentPage = shippingPage.clickProceedButton();
        Assert.assertTrue(paymentPage.isPageOpened(), "Payment page is not opened!");
        
        OrderSummaryPage orderSummaryPage = paymentPage.clickWireButton();
        Assert.assertTrue(orderSummaryPage.isPageOpened(), "Order summary page is not opened!");
        
        ConfirmedOrderPage confirmedOrderPage = orderSummaryPage.clickWireButton();
        Assert.assertTrue(confirmedOrderPage.isPageOpened(), "Order summary page is not opened!");
        Assert.assertEquals(confirmedOrderPage.getInfoMessage().getText(), R.TESTDATA.get("test_success_order_message"));
        
        
        LOGGER.info("Product has been purchased!");
    }
}
