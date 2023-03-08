package togle.plinko.mega.sigma.dominicanos.game.utils.actor

import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener

class ClickListener(val actor: Actor) {

    private var onClickBlock: (Actor) -> Unit = { }



    private fun Actor.getListener(sound: Sound? = null) = object : InputListener() {
        var isWithin = false

        override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
            touchDragged(event, x, y, pointer)
            return true
        }

        override fun touchDragged(event: InputEvent?, x: Float, y: Float, pointer: Int) {
            isWithin = x in 0f..width && y in 0f..height
        }

        override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
            if (isWithin) {
                isWithin = false
                onClickBlock(actor)
            }
        }
    }

    fun setOnClickListener(sound: Sound?/* = SoundUtil.CLICK*/, block: (Actor) -> Unit) {
        onClickBlock = block
        with(actor) { addListener(getListener(sound)) }
    }

}

fun Actor.toClickable() = ClickListener(this)