package scripts;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


public class FXMLSelectorClienteController implements Initializable {

    @FXML
    private TextField nombreJugador;
    @FXML
    private Button confirmar;
    @FXML
    private ImageView imagenJugador;
    @FXML
    private Label estado;
    @FXML
    private ComboBox<Integer> rondas;
    
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
        estado.setText("¡Elige tu nombre e icono!");
        rondas.getItems().add(3);rondas.getItems().add(5);rondas.getItems().add(8);
        rondas.getSelectionModel().selectFirst();
    }
    
    @FXML
    private void confirmar(){
        
        new Thread(() -> {
            try{
                PrintWriter pw = new PrintWriter(cliente.getOutputStream());
                pw.print(nombreJugador.getText() + "," + i + "," + rondas.getSelectionModel().getSelectedItem() + "\n"); pw.flush();
                
                Platform.runLater(() -> { //Esto se usa ya que en un hilo que no sea el de JavaFX no se pueden cambiar cosas gráficas, por lo que de esta forma se accede al hilo de JavaFX
                    estado.setText("Esperando al otro jugador...");
                    confirmar.setDisable(true);
                    rondas.setDisable(true);
                });
                
                Scanner sc = new Scanner(cliente.getInputStream());
                String respuesta = sc.nextLine();
                String[] r = respuesta.split(",");
                cambiarJugar(r[0], Integer.parseInt(r[1]), Integer.parseInt(r[2]), Integer.parseInt(r[3]));
                
            }catch(IOException e){
                System.out.println(e.toString());
            }
        }).start();
        
    }
    
    private void cambiarJugar(String nameOponent, int numOponent, int ronda, int turno){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLCuatroEnRaya.fxml"));
            Parent root = loader.load();
            FXMLCuatroEnRayaController controller = loader.getController();
            nombreJugador.getScene().setRoot(root);
            controller.setSocket(cliente);
            Stage stage = (Stage) root.getScene().getWindow();
            Platform.runLater(() -> {
                controller.configurar(nombreJugador.getText(), i, nameOponent, numOponent, ronda, turno, images);
                stage.setResizable(true);
                stage.setMinWidth(900);
                stage.setMinHeight(800);
            });
            
        }catch(IOException e){
            System.out.println(e.toString());
        }
        
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