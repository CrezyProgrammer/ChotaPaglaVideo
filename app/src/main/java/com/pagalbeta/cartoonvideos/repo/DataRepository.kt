package com.pagalbeta.cartoonvideos.repo

import com.pagalbeta.cartoonvideos.AppDB
import com.pagalbeta.cartoonvideos.entity.Video
import java.util.concurrent.Executors
import javax.inject.Inject

class DataRepository @Inject constructor(appDB: AppDB) {

    private val wordDao = appDB.pigeonDao()

    private val executor = Executors.newSingleThreadExecutor()

    fun insert(word: Video) {
        executor.execute {
            wordDao.insert(word)
        }
    }

    fun getAllVideo(b:String) = wordDao.getAllVideo(b)

}