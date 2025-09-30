import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SocketServerTest {

    private SocketServer.ClientHandler clientHandler;

    @BeforeEach
    void setUp() {
        // For testing the handleClientRequest method, we don't need a real socket.
        // We can pass null for the Socket and ServerSocket objects.
        clientHandler = new SocketServer.ClientHandler(null, null);
    }

    @Test
    void testHandleClientRequest_ValidNumber() {
        String clientName = "TestClient";
        int clientNumber = 50;
        String expectedResponse = SocketServer.SERVER_NAME + "," + SocketServer.SERVER_NUMBER;

        String actualResponse = clientHandler.handleClientRequest(clientName, clientNumber);

        assertEquals(expectedResponse, actualResponse, "Response for a valid number should contain server name and number.");
    }

    @Test
    void testHandleClientRequest_NumberTooLow() {
        String clientName = "TestClient";
        int clientNumber = 0; // Out of range (less than 1)
        String expectedResponse = "Server is shutting down due to out-of-range input.";

        String actualResponse = clientHandler.handleClientRequest(clientName, clientNumber);

        assertEquals(expectedResponse, actualResponse, "Response for a number less than 1 should be a shutdown message.");
    }

    @Test
    void testHandleClientRequest_NumberTooHigh() {
        String clientName = "TestClient";
        int clientNumber = 101; // Out of range (greater than 100)
        String expectedResponse = "Server is shutting down due to out-of-range input.";

        String actualResponse = clientHandler.handleClientRequest(clientName, clientNumber);

        assertEquals(expectedResponse, actualResponse, "Response for a number greater than 100 should be a shutdown message.");
    }
}

