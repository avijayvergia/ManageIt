package com.example.aksha.manageit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aksha on 28/03/2017.
 */

public class Order {

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    private String quantity;
    private String org_name;


    public Order() {
    }

    public Order(String id, String content) {
        this.quantity = id;
        this.org_name = content;
    }


    @Override
    public String toString() {
        return org_name;
    }

}
