package net.silkmc.silk.core.math.matrix

import org.joml.Matrix2d
import org.joml.Matrix2f
import org.joml.Matrix3d
import org.joml.Matrix3f
import org.joml.Matrix3x2d
import org.joml.Matrix3x2f
import org.joml.Matrix4d
import org.joml.Matrix4f
import org.joml.Matrix4x3d
import org.joml.Matrix4x3f
import org.joml.Vector2d
import org.joml.Vector2f
import org.joml.Vector3d
import org.joml.Vector3f
import org.joml.Vector4d
import org.joml.Vector4f


// Matrix2f operations

operator fun Matrix2f.unaryMinus(): Matrix2f = Matrix2f(this).invert()

operator fun Matrix2f.not(): Matrix2f = Matrix2f(this).invert()

operator fun Matrix2f.plus(matrix: Matrix2f): Matrix2f = Matrix2f(this).add(matrix)

operator fun Matrix2f.minus(matrix: Matrix2f): Matrix2f = Matrix2f(this).sub(matrix)

operator fun Matrix2f.times(n: Int): Matrix2f = Matrix2f(this).scale(n.toFloat())
operator fun Matrix2f.times(n: Long): Matrix2f = Matrix2f(this).scale(n.toFloat())
operator fun Matrix2f.times(n: Float): Matrix2f = Matrix2f(this).scale(n)
operator fun Matrix2f.times(n: Double): Matrix2f = Matrix2f(this).scale(n.toFloat())
operator fun Matrix2f.times(n: Number): Matrix2f = Matrix2f(this).scale(n.toFloat())

operator fun Int.times(matrix: Matrix2f): Matrix2f = Matrix2f(matrix).scale(this.toFloat())
operator fun Long.times(matrix: Matrix2f): Matrix2f = Matrix2f(matrix).scale(this.toFloat())
operator fun Float.times(matrix: Matrix2f): Matrix2f = Matrix2f(matrix).scale(this)
operator fun Double.times(matrix: Matrix2f): Matrix2f = Matrix2f(matrix).scale(this.toFloat())
operator fun Number.times(matrix: Matrix2f): Matrix2f = Matrix2f(matrix).scale(this.toFloat())

operator fun Matrix2f.times(vec: Vector2f): Vector2f = Matrix2f(this).transform(vec)

operator fun Vector2f.times(matrix: Matrix2f): Vector2f = Matrix2f(matrix).transpose().transform(this)

operator fun Matrix2f.times(matrix: Matrix2f): Matrix2f = Matrix2f(this).mul(matrix)


// Matrix2d operations

operator fun Matrix2d.unaryMinus(): Matrix2d = Matrix2d(this).invert()

operator fun Matrix2d.not(): Matrix2d = Matrix2d(this).invert()

operator fun Matrix2d.plus(matrix: Matrix2d): Matrix2d = Matrix2d(this).add(matrix)

operator fun Matrix2d.minus(matrix: Matrix2d): Matrix2d = Matrix2d(this).sub(matrix)

operator fun Matrix2d.times(n: Int): Matrix2d = Matrix2d(this).scale(n.toDouble())
operator fun Matrix2d.times(n: Long): Matrix2d = Matrix2d(this).scale(n.toDouble())
operator fun Matrix2d.times(n: Float): Matrix2d = Matrix2d(this).scale(n.toDouble())
operator fun Matrix2d.times(n: Double): Matrix2d = Matrix2d(this).scale(n)
operator fun Matrix2d.times(n: Number): Matrix2d = Matrix2d(this).scale(n.toDouble())

operator fun Int.times(matrix: Matrix2d): Matrix2d = Matrix2d(matrix).scale(this.toDouble())
operator fun Long.times(matrix: Matrix2d): Matrix2d = Matrix2d(matrix).scale(this.toDouble())
operator fun Float.times(matrix: Matrix2d): Matrix2d = Matrix2d(matrix).scale(this.toDouble())
operator fun Double.times(matrix: Matrix2d): Matrix2d = Matrix2d(matrix).scale(this)
operator fun Number.times(matrix: Matrix2d): Matrix2d = Matrix2d(matrix).scale(this.toDouble())

