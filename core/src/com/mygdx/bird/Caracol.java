package com.mygdx.bird;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Caracol extends Actor {
    private Rectangle bounds;
    private AssetManager assetManager;
    private float speedReduction;

    public Caracol() {
        this.speedReduction = speedReduction;
        bounds = new Rectangle();
        setVisible(false);
        setSize(64, 64); // Ajustar el tamaño del caracol

    }

    @Override
    public void act(float delta) {
        moveBy(-200 * delta, 0);
        bounds.set(getX(), getY(), getWidth(), getHeight());
        if (!isVisible()) setVisible(true);
        if (getX() < -64) remove();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(assetManager.get("caracol.png", Texture.class), getX(), getY());
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public float getSpeedReduction() {
        return speedReduction;
    }
}
