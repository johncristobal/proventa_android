package com.pro.venta.ws.singleton

data class settings (
    var autoPlayVideo: Boolean = true,
    var wifiDownload : Boolean = true,
    var qualityVideoDownload : quality = quality.alta
)
