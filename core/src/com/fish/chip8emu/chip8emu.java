package com.fish.chip8emu;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class chip8emu extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	cpu mCpu;
	Stage stage;
	Texture emuScreen;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		mCpu = new cpu();
		emuScreen = new Texture(mCpu.screenMap);




		mCpu.initialize();
		mCpu.loadGame("games/PONG");
	}



	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		mCpu.emulateCycle();

		emuScreen.draw(mCpu.screenMap,100,100);



	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		mCpu.screenMap.dispose();
		stage.dispose();
	}
}
