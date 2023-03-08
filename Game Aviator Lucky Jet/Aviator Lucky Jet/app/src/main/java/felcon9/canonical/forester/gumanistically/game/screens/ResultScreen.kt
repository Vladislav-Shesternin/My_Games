package felcon9.canonical.forester.gumanistically.game.screens

import android.content.Intent
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import felcon9.canonical.forester.gumanistically.BuildConfig.APPLICATION_ID
import felcon9.canonical.forester.gumanistically.R
import felcon9.canonical.forester.gumanistically.game.actors.Koleso
import felcon9.canonical.forester.gumanistically.game.actors.button.AButtonStyle
import felcon9.canonical.forester.gumanistically.game.actors.button.AButtonText
import felcon9.canonical.forester.gumanistically.game.actors.label.ALabelStyle
import felcon9.canonical.forester.gumanistically.game.game
import felcon9.canonical.forester.gumanistically.game.manager.NavigationManager
import felcon9.canonical.forester.gumanistically.game.manager.SpriteManager
import felcon9.canonical.forester.gumanistically.game.utils.GameColor
import felcon9.canonical.forester.gumanistically.game.utils.actor.setBounds
import felcon9.canonical.forester.gumanistically.game.utils.advanced.AdvancedGroup
import felcon9.canonical.forester.gumanistically.game.utils.advanced.AdvancedScreen
import felcon9.canonical.forester.gumanistically.game.utils.runGDX
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import felcon9.canonical.forester.gumanistically.game.utils.Layout.Result as LR

class ResultScreen: AdvancedScreen() {

    private val koleso      = Koleso()
    private val target      = Image(SpriteManager.GameRegion.TARGET.region)
    private val targetLabel = Label("", ALabelStyle.style(ALabelStyle.Inter.Light._123, GameColor.red))
    private val titleImage  = Image(SpriteManager.GameRegion.TITLE_RESULT.region)
    private val resultText  = Label("", ALabelStyle.style(ALabelStyle.Inter.Regular._27, GameColor.yellow))
    private val percentText = Label("", ALabelStyle.style(ALabelStyle.Inter.Regular._27, GameColor.yellow))
    private val restartButton = AButtonText("Заново", AButtonStyle.button1, ALabelStyle.style(ALabelStyle.Inter.SemiBold._30))
    private val sendButton    = AButtonText("Поделиться", AButtonStyle.button2, ALabelStyle.style(ALabelStyle.Inter.SemiBold._30, GameColor.red))




    override fun AdvancedGroup.addActorsOnGroup() {
        addKoleso()
        addTarget()
        addTargetCount()
        addTitle()
        addRestartButton()
        addSendButton()

        coroutine.launch {
            animKoleso()
            animTarget()
            animTitle()
            animResult()
            animButtons()
        }
    }

    // ------------------------------------------------------------------------
    // Add Actors
    // ------------------------------------------------------------------------

    private fun AdvancedGroup.addKoleso() {
        addActor(koleso)
        koleso.apply {
            setBounds(0f, 0f, WIDTH, HEIGHT)
            addAction(Actions.alpha(0f))
            startAnim()
        }
    }

    private fun AdvancedGroup.addTarget() {
        addActor(target)
        target.apply {
            setBounds(LR.target)
            addAction(Actions.alpha(0f))
        }
    }

    private fun AdvancedGroup.addTargetCount() {
        addActor(targetLabel)
        targetLabel.apply {
            setBounds(LR.targetText)
            addAction(Actions.alpha(0f))
            setText(GameScreen.targetCount)
        }
    }

    private fun AdvancedGroup.addTitle() {
        addActors(titleImage, resultText, percentText)
        titleImage.apply {
            setBounds(LR.title)
            addAction(Actions.alpha(0f))
        }
        resultText.apply {
            setBounds(LR.result)
            setAlignment(Align.center)
            addAction(Actions.alpha(0f))

            setText(GameScreen.targetCount)
        }
        percentText.apply {
            setBounds(LR.percent)
            setAlignment(Align.center)
            addAction(Actions.alpha(0f))

            setText("${(10..99).shuffled().first()}%")
        }
    }

    private fun AdvancedGroup.addRestartButton() {
        addActor(restartButton)
        restartButton.apply {
            setBounds(LR.restartStart)
            addAction(Actions.alpha(0f))

            setOnClickListener {
                mainGroup.addAction(Actions.sequence(
                    Actions.fadeOut(1f),
                    Actions.run { NavigationManager.navigate(GameScreen()) }
                ))
            }
        }
    }

    private fun AdvancedGroup.addSendButton() {
        addActor(sendButton)
        sendButton.apply {
            setBounds(LR.sendStart)
            addAction(Actions.alpha(0f))

            setOnClickListener {
                val text = "${"\uD83D\uDEE9"}  ${game.activity.getString(R.string.app_name)}  ${"\uD83D\uDEE9"}\n" +
                        "Мой рекорд: ${GameScreen.targetCount}\n" +
                        "Скачай и посмотрим сколько наберёш Ты!"

                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_SUBJECT, text)
                    putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=$APPLICATION_ID")
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, "Отправить:")
                game.activity.startActivity(shareIntent)
            }
        }
    }

    // ------------------------------------------------------------------------
    // Logic
    // ------------------------------------------------------------------------

    private suspend fun animKoleso() = suspendCoroutine<Unit> { continuation ->
        runGDX {
            koleso.addAction(Actions.sequence(
                Actions.fadeIn(0.5f),
                Actions.run { continuation.resume(Unit) }
            ))
        }
    }

    private suspend fun animTarget() = suspendCoroutine<Unit> { continuation ->
        runGDX {
            targetLabel.addAction(Actions.sequence(
                Actions.fadeIn(0.5f),
                Actions.forever(Actions.sequence(
                    Actions.moveBy(0f, 10f, 0.3f, Interpolation.sineOut),
                    Actions.moveBy(0f, -20f, 0.6f, Interpolation.sine),
                    Actions.moveBy(0f, 10f, 0.3f, Interpolation.sineIn),
                )))
            )
            target.addAction(Actions.sequence(
                Actions.fadeIn(0.5f),
                Actions.run { continuation.resume(Unit) }
            ))
        }
    }

    private suspend fun animTitle() = suspendCoroutine<Unit> { continuation ->
        runGDX {
            titleImage.addAction(Actions.sequence(
                Actions.fadeIn(0.5f),
                Actions.run { continuation.resume(Unit) }
            ))
        }
    }

    private suspend fun animResult() = suspendCoroutine<Unit> { continuation ->
        runGDX {
            resultText.addAction(Actions.fadeIn(0.5f))
            percentText.addAction(Actions.sequence(
                Actions.fadeIn(0.5f),
                Actions.run { continuation.resume(Unit) }
            ))
        }
    }

    private suspend fun animButtons() = suspendCoroutine<Unit> { continuation ->
        runGDX {
            restartButton.addAction(Actions.parallel(
                Actions.fadeIn(0.5f),
                Actions.moveTo(LR.restartEnd.x, LR.restartEnd.y, 0.5f)
            ))
            sendButton.addAction(Actions.sequence(
                Actions.parallel(
                    Actions.fadeIn(0.5f),
                    Actions.moveTo(LR.sendEnd.x, LR.sendEnd.y, 0.5f)
                ),
                Actions.run { continuation.resume(Unit) }
            ))
        }
    }

}