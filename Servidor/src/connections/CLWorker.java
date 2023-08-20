package connections;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class CLWorker implements Runnable {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public CLWorker(Socket socket) throws IOException{
        this.socket = socket;
        this.in = new DataInputStream(this.socket.getInputStream());
        this.out = new DataOutputStream(this.socket.getOutputStream());

    }

    public boolean chooseOP(boolean conexion) throws IOException{
        Options aux = Options.valueOf(this.in.readUTF());
        switch (aux) {
            case MSG:
                this.message();
                break;
            case CLOSE:
                conexion = false;
                this.socket.close();
                break;
            default:
                break;
        }
        return conexion;
    }


    @Override
    public void run() {
        NumClientes.NUMERO_CLIENTES += 1;
        System.out.println("Nuevo Cliente conectado! -- Clientes actuales : " + NumClientes.NUMERO_CLIENTES);
        boolean conexion = true;
        while (conexion) {
            try {
                conexion = this.chooseOP(conexion);
            } catch (Exception e) {
                System.out.println("Se ha interrumpido la conexion de un cliente inesperadamente");
                conexion = false;
            }
        }
        NumClientes.NUMERO_CLIENTES -= 1;
        System.out.println("Un Cliente se ha desconectado! -- Clientes actuales : " + NumClientes.NUMERO_CLIENTES);
        
    }

    public void message() throws IOException{
        this.out.writeUTF("Conexion con el servidor exitosa");
    }
    
}
