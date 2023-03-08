package felcon9.canonical.forester.gumanistically.game

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.ScreenUtils
import felcon9.canonical.forester.gumanistically.MainActivity
import felcon9.canonical.forester.gumanistically.game.manager.NavigationManager
import felcon9.canonical.forester.gumanistically.game.screens.SplashScreen
import felcon9.canonical.forester.gumanistically.game.utils.advanced.AdvancedGame

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