package pt.isel.canvas

actual fun onStart(fx: () -> Unit) { }

actual fun onFinish(fx: () -> Unit) { }

actual class Canvas actual constructor(
    actual val width: Int, actual val height: Int, actual val background: Int,
) {
    actual fun erase() {
        drawRect(0,0,width,height,background)
    }
    actual fun drawCircle(xCenter: Int, yCenter: Int, radius: Int, color: Int, thickness: Int) {
        drawArc(xCenter,yCenter,radius,0,360,color,thickness)
    }
    actual fun drawArc(xCenter: Int, yCenter: Int, radius: Int, startAng: Int, endAng: Int, color: Int, thickness: Int) { }
    actual fun drawRect(x: Int, y: Int, width: Int, height: Int, color: Int, thickness: Int) { }
    actual fun drawText(x: Int, y: Int, txt: String, color: Int, fontSize: Int?) { }
    actual fun drawLine(xFrom: Int, yFrom: Int, xTo: Int, yTo: Int, color: Int, thickness: Int) { }
    actual fun drawImage(fileName :String, xLeft: Int, yTop: Int, width: Int, height: Int) { }

    actual val mouse: MouseEvent
        get() = MouseEvent(0,0,false)
    actual fun onMouseDown(handler: (MouseEvent) -> Unit) { }

    actual fun onMouseMove(handler: (MouseEvent) -> Unit) { }

    actual fun onKeyPressed(handler: ((KeyEvent) -> Unit)?) { }

    actual fun onTimeProgress(period: Int, handler: (Long) -> Unit) : TimerCtrl =
        TimerCtrl(mutableListOf(),0)
    actual fun onTime(delay: Int, handler: () -> Unit) { }
    actual fun close() { }
}

actual class TimerCtrl(private val tms :MutableList<Int>, private val tm :Int) {
    actual fun stop() { }
}

actual fun loadSounds(vararg names: String) { }

actual fun playSound(sound: String) { }
