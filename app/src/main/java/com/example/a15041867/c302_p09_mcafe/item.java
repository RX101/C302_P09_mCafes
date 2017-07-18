package com.example.a15041867.c302_p09_mcafe;

/**
 * Created by 15041867 on 18/7/2017.
 */

public class item {
    private int item_id;
    private int category_id;
    private String item_desc;
    private double item_price;

    public item(){

    }

    public item(int item_id, int category_id, String item_desc, double item_price) {
        this.item_id = item_id;
        this.category_id = category_id;
        this.item_desc = item_desc;
        this.item_price = item_price;
    }


    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getItem_desc() {
        return item_desc;
    }

    public void setItem_desc(String item_desc) {
        this.item_desc = item_desc;
    }

    public double getItem_price() {
        return item_price;
    }

    public void setItem_price(double item_price) {
        this.item_price = item_price;
    }

    @Override
    public String toString() {
        return item_desc + "\n"
                + item_price;
    }
}
