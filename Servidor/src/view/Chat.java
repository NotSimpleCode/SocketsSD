package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.plaf.FontUIResource;


import connections.CLWorker;
public class Chat extends JPanel{

    JButton btnSendMSG;

    JTextArea txtGetMSG;
    JTextField txtSendMSG;
    JList<DynamicIcon> listPaneInfo;

    JLabel infoServer;

    JScrollPane Jscro;
    

    public JLabel getInfoServer() {
        return infoServer;
    }

    public Chat() {
        super();
        setBackground(Color.gray);
        setPreferredSize(new Dimension(400,1));
        setLayout(null);

        initComponents();

        this.add(listPaneInfo);
        this.add(Jscro);
        this.add(txtSendMSG);
        this.add(btnSendMSG);
        this.add(infoServer,BorderLayout.SOUTH);

        
    }

    public void setInfo(String info){
        infoServer.setText(info);
    }

    private void initComponents() {

        infoServer = new JLabel();

        btnSendMSG = new JButton("Send MSG");
        txtGetMSG = new JTextArea("MSG");
        txtSendMSG = new JTextField("MSG to Send");

        listPaneInfo = new JList<>();
        listPaneInfo.setBounds(70, 10, 240, 300);

        

        txtGetMSG.setBounds(70, 320 , 240, 100);
        Jscro = new JScrollPane(txtGetMSG);
        Jscro.setBounds(70, 320 , 240, 100);
        

        txtSendMSG.setBounds(70,440 , 240, 30);
        
        btnSendMSG.setBounds(140, 700-190, 100, 40);

        infoServer.setBounds(270-10,440+90, 300, 30);
        
        Font f1 = new FontUIResource(Font.SANS_SERIF, Font.ITALIC, 8);
        infoServer.setFont(f1);


        setActionComands();
    }

    private void setActionComands() {
        btnSendMSG.setActionCommand("send");
    }

    public void setListeners(JServer jServer) {
        this.btnSendMSG.addActionListener(jServer);
    }

    public JTextField getTxtSendMSG() {
        return txtSendMSG;
    }


    public JTextArea getTxtGetMSG() {
        return txtGetMSG;
    }

	public void setClients(ArrayList<CLWorker> inService) {
		addClientsToJList(inService);
		
	}

	private void addClientsToJList(ArrayList<CLWorker> inService) {
	

		DynamicIcon[] listData = new DynamicIcon[inService.size()];
		
        for (int index = 0; index < listData.length; index++) {
            CLWorker tmp = inService.get(index);
            listData[index] = new DynamicIcon(Color.getColor("",(tmp.getColor())), tmp.getHostName());
        }
		
		listPaneInfo.setListData(listData);
		
	}
	
    static class DynamicIcon implements Icon {
    	
    	Color color;
        String string;

    
		public DynamicIcon(Color color) {
			super();
			this.color = color;
		}

		public DynamicIcon(Color color2, String string) {
            super();
			this.color = color2;
            this.string = "Client : " + string;

        }

        @Override
		public int getIconHeight() {
			// TODO Auto-generated method stub
			return 20;
		}

		@Override
		public int getIconWidth() {
			// TODO Auto-generated method stub
			return 20;
		}

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(color);
            g2d.drawString(string, x+30, y+14);
			g2d.fill3DRect(x, y, getIconWidth(), getIconHeight(), false);
            
			
		}

    	
    }
}


	









