package com.demo.rule.paymenthandlers;

import static org.mockito.Mockito.*;

import org.junit.Test;

import com.demo.rule.domain.Agent;
import com.demo.rule.domain.Customer;
import com.demo.rule.domain.LineItem;
import com.demo.rule.domain.Order;
import com.demo.rule.domain.Payment;
import com.demo.rule.domain.ProductCategory;
import com.demo.rule.paymenthandlers.AgentCommissionHandler;
import com.demo.rule.paymenthandlers.PaymentHandler;

public class AgentCommissionHandlerTests{
    @Test
    public void runShouldNotGenerateAgentCommissionIfItemsInvalid(){
        LineItem[] lineItems = new LineItem[]{
            new LineItem("item", "item", new ProductCategory[]{
                ProductCategory.Membership,
                ProductCategory.Virtual,
            })
        };
        Customer customer = mock(Customer.class);
        Agent agent = mock(Agent.class);
        Order order = new Order(customer, lineItems, agent);
        Payment payment = new Payment(order);

        PaymentHandler sut = new AgentCommissionHandler();
        sut.run(payment);

        verify(agent, never()).generateCommission(any());
    }

    @Test
    public void runShouldGenerateAgentCommissionIfBookInOrder(){
        LineItem[] lineItems = new LineItem[]{
            new LineItem("item", "item", new ProductCategory[]{
                ProductCategory.Membership
            }),
            new LineItem("book1", "book1", new ProductCategory[]{
                ProductCategory.Books
            })
        };
        Customer customer = mock(Customer.class);
        Agent agent = mock(Agent.class);
        Order order = new Order(customer, lineItems, agent);
        Payment payment = new Payment(order);

        PaymentHandler sut = new AgentCommissionHandler();
        sut.run(payment);

        verify(agent, times(1)).generateCommission(any());
    }

    @Test
    public void runShouldGenerateAgentCommissionOnceIfMultipleBooksInOrder(){
        LineItem[] lineItems = new LineItem[]{
            new LineItem("book1", "book1", new ProductCategory[]{
                ProductCategory.Books
            }),
            new LineItem("book2", "book2", new ProductCategory[]{
                ProductCategory.Books
            }),
            new LineItem("book3", "book3", new ProductCategory[]{
                ProductCategory.Books
            })
        };
        Customer customer = mock(Customer.class);
        Agent agent = mock(Agent.class);
        Order order = new Order(customer, lineItems, agent);
        Payment payment = new Payment(order);

        PaymentHandler sut = new AgentCommissionHandler();
        sut.run(payment);

        verify(agent, times(1)).generateCommission(any());
    }

    @Test
    public void runShouldGenerateAgentCommissionIfPhysicalItemInOrder(){
        LineItem[] lineItems = new LineItem[]{
            new LineItem("item", "item", new ProductCategory[]{
                ProductCategory.Physical
            }),
            new LineItem("membership", "membership", new ProductCategory[]{
                ProductCategory.Membership
            })
        };
        Customer customer = mock(Customer.class);
        Agent agent = mock(Agent.class);
        Order order = new Order(customer, lineItems, agent);
        Payment payment = new Payment(order);

        PaymentHandler sut = new AgentCommissionHandler();
        sut.run(payment);

        verify(agent, times(1)).generateCommission(any());
    }
}