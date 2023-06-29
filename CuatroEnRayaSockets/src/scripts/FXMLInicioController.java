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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class FXMLInicioController implements Initializable {


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
                s.proceso();
        }).start();
    }
    
    @FXML
    private void crearCliente(){
        l.add("Cliente ip=" + clienteIP.getText() + " p=" + clientePort.getText());
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("FXMLSelectorCliente.fxml"));
        try{
            Parent root = miCargador.load();
            FXMLSelectorClienteController controladorCliente = miCargador.getController();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Selector Cliente");
            stage.show();
            
            controladorCliente.crearCliente(clienteIP.getText(), Integer.parseInt(clientePort.getText()));
        }catch(IOException e){ System.out.println(e.toString());}
        
        
    }
}
