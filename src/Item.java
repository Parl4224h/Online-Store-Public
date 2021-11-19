import java.io.Serializable;

public class Item implements Serializable {

    private static int itemCount = 0;
    protected int type;
    private String name;
    private int SKU, stock;
    private double width, height, length, weight, price;

    public Item(){}

    public Item(String name, double width, double height, double length, double weight, double price, int stock) {
        itemCount++;
        SKU = itemCount;
        type = 0;
        this.name = name;
        this.width = width;
        this.height = height;
        this.length = length;
        this.weight = weight;
        this.price = price;
        this.stock = stock;
    }

    public Item(String name, int SKU, int stock, double width, double height, double length, double weight, double price) {
        type = 0;
        this.name = name;
        this.SKU = SKU;
        this.stock = stock;
        this.width = width;
        this.height = height;
        this.length = length;
        this.weight = weight;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public int getItemNum() {
        return SKU;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setItemNum(int itemNum) {
        this.SKU = itemNum;
    }

    public static int getItemCount() {
        return itemCount;
    }

    public static void increment(){
        itemCount++;
    }

    public int getStock() {
        return stock;
    }

    public int getSKU() {
        return SKU;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "SKU: " + SKU + ", Name: " + name + ", Price: " + price;
    }
}
