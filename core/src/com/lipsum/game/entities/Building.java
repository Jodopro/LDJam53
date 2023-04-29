package com.lipsum.game.entities;

import com.lipsum.game.LDJam53;
import com.lipsum.game.event.EventQueue;
import com.lipsum.game.factory.factories.BuildingFactory;

import static java.lang.Math.abs;

public class Building extends Entity{
    protected int gridX;
    protected int gridY;
    protected Building northBuilding;
    protected Building southBuilding;
    protected Building eastBuilding;
    protected Building westBuilding;
    protected Packet packet;
    public Building(int x, int y){
        this.gridX = x;
        this.gridY = y;
        setBounds(x*50, y*50, 50, 50);

        BuildingFactory.getInstance().getAllManagedObjects().forEach(
               b -> {
                   if (b.gridX == x){
                        if (b.gridY == y+1) {
                            b.southBuilding=this;
                            northBuilding = b;
                        } else if (b.gridY == y-1){
                            b.northBuilding=this;
                            southBuilding = b;
                        }
                    } else if (gridY == this.gridY) {
                       if (b.gridX == x+1) {
                           b.westBuilding=this;
                           eastBuilding = b;
                       } else if (b.gridX == x-1){
                           b.eastBuilding=this;
                           westBuilding = b;
                       }
                    }
               }
        );
        LDJam53.machineGroup.addActor(this);
    }
    @Override
    public void onDispose() {
        //TODO: do onDispose() in Building
    }

    public void addPacket(Packet p){
        if (p == null){
            throw new IllegalStateException("Building already had a packet");
        } else {
            this.packet = p;
        }
    }

    public boolean hasPacket(){
        return packet != null;
    }
}
