package connections;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import presenter.ServerPresenter;

public class Server {

    boolean running = true;

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    private ServerSocket serverSocket;
    private final int CAPACIDAD = 30;
    private ArrayList<CLWorker> inService = new ArrayList<>();
    private ArrayList<Thread> inQuery = new ArrayList<>();
    private ArrayList<String> msg = new ArrayList<>();
    private ServerPresenter presenter;

    public Server() {
        try {
            this.serverSocket = new ServerSocket(4900);
            this.serverSocket.setSoTimeout(1000000000);
        } catch (IOException e) {
            System.out.println("puerto del servidor ocupado");
        }
    }

    public Server(int port, int timeOut) {
        try {
            this.serverSocket = new ServerSocket(port);
            this.serverSocket.setSoTimeout(timeOut);
        } catch (IOException e) {
            System.out.println("puerto del servidor ocupado");
        }
    }

    public Server(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            this.serverSocket.setSoTimeout(1000000000);
        } catch (IOException e) {
            System.out.println("puerto del servidor ocupado");
        }
    }

    public void startListening() throws IOException {

        System.out.println("Servidor ejecutandose...");

        Thread t1 = new Thread(){
            @Override
            public void run(){
                while (running) {

                    try {
                        Socket cliente = toReceive();
                        checkAll();
      
                        addClient(cliente);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println(e);
                    }
                    
                }
            }
        };
        t1.start();
        

    }

    public void addClient(Socket cliente) throws IOException {
        CLWorker clwTemp;
        Thread th1Temp;
        if (cliente != null) {
            clwTemp = new CLWorker(cliente);
            clwTemp.setServer(this);

            inService.add(clwTemp);

            th1Temp = new Thread(clwTemp);
            inQuery.add(th1Temp);

            th1Temp.start();

            System.out.println("Nuevo Cliente conectado! -- Clientes actuales : " + inQuery.size());

        }
    }

    public Socket toReceive() throws IOException {
        Socket cliente = null;
        try {
            cliente = this.serverSocket.accept();
        } catch (SocketTimeoutException e) {
            System.out.println(
                    "Paso el tiempo limite en segundos sin recibir peticiones, revisando colas de servicio y de espera...");
            presenter.updateClients(inService);
        }
        return cliente;
    }

    public void checkAll() {
        for (int i = 0; i < inQuery.size(); i++) {
            if (!inQuery.get(i).isAlive() || !inService.get(i).isConexion()) {
                inQuery.remove(i);
                inService.remove(i);
                System.out.println("Un Cliente desconectado! -- Clientes actuales : " + inQuery.size());
                presenter.updateClients(inService);

            }
        }
    }

    public void sortOut(Socket cliente) throws IOException {
        CLWaiter clwaTemp;
        if (cliente != null || inQuery.size() > 0) {

            if (inQuery.size() > 0 && CAPACIDAD > inService.size()) {
                inService.add(new CLWorker(cliente));
                inQuery.get(0).start();
                inQuery.remove(0);
                System.out.println("Nuevo Cliente conectado! -- Clientes actuales : " + inService.size());
            } else {
                if (cliente != null) {
                    clwaTemp = new CLWaiter(cliente, inQuery.size());
                    new Thread(clwaTemp).start();
                }
            }
        }
    }

    public ArrayList<String> getMsg() {
        return msg;
    }

    /**
     * Original: agrega a el array los mensajes y hace update al presenter
     * sobreescribir para saber que hacer con el json
     * 
     * @param json texto en cualquier formato, preferible json para objetos
     */
    public void addMsg(String json) {
        presenter.updateMSG(json);
    }

    public void setPresenter(ServerPresenter presenter) {
        this.presenter = presenter;
    }

    public void updateClients() {
        presenter.updateClients(inService);
    }

    public String getIP() {
        return "localhost";
    }

    public void stop() {
        running = false;
    }

}
