package app;

import presenter.ClientPresenter;
import view.JClient;

public class App {
    public static void main(String[] args) throws Exception {

        try {
            ClientPresenter presenter = new ClientPresenter();
            JClient view = new JClient();

            
            presenter.setView(view);

            view.setPresenter(presenter);

            view.start();

        } catch (Exception e) {
            System.out.println("Por favor intente mas tarde...");
        }
    }

    
}
