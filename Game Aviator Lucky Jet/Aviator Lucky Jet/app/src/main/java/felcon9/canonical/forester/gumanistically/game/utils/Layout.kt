package felcon9.canonical.forester.gumanistically.game.utils

import com.badlogic.gdx.math.Vector2

object Layout {

    object Splash {
        val logo     = LayoutData(538f,251f,485f,220f)
        val loader   = LayoutData(746f,124f,69f,69f)
        val aviatorStart  = LayoutData(691f,509f,175f,87f)
        val aviatorEnd    = Vector2(942f,720f)
    }

    object Game {
        val logo   = Vector2(599f, 492f)
        val title  = LayoutData(503f,246f,555f,179f)
        val button = LayoutData(599f,83f,364f,96f)
        val panel  = LayoutData(663f,-100f,233f,94f)
        val panelY = 37f
    }

    object Result {
        val target     = LayoutData(634f,498f,123f,123f)
        val targetText = LayoutData(777f,479f,149f,160f)
        val title      = LayoutData(570f,274f,421f,167f)
        val result     = LayoutData(818f,313f,47f,38f)
        val percent    = LayoutData(814f,275f,56f,38f)
        val restartStart = LayoutData(128f,-100f,384f,96f)
        val restartEnd   = Vector2(367f, 81f)
        val sendStart    = LayoutData(1419f,-100f,384f,96f)
        val sendEnd      = Vector2(809f, 81f)
    }

    object Panel {
        val text = LayoutData(101f,8f,118f,77f)
    }

    object BackCloud {
        val backCloud = LayoutData(0f, 105f, 5966f, 511f)

        val cloud      = LayoutData(0f,0f,1560f,511f)
        val lastCloud  = LayoutData(4406f,0f,1560f,511f)

        val endX = -4406f
    }

    object FrontCloud {
        val frontCloud = LayoutData(0f, -216f, 7800f, 1152f)

        val cloud = LayoutData(0f,0f,1560f,1152f)
        val endX  = -6240f
    }

    object Aviator {
        val aviatorShow = LayoutData(-88f,284f,519f,153f)
        val aviatorHide = LayoutData(-519f,284f,519f,153f)
        val tryActor    = LayoutData(1249f,181f,233f,359f)

        val itemStart = LayoutData(1570f,0f,99f,99f)
    }

    object Try {
        val up   = LayoutData(8f,279f,29f,80f)
        val down = LayoutData(8f,0f,29f,80f)
        val hand = LayoutData(0f,115f,233f,87f)
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












