package scripts;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


public class FXMLInicioController implements Initializable {


    @FXML
    private TextField clienteIP;
    @FXML
    private TextField clientePort;
    @FXML
    private TextField servidorPort;
    @FXML
    private ImageView servidor;
    @FXML
    private Button bCliente;
    @FXML
    private Button bServidor;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        servidor.setOpacity(0.2);
        
        bCliente.disableProperty().bind(Bindings.createBooleanBinding(
                () -> clienteIP.getText().isEmpty() || clientePort.getText().isEmpty(),
                clienteIP.textProperty(),
                clientePort.textProperty()
        ));

        bServidor.disableProperty().bind(Bindings.isEmpty(servidorPort.textProperty()));
    }
    
    @FXML
    private void crearServidor(){     
        new Thread(() -> {
                Servidor s = new Servidor(Integer.parseInt(servidorPort.getText()));
                servidor.setOpacity(1);
                FXMLInicioController controller = this;
                s.procesoInicio(controller);
        }).start();
    }
    
    @FXML
    private void crearCliente(){
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("FXMLSelectorCliente.fxml"));
        try{
            Parent root = miCargador.load();
            FXMLSelectorClienteController controladorCliente = miCargador.getController();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Selector Cliente");
            stage.setResizable(false);
            stage.show();
            
            controladorCliente.crearCliente(clienteIP.getText(), Integer.parseInt(clientePort.getText()));
        }catch(IOException e){ System.out.println(e.toString());}
        
        
    }
    
    public void cambiarOpacidad(){
        servidor.setOpacity(0.2);
    }
}