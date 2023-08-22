package app;

import presenter.ClientPresenter;
import view.JClient;

public class App {
    public static void main(String[] args) throws Exception {

        ClientPresenter presenter = new ClientPresenter();
        JClient view = new JClient();

        presenter.setView(view);
        view.setPresenter(presenter);

        try {

            view.start();

        } catch (Exception e) {
            view.showInfo("Por favor intente mas tarde...");
        }
    }

}
