package com.hyf.iot.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import android.text.TextUtils
import android.util.Log
import com.baidu.mapapi.model.LatLng

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

object ImageUtils {

    const val REQUEST_CODE_GETIMAGE_BYCAMERA = 102

    /**
     * 根据Uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    fun getRealPathFromUri(context: Context, uri: Uri): String? {
        val sdkVersion = Build.VERSION.SDK_INT
        return if (sdkVersion >= 24) {
            getFilePathFromURI(context, uri)
        } else { // api < 19
            getRealPathFromUriBelowAPI19(context, uri)
        }
    }

    /**
     * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    private fun getRealPathFromUriBelowAPI19(context: Context, uri: Uri): String? {
        return getDataColumn(context, uri, null, null)
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     *
     * @return
     */
    private fun getDataColumn(context: Context, uri: Uri, selection: String?, selectionArgs: Array<String>?): String? {
        var path: String? = null

        val projection = arrayOf(MediaStore.MediaColumns.DATA)
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(projection[0])
                if (columnIndex > -1)
                    path = cursor.getString(columnIndex)
            }
        } catch (e: Exception) {
            cursor?.close()
        }

        return path
    }

    private fun getFilePathFromURI(context: Context, contentUri: Uri): String? {
        val rootDataDir = context.filesDir
        val fileName = getFileName(contentUri)
        if (!TextUtils.isEmpty(fileName)) {
            val copyFile = File(rootDataDir.toString() + File.separator + fileName)
            copyFile(context, contentUri, copyFile)
            return copyFile.absolutePath
        }
        return null
    }

    private fun getFileName(uri: Uri?): String? {
        if (uri == null) return null
        var fileName: String? = null
        val path = uri.path
        val cut = path!!.lastIndexOf('/')
        if (cut != -1) {
            fileName = path.substring(cut + 1)
        }
        return fileName
    }

    private fun copyFile(context: Context, srcUri: Uri, dstFile: File) {
        try {
            val inputStream = context.contentResolver.openInputStream(srcUri) ?: return
            val outputStream = FileOutputStream(dstFile)
            copyStream(inputStream, outputStream)
            inputStream.close()
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @Throws(Exception::class, IOException::class)
    fun copyStream(input: InputStream, output: OutputStream): Int {
        val BUFFER_SIZE = 1024 * 2
        val buffer = ByteArray(BUFFER_SIZE)
        val inputStream = BufferedInputStream(input, BUFFER_SIZE)
        val out = BufferedOutputStream(output, BUFFER_SIZE)
        var count = 0
        var n = 0
        try {
            do {
                n = inputStream.read(buffer, 0, BUFFER_SIZE)
                out.write(buffer, 0, n)
                count += n
            } while (n != -1)

            out.flush()
        } finally {
            try {
                out.close()
            } catch (e: IOException) {
            }

            try {
                inputStream.close()
            } catch (e: IOException) {
            }

        }
        return count
    }

    private fun getUriForFile(context: Context, file: File): Uri {
        if (file == null) {
            throw NullPointerException()
        }
        //判断是否是AndroidN以及更高的版本
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果SDK版本>=24，即：Build.VERSION.SDK_INT >= 24
            FileProvider.getUriForFile(context, context.packageName + ".provider", file)
        } else {
            Uri.fromFile(file)
        }
    }

    /**
     * 创建一条图片地址uri,用于保存拍照后的照片
     *
     * @param context
     * @return 图片的uri
     */

    fun createImagePathUri(context: Activity): Uri {
        var imageFilePath: Uri
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            imageFilePath = Uri.parse("")
            context?.showToast("请先获取写入SDCard权限")
        } else {
            //拍照前保存一条uri的地址
            val status = Environment.getExternalStorageState()
            val timeFormatter = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA)
            val time = System.currentTimeMillis()
            val imageName = timeFormatter.format(Date(time))
            val parentPath = if (status == Environment.MEDIA_MOUNTED) {// 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
                Environment.getExternalStorageDirectory().path
            } else {
                context.externalCacheDir.path
            }
            val fileName = parentPath + File.separator + imageName + ".jpg"
            imageFilePath = getUriForFile(context, File(fileName))
        }
        Log.i("camera", "生成的照片输出路径：$imageFilePath")
        return imageFilePath
    }

    fun startCamera(context: Activity): Uri {
        val imageUriFromCamera = createImagePathUri(context)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // MediaStore.EXTRA_OUTPUT参数不设置时,系统会自动生成一个uri,但是只会返回一个缩略图
        // 返回图片在onActivityResult中通过以下代码获取
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriFromCamera)//注意这一行
        context.startActivityForResult(intent, REQUEST_CODE_GETIMAGE_BYCAMERA)
        return imageUriFromCamera
    }

    /**
     * 计算多边形面积
     * 不准确
     * @return
     */
    fun getArea(pts: List<LatLng>): String {
        var totalArea = 0.0// 初始化总面积
        var LowX = 0.0
        var LowY = 0.0
        var MiddleX = 0.0
        var MiddleY = 0.0
        var HighX = 0.0
        var HighY = 0.0
        var AM = 0.0
        var BM = 0.0
        var CM = 0.0
        var AL = 0.0
        var BL = 0.0
        var CL = 0.0
        var AH = 0.0
        var BH = 0.0
        var CH = 0.0
        var CoefficientL = 0.0
        var CoefficientH = 0.0
        var ALtangent = 0.0
        var BLtangent = 0.0
        var CLtangent = 0.0
        var AHtangent = 0.0
        var BHtangent = 0.0
        var CHtangent = 0.0
        var ANormalLine = 0.0
        var BNormalLine = 0.0
        var CNormalLine = 0.0
        var OrientationValue = 0.0
        var AngleCos = 0.0
        var Sum1 = 0.0
        var Sum2 = 0.0
        var Count2 = 0.0
        var Count1 = 0.0
        var Sum = 0.0
        val Radius = 6378137.0// WGS84椭球半径
        val Count = pts.size
        //最少3个点
        if (Count < 3) {
            return ""
        }
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
            AngleCos = (AHtangent * ALtangent + BHtangent * BLtangent + CHtangent * CLtangent) / (Math.sqrt(AHtangent * AHtangent + BHtangent * BHtangent

                    + CHtangent * CHtangent) * Math.sqrt(ALtangent * ALtangent + BLtangent * BLtangent + CLtangent * CLtangent))
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

        val tempSum1: Double
        val tempSum2: Double
        tempSum1 = Sum1 + (2.0 * Math.PI * Count2 - Sum2)
        tempSum2 = 2.0 * Math.PI * Count1 - Sum1 + Sum2
        Sum = when {
            Sum1 > Sum2 -> if (tempSum1 - (Count - 2) * Math.PI < 1)
                tempSum1.absoluteValue
            else
                tempSum2.absoluteValue
            else -> if (tempSum2 - (Count - 2) * Math.PI < 1)
                tempSum2.absoluteValue
            else
                tempSum1.absoluteValue
        }
        totalArea = (Sum - (Count - 2) * Math.PI) * Radius * Radius

        return Math.floor(totalArea).toString() // 返回总面积
    }
}
