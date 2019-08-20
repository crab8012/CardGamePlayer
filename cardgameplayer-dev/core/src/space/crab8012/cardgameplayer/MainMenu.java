package space.crab8012.cardgameplayer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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

public class MainMenu implements Screen {

    private SpriteBatch batch;
    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    protected Skin skin;

    int sf = 1; //The viewport scaling factor

    public MainMenu()
    {
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
        //Stage should control input:
        Gdx.input.setInputProcessor(stage);

        //Create Table
        Table mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.center();

        //Create buttons
        TextButton quickConnectButton = new TextButton("Quick Connect", skin);
        TextButton serverBrowserButton = new TextButton("Server Browser", skin);
        TextButton optionsButton = new TextButton("Options", skin);
        TextButton exitButton = new TextButton("Exit", skin);


        //Create Title Object
        Label gameNameLabel = new Label("Crab's Card Game Player", skin, "font", new Color(1, 0, 0, 1));
        gameNameLabel.setFontScale(3);

        //Add listeners to buttons
        quickConnectButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new TestNetwork());
            }
        });
        serverBrowserButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new Dialog("Feature Not Implemented", skin).text("Unfortunately, we don't have a\nserver browser yet. Check back later.").button("OK", true).key(Input.Keys.ENTER, true).show(stage);
            }
        });
        optionsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new SettingsMenu());
            }
        });
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new Dialog("Quit Game?", skin) {
                    protected void result (Object object) {
                        if((boolean)object) Gdx.app.exit(); //If the user clicks "Yes", object is True, and the game is closed
                    }
                }.text("Are sure that you would like to quit to the desktop?").button("Yes", true).button("No", false).key(Input.Keys.ENTER, true).key(Input.Keys.ESCAPE, false).show(stage);
            }
        });

        //Add buttons to table
        mainTable.add(gameNameLabel);
        mainTable.row();
        mainTable.add(quickConnectButton).width(400).uniform();
        mainTable.row();
        mainTable.add(serverBrowserButton).width(400).uniform();
        mainTable.row();
        mainTable.add(optionsButton).width(400).uniform();
        mainTable.row();
        mainTable.add(exitButton).width(400).uniform();

        //Add table to stage
        stage.addActor(mainTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
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