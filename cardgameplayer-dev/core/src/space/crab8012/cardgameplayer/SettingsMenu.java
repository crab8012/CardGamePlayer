package space.crab8012.cardgameplayer;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SettingsMenu implements Screen {
    private SpriteBatch batch;
    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    protected Skin skin;

    int sf = 1; //The viewport scaling factor

    boolean changed = false;

    Preferences prefs;

    private float sfxVolume;
    float bgmVolume;

    String name;
    String serverAddress;


    public SettingsMenu(){
        atlas = new TextureAtlas("skins/clean-crispy-ui.atlas");
        skin = new Skin(Gdx.files.internal("skins/clean-crispy-ui.json"));

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth()/sf, Gdx.graphics.getHeight()/sf, camera);

        viewport.apply();

        camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);
        camera.update();

        stage = new Stage(viewport, batch);


        prefs = Gdx.app.getPreferences("settings");
        serverAddress = prefs.getString("serveraddress", "Remote Server");
        name = prefs.getString("name", "Player");
        sfxVolume = prefs.getFloat("sfxVol", 0);
        bgmVolume = prefs.getFloat("bgmVol", 0);
    }

    @Override
    public void show() {
        //Set the stage to the input processor
        Gdx.input.setInputProcessor(stage);

        //Create a Table
        Table mainTable = new Table();
        //Set the table to fill the stage
        mainTable.setFillParent(true);
        //Set the alignment of the contents in the table
        mainTable.center();

        //Create buttons
        Label fullscreenLabel = new Label("Set Fullscreen", skin);
        TextButton fullscreenButton = new TextButton(Gdx.graphics.isFullscreen() + "", skin);

        Label setRemoteServerLabel = new Label("Set Remote Server", skin);
        final TextButton setRemoteServerButton = new TextButton(serverAddress, skin);

        Label setPlayerNameLabel = new Label("Set Player Name", skin);
        final TextButton setPlayerNameButton = new TextButton(name, skin);

        TextButton advancedOptionsButton = new TextButton("Advanced", skin);
        TextButton applySettingsButton = new TextButton("Apply", skin);
        TextButton mainMenuButton = new TextButton("Cancel", skin);

        Label bgmVolumeLabel = new Label("Set BGM Volume", skin);
        final Slider bgmVolumeSlider = new Slider(0f, 1f, 0.01f, false, skin);
        final Label bgmVolumeLevelLabel = new Label(scaleSlider100(bgmVolume) + "/100", skin);
        bgmVolumeSlider.setValue(bgmVolume);

        Label sfxVolumeLabel = new Label("Set SFX Volume", skin);
        final Slider sfxVolumeSlider = new Slider(0f, 1f, 0.01f, false, skin);
        final Label sfxVolumeLevelLabel = new Label(scaleSlider100(sfxVolume) + "/100", skin);
        sfxVolumeSlider.setValue(sfxVolume);


        //Add listeners to buttons
        fullscreenButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new Dialog("Feature Not Implemented", skin).text("Unfortunately, we don't have\nfullscreen yet. Check back later.").button("OK", true).key(Input.Keys.ENTER, true).show(stage);
                changed = true;
                System.out.println("fullscreen clicked");
            }
        });
        setRemoteServerButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changed = true;
                Gdx.input.getTextInput(new Input.TextInputListener() {
                    @Override
                    public void input(String text) {
                        prefs.putString("serveraddress", text);
                        setRemoteServerButton.setText(text);
                        System.out.println(text);
                    }

                    @Override
                    public void canceled() {

                    }
                }, "Enter Server Address", "", "Server Address");

                System.out.println("setRemoteServer clicked");
            }
        });
        setPlayerNameButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("setPlayerName clicked");
                Gdx.input.getTextInput(new Input.TextInputListener() {
                    @Override
                    public void input(String text) {
                        prefs.putString("name", text);
                        setPlayerNameButton.setText(name);
                        System.out.println(name);
                    }

                    @Override
                    public void canceled() {

                    }
                }, "Enter Player Name", "", "Player Name");
                prefs.putString("name", name);
                setPlayerNameButton.setText(name);
                changed = true;
                //new Dialog("Feature Not Implemented", skin).text("Unfortunately, we don't have\nplayer names yet. Check back later.").button("OK", true).key(Input.Keys.ENTER, true).show(stage);
            }
        });
        mainMenuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(changed){
                    new Dialog("Discard Changes?", skin) {
                        protected void result (Object object) {
                            if((boolean)object) ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenu()); //If the user clicks "Yes", object is True, and the user is sent to the main menu.
                        }
                    }.text("Are sure that you would like to discard\nyour changes and go to the main menu?").button("Yes", true).button("No", false).key(Input.Keys.ENTER, true).key(Input.Keys.ESCAPE, false).show(stage);
                }else{
                    ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenu());
                }

            }
        });
        applySettingsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                new Dialog("Apply Changes?", skin) {
                    protected void result (Object object) {
                        if((boolean)object){
                            changed = false;
                            prefs.putFloat("sfxVol", sfxVolume);
                            prefs.putFloat("bgmVol", bgmVolume);
                            prefs.flush();
                        }
                    }
                }.text("Are you sure that you would like to apply these changes?").button("Yes", true).button("No", false).key(Input.Keys.ENTER, true).key(Input.Keys.ESCAPE, false).show(stage);
            }
        });
        advancedOptionsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                new Dialog("Feature Not Implemented", skin).text("There are no advanced options yet.\nPlease check back later.").button("OK", true).key(Input.Keys.ENTER, true).show(stage);
            }
        });
        bgmVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                bgmVolumeLevelLabel.setText(scaleSlider100(bgmVolumeSlider.getValue()) + "/" + scaleSlider100(bgmVolumeSlider.getMaxValue()));
                bgmVolume = bgmVolumeSlider.getValue();
                changed = true;
                //System.out.println("BGM: " + bgmVolumeSlider.getValue() + "/" + bgmVolumeSlider.getMaxValue());
            }
        });
        sfxVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sfxVolumeLevelLabel.setText(scaleSlider100(sfxVolumeSlider.getValue()) + "/" + scaleSlider100(sfxVolumeSlider.getMaxValue()));
                sfxVolume = sfxVolumeSlider.getValue();
                changed = true;
                //System.out.println("SFX: " + sfxVolumeSlider.getValue() + "/" + sfxVolumeSlider.getMaxValue());
            }
        });


        //Add buttons and stuff to the table
        mainTable.add(fullscreenLabel);
        mainTable.add(fullscreenButton);
        mainTable.row();
        mainTable.add(setRemoteServerLabel);
        mainTable.add(setRemoteServerButton);
        mainTable.row();
        mainTable.add(setPlayerNameLabel);
        mainTable.add(setPlayerNameButton);
        mainTable.row();
        mainTable.add(bgmVolumeLabel);
        mainTable.add(bgmVolumeSlider).uniform();
        mainTable.add(bgmVolumeLevelLabel).uniform();
        mainTable.row();
        mainTable.add(sfxVolumeLabel);
        mainTable.add(sfxVolumeSlider).uniform();
        mainTable.add(sfxVolumeLevelLabel).uniform();
        mainTable.row();
        mainTable.add(advancedOptionsButton);
        mainTable.add(applySettingsButton);
        mainTable.add(mainMenuButton);

        stage.addActor(mainTable);
    }

    public int scaleSlider100(float value){
        return (int)(value * 100);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.5f, 0f, .5f, 1);
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
