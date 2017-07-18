package com.example.a15041867.c302_p09_mcafe;

/**
 * Created by 15041867 on 18/7/2017.
 */

public class category {
    private String category_id;
    private String category_desc;

    public category() {

    }

    public category(String category_id, String category_desc) {
        this.category_id = category_id;
        this.category_desc = category_desc;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_desc() {
        return category_desc;
    }

    public void setCategory_desc(String category_desc) {
        this.category_desc = category_desc;
    }

    @Override
    public String toString() {
        return category_desc;
    }
}
