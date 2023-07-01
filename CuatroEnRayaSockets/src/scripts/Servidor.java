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
    
    public void procesoInicio(){
        
        System.out.println("SERVER: Servidor iniciado");
        CountDownLatch c = new CountDownLatch(2);
        AtomicReference<String> m1 = new AtomicReference<>();
        AtomicReference<String> m2 = new AtomicReference<>();
        //Crear dos hilos que se encarguen de conectar los sockets a los clientes y continuen la conexión hasta que se confirme
        new Thread(() -> {
            try{
                cliente1 = ss.accept();
                Scanner sc = new Scanner(cliente1.getInputStream());
                String mensajeAccion = sc.nextLine();
                m1.set(mensajeAccion);
                c.countDown();
            }catch(IOException e){
                System.out.println(e.toString());
            }

        }).start();
        new Thread(() -> {
            try{
                cliente2 = ss.accept();
                
                Scanner sc2 = new Scanner(new InputStreamReader(cliente2.getInputStream()));
                String mensajeAccion = sc2.nextLine();
                m2.set(mensajeAccion);
                c.countDown();
            }catch(IOException e){
                System.out.println(e.toString());
            }

        }).start();
        
        try{
            c.await();
            PrintWriter pw1 = new PrintWriter(cliente1.getOutputStream());
            PrintWriter pw2 = new PrintWriter(cliente2.getOutputStream());
            pw1.println(m2 + ",1"); pw1.flush();
            pw2.println(m1 + ",2"); pw2.flush();
            procesoJuego();
        }catch(InterruptedException e){
            System.out.println(e.toString());
        }catch(IOException e){
            System.out.println(e.toString());
        }
    }
    
    public void procesoJuego(){
        // El primero es el cliente1, el segundo es el cliente2
        try{
            while(true){
                Scanner sc1 = new Scanner(cliente1.getInputStream());
                String m1 = sc1.nextLine();
                PrintWriter pw2 = new PrintWriter(cliente2.getOutputStream());
                pw2.println(m1); pw2.flush();
                
                Scanner sc2 = new Scanner(cliente2.getInputStream());
                String m2 = sc2.nextLine();
                PrintWriter pw1 = new PrintWriter(cliente1.getOutputStream());
                pw1.println(m2); pw1.flush();
            } 
        }catch(IOException e){
            System.out.println(e.toString());
        }
        
    }
    
    public ServerSocket getSocket(){
        return ss;
    }
}