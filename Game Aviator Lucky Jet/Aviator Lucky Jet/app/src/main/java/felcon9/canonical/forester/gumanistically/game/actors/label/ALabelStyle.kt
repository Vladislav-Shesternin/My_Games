package felcon9.canonical.forester.gumanistically.game.actors.label

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label
import felcon9.canonical.forester.gumanistically.game.manager.FontTTFManager

object ALabelStyle {

    fun style(type: Type, color: Color = Color.WHITE) = Label.LabelStyle(type.font, color)

    interface Type {
        val font: BitmapFont
    }

    object Inter {
        enum class Light(override val font: BitmapFont): Type {
            _57(FontTTFManager.LightFont.font_57.font),
            _123(FontTTFManager.LightFont.font_123.font),
        }
        enum class Regular(override val font: BitmapFont): Type {
            _27(FontTTFManager.RegularFont.font_27.font),
        }
        enum class SemiBold(override val font: BitmapFont): Type {
            _30(FontTTFManager.SemiBoldFont.font_30.font),
        }
    }

}