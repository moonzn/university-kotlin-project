package mvc

import JSONStructure
import java.awt.Dimension
import java.awt.GridLayout
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea

class JSONEditor(private val jsonSource: JSONStructure) {

    private val frame = JFrame("JSON Editor - Programação Avançada 2022/2023 | Docente: André Santos | Realizado por: Constança Silva e Raúl Nascimento | ISCTE-IUL |")
    private val srcArea = JTextArea()

    init {

        frame.apply {
            defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            layout = GridLayout(0, 2)
            size = Dimension(1000, 700)

            //Initialize left panel
            val leftPanel = JPanel()
            leftPanel.layout = GridLayout()
            val parsedPanel = JSONSourceParser(srcArea, jsonSource).parse(jsonSource = jsonSource).jPanel
            val scrollPane = JScrollPane(parsedPanel).apply {
                horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
                verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
            }
            leftPanel.add(scrollPane)

            //Initialize right panel
            val rightPanel = JPanel()
            rightPanel.layout = GridLayout()
            srcArea.tabSize = 2
            srcArea.text = jsonSource.toString()
            srcArea.isEditable = false
            rightPanel.add(srcArea)

            //Add panels
            add(leftPanel)
            add(rightPanel)

            //Show Editor
            isVisible = true
        }

    }
}