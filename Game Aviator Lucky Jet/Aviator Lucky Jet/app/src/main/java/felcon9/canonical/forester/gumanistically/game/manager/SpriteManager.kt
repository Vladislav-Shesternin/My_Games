package felcon9.canonical.forester.gumanistically.game.manager

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import felcon9.canonical.forester.gumanistically.game.utils.region

object SpriteManager {

    var loadableAtlasList   = mutableListOf<IAtlas>()
    var loadableTextureList = mutableListOf<ITexture>()



    fun loadAtlas(assetManager: AssetManager) {
        loadableAtlasList.onEach { assetManager.load(it.data.path, TextureAtlas::class.java) }
    }

    fun loadTexture(assetManager: AssetManager) {
        loadableTextureList.onEach {
            assetManager.load(it.data.path, Texture::class.java, TextureLoader.TextureParameter().apply {
                minFilter = Texture.TextureFilter.Linear
                magFilter = Texture.TextureFilter.Linear
                genMipMaps = true
            })
        }
    }

    fun initAtlas(assetManager: AssetManager) {
        loadableAtlasList.onEach { it.data.atlas = assetManager[it.data.path, TextureAtlas::class.java] }
    }

    fun initTexture(assetManager: AssetManager) {
        loadableTextureList.onEach { it.data.texture = assetManager[it.data.path, Texture::class.java] }
    }



    enum class EnumAtlas(override val data: TextureAtlasData): IAtlas {
        SPLASH( TextureAtlasData("atlas/splash.atlas") ),
        GAME(   TextureAtlasData("atlas/game.atlas")   ),
    }

    enum class EnumTexture(override val data: TextureData): ITexture {
        KOLESO(   TextureData("textures/koleso.png")   ),

        BACK_CLOUD_1(TextureData("textures/back_cloud/back_cloud-1.png")),
        BACK_CLOUD_2(TextureData("textures/back_cloud/back_cloud-2.png")),
        BACK_CLOUD_3(TextureData("textures/back_cloud/back_cloud-3.png")),

        FRONT_CLOUD_1(TextureData("textures/front_cloud/front_cloud-1.png")),
        FRONT_CLOUD_2(TextureData("textures/front_cloud/front_cloud-2.png")),
        FRONT_CLOUD_3(TextureData("textures/front_cloud/front_cloud-3.png")),
        FRONT_CLOUD_4(TextureData("textures/front_cloud/front_cloud-4.png")),
    }



    enum class SplashRegion(override val region: TextureRegion): IRegion {
        LOADER( EnumAtlas.SPLASH.data.atlas.findRegion("loader")        ),
        LOGO(   EnumAtlas.SPLASH.data.atlas.findRegion("logo")          ),
        AVIATOR(EnumAtlas.SPLASH.data.atlas.findRegion("splash_aviator")),

        KOLESO(  EnumTexture.KOLESO.data.texture.region  ),
    }

    enum class GameRegion(override val region: TextureRegion): IRegion {
        ARROW_DOWN(   EnumAtlas.GAME.data.atlas.findRegion("arrow_down")  ),
        ARROW_UP(     EnumAtlas.GAME.data.atlas.findRegion("arrow_up")    ),
        AVIATOR(      EnumAtlas.GAME.data.atlas.findRegion("aviator")     ),
        BTN_1(        EnumAtlas.GAME.data.atlas.findRegion("btn-1")       ),
        BTN_2(        EnumAtlas.GAME.data.atlas.findRegion("btn-2")       ),
        ENEMY(        EnumAtlas.GAME.data.atlas.findRegion("enemy")       ),
        PANEL(        EnumAtlas.GAME.data.atlas.findRegion("panel")       ),
        TARGET(       EnumAtlas.GAME.data.atlas.findRegion("target")      ),
        TITLE_MENU(   EnumAtlas.GAME.data.atlas.findRegion("title_menu")  ),
        TITLE_RESULT( EnumAtlas.GAME.data.atlas.findRegion("title_result")),
        TRY(          EnumAtlas.GAME.data.atlas.findRegion("try")         ),

        BACK_CLOUD_1(  EnumTexture.BACK_CLOUD_1.data.texture.region  ),
        BACK_CLOUD_2(  EnumTexture.BACK_CLOUD_2.data.texture.region  ),
        BACK_CLOUD_3(  EnumTexture.BACK_CLOUD_3.data.texture.region  ),

        FRONT_CLOUD_1(  EnumTexture.FRONT_CLOUD_1.data.texture.region  ),
        FRONT_CLOUD_2(  EnumTexture.FRONT_CLOUD_2.data.texture.region  ),
        FRONT_CLOUD_3(  EnumTexture.FRONT_CLOUD_3.data.texture.region  ),
        FRONT_CLOUD_4(  EnumTexture.FRONT_CLOUD_4.data.texture.region  ),
    }

    enum class ListRegion(override val regionList: List<TextureRegion>): IRegionList {
//        MAN( List(21) { EnumAtlas.MAN.data.atlas.findRegion("man (${it.inc()})") }),
//        FIRE(List(10) { EnumAtlas.FIRE.data.atlas.findRegion("fire (${it.inc()})") }),
    }



    interface IAtlas {
        val data: TextureAtlasData
    }

    interface ITexture {
        val data: TextureData
    }

    interface IRegion {
        val region: TextureRegion
    }

    interface IRegionList {
        val regionList: List<TextureRegion>
    }

    data class TextureAtlasData(
        val path: String,
    ) {
        lateinit var atlas: TextureAtlas
    }

    data class TextureData(
        val path: String,
    ) {
        lateinit var texture: Texture
    }

}