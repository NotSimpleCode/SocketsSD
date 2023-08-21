package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import presenter.ClientPresenter;

public class JClient extends JDialog implements ActionListener {

    JPanel top;
    JPanel mid;
    JPanel down;

    JButton selectColor;
    JButton run;
    JButton stop;
    JButton cancel;

    JButton choosedColor;

    JLabel title;
    JLabel IP;
    JLabel port;
    JLabel color;

    JTextField portField;
    JTextField ipField;

    ColorChooser viewCli;

    Chat chat;

    Object game;

    private ClientPresenter presenter;

    public JClient() {

        setSize(400, 310);
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

        top.setPreferredSize(new Dimension(1,40));
        
        mid.setPreferredSize(new Dimension(1,200));

        down.setPreferredSize(new Dimension(1,70));

        initComponents();


        add(top, BorderLayout.NORTH);

        add(mid, BorderLayout.CENTER);

        add(down, BorderLayout.SOUTH);

        //this.setVisible(true);
    }

    public void setPresenter(ClientPresenter presenter) {
        this.presenter = presenter;
    }

    public void initComponents() {

        chat = new Chat();

        title = new JLabel("Client");
        title.setBounds(120, 10, 70, 30);

        IP = new JLabel("IP:");
        IP.setBounds(70, 20, 20, 20);

        port = new JLabel("Port:");
        port.setBounds(IP.getX(), IP.getY() + 40, 70, 20);

        color = new JLabel("Color");
        color.setBounds(port.getX(), port.getY() + 40, 70, 20);

        choosedColor = new JButton();
        choosedColor.setBounds(color.getX() + color.getWidth() + 10, color.getY(), 40, 40);
        choosedColor.setBackground(Color.BLACK);
        

        selectColor = new JButton("Select");
        selectColor.setBounds(choosedColor.getX() + choosedColor.getWidth() + 40, color.getY(), 70, 30);

        run = new JButton("Run");
        run.setBounds(40, 10, 70, 30);
        run.setBackground(Color.green);

        stop = new JButton("Stop");
        stop.setBounds(run.getX() + 100, run.getY(), run.getWidth(), run.getHeight());

        cancel = new JButton("Cancel");
        cancel.setBounds(stop.getX() + 90, run.getY(), run.getWidth() + 10, run.getHeight());

        ipField = new JTextField();
        ipField.setBounds(IP.getX() + 70, IP.getY(), 90, 20);

        portField = new JTextField();
        portField.setBounds(port.getX() + 70, port.getY(), 40, 20);

        listeners();

        add();


    }

    public void setGame(Object game) {
        this.game = game;
    }

    private void listeners() {
        selectColor.setActionCommand("setColor");
        run.setActionCommand("runClient");
        stop.setActionCommand("stopClient");
        cancel.setActionCommand("cancelClient");

        selectColor.addActionListener(this);
        run.addActionListener(this);
        stop.addActionListener(this);
        cancel.addActionListener(this);


        this.chat.setListeners(this);
    }

    private void add() {


        top.add(title);

        mid.add(IP);
        mid.add(port);
        mid.add(color);
        mid.add(choosedColor);
        mid.add(selectColor);
        mid.add(ipField);
        mid.add(portField);

        down.add(run);
        down.add(stop);
        down.add(cancel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase("setColor")) {
            viewCli = new ColorChooser(this);
        }
        if (e.getActionCommand().equalsIgnoreCase("send")) {
            try {
                presenter.sendMSG(chat.getTxtSendMSG().getText());
            } catch (Exception e1) {
                showInfo("Conexion no lograda");
            }
        }

        if (e.getActionCommand().equalsIgnoreCase("cancelClient")) {
            this.setVisible(false);
        }

        if (e.getActionCommand().equalsIgnoreCase("stopClient")) {
            try {
                presenter.stopClient();
            } catch (Exception e1) {
                showInfo("No se pudo detener el cliente");
            }
        }

        if (e.getActionCommand().equalsIgnoreCase("runClient")) {
            try {
                
                connectTo();

                if (presenter.isConexion()) {

                    setColor();

                    buildGame();
                } else {
                    retryConn();
                }

            } catch (Exception e1) {
                showInfo("No aceptado");
            }
        }

    }

    private void setColor() throws IOException {
        presenter.setColor(this.choosedColor.getBackground().getRGB());
    }

    private void retryConn() {
        showInfo("ESTAS SIN CONEXION!!!");
        showInfo("no hay conexion");
        if (showYes() == 0) {
            presenter.retryCon();
        }
    }

    private void buildGame() {
        this.setVisible(false);
        try {
            //((Component) game).setVisible(false);
            ((Container) game).add(chat, BorderLayout.EAST);
            ((Component) this.game).setName("CLIENT");
            ((Component) this.game).setVisible(true);
            System.out.println("Se ha cargado un juego previamente");
        } catch (Exception e2) {
            this.game = new Game();
            ((Container) game).add(chat, BorderLayout.EAST);
        }
    }

    private void connectTo() {
        try {
            int port = 0;
            try {
                port = Integer.parseInt(portField.getText());
            } catch (Exception e4) {
                showInfo("Se usaran los valores por defecto para el puerto");
            }
            presenter.connect(ipField.getText(), port);
        } catch (Exception e3) {
            showInfo("Conexion No Valido");
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

    public void startGame() {
        try {
            ((Component) this.game).setVisible(true);
        } catch (Exception e) {
            showInfo("No existe juego para iniciar");
        }
    }

    public void setColor(Color color2) {
        this.choosedColor.setBackground(color2);
    }
}
