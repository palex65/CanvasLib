package pt.isel.canvas

// Main color codes
const val BLACK  = 0x000000
const val WHITE  = 0xFFFFFF
const val RED    = 0xFF0000
const val GREEN  = 0x00FF00
const val BLUE   = 0x0000FF
const val YELLOW = 0xFFFF00
const val CYAN   = 0x00FFFF
const val MAGENTA= 0xFF00FF

// Some char values for KeyEvent
/**
 * Value used in the KeyEvent char property when the key does not represent a Unicode symbol.
 */
const val UNDEFINED_CHAR = 0xFFFF.toChar()
const val ESCAPE = 27.toChar()

// Some key codes for KeyEvent
const val ESCAPE_CODE = 27
const val LEFT_CODE = 37
const val UP_CODE = 38
const val RIGHT_CODE = 39
const val DOWN_CODE = 40

/**
 * Defines the [fx] function to be called when creating the window.
 */
expect fun onStart(fx: ()->Unit)

/**
 * Defines the [fx] function to be called when the window is closed.
 */
expect fun onFinish(fx: ()->Unit)

expect class Canvas(width: Int, height: Int, background: Int = WHITE) {
    val width: Int
    val height: Int
    val background: Int
    val mouse: MouseEvent

    fun erase()
    fun drawCircle(xCenter: Int, yCenter: Int, radius: Int, color: Int = BLACK, thickness: Int = 0)
    fun drawArc(xCenter: Int, yCenter: Int, radius: Int, startAng: Int, endAng: Int = 360, color: Int = BLACK, thickness: Int = 0)
    fun drawRect(x: Int, y: Int, width: Int, height: Int, color: Int = BLACK, thickness: Int = 0)
    fun drawText(x: Int, y: Int, txt: String, color: Int = BLACK, fontSize: Int? = null)
    fun drawLine(xFrom: Int, yFrom: Int, xTo: Int, yTo: Int, color: Int = BLACK, thickness: Int =3)
    fun drawImage(fileName :String, xLeft: Int, yTop: Int, width: Int=0, height: Int=0)

    fun onMouseDown(handler: (MouseEvent) -> Unit)
    fun onMouseMove(handler: (MouseEvent) -> Unit)
    fun onKeyPressed(handler: ((KeyEvent) -> Unit)?)
    fun onTimeProgress(period: Int, handler: (Long) -> Unit) : TimerCtrl
    fun onTime(delay: Int, handler: () -> Unit)

    fun close()
}

fun erase(cv: Canvas) { cv.erase() }

/**
 * Information about the key pressed.
 * The [char] represented by the key, the key [code] and the [text] description.
 * @property char Symbol represented by the key. It may be unpresentable.
 * @property code Code assigned to the key. (key code)
 * @property text Text description of the key. It may not exist.
 */
data class KeyEvent(val char: Char, val code: Int, val text: String)

/**
 * Information about the mouse. The ([x],[y]) position and state of key ([down] or up)
 * @property x coordinate of mouse position (0 .. Canvas.width)
 * @property y coordinate of mouse position (0 .. Canvas.height)
 * @property down True if mouse key (left or only) is pressed
 */
data class MouseEvent(val x: Int, val y: Int, val down: Boolean = false)

expect class TimerCtrl {
    fun stop()
}

expect fun playSound( sound: String )
expect fun loadSounds(vararg names: String)
