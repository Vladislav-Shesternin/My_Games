package togle.plinko.mega.sigma.dominicanos.game.box2d.bodies

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import togle.plinko.mega.sigma.dominicanos.game.actors.image.AImage
import togle.plinko.mega.sigma.dominicanos.game.box2d.AbstractBody
import togle.plinko.mega.sigma.dominicanos.game.box2d.BodyId
import togle.plinko.mega.sigma.dominicanos.game.box2d.BodyId.*
import togle.plinko.mega.sigma.dominicanos.game.manager.SpriteManager
import togle.plinko.mega.sigma.dominicanos.game.utils.advanced.AdvancedBox2dScreen

class BKopilka(
    override val screenBox2d: AdvancedBox2dScreen,
    val type: Type,
): AbstractBody() {
    override var id            = type.id
    override val name          = "Kopilka"
    override val bodyDef       = BodyDef().apply {
        type = BodyDef.BodyType.StaticBody
    }
    override val fixtureDef    = FixtureDef()
    override val collisionList = mutableListOf<BodyId>(BALL)
    override val actor         = AImage(type.region)

    enum class Type(
        val id: BodyId,
        val region: TextureRegion,
    ) {
        _1(BodyId._1, SpriteManager.GameRegion._1.region),
        _3(BodyId._3, SpriteManager.GameRegion._3.region),
        _5(BodyId._5, SpriteManager.GameRegion._5.region),
    }

}