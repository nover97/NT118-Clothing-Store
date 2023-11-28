package app.nover.clothingstore.models;

public class ItemModel {
    String imageUrl;
    String name;
    int Price;
    int originalPrice;

    public ItemModel() {
    }

    public ItemModel(String imageUrl, String name, int price, int originalPrice) {
        this.imageUrl = imageUrl;
        this.name = name;
        Price = price;
        this.originalPrice = originalPrice;
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

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(int originalPrice) {
        this.originalPrice = originalPrice;
    }
}
