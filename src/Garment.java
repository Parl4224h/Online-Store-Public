import java.io.Serializable;

public class Garment extends Item implements Serializable{

    private String size;

    public Garment(){}

    public Garment(String name, int SKU, int stock, double width, double height, double length, double weight, double price, String size) {
        super(name, SKU, stock, width, height, length, weight, price);
        super.type = 2;
        this.size = size;
    }

    public String toString(){
        return "SKU: " + getSKU() + ", Name: " + getName() + ", Price: " + getPrice() + ", size: " + size;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
