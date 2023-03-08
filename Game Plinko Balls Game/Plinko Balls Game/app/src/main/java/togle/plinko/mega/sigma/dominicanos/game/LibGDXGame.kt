package togle.plinko.mega.sigma.dominicanos.game

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.ScreenUtils
import togle.plinko.mega.sigma.dominicanos.MainActivity
import togle.plinko.mega.sigma.dominicanos.game.manager.NavigationManager
import togle.plinko.mega.sigma.dominicanos.game.screens.SplashScreen
import togle.plinko.mega.sigma.dominicanos.game.utils.advanced.AdvancedGame

lateinit var game: LibGDXGame private set

class LibGDXGame(val activity: MainActivity) : AdvancedGame() {

    lateinit var assetManager: AssetManager private set



    override fun create() {
        game         = this
        assetManager = AssetManager()

        NavigationManager.navigate(SplashScreen())
    }

    override fun render() {
        ScreenUtils.clear(Color.BLACK)
        super.render()
    }

    override fun dispose() {
        super.dispose()
        assetManager.dispose()
    }

}