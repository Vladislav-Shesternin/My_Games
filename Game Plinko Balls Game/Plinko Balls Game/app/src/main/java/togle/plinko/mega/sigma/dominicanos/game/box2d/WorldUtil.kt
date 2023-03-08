package togle.plinko.mega.sigma.dominicanos.game.box2d

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.Joint
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Disposable
import togle.plinko.mega.sigma.dominicanos.utils.cancelCoroutinesAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class WorldUtil: Disposable {

    companion object {
        const val GRAVITY_X = 0f
        const val GRAVITY_Y = -9.8f
        private const val TIME_STEP: Float = 1f / 60f
    }

    private var accumulatorTime = 0f
    private val coroutine       = CoroutineScope(Dispatchers.Default)

    val world         by lazy { World(Vector2(GRAVITY_X, GRAVITY_Y), true) }
    val debugRenderer by lazy { Box2DDebugRenderer(true, true, true, true, false, true) }
    val bodyEditor    by lazy { BodyEditorLoader(Gdx.files.internal("physics/PhysicsData")) }

    val destroyBodyList  = mutableListOf<AbstractBody>()
    //val destroyJointList = mutableListOf<AbstractJoint>()



    init {
        world.setContactFilter(WorldContactFilter)
        world.setContactListener(WorldContactListener)
    }

    override fun dispose() {
        world.bodies().onEach { (it.userData as AbstractBody).destroy() }
       // world.joints().onEach { (it.userData as AbstractJoint).destroy() }

        cancelCoroutinesAll(coroutine)
        world.dispose()
    }



    fun update(deltaTime: Float) {
        accumulatorTime += deltaTime

        while (accumulatorTime >= TIME_STEP) {
            world.step(TIME_STEP, 6, 2)
            accumulatorTime -= TIME_STEP
        }

        world.bodies().onEach { (it.userData as AbstractBody).render(deltaTime) }
        destroyBodyList.onEach { it.destroy() }.clear()
       // destroyJointList.onEach { it.destroy() }.clear()
    }

    fun debug(matrix4: Matrix4) {
        //debugRenderer.render(world, matrix4)
    }

}