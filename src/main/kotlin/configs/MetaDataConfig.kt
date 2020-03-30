package dev.alpas.alpasdev.configs

import dev.alpas.Config
import dev.alpas.http.HttpCall
import dev.alpas.http.HttpCallHook


@Suppress("unused")
class MetaDataConfig : HttpCallHook {

    var canonicalUrl = getCanonical()

    fun getCanonical(call: HttpCall): String {
        return call.url
    }

}





