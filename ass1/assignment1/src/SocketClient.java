import java.io.*;
import java.net.*;
import java.util.Scanner;

public class SocketClient {
    public static void main(String[] args) {
        final String SERVER_HOST = "localhost";
        final int SERVER_PORT = 5701;
        final String CLIENT_NAME = "Client of Tony";

        Scanner scanner = new Scanner(System.in);


            System.out.print("Enter an integer between 1 and 100 (or an invalid number to shut down the server): ");
            int clientNumber = scanner.nextInt();

            try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                System.out.println("[Client] Connected to server.");

                String message = CLIENT_NAME + "," + clientNumber;
                out.println(message);
                System.out.println("[Client] Sent: " + message);

                String response = in.readLine();
                System.out.println("[Client] Received: " + response);

                if (response == null || response.contains("shutting down")) {
                    System.out.println("[Client] Server has closed the connection.");
                }

                String[] parts = response.split(",");
                String serverName = parts[0];
                int serverNumber = Integer.parseInt(parts[1]);

                int sum = clientNumber + serverNumber;

                System.out.println("[Client] My name: " + CLIENT_NAME);
                System.out.println("[Client] Server name: " + serverName);
                System.out.println("[Client] My number: " + clientNumber);
                System.out.println("[Client] Server number: " + serverNumber);
                System.out.println("[Client] Sum: " + sum);

            } catch (IOException e) {
                System.out.println("[Client] Error: " + e.getMessage());

            }


        System.out.println("[Client] Terminated.");
    }
}
