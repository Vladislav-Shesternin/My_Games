package felcon9.canonical.forester.gumanistically.game.screens

import android.annotation.SuppressLint
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Align
import felcon9.canonical.forester.gumanistically.MainActivity
import felcon9.canonical.forester.gumanistically.game.actors.Koleso
import felcon9.canonical.forester.gumanistically.game.game
import felcon9.canonical.forester.gumanistically.game.manager.FontTTFManager
import felcon9.canonical.forester.gumanistically.game.manager.NavigationManager
import felcon9.canonical.forester.gumanistically.game.manager.SpriteManager
import felcon9.canonical.forester.gumanistically.game.utils.actor.setBounds
import felcon9.canonical.forester.gumanistically.game.utils.advanced.AdvancedGroup
import felcon9.canonical.forester.gumanistically.game.utils.advanced.AdvancedScreen
import felcon9.canonical.forester.gumanistically.game.utils.advanced.AdvancedStage
import felcon9.canonical.forester.gumanistically.util.log
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import felcon9.canonical.forester.gumanistically.game.utils.Layout.Splash as LS

@SuppressLint("CustomSplashScreen")
class SplashScreen : AdvancedScreen() {

    private val mutex = Mutex()
    private var progress = 0
    private var isFinishLoading = false

    private val logo    by lazy { Image(SpriteManager.SplashRegion.LOGO.region) }
    private val loader  by lazy { Image(SpriteManager.SplashRegion.LOADER.region) }
    private val aviator by lazy { Image(SpriteManager.SplashRegion.AVIATOR.region) }
    private val koleso  by lazy { Koleso() }



    override fun show() {
        MainActivity.lottie.hideLoader()
        loadSplashAssets()
        super.show()
        loadAssets()
    }

    override fun render(delta: Float) {
        super.render(delta)
        loadingAssets()
    }


    override fun AdvancedGroup.addActorsOnGroup() {
        addKoleso()
        addLoader()
        addAviator()
    }

    override fun AdvancedStage.addActorsOnStageUI() {
        addLogo()
    }

    // ------------------------------------------------------------------------
    // Add Actors
    // ------------------------------------------------------------------------

    private fun AdvancedGroup.addKoleso() {
        addActor(koleso)
        koleso.apply {
            setBounds(0f, 0f, WIDTH, HEIGHT)
            startAnim()
        }
    }

    private fun AdvancedStage.addLogo() {
        addActor(logo)
        logo.setBounds(LS.logo)
    }

    private fun AdvancedGroup.addLoader() {
        addActor(loader)
        loader.apply {
            setBounds(LS.loader)
            setOrigin(Align.center)
            addAction(Actions.forever(Actions.rotateBy(-360f, 1f)))
        }
    }

    private fun AdvancedGroup.addAviator() {
        addActor(aviator)
        aviator.setBounds(LS.aviatorStart)
    }

    // ------------------------------------------------------------------------
    // Logic
    // ------------------------------------------------------------------------

    private fun loadSplashAssets() {
        with(SpriteManager) {
            loadableAtlasList = mutableListOf(SpriteManager.EnumAtlas.SPLASH)
            loadAtlas(game.assetManager)
            loadableTextureList = mutableListOf(SpriteManager.EnumTexture.KOLESO)
            loadTexture(game.assetManager)
        }

        game.assetManager.finishLoading()

        SpriteManager.initAtlas(game.assetManager)
        SpriteManager.initTexture(game.assetManager)
    }

    private fun loadAssets() {
        with(SpriteManager) {
            loadableAtlasList = SpriteManager.EnumAtlas.values().toMutableList()
            loadAtlas(game.assetManager)
            loadableTextureList = SpriteManager.EnumTexture.values().toMutableList()
            loadTexture(game.assetManager)
        }
        with(FontTTFManager) {
            loadableListFont = (FontTTFManager.LightFont.values + FontTTFManager.RegularFont.values + FontTTFManager.SemiBoldFont.values).toMutableList()
            load(game.assetManager)
        }
    }

    private fun loadingAssets() {
        if (isFinishLoading.not() && game.assetManager.update(17)) {
            initAssets()
            coroutine.launch { mutex.withLock { while (progress < (game.assetManager.progress * 100)) {
                log("progress - $progress")
                progress += 1
                if (progress == 100) {
                    isFinishLoading = true
                    navToMenu()
                }
                delay((5..15).shuffled().first().toLong())
            } } }
        }
    }

    private fun initAssets() {
        SpriteManager.initAtlas(game.assetManager)
        SpriteManager.initTexture(game.assetManager)
        FontTTFManager.init(game.assetManager)
    }


    private fun navToMenu() {
        aviator.addAction(Actions.moveTo(LS.aviatorEnd.x, LS.aviatorEnd.y, 0.7f))
        mainGroup.addAction(Actions.sequence(
            Actions.fadeOut(0.7f),
            Actions.run { NavigationManager.navigate(GameScreen()) }
        ))
    }

}