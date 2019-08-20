package space.crab8012.cardgameplayer;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.DataOutput;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import space.crab8012.cardgameplayer.gameobjects.Player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TestNetwork implements Screen {

    private SpriteBatch batch;
    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    protected Skin skin;

    Preferences prefs;

    int sf = 1; //The viewport scaling factor

    String serverIP = "192.168.2.138";

    public TestNetwork()
    {
        prefs = Gdx.app.getPreferences("settings");


        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("skins/PublicSans-Black.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 10;
        BitmapFont bitmapfont = gen.generateFont(parameter);
        gen.dispose();

        atlas = new TextureAtlas("skins/clean-crispy-ui.atlas");
        skin = new Skin(Gdx.files.internal("skins/clean-crispy-ui.json"), atlas);
        skin.add("other-font", bitmapfont);

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth()/sf, Gdx.graphics.getHeight()/sf, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);
        camera.update();

        stage = new Stage(viewport, batch);
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage); //Set the stage as the input processor

        //Item Table Stuff
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.center();

        //Create Title Object
        Label roomNameLabel = new Label("Network Test Room", skin, "font", new Color(1, 1, 1, 1));
        roomNameLabel.setFontScale(3);
        //Create buttons
        TextButton sendPlayerButton1 = new TextButton("Send 'John Deer'", skin);
        TextButton sendPlayerButton2 = new TextButton("Send 'Legoman888'", skin);
        TextButton sendPlayerButton3 = new TextButton("Send 'MrPuggles'", skin);
        TextButton mainMenuButton = new TextButton("Main Menu", skin);

        //Add listeners to buttons
        sendPlayerButton1.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sendPlayerObject("John Deer", "default", 5);
            }
        });
        sendPlayerButton2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sendPlayerObject("Legoman888", "blue-lego", 0);
            }
        });
        sendPlayerButton3.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sendPlayerObject("MrPuggles", "pixel-pug", 20);
            }
        });
        mainMenuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                leave();
            }
        });

        //Add buttons to table
        mainTable.add(roomNameLabel);
        mainTable.row();
        mainTable.add(sendPlayerButton1).width(400).uniform();
        mainTable.row();
        mainTable.add(sendPlayerButton2).width(400).uniform();
        mainTable.row();
        mainTable.add(sendPlayerButton3).width(400).uniform();
        mainTable.row();
        mainTable.add(mainMenuButton).width(400).uniform();

        //Add table to stage
        stage.addActor(mainTable);
    }

    public void serverStuff(){
            String serverMessage = "";
            try{
                Socket s = new Socket(serverIP, 8888);
                s.setSoTimeout(1000); //Set the socket timeout to 1 second.
                DataInputStream din = new DataInputStream(s.getInputStream());
                DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                dout.writeUTF(prefs.getString("name", "DEFAULT_PLAYER_NAME"));
                dout.flush();
                serverMessage = din.readUTF();
                dout.close();
                s.close();

                notificationDialog("Message Recieved", "Recieved message '" + serverMessage + "' from the server");
            }catch(Exception e){
                System.out.println(e);
            }
    }

    public void sendPlayerObject(String name, String icon, int score){
        try {
            //Set up the basic networking crap
            Socket s = new Socket(prefs.getString("serveraddress", serverIP), 8888);
            OutputStream os = s.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);

            //Create the test player object
            Player testPlayer = new Player(name, icon);
            testPlayer.setScore(score);

            oos.writeObject(testPlayer); // Send the player object to the server.
            oos.flush();
            notificationDialog("Sent Player", "Sent Player " + name + " with Icon " + icon + "\nwith Score: " + score);

            oos.close();
            s.close(); // Close the network socket.

        }catch(Exception e){
            System.out.println(e);
        }

    }

    public void notificationDialog(String title, String message){
        new Dialog("Message Recieved", skin).text(message).button("OK", true).key(Input.Keys.ENTER, true).show(stage);
    }

    public void leave(){
        //Network Closing Code


        //Go to Main Menu
        ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenu());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.01f, 0f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width/sf, height/sf);
        camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);
        camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        skin.dispose();
        atlas.dispose();
    }
}