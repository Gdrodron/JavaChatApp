import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class ChatClientGUI {
    private String username;
    private JTextArea chatArea;
    private JTextField inputField;
    private PrintWriter output;

    public ChatClientGUI() throws Exception {
        Socket socket = new Socket("localhost", 1234);

        BufferedReader input = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);

        // 👇 ISA LANG DAPAT
        username = JOptionPane.showInputDialog("Enter your name:");

        JFrame frame = new JFrame("Messenger");
        chatArea = new JTextArea();
        inputField = new JTextField();

        chatArea.setEditable(false);

        frame.setLayout(new BorderLayout());
        frame.add(new JScrollPane(chatArea), BorderLayout.CENTER);
        frame.add(inputField, BorderLayout.SOUTH);

        inputField.addActionListener(e -> {
            output.println(username + ": " + inputField.getText());
            inputField.setText("");
        });

        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        new Thread(() -> {
            try {
                String msg;
                while ((msg = input.readLine()) != null) {
                    chatArea.append(msg + "\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) throws Exception {
        new ChatClientGUI();
    }
}