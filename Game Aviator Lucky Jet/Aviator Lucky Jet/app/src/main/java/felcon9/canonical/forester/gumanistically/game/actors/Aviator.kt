package felcon9.canonical.forester.gumanistically.game.actors

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Align
import felcon9.canonical.forester.gumanistically.game.manager.SpriteManager
import felcon9.canonical.forester.gumanistically.game.screens.GameScreen
import felcon9.canonical.forester.gumanistically.game.utils.actor.disable
import felcon9.canonical.forester.gumanistically.game.utils.actor.setBounds
import felcon9.canonical.forester.gumanistically.game.utils.advanced.AdvancedGroup
import felcon9.canonical.forester.gumanistically.game.utils.runGDX
import felcon9.canonical.forester.gumanistically.util.log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import felcon9.canonical.forester.gumanistically.game.utils.Layout.Aviator as LA

class Aviator(private val isAnim: Boolean): AdvancedGroup() {

    private val aviator  = Image(SpriteManager.GameRegion.AVIATOR.region)
    private val tryActor = Try()
    private val listItem = (List(5) { Item(Item.Type.TARGET) } + List(2) { Item(Item.Type.ENEMY) }).shuffled()

    private val listener = object : InputListener() {
        override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
            touchDragged(event, x, y, pointer)
            tryActor.addAction(Actions.fadeOut(0.3f))
            return true
        }

        override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
            tryActor.addAction(Actions.fadeIn(0.3f))
        }

        override fun touchDragged(event: InputEvent?, x: Float, y: Float, pointer: Int) {
            if (isEnd) return
            aviator.addAction(Actions.moveTo(
                LA.aviatorShow.x,
                when {
                    y < 0f   -> 0f
                    y > 566f -> 550f
                    else     -> y - 90f
                },
                0.2f
            ))
        }
    }

    private var isEnd = false

    private val itemFlow  = MutableSharedFlow<Item>(listItem.size)
    val countFlow         = MutableStateFlow(0)

    var onEnd: () -> Unit = {}


    override fun sizeChanged() {
        super.sizeChanged()
        if(width > 0 && height > 0) {
            addActorsOnGroup()
            addListener(listener)
        }
    }


    private fun addActorsOnGroup() {
        addAviator()
        addTry()
        addItemList()
    }

    // ------------------------------------------------------------------------
    // Add Actors
    // ------------------------------------------------------------------------

    private fun addAviator() {
        addActor(aviator)
        aviator.apply {
            disable()
            if (isAnim) {
                setBounds(LA.aviatorHide)
                addAction(Actions.moveTo(LA.aviatorShow.x, LA.aviatorShow.y, 0.5f))
            } else setBounds(LA.aviatorShow)

            setOrigin(Align.center)

            coroutine.launch {
                MutableStateFlow(0.001f).also { flow -> flow.collect { runGDX {
                    val angle = (-12..12).shuffled().first().toFloat()
                    val time  = (70..100).shuffled().first() / 100f

                    addAction(Actions.sequence(
                        Actions.rotateBy(angle, time),
                        Actions.rotateBy(angle * -1, time),
                        Actions.run { flow.value += 0.001f }
                    ))
                } } }
            }
        }
    }

    private fun addTry() {
        addActor(tryActor)
        tryActor.apply {
            disable()
            setBounds(LA.tryActor)
        }
    }

    private fun addItemList() {
        listItem.onEach { item ->
            addActor(item)
            item.setBounds(LA.itemStart)
        }
    }

    // ------------------------------------------------------------------------
    // Logic
    // ------------------------------------------------------------------------

    fun animHideTry() {
        tryActor.addAction(Actions.moveTo(width, LA.tryActor.y, 0.5f))
    }

    fun goItem() {
        coroutine.launch {
            listItem.onEach { item ->
                log("emit")
                itemFlow.emit(item)
                delay((1000..2000).shuffled().first().toLong())
            }
        }

        coroutine.launch {
            var time: Float
            itemFlow.collect { item ->
                time = (1..15).shuffled().first().toFloat()
                runGDX { item.apply {
                    y = (0..620).shuffled().first().toFloat()
                    addAction(Actions.moveTo(-200f, y, time))
                    onRenderBlock = {
                        when {
                            it.x.toInt() <= -100 -> {
                                clearActions()
                                it.x = LA.itemStart.x
                                itemFlow.tryEmit(this)
                            }
                            it.x.toInt() in (135..400) && (it.y + (it.height / 2)) in (aviator.y..(aviator.y + aviator.height)) -> {
                                clearActions()
                                it.x = LA.itemStart.x
                                itemFlow.tryEmit(this)

                                if (isEnd.not()) if (it.type == Item.Type.ENEMY) end() else countFlow.value++
                            }
                        }
                    }
                } }
                delay((500..1000).shuffled().first().toLong())
            }
        }
    }

    private fun end() {
        isEnd = true
        disable()
        aviator.apply {
            clearActions()
            addAction(Actions.sequence(
                Actions.parallel(
                    Actions.rotateTo(-60f, 1f),
                    Actions.moveTo(340f, -500f, 2f)
                ),
                Actions.run(onEnd)
            ))
        }

    }

}