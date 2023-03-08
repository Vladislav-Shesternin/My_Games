package felcon9.canonical.forester.gumanistically.game.actors.button

import com.badlogic.gdx.graphics.g2d.TextureRegion
import felcon9.canonical.forester.gumanistically.game.manager.SpriteManager

data class AButtonStyle(
    val default : TextureRegion,
    val pressed : TextureRegion,
    val disabled: TextureRegion? = null,
) {
    
    companion object {
        val button1 get() = AButtonStyle(
            default = SpriteManager.GameRegion.BTN_1.region,
            pressed = SpriteManager.GameRegion.BTN_2.region,
            disabled = SpriteManager.GameRegion.BTN_2.region,
        )
        val button2 get() = AButtonStyle(
            default = SpriteManager.GameRegion.BTN_2.region,
            pressed = SpriteManager.GameRegion.BTN_1.region,
            disabled = SpriteManager.GameRegion.BTN_1.region,
        )
    }
    
}