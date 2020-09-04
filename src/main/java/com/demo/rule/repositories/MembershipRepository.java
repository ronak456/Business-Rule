package com.demo.rule.repositories;

import com.demo.rule.domain.Membership;

public interface MembershipRepository{
	Membership findBySku(String sku);
}