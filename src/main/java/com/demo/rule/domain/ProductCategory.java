package com.demo.rule.domain;

import com.demo.rule.domain.ProductCategory;


public class ProductCategory {
    private ProductCategory(final String name) {
        _name = name;
    }

    public final String _name;
    public String getName() {
        return _name;
    }

    public static final ProductCategory Books = new ProductCategory("books");
    public static final ProductCategory Physical = new ProductCategory("physical");
    public static final ProductCategory Virtual = new ProductCategory("virtual");
    public static final ProductCategory Membership = new ProductCategory("membership");
    public static final ProductCategory Videos = new ProductCategory("videos");
}