package scripts;

import java.io.IOException;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
    @FXML
    private Label count1;
    @FXML
    private Label count2;
    
    public boolean turno;
    public int[][] tableroLogico;
    Socket cliente;
    int limPuntos;
    int numFichas;
    int capMaxFichas = 49;
    
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
        numFichas = 0;
    }    
    
    public int introducir(int c){
        if(!turno){ return -1;}
        if(tableroLogico[1][c] != 0){ return -1;}
        if(capMaxFichas == numFichas){ vaciarTablero(); numFichas = 0;}
        numFichas++;
        Circle circle = new Circle();
        circle.setRadius(25);
        Paint paint = (Paint) Color.rgb(215, 218, 14);
        circle.setFill(paint);
        
        int f = 1;
        while(f <= 6 && tableroLogico[f][c] == 0){
            f++;
        }
        tablero.add(circle, c, --f);
        tableroLogico[f][c] = 1;
        int g = 0; //ganar punto
        int a = 0; //acabar
        if(comprobarGanar()){ 
            g = 1;
            int count = Integer.parseInt(yourCount.getText()) + 1;
            if(count == limPuntos){ a = 1;} // LLamar a acabar después de enviar un mensaje que indique que hay que acabar al otro cliente
            yourCount.setText(count + "");
            
            esperar10();
        }
        
        try{
            PrintWriter pw = new PrintWriter(cliente.getOutputStream());
            pw.println(f + "" + c + "" + g + "" + a); pw.flush();
            yourTurn.setDisable(true);
            otherTurn.setDisable(false);
            turno = false;
            if(a == 1){ acabar(1);}
            recibir();
            
        }catch(IOException e){
            System.out.println(e.toString());
        }
        return 0;
    }
    
    public void recibir(){
        new Thread(() -> {
            try{
                Scanner sc = new Scanner(cliente.getInputStream());
                String mensaje = sc.nextLine(); //Esperando al mensaje
                int f = Integer.parseInt(mensaje.charAt(0) + "");
                int c = Integer.parseInt(mensaje.charAt(1) + "");
                int g = Integer.parseInt(mensaje.charAt(2) + "");
                int a = Integer.parseInt(mensaje.charAt(3) + "");
                
                Platform.runLater(() -> { //Esto se usa ya que en un hilo que no sea el de JavaFX no se pueden cambiar cosas gráficas, por lo que de esta forma se accede al hilo de JavaFX
                    Circle circle = new Circle();
                    circle.setRadius(25);
                    Paint paint = (Paint) Color.rgb(228, 31, 31);
                    circle.setFill(paint);
                    tablero.add(circle, c, f);
                    tableroLogico[f][c] = 2;
                    if(g == 1){ /*Ha ganado el otro*/
                        esperar10();
                        int count = Integer.parseInt(otherCount.getText()) + 1;
                        otherCount.setText(count + "");
                    }
                    
                    if(a == 1){ //Acabar, ha ganado el otro
                        acabar(2);
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
    
    public void acabar(int ganador){
        Alert a = new Alert(AlertType.INFORMATION);
        if(ganador == 1){ //Tu
            a.setContentText("Ha ganado el jugador " + yourName.getText());
        }else{ a.setContentText("Ha ganado el jugador " + otherName.getText());}
        a.showAndWait();
        try{ cliente.close();}catch(IOException e){ System.out.println(e.toString());}
        ((Stage)yourName.getScene().getWindow()).close();
    }
    
    public boolean comprobarGanar(){
        
        //Filas
        for(int i = 1; i < 7; i++){
            for(int j = 0; j < 4; j++){
                if(tableroLogico[i][j] == 1 && tableroLogico[i][j + 1] == 1 && tableroLogico[i][j + 2] == 1 && tableroLogico[i][j + 3] == 1){ return true;}
            }
        }
        
        //Columnas
        for(int j = 0; j < 7; j++){
            for(int i = 1; i < 4; i++){
                if(tableroLogico[i][j] == 1 && tableroLogico[i + 1][j] == 1 && tableroLogico[i + 2][j] == 1 && tableroLogico[i + 3][j] == 1){ return true;}
            }
        }
        
        //Diagonal menguante
        if(tableroLogico[3][0] == 1 && tableroLogico[4][1] == 1 && tableroLogico[5][2] == 1 && tableroLogico[6][3] == 1){ return true;}
        for(int i = 2, j = 0, veces = 2; veces > 0; veces--, i++, j++){ if(tableroLogico[i][j] == 1 && tableroLogico[i + 1][j + 1] == 1 && tableroLogico[i + 2][j + 2] == 1 && tableroLogico[i + 3][j + 3] == 1){ return true;}}
        for(int i = 1, j = 0, veces = 3; veces > 0; veces--, i++, j++){ if(tableroLogico[i][j] == 1 && tableroLogico[i + 1][j + 1] == 1 && tableroLogico[i + 2][j + 2] == 1 && tableroLogico[i + 3][j + 3] == 1){ return true;}}
        for(int i = 1, j = 1, veces = 3; veces > 0; veces--, i++, j++){ if(tableroLogico[i][j] == 1 && tableroLogico[i + 1][j + 1] == 1 && tableroLogico[i + 2][j + 2] == 1 && tableroLogico[i + 3][j + 3] == 1){ return true;}}
        for(int i = 1, j = 2, veces = 2; veces > 0; veces--, i++, j++){ if(tableroLogico[i][j] == 1 && tableroLogico[i + 1][j + 1] == 1 && tableroLogico[i + 2][j + 2] == 1 && tableroLogico[i + 3][j + 3] == 1){ return true;}}
        if(tableroLogico[1][3] == 1 && tableroLogico[2][4] == 1 && tableroLogico[3][5] == 1 && tableroLogico[4][6] == 1){ return true;}
        
        //Diagonal creciente
        if(tableroLogico[3][6] == 1 && tableroLogico[4][5] == 1 && tableroLogico[5][4] == 1 && tableroLogico[6][3] == 1){ return true;}
        for(int i = 2, j = 6, veces = 2; veces > 0; veces--, i++, j--){ if(tableroLogico[i][j] == 1 && tableroLogico[i + 1][j - 1] == 1 && tableroLogico[i + 2][j - 2] == 1 && tableroLogico[i + 3][j - 3] == 1){ return true;}}
        for(int i = 1, j = 6, veces = 3; veces > 0; veces--, i++, j--){ if(tableroLogico[i][j] == 1 && tableroLogico[i + 1][j - 1] == 1 && tableroLogico[i + 2][j - 2] == 1 && tableroLogico[i + 3][j - 3] == 1){ return true;}}
        for(int i = 1, j = 5, veces = 3; veces > 0; veces--, i++, j--){ if(tableroLogico[i][j] == 1 && tableroLogico[i + 1][j - 1] == 1 && tableroLogico[i + 2][j - 2] == 1 && tableroLogico[i + 3][j - 3] == 1){ return true;}}
        for(int i = 1, j = 4, veces = 2; veces > 0; veces--, i++, j--){ if(tableroLogico[i][j] == 1 && tableroLogico[i + 1][j - 1] == 1 && tableroLogico[i + 2][j - 2] == 1 && tableroLogico[i + 3][j - 3] == 1){ return true;}}
        if(tableroLogico[1][3] == 1 && tableroLogico[2][2] == 1 && tableroLogico[3][1] == 1 && tableroLogico[4][0] == 1){ return true;}
        
        //No se ha ganado
        return false;
    }
    
    public void vaciarTablero(){
        for(int i = 1; i < 7; i++){
            for(int j = 0; j < 7; j++){
                tableroLogico[i][j] = 0;
            }
        }
        List<Node> circlesToRemove = new ArrayList<>();

        for(Node node : tablero.getChildren()) {
            if(node instanceof Circle) {
                circlesToRemove.add(node);
            }
        }

        tablero.getChildren().removeAll(circlesToRemove);
    }
    
    private void esperar10(){
        //Esperar x tiempo para ver la victoria
        c0.setDisable(true);
        c1.setDisable(true);
        c2.setDisable(true);
        c3.setDisable(true);
        c4.setDisable(true);
        c5.setDisable(true);
        c6.setDisable(true);
        new Thread(() -> {
            try {
                // Espera de 10 segundos (10000 milisegundos)
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.toString();
            }

            Platform.runLater(() -> {
                vaciarTablero(); 
                c0.setDisable(false);
                c1.setDisable(false);
                c2.setDisable(false);
                c3.setDisable(false);
                c4.setDisable(false);
                c5.setDisable(false);
                c6.setDisable(false);
            });
        }).start();
    }
    
    public void configurar(String yName, int yImage, String oName, int oImage, int ronda, int turno, Image[] images){
        yourName.setText(yName);
        yourImage.setImage(images[yImage]);
        otherName.setText(oName);
        otherImage.setImage(images[oImage]);
        count1.setText(ronda + "");
        count2.setText(ronda + "");
        limPuntos = ronda;
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
        Alert a = new Alert(AlertType.INFORMATION);
        a.setContentText("Se ha decidido jugar a " + ronda + " rondas.");
        a.showAndWait();
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