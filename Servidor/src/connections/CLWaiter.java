package connections;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class CLWaiter extends Thread {

    private Socket socket;
    private DataOutputStream out;
    private int pos;
    private boolean redirect = false;

    public CLWaiter(Socket socket, int pos) throws IOException {
        this.socket = socket;
        this.out = new DataOutputStream(this.socket.getOutputStream());
        this.pos = pos;
    }

    @Override
    public void run() {
        try {
            // envia un mensaje
            //this.out.writeUTF("Gracias por esperar, posicion en cola : " + pos);
            // TO DO
            System.out.println("Alguien esta en espera");

            do {
                if (redirect) {
                    redirectToServer();
                }
                //Thread.sleep(1000); //opcion a quitar
            } while (!redirect);

        } catch (Exception e) {
            System.out.println("Se desconecto alguien que estaba en espera");
        }

    }

    protected void redirectToServer() {
        try {
            this.out.writeUTF("redirect");
        } catch (IOException e) {
            System.out.println("No se pudo redirigir al cliente");
            e.printStackTrace();
        }
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int i) {
        this.pos = i;
    }

    public Socket getSocket() {
        return socket;
    }

    public void redirect() {
        this.redirect = true;
    }

}
