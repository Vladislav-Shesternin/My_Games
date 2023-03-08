package togle.plinko.mega.sigma.dominicanos.game.manager

import com.badlogic.gdx.Gdx
import togle.plinko.mega.sigma.dominicanos.game.game
import togle.plinko.mega.sigma.dominicanos.game.utils.advanced.AdvancedScreen
import togle.plinko.mega.sigma.dominicanos.game.utils.runGDX

object NavigationManager {

    private val backStack = mutableListOf<AdvancedScreen>()
    var key: Int? = null
        private set

    fun navigate(to: AdvancedScreen, from: AdvancedScreen? = null, key: Int? = null) {
        runGDX {
            NavigationManager.key = key

            game.screen = to
            from?.let { f ->
                backStack.filter { it.name == f.name }.onEach { backStack.remove(it) }
                backStack.add(f)
            }
            backStack.filter { it.name == to.name }.onEach { backStack.remove(it) }
        }
    }

    fun back(key: Int? = null) {
        runGDX {
            NavigationManager.key = key

            if (backStack.isEmpty()) exit()
            else game.screen = backStack.removeLast()
        }
    }

    fun exit() {
        runGDX {
            backStack.clear()
            game.dispose()
            Gdx.app.exit()
        }
    }

}