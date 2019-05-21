package com.hyf.iot.utils.mapCeshi

import android.widget.Toast
import com.baidu.mapapi.map.*

import com.baidu.mapapi.model.LatLng
import com.hyf.iot.R
import com.hyf.iot.utils.mapCeshi.MapUtils.getLatLngByOffset

import java.util.ArrayList
import com.hyf.iot.domain.MyLatLng
import com.hyf.iot.utils.mapCeshi.MapUtils.getArea

/**
 * CombinationOverlay  组合覆盖 一个代表一个组
 */

class CombinationOverlay(private val mMapView: MapView, private var latLngList: MutableList<LatLng>?) {
    private val mBaiduMap: BaiduMap
    private val polygonOptions = PolygonOptions()
    private var lineListList: MutableList<MutableList<LatLng>>? = null
    private var polygonOverlay: Polygon? = null
    private var markerList: MutableList<Marker>? = null
    private var polylineList: MutableList<Polyline>? = null
    private var tempPolygon: Polygon? = null
    private var bdA = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_dot)
    private val stroke = Stroke(3, -0x55ff0100)

    init {
        if (latLngList!!.size < 3) {
            throw IllegalArgumentException("点数小于3，无法构成多边形")
        }
        mBaiduMap = mMapView.map
        initZiyuan()
    }

    private fun initZiyuan() {

        polygonOptions.points(latLngList!!)
        polygonOptions.stroke(stroke)
        polygonOptions.fillColor(-0x79010101)
        polygonOverlay = mBaiduMap.addOverlay(polygonOptions) as Polygon
        polygonOverlay
        markerList = ArrayList()

        lineListList = ArrayList()
        polylineList = ArrayList()
        for (i in latLngList!!.indices) {
            val latLngLineList = ArrayList<LatLng>()
            latLngLineList.add(latLngList!![i])
            if (i < latLngList!!.size - 1) {
                latLngLineList.add(latLngList!![i + 1])
            } else {
                latLngLineList.add(latLngList!![0])
            }
            lineListList!!.add(latLngLineList)
            val polylineOptions = PolylineOptions().points(latLngLineList).color(-0x55000100).focus(true).width(10)
            val polyline = mBaiduMap.addOverlay(polylineOptions) as Polyline
            polylineList!!.add(polyline)
        }

        for (i in latLngList!!.indices) {
            val markerOptions = MarkerOptions().position(latLngList!![i]).icon(bdA).draggable(true).anchor(0.5f,0.5f)
            val marker = mBaiduMap.addOverlay(markerOptions) as Marker
            markerList!!.add(marker)
        }

    }

    fun getMarkerList(): List<Marker>? {
        return markerList
    }

    fun setMarkerList(markerList: MutableList<Marker>) {
        this.markerList = markerList
    }

    fun getPolylineList(): List<Polyline>? {
        return polylineList
    }

    fun setPolylineList(polylineList: MutableList<Polyline>) {
        this.polylineList = polylineList
    }

    fun getLatLngList(): List<LatLng>? {
        return latLngList
    }

    /**
     * 更新覆盖物的位置
     */
    fun updateOverlayByMarker(marker: Marker) {
        val position = markerList!!.indexOf(marker)
        if (position == -1) {
            return
        }
        latLngList!![position] = marker.position
        polygonOverlay!!.points = latLngList!!
        if (position == 0) {  //第一个点   更新第一条线和最后一条线
            lineListList!![position][0] = marker.position  //更新第一个点的坐标
            polylineList!![position].points = lineListList!![position]

            lineListList!![lineListList!!.size - 1][1] = marker.position //更新第二个点
            polylineList!![polylineList!!.size - 1].points = lineListList!![polylineList!!.size - 1]
        } else {
            lineListList!![position][0] = marker.position  //更新第一个点
            polylineList!![position].points = lineListList!![position]

            lineListList!![position - 1][1] = marker.position
            polylineList!![position - 1].points = lineListList!![position - 1]
        }
    }

    /**
     * 根据总的点集合，刷新polygonOptions数据
     * */
    fun refreshPolygonOptions(){
        if (polygonOptions.points.size !== MyLatLng.UNABLE) {
            polygonOptions.points.clear()
        }
//        for (i in 0 until allLatLngs.size()) {
//            polygonOptions.add(allLatLngs.get(i).getLatLng())
//        }
    }

    /**
     * 根据偏移量更新显示位置
     * @param offsetx
     * @param offsety
     */
    fun updateOverlayByPolygon(offsetx: Float, offsety: Float)  {
        latLngList = getLatLngByOffset(mMapView, latLngList!!, offsetx, offsety)
        polygonOverlay!!.points = latLngList!!
        for (i in markerList!!.indices) {
            markerList!![i].position = latLngList!![i]
        }
        lineListList!!.clear()
        for (i in latLngList!!.indices) {
            val latLngLineList = ArrayList<LatLng>()
            latLngLineList.add(latLngList!![i])
            if (i < latLngList!!.size - 1) {
                latLngLineList.add(latLngList!![i + 1])
            } else {
                latLngLineList.add(latLngList!![0])
            }
            lineListList!!.add(latLngLineList)

            polylineList!![i].points = latLngLineList
        }
    }


    /**
     * 点击线条触发
     * @param polyline
     */
    fun updateOverlayByLineClick(polyline: Polyline) {
        val positon = polylineList!!.indexOf(polyline)
        if (-1 == positon) {
            return
        }
        val latLng = MapUtils.getCenterOfLines(mMapView, polyline.points)  //得到中心点
        latLngList!!.add(positon + 1, latLng)
        removeCombinationOverlay()
        initZiyuan()
    }

    /**
     * 点击 删除marker
     *
     * */
    fun updateOverlayByRemoveOneMarker(marker: Marker) {
        val positon = markerList!!.indexOf(marker)
        if (-1 == positon) {
            return
        }
        if (markerList!!.size < 4) {
            Toast.makeText(mMapView.context, "不能移除当前点，移除后无法构成多边形", Toast.LENGTH_SHORT).show()
        } else {
            latLngList!!.removeAt(positon)
            removeCombinationOverlay()
            initZiyuan()
        }
        mBaiduMap.hideInfoWindow()
    }


    /**
     * 移除覆盖物
     */
    fun removeCombinationOverlay() {
        polygonOverlay!!.remove()

        for (i in markerList!!.indices) {
            markerList!![i].remove()
        }

        for (i in polylineList!!.indices) {
            polylineList!![i].remove()
        }
        markerList!!.clear()
        polylineList!!.clear()
        lineListList!!.clear()

    }


}
