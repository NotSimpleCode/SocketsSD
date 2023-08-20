package connections;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connect {
    
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;


    public Socket getSocket(){
        return socket;
    }

    public Connect() {
        try {
            this.socket = new Socket("localhost",4900);
            this.in = new DataInputStream(this.socket.getInputStream());
            this.out = new DataOutputStream(this.socket.getOutputStream());
        } catch (Exception e) {
            System.out.println("El servidor se encuentra en mantenimiento...");;
        }
        
    }

    public String readMsg() throws IOException{
        String textoServer = "vacio";

        try {
            this.out.writeUTF(Options.MSG.toString());
            textoServer = this.in.readUTF();
        } catch (Exception e) {
            System.out.println("El servidor ha entrado en mantenimiento...");
            this.socket.close();
        }
        return textoServer;
    }

    public void closeConnect(){
        try {
            this.out.writeUTF(Options.CLOSE.toString());
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
