package com.fish.chip8emu;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class chip8emu extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	cpu mCpu;
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

		mCpu.initialize();
		mCpu.loadGame("games/PONG");



	}



	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		mCpu.emulateCycle();

		graphics = mCpu.getGfx();



		batch.begin();



		for(int i = 0; i < 32; i++){
			for(int j = 0; j < 64; j++){


				Sprite pixSprite = new Sprite(Pixel);
				if(graphics[i*64+j] != 0) {
					pixSprite.setX(j*10);
					pixSprite.setY(310 - (i*10));
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

		stage.dispose();
	}



}
