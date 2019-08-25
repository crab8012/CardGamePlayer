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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import space.crab8012.cardgameplayer.gameobjects.Enums;
import space.crab8012.cardgameplayer.gameobjects.Player;
import space.crab8012.cardgameplayer.gameobjects.ServerCommand;
import space.crab8012.cardgameplayer.payloads.GameWinnerPayload;
import space.crab8012.cardgameplayer.payloads.PlayerMovePayload;
import space.crab8012.cardgameplayer.payloads.PlayerObjectPayload;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RockPaperScissors implements Screen {
    private SpriteBatch batch;
    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    protected Skin skin;

    public static Enums.RPSMOVES move;

    Preferences prefs;

    String pName = null;

    int sf = 1; //The viewport scaling factor

    String serverIP = "192.168.2.138";

    public RockPaperScissors() {
        prefs = Gdx.app.getPreferences("settings");


        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("skins/PublicSans-Black.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 10;
        BitmapFont bitmapfont = gen.generateFont(parameter);
        gen.dispose();

        pName = prefs.getString("name", "PLAYER");

        atlas = new TextureAtlas("skins/clean-crispy-ui.atlas");
        skin = new Skin(Gdx.files.internal("skins/clean-crispy-ui.json"), atlas);
        skin.add("other-font", bitmapfont);

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth() / sf, Gdx.graphics.getHeight() / sf, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
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
        Label roomNameLabel = new Label("Rock Paper Scissors Game", skin, "font", new Color(1, 1, 1, 1));
        roomNameLabel.setFontScale(3);
        //Create buttons
        TextButton playGameButton = new TextButton("Send to Server", skin);
        TextButton mainMenuButton = new TextButton("Main Menu", skin);

        //Add listeners to buttons
        playGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sendPlayerObject(pName, "default", 5);
            }
        });
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                leave();
            }
        });

        //Add buttons to table
        mainTable.add(roomNameLabel);
        mainTable.row();
        mainTable.add(playGameButton);
        mainTable.row();
        mainTable.add(mainMenuButton).width(400).uniform();

        //Add table to stage
        stage.addActor(mainTable);
        move = getMove();
    }

    public void sendPlayerObject(String name, String icon, int score) {
        try {
            //Set up the basic networking crap
            Socket s = new Socket(prefs.getString("serveraddress", serverIP), 8888);
            OutputStream os = s.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            InputStream is = s.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);

            //Create the test player object
            Player testPlayer = new Player(name, icon);
            testPlayer.setScore(score);


            while (true) {
                Object o = ois.readObject();

                ServerCommand c = null;
                if (o instanceof ServerCommand) {
                    c = (ServerCommand) o;
                } else {
                    System.out.println(o);
                }

                if (c.getCommand().equals(Enums.COMMANDS.QUIT.name())) {
                    break;
                } else if (c.getCommand().equals(Enums.COMMANDS.GETMOVE.name())) {
                    PlayerMovePayload payload = new PlayerMovePayload(testPlayer, move);
                    oos.writeObject(new ServerCommand(Enums.COMMANDS.GETMOVE.name(), payload));
                } else if (c.getCommand().equals(Enums.COMMANDS.GETPLAYER.name())) {
                    oos.writeObject(new ServerCommand(Enums.COMMANDS.SENDPLAYER.name(), new PlayerObjectPayload(testPlayer)));
                } else if (c.getCommand().equals(Enums.COMMANDS.SENDWINNER.name())) {
                    if (c.getPayload() instanceof GameWinnerPayload) {
                        notifyOfWinner((GameWinnerPayload) c.getPayload());
                    } else {
                        c.getPayload().getClass().getName();
                    }
                }


                oos.flush();
            }

            oos.close();
            s.close(); // Close the network socket.

        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }

    }

    public Enums.RPSMOVES getMove() {
        new Dialog("RPS MOVE SELECTION", skin) {
            protected void result(Object object) {
                move = (Enums.RPSMOVES) object;
            }
        }.text("Which Symbol do you wish to throw?\n\n\tChoose Wisely!!").button("ROCK", Enums.RPSMOVES.ROCK).button("PAPER", Enums.RPSMOVES.PAPER).button("SCISSORS", Enums.RPSMOVES.SCISSORS)
                .key(Input.Keys.R, Enums.RPSMOVES.ROCK).key(Input.Keys.P, Enums.RPSMOVES.PAPER).key(Input.Keys.S, Enums.RPSMOVES.SCISSORS).show(stage);
        return move;
    }

    public void notifyOfWinner(GameWinnerPayload payload) {
        String winnerName = payload.getPlayer().getName();
        String winnerMove = payload.getMove().name();

        notificationDialog("PLAYER WINS!!!", "Player " + winnerName + " WINS!\nThey used " + winnerMove + "!");
    }

    public void notificationDialog(String title, String message) {
        new Dialog("Message Recieved", skin).text(message).button("OK", true).key(Input.Keys.ENTER, true).show(stage);
    }

    public void leave() {
        //Network Closing Code


        //Go to Main Menu
        ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
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
        viewport.update(width / sf, height / sf);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
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