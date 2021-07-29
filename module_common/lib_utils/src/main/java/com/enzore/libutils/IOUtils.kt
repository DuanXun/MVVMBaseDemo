package com.enzore.libutils

import java.io.ByteArrayOutputStream
import java.io.Closeable
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

const val BUFFER_SIZE = 0x400 // 1024

fun Closeable?.closeQuietly(){
    try {
        this?.close()
    }catch (e: IOException){
        e.printStackTrace()
    }
}


object IOUtils {

    /**
     * 从输入流读取数据
     * @param inStream
     * @return
     * @throws Exception
     */
    @Throws(IOException::class)
    fun readInputStream(inStream: InputStream): ByteArray? {
        val outSteam = ByteArrayOutputStream()
        val buffer = ByteArray(BUFFER_SIZE)
        var len = 0
        while (inStream.read(buffer).also { len = it } != -1) {
            if (len != 0) {
                outSteam.write(buffer, 0, len)
            }
        }
        outSteam.close()
        inStream.close()
        return outSteam.toByteArray()
    }

}