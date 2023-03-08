package togle.plinko.mega.sigma.dominicanos.game.utils

import com.badlogic.gdx.math.Vector2

object Layout {

    object Menu {
        val game    = LayoutData(93f, 654f, 313f, 193f)
        val privacy = LayoutData(93f, 403f, 313f, 193f)
        val exit    = LayoutData(93f, 152f, 313f, 193f)
    }

    object Game {
        // Actor

        val aShadow  = LayoutData(230f, 927f, 40f, 40f)
        val aPlay    = LayoutData(166f, 162f, 168f, 165f)
        val aPlus    = LayoutData(357f, 67f, 72f, 72f)
        val aMinus   = LayoutData(72f, 67f, 72f, 72f)
        val aStavka  = LayoutData(144f, 67f, 213f, 72f)
        val aBalance = LayoutData(67f, 0f, 366f, 45f)

        // Body

        val bBalkSpace   = 42f
        val bBalkSize    = Size(20f, 20f)
        val bBalkPosList = listOf(
            Vector2(54f, 477f),
            Vector2(85f, 552f),
            Vector2(116f, 627f),
            Vector2(147f, 702f),
            Vector2(178f, 777f),
            Vector2(209f, 852f),
        )

        val bKopilka = LayoutData(69f, 392f, 52f, 30f, hs = 10f)
    }

    data class LayoutData(
        val x: Float = 0f,
        val y: Float = 0f,
        val w: Float = 0f,
        val h: Float = 0f,
        // horizontal space
        val hs: Float = 0f,
        // vertical space
        val vs: Float = 0f,
    ) {

        fun position() = Vector2(x, y)
        fun size() = Size(w, h)

    }

}












