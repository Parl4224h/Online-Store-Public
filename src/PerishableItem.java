import java.io.Serializable;
import java.time.LocalDate;

public class PerishableItem extends Item implements Serializable {
    private LocalDate expirationDate;
    private boolean donatable;

    public PerishableItem() {
    }

    public PerishableItem(String name, int SKU, int stock, double width, double height, double length, double weight, double price, LocalDate expirationDate, boolean donatable) {
        super(name, SKU, stock, width, height, length, weight, price);
        super.type = 1;
        this.expirationDate = expirationDate;
        this.donatable = donatable;
    }

    public String toString() {
        return "SKU: " + getSKU() + ", Name: " + getName() + ", Price: " + getPrice() + ", expires on: " + expirationDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public String getExpirationDateString(){
        return ""+expirationDate;
    }

    public boolean isDonatable() {
        return donatable;
    }
}
