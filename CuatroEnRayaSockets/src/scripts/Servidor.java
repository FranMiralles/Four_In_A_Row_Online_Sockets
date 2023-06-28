package scripts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class Servidor {
    
    private ServerSocket ss;
    private Socket cliente1;
    private Socket cliente2;
    
    public Servidor(int port){
        try{
            ss = new ServerSocket(port);
        }catch(IOException e){
            System.out.println(e.toString());
        }
    }
    
    public void proceso(){
        
        System.out.println("Servidor iniciado");

        //Crear dos hilos que se encarguen de conectar los sockets a los clientes y continuen la conexión hasta que se confirme
        new Thread(() -> {
            try{
                cliente1 = ss.accept();
                System.out.println("LLegó primer cliente");

                InputStream inputStream = cliente1.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

                // Leer el mensaje de acción enviado por el cliente
                System.out.println("Esperando por una linea");
                String mensajeAccion = in.readLine();
                System.out.println("Mensaje de acción recibido: " + mensajeAccion);
            }catch(IOException e){
                System.out.println(e.toString());
            }

        }).start();
        new Thread(() -> {
            try{
                cliente2 = ss.accept();
                System.out.println("LLegó segundo cliente");

                InputStream inputStream = cliente2.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

                // Leer el mensaje de acción enviado por el cliente
                System.out.println("Esperando por una linea");
                String mensajeAccion = in.readLine();
                System.out.println("Mensaje de acción recibido: " + mensajeAccion);
            }catch(IOException e){
                System.out.println(e.toString());
            }

        }).start();
        
    }
    
    public ServerSocket getSocket(){
        return ss;
    }
}