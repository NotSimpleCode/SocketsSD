package connections;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import presenter.ServerPresenter;
import view.JServer;

public class Server {

    boolean running = true;

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    private ServerSocket serverSocket;
    private int CAPACIDAD = 2;
    private ArrayList<CLWaiter> inQueue = new ArrayList<>();
    private ArrayList<CLWorker> inService = new ArrayList<>();
    private ArrayList<Thread> inRunThread = new ArrayList<>();
    private ArrayList<String> msg = new ArrayList<>();
    private ServerPresenter presenter;
    private boolean support = false;

    public void setSupport() {
        this.support = true;
    }

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

        if (support) {
            System.out.println("Servidor de respaldo ejecutandose...");

            Thread t3 = new Thread() {
                @Override
                public void run() {
                    while (true) {

                        try {

                            Socket cliente = toReceive();

                            checkAll();

                            addClient(cliente);

                        } catch (Exception e) {
                            e.printStackTrace();
                            System.err.println(e);
                        }

                    }
                    //cerrar el servidor
                }
            };
            t3.start();

        } else {
            System.out.println("Servidor ejecutandose...");

            Thread t1 = new Thread() {
                @Override
                public void run() {
                    while (running) {

                        try {
                            Socket cliente = toReceive();
                            checkAll();

                            chooseClientWay(cliente);

                        } catch (Exception e) {
                            e.printStackTrace();
                            System.err.println(e);
                        }

                    }
                }

            };
            t1.start();
        }

    }

    private void chooseClientWay(Socket cliente) throws IOException {

        if (isCapacity()) {
            addClient(cliente);
        } else {
            addToQueue(cliente);

            if (isLimitQueue()) {
                startSupportServer();
            }
        }
    }

    protected void startSupportServer() throws IOException {
        ServerPresenter presenter2 = new ServerPresenter();
        JServer view2 = new JServer();

        presenter2.setView(view2);

        view2.setPresenter(presenter2);

        view2.start();

        view2.buildGame();

        presenter2.connect(4800);
        view2.getChat().getInfoServer().setText(presenter2.getServerInfo());

        presenter2.setSupport();

        view2.hideMy();



    }

    protected boolean isLimitQueue() {
        return inQueue.size() > CAPACIDAD;
    }

    protected void addToQueue(Socket cliente) {
        try {
            inQueue.add(new CLWaiter(cliente, inQueue.size() + 1));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private boolean isCapacity() {
        return inService.size() < this.CAPACIDAD;
    }

    public void addClient(Socket cliente) throws IOException {
        CLWorker clwTemp;
        Thread th1Temp;
        if (cliente != null) {
            clwTemp = new CLWorker(cliente);
            clwTemp.setServer(this);

            inService.add(clwTemp);

            th1Temp = new Thread(clwTemp);
            inRunThread.add(th1Temp);

            th1Temp.start();

            System.out.println("Nuevo Cliente conectado! -- Clientes actuales : " + inRunThread.size());

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

    public void checkAll() throws IOException {
        for (int i = 0; i < inRunThread.size(); i++) {
            if (!inRunThread.get(i).isAlive() || !inService.get(i).isConexion()) {
                inRunThread.remove(i);
                inService.remove(i); // se remueve un cliente
                System.out.println("Un Cliente desconectado! -- Clientes actuales : " + inRunThread.size());

                if (isQueueClient()) {
                    // agregamos el cliente que este en cola si hay espacio en el servidor
                    addClient(inQueue.get(0).getSocket());
                    inQueue.remove(0);
                }

                presenter.updateClients(inService);

            }
        }
    }

    private boolean isQueueClient() {
        return inQueue.size() > 0;
    }

    private void sortOut(Socket cliente) throws IOException {
        CLWaiter clwaTemp;
        if (cliente != null || inRunThread.size() > 0) {

            if (inRunThread.size() > 0 && CAPACIDAD > inService.size()) {
                inService.add(new CLWorker(cliente));
                inRunThread.get(0).start();
                inRunThread.remove(0);
                System.out.println("Nuevo Cliente conectado! -- Clientes actuales : " + inService.size());
            } else {
                if (cliente != null) {
                    clwaTemp = new CLWaiter(cliente, inRunThread.size());
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
