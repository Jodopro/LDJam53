package com.lipsum.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;

import java.util.stream.IntStream;

public class TextureStore {
    public final static Texture edgeTile = new Texture("edge_tile.png");
    public final static Texture backgroundTile = new Texture("background_tile3.png");

    public static final Animation<Texture> CONVEYOR_BELT_STRAIGHT =
            loadMultiFileAnimation("textures/belt/belty_boi%d.png", 8, 0.1f);

    public static final Animation<Texture> CONVEYOR_BELT_LEFT =
            loadMultiFileAnimation("textures/bendy_left/bendy_left_belty_boi%d.png", 8, 0.1f);

    public static final Animation<Texture> CONVEYOR_BELT_RIGHT =
            loadMultiFileAnimation("textures/bendy_right/bendy_right_belty_boi%d.png", 8, 0.1f);

    public static final Animation<Texture> SPLITTER =
            loadMultiFileAnimation("textures/split/splitty_boi%d.png", 8, 0.1f);

    public static final Animation<Texture> MERGER =
            loadMultiFileAnimation("textures/merge/mergy_boi%d.png", 8, 0.1f);

    public static final Texture PACKET = new Texture(Gdx.files.internal("textures/packet.png"));

    public static final Texture BUILD_BUTTON = new Texture(Gdx.files.internal("textures/build_button.png"));



    /**
     * Loads multiple texture files into one animation
     * @param filePathFormat a format for the filepath, i.e. "textures/belty_boi%d.png", where %d will be filled with
     *                       frame number 1 up to and including n
     * @param nFrames the number of frames
     * @param frameDuration the duration of each frame
     * @return the animation object
     */
    private static Animation<Texture> loadMultiFileAnimation(String filePathFormat, int nFrames, float frameDuration) {
        Array<Texture> textureArray = new Array<>(IntStream.range(1, nFrames + 1).boxed()
                .map(index -> String.format(filePathFormat, index))
                .map(Gdx.files::internal)
                .map(Texture::new)
                .toArray(Texture[]::new));
        return new Animation<>(frameDuration, textureArray);
    }
}
