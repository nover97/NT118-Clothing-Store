package app.nover.clothingstore.models;

import java.util.ArrayList;
import java.util.List;

public class ItemModel {
    String imageUrl;
    String name;
    String Price;
    String originalPrice;
    String description;
    List<String> arraySize;
    List<String> arrayColor;
    String projectId;
    String category;

    public List<String> getArrayColor() {
        return arrayColor;
    }

    public void setArrayColor(List<String> arrayColor) {
        this.arrayColor = arrayColor;
    }

    public List<String> getArraySize() {
        return arraySize;
    }

    public void setArraySize(List<String> arraySize) {
        this.arraySize = arraySize;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ItemModel() {
    }

    public ItemModel(String imageUrl, String name, String price, String originalPrice) {
        this.imageUrl = imageUrl;
        this.name = name;
        Price = price;
        this.originalPrice = originalPrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }


}
