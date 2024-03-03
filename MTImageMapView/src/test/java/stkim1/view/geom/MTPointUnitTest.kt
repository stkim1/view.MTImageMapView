package stkim1.view.geom

import org.junit.Assert
import org.junit.Test
import java.security.InvalidParameterException

class MTPointUnitTest {

    @Test
    fun check_isCreatedCorrect() {
        val zpt = MTPoint()
        Assert.assertEquals(zpt.x, 0.0, 0.0)
        Assert.assertEquals(zpt.y, 0.0, 0.0)

        val x = 1234.5678
        val y = 8765.4321

        val npt = MTPoint(x, y)
        Assert.assertEquals(x, npt.x, 0.0)
        Assert.assertEquals(y, npt.y, 0.0)

        val fpt = MTPoint(floatArrayOf(x.toFloat(), y.toFloat()))
        Assert.assertEquals(x, fpt.x, 0.001)
        Assert.assertEquals(y, fpt.y, 0.001)

        val dpt = MTPoint(doubleArrayOf(x, y))
        Assert.assertEquals(x, dpt.x, 0.0)
        Assert.assertEquals(y, dpt.y, 0.0)

        val opt = MTPoint(listOf(x, y))
        Assert.assertEquals(x, opt.x, 0.0)
        Assert.assertEquals(y, opt.y, 0.0)
    }

    @Test(expected = NullPointerException::class)
    fun check_throwNullPointerException() {
        val plist : List<Double> = null as List<Double>
        MTPoint(plist)
    }

    @Test
    fun check_throwInsufficientPoints() {
        val exception = Assert.assertThrows(InvalidParameterException::class.java) {
            MTPoint(listOf(0.0))
        }
        Assert.assertEquals("MTPoint must be instantiated with two points.", exception.message)
    }
}