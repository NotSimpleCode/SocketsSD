package app;

import presenter.ServerPresenter;
import view.JServer;

public class App {

    public static void main(String[] args) throws Exception {
        
        try {
            ServerPresenter presenter = new ServerPresenter();
            JServer view = new JServer();

            presenter.setView(view);

            view.setPresenter(presenter);

            view.start();
            
        } catch (Exception e) {
            System.out.println("Por favor intente mas tarde...");
        }
    }


    
}
