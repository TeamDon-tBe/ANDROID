package com.teamdontbe.data.repositoryimpl.utils

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore
import androidx.exifinterface.media.ExifInterface
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.ByteArrayOutputStream

class ContentUriRequestBody(
    private val contentResolver: ContentResolver,
    private val uri: Uri,
) : RequestBody() {

    private var fileName = ""
    private var size = -1L
    private var compressedImage: ByteArray? = null

    init {
        contentResolver.query(
            uri,
            arrayOf(MediaStore.Images.Media.SIZE, MediaStore.Images.Media.DISPLAY_NAME),
            null,
            null,
            null,
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                size =
                    cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE))
                fileName =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
            }
        }

        compressBitmap() // 이미지 압축 수행
    }

    private fun compressBitmap() {
        var originalBitmap: Bitmap? = null
        val exif: ExifInterface

        contentResolver.openInputStream(uri).use { inputStream ->
            if (inputStream == null) return
            exif = ExifInterface(inputStream)
        }

        contentResolver.openInputStream(uri).use { inputStream ->
            if (inputStream == null) return
            val option = BitmapFactory.Options().apply {
                inSampleSize = calculateInSampleSize(this, MAX_WIDTH, MAX_HEIGHT)
            }
            originalBitmap = BitmapFactory.decodeStream(inputStream, null, option)
        }

        originalBitmap?.let {
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            val rotatedBitmap = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(it, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(it, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(it, 270f)
                else -> it
            }

            // 회전된 이미지를 사용하도록 originalBitmap을 rotatedBitmap으로 변경
            originalBitmap = rotatedBitmap

            // 압축 로직 추가
            val outputStream = ByteArrayOutputStream()
            var quality = 100

            do {
                outputStream.reset()
                originalBitmap?.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                quality = (quality * 0.9).toInt() // 퀄리티를 10% 감소
            } while (outputStream.toByteArray().size > 3 * 1024 * 1024)

            compressedImage = outputStream.toByteArray()
            size = compressedImage?.size?.toLong() ?: -1L
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix().apply {
            postRotate(degrees)
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    private fun getFileName() = fileName

    override fun contentLength(): Long = size

    override fun contentType(): MediaType? =
        uri.let { contentResolver.getType(it)?.toMediaTypeOrNull() }

    override fun writeTo(sink: BufferedSink) {
        compressedImage?.let(sink::write)
    }

    fun toFormData(name: String) = MultipartBody.Part.createFormData(name, getFileName(), this)

    companion object {
        const val MAX_WIDTH = 1024 // 이미지 최대 너비
        const val MAX_HEIGHT = 1024 // 이미지 최대 높이
    }
}
