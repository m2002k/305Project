package pkg305project;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        final String SIP = "localhost";
        final int Server_port = 12345;

        try (Socket socket = new Socket(SIP, Server_port);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {


            oos.flush();
            oos.close();
            ois.close();

            System.out.println("User sent successfully.");

        } catch (IOException e) {
            e.printStackTrace();}
    }
    
public static boolean isPasswordValid(String password) {
        boolean hasNumber = false;
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;

        try {
            if (password == null) {
                throw new IllegalArgumentException("Password cannot be null.");
            }

            for (char c : password.toCharArray()) {
                if (Character.isDigit(c)) {
                    hasNumber = true;
                } else if (Character.isUpperCase(c)) {
                    hasUpperCase = true;
                } else if (Character.isLowerCase(c)) {
                    hasLowerCase = true;
                }

                // Optimization: If all conditions are already met, exit the loop
                if (hasNumber && hasUpperCase && hasLowerCase) {
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }

        return hasNumber && hasUpperCase && hasLowerCase;
    }

public static boolean isValidPhoneNumber(String phoneNumber) {
        try {
            if (phoneNumber == null || phoneNumber.length() != 10) {
                throw new IllegalArgumentException("Phone number must be 10 digits long.");
            }
            if (!phoneNumber.startsWith("05")) {
                throw new IllegalArgumentException("Phone number must start with '05'.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }}


