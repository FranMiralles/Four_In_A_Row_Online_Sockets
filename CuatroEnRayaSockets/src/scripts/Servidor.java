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
    
    public void proceso() throws Exception{
        try{
            System.out.println("Servidor iniciado");
            cliente1 = ss.accept();
            System.out.println("LLegó primer cliente");
            
            InputStream inputStream = cliente1.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

            // Leer el mensaje de acción enviado por el cliente
            System.out.println("Esperando por una linea");
            String mensajeAccion = in.readLine();
            System.out.println("Mensaje de acción recibido: " + mensajeAccion);
            
            
            cliente2 = ss.accept();
            System.out.println("LLegó segundo cliente");
            ss.close();
        }catch(IOException e){
            System.out.println(e.toString());
        }
        
    }
    
    public ServerSocket getSocket(){
        return ss;
    }
}