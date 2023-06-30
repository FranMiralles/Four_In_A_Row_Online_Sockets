package scripts;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
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
    @FXML
    private GridPane tablero;
    
    public boolean turno;
    public int[][] tableroLogico;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        c0.setOnAction(c -> {introducir(0);});
        c1.setOnAction(c -> {introducir(1);});
        c2.setOnAction(c -> {introducir(2);});
        c3.setOnAction(c -> {introducir(3);});
        c4.setOnAction(c -> {introducir(4);});
        c5.setOnAction(c -> {introducir(5);});
        c6.setOnAction(c -> {introducir(6);});
        tableroLogico = new int[7][7];
    }    
    
    public int introducir(int c){
        if(!turno){ return -1;}
        if(tableroLogico[1][c] != 0){ return -1;}
        
        Circle circle = new Circle();
        circle.setRadius(20);
        Paint paint = (Paint) Color.BLUE;
        circle.setFill(paint);
        
        int f = 1;
        while(f <= 6 && tableroLogico[f][c] == 0){
            f++;
        }
        tablero.add(circle, c, --f);
        tableroLogico[f][c] = 1;
        System.out.println(comprobarGanar());
        return 0;
    }
    
    public boolean comprobarGanar(){
        
        //Filas
        for(int i = 1; i < 7; i++){
            for(int j = 0; j < 4; j++){
                if(tableroLogico[i][j] == 1 && tableroLogico[i][j + 1] == 1 && tableroLogico[i][j + 2] == 1 && tableroLogico[i][j + 3] == 1){ return true;}
            }
        }
        
        //Columnas
        for(int j = 0; j < 6; j++){
            for(int i = 1; i < 4; i++){
                if(tableroLogico[i][j] == 1 && tableroLogico[i + 1][j] == 1 && tableroLogico[i + 2][j] == 1 && tableroLogico[i + 3][j] == 1){ return true;}
            }
        }
        
        return false;
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