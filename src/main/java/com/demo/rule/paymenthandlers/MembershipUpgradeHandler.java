package com.demo.rule.paymenthandlers;

import com.demo.rule.domain.Customer;
import com.demo.rule.domain.LineItem;
import com.demo.rule.domain.Membership;
import com.demo.rule.domain.Order;
import com.demo.rule.domain.Payment;
import com.demo.rule.domain.ProductCategory;
import com.demo.rule.repositories.MembershipRepository;
import com.demo.rule.services.NotificationService;

// If the payment is an upgrade to a membership, apply the upgrade.
// If the payment is for a membership or upgrade, e-mail the owner and inform them of the activation/upgrade.
public class MembershipUpgradeHandler implements PaymentHandler {

    private final MembershipRepository _repo;
    private final NotificationService _notificationService;
    
    public MembershipUpgradeHandler(final MembershipRepository repo, final NotificationService notificationService) {
        _repo = repo;
        _notificationService = notificationService;
	}

	@Override
    public void run(Payment payment) {
        Order order = payment.getOrder();
        Customer customer = order.getCustomer();
        LineItem[] lineItems = order.getLineItems();
        for (LineItem lineItem : lineItems) {
            if(!lineItem.hasCategory(ProductCategory.Membership))
                continue;

            Membership membership = _repo.findBySku(lineItem.getSku());
            Membership previousLevel = membership.getPreviousLevel();
            if(customer.hasMembership(previousLevel))
                customer.addMembership(membership, _notificationService);
        }
    }
}