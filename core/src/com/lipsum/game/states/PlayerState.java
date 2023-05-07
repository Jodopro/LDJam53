package com.lipsum.game.states;

import com.lipsum.game.utils.Threeple;

import java.util.concurrent.BrokenBarrierException;

public class PlayerState {
    private class single {
        // omdat het geen double is
        float value;

        public single(float value) {this.value = value;}
    }

    private Threeple<single, single, single> theState;

    private PlayerState() {
        theState = new Threeple<>(
            new single(100f),
            new single(0f),
            new single(100f)
        );
    }
    private static PlayerState instance;

    public static PlayerState getInstance() {
        if (instance == null) {
            instance = new PlayerState();
        }
        return instance;
    }

    public float getMoneyLeft() { return theState.first.value; }
    public float getScore() {return theState.second.value; }
    public float getHealth() {return theState.third.value; }

    public void setMoney(float value) {this.theState.first.value = value; }
    public void addMoney(float salaris) {this.theState.first.value += salaris; }
    public void subtractMoney(float belastingenVanDeRoverheid) {this.theState.first.value -= belastingenVanDeRoverheid; }
    public void setScore(float value) {this.theState.second.value = value; }
    public void addScore(float earnedPoints) {this.theState.second.value += earnedPoints; }
    public void subtractScore(float penalty) {this.theState.second.value -= penalty; }
    public void setHealth(float value) {this.theState.third.value = value; }
    public void addHealth(float healthPotion) {this.theState.third.value += healthPotion; }
    public void subtractHealth(float damageTaken) {this.theState.third.value -= damageTaken; }
}
