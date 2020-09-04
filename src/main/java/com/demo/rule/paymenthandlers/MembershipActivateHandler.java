package com.demo.rule.paymenthandlers;

import com.demo.rule.domain.Customer;
import com.demo.rule.domain.LineItem;
import com.demo.rule.domain.Membership;
import com.demo.rule.domain.Order;
import com.demo.rule.domain.Payment;
import com.demo.rule.domain.ProductCategory;
import com.demo.rule.repositories.MembershipRepository;
import com.demo.rule.services.NotificationService;

// If the payment is for a membership, activate that membership.
// If the payment is for a membership or upgrade, e-mail the owner and inform them of the activation/upgrade.
public class MembershipActivateHandler implements PaymentHandler{
    private final MembershipRepository _service;
    private final NotificationService _notificationService;

    public MembershipActivateHandler(final MembershipRepository service, final NotificationService notificationService) {
        _service = service;
        _notificationService = notificationService;
    }

    @Override
    public void run(final Payment payment) {
        final Order order = payment.getOrder();
        final LineItem[] lineItems = order.getLineItems();
        final Customer customer = order.getCustomer();
        for (final LineItem lineItem : lineItems) {
            if (!lineItem.hasCategory(ProductCategory.Membership))
                continue;

            final Membership membership = _service.findBySku(lineItem.getSku());
            if(membership != null)
                customer.addMembership(membership, _notificationService);
        }
    }

}