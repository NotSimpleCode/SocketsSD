package connections;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class CLWorker implements Runnable {

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String msg;
    private Server server;
    private int color;
    boolean conexion = true;
    
    
    public boolean isConexion() {
        return conexion;
    }

    public Socket getSocket() {
        return socket;
    }

    public int getColor() {
        return color;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public CLWorker(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new DataInputStream(this.socket.getInputStream());
        this.out = new DataOutputStream(this.socket.getOutputStream());
    }

    public boolean chooseOP(boolean conexion) throws IOException {
        Options aux = Options.valueOf(this.in.readUTF());
        switch (aux) {
            case MSG:
                this.message();
                break;
            case CLOSE:
                conexion = false;
                break;
            default:
                break;
        }
        return conexion;
    }

    @Override
    public void run() {
        try {
            
            this.color = in.readInt();
            server.updateClients();
            conexion = true;
            read();

        } catch (Exception e1) {
            System.out.println("No se pudo ejecutar desde el server");
        }

    }

    public void read() throws IOException {
        while (conexion) {
            try {

                this.msg = readObj();
                
                //elegir que tipo de lectura es
                chooseTypeObj(msg);
                
            } catch (Exception e) {
                conexion = false;
                System.out.println("Se ha interrumpido la conexion de un cliente inesperadamente");
                server.checkAll();
                server.updateClients();
            }
        }
    }

    public void chooseTypeObj(String msgT) {
        server.addMsg(msgT);
    }

    public void message() throws IOException {
        this.out.writeUTF("Hola usuario");
    }
    
    public String readObj() throws IOException{
        return this.in.readUTF();
    }

    public String getHostName() {
        String txt = "Desconocida";
        try {
            txt = ""+InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return " IP: " + txt;
        
    }

}
