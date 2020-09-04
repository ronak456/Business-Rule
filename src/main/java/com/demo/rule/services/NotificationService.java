package com.demo.rule.services;

import com.demo.rule.domain.Customer;
import com.demo.rule.domain.Membership;

public interface NotificationService {
	void notify(Customer customer, Membership membership);
}
