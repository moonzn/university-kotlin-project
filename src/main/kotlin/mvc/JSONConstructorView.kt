package mvc

import JSONArray
import JSONArrayObserver
import JSONElement
import JSONGenerator
import JSONNull
import java.awt.Component
import java.awt.GridLayout
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*


class JSONConstructorView(private val frame: JFrame, private val jsonSource: JSONArray) {

    private var panel = JPanel()
    private val associations : MutableMap<JPanel, JSONElement> = mutableMapOf()

    init {

        panel.layout = GridLayout()
        val scrollPane = JScrollPane(getMenuPanel()).apply {
            horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
            verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        }
        panel.add(scrollPane)


        jsonSource.addObserver(object: JSONArrayObserver {
            override fun elementAdded(value: JSONElement) {
                println("Constructor: Element Added")
                println(value.toString())
            }

            override fun elementRemoved(value: JSONElement) {
                println("Constructor: Element Removed")
                println(value.toString())
            }

            override fun elementReplaced(old: JSONElement, new: JSONElement) {
                println("Constructor: Element Modified")
                println(old.toString())
                println(new.toString())
            }
        })
    }

    fun getPanel() = panel

    fun getMenuPanel() =
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            alignmentX = Component.LEFT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT

            border = BorderFactory.createEmptyBorder(0, 10, 0, 10)

            addMouseListener(object : MouseAdapter() {

                override fun mouseClicked(e: MouseEvent) {

                    if (SwingUtilities.isRightMouseButton(e)) {

                        val menu = JPopupMenu("Menu")

                        /* ---------------- ADD ----------------  */

                        val add = JButton("add")
                        add.addActionListener {
                            add(getElementPanel())
                            menu.isVisible = false
                            revalidate()
                            frame.repaint()
                        }

                        menu.add(add);
                        menu.show(this@apply, 100, 100);

                    }
                }
            })
        }

    fun getElementPanel(): JPanel =
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = Component.LEFT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT

            val label = JLabel()
            label.text = "N/A"

            val thisPanel = this

            val jsonNull = JSONNull
            associations[thisPanel] = jsonNull
            jsonSource.addElement(jsonNull)


            label.addMouseListener(object : MouseAdapter() {

                override fun mouseClicked(e: MouseEvent) {
                    val textField = JTextField("");
                    textField.addFocusListener(object : FocusAdapter() {

                        override fun focusLost(e: FocusEvent) {
                            val new = JSONGenerator().generate(textField.text)
                            associations[thisPanel]?.let { jsonSource.replaceElement(it, new) }
                            associations[thisPanel] = new
                            println("Constructor: Lost focus")
                        }
                    })
                    remove(label)
                    add(textField)
                    revalidate()
                    repaint()
                }
            })

            add(label)
        }


}