package togle.plinko.mega.sigma.dominicanos.game.screens

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import togle.plinko.mega.sigma.dominicanos.game.actors.button.AButton
import togle.plinko.mega.sigma.dominicanos.game.actors.button.AButtonStyle
import togle.plinko.mega.sigma.dominicanos.game.actors.label.ALabelStyle
import togle.plinko.mega.sigma.dominicanos.game.box2d.BodyId
import togle.plinko.mega.sigma.dominicanos.game.box2d.WorldUtil
import togle.plinko.mega.sigma.dominicanos.game.box2d.bodies.BBalk
import togle.plinko.mega.sigma.dominicanos.game.box2d.bodies.BBall
import togle.plinko.mega.sigma.dominicanos.game.box2d.bodies.BKopilka
import togle.plinko.mega.sigma.dominicanos.game.manager.GameDataStoreManager
import togle.plinko.mega.sigma.dominicanos.game.manager.SpriteManager
import togle.plinko.mega.sigma.dominicanos.game.utils.actor.setBounds
import togle.plinko.mega.sigma.dominicanos.game.utils.advanced.AdvancedBox2dScreen
import togle.plinko.mega.sigma.dominicanos.game.utils.advanced.AdvancedStage
import togle.plinko.mega.sigma.dominicanos.game.utils.runGDX
import togle.plinko.mega.sigma.dominicanos.utils.Once
import togle.plinko.mega.sigma.dominicanos.utils.log
import togle.plinko.mega.sigma.dominicanos.game.utils.Layout.Game as LG

class GameScreen: AdvancedBox2dScreen(WorldUtil()) {

    private val minStavka  = 1000
    private val stepStavka = 500

    private val balanceFlow = MutableStateFlow(-1L)
    private val stavkaFlow  = MutableStateFlow(minStavka)

    // Actors
    private val aShadowImage  = Image(SpriteManager.GameRegion.SHADER.region)
    private val aPlayButton   = AButton(AButtonStyle.play)
    private val aPlusButton   = AButton(AButtonStyle.plus)
    private val aMinusButton  = AButton(AButtonStyle.minus)
    private val aStavkaLabel  = Label("0", ALabelStyle.style(ALabelStyle.Inter._40))
    private val aBalanceLabel = Label("0", ALabelStyle.style(ALabelStyle.Inter._20))

    private val buttons = listOf<AButton>(aPlayButton, aPlusButton, aMinusButton)

    // Body
    private val bBalkList    = listOf(
        List(7) { BBalk(this) },
        List(6) { BBalk(this) },
        List(5) { BBalk(this) },
        List(4) { BBalk(this) },
        List(3) { BBalk(this) },
        List(2) { BBalk(this) },
    )
    private val bBall        = BBall(this)
    private val bKopilkaList = listOf(
        BKopilka(this, BKopilka.Type._5),
        BKopilka(this, BKopilka.Type._3),
        BKopilka(this, BKopilka.Type._1),
        BKopilka(this, BKopilka.Type._1),
        BKopilka(this, BKopilka.Type._3),
        BKopilka(this, BKopilka.Type._5),
    )

    private val startPos by lazy { sizeConverterUIToBox.getSize(LG.aShadow.position()).add(bBall.center) }



    override fun show() {
        super.show()
        coroutine.launch(Dispatchers.IO) { balanceFlow.value = GameDataStoreManager.Balance.get() ?: 10_000L }
    }

    override fun AdvancedStage.addActorsOnStageUI() {
        addA_ShadowImage()

        createB_BalkList()
        createB_KopilkaList()
        createB_Ball()

        addA_PlayButton()
        addA_PlusMinusButton()
        addA_StavkaLabel()
        addA_BalanceLabel()
    }

    // ------------------------------------------------------------------------
    // Add Actors
    // ------------------------------------------------------------------------

    private fun AdvancedStage.addA_ShadowImage() {
        addActor(aShadowImage)
        aShadowImage.apply {
            setBounds(LG.aShadow)
        }
    }

    private fun AdvancedStage.addA_PlayButton() {
        addActor(aPlayButton)
        aPlayButton.apply {
            setBounds(LG.aPlay)
            setOnClickListener { playHandler() }
        }
    }

