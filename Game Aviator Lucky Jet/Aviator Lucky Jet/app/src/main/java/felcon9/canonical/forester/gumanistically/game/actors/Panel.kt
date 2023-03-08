package felcon9.canonical.forester.gumanistically.game.actors

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import felcon9.canonical.forester.gumanistically.game.actors.label.ALabelStyle
import felcon9.canonical.forester.gumanistically.game.manager.SpriteManager
import felcon9.canonical.forester.gumanistically.game.utils.actor.setBounds
import felcon9.canonical.forester.gumanistically.game.utils.advanced.AdvancedGroup
import felcon9.canonical.forester.gumanistically.game.utils.Layout.Panel as LP

class Panel: AdvancedGroup() {

    private val panel = Image(SpriteManager.GameRegion.PANEL.region)
    val text          = Label("0", ALabelStyle.style(ALabelStyle.Inter.Light._57))


    override fun sizeChanged() {
        super.sizeChanged()
        if(width > 0 && height > 0) addActorsOnGroup()
    }


    private fun addActorsOnGroup() {
        addAndFillActor(panel)
        addText()
    }

    // ------------------------------------------------------------------------
    // Add Actors
    // ------------------------------------------------------------------------
    private fun addText() {
        addActor(text)
        text.setBounds(LP.text)
        text.setAlignment(Align.center)
    }

}