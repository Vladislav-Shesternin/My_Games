package felcon9.canonical.forester.gumanistically.game.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.Viewport

val Texture.region: TextureRegion get() = TextureRegion(this)

val Viewport.zeroScreenVector: Vector2 get() = project(Vector2(0f, 0f))

val TextureEmpty get() = Texture(1, 1, Pixmap.Format.RGBA8888)



fun disposeAll(vararg disposable: Disposable) {
    disposable.forEach { it.dispose() }
}

fun List<Disposable>.disposeAll() {
    onEach { it.dispose() }
}

fun Batch.beginend(block: Batch.() -> Unit = { }) {
    begin()
    block()
    end()
}

fun Batch.endbegin(block: Batch.() -> Unit = { }) {
    end()
    block()
    begin()
}

fun InputMultiplexer.addProcessors(vararg processor: InputProcessor) {
    processor.onEach { addProcessor(it) }
}

fun runGDX(block: () -> Unit) {
    Gdx.app.postRunnable { block() }
}

data class Size(
    var width : Float = 0f,
    var height: Float = 0f,
) {
    fun set(width: Float, height: Float) {
        this.width = width
        this.height = height
    }

    fun set(size: Size) {
        width = size.width
        height = size.height
    }
}