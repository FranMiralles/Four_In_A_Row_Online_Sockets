package scripts;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
    @FXML
    private Label yourCount;
    @FXML
    private Label otherCount;
    
    public boolean turno;
    public int[][] tableroLogico;
    Socket cliente;
    
    
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
        int g = 0;
        if(comprobarGanar()){ 
            vaciarTablero(); g = 1;
            int count = Integer.parseInt(yourCount.getText()) + 1;
            yourCount.setText(count + "");
        }
        
        try{
            PrintWriter pw = new PrintWriter(cliente.getOutputStream());
            pw.println(f + "" + c + "" + g); pw.flush();
            yourTurn.setDisable(true);
            otherTurn.setDisable(false);
            turno = false;
            recibir();
            
        }catch(IOException e){
            System.out.println(e.toString());
        }
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
    
    public void vaciarTablero(){
        for(int i = 1; i < 7; i++){
            for(int j = 0; j < 7; j++){
                tableroLogico[i][j] = 0;
            }
        }
        List<Node> circlesToRemove = new ArrayList<>();

        for (Node node : tablero.getChildren()) {
            if (node instanceof Circle) {
                circlesToRemove.add(node);
            }
        }

        tablero.getChildren().removeAll(circlesToRemove);
    }
    
    public void recibir(){
        new Thread(() -> {
            try{
                Scanner sc = new Scanner(cliente.getInputStream());
                String mensaje = sc.nextLine(); //Esperando al mensaje
                int f = Integer.parseInt(mensaje.charAt(0) + "");
                int c = Integer.parseInt(mensaje.charAt(1) + "");
                int g = Integer.parseInt(mensaje.charAt(2) + "");
                
                Platform.runLater(() -> { //Esto se usa ya que en un hilo que no sea el de JavaFX no se pueden cambiar cosas gráficas, por lo que de esta forma se accede al hilo de JavaFX
                    Circle circle = new Circle();
                    circle.setRadius(20);
                    Paint paint = (Paint) Color.RED;
                    circle.setFill(paint);
                    tablero.add(circle, c, f);
                    tableroLogico[f][c] = 2;
                    if(g == 1){ /*Ha ganado el otro*/
                        vaciarTablero();
                        int count = Integer.parseInt(otherCount.getText()) + 1;
                        otherCount.setText(count + "");
                    }
                });
                turno = true;
                yourTurn.setDisable(false);
                otherTurn.setDisable(true);
            }catch(IOException e){
                System.out.println(e.toString());
            }

        }).start();
    }
    
    public void configurar(String yName, int yImage, String oName, int oImage, int ronda, int turno, Image[] images){
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
            //Método a esperar por mensaje
            recibir();
        }
        setMin();
    }
    
    public void setMin(){
        Stage stage = (Stage) c1.getScene().getWindow();
        stage.setMinWidth(900);
        stage.setMinHeight(800);
    }
    
    public void setSocket(Socket cliente){
        this.cliente = cliente;
    }
}