package com.dadachen.magicwear.utils

import java.io.File
import java.io.RandomAccessFile

const val filePath = "/storage/emulated/0/watch_data.csv"

fun writeToLocalStorage(content:String){

    val file = File(filePath)
    if (!file.exists()){
        file.createNewFile()
    }
//    Log.d("csv files:", content)

    val raf = RandomAccessFile(file,"rwd")
    raf.seek((file.length()))
    raf.write(content.toByteArray())
    raf.close()
}