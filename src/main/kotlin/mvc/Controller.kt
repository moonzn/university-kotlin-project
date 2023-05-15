package mvc

import JSONArray
import java.awt.Dimension
import java.awt.GridLayout
import javax.swing.JFrame

fun main() {

    val jsonSource = JSONArray()

    val frame = JFrame("JSON Object Editor").apply {
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        layout = GridLayout(0, 2)
        size = Dimension(600, 600)

        val leftView = JSONConstructorView(this, jsonSource)
        val rightView = JSONViewerView(this, jsonSource)

        add(leftView.getPanel())
        add(rightView.getPanel())
    }

    frame.isVisible = true

}