package com.lipsum.game.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class LayeredActor extends Actor {
    private int z;

    public LayeredActor(int z){
        this.z = z;
    }
    public int getZ(){
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }
}
