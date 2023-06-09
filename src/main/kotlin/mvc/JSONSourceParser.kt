package mvc

import Command
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

    private var panelMatches: MutableMap<JComponent, JSONPanel> = mutableMapOf()
    private var commands: ArrayDeque<Command> = ArrayDeque()

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
                        addJSONElement(addParent)
                    }

                    var index = 0
                    val del = JMenu("Delete")
                    (e?.component as JPanel).components.forEach {
                        val menuItem = JMenuItem(it.name)
                        val arrayIndex = index

                        menuItem.addActionListener {
                            val delParent = panelMatches[e.component]?.jsonParent
                            removeJSONElement(delParent, arrayIndex, menuItem)
                        }

                        del.add(menuItem)
                        index += 1
                    }

                    val undo = JButton("Undo")
                    undo.addActionListener {
                        menu.isVisible = false

                        val command = commands.removeLast()
                        command.undo()
                    }

                    menu.add(add)
                    menu.add(del)
                    if (commands.size != 0)
                        menu.add(undo)
                    menu.show(e.component, e.x, e.y)
                }
            }
        }
    }

    private fun addJSONElement(addParent: JSONElement?) {
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

                val addCommand = AddElementCommand(addParent, key = key.text, jsonInput)
                addCommand.execute()
                commands.addLast(addCommand)
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

                val addCommand = AddElementCommand(addParent, key = null, jsonInput)
                addCommand.execute()
                commands.addLast(addCommand)
            }
        }
    }

    fun removeJSONElement(delParent: JSONElement?, arrayIndex: Int, menuItem: JMenuItem) {
        if (delParent is JSONObject) {
            val deleteCommand = DeleteElementCommand(delParent, menuItem.text, index = null)
            deleteCommand.execute()
            commands.addLast(deleteCommand)
        } else if (delParent is JSONArray){
            val deleteCommand = DeleteElementCommand(delParent, key = null, index = arrayIndex)
            deleteCommand.execute()
            commands.addLast(deleteCommand)
        }
    }

    private fun addObservers(jsonElement: JSONElement){
        (jsonElement as? JSONArray)?.addObserver(object: JSONArrayObserver {
            override fun elementAdded(value: JSONElement) {
                srcArea.text = jsonSource.toString()
                reparse()
            }

            override fun elementRemoved(index: Int) {
                srcArea.text = jsonSource.toString()
                reparse()
            }

            override fun elementReplaced(index: Int, new: JSONElement) {
                srcArea.text = jsonSource.toString()
                reparse()
            }
        })

        (jsonElement as? JSONObject)?.addObserver(object: JSONObjectObserver {
            override fun elementAdded(value: JSONElement) {
                srcArea.text = jsonSource.toString()
                reparse()
            }

            override fun elementRemoved(key: String) {
                srcArea.text = jsonSource.toString()
                reparse()
            }

            override fun elementReplaced(key: String, new: JSONElement) {
                srcArea.text = jsonSource.toString()
                reparse()
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
                            val index = parent.jPanel.components.indexOf(e.source)

                            val prevText = (parent.jsonParent as JSONArray).getChildren()[index].toString()

                            if (prevText != arrayTextfield.text) {
                                val replaceCommand = ReplaceElementCommand(parent.jsonParent, key = null, index = index, getJSONElement(arrayTextfield.text))
                                replaceCommand.execute()
                                commands.addLast(replaceCommand)
                            }
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

            jsonSource.getChildren().forEach {
                newParent.apply {
                    newParent.children.add(parse(newParent, it, firstRun))
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

            jsonSource.getChildren().forEach {
                newParent.apply {
                    newParent.children.add(parse(newParent, JSONObjectEntry(it), firstRun))
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

                                    val prevText = (parent.jsonParent as JSONObject).getChildren()[jsonSource.entry.key].toString()

                                    if (prevText != objectTextfield.text) {
                                        val replaceCommand = ReplaceElementCommand(parent.jsonParent, key = jsonSource.entry.key, index = null, getJSONElement(objectTextfield.text))
                                        replaceCommand.execute()
                                        commands.addLast(replaceCommand)
                                    }
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

                        parse(newParent, jsonSource.entry.value, firstRun)
                        add(newParent.jPanel)
                    }).name = jsonSource.entry.key
                }
            }
        }
        return rootPanel
    }

    private fun reparse() {
        panelMatches = mutableMapOf()
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
                addObservers(array)
                array
            }
            "__Object" -> {
                val obj = JSONObject()
                obj.addElement("placeholder", JSONString(""))
                addObservers(obj)
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