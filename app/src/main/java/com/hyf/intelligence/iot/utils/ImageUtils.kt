package com.hyf.intelligence.iot.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.text.TextUtils
import android.util.Log

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

object ImageUtils {

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
     * 适配api19及以上,根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @SuppressLint("NewApi")
    private fun getRealPathFromUriAboveApi19(context: Context, uri: Uri): String? {
        var filePath: String? = null
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            val documentId = DocumentsContract.getDocumentId(uri)
            if (isMediaDocument(uri)) { // MediaProvider
                // 使用':'分割
                val id = documentId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]

                val selection = MediaStore.Images.Media._ID + "=?"
                val selectionArgs = arrayOf(id)
                filePath = getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs)
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(documentId))
                filePath = getDataColumn(context, contentUri, null, null)
            }
        } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null)
        } else if ("file" == uri.scheme) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.path
        }
        return filePath
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

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
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

//    private fun  getUriForFile( file: File): Uri {
//        if ( file == null) {
//            throw NullPointerException()
//        }
//        //判断是否是AndroidN以及更高的版本
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果SDK版本>=24，即：Build.VERSION.SDK_INT >= 24
//            FileProvider.getUriForFile(this, applicationContext.packageName + ".provider", file)
//        } else {
//            Uri.fromFile(file)
//        }
//    }
//
//    lateinit var imageFilePath: Uri
//    /**
//     * 创建一条图片地址uri,用于保存拍照后的照片
//     *
//     * @param context
//     * @return 图片的uri
//     */
//
//    private fun createImagePathUri(): Uri {
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
//            imageFilePath = Uri.parse("")
//            showToast("请先获取写入SDCard权限")
//        } else {
//            //拍照前保存一条uri的地址
//            val status = Environment.getExternalStorageState()
//            val timeFormatter = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA)
//            val time = System.currentTimeMillis()
//            val imageName = timeFormatter.format( Date(time))
//            val parentPath = if (status == Environment.MEDIA_MOUNTED) {// 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
//                Environment.getExternalStorageDirectory().path
//            } else {
//                externalCacheDir.path
//            }
//            val fileName = parentPath + File.separator + imageName + ".jpg"
//            Log.i("account", "生成的照片输出路径0000：" + getUriForFile( File(fileName)).toString())
//            imageFilePath = getUriForFile( File(fileName))
//        }
//
//        Log.i("account", "生成的照片输出路径：$imageFilePath")
//        return imageFilePath
//    }
//private fun startCamera() {
//    val imageUriFromCamera = createImagePathUri()
//    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//    // MediaStore.EXTRA_OUTPUT参数不设置时,系统会自动生成一个uri,但是只会返回一个缩略图
//    // 返回图片在onActivityResult中通过以下代码获取
//    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriFromCamera)//注意这一行
//    startActivityForResult(intent, REQUEST_CODE_GETIMAGE_BYCAMERA)
//}

}
