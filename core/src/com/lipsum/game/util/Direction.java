package com.lipsum.game.util;

public enum Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST;

    /**
     * @return The direction when you rotate to the right from the current direction
     */
    public Direction rotateRight() {
        return switch (this) {
            case NORTH -> EAST;
            case SOUTH -> WEST;
            case EAST -> SOUTH;
            case WEST -> NORTH;
        };
    }

    /**
     * @return The direction when you rotate to the left from the current direction
     */
    public Direction rotateLeft() {
        return switch (this) {
            case NORTH -> WEST;
            case SOUTH -> EAST;
            case EAST -> NORTH;
            case WEST -> SOUTH;
        };
    }

    /**
     * @return the opposite direction of the current direction
     */
    public Direction opposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST -> WEST;
            case WEST -> EAST;
        };
    }
}