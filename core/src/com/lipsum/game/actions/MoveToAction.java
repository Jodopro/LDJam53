package com.lipsum.game.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static java.lang.Math.abs;

public class MoveToAction extends Action {
    float diff_x;
    float diff_y;
    int target_x;
    int target_y;
    public MoveToAction(Actor actor, int x, int y, int speed){
        setActor(actor);
        float diff_x_tmp = x - actor.getX();
        float diff_y_tmp = y - actor.getY();
        float factor = speed*speed/((diff_y_tmp*diff_y_tmp + diff_x_tmp*diff_x_tmp));
        this.diff_x = diff_x_tmp*factor;
        this.diff_y = diff_y_tmp*factor;
        this.target_x = x;
        this.target_y = y;
    }
    @Override
    public boolean act(float delta) {
        System.out.println("hoi");
        System.out.println(diff_x*delta);
        System.out.println(diff_x*delta);
        if (abs(target_x-actor.getX()) <= abs(diff_x*delta)){
            actor.setX(target_x);
            actor.setY(target_y);
            return true;
        } else {
            actor.moveBy(diff_x*delta, diff_y*delta);
            return false;
        }
    }
}
