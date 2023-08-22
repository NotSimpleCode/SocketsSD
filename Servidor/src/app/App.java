package app;

import presenter.ServerPresenter;
import view.JServer;

public class App {

    public static void main(String[] args) throws Exception {

        ServerPresenter presenter = new ServerPresenter();
        JServer view = new JServer();

        presenter.setView(view);

        view.setPresenter(presenter);

        try {

            view.start();

        } catch (Exception e) {
            view.showInfo("Por favor intente mas tarde...");
        }
    }

}
