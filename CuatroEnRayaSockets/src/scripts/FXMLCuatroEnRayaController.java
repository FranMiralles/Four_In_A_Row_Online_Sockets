package scripts;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;


public class FXMLCuatroEnRayaController implements Initializable {

    @FXML
    private ImageView yourImage;
    @FXML
    private Label yourName;
    @FXML
    private Label otherName;
    @FXML
    private ImageView otherImage;
    @FXML
    private Circle yourFicha;
    @FXML
    private Circle otherFicha;
    @FXML
    private Label yourTurn;
    @FXML
    private Label otherTurn;
    @FXML
    private Button c0;
    @FXML
    private Button c1;
    @FXML
    private Button c2;
    @FXML
    private Button c3;
    @FXML
    private Button c4;
    @FXML
    private Button c5;
    @FXML
    private Button c6;
    
    public boolean turno;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    public void configurar(String yName, int yImage, String oName, int oImage, int turno, Image[] images){
        yourName.setText(yName);
        yourImage.setImage(images[yImage]);
        otherName.setText(oName);
        otherImage.setImage(images[oImage]);
        if(turno == 1){
            otherTurn.setDisable(true);
            this.turno = true;
        }else{
            yourTurn.setDisable(true);
            this.turno = false;
        }
        setMin();
    }
    
    public void setMin(){
        Stage stage = (Stage) c1.getScene().getWindow();
        stage.setMinWidth(900);
        stage.setMinHeight(800);
    }
}
