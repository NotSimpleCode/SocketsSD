package view;

import javax.swing.JFrame;

public class Game extends JFrame{

    public Game() {
        super();
        setName("CHAT - SERVER");
        this.setSize(700, 700-100);
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
    
}
