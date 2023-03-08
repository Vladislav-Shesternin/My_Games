package togle.plinko.mega.sigma.dominicanos.game.box2d

import com.badlogic.gdx.physics.box2d.ContactFilter
import com.badlogic.gdx.physics.box2d.Fixture

object WorldContactFilter: ContactFilter {

    override fun shouldCollide(fixtureA: Fixture, fixtureB: Fixture): Boolean {
        return (fixtureA.body.userData as AbstractBody).collisionList.contains((fixtureB.body.userData as AbstractBody).id) &&
                (fixtureB.body.userData as AbstractBody).collisionList.contains((fixtureA.body.userData as AbstractBody).id)
    }
}