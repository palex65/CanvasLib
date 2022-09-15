import kotlin.test.*
import pt.isel.canvas.*

class MainTest {
    @Test
    fun main() {
        var running = true
        onStart {
            val cv = Canvas(600, 400, background = 0x00FFFF)
            cv.onMouseDown { mouse ->
                cv.drawCircle(mouse.x, mouse.y, 10, color = RED, thickness = 5)
                playSound("click")
            }
            cv.onMouseMove { mouse ->
                if (mouse.down) cv.drawCircle(mouse.x, mouse.y, 1, BLUE, 1)
            }
            cv.onKeyPressed { key ->
                when (key.char) {
                    ' ' -> cv.erase()
                    ESCAPE -> cv.close()
                }
                println("Key: '${key.char}'=${key.code} ${key.text}")
            }
            val tmCtrl = cv.onTimeProgress(1000) { tm ->
                //cv.drawRect(0, 0, 30, 25, color = 0xAAAAAA)
                //cv.drawText(5, 17, (tm / 1000).toString(), fontSize = 18)
                cv.drawRect(0, 0, 150, 150, color = 0xAAAAAA)
                cv.drawText(5, 100, (tm / 1000).toString(), fontSize = 64)
                println(cv.mouse)
            }
            cv.drawRect(50, 50, cv.width - 100, cv.height - 100, thickness = 10)
            cv.drawLine(0, 0, cv.width, cv.height)
            cv.drawLine(0, cv.height, cv.width, 0)
            cv.drawCircle(0, 0, 300, 0x7777FF, 10)
            cv.drawArc(cv.width / 2, cv.height / 2, 100, 0, 90, 0x7777FF)
            cv.drawArc(cv.width / 2, cv.height / 2, 50, 90, 270, 0xFF7777, 5)
            cv.onTime(10500) { tmCtrl.stop(); cv.erase() }
            cv.drawImage("man", 200, 200, 100, 100)
            cv.drawImage("bulbOffOn|0,0,400,604", 300, 200, 40, 60)
            cv.drawImage("bulbOffOn|400,0,400,604", 400, 300, 40, 60)
            cv.onTime(12000) { running = false }
        }
        onFinish {
            running = false
        }
        while (running) {
            Thread.sleep(1000)
        }
    }
}