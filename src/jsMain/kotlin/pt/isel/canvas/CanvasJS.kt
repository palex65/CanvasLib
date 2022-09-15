package pt.isel.canvas

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.*
import kotlin.js.Date
import kotlin.math.PI

private val all = mutableListOf<Canvas>()
private var closing = false

actual fun onStart(fx: () -> Unit) {
    window.onclose = { closeAll() }
    window.onload = { fx() }
}

private fun closeAll() {
    closing = true
    all.forEach { it.close() }
    all.clear()
}

actual fun onFinish(fx: () -> Unit) {
    window.onclose = {
        fx()
        closeAll()
    }
}

actual class Canvas actual constructor(
    actual val width: Int, actual val height: Int, actual val background: Int,
) {
    private val context: CanvasRenderingContext2D
    init {
        val canvas = document.createElement("canvas") as HTMLCanvasElement
        canvas.width = width
        canvas.height = height
        canvas.setAttribute("class", "canvas")
        document.body?.appendChild(canvas)
        context = canvas.getContext("2d") as CanvasRenderingContext2D
        all.add(this)
        context.font = "32px verdana"
        erase()
    }

    private fun Int.toRGB() : String {
        val str = toString(16)
        return "#"+"0".repeat(6-str.length)+str
    }
    actual fun erase() {
        drawRect(0,0,width,height,background)
    }
    actual fun drawCircle(xCenter: Int, yCenter: Int, radius: Int, color: Int, thickness: Int) {
        drawArc(xCenter,yCenter,radius,0,360,color,thickness)
    }
    actual fun drawArc(xCenter: Int, yCenter: Int, radius: Int, startAng: Int, endAng: Int, color: Int, thickness: Int) {
        fun Int.toRadian() = 2*PI + (-2*this) * PI / 360
        val eAng = startAng.toRadian()
        val sAng = endAng.toRadian()
        val x = xCenter.toDouble()
        val y = yCenter.toDouble()
        val r = radius.toDouble()
        if (thickness==0) {
            context.fillStyle = color.toRGB()
            context.beginPath()
            context.moveTo(x,y)
            context.arc(x, y, r, sAng, eAng)
            context.closePath()
            context.fill()
        } else {
            context.strokeStyle = color.toRGB()
            context.lineWidth = thickness.toDouble()
            context.beginPath()
            context.arc(x, y, r, sAng, eAng)
            context.stroke()
        }
    }
    actual fun drawRect(x: Int, y: Int, width: Int, height: Int, color: Int, thickness: Int) {
        if (thickness==0) {
            context.fillStyle = color.toRGB()
            context.fillRect(x.toDouble(),y.toDouble(), width.toDouble(),height.toDouble())
        } else {
            context.beginPath()
            context.lineWidth = thickness.toDouble()
            context.strokeStyle = color.toRGB()
            context.moveTo(x.toDouble(),y.toDouble())
            context.lineTo((x+width).toDouble(),y.toDouble())
            context.lineTo((x+width).toDouble(),(y+height).toDouble())
            context.lineTo(x.toDouble(),(y+height).toDouble())
            context.lineTo(x.toDouble(),y.toDouble())
            context.stroke()
        }
    }
    actual fun drawText(x: Int, y: Int, txt: String, color: Int, fontSize: Int?) {
        context.fillStyle = color.toRGB()
        if (fontSize!=null)
            context.font = "${fontSize}px verdana"
        context.textAlign = CanvasTextAlign.LEFT
        context.fillText(txt, x.toDouble(), y.toDouble(), 400.0)
    }
    actual fun drawLine(xFrom: Int, yFrom: Int, xTo: Int, yTo: Int, color: Int, thickness: Int) {
        context.beginPath()
        context.lineWidth = thickness.toDouble()
        context.strokeStyle = color.toRGB()
        context.moveTo(xFrom.toDouble(),yFrom.toDouble())
        context.lineTo(xTo.toDouble(),yTo.toDouble())
        context.stroke()
    }
    actual fun drawImage(fileName :String, xLeft: Int, yTop: Int, width: Int, height: Int) {
        //Do nothing in Browser
    }


    private var lastMouse: MouseEvent? = null
    actual val mouse: MouseEvent
    get() {
        if ( context.canvas.onmousedown == null ) onMouseDown {  }
        if ( context.canvas.onmousemove == null ) onMouseMove {  }
        context.canvas.onmouseup = {
            lastMouse =  MouseEvent(it.offsetX.toInt(), it.offsetY.toInt(), false)
            true
        }
        return lastMouse ?: MouseEvent(0,0,false)
    }
    actual fun onMouseDown(handler: (MouseEvent) -> Unit) {
        context.canvas.onmousedown = {
            val m = MouseEvent(it.offsetX.toInt(), it.offsetY.toInt(), true)
            lastMouse = m
            handler(m)
        }
    }

    actual fun onMouseMove(handler: (MouseEvent) -> Unit) {
        context.canvas.onmousemove = {
            val m = MouseEvent(it.offsetX.toInt(), it.offsetY.toInt(), it.buttons == 1.toShort())
            lastMouse = m
            handler(m)
        }
    }

    actual fun onKeyPressed(handler: ((KeyEvent) -> Unit)?) {
        window.onkeydown = if (handler==null) null
        else { ke ->
            val c = when {
                ke.key.length == 1 -> ke.key[0]
                ke.keyCode == 27 -> ESCAPE
                else -> UNDEFINED_CHAR
            }
            handler(KeyEvent(c, ke.keyCode, ke.key))
            /*
            println("charCode=|${ke.charCode.toChar()}|${ke.charCode}")
            println("code=|${ke.code}|")
            println("key=|${ke.key}|")
            println("keyCode=|${ke.keyCode.toChar()}|${ke.keyCode.toInt()}")
            println("which=|${ke.which.toChar()}|${ke.which.toInt()}")
            println("isComposing=|${ke.isComposing}|")
            println("repeat=|${ke.repeat}|")
            */
        }
    }

    private val timers = mutableListOf<Int>()
    actual fun onTimeProgress(period: Int, handler: (Long) -> Unit) : TimerCtrl {
        val startTm = Date().getTime()
        val timer = window.setInterval( {
            val tm = Date().getTime()-startTm
            handler(tm.toLong())
        }, period )
        timers.add(timer)
        return TimerCtrl(timers, timer)
    }
    actual fun onTime(delay: Int, handler: () -> Unit) {
        window.setTimeout( { handler() }, delay )
    }

    actual fun close() {
        if (all.contains(this)) {
            timers.forEach { window.clearInterval(it) }
            if (!closing) {
                all.remove(this)
                context.canvas.remove()
                if (all.isEmpty()) window.close()
            }
        }
    }
}

actual class TimerCtrl(private val tms :MutableList<Int>, private val tm :Int) {
    actual fun stop() {
        window.clearInterval(tm)
        tms.remove(tm)
    }
}

private val sounds = mutableMapOf<String, Audio>()

private fun String.toSoundName() = if (lastIndexOf('.')>0) this else "$this.wav"

actual fun loadSounds(vararg names: String) {
    for(s in names) {
        val fileName = s.toSoundName()
        sounds[fileName] = Audio(fileName)
    }
}

actual fun playSound(sound: String) {
    val fileName = sound.toSoundName()
    val clip =
        if (sounds.contains(fileName)) sounds[fileName]
        else {
            val clp = Audio(fileName)
            sounds[fileName] = clp
            clp
        }
    clip?.play()
}
