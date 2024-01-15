public class Room {
    private int number;
    private double price;
    private boolean isAvailable;

    public Room(int number, double price) {
        this.number = number;
        this.price = price;
        this.isAvailable = true;
    }

    public int getNumber() {
        return number;
    }

    public double getPrice() {
        return price;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
