package felcon9.canonical.forester.gumanistically.game.actors.masks.normal

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import felcon9.canonical.forester.gumanistically.game.utils.advanced.AdvancedGroup
import felcon9.canonical.forester.gumanistically.game.utils.zeroScreenVector

class Mask(
    private val mask: TextureRegion? = null,
) : AdvancedGroup() {

    private lateinit var screenCoordinates: Vector2
    private lateinit var screenSize       : Vector2

    private var mX: Float = 0f
    private var mY: Float = 0f
    private var mW: Float = 0f
    private var mH: Float = 0f



    override fun draw(batch: Batch?, parentAlpha: Float) {
        if (stage != null) {
            batch?.flush()
            updateCoordinatesAndSize()
            drawScissor(batch, parentAlpha)
        }
    }



    private fun drawSuper(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
    }

    private fun updateCoordinatesAndSize() {
        screenCoordinates = localToScreenCoordinates(Vector2())
        screenSize        = stage.viewport.project(Vector2(width, height))

        mX = screenCoordinates.x
        mY = Gdx.graphics.height - screenCoordinates.y
        mW = screenSize.x - stage.viewport.zeroScreenVector.x
        mH = screenSize.y - stage.viewport.zeroScreenVector.y
    }

    private fun drawScissor(batch: Batch?, parentAlpha: Float) {
        Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST)
        Gdx.gl.glScissor(mX.toInt(), mY.toInt(), mW.toInt(), mH.toInt())

        if (mask != null) batch?.drawMask(parentAlpha) else drawSuper(batch, parentAlpha)
        Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST)
    }

    private fun Batch.drawMask(parentAlpha: Float) {
        Gdx.gl.glColorMask(false, false, false, true)

        setBlendFunction(GL20.GL_ONE, GL20.GL_ZERO)

        setColor(0f, 0f, 0f, parentAlpha)
        draw(mask, x, y, width, height)

        drawMasked(parentAlpha)
    }

    private fun Batch.drawMasked(parentAlpha: Float) {
        setBlendFunction(GL20.GL_ZERO, GL20.GL_SRC_ALPHA)
        drawSuper(this, parentAlpha)
        flush()

        Gdx.gl.glColorMask(true, true, true, true)
        setBlendFunction(GL20.GL_DST_ALPHA, GL20.GL_ONE_MINUS_DST_ALPHA)
        drawSuper(this, parentAlpha)
        flush()

        setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
    }

}