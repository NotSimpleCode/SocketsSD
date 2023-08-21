package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class ColorChooser extends JFrame implements ActionListener {

	

	JColorChooser color;

	JButton colorAccept;
	JPanel down;

	JClient dad;

	public ColorChooser(JClient dad) {

		super("Color");
		
		this.dad = dad;

		setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setSize(700, 400);
		this.setLocationRelativeTo(null);

		initComponents();

		setListeners();

		this.add(color, BorderLayout.CENTER);

		
		down.setLayout(null);
		down.setPreferredSize(new Dimension(1, 40));
		down.add(colorAccept);

		this.add(down, BorderLayout.SOUTH);


		setVisible(true);

	}


	private void setListeners() {
		colorAccept.setActionCommand("color");
		colorAccept.addActionListener(this);
	}

	private void initComponents() {

		down = new JPanel();
		down.setLocation(0, 400);

		color = new JColorChooser();
		colorAccept = new JButton("Elegir Color");

		color.setBounds(0, 20, 700, 400);

		colorAccept.setBounds(120, 10, 120, 30);

	}

	public void showMessage(String msg) {
		System.out.println(msg);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equalsIgnoreCase("color")){
			dad.setColor(color.getColor());
			setVisible(false);
		}
		
	}


}
