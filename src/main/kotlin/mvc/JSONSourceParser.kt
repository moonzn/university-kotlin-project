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
                    val menu = JPopupMenu("Message")

                    val add = JButton("Add")
                    add.addActionListener {

                    }

                    val del = JMenu("Delete")
                    var index = 0

                    (e?.component as JPanel).components.forEach {
                        val menuItem = JMenuItem(it.name)
                        val teste = index

                        menuItem.addActionListener {
                            val parent = panelMatches[e.component]?.jsonParent

                            if (parent is JSONObject)
                                parent.deleteElement(menuItem.text)
                            else
                                (parent as JSONArray).deleteElement(teste)

                            rootPanel.jPanel.removeAll()

                            /*
                            * Se esta funçao estiver dentro do deleteElement, dá esta exceção:
                            * Exception in thread "AWT-EventQueue-0" java.util.ConcurrentModificationException
                            *
                            * se ficasse lá, a UI continuava a funcionar normalmente, mas dá essa exceçao
                            * se estiver aqui, a UI tb funciona normalmente, mas ja nao da essa exceçao
                            *
                            * */
                            reparse()
                        }
                        del.add(menuItem)
                        index += 1
                    }
                    menu.add(add)
                    menu.add(del)
                    menu.show(e.component, e.x, e.y);
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
                srcArea.text = jsonSource.toString()
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
                srcArea.text = jsonSource.toString()
            }

            override fun elementReplaced(key: String, new: JSONElement) {
                srcArea.text = jsonSource.toString()
            }

        })
    }

    fun reparse() {
        parse(jsonSource = jsonSource, firstRun = false)
        rootPanel.jPanel.revalidate()
        rootPanel.jPanel.repaint()
    }

    fun parse(parent: JSONPanel = rootPanel, jsonSource: JSONElement, firstRun: Boolean = true): JSONPanel {

        if (jsonSource is JSONValue) {
            parent.jPanel.apply {
                add(JTextField().apply {
                    text = jsonSource.toString()
                    val arrayTextfield = this
                    addFocusListener(object : FocusAdapter() {
                        override fun focusLost(e: FocusEvent) {  // Modify element
                            (parent.jsonParent as JSONArray).replaceElement(parent.jPanel.components.indexOf(e.source), JSONString(arrayTextfield.text))
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

                        add(JTextField(jsonSource.entry.value.toString()).apply {
                            val objectTextfield = this
                            addFocusListener(object : FocusAdapter() {
                                override fun focusLost(e: FocusEvent) {
                                    (parent.jsonParent as JSONObject).replaceElement(jsonSource.entry.key, JSONString(objectTextfield.text))
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