operator fun Matrix2d.times(vec: Vector2d): Vector2d = Matrix2d(this).transform(vec)

operator fun Vector2d.times(matrix: Matrix2d): Vector2d = Matrix2d(matrix).transpose().transform(this)

operator fun Matrix2d.times(matrix: Matrix2d): Matrix2d = Matrix2d(this).mul(matrix)


// Matrix3f operations

operator fun Matrix3f.unaryMinus(): Matrix3f = Matrix3f(this).invert()

operator fun Matrix3f.not(): Matrix3f = Matrix3f(this).invert()

operator fun Matrix3f.plus(matrix: Matrix3f): Matrix3f = Matrix3f(this).add(matrix)

operator fun Matrix3f.minus(matrix: Matrix3f): Matrix3f = Matrix3f(this).sub(matrix)

operator fun Matrix3f.times(n: Int): Matrix3f = Matrix3f(this).scale(n.toFloat())
operator fun Matrix3f.times(n: Long): Matrix3f = Matrix3f(this).scale(n.toFloat())
operator fun Matrix3f.times(n: Float): Matrix3f = Matrix3f(this).scale(n)
operator fun Matrix3f.times(n: Double): Matrix3f = Matrix3f(this).scale(n.toFloat())
operator fun Matrix3f.times(n: Number): Matrix3f = Matrix3f(this).scale(n.toFloat())

operator fun Int.times(matrix: Matrix3f): Matrix3f = Matrix3f(matrix).scale(this.toFloat())
operator fun Long.times(matrix: Matrix3f): Matrix3f = Matrix3f(matrix).scale(this.toFloat())
operator fun Float.times(matrix: Matrix3f): Matrix3f = Matrix3f(matrix).scale(this)
operator fun Double.times(matrix: Matrix3f): Matrix3f = Matrix3f(matrix).scale(this.toFloat())
operator fun Number.times(matrix: Matrix3f): Matrix3f = Matrix3f(matrix).scale(this.toFloat())

operator fun Matrix3f.times(vec: Vector3f): Vector3f = Matrix3f(this).transform(vec)

operator fun Vector3f.times(matrix: Matrix3f): Vector3f = Matrix3f(matrix).transpose().transform(this)

operator fun Matrix3f.times(matrix: Matrix3f): Matrix3f = Matrix3f(this).mul(matrix)


// Matrix3d operations

operator fun Matrix3d.unaryMinus(): Matrix3d = Matrix3d(this).invert()

operator fun Matrix3d.not(): Matrix3d = Matrix3d(this).invert()

operator fun Matrix3d.plus(matrix: Matrix3d): Matrix3d = Matrix3d(this).add(matrix)

operator fun Matrix3d.minus(matrix: Matrix3d): Matrix3d = Matrix3d(this).sub(matrix)

operator fun Matrix3d.times(n: Int): Matrix3d = Matrix3d(this).scale(n.toDouble())
operator fun Matrix3d.times(n: Long): Matrix3d = Matrix3d(this).scale(n.toDouble())
operator fun Matrix3d.times(n: Float): Matrix3d = Matrix3d(this).scale(n.toDouble())
operator fun Matrix3d.times(n: Double): Matrix3d = Matrix3d(this).scale(n)
operator fun Matrix3d.times(n: Number): Matrix3d = Matrix3d(this).scale(n.toDouble())

operator fun Int.times(matrix: Matrix3d): Matrix3d = Matrix3d(matrix).scale(this.toDouble())
operator fun Long.times(matrix: Matrix3d): Matrix3d = Matrix3d(matrix).scale(this.toDouble())
operator fun Float.times(matrix: Matrix3d): Matrix3d = Matrix3d(matrix).scale(this.toDouble())
operator fun Double.times(matrix: Matrix3d): Matrix3d = Matrix3d(matrix).scale(this)
operator fun Number.times(matrix: Matrix3d): Matrix3d = Matrix3d(matrix).scale(this.toDouble())

