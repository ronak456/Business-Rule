package com.demo.rule.paymenthandlers;

import com.demo.rule.domain.Payment;

public interface PaymentHandler {
    void run(final Payment payment);
}
