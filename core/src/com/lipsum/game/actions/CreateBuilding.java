package com.lipsum.game.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.lipsum.game.entities.Consumer;
import com.lipsum.game.entities.Producer;
import com.lipsum.game.util.Direction;
import com.lipsum.game.util.PacketType;
import com.lipsum.game.world.Coordinate;
import com.lipsum.game.world.World;

import java.util.Random;

public class CreateBuilding extends Action {
    private static final Random rand = new Random();
    private float progress = 0;
    private float speed;
    private float minLocation = 5;
    private float maxLocation = 10;

    public CreateBuilding(float speed){
        this.speed = speed;
    }
    @Override
    public boolean act(float delta) {
        progress += delta*speed;
        if (progress >= 1){
            progress = 1;
        }
        if (progress == 1){
            Coordinate c = this.getLocation();
            while(World.getInstance().tileAt(c).getBuilding() != null){
                c = this.getLocation();
            }
            if (rand.nextDouble() > 0.7){
                new Consumer(c.x(), c.y(), getDirection(), getPacketType());
            } else {
                new Producer(c.x(), c.y(), getDirection(), getPacketType());
            }
            minLocation *= 1.1;
            maxLocation *= 1.1;
            progress = 0;
        }
        return false;
    }

    private Coordinate getLocation() {
        double angle = rand.nextDouble(360);
        double distance = rand.nextDouble(minLocation, maxLocation);
        double x = distance*Math.sin(angle);
        double y = distance*Math.cos(angle);
        return new Coordinate((int) x, (int) y);
    }

    private static PacketType getPacketType(){
        return PacketType.values()[rand.nextInt(PacketType.values().length)];
    }

    private static Direction getDirection(){
        return Direction.values()[rand.nextInt(Direction.values().length)];
    }
}
