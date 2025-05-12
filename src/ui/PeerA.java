package ui;

import util.UDPconnection;
import java.util.Scanner;

public class PeerA {
    public static void main(String[] args) {
        UDPconnection udpConnection = UDPconnection.getInstance();
        udpConnection.setPort(5000);
        udpConnection.start();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Soy el Peer A. ¿A quién quieres enviarle un mensaje?");
            System.out.println("1. B");
            System.out.println("2. C");
            System.out.print("Opción: ");
            int option = Integer.parseInt(scanner.nextLine());

            String destIp = "127.0.0.1";
            int destPort = 0;
            switch (option) {
                case 1 -> {
                    destPort = 5001;
                    destIp = "127.0.0.1";
                }
                case 2 -> {
                    destPort = 5002;
                    destIp = "192.168.69.179";
                }
                default -> {
                    System.out.println("Opción no válida.");
                    continue;
                }
            };
            System.out.print("Escribe tu mensaje: ");
            String message = "[A]: " + scanner.nextLine();
            udpConnection.sendMessage(message, destIp, destPort);
        }
    }
}
