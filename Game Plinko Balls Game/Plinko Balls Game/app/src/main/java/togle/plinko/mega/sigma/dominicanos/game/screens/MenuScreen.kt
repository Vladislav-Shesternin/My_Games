package togle.plinko.mega.sigma.dominicanos.game.screens

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import togle.plinko.mega.sigma.dominicanos.MainActivity
import togle.plinko.mega.sigma.dominicanos.bebView.BebViewController
import togle.plinko.mega.sigma.dominicanos.game.actors.button.AButton
import togle.plinko.mega.sigma.dominicanos.game.actors.button.AButtonStyle
import togle.plinko.mega.sigma.dominicanos.game.actors.button.AButtonText
import togle.plinko.mega.sigma.dominicanos.game.actors.label.ALabelStyle
import togle.plinko.mega.sigma.dominicanos.game.box2d.BodyId
import togle.plinko.mega.sigma.dominicanos.game.box2d.WorldUtil
import togle.plinko.mega.sigma.dominicanos.game.box2d.bodies.BBalk
import togle.plinko.mega.sigma.dominicanos.game.box2d.bodies.BBall
import togle.plinko.mega.sigma.dominicanos.game.box2d.bodies.BKopilka
import togle.plinko.mega.sigma.dominicanos.game.manager.GameDataStoreManager
import togle.plinko.mega.sigma.dominicanos.game.manager.NavigationManager
import togle.plinko.mega.sigma.dominicanos.game.manager.SpriteManager
import togle.plinko.mega.sigma.dominicanos.game.utils.Layout
import togle.plinko.mega.sigma.dominicanos.game.utils.actor.setBounds
import togle.plinko.mega.sigma.dominicanos.game.utils.advanced.AdvancedBox2dScreen
import togle.plinko.mega.sigma.dominicanos.game.utils.advanced.AdvancedGroup
import togle.plinko.mega.sigma.dominicanos.game.utils.advanced.AdvancedScreen
import togle.plinko.mega.sigma.dominicanos.game.utils.advanced.AdvancedStage
import togle.plinko.mega.sigma.dominicanos.game.utils.runGDX
import togle.plinko.mega.sigma.dominicanos.utils.Once
import togle.plinko.mega.sigma.dominicanos.utils.log
import togle.plinko.mega.sigma.dominicanos.game.utils.Layout.Game as LG
import togle.plinko.mega.sigma.dominicanos.game.utils.Layout.Menu as LM

lateinit var menuScreen: MenuScreen

class MenuScreen: AdvancedScreen() {
    private var isPrivacyPolicy = false

    private val gameButton    = AButtonText("GAME", AButtonStyle.btn, ALabelStyle.style(ALabelStyle.Inter._48, Color.BLACK))
    private val privacyButton = AButtonText("Privacy Policy", AButtonStyle.btn, ALabelStyle.style(ALabelStyle.Inter._48, Color.BLACK))
    private val exitButton    = AButtonText("Exit", AButtonStyle.btn, ALabelStyle.style(ALabelStyle.Inter._48, Color.BLACK))

    override fun AdvancedGroup.addActorsOnGroup() {
        addButtons()
        menuScreen = this@MenuScreen
    }

    // ------------------------------------------------------------------------
    // Add Actors
    // ------------------------------------------------------------------------

    private fun AdvancedGroup.addButtons() {
        addActors(gameButton, privacyButton, exitButton)
        gameButton.apply {
            setBounds(LM.game)
            setOnClickListener { NavigationManager.navigate(GameScreen(), MenuScreen()) }
        }
        privacyButton.apply {
            setBounds(LM.privacy)
            setOnClickListener {
                BebViewController.bebUrlFlow.value = MainActivity.kakayatoDom
                MainActivity.isBebViewVisibleFlow.value = true
                isPrivacyPolicy = true
            }
        }
        exitButton.apply {
            setBounds(LM.exit)
            setOnClickListener { NavigationManager.exit() }
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK) {
            if (isPrivacyPolicy) {
                isPrivacyPolicy = false
                MainActivity.isBebViewVisibleFlow.value = false
            } else super.keyDown(keycode)
        }
        return false
    }


}