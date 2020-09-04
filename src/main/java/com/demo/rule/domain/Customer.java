package com.demo.rule.domain;

import com.demo.rule.services.NotificationService;

// fake empty class, behaviour is mocked in the tests 
public class Customer {
   public void addMembership(Membership membership, NotificationService notificationService) {
      // notifications should happen via Domain Events,
      // this is just a quick hack.
      notificationService.notify(this, membership);
   }

   public boolean hasMembership(Membership membership) {
      return false;
   }
}
