package scripts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;


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
        
        System.out.println("SERVER: Servidor iniciado");
        CountDownLatch c = new CountDownLatch(2);
        AtomicReference<String> m1 = new AtomicReference<>();
        AtomicReference<String> m2 = new AtomicReference<>();
        //Crear dos hilos que se encarguen de conectar los sockets a los clientes y continuen la conexión hasta que se confirme
        new Thread(() -> {
            try{
                cliente1 = ss.accept();
                System.out.println("SERVER: LLegó primer cliente");
                
                BufferedReader bf = new BufferedReader(new InputStreamReader(cliente1.getInputStream()));
                
                System.out.println("SERVER: Esperando por una linea");
                String mensajeAccion = bf.readLine();
                
                System.out.println("SERVER: Mensaje de acción recibido: " + mensajeAccion);
                m1.set(mensajeAccion);
                c.countDown();
            }catch(IOException e){
                System.out.println(e.toString());
            }

        }).start();
        new Thread(() -> {
            try{
                cliente2 = ss.accept();
                System.out.println("SERVER: LLegó segundo cliente");
                
                Scanner sc2 = new Scanner(new InputStreamReader(cliente2.getInputStream()));
                
                System.out.println("SERVER: Esperando por una linea");
                String mensajeAccion = sc2.nextLine();
                
                System.out.println("SERVER: Mensaje de acción recibido: " + mensajeAccion);
                m2.set(mensajeAccion);
                c.countDown();
            }catch(IOException e){
                System.out.println(e.toString());
            }

        }).start();
        
        try{
            c.await();
            System.out.println("SERVER: Los mensajes recibidos son: " + m1 + m2);
            System.out.println("SERVER: Enviando mensajes a los clientes");
            PrintWriter pw1 = new PrintWriter(cliente1.getOutputStream());
            PrintWriter pw2 = new PrintWriter(cliente2.getOutputStream());
            pw1.println(m2); pw1.flush();
            pw2.println(m1); pw2.flush();
        }catch(InterruptedException e){
            
        }catch(IOException e){
            
        }
    }
    
    public ServerSocket getSocket(){
        return ss;
    }
}