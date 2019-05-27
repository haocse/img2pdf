package com.haotran.img2pdf

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log

import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Image
import com.itextpdf.text.PageSize
import com.itextpdf.text.pdf.PdfWriter

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    internal var outputFile = "output.pdf"
    internal lateinit var theOutput: File
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            theOutput = img2pdf()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Convert to base 64
        //        String base64 = pdf2base64(theOutput);
        val base64 = getBase64FromPath2(theOutput)
        Log.d(">>>", base64)

        // display for testing here
        val target = Intent(Intent.ACTION_VIEW)
        val output = File(Environment.getExternalStorageDirectory(), outputFile)
        target.setDataAndType(Uri.fromFile(output), "application/pdf")
        target.flags = Intent.FLAG_ACTIVITY_NO_HISTORY

        val intent = Intent.createChooser(target, "Open File")
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Instruct the user to install a PDF reader here, or something
        }

    }

    @Throws(IOException::class, DocumentException::class)
    private fun img2pdf(): File {
        //        File root = new File(Environment.getExternalStorageDirectory(), "0.png");

        //        File output = new File("output/", outputFile);
        val output = File(Environment.getExternalStorageDirectory(), outputFile)

        if (output.exists()) output.delete()

        val files = ArrayList<String>()
//        files.add("1.png")
//        files.add("2.jpg")
        files.add("3.png")

        val document = Document()
        PdfWriter.getInstance(document, FileOutputStream(output))
        document.open()
        for (f in files) {
            document.newPage()
            val image = Image.getInstance(File(Environment.getExternalStorageDirectory(), f).absolutePath)
            image.setAbsolutePosition(0f, 0f)
            image.borderWidth = 0f
            image.scaleAbsolute(PageSize.A4)

            document.add(image)
        }
        document.close()

        return output


    }

    companion object {

        fun getBaseDirectoryFromPathString(mPath: String, mContext: Context): File {

            val mContextWrapper = ContextWrapper(mContext)

            return mContextWrapper.getDir(mPath, Context.MODE_PRIVATE)
        }

        fun getBase64FromPath2(file: File): String {
            var base64 = ""
            try {/*from w  w w.j a v  a2 s  .  c  om*/
//                val file = File(path)
                val buffer = ByteArray(file.length().toInt() + 100)
                val length = FileInputStream(file).read(buffer)
                base64 = Base64.encodeToString(buffer, 0, length,
                        Base64.DEFAULT)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return base64
        }

        fun decodeBase64(path: String): String {
            var base64 = ""
            try {/*from w  w w.j a v  a2 s  .  c  om*/
                val file = File(path)
                val buffer = ByteArray(file.length().toInt() + 100)
                val length = FileInputStream(file).read(buffer)
                val byte = Base64.decode(buffer, Base64.DEFAULT)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return base64
        }
    }

}
