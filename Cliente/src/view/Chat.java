package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.plaf.FontUIResource;

public class Chat extends JPanel{

    JButton btnSendMSG;

    JTextArea txtGetMSG;
    JTextField txtSendMSG;
    JList<ImageIcon> listPaneInfo;


    JLabel infoServer;

    public Chat() {
        super();
        //this.setSize(400, 700-100);
        //this.setResizable(false);
        setLayout(null);
        setPreferredSize(new Dimension(350,1));
        setBackground(Color.gray);

        initComponents();


        this.add(listPaneInfo);
        this.add(txtGetMSG);
        this.add(txtSendMSG);
        this.add(btnSendMSG);
        this.add(infoServer);
        


        //this.setVisible(true);

    }

    public void setInfo(String info){
        infoServer.setText(info);
    }

    private void initComponents() {

       

        infoServer = new JLabel("Desconectado");

        btnSendMSG = new JButton("Send MSG");
        txtGetMSG = new JTextArea("MSG");
        txtSendMSG = new JTextField("MSG to Send");

        listPaneInfo = new JList<>();
        listPaneInfo.setBounds(70, 10, 240, 300);


        txtGetMSG.setBounds(70, 320 , 240, 100);

        txtSendMSG.setBounds(70,440 , 240, 30);
        
        btnSendMSG.setBounds(140, 500, 100, 40);

        infoServer.setBounds(200,640, 100, 30);
        
        Font f1 = new FontUIResource(Font.SANS_SERIF, Font.ITALIC, 8);
        infoServer.setFont(f1);


        setActionComands();
    }

    private void setActionComands() {
        btnSendMSG.setActionCommand("send");
    }

    public void setListeners(JClient jClient) {
        this.btnSendMSG.addActionListener(jClient);
    }

    public JTextField getTxtSendMSG() {
        return txtSendMSG;
    }

    
}
