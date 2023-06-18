package mvc

import JSONArray
import JSONArrayObserver
import JSONBool
import JSONDouble
import JSONElement
import JSONInt
import JSONNull
import JSONObject
import JSONObjectEntry
import JSONObjectObserver
import JSONString
import JSONValue
import java.awt.Color
import java.awt.Component
import java.awt.GridLayout
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*

class JSONSourceParser(private val srcArea: JTextArea, private val jsonSource: JSONElement) {

    private val panelMatches: MutableMap<JComponent, JSONPanel> = mutableMapOf()

    private val rootPanel = JSONPanel(JPanel().apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        alignmentX = Component.LEFT_ALIGNMENT
        alignmentY = Component.TOP_ALIGNMENT
        border = BorderFactory.createEmptyBorder(7, 7, 7, 7)
    })

    private fun showMenu(): MouseAdapter {
        return object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    val menu = JPopupMenu()

                    val add = JButton("Add")
                    add.addActionListener {
                        menu.isVisible = false

                        val addParent = panelMatches[(e?.component as JPanel)]?.jsonParent
                        add(addParent)
                    }

                    var index = 0
                    val del = JMenu("Delete")
                    (e?.component as JPanel).components.forEach {
                        val menuItem = JMenuItem(it.name)
                        val arrayIndex = index

                        menuItem.addActionListener {
                            val delParent = panelMatches[e.component]?.jsonParent
                            remove(delParent, arrayIndex, menuItem)
                        }
                        del.add(menuItem)
                        index += 1
                    }

                    menu.add(add)
                    menu.add(del)
                    menu.show(e.component, e.x, e.y)
                }
            }
        }
    }

    fun add(addParent: JSONElement?) {
        val key = JTextField(10)
        val value = JTextField(10)

        if (addParent is JSONObject) {
            val panel = JPanel().apply {
                add(JLabel("Key:"))
                add(key)
                add(Box.createHorizontalStrut(15)) // a spacer
                add(JLabel("Value:"))
                add(value)
            }

            val result = JOptionPane.showConfirmDialog(null, panel,
                "Enter the key, value pair", JOptionPane.OK_CANCEL_OPTION)

            if (result == JOptionPane.OK_OPTION) {

                val jsonInput = getJSONElement(value.text)
                addObservers(jsonInput)

                addParent.addElement(key.text, jsonInput)
                reparse()
            }

        } else if (addParent is JSONArray) {
            val panel = JPanel().apply {
                add(JLabel("Value:"))
                add(value)
            }

            val result = JOptionPane.showConfirmDialog(null, panel,
                "Enter the value", JOptionPane.OK_CANCEL_OPTION)

            if (result == JOptionPane.OK_OPTION) {

                val jsonInput = getJSONElement(value.text)
                addObservers(jsonInput)

                addParent.addElement(jsonInput)
                reparse()
            }
        }
    }

    fun remove(delParent: JSONElement?, arrayIndex: Int, menuItem: JMenuItem) {
        if (delParent is JSONObject)
            delParent.deleteElement(menuItem.text)
        else
            (delParent as JSONArray).deleteElement(arrayIndex)

        reparse()
    }

    fun replace() {
        reparse()
    }

    private fun addObservers(jsonElement: JSONElement){
        (jsonElement as? JSONArray)?.addObserver(object: JSONArrayObserver {
            override fun elementAdded(value: JSONElement) {
                srcArea.text = jsonSource.toString()
            }

            override fun elementRemoved(index: Int) {
                srcArea.text = jsonSource.toString()
            }

            override fun elementReplaced(index: Int, new: JSONElement) {
                srcArea.text = jsonSource.toString()
                replace()
            }
        })

        (jsonElement as? JSONObject)?.addObserver(object: JSONObjectObserver {
            override fun elementAdded(value: JSONElement) {
                srcArea.text = jsonSource.toString()
            }

            override fun elementRemoved(key: String) {
                srcArea.text = jsonSource.toString()
            }

            override fun elementReplaced(key: String, new: JSONElement) {
                srcArea.text = jsonSource.toString()
                replace()
            }
        })
    }

    fun parse(parent: JSONPanel = rootPanel, jsonSource: JSONElement, firstRun: Boolean = true): JSONPanel {

        if (jsonSource is JSONValue) {
            parent.jPanel.apply {
                add(JTextField().apply {
                    text = jsonSource.toString()
                    val arrayTextfield = this
                    addFocusListener(object : FocusAdapter() {
                        override fun focusLost(e: FocusEvent) {  // Modify element
                            (parent.jsonParent as JSONArray).replaceElement(parent.jPanel.components.indexOf(e.source), getJSONElement(arrayTextfield.text))
                        }
                    })
                }).name = jsonSource.toString()
            }
        }

        if (jsonSource is JSONArray) {

            if (firstRun)
                addObservers(jsonSource)

            val newParent = JSONPanel(JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                border = BorderFactory.createLineBorder(Color.darkGray, 10)
            }, parent, jsonSource)

            panelMatches[newParent.jPanel] = newParent

            newParent.jPanel.addMouseListener(showMenu())

            jsonSource.getChildren().forEach() {
                newParent.apply {
                    newParent.children.add(parse(newParent, it))
                }

                parent.jPanel.apply {
                    add(newParent.jPanel)
                }
            }
        }

        if (jsonSource is JSONObject) {

            if (firstRun)
                addObservers(jsonSource)

            val newParent = JSONPanel(JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                border = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 10)
            }, parent, jsonSource)

            panelMatches[newParent.jPanel] = newParent

            newParent.jPanel.addMouseListener(showMenu())

            jsonSource.getChildren().forEach() {
                newParent.apply {
                    newParent.children.add(parse(newParent, JSONObjectEntry(it)))
                }

                parent.jPanel.apply {
                    add(newParent.jPanel).name = "JSONObject"
                }
            }
        }

        if (jsonSource is JSONObjectEntry) {

            if (jsonSource.entry.value is JSONValue) {
                parent.jPanel.apply {
                    add(JPanel().apply {
                        layout = GridLayout(1, 2)

                        add(JLabel(jsonSource.entry.key))

                        add(JTextField((jsonSource.entry.value as JSONValue).toString()).apply {
                            val objectTextfield = this
                            addFocusListener(object : FocusAdapter() {
                                override fun focusLost(e: FocusEvent) {
                                    (parent.jsonParent as JSONObject).replaceElement(jsonSource.entry.key, getJSONElement(objectTextfield.text))
                                }
                            })
                        })
                    }).name = jsonSource.entry.key
                }

            } else {
                parent.jPanel.apply {

                    add(JPanel().apply {
                        layout = GridLayout(1, 2)

                        add(JLabel(jsonSource.entry.key))

                        val newParent = JSONPanel(JPanel().apply {
                            layout = BoxLayout(this, BoxLayout.Y_AXIS)
                            alignmentX = Component.LEFT_ALIGNMENT
                            alignmentY = Component.TOP_ALIGNMENT
                        }, parent)

                        parse(newParent, jsonSource.entry.value)
                        add(newParent.jPanel)
                    }).name = jsonSource.entry.key
                }
            }
        }
        return rootPanel
    }

    private fun reparse() {
        rootPanel.jPanel.removeAll()
        parse(jsonSource = jsonSource, firstRun = false)
        rootPanel.jPanel.revalidate()
        rootPanel.jPanel.repaint()
    }

    private fun getJSONElement(text: String): JSONElement {
        if (text.toIntOrNull() != null) {
            return JSONInt(text.toIntOrNull()!!)
        }

        if (text.toDoubleOrNull() != null) {
            return JSONDouble(text.toDoubleOrNull()!!)
        }

        return when (text) {
            "true" -> JSONBool(true)
            "false" -> JSONBool(false)
            "null" -> JSONNull
            "__Array" -> {
                val array = JSONArray()
                array.addElement(JSONNull)
                array
            }
            "__Object" -> {
                val obj = JSONObject()
                obj.addElement("placeholder", JSONString(""))
                obj
            }
            else -> JSONString(text)
        }
    }
}

data class JSONPanel(
    val panel: JPanel,
    val parent: JSONPanel? = null,
    val jsonParent: JSONElement? = null,
    internal val children: MutableList<JSONPanel> = mutableListOf())
{
    val jPanel: JPanel
        get() = panel
}