package scripts;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;


public class FXMLSeleccionController implements Initializable {


    @FXML
    private TextField clienteIP;
    @FXML
    private TextField clientePort;
    @FXML
    private TextField servidorPort;
    @FXML
    private Button crearCliente;
    @FXML
    private Button crearServidor;
    @FXML
    private ListView<String> lista;
   
    private ObservableList<String> l;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        l = FXCollections.observableArrayList();
        lista.setItems(l);
    }
    
    @FXML
    private void crearServidor(){
        
        l.add("Servidor ip=localhost p=" + servidorPort.getText());
        
        new Thread(() -> {
                System.out.println("Iniciando servidor en el puerto " + servidorPort.getText());
                Servidor s = new Servidor(Integer.parseInt(servidorPort.getText()));
                //s.proceso();
                System.out.println(s.getSocket().getInetAddress().toString());
                //try{s.getSocket().close();}catch(Exception e){}
            }).start();
    }
    
    @FXML
    private void crearCliente(){
        l.add("Cliente ip=" + clienteIP.getText() + " p=" + clientePort.getText());
        try{Socket s = new Socket(clienteIP.getText(), Integer.parseInt(clientePort.getText())); System.out.println(s.toString()); s.close();}catch(UnknownHostException e){
            System.out.println("No se encontro el host");
            System.out.println(e);
        }catch(IOException e){
            System.out.println("No se pudo conectar");
            System.out.println(e);
        }
        
    }
}
