# Socket Programming Assignment

## Program Description

This project consists of a client-server application developed in Java. The client sends a number to the server, and the server calculates the sum of its own number and the client's number, then sends the result back.

The server is designed to be concurrent, meaning it can handle multiple client connections simultaneously. Each client connection is managed in a separate thread.

## Concurrent Server Configuration

The concurrent server is implemented using Java's built-in threading capabilities. No external libraries are required.

-   **`java.net.ServerSocket`**: This class is used to create a server socket that listens for incoming client connection requests on a specified port.
-   **`java.net.Socket`**: When the server accepts a connection, it creates a `Socket` object to communicate with the client.
-   **`java.lang.Thread`**: For each accepted client connection, a new `Thread` is created. This allows the server to handle multiple clients at the same time without blocking. The main server loop can immediately go back to waiting for new connections after spawning a new thread for the current client.
-   **`ClientHandler` (Runnable)**: A nested class `ClientHandler` that implements the `Runnable` interface contains the logic for handling a single client's request. An instance of this class is passed to the `Thread` constructor.

This multi-threaded approach ensures that the server is scalable and responsive to multiple users.

## Single-threaded Server Configuration

A single-threaded version of the server is also provided in `SocketServerSingleThread.java`. This server handles one client connection at a time, processing each request sequentially. It reuses the same `handleClientRequest` logic as the concurrent server, ensuring consistent behavior.

-   **`SocketServerSingleThread`**: Listens for client connections and processes them one by one in a single thread. Each client is handled to completion before the next connection is accepted.
-   **Reuse of Logic**: The single-threaded server internally creates a `ClientHandler` instance and calls its `handleClientRequest` method to process client requests, ensuring the same business logic as the multi-threaded version.

## User Guide

### How to Compile and Run

You will need a Java Development Kit (JDK) installed.

1.  **Compile the code:**
    Open a terminal, navigate to the `src` directory, and run the following commands:
    ```sh
    javac SocketServer.java
    javac SocketServerSingleThread.java
    javac SocketClient.java
    ```

2.  **Run the Server (Multi-threaded):**
    In the same terminal, run:
    ```sh
    java SocketServer
    ```
    The server will start and wait for client connections.

3.  **Run the Server (Single-threaded):**
    In the same terminal, run:
    ```sh
    java SocketServerSingleThread
    ```
    The single-threaded server will start and handle one client at a time.

4.  **Run the Client:**
    Open a *new* terminal window, navigate to the `src` directory, and run:
    ```sh
    java SocketClient
    ```
    The client will prompt you to enter a number.

### Testing

-   **Valid Input**: Enter a number between 1 and 100 to see the normal operation.
-   **Invalid Input**: Enter a number outside the 1-100 range (e.g., 0 or 101) to test the server's shutdown functionality.
-   **Concurrency**: You can run multiple instances of the client to see the server handling them concurrently.

### Unit Tests

The project includes unit tests for the server's core logic to ensure its correctness.

-   **Framework**: The tests are written using JUnit 5.
-   **Test File**: `SocketServerTest.java` contains tests for the `handleClientRequest` method in `SocketServer.java`.
-   **Test Cases**:
    -   `testHandleClientRequest_ValidNumber`: Checks the server's response for a valid input number (1-100).
    -   `testHandleClientRequest_NumberTooLow`: Checks the server's response for a number less than 1.
    -   `testHandleClientRequest_NumberTooHigh`: Checks the server's response for a number greater than 100.

#### How to Run the Unit Tests

If you are using an IDE like IntelliJ IDEA or Eclipse, you can run the tests by opening the `SocketServerTest.java` file and clicking the "run" button next to the class definition. The IDE will automatically download the necessary JUnit dependencies if they are not already present.
