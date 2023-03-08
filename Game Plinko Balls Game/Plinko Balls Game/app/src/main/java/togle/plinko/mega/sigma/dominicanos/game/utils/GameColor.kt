package togle.plinko.mega.sigma.dominicanos.game.utils

import com.badlogic.gdx.graphics.Color

object GameColor {

    val background = rgba(75, 167, 176, 1)

    fun rgba(r: Int, g: Int, b: Int, a: Int = 1) = Color(r / 255f, g / 255f, b / 255f, a.toFloat())

}