package net.recraft.annihilatoin.check

class ProcessingTime {
    var startTime = System.currentTimeMillis()
    fun fin() {
        println("${System.currentTimeMillis() - startTime}ms 時間かかりました")
        var startTime = 0L
    }
}