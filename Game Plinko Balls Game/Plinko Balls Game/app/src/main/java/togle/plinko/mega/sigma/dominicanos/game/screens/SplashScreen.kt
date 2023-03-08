package togle.plinko.mega.sigma.dominicanos.game.screens

import android.annotation.SuppressLint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import togle.plinko.mega.sigma.dominicanos.MainActivity
import togle.plinko.mega.sigma.dominicanos.game.game
import togle.plinko.mega.sigma.dominicanos.game.manager.FontTTFManager
import togle.plinko.mega.sigma.dominicanos.game.manager.NavigationManager
import togle.plinko.mega.sigma.dominicanos.game.manager.SpriteManager
import togle.plinko.mega.sigma.dominicanos.game.utils.advanced.AdvancedGroup
import togle.plinko.mega.sigma.dominicanos.game.utils.advanced.AdvancedScreen
import togle.plinko.mega.sigma.dominicanos.utils.log

@SuppressLint("CustomSplashScreen")
class SplashScreen : AdvancedScreen() {

    private val mutex           = Mutex()
    private var progress        = 0
    private var isFinishLoading = false

   // private val progressLabel by lazy { Label("", ALabelStyle.style(ALabelStyle.Roboto._40)) }


    override fun show() {
        loadSplashAssets()
        super.show()
        loadAssets()
    }

    override fun render(delta: Float) {
        super.render(delta)
        loadingAssets()
    }


    override fun AdvancedGroup.addActorsOnGroup() {
        addProgress()
    }


    // ------------------------------------------------------------------------
    // Add Actors
    // ------------------------------------------------------------------------

    private fun AdvancedGroup.addProgress() {
//        addActor(progressLabel)
//        progressLabel.apply {
//            setBounds(LS.progress)
//            setAlignment(Align.center)
//        }
    }

    // ------------------------------------------------------------------------
    // Logic
    // ------------------------------------------------------------------------

    private fun loadSplashAssets() {
//        with(SpriteManager) {
//            loadableAtlasList = mutableListOf(SpriteManager.EnumAtlas.GAME)
//            loadAtlas(game.assetManager)
//        }
//
//        game.assetManager.finishLoading()
//
//        SpriteManager.initAtlas(game.assetManager)
//        FontTTFManager.init(game.assetManager)
    }

    private fun loadAssets() {
        with(SpriteManager) {
            loadableAtlasList = mutableListOf(SpriteManager.EnumAtlas.GAME)
            loadAtlas(game.assetManager)
        }
        with(FontTTFManager) {
            loadableListFont = FontTTFManager.ExtraBoldFont.values.toMutableList()
            load(game.assetManager)
        }
        game.assetManager.finishLoading()

        SpriteManager.initAtlas(game.assetManager)
        FontTTFManager.init(game.assetManager)
    }

    private fun loadingAssets() {
        if (isFinishLoading.not() && game.assetManager.update(17)) {
            initAssets()
            coroutine.launch { mutex.withLock { while (progress < (game.assetManager.progress * 100)) {
                log("progress - $progress")
                progress += 1
               // runGDX { progressLabel.setText("$progress%") }
                if (progress == 100) {
                    isFinishLoading = true
                    NavigationManager.navigate(MenuScreen())
                }
                delay((10..15).shuffled().first().toLong())
            } } }
        }
    }

    private fun initAssets() {
        SpriteManager.initAtlas(game.assetManager)
        //SpriteManager.initTexture(game.assetManager)
        FontTTFManager.init(game.assetManager)
    }



}