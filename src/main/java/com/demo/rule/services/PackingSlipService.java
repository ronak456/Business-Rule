package com.demo.rule.services;

import com.demo.rule.domain.Order;
import com.demo.rule.domain.PackingSlip;

public interface PackingSlipService {
    PackingSlip generate(Order order);
}