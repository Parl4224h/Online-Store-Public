import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.util.ArrayList;

public class Order {
    private final LocalDate orderDate;
    private int customerID;
    private int invoice, OID;//OID is order id
    private double total, tax, shippingCost;
    private ArrayList<Boxes.Box> boxes;
    private ArrayList<Item> items;
    private boolean perishable, hasGarments;
    private Carriers.Carrier carrier;

    public Order(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public Order(LocalDate orderDate, int customerID, int invoice, int OID, double total, double tax, double shippingCost, ArrayList<Boxes.Box> boxes, ArrayList<Item> items, boolean perishable, boolean hasGarments, Carriers.Carrier carrier) {
        this.orderDate = orderDate;
        this.customerID = customerID;
        this.invoice = invoice;
        this.OID = OID;
        this.total = total;
        this.tax = tax;
        this.shippingCost = shippingCost;
        this.boxes = boxes;
        this.items = items;
        this.perishable = perishable;
        this.hasGarments = hasGarments;
        this.carrier = carrier;
    }

    public int getOID() {
        return OID;
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("Date: ").append(orderDate);
        builder.append("\nInvoice: ").append(invoice);
        builder.append("\nTotal: ").append(total);
        builder.append("\nTax: ").append(tax);
        builder.append("\nShipping Cost: ").append(shippingCost);
        builder.append("\nBoxes: ");
        for (Boxes.Box box: boxes){
            builder.append(box).append(", ");
        }
        builder.append("\nItems: ");
        for (Item item : items){
            builder.append(item.toString()).append(", ");
        }
        builder.append("\nHas Perishables: ").append(perishable);
        builder.append("\nHas Garments: ").append(hasGarments);
        builder.append("\nCarrier: ").append(carrier);
        return builder.toString();
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public int getCustomerID() {
        return customerID;
    }

    public int getInvoice() {
        return invoice;
    }

    public double getTotal() {
        return total;
    }

    public double getTax() {
        return tax;
    }

    public double getShippingCost() {
        return shippingCost;
    }

    public ArrayList<Boxes.Box> getBoxes() {
        return boxes;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public boolean isPerishable() {
        return perishable;
    }

    public boolean hasGarments() {
        return hasGarments;
    }

    public Carriers.Carrier getCarrier() {
        return carrier;
    }
}
