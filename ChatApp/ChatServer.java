import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static List<PrintWriter> clients = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(1234);
        System.out.println("Server started...");

        while (true) {
            Socket socket = server.accept();
            System.out.println("New client connected");

            new Thread(() -> handleClient(socket)).start();
        }
    }

    private static void handleClient(Socket socket) {
        PrintWriter output = null;

        try {
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            clients.add(output);

            String msg;
            while ((msg = input.readLine()) != null) {
                System.out.println("Message: " + msg);

                synchronized (clients) {
                    for (PrintWriter client : clients) {
                        client.println(msg);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Client disconnected");
        } finally {
            if (output != null) {
                clients.remove(output);
            }
        }
    }
}