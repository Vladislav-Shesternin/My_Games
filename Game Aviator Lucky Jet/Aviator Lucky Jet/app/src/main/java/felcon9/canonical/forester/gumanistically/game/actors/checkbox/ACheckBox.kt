package felcon9.canonical.forester.gumanistically.game.actors.checkbox

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import felcon9.canonical.forester.gumanistically.game.utils.advanced.AdvancedGroup
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ACheckBox(
    style: ACheckBoxStyle? = null,
) : AdvancedGroup() {

    private val defaultImage = if (style != null)  Image(style.default) else Image()
    private val checkImage   = (if (style != null) Image(style.checked) else Image()).apply { isVisible = false }

    private var onCheckBlock: (Boolean) -> Unit = { }

    var checkBoxGroup: ACheckBoxGroup? = null

    val checkFlow = MutableStateFlow(false)



    init {
        addAndFillActors(getActors())
        addListener(getListener())

        collectCheckFlow()
    }



    private fun getActors() = listOf<Actor>(
        defaultImage,
        checkImage,
    )

    private fun collectCheckFlow() {
        coroutine.launch { checkFlow.collect { isCheck -> onCheckBlock(isCheck) } }
    }

    private fun getListener() = object : InputListener() {
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
                if (checkBoxGroup != null) {
                    if (checkFlow.value.not()) check()
                } else {
                    if (checkFlow.value) uncheck() else check()
                }
            }
        }
    }



    fun check() {
        checkBoxGroup?.let {
            it.currentCheckedCheckBox?.uncheck()
            it.currentCheckedCheckBox = this
        }

        defaultImage.isVisible = false
        checkImage.isVisible   = true

        checkFlow.value = true
    }

    fun uncheck() {
        checkBoxGroup?.let { it.currentCheckedCheckBox == null }

        defaultImage.isVisible = true
        checkImage.isVisible   = false

        checkFlow.value = false
    }

    fun checkAndDisable() {
        check()
        disable()
    }

    fun uncheckAndEnabled() {
        uncheck()
        enable()
    }

    fun enable() {
        touchable = Touchable.enabled
    }

    fun disable() {
        touchable = Touchable.disabled
    }

    fun setStyle(style: ACheckBoxStyle) {
        defaultImage.drawable = TextureRegionDrawable(style.default)
        checkImage.drawable   = TextureRegionDrawable(style.checked)
    }

    fun setOnCheckListener(block: (Boolean) -> Unit) {
        onCheckBlock = block
    }

}