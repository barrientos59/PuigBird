package com.mygdx.bird;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Pipe extends Actor {
    Rectangle bounds;
    boolean upsideDown;
    AssetManager manager;

    private float gameSpeed;

    public Pipe(float gameSpeed) {
        this.gameSpeed = gameSpeed;
        setSize(64, 230);
        bounds = new Rectangle();
        setVisible(false);
    }
    @Override
    public void act(float delta) {
        moveBy(-gameSpeed * delta, 0); // actualiza el movimiento con la velocidad del juego
        bounds.set(getX(), getY(), getWidth(), getHeight());
        if (!isVisible()) {
            setVisible(true);
        }
        if (getX() < -64) {
            remove();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(manager.get(upsideDown ? "pipe_up.png" : "pipe_down.png", Texture.class), getX(), getY());
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isUpsideDown() {
        return upsideDown;
    }

    public void setUpsideDown(boolean upsideDown) {
        this.upsideDown = upsideDown;
    }

    public void setManager(AssetManager manager) {
        this.manager = manager;
    }
    public void setGameSpeed(float gameSpeed) {
        this.gameSpeed = gameSpeed;
    }
}
