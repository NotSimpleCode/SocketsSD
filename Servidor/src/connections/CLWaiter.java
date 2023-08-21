package connections;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class CLWaiter extends Thread {

    private Socket socket;
    private DataOutputStream out;
    private int pos;

    public CLWaiter(Socket socket, int pos) throws IOException {
        this.socket = socket;
        this.out = new DataOutputStream(this.socket.getOutputStream());
        this.pos = pos;
    }

    @Override
    public void run() {
        try {
            this.out.writeUTF("Gracias por esperar, posicion en cola : "+pos);
            System.out.println("Alguien esta en espera");
        } catch (IOException e) {
            System.out.println("Se desconecto alguien que estaba en espera");
        }
        
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int i) {
        this.pos = i;
    }

}