    private fun AdvancedStage.addA_PlusMinusButton() {
        addActors(aPlusButton, aMinusButton)
        aPlusButton.apply {
            setBounds(LG.aPlus)
            setOnClickListener { plusHandler() }
        }
        aMinusButton.apply {
            setBounds(LG.aMinus)
            setOnClickListener { minusHandler() }
        }
    }

    private fun AdvancedStage.addA_StavkaLabel() {
        addActor(aStavkaLabel)
        aStavkaLabel.apply {
            setBounds(LG.aStavka)
            setAlignment(Align.center)
        }
        collectStavka()
    }

    private fun AdvancedStage.addA_BalanceLabel() {
        addActor(aBalanceLabel)
        aBalanceLabel.apply {
            setBounds(LG.aBalance)
            setAlignment(Align.center)
        }
        collectBalance()
    }

    // ------------------------------------------------------------------------
    // Create Body
    // ------------------------------------------------------------------------
    private fun createB_BalkList() {
        bBalkList.onEachIndexed { index, bList ->
            var nx = LG.bBalkPosList[index].x
            bList.onEach { balk ->
                balk.create(nx, LG.bBalkPosList[index].y, LG.bBalkSize.width, LG.bBalkSize.height)
                nx += LG.bBalkSize.width + LG.bBalkSpace
            }
        }
    }

    private fun createB_KopilkaList() {
        var nx = LG.bKopilka.x
        bKopilkaList.onEach { kopilka ->
            with(LG.bKopilka) {
                kopilka.create(nx, y, w, h)
                nx += w + hs
            }
        }
    }

    var flag = true
    private fun createB_Ball() {
        bBall.create(LG.aShadow)
        val minY     = sizeConverterUIToBox.getSizeY(392f)
        bBall.apply {
            actor.addAction(Actions.alpha(0f))
            body!!.isAwake = false

            beginContactBlock = { enemyBody ->
                if(flag) {
                    flag = false
                    body!!.applyLinearImpulse(body!!.worldCenter, Vector2((-2..2).shuffled().first().toFloat(), 0f), true)
                }

                when (enemyBody.id) {
                    BodyId._1 -> {
                        moveToStart()
                        balanceFlow.value += stavkaFlow.value * 1
                    }
                    BodyId._3 -> {
                        moveToStart()
                        balanceFlow.value += stavkaFlow.value * 3
                    }
                    BodyId._5 -> {
                        moveToStart()
                        balanceFlow.value += stavkaFlow.value * 5
                    }
                    else -> {}
                }
            }

            renderBlock = { delta -> if (body!!.position.y <= minY) moveToStart() }
        }
    }

    // ------------------------------------------------------------------------
    // Logic
    // ------------------------------------------------------------------------

    private fun collectBalance() {
        coroutine.launch(Dispatchers.IO) {
            balanceFlow.collect { balance ->
                if (balance != -1L) {
                    GameDataStoreManager.Balance.update { balance }
                    runGDX { aBalanceLabel.setText(balance.toString()) }
                }
            }
        }
    }

    private fun collectStavka() {
        coroutine.launch {
            stavkaFlow.collect { stavka ->
                runGDX { aStavkaLabel.setText(stavka.toString()) }
            }
        }
    }

    private fun plusHandler() {
        if (balanceFlow.value - stavkaFlow.value >= 0) {
            stavkaFlow.value += stepStavka
        }
    }

    private fun minusHandler() {
        if (stavkaFlow.value - stepStavka >= minStavka) {
            stavkaFlow.value -= stepStavka
        }
    }

    private fun playHandler() {
        flag = true
        buttons.onEach { it.disable() }
        balanceFlow.value -= stavkaFlow.value
        bBall.apply {
            actor.addAction(Actions.fadeIn(0.5f))
            body!!.isAwake = true
        }
    }

    private fun moveToStart() {
        runGDX {
            bBall.apply {
                actor.addAction(Actions.alpha(0f))
                body!!.apply {
                    linearVelocity = Vector2(0f, 0f)
                    isAwake = false
                    setTransform(startPos, 0f)
                }
            }
            buttons.onEach { it.enable() }
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        log("game")
        return super.keyDown(keycode)
    }

}