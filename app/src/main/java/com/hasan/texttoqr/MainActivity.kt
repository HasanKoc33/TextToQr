package com.hasan.texttoqr.MainActivity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.print.PrintHelper
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.hasan.texttoqr.R
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class MainActivity() : AppCompatActivity() {
    private var yazdir: Button? = null
    private var olustur: Button? = null
    private var yazi: EditText? = null
    private var qr: ImageView? = null
    var t: TextView? = null
    private val context: Context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        allahin_izni_ile(this)
        yazdir = findViewById(R.id.yaz)
        olustur = findViewById(R.id.creat)
        yazi = findViewById(R.id.str)
        qr = findViewById(R.id.qr)
        t = findViewById(R.id.bitmap)
        val s = BitMapToString(
            BitmapFactory.decodeResource(
                context.resources,
                R.drawable.s
            )
        )
        olustur?.setOnClickListener(View.OnClickListener {
            val y = yazi?.text.toString()
            QR(y)
        })
    }

    fun BitMapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun StringToBitMap(encodedString: String?): Bitmap? {
        try {
            val encodeByte =
                Base64.decode(encodedString, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        } catch (e: Exception) {
            e.message
            return null
        }
    }

    fun QR(s: String) {
        val multiFormatWriter = MultiFormatWriter()
        try {
            val bitMatrix: BitMatrix = multiFormatWriter.encode(s, BarcodeFormat.QR_CODE, 200, 200)
            val barcodeEncoder = BarcodeEncoder()
            val bitmap: Bitmap = barcodeEncoder.createBitmap(bitMatrix)
            qr!!.setImageBitmap(bitmap)
            yazdir!!.setOnClickListener {
                val photoPrinter = PrintHelper(context)
                photoPrinter.scaleMode = PrintHelper.SCALE_MODE_FIT
                photoPrinter.printBitmap("droids.jpg - test print", bitmap)
            }
            /*val root = Environment.getExternalStorageDirectory()
            val f = File(
                root.toString() + File.separator +
                        "/QR/"
            )
            if (!f.exists()) {
                f.mkdirs()
            }
            val cachePath = File(root.absolutePath + "/QR/" + s + ".jpg")
            try {
                allahin_izni_ile(this)
                cachePath.createNewFile()
                val ostream = FileOutputStream(cachePath)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream)
                ostream.flush()
                ostream.close()
                Toast.makeText(applicationContext, "kaydedildi", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
                yazi!!.setText(e.toString())
                Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show()
            }*/
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private val REQUEST_EXTERNAL_STORAGE = 1
        private val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        fun allahin_izni_ile(activity: Activity?) {
            // Check if we have write permission
            val permission = ActivityCompat.checkSelfPermission(
                (activity)!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                    (activity),
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
                )
            }
        }
    }
}