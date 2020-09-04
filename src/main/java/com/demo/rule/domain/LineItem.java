package com.demo.rule.domain;

import java.util.HashMap;

public class LineItem {
    public LineItem(final String sku, final String name, final ProductCategory[] categories){
        _sku = sku;
        _name = name;

        _categories = new HashMap<String, ProductCategory>();
         if(categories != null){
             for(ProductCategory cat : categories){
                 if(!_categories.containsKey(cat.getName()))
                    _categories.put(cat.getName(), cat);
             }
         }
    }

    private final String _sku;
    public String getSku() {
        return _sku;
    }

    public final String _name;
    public String getName() {
        return _name;
    }

    private final HashMap<String, ProductCategory> _categories;
	public boolean hasCategory(final ProductCategory category) {
		return _categories.containsKey(category.getName());
	}
}