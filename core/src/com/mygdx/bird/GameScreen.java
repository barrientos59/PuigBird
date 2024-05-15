package com.mygdx.bird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;

import java.util.Iterator;

public class GameScreen implements Screen {
    final Bird game;
    OrthographicCamera camera;
    Stage stage;
    Player player;
    boolean dead;
    Array<Pipe> obstacles;
    long lastObstacleTime;
    float score;
    float gameSpeed = 200;
    Pill lorazepam;
    Caracol caracol;
    long pilledTime;
    boolean pilled;
    boolean lorazepamTouch;
    boolean caracolTouch;
    int lastPill;

    public GameScreen(final Bird gam) {
        this.game = gam;
        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        player = new Player();
        player.setManager(game.manager);
        stage = new Stage();
        stage.getViewport().setCamera(camera);
        stage.addActor(player);
        // create the obstacles array and spawn the first obstacle
        obstacles = new Array<Pipe>();
        spawnObstacle();
        score = 0;
        lastPill = 0;
    }

    @Override
    public void render(float delta) {
        dead = false;
        // clear the screen with a color
        ScreenUtils.clear(0.3f, 0.8f, 0.8f, 1);
        // tell the camera to update its matrices.
        camera.update();
        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);
        // begin a new batch
        game.batch.begin();
        game.batch.draw(game.manager.get("background.png", Texture.class), 0, 0);
        game.batch.end();
        // Stage batch: Actors
        stage.getBatch().setProjectionMatrix(camera.combined);
        stage.draw();
        if (Gdx.input.justTouched()) {
            player.impulso();
        }
        stage.act();
        // Comprova que el jugador no es surt de la pantalla.
        // Si surt per la part inferior, game over
        if (player.getBounds().y > 480 - 45){
            player.setY( 480 - 45 );
        }
        if (player.getBounds().y < 0 - 45) {
            dead = true;
        }

        if (TimeUtils.nanoTime() - lastObstacleTime >= 750000000 && lastPill == 5) {
            spawnLorazepam();
            lorazepamTouch = true;
            lastPill = 0;
        }

        // Ajusta el tiempo de espera antes de invocar spawnCaracol() para que sea la mitad del tiempo de la pastilla
        if (TimeUtils.nanoTime() - lastObstacleTime >= 375000000 && lastPill == 3 && !caracolTouch) {
            spawnCaracol(); // Llamar a spawnCaracol() para que aparezca el caracol
            caracolTouch = true; // Marcar que el caracol ha sido generado
        }

        if (pilled && TimeUtils.nanoTime() >= pilledTime) {
            pilled = false;
            player.setPilled(false);
            player.remove();
            stage.addActor(player);
        }

        if (lorazepamTouch) {
            if (lorazepam.getBounds().overlaps(player.getBounds())) {
                pilled = true;
                lorazepamTouch = false;
                pilledTime = TimeUtils.nanoTime() + 10000000000L;
                lorazepam.remove();

                // Parpadear la imagen del jugador mientras está "pilled"
                final float blinkDuration = 0.2f;
                final float blinkInterval = 0.1f;
                final boolean[] isPilled = { true }; // Variable para verificar si el jugador está en modo "pilled"
                Timer.schedule(new Timer.Task() {
                    int count = 0;
                    boolean visible = true;
                    @Override
                    public void run() {
                        visible = !visible;
                        player.setVisible(visible);
                        count++;

                        if (isPilled[0] && TimeUtils.nanoTime() < pilledTime) { // Verificar si el jugador está en modo "pilled"
                            if (count >= (blinkDuration / blinkInterval)) {
                                count = 0;
                            }
                        } else { // Si el jugador ya no está "pilled", restablecer su estado normal
                            player.setPilled(false);
                            player.setVisible(true);
                            isPilled[0] = false; // Establecer el modo "pilled" como falso
                            this.cancel();
                        }
                    }
                }, 0, blinkInterval);
            }
        }

        if (caracolTouch) {
            if (caracol.getBounds().overlaps(player.getBounds())) {
                gameSpeed -= 200; // Reducir la velocidad del juego al recoger el caracol
                caracol.remove();
                caracolTouch = false;
            }
        }


        // Comprova si cal generar un obstacle nou
        if (TimeUtils.nanoTime() - lastObstacleTime > 1500000000){
            spawnObstacle();
            if(!pilled){
                lastPill++;
            }
        }
        // Comprova si les tuberies colisionen amb el jugador
        Iterator<Pipe> iter = obstacles.iterator();
        while (iter.hasNext()) {
            Pipe pipe = iter.next();
            if (pipe.getBounds().overlaps(player.getBounds()) && !pilled) {
                dead = true;
            }
        }

        // Treure de l'array les tuberies que estan fora de pantalla
        iter = obstacles.iterator();
        while (iter.hasNext()) {
            Pipe pipe = iter.next();
            if (pipe.getX() < -64) {
                obstacles.removeValue(pipe, true);
            }
        }

        game.batch.begin();
        game.smallFont.draw(game.batch, "Score: " + (int)score, 10, 470);
        game.batch.end();
        //La puntuació augmenta amb el temps de joc
        score += Gdx.graphics.getDeltaTime();

        // Incrementa la velocidad cada 5 puntos en el score
        if ((int)score % 5 == 0 && (int)score > 0) {
            gameSpeed += 3; // Aumenta la velocidad
            for (Pipe pipe : obstacles) {
                pipe.setGameSpeed(gameSpeed); // Actualiza la velocidad de los obstáculos
            }
        }

        if(dead) {
            game.lastScore = (int)score;
            if(game.lastScore > game.topScore){
                game.topScore = game.lastScore;
            }
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
    }
    @Override
    public void show() {
    }
    @Override
    public void hide() {
    }
    @Override
    public void pause() {
    }
    @Override
    public void resume() {
    }
    @Override
    public void dispose() {
    }
    private void spawnObstacle() {
        float holey = MathUtils.random(50, 230);
        Pipe pipe1 = new Pipe(gameSpeed);
        pipe1.setX(800);
        pipe1.setY(holey - 230);
        pipe1.setUpsideDown(true);
        pipe1.setManager(game.manager);
        obstacles.add(pipe1);
        stage.addActor(pipe1);
        Pipe pipe2 = new Pipe(gameSpeed);
        pipe2.setX(800);
        pipe2.setY(holey + 200);
        pipe2.setUpsideDown(false);
        pipe2.setManager(game.manager);
        obstacles.add(pipe2);
        stage.addActor(pipe2);
        lastObstacleTime = TimeUtils.nanoTime();
    }
    private void spawnLorazepam() {
        float holey = MathUtils.random(50, 230);
        Pill lorazepam = new Pill();
        lorazepam.setX(800);
        lorazepam.setY(holey);
        lorazepam.setAssetManager(game.manager);
        stage.addActor(lorazepam);
        this.lorazepam = lorazepam;
    }
    private void spawnCaracol() {
        float holey = MathUtils.random(50, 230);
        Caracol caracol = new Caracol();
        caracol.setX(800);
        caracol.setY(holey);
        caracol.setAssetManager(game.manager);
        stage.addActor(caracol);
        this.caracol = caracol;

    }
}
