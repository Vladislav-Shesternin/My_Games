package felcon9.canonical.forester.gumanistically.game.screens

import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import felcon9.canonical.forester.gumanistically.game.actors.Aviator
import felcon9.canonical.forester.gumanistically.game.actors.BackCloud
import felcon9.canonical.forester.gumanistically.game.actors.FrontCloud
import felcon9.canonical.forester.gumanistically.game.actors.Panel
import felcon9.canonical.forester.gumanistically.game.actors.button.AButtonStyle
import felcon9.canonical.forester.gumanistically.game.actors.button.AButtonText
import felcon9.canonical.forester.gumanistically.game.actors.label.ALabelStyle
import felcon9.canonical.forester.gumanistically.game.manager.NavigationManager
import felcon9.canonical.forester.gumanistically.game.manager.SpriteManager
import felcon9.canonical.forester.gumanistically.game.utils.*
import felcon9.canonical.forester.gumanistically.game.utils.actor.disable
import felcon9.canonical.forester.gumanistically.game.utils.actor.setBounds
import felcon9.canonical.forester.gumanistically.game.utils.advanced.AdvancedGroup
import felcon9.canonical.forester.gumanistically.game.utils.advanced.AdvancedScreen
import felcon9.canonical.forester.gumanistically.util.log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import felcon9.canonical.forester.gumanistically.game.utils.Layout.Game as LG
import felcon9.canonical.forester.gumanistically.game.utils.Layout.Splash as LS

class GameScreen: AdvancedScreen() {

    companion object {
        var level = 0
            private set
        var targetCount = 0
            private set
    }

    private val backCloud    = BackCloud()
    private val frontCloud   = FrontCloud()
    private val panel        = Panel()
    private val aviator      = Aviator(true)
    private val logo         = Image(SpriteManager.SplashRegion.LOGO.region)
    private val titleImage   = Image(SpriteManager.GameRegion.TITLE_MENU.region)
    private val startButton  = AButtonText("Начать игру", AButtonStyle.button1, ALabelStyle.style(ALabelStyle.Inter.SemiBold._30))


    override fun show() {
        super.show()
        level = 0
    }

    override fun AdvancedGroup.addActorsOnGroup() {
        addBackCloud()
        addFrontCloud()
        addLogo()

        coroutine.launch {
            launch { animBackCloud() }
            launch { animFrontCloud() }
            launch {
                animLogo()
                addAviator()
                addTitle()
                addStartButton()
                runGDX { addPanel() }
            }
        }
    }

    // ------------------------------------------------------------------------
    // Add Actors
    // ------------------------------------------------------------------------

    private fun AdvancedGroup.addBackCloud() {
        runGDX {
            addActor(backCloud)
            backCloud.apply {
                setBounds(0f, HEIGHT, WIDTH, HEIGHT)
                startAnim()
            }
        }
    }

    private fun AdvancedGroup.addFrontCloud() {
        runGDX {
            addActor(frontCloud)
            frontCloud.apply {
                setBounds(0f, HEIGHT, WIDTH, HEIGHT)
                startAnim()
            }
        }
    }

    private fun AdvancedGroup.addLogo() {
        runGDX {
            addActor(logo)
            logo.apply {
                setBounds(LS.logo)
            }
        }
    }

    private fun AdvancedGroup.addPanel() {
        addActor(panel)
        panel.apply {
            setBounds(LG.panel)
            coroutine.launch { aviator.countFlow.collect { count -> runGDX { text.setText(count) } } }
        }
    }

    private suspend fun AdvancedGroup.addAviator() = suspendCoroutine<Unit> { continuation ->
        runGDX {
            addActor(aviator)
            aviator.apply {
                setBounds(0f, 0f, WIDTH, HEIGHT)
                continuation.resume(Unit)

                onEnd = { navToResult() }
            }
        }
    }

    private suspend fun AdvancedGroup.addTitle() = suspendCoroutine<Unit> { continuation ->
        runGDX {
            addActor(titleImage)
            titleImage.apply {
                disable()
                setBounds(LG.title)
                addAction(Actions.sequence(
                    Actions.alpha(0f),
                    Actions.fadeIn(0.4f),
                    Actions.run { continuation.resume(Unit) }
                ))
            }
        }
    }

    private suspend fun AdvancedGroup.addStartButton() = suspendCoroutine<Unit> { continuation ->
        runGDX {
            addActor(startButton)
            startButton.apply {
                setBounds(LG.button)
                addAction(Actions.sequence(
                    Actions.alpha(0f),
                    Actions.fadeIn(0.4f),
                    Actions.run {
                        continuation.resume(Unit)
                        setOnClickListener {
                            disable(false)
                            hideMenu { startGame() }
                        }
                    }
                ))
            }
        }
    }

    // ------------------------------------------------------------------------
    // Logic
    // ------------------------------------------------------------------------

    private suspend fun animBackCloud() = suspendCoroutine<Unit> { continuation ->
        runGDX {
            backCloud.addAction(Actions.sequence(
                Actions.moveTo(0f, 0f, 0.4f),
                Actions.run { continuation.resume(Unit) }
            ))
        }
    }

    private suspend fun animFrontCloud() = suspendCoroutine<Unit> { continuation ->
        runGDX {
            frontCloud.addAction(Actions.sequence(
                Actions.moveTo(0f, 0f, 0.4f),
                Actions.run { continuation.resume(Unit) }
            ))
        }
    }

    private suspend fun animLogo() = suspendCoroutine<Unit> { continuation ->
        runGDX {
            logo.addAction(Actions.sequence(
                Actions.parallel(
                    Actions.moveTo(LG.logo.x, LG.logo.y, 0.4f),
                    Actions.scaleTo(0.75f, 0.75f, 0.4f),
                ),
                Actions.run { continuation.resume(Unit) }
            ))
        }
    }

    private fun animPanel() {
        panel.addAction(Actions.moveTo(LG.panel.x, LG.panelY, 0.4f))
    }

    private fun hideMenu(block: () -> Unit) {
        aviator.animHideTry()
        logo.addAction(Actions.moveTo(LG.logo.x, HEIGHT, 0.5f))
        startButton.addAction(Actions.moveTo(LG.button.x, -LG.button.h, 0.5f))
        titleImage.addAction(Actions.sequence(
            Actions.fadeOut(0.5f),
            Actions.run { block() }
        ))
    }

    private fun startGame() {
        animPanel()
        aviator.goItem()

        timer { second ->
            if (second % 5 == 0) if (level < 50) level++
        }
    }

    private fun timer(block: (Int) -> Unit) {
        coroutine.launch {
            MutableStateFlow(0).also { flow -> flow.collect { second ->
                delay(1000)
                flow.value += 1
                runGDX { block(second) }
            } }
        }
    }

    private fun navToResult() {
        targetCount = aviator.countFlow.value
        mainGroup.addAction(Actions.sequence(
            Actions.fadeOut(0.7f),
            Actions.run { NavigationManager.navigate(ResultScreen(), GameScreen()) }
        ))
    }

}