import java.io.*;
import java.net.*;

public class SocketServer {
     public static final int SERVER_PORT = 5701;
    public static final String SERVER_NAME = "Server of Tony";
    public static final int SERVER_NUMBER = 42;

    public static void main(String[] args) {
        System.out.println("[Server] Starting on port " + SERVER_PORT);
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("[Server] Waiting for client connections...");
            while (!serverSocket.isClosed()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("[Server] Accepted connection from " + clientSocket.getInetAddress());
                    // Create a new thread to handle the client connection
                    new Thread(new ClientHandler(clientSocket, serverSocket)).start();
                } catch (IOException e) {
                    if (serverSocket.isClosed()) {
                        System.out.println("[Server] Server socket closed, shutting down.");
                        break;
                    }
                    System.out.println("[Server] Error accepting client connection: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("[Server] Error starting server: " + e.getMessage());
        }
        System.out.println("[Server] Terminated.");
    }

    static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private final ServerSocket serverSocket;

        public ClientHandler(Socket socket, ServerSocket serverSocket) {
            this.clientSocket = socket;
            this.serverSocket = serverSocket;
        }

        /**
         * Processes the client request and returns a response string.
         * This method contains the core logic for handling client input.
         * @param clientName The name of the client.
         * @param clientNumber The number sent by the client.
         * @return A string response to be sent back to the client.
         */
        public String handleClientRequest(String clientName, int clientNumber) {
            System.out.println("[Server] Received from " + clientName + ": " + clientNumber);

            if (clientNumber < 1 || clientNumber > 100) {
                System.out.println("[Server] Client number " + clientNumber + " is out of range. Informing client.");
                return "Number is out of range. Server continues running.";
            }

            System.out.println("[Server] Client's name: " + clientName);
            System.out.println("[Server] My name: " + SocketServer.SERVER_NAME);
            System.out.println("[Server] Client's number: " + clientNumber);
            System.out.println("[Server] My number: " + SocketServer.SERVER_NUMBER);
            int sum = clientNumber + SocketServer.SERVER_NUMBER;
            System.out.println("[Server] Sum: " + sum);

            // Send server name and number back to the client
            return SocketServer.SERVER_NAME + "," + SocketServer.SERVER_NUMBER;
        }

        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String message = in.readLine();
                if (message == null) {
                    System.out.println("[Server] Client disconnected before sending a message.");
                    return;
                }

                String[] parts = message.split(",");
                if (parts.length != 2) {
                    System.out.println("[Server] Invalid message format from client.");
                    return;
                }

                String clientName = parts[0];
                int clientNumber = Integer.parseInt(parts[1]);

                String response = handleClientRequest(clientName, clientNumber);
                out.println(response);

            } catch (IOException e) {
                System.out.println("[Server] Error handling client: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("[Server] Invalid number format from client.");
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.out.println("[Server] Error closing client socket: " + e.getMessage());
                }
            }
        }
    }
}
