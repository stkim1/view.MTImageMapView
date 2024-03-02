package stkim1.view.geom

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import stkim1.view.geom.MTPoint
import stkim1.view.geom.MTPolygon
import java.io.InvalidObjectException
import java.security.InvalidParameterException

class MTPolygonUnitTest {

    @Test(expected = NullPointerException::class)
    fun check_invalidInstantiation() {
        val vts : List<MTPoint> = null as List<MTPoint>
        MTPolygon(null, vts)
    }

    @Test
    fun check_insufficientInstantiation() {
        val exception = assertThrows(InvalidParameterException::class.java) {
            val vts : List<MTPoint> = listOf(
                MTPoint(),
                MTPoint()
            )
            MTPolygon(null, vts)
        }
        assertEquals("MTPolygon must be instantiated with at least three vertices.", exception.message)
    }

    @Test
    fun check_insufficientPointsOfClosing() {
        val plg = MTPolygon()
        plg.addVertex(MTPoint())
        plg.addVertex(MTPoint())

        val exception = assertThrows(InvalidObjectException::class.java) {
            plg.close()
        }
        assertEquals("MTPolygon must have at least three vertices.", exception.message)
    }

    @Test
    fun check_invalidStateOfAddition() {
        val plg = MTPolygon()
        plg.addVertex(MTPoint())
        plg.addVertex(MTPoint())
        plg.addVertex(MTPoint())
        plg.close()

        val exception = assertThrows(InvalidObjectException::class.java) {
            plg.addVertex(MTPoint())
        }
        assertEquals("A closed MTPolygon cannot take a further vertex.", exception.message)
    }

    @Test
    fun check_invalidStateOfVerticesAddition() {
        val plg = MTPolygon()
        plg.addVertex(MTPoint())
        plg.addVertex(MTPoint())
        plg.addVertex(MTPoint())
        plg.close()

        val exception = assertThrows(InvalidObjectException::class.java) {
            plg.addVertices(arrayOf(
                MTPoint(),
                MTPoint()
            ))
        }
        assertEquals("A closed MTPolygon cannot take a further vertex.", exception.message)
    }

    @Test
    fun check_containedInTriangle() {
        val plg = MTPolygon()
        plg.addVertex(MTPoint(2.0, 2.0))
        plg.addVertex(MTPoint(8.48, 1.28))
        plg.addVertex(MTPoint(4.0, 5.0))
        plg.close()

        val pin0 = MTPoint(4.48704, 2.43366)
        assertTrue(plg.isPointInBBox(pin0))
        assertTrue(plg.isPointInPolygon(pin0))

        val pout0 = MTPoint(0.74518, 5.73454)
        assertFalse(plg.isPointInBBox(pout0))
        assertFalse(plg.isPointInPolygon(pout0))

        val pout1 =
            MTPoint(2.6280081207657, 1.6544778832361)
        assertTrue(plg.isPointInBBox(pout1))
        assertFalse(plg.isPointInPolygon(pout1))

        val pout2 =
            MTPoint(2.2047619322035, 0.5504882657519)
        assertFalse(plg.isPointInBBox(pout2))
        assertFalse(plg.isPointInPolygon(pout2))
    }

    @Test
    fun check_containedInPolygon() {
        val plg = MTPolygon()
        plg.addVertex(
            MTPoint(
                -3.9060685661704,
                3.1409490204973
            )
        )
        plg.addVertex(
            MTPoint(
                -4.1717568487084,
                -1.4753848886004
            )
        )
        plg.addVertex(
            MTPoint(
                1.606963296493,
                -2.7706152659731
            )
        )
        plg.addVertex(
            MTPoint(
                4.6291675103627,
                0.2183779125794
            )
        )
        plg.addVertex(
            MTPoint(
                2.8357716032312,
                3.6723255855733
            )
        )
        plg.addVertex(
            MTPoint(
                0.942742590148,
                5.2996663161186
            )
        )
        plg.close()

        val pin0 = MTPoint(0.0, 0.0)
        assertTrue(plg.isPointInBBox(pin0))
        assertTrue(plg.isPointInPolygon(pin0))

        val pout0 =
            MTPoint(-2.5166132712889, 5.0510486438397)
        assertTrue(plg.isPointInBBox(pout0))
        assertFalse(plg.isPointInPolygon(pout0))

        val pout1 =
            MTPoint(-2.1452355241319, -2.422928517696)
        assertTrue(plg.isPointInBBox(pout1))
        assertFalse(plg.isPointInPolygon(pout1))

        val pout2 =
            MTPoint(3.8419772309624, -1.6032547323138)
        assertTrue(plg.isPointInBBox(pout2))
        assertFalse(plg.isPointInPolygon(pout2))

        val pout3 =
            MTPoint(-2.5423726942885, -4.62742048638)
        assertFalse(plg.isPointInBBox(pout3))
        assertFalse(plg.isPointInPolygon(pout3))
    }


