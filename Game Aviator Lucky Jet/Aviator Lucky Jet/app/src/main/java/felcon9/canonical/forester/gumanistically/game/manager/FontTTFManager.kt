package felcon9.canonical.forester.gumanistically.game.manager

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader

object FontTTFManager {

    private const val pathInter_Light    = "TTF/Inter-Light.ttf"
    private const val pathInter_Regular  = "TTF/Inter-Regular.ttf"
    private const val pathInter_SemiBold = "TTF/Inter-SemiBold.ttf"

    private val resolverInternal = InternalFileHandleResolver()

    var loadableListFont = mutableListOf<FontTTFData>()

//    val fontText: IFont get() = when(Language.locale.language) {
//        "ru", "uk" -> NotoSansFont
//        else -> MerriweatherSansFont
//    }



    private fun AssetManager.setLoaderTTF() {
        setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(resolverInternal))
        setLoader(BitmapFont::class.java, ".ttf", FreetypeFontLoader(resolverInternal))
    }

    private fun getLoaderParameter(
        path: String,
        parameters: FreeTypeFontGenerator.FreeTypeFontParameter.() -> Unit = { }
    ) = FreetypeFontLoader.FreeTypeFontLoaderParameter().apply {
        fontFileName = path
        fontParameters.apply {
            characters  = FreeTypeFontGenerator.DEFAULT_CHARS + ("АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ" + "абвгдеёжзийклмнопрстуфхцчшщъыьэюя")
            minFilter   = TextureFilter.Linear
            magFilter   = TextureFilter.Linear
            incremental = true
            parameters()
        }
    }



    fun load(assetManager: AssetManager) {
        with(assetManager) {
            setLoaderTTF()
            loadableListFont.onEach { load(it.name  + ".ttf", BitmapFont::class.java, it.parameters) }
        }
    }

    fun init(assetManager: AssetManager) {
        loadableListFont.onEach { it.font = assetManager[it.name + ".ttf", BitmapFont::class.java] }
    }


    object LightFont: IFont {
        val font_57  = FontTTFData("Regular_57", getLoaderParameter(pathInter_Light) { size = 57 })
        val font_123 = FontTTFData("Regular_123", getLoaderParameter(pathInter_Light) { size = 123 })

        override val values: List<FontTTFData>
            get() = super.values + listOf(font_57, font_123)
    }

    object RegularFont: IFont {
        val font_27 = FontTTFData("Bold_27", getLoaderParameter(pathInter_Regular) { size = 27 })

        override val values: List<FontTTFData>
            get() = super.values + listOf(font_27)
    }

    object SemiBoldFont: IFont {
        val font_30 = FontTTFData("Bold_30", getLoaderParameter(pathInter_SemiBold) { size = 30 })

        override val values: List<FontTTFData>
            get() = super.values + listOf(font_30)
    }



    interface IFont {
        val values get() = listOf<FontTTFData>()
    }


    data class FontTTFData(
        val name: String,
        val parameters: FreetypeFontLoader.FreeTypeFontLoaderParameter,
    ) {
        lateinit var font: BitmapFont
    }
}