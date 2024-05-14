package pkg305project;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws ClassNotFoundException {
        final String SIP = "localhost";
        final int Server_port = 12345;

        try (Socket socket = new Socket(SIP, Server_port); ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream ois = new ObjectInputStream(socket.getInputStream()); Scanner scanner = new Scanner(System.in)) {
            char login;

            while (true) {
                System.out.print("Enter L for log-in and C to Create Account:");
                login = scanner.nextLine().charAt(0);
                if ((login == 'c' || login == 'C') || (login == 'l' || login == 'L')) {
                    break;
                }
            }

            if (login == 'c' || login == 'C') {
                oos.writeObject(true);
                CreateAccount(oos, ois, scanner);
            } else {
                oos.writeObject(false);
                LingIn(oos, ois, scanner);
            }

            oos.flush();
            oos.close();
            ois.close();

            System.out.println("User sent successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void CreateAccount(ObjectOutputStream oos, ObjectInputStream ois, Scanner scanner) throws IOException, ClassNotFoundException {
        Donor donor;
        String phone;
        String Passeword;
        String Fname;
        String Lname;
        String city = null;
        while (true) {
            while (true) {
                System.out.print("Enter Phone Number:");
                phone = scanner.nextLine();
                if (isValidPhoneNumber(phone)) {
                    break;
                }
            }

            oos.writeObject(phone);
            boolean found = (boolean) ois.readObject();;
            if (!found) {
                break;
            }
            System.out.println("Phone is used:");
        }

        while (true) {
            while (true) {
                System.out.print("Enter Passeword:");
                Passeword = scanner.nextLine();
                if (isPasswordValid(Passeword)) {
                    break;
                }
            }
            System.out.print("repeat Passeword:");
            String teamp = scanner.nextLine();
            if (teamp.equals(Passeword)) {
                break;
            }
        }

        while (true) {
            System.out.print("Enter first name:");
            Fname = scanner.nextLine();
            if (Fname.length() <= 29) {
                break;
            }
            System.out.println("name is too long:");
        }

        while (true) {
            System.out.print("Enter last name:");
            Lname = scanner.nextLine();
            if (Lname.length() <= 29) {
                break;
            }
            System.out.println("name is too long:");
        }

        while (true) {
            System.out.println("Choose city name:");
            System.out.println("1-Makkah");
            System.out.println("2-Jeddah");
            System.out.println("3-Riyadh");
            try {
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        city = "Makkah";
                        break;
                    case 2:
                        city = "Jeddah";
                        break;
                    case 3:
                        city = "Riyadh";
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
            }

            if (city != null) {
                break;
            }
        }
        donor = new Donor(Fname, Lname, city, phone, Passeword);
        oos.writeObject(donor);
    }

    public static void LingIn(ObjectOutputStream oos, ObjectInputStream ois, Scanner scanner) throws IOException, ClassNotFoundException {
        String phone;
        String Passeword;
        while (true) {
            while (true) {
                System.out.print("Enter Phone Number:");
                phone = scanner.nextLine();
                if (isValidPhoneNumber(phone)) {
                    break;
                }
            }

            oos.writeObject(phone);
            boolean found = (boolean) ois.readObject();
            if (found) {
                break;
            }
            System.out.println("Phone not found:");
        }
        String PWord=(String) ois.readObject();
        while (true) {
        while (true) {
            System.out.print("Enter Passeword:");
            Passeword = scanner.nextLine();
            if (isPasswordValid(Passeword)) {
                break;
            }
        }
        if (PWord.equals(Passeword)){
             oos.writeObject(true);
             break;
        }
          System.out.println("Passeword is wrong:");
        }

    }

    public static boolean isPasswordValid(String password) {
        boolean hasNumber = false;
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;

        try {
            if (password == null) {
                throw new IllegalArgumentException("Password cannot be null.");
            } else if (password.length() > 12 || password.length() < 8) {
                throw new IllegalArgumentException("Password must be between 8-12 characters long");
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
            if (!(hasNumber && hasUpperCase && hasLowerCase)) {
                System.out.println("Password must has Number , UpperCase ,LowerCase");
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
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
