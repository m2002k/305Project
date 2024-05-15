package pkg305project;

import java.sql.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
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

                    Donor donor;
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
                            } else {
                                found = false;
                                oos.writeObject(found);
                                break;
                            }
                        }
                        donor = (Donor) ois.readObject();
                        String in = "INSERT INTO donor (phone_number,Fname,Lname,city,street,PWord) "
                                + "VALUES('" + donor.getPhoneNumber() + "','" + donor.getFname() + "','" + donor.getLname()
                                + "','" + donor.getCity() + "','" + donor.getStreet() + "','" + donor.getPassword() + "')";
                        statement.executeUpdate(in);

                    } else {
                        while (true) {
                            String phone;
                            String Pword;
                            ResultSet rs;
                            while (true) {
                                boolean found;
                                phone = (String) ois.readObject();
                                String s1 = "select * from donor where phone_number = " + phone;
                                rs = statement.executeQuery(s1);
                                if (rs.next()) {
                                    found = true;
                                    oos.writeObject(found);
                                    Pword = rs.getString("PWord");
                                    Pword = Pword.replaceAll("\\s+$", "");//remove extar space
                                    oos.writeObject(Pword);
                                    break;
                                } else {
                                    found = false;
                                    oos.writeObject(found);
                                }
                            }//collect donor info form database + remove extar spaces
                            String Fname = rs.getString("Fname").replaceAll("\\s+$", "");
                            String Lname = rs.getString("Lname").replaceAll("\\s+$", "");
                            String city = rs.getString("city").replaceAll("\\s+$", "");

                            donor = new Donor(Fname, Lname, city, phone, Pword);
                            teamp = (boolean) ois.readObject();
                            if (teamp) {
                                rs.close();
                                break;
                            }
                        }
                    }
                    String s1 = "select * from cities where city = ?";
                    PreparedStatement pstmt1 = connection.prepareStatement(s1);
                    pstmt1.setString(1, donor.getCity());
                    ResultSet rs1 = pstmt1.executeQuery();
                    ArrayList<Association> associations = new ArrayList<>();

                    try {
                        while (rs1.next()) {
                            String association = rs1.getString("association");
                            String s2 = "select * from Association where Phone_number = ?";
                            PreparedStatement pstmt2 = connection.prepareStatement(s2);
                            pstmt2.setString(1, association);
                            ResultSet rs2 = pstmt2.executeQuery();

                            try {
                                if (rs2.next()) {
                                    String name = rs2.getString("name").replaceAll("\\s+$", "");
                                    String cause = rs2.getString("cause").replaceAll("\\s+$", "");
                                    String password = rs2.getString("PWord").replaceAll("\\s+$", "");
                                    Association as = new Association(name, cause, association, password);
                                    as.addCity(donor.getCity());
                                    associations.add(as);
                                }
                            } finally {
                                rs2.close();
                                pstmt2.close();
                            }
                        }
                    } finally {
                        rs1.close();
                        pstmt1.close();
                    }
                    oos.writeObject(associations);

                    int id = generateUniqueId(connection);

                    oos.writeObject(id);
                    

                    clothing clothing = (clothing) ois.readObject();
                    String in = "INSERT INTO clothing (id,Type,Size) "
                            + "VALUES('" + clothing.getID() + "','" + clothing.getType() + "','" + clothing.getSize() + "')";
                    statement.executeUpdate(in);

                    String association = (String)ois.readObject();
                    
                    in = "INSERT INTO donate (donor,association,clothing) "
                            + "VALUES('" + donor.getPhoneNumber() + "','" + association + "','" + clothing.getID() + "')";
                    statement.executeUpdate(in);

                    synchronized (Server.class) {
                        writer.println(donor);
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

    private static int generateUniqueId(Connection connection) throws SQLException {
        Random random = new Random();
        int Id;
        boolean isUnique;

        do {
            Id = random.nextInt(1000000);
            isUnique = isIdUnique(connection, Id);
        } while (!isUnique);

        return Id;
    }

    private static boolean isIdUnique(Connection connection, int Id) throws SQLException {
        String query = "SELECT COUNT(*) FROM clothing WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, Id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        }
        return false;
    }
}
