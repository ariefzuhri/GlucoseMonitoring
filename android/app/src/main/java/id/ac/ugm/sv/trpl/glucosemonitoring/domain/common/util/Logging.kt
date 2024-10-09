package id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util

import android.util.Log

fun log(message: Any?) {
    val trace = Thread.currentThread().stackTrace[3]
    val fileName = trace.fileName
    val classPath = trace.className
    val className = classPath.substring(classPath.lastIndexOf(".") + 1)
    val methodName = trace.methodName
    val lineNumber = trace.lineNumber
    
    Log.d("Logging", "$className.$methodName($fileName:$lineNumber) = $message")
}