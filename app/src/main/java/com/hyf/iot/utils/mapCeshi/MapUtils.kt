package com.hyf.iot.utils.mapCeshi

import android.graphics.Point
import android.support.annotation.Size

import com.baidu.mapapi.map.MapView
import com.baidu.mapapi.map.Polygon
import com.baidu.mapapi.map.Projection
import com.baidu.mapapi.model.LatLng
import kotlin.math.absoluteValue

/**
 * Created by DELL on 2018/4/13.
 */

object MapUtils {

    /**
     * 获取线段中心点坐标
     * @param mPoints
     * @return
     */
    fun getCenterOfLines(mapView: MapView, @Size(2) mPoints: List<LatLng>): LatLng {
        if (mPoints.size != 2) {
            throw IllegalArgumentException("线段点个数应为2个")
        }
        val projection = mapView.map.projection
        val point0 = projection.toScreenLocation(mPoints[0])
        val point1 = projection.toScreenLocation(mPoints[1])
        val point = getCenterOfScrrenTwoPoint(point0, point1)
        return projection.fromScreenLocation(point)
    }

    /**
     * 获取屏幕上两点的中心坐标
     * @return
     */
    fun getCenterOfScrrenTwoPoint(point0: Point, point1: Point): Point {
        return Point((point0.x + point1.x) / 2, (point0.y + point1.y) / 2)
    }

    /**
     * 根据偏移量获取新的坐标值
     * @param list
     * @param offsetx
     * @param offsety
     * @return
     */
    fun getLatLngByOffset(mapView: MapView, list: MutableList<LatLng>, offsetx: Float, offsety: Float): MutableList<LatLng>? {
        val projection = mapView.map.projection

        for (i in list.indices) {
            val tempPoint = projection.toScreenLocation(list[i])
            tempPoint.offset(offsetx.toInt(), offsety.toInt())
            list[i] = projection.fromScreenLocation(tempPoint)
        }
        return list
    }

    //计算多边形重心  也可计算面积
    fun getCenterOfGravityPoint(mPoints: List<LatLng>): LatLng {
        var area = 0.0//多边形面积
        var Gx = 0.0
        var Gy = 0.0// 重心的x、y
        for (i in 1..mPoints.size) {
            val iLat = mPoints[i % mPoints.size].latitude
            val iLng = mPoints[i % mPoints.size].longitude
            val nextLat = mPoints[i - 1].latitude
            val nextLng = mPoints[i - 1].longitude
            val temp = (iLat * nextLng - iLng * nextLat) / 2.0
            area += temp
            Gx += temp * (iLat + nextLat) / 3.0
            Gy += temp * (iLng + nextLng) / 3.0
        }
        Gx /= area
        Gy /= area
        return LatLng(Gx, Gy)
    }

