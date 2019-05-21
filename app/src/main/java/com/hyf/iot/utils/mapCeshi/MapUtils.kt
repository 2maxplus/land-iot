package com.hyf.iot.utils.mapCeshi

import android.graphics.Point
import android.support.annotation.Size

import com.baidu.mapapi.map.MapView
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

}
