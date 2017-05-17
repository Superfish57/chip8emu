package com.fish.chip8emu;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class chip8emu extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	public static cpu mCpu;
	Stage stage;
	Texture emuScreen;
	Texture Pixel;
	byte[] graphics;
	int x;
	int y;
	int inc;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		mCpu = new cpu();
		emuScreen = new Texture(mCpu.screenMap);
		Pixel = new Texture("pixel.png");

		MyInputProcessor inputProcessor = new MyInputProcessor();
		Gdx.input.setInputProcessor(inputProcessor);
		mCpu.initialize();
		mCpu.loadGame("games/TETRIS");

		Timer.schedule(new Timer.Task(){
						   @Override
						   public void run() {
							   mCpu.emulateCycle();
						   }
					   }
				, 0        //    (delay)
				, 1/500f     //    (seconds)
		);


	}

	double lastTime = 0;

	@Override
	public void render () {


		//for(int k = 0; k < 20; k ++) {

			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);




			graphics = mCpu.getGfx();

			drawScreen();

			Gdx.graphics.setTitle(String.valueOf(Gdx.graphics.getFramesPerSecond()));


		//}

	}

	public void drawScreen(){
		batch.begin();


		for (int i = 0; i < 32; i++) {
			for (int j = 0; j < 64; j++) {


				Sprite pixSprite = new Sprite(Pixel);
				if (graphics[i * 64 + j] != 0) {
					pixSprite.setX(j * 10);
					pixSprite.setY(310 - (i * 10));
//					System.out.println("j: " + j + " i: " + i);

					pixSprite.draw(batch);

				}
			}
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		mCpu.screenMap.dispose();


	}



}

class MyInputProcessor implements InputProcessor {

	public boolean keyDown (int keycode)
	{
		switch (keycode){
			case Input.Keys.NUM_1:
				chip8emu.mCpu.key[0x1] = 1;
				break;

			case Input.Keys.NUM_2:
				chip8emu.mCpu.key[0x2] = 1;
				break;

			case Input.Keys.NUM_3:
				chip8emu.mCpu.key[0x3] = 1;
				break;

			case Input.Keys.NUM_4:
				chip8emu.mCpu.key[0xC] = 1;
				break;

			case Input.Keys.Q:
				chip8emu.mCpu.key[0x4] = 1;
				break;

			case Input.Keys.W:
				chip8emu.mCpu.key[0x5] = 1;
				break;

			case Input.Keys.E:
				chip8emu.mCpu.key[0x6] = 1;
				break;

			case Input.Keys.R:
				chip8emu.mCpu.key[0xD] = 1;
				break;

			case Input.Keys.A:
				chip8emu.mCpu.key[0x7] = 1;
				break;

			case Input.Keys.S:
				chip8emu.mCpu.key[0x8] = 1;
				break;

			case Input.Keys.D:
				chip8emu.mCpu.key[0x9] = 1;
				break;

			case Input.Keys.F:
				chip8emu.mCpu.key[0xE] = 1;
				break;

			case Input.Keys.Z:
				chip8emu.mCpu.key[0xA] = 1;
				break;

			case Input.Keys.X:
				chip8emu.mCpu.key[0x0] = 1;
				break;

			case Input.Keys.C:
				chip8emu.mCpu.key[0xB] = 1;
				break;

			case Input.Keys.V:
				chip8emu.mCpu.key[0xF] = 1;
				break;

			case Input.Keys.SPACE:
				chip8emu.mCpu.partialReload();

		}


		return true;
	}

	public boolean keyUp (int keycode) {
		switch (keycode){
			case Input.Keys.NUM_1:
				chip8emu.mCpu.key[0x1] = 0;
				break;

			case Input.Keys.NUM_2:
				chip8emu.mCpu.key[0x2] = 0;
				break;

			case Input.Keys.NUM_3:
				chip8emu.mCpu.key[0x3] = 0;
				break;

			case Input.Keys.NUM_4:
				chip8emu.mCpu.key[0xC] = 0;
				break;

			case Input.Keys.Q:
				chip8emu.mCpu.key[0x4] = 0;
				break;

			case Input.Keys.W:
				chip8emu.mCpu.key[0x5] = 0;
				break;

			case Input.Keys.E:
				chip8emu.mCpu.key[0x6] = 0;
				break;

			case Input.Keys.R:
				chip8emu.mCpu.key[0xD] = 0;
				break;

			case Input.Keys.A:
				chip8emu.mCpu.key[0x7] = 0;
				break;

			case Input.Keys.S:
				chip8emu.mCpu.key[0x8] = 0;
				break;

			case Input.Keys.D:
				chip8emu.mCpu.key[0x9] = 0;
				break;

			case Input.Keys.F:
				chip8emu.mCpu.key[0xE] = 0;
				break;

			case Input.Keys.Z:
				chip8emu.mCpu.key[0xA] = 0;
				break;

			case Input.Keys.X:
				chip8emu.mCpu.key[0x0] = 0;
				break;

			case Input.Keys.C:
				chip8emu.mCpu.key[0xB] = 0;
				break;

			case Input.Keys.V:
				chip8emu.mCpu.key[0xF] = 0;
				break;

		}

		return true;
	}

	public boolean keyTyped (char character) {
		return false;
	}

	public boolean touchDown (int x, int y, int pointer, int button) {
		return false;
	}

	public boolean touchUp (int x, int y, int pointer, int button) {
		return false;
	}

	public boolean touchDragged (int x, int y, int pointer) {
		return false;
	}

	public boolean mouseMoved (int x, int y) {
		return false;
	}

	public boolean scrolled (int amount) {
		return false;
	}
}


