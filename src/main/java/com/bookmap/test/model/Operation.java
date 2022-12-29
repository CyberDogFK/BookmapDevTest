package com.bookmap.test.model;

import java.util.Objects;

public class Operation {
    private int price;
    private int size;
    private Type type;

    public enum Type {
        BID,
        ASK,
        SPREAD
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Operation operation = (Operation) o;
        return price == operation.price
                && size == operation.size
                && type == operation.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, size, type);
    }
}
