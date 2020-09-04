package com.demo.rule.paymenthandlers;

import com.demo.rule.domain.Customer;
import com.demo.rule.domain.LineItem;
import com.demo.rule.domain.Membership;
import com.demo.rule.domain.Order;
import com.demo.rule.domain.Payment;
import com.demo.rule.domain.ProductCategory;
import com.demo.rule.paymenthandlers.MembershipActivateHandler;
import com.demo.rule.paymenthandlers.PaymentHandler;
import com.demo.rule.repositories.MembershipRepository;
import com.demo.rule.services.NotificationService;

import org.junit.Test;
import static org.mockito.Mockito.*;

public class MembershipActivateHandlerTests{
    @Test
    public void runShouldDoNothingIfNoMembershipsInOrder() throws Exception {
        LineItem[] lineItems = new LineItem[]{
            new LineItem("item1", "item1", new ProductCategory[]{
                ProductCategory.Physical
            })
        };
        Customer customer = mock(Customer.class);
        Order order = new Order(customer, lineItems, null);
        Payment payment = new Payment(order);

        MembershipRepository service = mock(MembershipRepository.class);
        NotificationService notificationService = mock(NotificationService.class);
        PaymentHandler sut = new MembershipActivateHandler(service, notificationService);
        sut.run(payment);

        verify(customer, never()).addMembership(any(), any());
    }

    @Test
    public void runShouldActivateMembershipIfInOrder() throws Exception {
        LineItem[] lineItems = new LineItem[]{
            new LineItem("item1", "item1", new ProductCategory[]{
                ProductCategory.Membership
            })
        };
        Customer customer = mock(Customer.class);
        Order order = new Order(customer, lineItems, null);
        Payment payment = new Payment(order);

        MembershipRepository repo = mock(MembershipRepository.class);
        Membership membership = new Membership("item1", null);
        when(repo.findBySku("item1")).thenReturn(membership);

        NotificationService notificationService = mock(NotificationService.class);

        PaymentHandler sut = new MembershipActivateHandler(repo, notificationService);
        sut.run(payment);

        verify(customer, times(1)).addMembership(membership, notificationService);
    }

    @Test
    public void runShouldNotifyCustomer() throws Exception {
        LineItem[] lineItems = new LineItem[]{
            new LineItem("item1", "item1", new ProductCategory[]{
                ProductCategory.Membership
            })
        };
        Customer customer = spy(Customer.class);
        Order order = new Order(customer, lineItems, null);
        Payment payment = new Payment(order);

        MembershipRepository repo = mock(MembershipRepository.class);
        Membership membership = new Membership("item1", null);
        when(repo.findBySku("item1")).thenReturn(membership);

        NotificationService notificationService = mock(NotificationService.class);

        PaymentHandler sut = new MembershipActivateHandler(repo, notificationService);
        sut.run(payment);

        verify(notificationService, times(1)).notify(customer, membership);
    }
}