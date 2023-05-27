package mvc

import JSONArray
import JSONArrayObserver
import JSONElement
import java.awt.GridLayout
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JTextArea

class JSONViewerView(private val frame: JFrame, private val jsonSource: JSONArray) {

    private var panel = JPanel()

    init {

        panel.layout = GridLayout()

        val srcArea = JTextArea()
        srcArea.tabSize = 2
        srcArea.text = "TODO"
        panel.add(srcArea)


        jsonSource.addObserver(object: JSONArrayObserver {
            override fun elementAdded(value: JSONElement) {
                srcArea.text = jsonSource.toString()
            }

            override fun elementRemoved(index: Int) {
                srcArea.text = jsonSource.toString()
            }

            override fun elementReplaced(index: Int, new: JSONElement) {
                srcArea.text = jsonSource.toString()
            }
        })
    }

    fun getPanel() = panel

}

