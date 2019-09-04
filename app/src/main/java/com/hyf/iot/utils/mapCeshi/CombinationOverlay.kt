package com.hyf.iot.utils.mapCeshi

import android.widget.Toast
import com.baidu.mapapi.map.*

import com.baidu.mapapi.model.LatLng
import com.hyf.iot.R
import com.hyf.iot.domain.LatLonData
import com.hyf.iot.utils.mapCeshi.MapUtils.getLatLngByOffset

import java.util.ArrayList
import com.hyf.iot.domain.MyLatLng
import kotlin.math.abs

/**
 * CombinationOverlay  组合覆盖 一个代表一个组
 */

class CombinationOverlay(private val mMapView: MapView) {
    private val mBaiduMap: BaiduMap
    private val polygonOptions = PolygonOptions()
    private var lineListList: MutableList<MutableList<LatLng>>? = null
    private var mMarkerList: ArrayList<LatLonData> = ArrayList()

    private var polygonOverlay: Polygon? = null
    private var markerList: MutableList<Marker>? = null
    private var polylineList: MutableList<Polyline>? = null
    private var bdA = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_dot_anchor)
    private var latLngList: MutableList<LatLng> = mutableListOf()
    private var polyline1: Polyline? = null

    private val stroke = Stroke(6, 0x55FF33FF)

    init {
//        if (latLngList!!.size < 3) {
//            throw IllegalArgumentException("点数小于3，无法构成多边形")
//        }
        mBaiduMap = mMapView.map
//        initZiyuan()
    }

    fun initZiyuan(latLngList: MutableList<LatLng>?) {
        if (latLngList != null) {
            this.latLngList = latLngList
        }
        polygonOptions.points(latLngList!!)
        polygonOptions.stroke(stroke)
        polygonOptions.fillColor(0x551791fc)
        polygonOverlay = mBaiduMap.addOverlay(polygonOptions) as Polygon
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
            val polylineOptions = PolylineOptions().points(latLngLineList).color(0x55FF33FF).focus(true).width(6)
            val polyline = mBaiduMap.addOverlay(polylineOptions) as Polyline
            polylineList!!.add(polyline)
        }

        for (i in latLngList!!.indices) {
            val markerOptions = MarkerOptions().position(latLngList!![i]).icon(bdA).draggable(true).anchor(0.5f,0.5f)
            val marker = mBaiduMap.addOverlay(markerOptions) as Marker
            markerList!!.add(marker)
        }
    }

    fun getLatLngMarkerList(): ArrayList<LatLonData>? {
        return mMarkerList
    }

    fun setLatLngMarkerList(mMarkerList: ArrayList<LatLonData>) {
        this.mMarkerList = mMarkerList
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

    fun setLatLngList(latLngList: MutableList<LatLng>) {
        this.latLngList = latLngList
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
        latLngList = getLatLngByOffset(mMapView, latLngList!!, offsetx, offsety)!!
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
//        initZiyuan()
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
//            initZiyuan()
        }
        mBaiduMap.hideInfoWindow()
    }

    /**
     * 移除覆盖物
     */
    private fun removeCombinationOverlay() {
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

    fun getMidPoint(latLng0: LatLng, latLng1: LatLng): LatLng {
        val dx = if (latLng0.latitude - latLng1.latitude > 0) {
            abs(latLng0.latitude - latLng1.latitude) / 2 + latLng1.latitude
        } else {
            abs(latLng0.latitude - latLng1.latitude) / 2 + latLng0.latitude
        }

        val dy = if (latLng0.longitude - latLng1.longitude > 0) {
            abs(latLng0.longitude - latLng1.longitude) / 2 + latLng1.longitude
        } else {
            abs(latLng0.longitude - latLng1.longitude) / 2 + latLng0.longitude
        }
        return LatLng(dx, dy)
    }

    fun drawArea(): Polygon {
        if(polygonOverlay != null){
            polygonOverlay!!.remove()
        }
        if(polyline1 != null){
            polyline1!!.remove()
        }
        val mPolygonOptions = PolygonOptions()
                .points(latLngList)
                .fillColor(0x551791fc) //填充颜色
                .stroke(Stroke(6, 0x55FF33FF)) //边框宽度和颜色
//        convertViewToBitmap(toScreenPoints()!!)
        polygonOverlay = mBaiduMap.addOverlay(mPolygonOptions) as Polygon
        return polygonOverlay as Polygon
    }


    fun drawPoint(p0: LatLng?) {
        val option: MarkerOptions = MarkerOptions()
                .position(p0)
                .icon(bdA)
                .anchor(0.5f, 0.5f)
        val marker = mBaiduMap.addOverlay(option) as Marker
        mMarkerList.add(LatLonData(marker, false))
        if (p0 != null) {
            latLngList.add(p0)
//            pos = latLngList!!.size - 1
        }
    }

    fun drawPoints() {
        mMarkerList.forEach {
            it.marker.remove()
        }
        mMarkerList.clear()
        //线段中间加小锚点
        latLngList.forEach {
            val option: MarkerOptions = MarkerOptions()
                    .position(it)
                    .icon(bdA)
                    .anchor(0.5f, 0.5f)
            val marker = mBaiduMap.addOverlay(option) as Marker
            mMarkerList.add(LatLonData(marker, false))
        }
    }


    /**
     *  画线
     *
     * */
    fun drawLine() {
        mBaiduMap.clear()
        mMarkerList.clear()
        if (latLngList!!.size >= 2) {
            //在地图上绘制折线
            //mPloyline 折线对象
            val mOverlayOptions = PolylineOptions()
                    .width(6)
                    .color(0x55FF33FF)
                    .points(latLngList)
            polyline1 = mBaiduMap.addOverlay(mOverlayOptions) as Polyline
        }

        latLngList.forEach {
            val option: MarkerOptions = MarkerOptions()
                    .position(it)
                    .icon(bdA)
                    .anchor(0.5f, 0.5f)
            val marker = mBaiduMap.addOverlay(option) as Marker
            mMarkerList.add(LatLonData(marker, false))
        }
//        initMarker(mLoactionLatLng!!)
    }

}
