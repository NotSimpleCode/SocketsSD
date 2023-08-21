package presenter;

import java.io.IOException;
import java.util.ArrayList;

import connections.CLWorker;
import connections.Server;
import view.JServer;

public class ServerPresenter {

	private JServer view;
	private Server model;
	
	public ServerPresenter(JServer view, Server model) {
		super();
		this.view = view;
		this.model = model;
	}
	
	public void setView(JServer view) {
        this.view = view;
    }

    public void setModel(Server model) {
        this.model = model;
    }

    public ServerPresenter() {
    }

    public void sendMSG(String text) throws IOException {
        
    }

    public ArrayList<String> readMSG() {
        return model.getMsg();
    }
    
	public String getServerInfo() throws IOException{
        return "Port : " + model.getServerSocket().getLocalPort() + " Running ON: localhost";
    }

    /**
     * Original: actualizar el array de los mensajes y hace update a la vista
     * sobreescribir para saber que hacer con el json
     * 
     * @param json texto en cualquier formato, preferible json para objetos
     */
    public void updateMSG(String json) {
 
    	model.getMsg().add(json);
        view.updateMSG();
        
        
    }

    public void updateClients(ArrayList<CLWorker> inService) {
        view.showClients(inService);
    }

	public JServer getView() {
		return view;
	}

	public Server getModel() {
		return model;
	}

    public boolean isConexion() {
        if (model.getServerSocket() != null){
            return true;
        }
        return false;
    }

    public void connect(int port) {
        if (port == 0) {
            this.model = new Server();
        } else {
            this.model = new Server(port);
        }
    }

    public void startListen() {
        this.model.setPresenter(this);
        try {
            this.model.startListening();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        model.stop();
    }

    public void forceStop() {
        try {
            model.getServerSocket().close();
            model = null;
            view.showInfo("Se ha detenido el server con exito!");
        } catch (IOException e) {
            e.printStackTrace();
            view.showInfo("No se pudo detener el servidor");
        }
    }

    public void setSupport() {
        model.setSupport();
    }
    
}
