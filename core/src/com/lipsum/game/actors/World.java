package com.lipsum.game.actors;

import com.lipsum.game.actions.MoveToAction;

public class World extends Layer{
    public World(){
        MyActor myActor = new MyActor(10, 40, 1, "left");
        MyActor myActor2 = new MyActor(20, 50, 2, "right");
        this.addActor(myActor2);
        this.addActor(myActor);
        MoveToAction move = new MoveToAction(myActor2, 100, 100, 10);
        myActor2.addAction(move);
    }
}
