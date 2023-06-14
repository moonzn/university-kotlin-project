package mvc

import JSONArray
import JSONArrayObserver
import JSONElement
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
                    if (e != null) {
                        println("samsung")
                    }
                }
            }
        }
    }

    private fun addObservers(jsonElement: JSONElement){
        (jsonElement as? JSONArray)?.addObserver(object: JSONArrayObserver {
            override fun elementAdded(value: JSONElement) {
                //TODO
            }

            override fun elementRemoved(index: Int) {
                //TODO
            }

            override fun elementReplaced(index: Int, new: JSONElement) {
                srcArea.text = jsonSource.toString()
            }

        })

        (jsonElement as? JSONObject)?.addObserver(object: JSONObjectObserver {
            override fun elementAdded(value: JSONElement) {
                //TODO
                // If new element is key-value, add it to the existent obj
                // If new element is just a value, convert the parent to a JSONArray
            }

            override fun elementRemoved(key: String) {
                //TODO
            }

            override fun elementReplaced(key: String, new: JSONElement) {
                srcArea.text = jsonSource.toString()
            }

        })
    }

    fun parse(parent: JSONPanel = rootPanel, jsonSource: JSONElement): JSONPanel {

        if (jsonSource is JSONValue) {
            parent.jPanel.apply {
                add(JTextField().apply {
                    text = jsonSource.toString()
                    val arrayTextfield = this
                    addFocusListener(object : FocusAdapter() {
                        override fun focusLost(e: FocusEvent) {
                            (parent.jsonParent as JSONArray).replaceElement(parent.jPanel.components.indexOf(e.source), JSONString(arrayTextfield.text))
                        }
                    })
                })
            }
        }

        if (jsonSource is JSONArray) {

            addObservers(jsonSource)

            val newParent = JSONPanel(JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                border = BorderFactory.createLineBorder(Color.darkGray, 10)
            }, parent, jsonSource)

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

            addObservers(jsonSource)

            val newParent = JSONPanel(JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                border = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 10)
            }, parent, jsonSource)

            newParent.jPanel.addMouseListener(showMenu())

            jsonSource.getChildren().forEach() {
                newParent.apply {
                    newParent.children.add(parse(newParent, JSONObjectEntry(it)))
                }

                parent.jPanel.apply {
                    add(newParent.jPanel)
                }
            }
        }

        if (jsonSource is JSONObjectEntry) {

            if (jsonSource.entry.value is JSONValue) {
                parent.jPanel.apply {
                    add(JPanel().apply {
                        layout = GridLayout(1, 2)

                        add(JLabel(jsonSource.entry.key))

                        add(JTextField(jsonSource.entry.value.toString()).apply {
                            val objectTextfield = this
                            addFocusListener(object : FocusAdapter() {
                                override fun focusLost(e: FocusEvent) {
                                    (parent.jsonParent as JSONObject).replaceElement(jsonSource.entry.key, JSONString(objectTextfield.text))
                                }
                            })
                        })
                    })
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
                    })
                }
            }
        }
        return rootPanel
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