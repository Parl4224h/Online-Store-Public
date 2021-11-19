import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class Customer {
    private String firstName, lastName, address, userName, password;
    private int userID, CVC;
    private long ccNumber;
    private ArrayList<Order> pastOrders;
    private LocalDate DOB, ccExpiration;
    private double distance;

    public Customer(String firstName, String lastName, String address, String userName, String password, int userID, long ccNumber, int CVC, ArrayList<Order> pastOrders, LocalDate DOB, LocalDate ccExpiration, double distance) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.userName = userName;
        this.password = password;
        this.userID = userID;
        this.ccNumber = ccNumber;
        this.CVC = CVC;
        this.pastOrders = pastOrders;
        this.DOB = DOB;
        this.ccExpiration = ccExpiration;
        this.distance = distance;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public long getCcNumber() {
        return ccNumber;
    }

    public void setCcNumber(long ccNumber) {
        this.ccNumber = ccNumber;
    }

    public int getCVC() {
        return CVC;
    }

    public void setCVC(int CVC) {
        this.CVC = CVC;
    }

    public ArrayList<Order> getPastOrders() {
        return pastOrders;
    }

    public LocalDate getDOB() {
        return DOB;
    }

    public void setDOB(LocalDate DOB) {
        this.DOB = DOB;
    }

    public LocalDate getCcExpiration() {
        return ccExpiration;
    }

    public void setCcExpiration(LocalDate ccExpiration) {
        this.ccExpiration = ccExpiration;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPastOrders(ArrayList<Order> pastOrders) {
        this.pastOrders = pastOrders;
    }

    public void addOrder(Order order){
        pastOrders.add(order);
    }
}
