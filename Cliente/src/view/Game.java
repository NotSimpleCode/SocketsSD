package view;

import javax.swing.JFrame;

public class Game extends JFrame{

    public Game() {
        super("GAME - CLIENT");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(400, 700);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public Game(JFrame inicial) { 
        this.add(inicial);
    }
    
}
