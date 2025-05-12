package ui;

import util.UDPconnection;
import java.util.Scanner;

public class PeerC {
    public static void main(String[] args) {
        UDPconnection udpConnection = UDPconnection.getInstance();
        udpConnection.setPort(5002);
        udpConnection.start();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Soy el Peer C. ¿A quién quieres enviarle un mensaje?");
            System.out.println("1. A");
            System.out.println("2. B");
            System.out.print("Opción: ");
            int option = Integer.parseInt(scanner.nextLine());

            String destIp = "127.0.0.1";
            int destPort = 0;
            switch (option) {
                case 1 -> {
                    destPort = 5000;
                    destIp = "192.168.69.66";
                }
                case 2 -> {
                    destPort = 5001;
                    destIp = "127.0.0.1";
                }
                default -> {
                    System.out.println("Opción no válida.");
                    continue;
                }
            };

            System.out.print("Escribe tu mensaje: ");
            String message = "[C]: " + scanner.nextLine();
            udpConnection.sendMessage(message, destIp, destPort);
        }
    }
}
