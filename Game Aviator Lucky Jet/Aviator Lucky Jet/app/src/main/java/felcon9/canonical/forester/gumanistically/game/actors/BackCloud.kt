package felcon9.canonical.forester.gumanistically.game.actors

import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import felcon9.canonical.forester.gumanistically.game.manager.SpriteManager
import felcon9.canonical.forester.gumanistically.game.screens.GameScreen
import felcon9.canonical.forester.gumanistically.game.utils.actor.setBounds
import felcon9.canonical.forester.gumanistically.game.utils.advanced.AdvancedGroup
import felcon9.canonical.forester.gumanistically.game.utils.runGDX
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import felcon9.canonical.forester.gumanistically.game.utils.Layout.BackCloud as LBC

class BackCloud: AdvancedGroup() {

    private val listCloud = listOf(
        Image(SpriteManager.GameRegion.BACK_CLOUD_1.region),
        Image(SpriteManager.GameRegion.BACK_CLOUD_2.region),
        Image(SpriteManager.GameRegion.BACK_CLOUD_3.region),
    )

    private val lastCloud  = Image(SpriteManager.GameRegion.BACK_CLOUD_1.region)
    private val cloudGroup = AdvancedGroup()



    override fun sizeChanged() {
        super.sizeChanged()
        if(width > 0 && height > 0) addActorsOnGroup()
    }


    private fun addActorsOnGroup() {
        addClouds()
    }

    // ------------------------------------------------------------------------
    // Add Actors
    // ------------------------------------------------------------------------

    private fun addClouds() {
        addActor(cloudGroup)
        cloudGroup.apply {
            setBounds(LBC.backCloud)

            var nx = LBC.cloud.x

            listCloud.onEach { image ->
                addActor(image)
                with(LBC.cloud) {
                    image.setBounds(nx, y, w, h)
                    nx += w
                }
            }

            addActor(lastCloud)
            lastCloud.setBounds(LBC.lastCloud)
        }
    }

    // ------------------------------------------------------------------------
    // Logic
    // ------------------------------------------------------------------------

    fun startAnim() {
        coroutine.launch {
            var time: Float

            MutableStateFlow(0.001f).also { flow -> flow.collect { runGDX {
                time = if (GameScreen.level >= 15) 10f else (30 - GameScreen.level).toFloat()
                cloudGroup.addAction(Actions.sequence(
                    Actions.moveTo(LBC.endX, LBC.backCloud.y, time),
                    Actions.moveTo(LBC.backCloud.x, LBC.backCloud.y),
                    Actions.run { flow.value += 0.001f }
                ))
            } } }
        }
    }

}
