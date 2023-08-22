package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import connections.CLWorker;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import presenter.ServerPresenter;

public class JServer extends JDialog implements ActionListener {

    JPanel top;
    JPanel mid;
    JPanel down;

    JButton run;
    JButton stop;
    JButton cancel;

    JLabel title;
    JLabel IP;
    JLabel port;
    JLabel capacity;
    JLabel message;

    JTextField portField;
    JTextField ipField;
    JTextField capacityField;

    Chat chat;

    public Chat getChat() {
        return chat;
    }

    Object game;

    private ServerPresenter presenter;

    public JServer() {

        setSize(400, 330);
        setLocation(200, 100);
        setResizable(false);

        top = new JPanel();
        mid = new JPanel();
        down = new JPanel();

        top.setBackground(Color.gray);
        down.setBackground(Color.gray);

        top.setLayout(null);
        mid.setLayout(null);
        down.setLayout(null);

        top.setPreferredSize(new Dimension(1, 40));

        mid.setPreferredSize(new Dimension(1, 200));

        down.setPreferredSize(new Dimension(1, 70));

        initComponents();

        add(top, BorderLayout.NORTH);

        add(mid, BorderLayout.CENTER);

        add(down, BorderLayout.SOUTH);

    }

    public void setPresenter(ServerPresenter presenter) throws IOException {
        this.presenter = presenter;
    }

    public void initComponents() {

        chat = new Chat();

        title = new JLabel("Server");
        title.setBounds(120, 10, 70, 30);

        IP = new JLabel("IP:");
        IP.setBounds(70, 20, 20, 20);

        port = new JLabel("Port:");
        port.setBounds(IP.getX(), IP.getY() + 40, 70, 20);

        capacity = new JLabel("Capacity:");
        capacity.setBounds(port.getX(), port.getY() + 40, 100, 30);

        message = new JLabel("<<<MESSAGE>>>");
        message.setBackground(Color.red);
        message.setBounds(capacity.getX(), capacity.getY() + 40, 100, 30);

        run = new JButton("Run");
        run.setBounds(40, 10, 70, 30);
        run.setBackground(Color.green);

        stop = new JButton("Stop");
        stop.setBounds(run.getX() + 100, run.getY(), run.getWidth(), run.getHeight());

        cancel = new JButton("Cancel");
        cancel.setBounds(stop.getX() + 90, run.getY(), run.getWidth() + 10, run.getHeight());

        ipField = new JTextField();
        ipField.setBounds(IP.getX() + 70, IP.getY(), 140, 20);
        try {
            ipField.setText("" + InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            showInfo("NO se encontro IP para este equipo");
        }
        ipField.setEditable(false);

        portField = new JTextField();
        portField.setBounds(port.getX() + 70, port.getY(), 40, 20);

        capacityField = new JTextField();
        capacityField.setBounds(capacity.getX() + 70, capacity.getY(), 40, 20);

        listeners();

        add();

    }

    public void setGame(Object game) {
        this.game = game;
    }

    private void listeners() {
        run.setActionCommand("runServer");
        stop.setActionCommand("stopServer");
        cancel.setActionCommand("cancelServer");

        run.addActionListener(this);
        stop.addActionListener(this);
        cancel.addActionListener(this);

        // this.chat.setListeners(this);
    }

    private void add() {

        top.add(title);

        mid.add(IP);
        mid.add(port);
        mid.add(capacity);
        mid.add(ipField);
        mid.add(portField);
        mid.add(capacityField);
        mid.add(message);

        down.add(run);
        down.add(stop);
        down.add(cancel);
    }

    public void updateMSG() {
        StringBuilder str = new StringBuilder();
        ArrayList<String> msg = presenter.readMSG();

        if (msg != null) {
            for (int i = 0; i < msg.size(); i++) {
                str.append(msg.get(i) + "\n");
            }
            if (str != null) {
                chat.getTxtGetMSG().setText(str.toString());
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equalsIgnoreCase("runServer")) {

            buildGame();

            createServer();

            hideMy();
        }

        if (e.getActionCommand().equalsIgnoreCase("cancelServer")) {
            this.setVisible(false);
            System.exit(ABORT);
        }

        if (e.getActionCommand().equalsIgnoreCase("stopServer")) {
            try {
                presenter.stopServer();
                if (showForceStop() == 0) {
                    presenter.forceStop();
                }
            } catch (Exception e1) {
                showInfo("No se pudo detener el cliente");
            }
        }
    }

    private int showForceStop() {
        return 0;
    }

    public void buildGame() {
        try {
            ((Container) game).add(chat, BorderLayout.EAST);
            ((Component) this.game).setName("SERVER");
            ((Component) this.game).setVisible(true);
            showInfo("Se ha cargado un juego previamente");
        } catch (Exception e2) {
            this.game = new Game();
            ((Container) game).add(chat, BorderLayout.EAST);
        }
    }

    public void hideMy() {
        if (presenter.isConexion()) {
            this.setVisible(false);
            presenter.startListen();
        } else {
            showInfo("NO SE LOGRO CREAR EL SERVIDOR!!!");
        }
    }

    public void startGame() {
        try {
            ((Component) this.game).setVisible(true);
        } catch (Exception e) {
            showInfo("No existe juego para iniciar");
        }
    }

    private void setCapacity() {
        int capacity = 0;
        try {
            capacity = Integer.parseInt(capacityField.getText());
        } catch (Exception e) {
            showInfo("Se usara la capacidad por defecto");
        }
        presenter.setCapacity(capacity);
    }

    private void createServer() {
        try {
            int port = 0;
            try {
                port = Integer.parseInt(portField.getText());

                if (port == 4800) {
                    showInfo("Se usara el puerto 4900 porque el 4800 es el puerto para el servidor de soporte");
                    port = 4900;
                }

            } catch (Exception e4) {
                showInfo("Se usaran los valores por defecto para el puerto");
            }
            presenter.connect(port);
            setCapacity();
            chat.getInfoServer().setText(presenter.getServerInfo());

        } catch (Exception e3) {
            showInfo("Puerto NO valido");
        }
    }

    public void showInfo(String info) {
        JOptionPane.showMessageDialog(null, info, info, JOptionPane.INFORMATION_MESSAGE);
    }

    public int showYes() {
        return JOptionPane.showConfirmDialog(null, "Desea intentar conectar con valores recomendados¿",
                "Conexion No establecida",
                JOptionPane.OK_OPTION);
    }

    public void start() {
        setVisible(true);
    }

    public void showClients(ArrayList<CLWorker> inService) {
        chat.setClients(inService);
    }

}