operator fun Matrix3d.times(vec: Vector3d): Vector3d = Matrix3d(this).transform(vec)

operator fun Vector3d.times(matrix: Matrix3d): Vector3d = Matrix3d(matrix).transpose().transform(this)

operator fun Matrix3d.times(matrix: Matrix3d): Matrix3d = Matrix3d(this).mul(matrix)


// Matrix3x2f operations

operator fun Matrix3x2f.unaryMinus(): Matrix3x2f = Matrix3x2f(this).invert()

operator fun Matrix3x2f.not(): Matrix3x2f = Matrix3x2f(this).invert()

operator fun Matrix3x2f.times(n: Int): Matrix3x2f = Matrix3x2f(this).scale(n.toFloat())
operator fun Matrix3x2f.times(n: Long): Matrix3x2f = Matrix3x2f(this).scale(n.toFloat())
operator fun Matrix3x2f.times(n: Float): Matrix3x2f = Matrix3x2f(this).scale(n)
operator fun Matrix3x2f.times(n: Double): Matrix3x2f = Matrix3x2f(this).scale(n.toFloat())
operator fun Matrix3x2f.times(n: Number): Matrix3x2f = Matrix3x2f(this).scale(n.toFloat())

operator fun Int.times(matrix: Matrix3x2f): Matrix3x2f = Matrix3x2f(matrix).scale(this.toFloat())
operator fun Long.times(matrix: Matrix3x2f): Matrix3x2f = Matrix3x2f(matrix).scale(this.toFloat())
operator fun Float.times(matrix: Matrix3x2f): Matrix3x2f = Matrix3x2f(matrix).scale(this)
operator fun Double.times(matrix: Matrix3x2f): Matrix3x2f = Matrix3x2f(matrix).scale(this.toFloat())
operator fun Number.times(matrix: Matrix3x2f): Matrix3x2f = Matrix3x2f(matrix).scale(this.toFloat())

operator fun Matrix3x2f.times(vec: Vector3f): Vector3f = Matrix3x2f(this).transform(vec)

operator fun Matrix3x2f.times(matrix: Matrix3x2f): Matrix3x2f = Matrix3x2f(this).mul(matrix)


// Matrix3x2d operations

operator fun Matrix3x2d.unaryMinus(): Matrix3x2d = Matrix3x2d(this).invert()

operator fun Matrix3x2d.not(): Matrix3x2d = Matrix3x2d(this).invert()

operator fun Matrix3x2d.times(n: Int): Matrix3x2d = Matrix3x2d(this).scale(n.toDouble())
operator fun Matrix3x2d.times(n: Long): Matrix3x2d = Matrix3x2d(this).scale(n.toDouble())
operator fun Matrix3x2d.times(n: Float): Matrix3x2d = Matrix3x2d(this).scale(n.toDouble())
operator fun Matrix3x2d.times(n: Double): Matrix3x2d = Matrix3x2d(this).scale(n)
operator fun Matrix3x2d.times(n: Number): Matrix3x2d = Matrix3x2d(this).scale(n.toDouble())

operator fun Int.times(matrix: Matrix3x2d): Matrix3x2d = Matrix3x2d(matrix).scale(this.toDouble())
operator fun Long.times(matrix: Matrix3x2d): Matrix3x2d = Matrix3x2d(matrix).scale(this.toDouble())
operator fun Float.times(matrix: Matrix3x2d): Matrix3x2d = Matrix3x2d(matrix).scale(this.toDouble())
operator fun Double.times(matrix: Matrix3x2d): Matrix3x2d = Matrix3x2d(matrix).scale(this)
operator fun Number.times(matrix: Matrix3x2d): Matrix3x2d = Matrix3x2d(matrix).scale(this.toDouble())

operator fun Matrix3x2d.times(vec: Vector3d): Vector3d = Matrix3x2d(this).transform(vec)

operator fun Matrix3x2d.times(matrix: Matrix3x2d): Matrix3x2d = Matrix3x2d(this).mul(matrix)


// Matrix4f operations

