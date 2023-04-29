package com.lipsum.game.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.SnapshotArray;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Layer extends Group {
    @Override
    public void addActor(Actor a){
        if (!(a instanceof LayeredActor)){
            throw new IllegalArgumentException("Provided Actor is not a LayeredActor");
        }
        super.addActor(a);
    }
//    @Override
//    public SnapshotArray<Actor> getChildren() {
//        SnapshotArray<Actor> list = super.getChildren();
//        System.out.println(list);
//        list.sort((x, y) -> ((LayeredActor) x).getZ() - ((LayeredActor) y).getZ());
//        System.out.println(list);
//        return list;
//    }
    @Override
    public void act (float delta) {
        super.getChildren().sort((x, y) -> ((LayeredActor) x).getZ() - ((LayeredActor) y).getZ());
        super.act(delta);
    }
//    @Override
//    public void draw (Batch batch, float parentAlpha) {
//        Iterable<Actor> list = getChildren().select(x -> x instanceof MyActor);
//        Stream<Actor> l = StreamSupport.stream(list.spliterator(), false).sorted((x, y) -> ((MyActor) x).getZ() - ((MyActor) y).getZ());
//        for (Actor a:l.toList()){
//            a.draw(batch, parentAlpha);
//        }
//    }
}
