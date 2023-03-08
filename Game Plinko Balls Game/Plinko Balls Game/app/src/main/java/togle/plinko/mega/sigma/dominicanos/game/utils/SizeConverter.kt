package togle.plinko.mega.sigma.dominicanos.game.utils

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor

class SizeConverter(
    val fromSize: Size,
    val toSize  : Size,
) {

    private val fromOnePercentX get() = fromSize.width / 100f
    private val fromOnePercentY get() = fromSize.height / 100f

    private val toOnePercentX get() = toSize.width / 100f
    private val toOnePercentY get() = toSize.height / 100f



    private fun getPercentX(x: Float) = x / fromOnePercentX
    private fun getPercentY(y: Float) = y / fromOnePercentY

    fun getSizeX(x: Float) = getPercentX(x) * toOnePercentX
    fun getSizeY(y: Float) = getPercentY(y) * toOnePercentY

    fun getSize(x: Float, y: Float) = Vector2(getSizeX(x), getSizeY(y))
    fun getSize(vector2: Vector2) = Vector2(getSizeX(vector2.x), getSizeY(vector2.y))

    fun setSize(actor: Actor, width: Float, height: Float) {
        actor.width = getSizeX(width)
        actor.height = getSizeY(height)
    }

    fun setPosition(actor: Actor, x: Float, y: Float) {
        actor.x = getSizeX(x)
        actor.y = getSizeY(y)
    }

    fun setBounds(
        actor: Actor,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
    ) {
        setPosition(actor, x, y)
        setSize(actor, width, height)
    }

    // Только когда [toOnePercentX] это 1 процент по X для Box2D.
    fun getScale(width: Float): Float {
        val PHYSIC_EDITOR_W = 1f

        val figmaPercentX = getPercentX(width)
        val boxPercentX   = PHYSIC_EDITOR_W / toOnePercentX
        return figmaPercentX / boxPercentX
    }


}