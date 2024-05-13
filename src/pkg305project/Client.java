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
            char login;
            String phone;
            
            while (true) {
                 System.out.print("Enter L for log-in and C to Create Account:");
                 login = scanner.nextLine().charAt(0);
             if ((login=='c'||login=='C')||(login=='l'||login=='L')){
            break;}
                } 
        
             while (true) {
                 System.out.print("Enter Phone Number:");
                 phone =scanner.nextLine();
                         if (isValidPhoneNumber(phone)){
             break;}
           }

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

public static boolean isValidPhoneNumber(String input) {
        try {
            if (input.length() != 10) {
                System.out.println("Input must be 10 characters long.");
                throw new IllegalArgumentException("Input must be 10 characters long.");
            }
            if (!input.startsWith("05")) {
                System.out.println("Input must start with '05'.");
                throw new IllegalArgumentException("Input must start with '05'.");
            }
            for (char c : input.toCharArray()) {
                if (!Character.isDigit(c)) {
                  System.out.println("Input must contain only digits.");
                    throw new IllegalArgumentException("Input must contain only digits.");
                }
            }
            Long.parseLong(input); // Try parsing as a long to ensure all characters are digits
            return true;} catch (IllegalArgumentException e) {
            return false;}}
}


