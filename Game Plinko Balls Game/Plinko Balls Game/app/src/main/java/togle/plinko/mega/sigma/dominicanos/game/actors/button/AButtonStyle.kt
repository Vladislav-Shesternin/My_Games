package togle.plinko.mega.sigma.dominicanos.game.actors.button

import com.badlogic.gdx.graphics.g2d.TextureRegion
import togle.plinko.mega.sigma.dominicanos.game.manager.SpriteManager
import togle.plinko.mega.sigma.dominicanos.game.utils.TextureEmpty
import togle.plinko.mega.sigma.dominicanos.game.utils.region

data class AButtonStyle(
    val default : TextureRegion,
    val pressed : TextureRegion,
    val disabled: TextureRegion? = null,
) {
    
    companion object {
        val btn get() = AButtonStyle(
            default = SpriteManager.GameRegion.BTN_DEF.region,
            pressed = SpriteManager.GameRegion.BTN_DIS.region,
            disabled = SpriteManager.GameRegion.BTN_DIS.region,
        )
        val play get() = AButtonStyle(
            default = SpriteManager.GameRegion.PLAY_DEF.region,
            pressed = SpriteManager.GameRegion.PLAY_DIS.region,
            disabled = SpriteManager.GameRegion.PLAY_DIS.region,
        )
        val plus get() = AButtonStyle(
            default = SpriteManager.GameRegion.PLUS_DEF.region,
            pressed = SpriteManager.GameRegion.PLUS_DIS.region,
            disabled = SpriteManager.GameRegion.PLUS_DIS.region,
        )
        val minus get() = AButtonStyle(
            default = SpriteManager.GameRegion.MINUS_DEF.region,
            pressed = SpriteManager.GameRegion.MINUS_DIS.region,
            disabled = SpriteManager.GameRegion.MINUS_DIS.region,
        )
    }
    
}