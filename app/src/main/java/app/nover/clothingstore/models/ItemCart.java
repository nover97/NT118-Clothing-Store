package app.nover.clothingstore.models;

import java.util.List;

public class ItemCart {
    String imageUrl;
    String name;
    String Price;
    String size;
    String color;
    String count;
    String arraySize;
    String arrayColor;
    String oriPrice;
    String id;
    Boolean isCheck;
    String totalItem;

    public String getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(String totalItem) {
        this.totalItem = totalItem;
    }


    public Boolean getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(Boolean check) {
        this.isCheck = check;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriPrice() {
        return oriPrice;
    }

    public void setOriPrice(String oriPrice) {
        this.oriPrice = oriPrice;
    }

    public String getArrayColor() {
        return arrayColor;
    }

    public void setArrayColor(String arrayColor) {
        this.arrayColor = arrayColor;
    }

    public String getArraySize() {
        return arraySize;
    }

    public void setArraySize(String arraySize) {
        this.arraySize = arraySize;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
