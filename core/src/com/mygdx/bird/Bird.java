package com.mygdx.bird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ScreenUtils;

public class Bird extends Game {
	SpriteBatch batch;
	BitmapFont smallFont, bigFont;
	AssetManager manager;
	int topScore;
	int lastScore;



	public void create() {
		topScore = 0;
		lastScore = 0;
		manager = new AssetManager();
		manager.load("bird.png", Texture.class);
		manager.load("pastilla.png", Texture.class);
		manager.load("caracol.png", Texture.class);
		manager.load("pipe_up.png", Texture.class);
		manager.load("pipe_down.png", Texture.class);
		manager.load("background.png", Texture.class);
		manager.load("caracol.png", Texture.class);

		manager.finishLoading();

		batch = new SpriteBatch();
		// Create bitmap fonts from TrueType font
		FreeTypeFontGenerator generator = new
				FreeTypeFontGenerator(Gdx.files.internal("8bitOperatorPlus-Bold.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter params = new
				FreeTypeFontGenerator.FreeTypeFontParameter();
		params.size = 22;
		params.borderWidth = 2;
		params.borderColor = Color.BLACK;
		params.color = Color.WHITE;
		smallFont = generator.generateFont(params); // font size 22 pixels
		params.size = 50;
		params.borderWidth = 5;
		bigFont = generator.generateFont(params); // font size 50 pixels
		generator.dispose(); // don't forget to dispose to avoid memory leaks!
		this.setScreen(new MainMenuScreen(this));
	}

	public void render() {
		super.render(); // important!
	}
	public void dispose() {
	}

}
