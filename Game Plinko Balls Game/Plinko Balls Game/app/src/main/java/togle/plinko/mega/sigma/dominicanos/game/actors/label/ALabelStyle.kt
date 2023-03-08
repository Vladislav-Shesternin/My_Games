package togle.plinko.mega.sigma.dominicanos.game.actors.label

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label
import togle.plinko.mega.sigma.dominicanos.game.manager.FontTTFManager

object ALabelStyle {

    fun style(type: Type, color: Color = Color.WHITE) = Label.LabelStyle(type.font, color)

    interface Type {
        val font: BitmapFont
    }

    enum class Inter(override val font: BitmapFont) : Type {
        _48(FontTTFManager.ExtraBoldFont.font_48.font),
        _40(FontTTFManager.ExtraBoldFont.font_40.font),
        _20(FontTTFManager.ExtraBoldFont.font_20.font),
    }
}