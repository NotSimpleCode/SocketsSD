package presenter;

import java.io.IOException;

import model.Connect;
import view.JClient;

public class ClientPresenter {

	private JClient view;
	private Connect model;
	
	public ClientPresenter(JClient view, Connect model) {
		super();
		this.view = view;
		this.model = model;
	}
	
	public void setView(JClient view) {
        this.view = view;
    }

    public void setModel(Connect model) {
        this.model = model;
    }

    public ClientPresenter() {
    }


    public void sendMSG(String text) throws IOException {
        if(model.getSocket() != null && !model.getSocket().isClosed()){
            this.model.sendJson(text);
        }else {
            view.showInfo("no hay conexion");
            if (view.showYes() == 0) {
                this.model = new Connect();
            }
        }
    }

    public void setColor(int i) throws IOException {
        model.setColor(i);
    }

    public boolean isConexion(){
        return model.getSocket() != null;
    }

    public void retryCon() {
        this.model = new Connect();
    }

    public void connect(String ip, int port) {
        if (ip.equalsIgnoreCase("")) {
            if (port == 0) {
                retryCon();
            }else{
                model = new Connect(port);
            }
        }else{
            if (port == 0) {
                model = new Connect(ip);
            }else{
                model = new Connect(port, ip);
            }
        }
    }

    public void stopClient() {
        try {
            
            model.getSocket().close();
            model = null;

            view.showInfo("Se pudo cerrar la conexion!");
        } catch (IOException e) {
            view.showInfo("No se pudo eliminar el cliente");
        }
    }
    
}