package pkg305project;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server{
    public static void main(String[] args) {
        final int port = 12345;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started. Listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from " + clientSocket.getInetAddress());
                
                new Thread(new ClientHandler(clientSocket)).start();}
        } catch (IOException e) {
            e.printStackTrace();}
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;}

        @Override
        public void run() {
            try (
                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                PrintWriter writer = new PrintWriter(new FileWriter("users.txt", true))
            ) {
                User user = (User) ois.readObject();
                System.out.println("Received user: " + user);

                synchronized (Server.class) {
                    writer.println(user);
                    writer.flush();}
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();}
        }}}

