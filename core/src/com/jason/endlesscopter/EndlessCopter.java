package com.jason.endlesscopter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class EndlessCopter extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
    Texture gameover;

    Texture[] helicopters;
    Texture bottomBuilding;
    Texture topBuilding;
    float gap = 400;
    BitmapFont font;


    int propellerState = 0;
    float helicopterY = 0;
    double velocity = 0;
    Rectangle helicopterRectangle;

    int gameState = 0;
    double gravity = 0.5;
    float maxBuildingOffset;
    Random randomGenerator;
    float buildingVelocity = 4;
    int numberOfBuildings = 4;
    float[] buildingX = new float[numberOfBuildings];
    float[] buildingOffset = new float[numberOfBuildings];
    float distanceBetweenBuildings;
    Rectangle topBuildingRectangle[];
    Rectangle bottomBuildingRectangle[];
    int score = 0;
    int scoringBuilding = 0;

	
	@Override
	public void create () {
        batch = new SpriteBatch();
        background = new Texture("bg.jpg");
        gameover = new Texture("gameover.png");
        helicopterRectangle = new Rectangle();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);

        helicopters = new Texture[2];
        helicopters[0] = new Texture("helicopter1.png");
        helicopters[1] = new Texture("helicopter2.png");

        topBuilding = new Texture("topbuilding.png");
        bottomBuilding = new Texture("bottombuilding.png");
        maxBuildingOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
        randomGenerator = new Random();
        distanceBetweenBuildings = Gdx.graphics.getWidth() * 3 / 4 + 100;
        topBuildingRectangle = new Rectangle[numberOfBuildings];
        bottomBuildingRectangle = new Rectangle[numberOfBuildings];

        startGame();
	}


	public void startGame() {

        helicopterY = Gdx.graphics.getHeight() / 2 - helicopters[0].getHeight() / 2;

        for (int i = 0; i < numberOfBuildings; i++) {

            buildingOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

            buildingX[i] = Gdx.graphics.getWidth() / 2 - topBuilding.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenBuildings;

            topBuildingRectangle[i] = new Rectangle();
            bottomBuildingRectangle[i] = new Rectangle();

        }

    }


    @Override
    public void render () {

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gameState == 1) {

            if (Gdx.input.justTouched() || Gdx.input.isTouched()) {

                if(buildingX[scoringBuilding] < Gdx.graphics.getWidth() / 2) {

                    score++;

                    if(scoringBuilding < numberOfBuildings - 1) {
                        scoringBuilding++;
                    } else {
                        scoringBuilding = 0;
                    }

                }

                velocity = -5;

            }

            for (int i = 0; i < numberOfBuildings; i++) {

                if (buildingX[i] < - topBuilding.getWidth()) {

                    buildingX[i] += numberOfBuildings * distanceBetweenBuildings;
                    buildingOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

                } else {

                    buildingX[i] = buildingX[i] - buildingVelocity;


                }

                batch.draw(topBuilding, buildingX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + buildingOffset[i]);
                batch.draw(bottomBuilding, buildingX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomBuilding.getHeight() + buildingOffset[i]);
                topBuildingRectangle[i] = new Rectangle(buildingX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + buildingOffset[i], topBuilding.getWidth(), topBuilding.getHeight());
                bottomBuildingRectangle[i] = new Rectangle(buildingX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomBuilding.getHeight() + buildingOffset[i], bottomBuilding.getWidth(), bottomBuilding.getHeight());
            }



            if (helicopterY > 0) {

                velocity = velocity + gravity;
                helicopterY -= velocity;

            } else {

                gameState = 2;

            }

        } else if (gameState == 0) {

            if (Gdx.input.justTouched()) {

                gameState = 1;

            }

        } else if (gameState == 2) {

            batch.draw(gameover, Gdx.graphics.getWidth() / 2 - gameover.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameover.getHeight() / 2);

            if (Gdx.input.justTouched()) {

                gameState = 1;
                startGame();
                score = 0;
                scoringBuilding = 0;
                velocity = 0;

            }

        }

        if (propellerState == 0) {
            propellerState = 1;
        } else {
            propellerState = 0;
        }




        batch.draw(helicopters[propellerState], Gdx.graphics.getWidth() / 2 - helicopters[propellerState].getWidth() / 2, helicopterY);

        font.draw(batch, String.valueOf(score), 100, 200);

        batch.end();

        helicopterRectangle.set(Gdx.graphics.getWidth() / 2 - 80, helicopterY + helicopters[propellerState].getHeight() / 2 - 50, helicopters[propellerState].getWidth() / 2, helicopters[propellerState].getHeight() / 2);



        for (int i = 0; i < numberOfBuildings; i++) {

            if (Intersector.overlaps(helicopterRectangle, topBuildingRectangle[i]) || Intersector.overlaps(helicopterRectangle, bottomBuildingRectangle[i])) {
                gameState = 2;
            }

        }


    }
}
