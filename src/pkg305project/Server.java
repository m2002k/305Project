package pkg305project;

import java.sql.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) {
        final int port = 12345;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started. Listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from " + clientSocket.getInetAddress());

                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {

        private final Socket clientSocket;

        String database = "jdbc:sqlserver://mkser.database.windows.net:1433;database=volunteering;user=MK@mkser;password=Cpit3055"
                + ";encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (java.sql.Connection connection = DriverManager.getConnection(database); java.sql.Statement statement = connection.createStatement();) {
                try (
                        ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream()); ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream()); PrintWriter writer = new PrintWriter(new FileWriter("users.txt", true))) {

                    boolean teamp = (boolean) ois.readObject();
                    if (teamp) {

                        while (true) {
                            boolean found;
                            String phone = (String) ois.readObject();
                            String s1 = "select * from donor where phone_number = " + phone;
                            ResultSet rs = statement.executeQuery(s1);
                            if (rs.next()) {
                                found = true;
                                oos.writeObject(found);
                                break;
                            } else {
                                found = false;
                                oos.writeObject(found);
                            }
                        }

                    } else {
                        while (true) {
                            boolean found;
                            String phone = (String) ois.readObject();
                            String s1 = "select * from donor where phone_number = " + phone;
                            ResultSet rs = statement.executeQuery(s1);
                            if (rs.next()) {
                                found = true;
                                oos.writeObject(found);
                                break;
                            } else {
                                found = false;
                                oos.writeObject(found);
                            }
                        }

                    }

                    synchronized (Server.class) {
                        writer.println("");
                        writer.flush();
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
