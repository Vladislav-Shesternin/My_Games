package togle.plinko.mega.sigma.dominicanos.game.box2d.bodies

import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import togle.plinko.mega.sigma.dominicanos.game.actors.image.AImage
import togle.plinko.mega.sigma.dominicanos.game.box2d.AbstractBody
import togle.plinko.mega.sigma.dominicanos.game.box2d.BodyId
import togle.plinko.mega.sigma.dominicanos.game.box2d.BodyId.*
import togle.plinko.mega.sigma.dominicanos.game.manager.SpriteManager
import togle.plinko.mega.sigma.dominicanos.game.utils.advanced.AdvancedBox2dScreen

class BBall(
    override val screenBox2d: AdvancedBox2dScreen,
): AbstractBody() {
    override var id            = BALL
    override val name          = "Circle"
    override val bodyDef       = BodyDef().apply {
        type = BodyDef.BodyType.DynamicBody
    }
    override val fixtureDef    = FixtureDef().apply {
        density     = 10f
        restitution = 0.7f
        friction    = 0.7f
    }
    override val collisionList = mutableListOf<BodyId>(_1, _3, _5, BALK)
    override val actor         = AImage(SpriteManager.GameRegion.BALL.region)

}