package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import connections.CLWorker;
import presenter.ServerPresenter;

public class ServerView extends JFrame implements ActionListener{

	Chat chat;

	Object game;


	public ServerPresenter getPresenter() {
		return presenter;
	}

	private ServerPresenter presenter;

	public ServerView() {
		super();
		presenter = new ServerPresenter();
		this.setSize(700,700);
		

		initComponents();

		setListeners();

	}

	public void setPresenter(ServerPresenter presenter) throws IOException {
		this.presenter = presenter;
		chat.getInfoServer().setText(presenter.getServerInfo());
	}

	private void setListeners() {
		//this.chat.setListeners(this);
	}

	public void start(){
		setVisible(true);
	}

	public void updateMSG(){
		StringBuilder str = new StringBuilder();
		ArrayList<String> msg = presenter.readMSG();

		if(msg != null){
			for (int i = 0; i < msg.size(); i++) {
				str.append(msg.get(i) + "\n");
			}
			if (str != null) {
				chat.getTxtGetMSG().setText(str.toString());
			}
		}
		
	}

	private void initComponents() {
		
		chat = new Chat();
		try {
			((Container) game).add(chat,BorderLayout.EAST);
			System.out.println("se cargo un juego previamente para el server");
			((Component) game).setVisible(true);
		} catch (Exception e) {
			game = new Game();
			((Container) game).add(chat,BorderLayout.EAST);
		}
		
	}


	public void showMessage(String msg) {
		System.out.println(msg);
	}

	public void setGame(Object game) {
		this.game = game;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equalsIgnoreCase("send")) {
			try {
				presenter.sendMSG(chat.getTxtSendMSG().getText());
			} catch (Exception e1) {
				showInfo("Conexion no lograda");
			}
		}
		
	}

	public void showInfo(String info){
		JOptionPane.showMessageDialog(null, info, info, JOptionPane.INFORMATION_MESSAGE);
	}

    public int showYes() {
        return JOptionPane.showConfirmDialog(null, "Desea intentar conectar¿", "Conexion No establecida", JOptionPane.OK_OPTION);
    }

    public void showClients(ArrayList<CLWorker> inService) {
		chat.setClients(inService);
    }

}