operator fun Matrix4f.unaryMinus(): Matrix4f = Matrix4f(this).invert()

operator fun Matrix4f.not(): Matrix4f = Matrix4f(this).invert()

operator fun Matrix4f.plus(matrix: Matrix4f): Matrix4f = Matrix4f(this).add(matrix)

operator fun Matrix4f.minus(matrix: Matrix4f): Matrix4f = Matrix4f(this).sub(matrix)

operator fun Matrix4f.times(n: Int): Matrix4f = Matrix4f(this).scale(n.toFloat())
operator fun Matrix4f.times(n: Long): Matrix4f = Matrix4f(this).scale(n.toFloat())
operator fun Matrix4f.times(n: Float): Matrix4f = Matrix4f(this).scale(n)
operator fun Matrix4f.times(n: Double): Matrix4f = Matrix4f(this).scale(n.toFloat())
operator fun Matrix4f.times(n: Number): Matrix4f = Matrix4f(this).scale(n.toFloat())

operator fun Int.times(matrix: Matrix4f): Matrix4f = Matrix4f(matrix).scale(this.toFloat())
operator fun Long.times(matrix: Matrix4f): Matrix4f = Matrix4f(matrix).scale(this.toFloat())
operator fun Float.times(matrix: Matrix4f): Matrix4f = Matrix4f(matrix).scale(this)
operator fun Double.times(matrix: Matrix4f): Matrix4f = Matrix4f(matrix).scale(this.toFloat())
operator fun Number.times(matrix: Matrix4f): Matrix4f = Matrix4f(matrix).scale(this.toFloat())

operator fun Matrix4f.times(vec: Vector4f): Vector4f = Matrix4f(this).transform(vec)

operator fun Vector4f.times(matrix: Matrix4f): Vector4f = Matrix4f(matrix).transpose().transform(this)

operator fun Matrix4f.times(matrix: Matrix4f): Matrix4f = Matrix4f(this).mul(matrix)


// Matrix4d operations

operator fun Matrix4d.unaryMinus(): Matrix4d = Matrix4d(this).invert()

operator fun Matrix4d.not(): Matrix4d = Matrix4d(this).invert()

operator fun Matrix4d.plus(matrix: Matrix4d): Matrix4d = Matrix4d(this).add(matrix)

operator fun Matrix4d.minus(matrix: Matrix4d): Matrix4d = Matrix4d(this).sub(matrix)

operator fun Matrix4d.times(n: Int): Matrix4d = Matrix4d(this).scale(n.toDouble())
operator fun Matrix4d.times(n: Long): Matrix4d = Matrix4d(this).scale(n.toDouble())
operator fun Matrix4d.times(n: Float): Matrix4d = Matrix4d(this).scale(n.toDouble())
operator fun Matrix4d.times(n: Double): Matrix4d = Matrix4d(this).scale(n)
operator fun Matrix4d.times(n: Number): Matrix4d = Matrix4d(this).scale(n.toDouble())

operator fun Int.times(matrix: Matrix4d): Matrix4d = Matrix4d(matrix).scale(this.toDouble())
operator fun Long.times(matrix: Matrix4d): Matrix4d = Matrix4d(matrix).scale(this.toDouble())
operator fun Float.times(matrix: Matrix4d): Matrix4d = Matrix4d(matrix).scale(this.toDouble())
operator fun Double.times(matrix: Matrix4d): Matrix4d = Matrix4d(matrix).scale(this)
operator fun Number.times(matrix: Matrix4d): Matrix4d = Matrix4d(matrix).scale(this.toDouble())

operator fun Matrix4d.times(vec: Vector4d): Vector4d = Matrix4d(this).transform(vec)

operator fun Vector4d.times(matrix: Matrix4d): Vector4d = Matrix4d(matrix).transpose().transform(this)

operator fun Matrix4d.times(matrix: Matrix4d): Matrix4d = Matrix4d(this).mul(matrix)


// Matrix4x3f operations

operator fun Matrix4x3f.unaryMinus(): Matrix4x3f = Matrix4x3f(this).invert()

