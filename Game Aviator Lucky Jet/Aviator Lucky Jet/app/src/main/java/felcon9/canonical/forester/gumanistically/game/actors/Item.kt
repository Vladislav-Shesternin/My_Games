package felcon9.canonical.forester.gumanistically.game.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Align
import felcon9.canonical.forester.gumanistically.game.manager.SpriteManager
import felcon9.canonical.forester.gumanistically.game.utils.advanced.AdvancedGroup

class Item(val type: Type): AdvancedGroup() {

    private val image = Image(if (type == Type.TARGET) SpriteManager.GameRegion.TARGET.region else SpriteManager.GameRegion.ENEMY.region)

    var onRenderBlock: (Item) -> Unit = {}


    override fun sizeChanged() {
        super.sizeChanged()
        if(width > 0 && height > 0) addActorsOnGroup()
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        onRenderBlock(this)
    }

    private fun addActorsOnGroup() {
        addAndFillActor(image)
        image.setOrigin(Align.center)
        image.addAction(Actions.forever(Actions.sequence(
            Actions.scaleBy(-0.2f, -0.2f, 0.2f),
            Actions.scaleBy(0.2f, 0.2f, 0.2f),
        )))
    }



    enum class Type {
        TARGET, ENEMY
    }

}