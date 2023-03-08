package felcon9.canonical.forester.gumanistically.game.actors.button

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import felcon9.canonical.forester.gumanistically.game.utils.actor.disable

class AButtonText(
    text: String, style: AButtonStyle? = null, labelStyle: Label.LabelStyle, val alignment: Int = Align.center
): AButton(style) {

    val label = Label(text, labelStyle)

    override fun sizeChanged() {
        super.sizeChanged()
        if (width > 0 && height > 0) addActorsOnGroup()
    }

    private fun addActorsOnGroup() {
        addLabel()
    }

    // ------------------------------------------------------------------------
    // Add Actors
    // ------------------------------------------------------------------------

    private fun addLabel() {
        addAndFillActor(label)
        label.apply {
            disable()
            setAlignment(alignment)
            wrap = true
        }
    }

    // ------------------------------------------------------------------------
    // Logic
    // ------------------------------------------------------------------------

    fun setText(text: String) {
        label.setText(text)
    }

}