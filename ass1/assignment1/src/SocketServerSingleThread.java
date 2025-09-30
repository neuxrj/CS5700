import java.io.*;
import java.net.*;

public class SocketServerSingleThread {
    public static void main(String[] args) {
        System.out.println("[SingleThreadServer] Starting on port " + SocketServer.SERVER_PORT);
        try (ServerSocket serverSocket = new ServerSocket(SocketServer.SERVER_PORT)) {
            System.out.println("[SingleThreadServer] Waiting for client connections...");
            while (!serverSocket.isClosed()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("[SingleThreadServer] Accepted connection from " + clientSocket.getInetAddress());
                    handleClient(clientSocket, serverSocket);
                } catch (IOException e) {
                    if (serverSocket.isClosed()) {
                        System.out.println("[SingleThreadServer] Server socket closed, shutting down.");
                        break;
                    }
                    System.out.println("[SingleThreadServer] Error accepting client connection: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("[SingleThreadServer] Error starting server: " + e.getMessage());
        }
        System.out.println("[SingleThreadServer] Terminated.");
    }

    private static void handleClient(Socket clientSocket, ServerSocket serverSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String message = in.readLine();
            if (message == null) {
                System.out.println("[SingleThreadServer] Client disconnected before sending a message.");
                return;
            }

            String[] parts = message.split(",");
            if (parts.length != 2) {
                System.out.println("[SingleThreadServer] Invalid message format from client.");
                return;
            }

            String clientName = parts[0];
            int clientNumber = Integer.parseInt(parts[1]);

            // 复用 SocketServer.ClientHandler 的 handleClientRequest 方法
            SocketServer.ClientHandler handler = new SocketServer.ClientHandler(clientSocket, serverSocket);
            String response = handler.handleClientRequest(clientName, clientNumber);
            out.println(response);

        } catch (IOException e) {
            System.out.println("[SingleThreadServer] Error handling client: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("[SingleThreadServer] Invalid number format from client.");
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("[SingleThreadServer] Error closing client socket: " + e.getMessage());
            }
        }
    }
}

