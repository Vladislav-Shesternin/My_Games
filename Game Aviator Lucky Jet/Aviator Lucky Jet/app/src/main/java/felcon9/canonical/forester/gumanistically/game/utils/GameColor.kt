package felcon9.canonical.forester.gumanistically.game.utils

import com.badlogic.gdx.graphics.Color

object GameColor {

    val red    = rgba(229, 5, 57, 1f)
    val yellow = rgba(255, 220, 20, 1f)

    fun rgba(r: Int, g: Int, b: Int, a: Float = 1f) = Color(r / 255f, g / 255f, b / 255f, a)

}