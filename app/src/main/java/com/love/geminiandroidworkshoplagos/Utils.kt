package com.love.geminiandroidworkshoplagos

import android.net.Uri
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope


class UriSaver : Saver<MutableList<Uri>, List<String>> {
    override fun restore(value: List<String>): MutableList<Uri> = value.map {
        Uri.parse(it)
    }.toMutableList()

    override fun SaverScope.save(value: MutableList<Uri>): List<String> = value.map { it.toString() }
}
