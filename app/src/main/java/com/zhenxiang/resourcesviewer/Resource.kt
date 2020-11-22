package com.zhenxiang.resourcesviewer

class Resource {
    var resId : Int
    var resType : String
    var packageName : String

    constructor(resId : Int, resType : String, packageName : String) {
        this.resId = resId
        this.resType = resType
        this.packageName = packageName
    }
}