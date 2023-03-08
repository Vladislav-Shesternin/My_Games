package felcon9.canonical.forester.gumanistically.game.actors.checkbox

import com.badlogic.gdx.graphics.g2d.TextureRegion

data class ACheckBoxStyle(
    val default: TextureRegion,
    val checked: TextureRegion,
) {
//    companion object {
//        val bet get() = CheckBoxStyle(
//            default = SpriteManager.GameRegion.CB_DEFF.region,
//            checked = SpriteManager.GameRegion.CB_CHECK.region,
//        )
//    }
}