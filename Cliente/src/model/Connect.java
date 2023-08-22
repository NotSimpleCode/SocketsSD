package model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import connections.Options;

public class Connect {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public void setColor(int color) throws IOException {
        this.out.writeInt(color);
    }

    public Socket getSocket() {
        return socket;
    }

    public Connect() {
        try {
            this.socket = new Socket("localhost", 4900);
            this.in = new DataInputStream(this.socket.getInputStream());
            this.out = new DataOutputStream(this.socket.getOutputStream());

        } catch (Exception e) {
            System.out.println("El servidor se encuentra en mantenimiento...");
        }

    }

    public Connect(int port) {
        try {
            this.socket = new Socket("localhost", port);
            this.in = new DataInputStream(this.socket.getInputStream());
            this.out = new DataOutputStream(this.socket.getOutputStream());
        } catch (Exception e) {
        }

    }

    public Connect(int port, String host) {
        try {
            this.socket = new Socket(host, port);
            this.in = new DataInputStream(this.socket.getInputStream());
            this.out = new DataOutputStream(this.socket.getOutputStream());
        } catch (Exception e) {
        }

    }

    public Connect(String host) {
        try {
            this.socket = new Socket(host, 4900);
            this.in = new DataInputStream(this.socket.getInputStream());
            this.out = new DataOutputStream(this.socket.getOutputStream());
        } catch (Exception e) {
        }

    }

    public String readMsg() throws IOException {
        String textoServer = "vacio";

        try {
            this.out.writeUTF(Options.MSG.toString());
            try {
                textoServer = readJson();
            } catch (Exception e) {
                System.out.println("Actualmente estas en cola de espera, intenta de nuevo en 30 segundos");
            }
        } catch (Exception e) {
            System.out.println("El servidor ha entrado en mantenimiento...");
            this.socket.close();
        }
        return textoServer;
    }

    public String readJson() throws IOException {
        String json = "";
        try {
            json = this.in.readUTF();
        } catch (Exception e) {
            System.out.println("El servidor ha entrado en mantenimiento...");
            this.socket.close();
        }
        return json;
    }

    public void sendJson(String json) throws IOException {
        try {
            this.out.writeUTF(json);
        } catch (Exception e) {
            System.out.println("El servidor ha entrado en mantenimiento...");
            this.socket.close();
        }
    }

    public void closeConnect() {
        try {
            this.out.writeUTF(Options.CLOSE.toString());
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isQuery() throws IOException {

        try {
            String response = this.in.readUTF();
            if (response.equalsIgnoreCase("Has logrado ingresar a nuestros servicios!")) {
                return false;
            } else {
                System.out.println(response);
            }

        } catch (IOException e) {
            System.out.println("no se recibio respuesta del servidor");
            this.socket.close();
        }
        return true;
    }

    public String getConnectionStatus() {
        if (socket != null && socket.isConnected()) {
            return "Conexión exitosa";
        } else {
            return "El servidor se encuentra en mantenimiento...";
        }
    }

}
