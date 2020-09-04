package com.demo.rule.paymenthandlers;

import com.demo.rule.domain.LineItem;
import com.demo.rule.domain.Order;
import com.demo.rule.domain.Payment;
import com.demo.rule.domain.ProductCategory;
import com.demo.rule.services.PackingSlipService;
import com.demo.rule.services.RoyaltyService;
import com.demo.rule.services.ShippingService;


// If the payment is for a physical product, generate a packing slip for shipping.
// If the payment is for a book, create a duplicate packing slip for the royalty department.
// if the payment is for the video â€œLearning to Ski,â€? add a free â€œFirst Aidâ€? video to the packing slip
public class PackingSlipHandler implements PaymentHandler{
    private final PackingSlipService _packingSlipService;
    private final ShippingService _shippingService;
    private final RoyaltyService _royaltyService;

    public PackingSlipHandler(final ShippingService shippingService, 
                              final RoyaltyService royaltyService,
                              final PackingSlipService packingSlipService){
        _shippingService = shippingService;
        _royaltyService = royaltyService;
        _packingSlipService = packingSlipService;
    }

    @Override
    public void run(final Payment payment) {
        final Order order = payment.getOrder();
        final LineItem[] lineItems = order.getLineItems();

        Boolean generateForShipping = false;
        Boolean generateForRoyalty = false;

        for (final LineItem lineItem : lineItems) {
            if(lineItem.hasCategory(ProductCategory.Physical))
                generateForShipping = true;
            if(lineItem.hasCategory(ProductCategory.Books))
                generateForRoyalty = true;

            if(lineItem.getSku() == "learning-to-ski") 
                order.addGiftBySku("first-aid");
        }

        _packingSlipService.generate(order);

        if(generateForShipping)
            _shippingService.generatePackingSlip(order);
        if(generateForRoyalty)
            _royaltyService.generatePackingSlip(order);
    }
}
