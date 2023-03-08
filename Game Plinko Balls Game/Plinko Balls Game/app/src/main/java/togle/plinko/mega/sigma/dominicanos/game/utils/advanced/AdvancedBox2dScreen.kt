package togle.plinko.mega.sigma.dominicanos.game.utils.advanced

import com.badlogic.gdx.utils.viewport.FitViewport
import togle.plinko.mega.sigma.dominicanos.game.utils.Size
import togle.plinko.mega.sigma.dominicanos.game.utils.SizeConverter
import togle.plinko.mega.sigma.dominicanos.game.utils.disposeAll
import togle.plinko.mega.sigma.dominicanos.game.box2d.WorldUtil

abstract class AdvancedBox2dScreen(
    val worldUtil: WorldUtil,
    val uiW  : Float = 500f,
    val uiH  : Float = 1000f,
    val boxW : Float = 15f,
    val boxH : Float = 30f,
): AdvancedScreen(uiW, uiH) {

    private val viewportBox2d by lazy { FitViewport(boxW, boxH) }

    val sizeConverterUIToBox = SizeConverter(Size(uiW, uiH), Size(boxW, boxH))
    val sizeConverterBoxToUI = SizeConverter(Size(boxW, boxH), Size(uiW, uiH))



    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        viewportBox2d.update(width, height, true)
    }

    override fun render(delta: Float) {
        super.render(delta)
        worldUtil.update(delta)
        worldUtil.debug(viewportBox2d.camera.combined)
    }

    override fun hide() {
        dispose()
    }

    override fun dispose() {
        super.dispose()
        disposeAll(worldUtil)
    }

}