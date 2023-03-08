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

class BBalk(
    override val screenBox2d: AdvancedBox2dScreen,
): AbstractBody() {
    override var id            = BALK
    override val name          = "Circle"
    override val bodyDef       = BodyDef().apply {
        type = BodyDef.BodyType.StaticBody
    }
    override val fixtureDef    = FixtureDef().apply {
        restitution = 0.5f
        friction    = 0.4f
    }
    override val collisionList = mutableListOf<BodyId>(BALL)
    override val actor         = AImage(SpriteManager.GameRegion.BALK_DEF.region)


    override fun beginContact(contactBody: AbstractBody) {
        actor.drawable = TextureRegionDrawable(SpriteManager.GameRegion.BALK_PRESS.region)
        super.beginContact(contactBody)
    }

    override fun endContact(contactBody: AbstractBody) {
        actor.drawable = TextureRegionDrawable(SpriteManager.GameRegion.BALK_DEF.region)
        super.beginContact(contactBody)
    }

}