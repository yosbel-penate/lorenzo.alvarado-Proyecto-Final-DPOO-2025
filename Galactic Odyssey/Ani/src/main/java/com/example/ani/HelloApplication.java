package com.example.ani;
import Class.Player;
import eu.hansolo.tilesfx.addons.Switch;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import jdk.swing.interop.SwingInterOpUtils;

public class HelloApplication extends Application {
    private GraphicsContext graphics;
    private Group root;
    private Scene scene;
    private Canvas canvas;
    private int x=0;
    private Player player;
    public static boolean up;
    public static boolean down;
    public static boolean right;
    public static boolean left;


    public static void main(String[] args){
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        initializeComponent();
        managementEvent();
        paint();
        stage.setScene(scene);
        stage.setTitle("Galactic Odyssey: The Cosmic Enigma");
        stage.show();
        cycleGame();
    }

    public void cycleGame(){
        long initialTime = System.nanoTime();
        AnimationTimer animationTimer = new AnimationTimer() {  // este metodo se ejecuta aproximadamente 60 veces por segundo
            @Override
            public void handle(long currentTime) {
                double t= (currentTime - initialTime) / 1000000000.0;
                System.out.println(t);
                updateStatus();
                paint();
            }
        };
        animationTimer.start();
    }

    public void updateStatus(){
    player.move();
    }

    public void initializeComponent()     {
        player=new Player(20,40,3, "personaje.png");
        root = new Group();
        scene = new Scene(root, 700, 620);
        canvas = new Canvas(700, 620); // pintar
        root.getChildren().add(canvas);
        graphics = canvas.getGraphicsContext2D();

    }

    public void paint() {
        //graphics.fillRect(0, 0, 100, 100); // posicion de pintar

        graphics.drawImage(new Image("D:\\Con java fx\\lorenzo.alvarado-Proyecto-Final-DPOO-2025\\Ani\\src\\main\\resources\\Mapa.jpg"), 0, 0);
        player.pintar(graphics);
    }
    public void managementEvent(){
         scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
             @Override
             public void handle(KeyEvent event) {
                 switch(event.getCode().toString()) {
                     case "D":

                         right=true;
                         break;
                     case "A":
                         left=true;
                         break;
                     case "W":
                         up=true;
                         break;
                     case "S":
                         down=true;
                 }
            }
        });
         scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
             //este se ejecuta cuando se suelta una tecla
             @Override
             public void handle(KeyEvent event) {

                 switch(event.getCode().toString()) {
                     case "D":

                         right=false;
                         break;
                     case "A":
                         left=false;
                         break;
                     case "W":
                         up=false;
                         break;
                     case "S":
                         down=false;
                 }

             }
         });
    }
}
