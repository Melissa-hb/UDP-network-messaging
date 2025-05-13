package util;

import java.io.IOException;
import java.net.*;
import java.util.function.Consumer;

public class UDPconnection extends Thread {
    private static UDPconnection instance;
    private DatagramSocket socket;
    private boolean running = false;
    private int port;

    private Consumer<String> onMessageReceived;

    private UDPconnection() {
        // Constructor privado (Singleton)
    }

    public static UDPconnection getInstance() {
        if (instance == null) {
            instance = new UDPconnection();
        }
        return instance;
    }

    public void setPort(int port) {
        this.port = port;
        try {
            socket = new DatagramSocket(this.port);
            System.out.println("Socket iniciado en puerto " + this.port);
        } catch (SocketException e) {
            System.err.println("Error al crear el socket: " + e.getMessage());
        }
    }

    public void setOnMessageReceived(Consumer<String> onMessageReceived) {
        this.onMessageReceived = onMessageReceived;
    }

    @Override
    public void run() {
        running = true;

        Thread receiver = new Thread(() -> {
            byte[] buffer = new byte[1024];
            // System.out.println("Escuchando mensajes en puerto " + port + "...");

            while (running) {
                try {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);

                    String message = new String(packet.getData(), 0, packet.getLength());
                    // String senderIp = packet.getAddress().getHostAddress();
                    

                    System.out.println(message);

                    if (onMessageReceived != null) {
                        onMessageReceived.accept(message);
                    }
                } catch (IOException e) {
                    if (running) {
                        System.err.println("Error al recibir: " + e.getMessage());
                    }
                }
            }
        });

        receiver.setDaemon(true);
        receiver.start();
    }

    public void sendAsyncMessage(String message, String ip, int port) {
        new Thread(() -> sendMessage(message, ip, port)).start();
    }

    public void sendMessage(String message, String ip, int port) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            byte[] data = message.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            socket.send(packet);
            System.out.println("Enviado a " + ip + ":" + port + " → " + message);
        } catch (IOException e) {
            System.err.println("Error al enviar mensaje: " + e.getMessage());
        }
    }

    public void closeConnection() {
        running = false;
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        System.out.println("Conexión cerrada.");
    }
}
