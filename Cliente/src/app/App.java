package app;

import java.util.Scanner;

import connections.Connect;

public class App {
    public static void main(String[] args) throws Exception {
        
        try {
            Connect con = new Connect();
            Scanner sc = new Scanner(System.in);
            String aux = "";

            while (!aux.equalsIgnoreCase("CLOSE") && !(con.getSocket().isClosed())) {
                System.out.println("Elija su opcion");
                aux = sc.nextLine().toUpperCase();
                if (aux.equalsIgnoreCase("MSG")) {
                    System.out.println("Mensaje del servidor : " + con.readMsg());
                    if (con.readMsg().equalsIgnoreCase("vacio")) {
                        boolean flat = true;
                        System.out.println("Intentando volver a conectar...");

                        int i = 0;
                        while (flat) {
                            System.out.println("Segundos " +(i+=1));
                            con = new Connect();
                            if (con.getSocket()!=null) {
                                flat = false;
                            }
                            if (!(i < 30)) {
                                flat = false;
                            }
                        }
                    }
                }
            }
            sc.close();
            if (!con.getSocket().isClosed()) {
                con.closeConnect();
            }
            
        } catch (Exception e) {
            System.out.println("Por favor intente mas tarde...");
        }
        
    }
}
