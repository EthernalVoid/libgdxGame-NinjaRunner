package wiser.development.NinjaRunner.screens;

import java.util.ArrayList;

import wiser.development.NinjaRunner.utils.Assets;
import wiser.development.NinjaRunner.utils.Settings;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.TimeUtils;


public class MainMenuScreen implements Screen{
	public static final float CAMERA_WIDTH = 45f;
	public static final float CAMERA_HEIGHT = 31.5f;
	

	Game game;

	OrthographicCamera guiCam;
	SpriteBatch batcher;
	Rectangle soundBounds;
	Rectangle highscoresBounds;
	Rectangle helpBounds;
	Vector3 touchPoint;
	Rectangle levelsBounds;
	Rectangle levelSelectBounds[];
	String levelNum;
	TextButton button;
	TextButtonStyle textButtonStyle;
	BitmapFont font;
	Skin skin;
	TextureAtlas buttonAtlas;

	int levelSelect, i, j;

	public MainMenuScreen (Game game) {
		this.game = game;

		guiCam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
		guiCam.position.set(CAMERA_WIDTH/2, CAMERA_HEIGHT/2, 0);
		batcher = new SpriteBatch();
		soundBounds = new Rectangle(0, 0, 3f,3f);
		levelsBounds= new Rectangle(0, CAMERA_HEIGHT/5, CAMERA_WIDTH, 3*CAMERA_HEIGHT/5);
		touchPoint = new Vector3();
		ArrayList<Rectangle> levelSelectBounds = new ArrayList<Rectangle>();
		levelSelect=0;
	}

	public void update () {
		Settings.load();
		if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (levelsBounds.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				for (int z=0; z<=Settings.levelReached() ; z++){
					if(levelSelectBounds[z] != null){
						if (levelSelectBounds[z].contains(touchPoint.x, touchPoint.y)) {
							game.setScreen(new GameScreen(game, z));

						}


					}
				}

			}

			if (soundBounds.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				Settings.toggleSound();
				if (Settings.isSoundEnabled())
					Assets.music.play();
				else
					Assets.music.pause();
			}
		}
	}

	long last = TimeUtils.nanoTime();

	public void draw () {
		GL20 gl = Gdx.gl;
		gl.glClearColor(0,0,0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		guiCam.update();
		batcher.setProjectionMatrix(guiCam.combined);

		batcher.disableBlending();


		batcher.enableBlending();
		batcher.begin();
		batcher.draw(Assets.logo, 0, 3*CAMERA_HEIGHT/5 , CAMERA_WIDTH, 2*CAMERA_HEIGHT/5);
		
		if(Settings.isSoundEnabled()){
			batcher.draw(Assets.soundOn, 0, 0 , 3f, 3f);
		}else{
			batcher.draw(Assets.soundOff, 0, 0 , 3f, 3f);

		}
		int j=2;
		int k=0;
		levelSelectBounds= new Rectangle[Settings.levelReached()+1];
		for(int i=1; i<=Settings.levelReached() ; i++){
			if(i>7){
				j=1;
				k=7;

			}
			if(i>14){
				j=2;
				k=14;
			}
			if(i <= Settings.levelReached()){
				if (i == Settings.levelReached()){
					batcher.draw(Assets.blockTexture, CAMERA_WIDTH*(i-k-1) /7 , j*CAMERA_HEIGHT/5, CAMERA_WIDTH/8, CAMERA_HEIGHT/5 );
					batcher.enableBlending();
					batcher.draw(Assets.playOverlay, CAMERA_WIDTH*(i-k- 1) /7 , j*CAMERA_HEIGHT/5, CAMERA_WIDTH/8, CAMERA_HEIGHT/5 );		
					batcher.disableBlending();
				}
				else{
					batcher.draw(Assets.blockTexture, CAMERA_WIDTH*(i-k-1) /7 , j*CAMERA_HEIGHT/5, CAMERA_WIDTH/8, CAMERA_HEIGHT/5 );


				}
				levelSelectBounds[i]= new Rectangle( CAMERA_WIDTH*(i-k-1) /7 , (j)*CAMERA_HEIGHT/5, CAMERA_WIDTH/8, CAMERA_HEIGHT/5 );

			}
			else{
				batcher.draw(Assets.blockTexture, CAMERA_WIDTH*(i-k-1) /7 , j*CAMERA_HEIGHT/5, CAMERA_WIDTH/8, CAMERA_HEIGHT/5 );
				batcher.enableBlending();
				batcher.draw(Assets.na, CAMERA_WIDTH*(i-k-1) /7 , j*CAMERA_HEIGHT/5, CAMERA_WIDTH/8, CAMERA_HEIGHT/5 );
				batcher.disableBlending();
				levelSelectBounds[i]=null;
			}
			

			levelNum= ""+ i;
			batcher.enableBlending();
			Assets.smallfont.setScale(0.1f, 0.1f);
			Assets.smallfont.draw(batcher, levelNum, CAMERA_WIDTH*(i-k-0.75f) /7 , (j+0.5f)*CAMERA_HEIGHT/5);
			batcher.disableBlending();
		}	


		batcher.end();

//		if (TimeUtils.nanoTime() - last > 2000000000) {
//			Gdx.app.log("SuperJumper",
//					"version: " + Gdx.app.getVersion() + ", memory: " + Gdx.app.getJavaHeap() + ", " + Gdx.app.getNativeHeap()
//					+ ", native orientation:" + Gdx.input.getNativeOrientation() + ", orientation: " + Gdx.input.getRotation()
//					+ ", accel: " + (int)Gdx.input.getAccelerometerX() + ", " + (int)Gdx.input.getAccelerometerY() + ", "
//					+ (int)Gdx.input.getAccelerometerZ() + ", apr: " + (int)Gdx.input.getAzimuth() + ", " + (int)Gdx.input.getPitch()
//					+ ", " + (int)Gdx.input.getRoll());
//			last = TimeUtils.nanoTime();
//		}
	}

	@Override
	public void render (float delta) {
		update();
		draw();
	}

	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void show () {
	}

	@Override
	public void hide () {
	}

	@Override
	public void pause () {
		
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
	}
}
