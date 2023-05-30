package mvc

import JSONArray
import JSONElement
import JSONObject
import JSONObjectEntry
import JSONValue
import java.awt.Color
import java.awt.Component
import java.awt.GridLayout
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class JSONSourceParser {

    private val rootPanel = JSONPanel(JPanel().apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        alignmentX = Component.LEFT_ALIGNMENT
        alignmentY = Component.TOP_ALIGNMENT
        border = BorderFactory.createEmptyBorder(7, 7, 7, 7)
    })

    fun parse(parent: JSONPanel = rootPanel, jsonSource: JSONElement): JSONPanel {

        if (jsonSource is JSONValue) {
            parent.jPanel.apply {
                add(JTextField().apply {
                    text = jsonSource.toString()
                })
            }
        }

        if (jsonSource is JSONArray) {

            val newParent = JSONPanel(JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                border = BorderFactory.createLineBorder(Color.darkGray, 6)
            }, parent)

            jsonSource.getChildren().forEach() {
                newParent.apply {
                    parse(newParent, it)
                }

                parent.jPanel.apply {
                    add(newParent.jPanel)
                }
            }
        }

        if (jsonSource is JSONObject) {

            val newParent = JSONPanel(JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                border = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 6)
            }, parent)

            jsonSource.getChildren().forEach() {
                newParent.apply {
                    parse(newParent, JSONObjectEntry(it))
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

                        add(JTextField(jsonSource.entry.value.toString()))
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
    private val panel: JPanel,
    private val parent: JSONPanel? = null,
    internal val children: MutableList<JSONPanel> = mutableListOf())
{
    val jPanel: JPanel
        get() = panel
}