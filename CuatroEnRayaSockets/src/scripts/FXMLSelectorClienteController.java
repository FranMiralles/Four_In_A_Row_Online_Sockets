package scripts;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class FXMLSelectorClienteController implements Initializable {

    @FXML
    private TextField nombreJugador;
    @FXML
    private Button confirmar;
    @FXML
    private ImageView imagenJugador;
    
    Socket cliente;
    Image[] images;
    int i = 0;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        i = 0;
        images = new Image[12];
        images[0] = new Image("images/default.png", 140, 150, false, true);
        images[1] = new Image("images/men.png", 140, 150, false, true);
        images[2] = new Image("images/men2.png", 140, 150, false, true);
        images[3] = new Image("images/men3.png", 140, 150, false, true);
        images[4] = new Image("images/men4.png", 140, 150, false, true);
        images[5] = new Image("images/men5.png", 140, 150, false, true);
        images[6] = new Image("images/woman.png", 140, 150, false, true);
        images[7] = new Image("images/woman2.png", 140, 150, false, true);
        images[8] = new Image("images/woman3.png", 140, 150, false, true);
        images[9] = new Image("images/woman4.png", 140, 150, false, true);
        images[10] = new Image("images/woman5.png", 140, 150, false, true);
        images[11] = new Image("images/woman6.png", 140, 150, false, true);
        imagenJugador.setImage(images[0]);
    }
    
    public void crearCliente(String ipServidor, int puerto){
        try{
            cliente = new Socket(ipServidor, puerto);
        }catch(UnknownHostException e){
            System.out.println(e.toString());
        }catch(IOException e){
            System.out.println(e.toString());
        }
        
    }
    
    @FXML
    private void moverDer(){
        i++;
        if(i == 12){
            i = 0;
        }
        imagenJugador.setImage(images[i]);
    }
    
    @FXML
    private void moverIzq(){
        i--;
        if(i == -1){
            i = 11;
        }
        imagenJugador.setImage(images[i]);
    }
}
