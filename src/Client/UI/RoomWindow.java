package Client.UI;

import Client.ClientApp;
import Client.Networking.MainConnector;
import Client.Networking.RoomConnector;
import Client.Renderer.FpsCounter;
import Client.Renderer.HealthBar;
import Client.Renderer.KillDeathRatio;
import Client.Renderer.World;
import Templates.TScene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class RoomWindow extends TScene {

    RoomWindow(){
        super(new StackPane());
    }

    private Chat chat = new Chat();
    private World world = new World();
    private FpsCounter fps = new FpsCounter();
    private HealthBar healthBar = new HealthBar();
    private KillDeathRatio u_kdr = new KillDeathRatio("user");
    private KillDeathRatio top_kdr = new KillDeathRatio("top");
    private int number = 100;

    @Override
    public void setup() {
        StackPane root = (StackPane) getRoot();

        world.requestFocus();
        world.setFocusTraversable(true);
        root.getChildren().add(world);

        BorderPane bp = new BorderPane();
        root.getChildren().add(bp);

//        HBox bottomPanel = new HBox();

//        bottomPanel.getChildren().add(fps);
//        bottomPanel.getChildren().add(healthBar);

        VBox rightPanel = new VBox();
//        rightPanel.getChildren().add(fps);
        rightPanel.getChildren().add(chat);

        bp.setBottom(healthBar);
//        bp.setRight(chat);

        VBox scores = new VBox();

        scores.getChildren().addAll(u_kdr,top_kdr);

        bp.setLeft(scores);

        TopMenu top = new TopMenu();
        bp.setTop(top);


        setOnKeyPressed(key -> {
            switch (key.getCode()) {
                case ESCAPE:
                    top.leave();
                    break;
            }
        });
    }

    @Override
    public void refresh() {
        RoomConnector.update();

        fps.update();
        u_kdr.update(RoomConnector.getClientPlayerInfo());
        top_kdr.update(RoomConnector.getHighestKDRPlayerInfo());
        chat.update();

        world.update(RoomConnector.getGamestate());
        healthBar.update(RoomConnector.getClientPlayerInfo().health);

        // world.update(RoomConnector.update());
    }

    @Override
    public void closingProtocol() {
        try {
            MainConnector.leaveRoom();
            MainConnector.quit();
            MainConnector.exitApplication();
        } catch (Exception e) {e.printStackTrace();}
    }

    @Override
    public void leavingProtocol() {
        try {
            MainConnector.leaveRoom();
            ClientApp.setScene(new Lobby());
        } catch (Exception e) {e.printStackTrace();}
    }
}

