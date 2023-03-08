package togle.plinko.mega.sigma.dominicanos.game.utils

import com.badlogic.gdx.math.Vector2

fun Vector2.addNew(vector: Vector2) = vector.add(this)
fun Vector2.addNew(x: Float, y: Float) = Vector2(this.x + x, this.y + y)