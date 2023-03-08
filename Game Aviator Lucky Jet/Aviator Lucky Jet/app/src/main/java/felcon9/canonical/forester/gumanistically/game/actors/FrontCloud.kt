package felcon9.canonical.forester.gumanistically.game.actors

import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import felcon9.canonical.forester.gumanistically.game.manager.SpriteManager
import felcon9.canonical.forester.gumanistically.game.screens.GameScreen
import felcon9.canonical.forester.gumanistically.game.utils.actor.setBounds
import felcon9.canonical.forester.gumanistically.game.utils.advanced.AdvancedGroup
import felcon9.canonical.forester.gumanistically.game.utils.runGDX
import felcon9.canonical.forester.gumanistically.util.log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import felcon9.canonical.forester.gumanistically.game.utils.Layout.FrontCloud as LFC

class FrontCloud: AdvancedGroup() {

    private val listCloud = listOf(
        Image(SpriteManager.GameRegion.FRONT_CLOUD_1.region),
        Image(SpriteManager.GameRegion.FRONT_CLOUD_2.region),
        Image(SpriteManager.GameRegion.FRONT_CLOUD_3.region),
        Image(SpriteManager.GameRegion.FRONT_CLOUD_4.region),
        Image(SpriteManager.GameRegion.FRONT_CLOUD_1.region)
    )

    private val cloudGroup = AdvancedGroup()


    override fun sizeChanged() {
        super.sizeChanged()
        if(width > 0 && height > 0) addActorsOnGroup()
    }

    override fun dispose() {
        super.dispose()

        log("disp")

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
            setBounds(LFC.frontCloud)

            var nx = LFC.cloud.x

            listCloud.onEach { image ->
                addActor(image)
                with(LFC.cloud) {
                    image.setBounds(nx, y, w, h)
                    nx += w
                }
            }
        }
    }

    // ------------------------------------------------------------------------
    // Logic
    // ------------------------------------------------------------------------

    fun startAnim() {
        coroutine.launch {
            var time: Float

            MutableStateFlow(0.001f).also { flow -> flow.collect { runGDX {
                time = if (GameScreen.level >= 15) 7f else (25 - GameScreen.level).toFloat()
                cloudGroup.addAction(Actions.sequence(
                    Actions.moveTo(LFC.endX, LFC.frontCloud.y, time),
                    Actions.moveTo(LFC.frontCloud.x, LFC.frontCloud.y),
                    Actions.run { flow.value += 0.001f }
                ))
            } } }
        }
    }

}
