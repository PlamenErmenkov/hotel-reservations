import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable{
    private PrintStream writer;
    private Scanner reader;

    public ClientHandler(Socket socket) {
        try {
            writer = new PrintStream(socket.getOutputStream());
            reader = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        writer.println(message);
    }

    public String getMessage() {
        return reader.nextLine();
    }

    @Override
    public void run() {
        sendMessage("Welcome user!");
        sendMessage("""
                    Enter your choice:
                    1: Check if a room is available.
                    2: Reserve a room.
                    3: Cancel room reservation.
                    """);

        String choice = getMessage();
        switch (choice) {
            case "1":
                sendFirstChoiceMenu();
                break;
            case "2":
                sendSecondChoiceMenu();
                break;
            case "3":
                sendThirdChoiceMenu();
                break;
            default:
                sendMessage("No such option!");
                break;
        }
        sendMessage("Thank you for using our app.");
    }

    public void sendFirstChoiceMenu() {
        sendMessage("Enter room number: ");
        String roomNumber = getMessage();

        if (!checkIfRoomExists(roomNumber)) {
            sendMessage("The room does not exist!");
            return;
        }

        if (checkIfRoomIsAvailable(roomNumber)) {
            sendMessage("Room " + roomNumber + " is available.");
        } else {
            sendMessage("Room " + roomNumber + " is not available.");
        }
    }

    private synchronized boolean checkIfRoomExists(String roomNumber) {
        return Server.rooms.containsKey(roomNumber);
    }

    private synchronized boolean checkIfRoomIsAvailable(String roomNumber) {
        return Server.rooms.get(roomNumber).getIsAvailable();
    }

    public void sendSecondChoiceMenu() {
        sendMessage("Enter which room number you would like to reserve: ");
        String roomNumber = getMessage();
        if (!checkIfRoomExists(roomNumber) || !checkIfRoomIsAvailable(roomNumber)) {
            sendMessage("The room does not exist or is not available!");
            return;
        }

        reserveRoom(roomNumber);
        sendMessage("Room reserved successfully.");
    }

    private synchronized void reserveRoom(String roomNumber) {
        Room room = Server.rooms.get(roomNumber);
        room.setAvailable(false);
    }

    public void sendThirdChoiceMenu() {
        sendMessage("Enter your room for cancellation.");
        String roomNumber = getMessage();

        if (!checkIfRoomExists(roomNumber) && checkIfRoomIsAvailable(roomNumber)) {
            sendMessage("The room either does not exist or is no reserved!");
            return;
        }

        cancelRoomReservation(roomNumber);
        sendMessage("Room reservation cancelled successfully.");
    }

    private synchronized void cancelRoomReservation(String roomNumber) {
        Room room = Server.rooms.get(roomNumber);
        room.setAvailable(true);
    }
}