operator fun Matrix4x3f.not(): Matrix4x3f = Matrix4x3f(this).invert()

operator fun Matrix4x3f.plus(matrix: Matrix4x3f): Matrix4x3f = Matrix4x3f(this).add(matrix)

operator fun Matrix4x3f.minus(matrix: Matrix4x3f): Matrix4x3f = Matrix4x3f(this).sub(matrix)

operator fun Matrix4x3f.times(n: Int): Matrix4x3f = Matrix4x3f(this).scale(n.toFloat())
operator fun Matrix4x3f.times(n: Long): Matrix4x3f = Matrix4x3f(this).scale(n.toFloat())
operator fun Matrix4x3f.times(n: Float): Matrix4x3f = Matrix4x3f(this).scale(n)
operator fun Matrix4x3f.times(n: Double): Matrix4x3f = Matrix4x3f(this).scale(n.toFloat())
operator fun Matrix4x3f.times(n: Number): Matrix4x3f = Matrix4x3f(this).scale(n.toFloat())

operator fun Int.times(matrix: Matrix4x3f): Matrix4x3f = Matrix4x3f(matrix).scale(this.toFloat())
operator fun Long.times(matrix: Matrix4x3f): Matrix4x3f = Matrix4x3f(matrix).scale(this.toFloat())
operator fun Float.times(matrix: Matrix4x3f): Matrix4x3f = Matrix4x3f(matrix).scale(this)
operator fun Double.times(matrix: Matrix4x3f): Matrix4x3f = Matrix4x3f(matrix).scale(this.toFloat())
operator fun Number.times(matrix: Matrix4x3f): Matrix4x3f = Matrix4x3f(matrix).scale(this.toFloat())

operator fun Matrix4x3f.times(vec: Vector4f): Vector4f = Matrix4x3f(this).transform(vec)

operator fun Matrix4x3f.times(matrix: Matrix4x3f): Matrix4x3f = Matrix4x3f(this).mul(matrix)


// Matrix4x3d operations

operator fun Matrix4x3d.unaryMinus(): Matrix4x3d = Matrix4x3d(this).invert()

operator fun Matrix4x3d.not(): Matrix4x3d = Matrix4x3d(this).invert()

operator fun Matrix4x3d.plus(matrix: Matrix4x3d): Matrix4x3d = Matrix4x3d(this).add(matrix)

operator fun Matrix4x3d.minus(matrix: Matrix4x3d): Matrix4x3d = Matrix4x3d(this).sub(matrix)

operator fun Matrix4x3d.times(n: Int): Matrix4x3d = Matrix4x3d(this).scale(n.toDouble())
operator fun Matrix4x3d.times(n: Long): Matrix4x3d = Matrix4x3d(this).scale(n.toDouble())
operator fun Matrix4x3d.times(n: Float): Matrix4x3d = Matrix4x3d(this).scale(n.toDouble())
operator fun Matrix4x3d.times(n: Double): Matrix4x3d = Matrix4x3d(this).scale(n)
operator fun Matrix4x3d.times(n: Number): Matrix4x3d = Matrix4x3d(this).scale(n.toDouble())

operator fun Int.times(matrix: Matrix4x3d): Matrix4x3d = Matrix4x3d(matrix).scale(this.toDouble())
operator fun Long.times(matrix: Matrix4x3d): Matrix4x3d = Matrix4x3d(matrix).scale(this.toDouble())
operator fun Float.times(matrix: Matrix4x3d): Matrix4x3d = Matrix4x3d(matrix).scale(this.toDouble())
operator fun Double.times(matrix: Matrix4x3d): Matrix4x3d = Matrix4x3d(matrix).scale(this)
operator fun Number.times(matrix: Matrix4x3d): Matrix4x3d = Matrix4x3d(matrix).scale(this.toDouble())

operator fun Matrix4x3d.times(vec: Vector4d): Vector4d = Matrix4x3d(this).transform(vec)

operator fun Matrix4x3d.times(matrix: Matrix4x3d): Matrix4x3d = Matrix4x3d(this).mul(matrix)
