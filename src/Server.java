import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket server;
    private ExecutorService executor;
    public static Map<String, Room> rooms;

    public Server(int port) {
        try {
            server = new ServerSocket(port);
            executor = Executors.newFixedThreadPool(5);
            rooms = Collections.synchronizedMap(new HashMap<>());
            rooms.put("101", new Room(101, 250));
            rooms.put("102", new Room(102, 270));
            rooms.put("201", new Room(201, 300));
            rooms.put("202", new Room(202, 450));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        System.out.println("Server is running...");
        try {
            while (true) {
                Socket socket = server.accept();
                System.out.println("Client connected: " + socket.getInetAddress());
                executor.execute(new ClientHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server(8888);
        server.start();
    }
}
