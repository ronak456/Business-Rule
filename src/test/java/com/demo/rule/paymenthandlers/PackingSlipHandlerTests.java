package com.demo.rule.paymenthandlers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.junit.Test;

import com.demo.rule.domain.Customer;
import com.demo.rule.domain.LineItem;
import com.demo.rule.domain.Order;
import com.demo.rule.domain.Payment;
import com.demo.rule.domain.ProductCategory;
import com.demo.rule.paymenthandlers.PackingSlipHandler;
import com.demo.rule.paymenthandlers.PaymentHandler;
import com.demo.rule.services.PackingSlipService;
import com.demo.rule.services.RoyaltyService;
import com.demo.rule.services.ShippingService;

public class PackingSlipHandlerTests{
    public PackingSlipHandlerTests(){}

    @Test
    public void runShouldNotGenerateForShippingWhenNoValidItemsAvailable() throws Exception {
        LineItem[] lineItems = new LineItem[]{
            new LineItem("item1", "item1", new ProductCategory[]{
                ProductCategory.Membership
            }),
            new LineItem("item2", "item2", new ProductCategory[]{
                ProductCategory.Virtual,
            })
        };
        Customer customer = mock(Customer.class);
        Order order = new Order(customer, lineItems, null);
        Payment payment = new Payment(order);

        PackingSlipService packingSlipService = mock(PackingSlipService.class);
        ShippingService shippingService = mock(ShippingService.class);
        RoyaltyService royaltyService = mock(RoyaltyService.class);
        
        PaymentHandler sut = new PackingSlipHandler(shippingService, royaltyService, packingSlipService);
        sut.run(payment);

        verify(shippingService, never()).generatePackingSlip(order);
    }

    @Test
    public void runShouldGenerateForShippingWhenPhysicalAvailable() throws Exception {
        LineItem[] lineItems = new LineItem[]{
            new LineItem("item1", "item1", new ProductCategory[]{
                ProductCategory.Physical
            }),
            new LineItem("item2", "item2", new ProductCategory[]{
                ProductCategory.Membership,
            })
        };
        Customer customer = mock(Customer.class);
        Order order = new Order(customer, lineItems, null);
        Payment payment = new Payment(order);

        PackingSlipService packingSlipService = mock(PackingSlipService.class);
        ShippingService shippingService = mock(ShippingService.class);
        RoyaltyService royaltyService = mock(RoyaltyService.class);
        
        PaymentHandler sut = new PackingSlipHandler(shippingService, royaltyService, packingSlipService);
        sut.run(payment);

        verify(shippingService, times(1)).generatePackingSlip(order);
    }

    @Test
    public void runShouldGenerateForRoyaltyWhenBooksAvailable() throws Exception {
        LineItem[] lineItems = new LineItem[]{
            new LineItem("item1", "item1", new ProductCategory[]{
                ProductCategory.Books
            }),
            new LineItem("item2", "item2", new ProductCategory[]{
                ProductCategory.Membership,
            })
        };
        Customer customer = mock(Customer.class);
        Order order = new Order(customer, lineItems, null);
        Payment payment = new Payment(order);

        PackingSlipService packingSlipService = mock(PackingSlipService.class);
        ShippingService shippingService = mock(ShippingService.class);
        RoyaltyService royaltyService = mock(RoyaltyService.class);
        
        PaymentHandler sut = new PackingSlipHandler(shippingService, royaltyService, packingSlipService);
        sut.run(payment);

        verify(royaltyService, times(1)).generatePackingSlip(order);
    }

    @Test
    public void runShouldAddFirstAidGiftWhenRequested() throws Exception {
        LineItem[] lineItems = new LineItem[]{
            new LineItem("learning-to-ski", "Learning to Ski", new ProductCategory[]{
                ProductCategory.Videos
            })
        };
        Customer customer = mock(Customer.class);
        Order order = new Order(customer, lineItems, null);
        Payment payment = new Payment(order);

        PackingSlipService packingSlipService = mock(PackingSlipService.class);
        ShippingService shippingService = mock(ShippingService.class);
        RoyaltyService royaltyService = mock(RoyaltyService.class);
        
        PaymentHandler sut = new PackingSlipHandler(shippingService, royaltyService, packingSlipService);
        sut.run(payment);

        String[] gifts = order.getGiftSkus();
        assertNotNull(gifts);
        assertTrue(gifts.length == 1);
        assertTrue(Arrays.binarySearch(gifts, "first-aid") == 0); 
    }

    @Test
    public void runShouldGeneratePackingSlip() throws Exception {
        LineItem[] lineItems = new LineItem[]{
            new LineItem("item1", "item1", new ProductCategory[]{
                ProductCategory.Physical
            })
        };
        Customer customer = mock(Customer.class);
        Order order = new Order(customer, lineItems, null);
        Payment payment = new Payment(order);

        PackingSlipService packingSlipService = mock(PackingSlipService.class);
        ShippingService shippingService = mock(ShippingService.class);
        RoyaltyService royaltyService = mock(RoyaltyService.class);
        
        PaymentHandler sut = new PackingSlipHandler(shippingService, royaltyService, packingSlipService);
        sut.run(payment);

        verify(packingSlipService, times(1)).generate(order);
    }
}