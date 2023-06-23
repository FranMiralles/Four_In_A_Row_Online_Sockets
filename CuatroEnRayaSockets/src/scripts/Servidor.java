package scripts;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


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
        try{
            System.out.println("Servidor iniciado");
            cliente1 = ss.accept();
            System.out.println("LLegó primer cliente");
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