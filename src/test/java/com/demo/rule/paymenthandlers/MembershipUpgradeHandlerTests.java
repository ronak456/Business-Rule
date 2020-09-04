package com.demo.rule.paymenthandlers;

import static org.mockito.Mockito.*;

import com.demo.rule.domain.Customer;
import com.demo.rule.domain.LineItem;
import com.demo.rule.domain.Membership;
import com.demo.rule.domain.Order;
import com.demo.rule.domain.Payment;
import com.demo.rule.domain.ProductCategory;
import com.demo.rule.paymenthandlers.MembershipUpgradeHandler;
import com.demo.rule.paymenthandlers.PaymentHandler;
import com.demo.rule.repositories.MembershipRepository;
import com.demo.rule.services.NotificationService;

import org.junit.Test;

public class MembershipUpgradeHandlerTests{
    @Test
    public void runShouldUpgradeMembership() throws Exception {
        Membership membershipSilver = new Membership("membership-silver", null);
        Membership membershipGold = new Membership("membership-gold", membershipSilver);

        LineItem[] lineItems = new LineItem[]{
            new LineItem(membershipGold.getSku(), "gold", new ProductCategory[]{
                ProductCategory.Membership
            })
        };
        Customer customer = mock(Customer.class);
        when(customer.hasMembership(membershipSilver)).thenReturn(true);

        Order order = new Order(customer, lineItems, null);
        Payment payment = new Payment(order);

        MembershipRepository repo = mock(MembershipRepository.class);
        when(repo.findBySku(membershipGold.getSku())).thenReturn(membershipGold);
        
        NotificationService notificationService = mock(NotificationService.class);

        PaymentHandler sut = new MembershipUpgradeHandler(repo, notificationService);
        sut.run(payment);

        verify(customer, times(1)).addMembership(membershipGold, notificationService);
    }

    @Test
    public void runShouldNotifyCustomer() throws Exception {
        Membership membershipSilver = new Membership("membership-silver", null);
        Membership membershipGold = new Membership("membership-gold", membershipSilver);

        LineItem[] lineItems = new LineItem[]{
            new LineItem(membershipGold.getSku(), "gold", new ProductCategory[]{
                ProductCategory.Membership
            })
        };
        Customer customer = spy(Customer.class);
        when(customer.hasMembership(membershipSilver)).thenReturn(true);

        Order order = new Order(customer, lineItems, null);
        Payment payment = new Payment(order);

        MembershipRepository repo = mock(MembershipRepository.class);
        when(repo.findBySku(membershipGold.getSku())).thenReturn(membershipGold);
        
        NotificationService notificationService = mock(NotificationService.class);

        PaymentHandler sut = new MembershipUpgradeHandler(repo, notificationService);
        sut.run(payment);

        verify(notificationService, times(1)).notify(customer, membershipGold);
    }
}