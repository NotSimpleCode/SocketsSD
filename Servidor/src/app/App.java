package app;

import connections.Server;

public class App {
    public static void main(String[] args) throws Exception {
        try {
            new Server().startListening();
        } catch (Exception e) {
            System.out.println("El servidor no esta disponible");
        }

    }
}
