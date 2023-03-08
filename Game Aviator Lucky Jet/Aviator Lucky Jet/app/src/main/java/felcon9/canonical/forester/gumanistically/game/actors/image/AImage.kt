package felcon9.canonical.forester.gumanistically.game.actors.image

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import felcon9.canonical.forester.gumanistically.game.utils.advanced.AdvancedGroup

class AImage: AdvancedGroup {

    private val image = Image()

    var drawable: Drawable = TextureRegionDrawable()
        get() = image.drawable
        set(value) {
            image.drawable = value
            field = value
        }

    constructor()

    constructor(region: TextureRegion) {
        image.drawable = TextureRegionDrawable(region)
    }
    constructor(texture: Texture) {
        image.drawable = TextureRegionDrawable(texture)
    }
    constructor(drawable: Drawable) {
        image.drawable = drawable
    }

    override fun sizeChanged() {
        if (width > 0 && height > 0) addAndFillActor(image)
    }

}