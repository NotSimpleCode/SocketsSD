package connections;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;

    public Server(){
        try {
            this.serverSocket = new ServerSocket(4900);
        } catch (IOException e) {
            System.out.println("El puerto esta ocupado");;
        }
    }

    public void startListening() throws IOException{
        System.out.println("Servidor ejecutandose...");
        while (true) {
            Socket cliente = this.serverSocket.accept();
            System.out.println("Conexion entrante con IP y Puerto : " + cliente.getRemoteSocketAddress());
            new Thread(new CLWorker(cliente)).start();
            
        }
    }
}
