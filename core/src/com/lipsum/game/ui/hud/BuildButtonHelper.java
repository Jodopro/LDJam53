package com.lipsum.game.ui.hud;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.lipsum.game.TextureStore;

public class BuildButtonHelper {
    public static final int SIZE = 40; // px
    public static final int SCALE = 2; // px
    public static final TextureRegion BLANK_BUTTON =
            new TextureRegion(TextureStore.BUILD_BUTTON, 0, 0, SIZE, SIZE);
    public static final TextureRegion UNPRESSED_BUTTON =
            new TextureRegion(TextureStore.BUILD_BUTTON, SIZE, 0, SIZE, SIZE);
    public static final TextureRegion PRESSED_BUTTON =
            new TextureRegion(TextureStore.BUILD_BUTTON, SIZE*2, 0, SIZE, SIZE);
    public static final TextureRegion SELECTED_BUTTON =
            new TextureRegion(TextureStore.BUILD_BUTTON, SIZE*3, 0, SIZE, SIZE);

}
