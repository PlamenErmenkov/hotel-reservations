import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("localhost", 8888)) {
            System.out.println("Client connected: " + socket.getInetAddress());
            Scanner scanner = new Scanner(System.in);
            PrintStream output = new PrintStream(socket.getOutputStream());

            startResponseReaderThread(socket);

            while (true) {
                String command = scanner.nextLine();
                if (command.equals("exit")) {
                    break;
                }
                output.println(command);
            }
        }
    }

    private static void startResponseReaderThread(Socket socket) {
        Thread responseThread = new Thread(() -> {
            try {
                Scanner serverResponse = new Scanner(socket.getInputStream());
                while (serverResponse.hasNextLine()) {
                    String response = serverResponse.nextLine();
                    System.out.println(response);
                }
            } catch (IOException e) {
                System.out.println("Server disconnected");
            }
        });
        responseThread.start();
    }
}