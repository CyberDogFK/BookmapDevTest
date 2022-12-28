package com.bookmap.test.model;

public class Operation {
    private int price;
    private int size;
    private Type type;

    public enum Type {
        BID,
        ASK
    }

    public Operation(int price, int size, Type type) {
        this.price = price;
        this.size = size;
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Type getType() {
        return type;
    }
}
