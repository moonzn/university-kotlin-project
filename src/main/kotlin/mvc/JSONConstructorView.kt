package mvc

import JSONArray
import JSONArrayObserver
import JSONBool
import JSONElement
import JSONGenerator
import JSONNull
import JSONObject
import JSONObjectObserver
import JSONString
import JSONStructure
import java.awt.*
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*

class JSONElementPanel(private val frame: JFrame, private var jsonElement: JSONElement, private val jsonElementParent: JSONStructure) {

    private var thisPanel = JPanel()
    private var index = 0

    init {

        setPanelSettings()
        addObservers()

        val label = JLabel()
        label.addMouseListener(object: MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                val textField = JTextField("");
                textField.addFocusListener(object : FocusAdapter() {

                    override fun focusLost(e: FocusEvent) {

                        val new = getJSONElement(textField.text)

                        if (jsonElementParent is JSONArray){
                            jsonElementParent.replaceElement(index, new)
                        }
                        else if (jsonElementParent is JSONObject) {
                            val keys = jsonElementParent.getChildren().keys
                            val key = keys.elementAt(index)
                            jsonElementParent.replaceElement(key, new)
                        }
                    }
                })
                thisPanel.remove(label)
                thisPanel.add(textField)
                thisPanel.revalidate()
                thisPanel.repaint()
            }
        })

        when(jsonElementParent::class) {
            JSONArray::class -> {
                index = (jsonElementParent as JSONArray).getChildren().size - 1
                if (index > -1) {
                    label.text = "$index N/A"
                    thisPanel.add(label)
                }
                attachButtons()
            }
            JSONObject::class -> {
                index = (jsonElementParent as JSONObject).getChildren().size - 1
                if (index > -1) {
                    label.text = "$index key: N/A"
                    thisPanel.add(label)
                }
                attachButtons()
            }
        }

        frame.repaint()
    }

    fun getPanel() = thisPanel

    private fun attachButtons(){
        if (jsonElement is JSONStructure){

            val buttonsPanel = JPanel(FlowLayout(FlowLayout.RIGHT))
            val addBtn = JButton("+")
            val removeBtn = JButton("-")
            buttonsPanel.add(addBtn)
            buttonsPanel.add(removeBtn)

            addBtn.addActionListener {
                when(jsonElement::class) {
                    JSONArray::class -> (jsonElement as JSONArray).addElement(JSONNull)
                    JSONObject::class -> (jsonElement as JSONObject).addElement("key", JSONNull)
                }
                frame.repaint()
            }
            thisPanel.add(buttonsPanel, BorderLayout.PAGE_START)
        }
    }

    private fun setPanelSettings(){
        thisPanel.layout = BoxLayout(thisPanel, BoxLayout.Y_AXIS)
        thisPanel.alignmentX = Component.LEFT_ALIGNMENT
        thisPanel.alignmentY = Component.BOTTOM_ALIGNMENT
    }

    private fun addObservers() {
        (jsonElement as? JSONArray)?.addObserver(object: JSONArrayObserver {
            override fun elementAdded(value: JSONElement) {
                thisPanel.add(JSONElementPanel(frame, value, jsonElement as JSONArray).getPanel())
            }

            override fun elementRemoved(index: Int) {
            }

            override fun elementReplaced(index: Int, new: JSONElement) {
                jsonElement = new
                println("here")
            }
        })

        (jsonElement as? JSONObject)?.addObserver(object: JSONObjectObserver {
            override fun elementAdded(value: JSONElement) {
                thisPanel.add(JSONElementPanel(frame, value, jsonElement as JSONArray).getPanel())
            }

            override fun elementRemoved(key: String) {
            }

            override fun elementReplaced(key: String, new: JSONElement) {
                jsonElement = new
            }

        })
    }

    private fun getJSONElement(text: String): JSONElement {

        if(text.contains(":")){
            val obj = JSONObject()
            obj.addElement(text.split(":")[0], JSONString(text.split(":")[1]))
            return obj
        }

        return when(text){
            "true" -> JSONBool(true)
            "false" -> JSONBool(false)
            "null" -> JSONNull
            else -> JSONString(text)
        }
    }

}


class JSONConstructorView(private val frame: JFrame, private val jsonSource: JSONArray) {

    private var panel = JPanel()
    private val associations : MutableMap<JPanel, Int> = mutableMapOf()

    init {
        panel = JSONElementPanel(frame, jsonSource, jsonSource).getPanel()
    }

    fun getPanel() = panel

}