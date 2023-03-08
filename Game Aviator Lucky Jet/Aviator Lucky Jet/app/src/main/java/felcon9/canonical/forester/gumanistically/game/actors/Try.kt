package felcon9.canonical.forester.gumanistically.game.actors

import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Align
import felcon9.canonical.forester.gumanistically.game.manager.SpriteManager
import felcon9.canonical.forester.gumanistically.game.utils.Layout
import felcon9.canonical.forester.gumanistically.game.utils.actor.setBounds
import felcon9.canonical.forester.gumanistically.game.utils.advanced.AdvancedGroup
import felcon9.canonical.forester.gumanistically.util.log
import felcon9.canonical.forester.gumanistically.game.utils.Layout.Try as LT

class Try: AdvancedGroup() {

    private val up   = Image(SpriteManager.GameRegion.ARROW_UP.region)
    private val down = Image(SpriteManager.GameRegion.ARROW_DOWN.region)
    private val hand = Image(SpriteManager.GameRegion.TRY.region)



    override fun sizeChanged() {
        super.sizeChanged()
        if(width > 0 && height > 0) addActorsOnGroup()
    }


    private fun addActorsOnGroup() {
        addUpDown()
        addHand()
    }

    // ------------------------------------------------------------------------
    // Add Actors
    // ------------------------------------------------------------------------

    private fun addUpDown() {
        addActors(up, down)
        up.apply {
            setBounds(LT.up)
            addAction(Actions.forever(
                Actions.sequence(
                    Actions.moveBy(0f, 15f, 0.5f),
                    Actions.moveBy(0f, -15f, 0.5f),
                )
            ))
        }
        down.apply {
            setBounds(LT.down)
            addAction(Actions.forever(
                Actions.sequence(
                    Actions.moveBy(0f, -15f, 0.5f),
                    Actions.moveBy(0f, 15f, 0.5f),
                )
            ))
        }
    }

    private fun addHand() {
        addActor(hand)
        hand.apply {
            setBounds(LT.hand)
            setOrigin(Align.center)
            addAction(Actions.forever(Actions.sequence(
                Actions.scaleBy(-0.2f, -0.2f, 0.5f),
                Actions.scaleBy(0.2f, 0.2f, 0.5f),
            )))
        }
    }

}
