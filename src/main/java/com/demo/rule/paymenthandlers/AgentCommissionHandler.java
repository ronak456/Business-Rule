package com.demo.rule.paymenthandlers;

import com.demo.rule.domain.Agent;
import com.demo.rule.domain.LineItem;
import com.demo.rule.domain.Order;
import com.demo.rule.domain.Payment;
import com.demo.rule.domain.ProductCategory;

// If the payment is for a physical product or a book, generate a commission payment to the agent.
public class AgentCommissionHandler implements PaymentHandler{

    @Override
    public void run(Payment payment) {
        Order order = payment.getOrder();
        LineItem[] lineItems = order.getLineItems();

        Boolean addCommission = false;

        for (LineItem lineItem : lineItems) {
            if(lineItem.hasCategory(ProductCategory.Books) || lineItem.hasCategory(ProductCategory.Physical)){
                addCommission = true;
                break;
            }
        }

        if(addCommission){
            Agent agent = order.getAgent();
            agent.generateCommission(order);
        }
    }

}