    fun getArea(pl: Polygon?): Double {
        val pts = pl?.points ?: return 0.0
        //判断数组的长度，如果是小于3的话，不构成面，无法计算面积
        if (pts.size < 3) {
            return 0.0
        }

        var totalArea: Double
        var LowX: Double
        var LowY: Double
        var MiddleX: Double
        var MiddleY: Double
        var HighX: Double
        var HighY: Double
        var AM: Double
        var BM: Double
        var CM: Double
        var AL: Double
        var BL: Double
        var CL: Double
        var AH: Double
        var BH: Double
        var CH: Double
        var CoefficientL: Double
        var CoefficientH: Double
        var ALtangent: Double
        var BLtangent: Double
        var CLtangent: Double
        var AHtangent: Double
        var BHtangent: Double
        var CHtangent: Double
        var ANormalLine: Double
        var BNormalLine: Double
        var CNormalLine: Double
        var OrientationValue: Double
        var AngleCos: Double
        var Sum1: Double = 0.0
        var Sum2: Double = 0.0
        var Count2: Int = 0
        var Count1: Int = 0
        var Sum: Double
        var Radius = 6378137.0// ,WGS84椭球半径
        var Count = pts.size
        for (i in 0 until Count) {
            when (i) {
                0 -> {
                    LowX = pts[Count - 1].longitude * Math.PI / 180
                    LowY = pts[Count - 1].latitude * Math.PI / 180
                    MiddleX = pts[0].longitude * Math.PI / 180
                    MiddleY = pts[0].latitude * Math.PI / 180
                    HighX = pts[1].longitude * Math.PI / 180
                    HighY = pts[1].latitude * Math.PI / 180
                }
                Count - 1 -> {
                    LowX = pts[Count - 2].longitude * Math.PI / 180
                    LowY = pts[Count - 2].latitude * Math.PI / 180
                    MiddleX = pts[Count - 1].longitude * Math.PI / 180
                    MiddleY = pts[Count - 1].latitude * Math.PI / 180
                    HighX = pts[0].longitude * Math.PI / 180
                    HighY = pts[0].latitude * Math.PI / 180
                }
                else -> {
                    LowX = pts[i - 1].longitude * Math.PI / 180
                    LowY = pts[i - 1].latitude * Math.PI / 180
                    MiddleX = pts[i].longitude * Math.PI / 180
                    MiddleY = pts[i].latitude * Math.PI / 180
                    HighX = pts[i + 1].longitude * Math.PI / 180
                    HighY = pts[i + 1].latitude * Math.PI / 180
                }
            }
            AM = Math.cos(MiddleY) * Math.cos(MiddleX)
            BM = Math.cos(MiddleY) * Math.sin(MiddleX)
            CM = Math.sin(MiddleY)
            AL = Math.cos(LowY) * Math.cos(LowX)
            BL = Math.cos(LowY) * Math.sin(LowX)
            CL = Math.sin(LowY)
            AH = Math.cos(HighY) * Math.cos(HighX)
            BH = Math.cos(HighY) * Math.sin(HighX)
            CH = Math.sin(HighY)
            CoefficientL = (AM * AM + BM * BM + CM * CM) / (AM * AL + BM * BL + CM * CL)
            CoefficientH = (AM * AM + BM * BM + CM * CM) / (AM * AH + BM * BH + CM * CH)
            ALtangent = CoefficientL * AL - AM
            BLtangent = CoefficientL * BL - BM
            CLtangent = CoefficientL * CL - CM
            AHtangent = CoefficientH * AH - AM
            BHtangent = CoefficientH * BH - BM
            CHtangent = CoefficientH * CH - CM
            AngleCos =
                    (AHtangent * ALtangent + BHtangent * BLtangent + CHtangent * CLtangent) / (Math.sqrt(AHtangent * AHtangent + BHtangent * BHtangent + CHtangent * CHtangent) * Math.sqrt(
                            ALtangent * ALtangent + BLtangent * BLtangent + CLtangent * CLtangent
                    ))
            AngleCos = Math.acos(AngleCos)
            ANormalLine = BHtangent * CLtangent - CHtangent * BLtangent
            BNormalLine = 0 - (AHtangent * CLtangent - CHtangent * ALtangent)
            CNormalLine = AHtangent * BLtangent - BHtangent * ALtangent
            OrientationValue = when {
                AM != 0.0 -> ANormalLine / AM
                BM != 0.0 -> BNormalLine / BM
                else -> CNormalLine / CM
            }
            if (OrientationValue > 0) {
                Sum1 += AngleCos
                Count1++
            } else {
                Sum2 += AngleCos
                Count2++
            }
        }

        var tempSum1: Double
        var tempSum2: Double
        tempSum1 = Sum1 + (2 * Math.PI * Count2 - Sum2)
        tempSum2 = (2 * Math.PI * Count1 - Sum1) + Sum2
        Sum = if (Sum1 > Sum2) {
            if ((tempSum1 - (Count - 2) * Math.PI) < 1)
                tempSum1
            else
                tempSum2
        } else {
            if ((tempSum2 - (Count - 2) * Math.PI) < 1)
                tempSum2
            else
                tempSum1
        }
        totalArea = (Sum - (Count - 2) * Math.PI) * Radius * Radius

        return Math.abs(totalArea / 1000000)

    }

}