    @Test
    fun check_containedInPineApplePolygon() {
        val plg = MTPolygon()
        plg.addVertex(
            MTPoint(
                -36.7450621119697,
                90.8717242738671
            )
        )
        plg.addVertex(
            MTPoint(
                -7.0503074176843,
                67.9257774646465
            )
        )
        plg.addVertex(
            MTPoint(
                26.69373200764,
                38.9059035588676
            )
        )
        plg.addVertex(
            MTPoint(
                41.5411093547827,
                7.1865064990627
            )
        )
        plg.addVertex(
            MTPoint(
                37.4918246237437,
                -44.1044334274302
            )
        )
        plg.addVertex(
            MTPoint(
                5.7724275639389,
                -73.7991881217157
            )
        )
        plg.addVertex(
            MTPoint(
                -46.868273939567,
                -79.873115218274
            )
        )
        plg.addVertex(
            MTPoint(
                -72.5137439028134,
                -47.4788373699627
            )
        )
        plg.addVertex(MTPoint(-65.5137439028134, 0.0))
        plg.addVertex(
            MTPoint(
                -15.8237576682686,
                33.5068572508157
            )
        )
        plg.addVertex(
            MTPoint(
                45.5903940858216,
                65.2262543106206
            )
        )
        plg.addVertex(
            MTPoint(
                88.1078837617301,
                71.300181407179
            )
        )
        plg.addVertex(
            MTPoint(
                33.4425398927048,
                102.3446976784774
            )
        )
        plg.close()

        val pin0 = MTPoint(0.0, 0.0)
        assertTrue(plg.isPointInBBox(pin0))
        assertTrue(plg.isPointInPolygon(pin0))

        val pin1 = MTPoint(
            -13.7991153027492,
            87.4973203313347
        )
        assertTrue(plg.isPointInBBox(pin1))
        assertTrue(plg.isPointInPolygon(pin1))

        val pin2 = MTPoint(
            73.1160909482139,
            74.1486399135347
        )
        assertTrue(plg.isPointInBBox(pin2))
        assertTrue(plg.isPointInPolygon(pin2))

        val pin3 = MTPoint(
            11.6838771059102,
            38.4269451978247
        )
        assertTrue(plg.isPointInBBox(pin3))
        assertTrue(plg.isPointInPolygon(pin3))


        val pout0 = MTPoint(
            -54.2919626131383,
            57.1276848485428
        )
        assertTrue(plg.isPointInBBox(pout0))
        assertFalse(plg.isPointInPolygon(pout0))

        val pout1 = MTPoint(
            74.6102679916004,
            14.6101951726341
        )
        assertTrue(plg.isPointInBBox(pout1))
        assertFalse(plg.isPointInPolygon(pout1))

        val pout2 = MTPoint(
            -1.0576190984195,
            46.8454337613997
        )
        assertTrue(plg.isPointInBBox(pout2))
        assertFalse(plg.isPointInPolygon(pout2))

        val pout3 = MTPoint(
            35.5741824890283,
            47.0729604793341
        )
        assertTrue(plg.isPointInBBox(pout3))
        assertFalse(plg.isPointInPolygon(pout3))


        val out0 = MTPoint(
            -101.3159567336118,
            86.212692141246
        )
        assertFalse(plg.isPointInBBox(out0))
        assertFalse(plg.isPointInPolygon(out0))

        val out1 = MTPoint(
            18.4406237273981,
            151.4386229418981
        )
        assertFalse(plg.isPointInBBox(out1))
        assertFalse(plg.isPointInPolygon(out1))

        val out2 = MTPoint(
            27.3914914582772,
            -107.2105894193644
        )
        assertFalse(plg.isPointInBBox(out2))
        assertFalse(plg.isPointInPolygon(out2))
    }
}