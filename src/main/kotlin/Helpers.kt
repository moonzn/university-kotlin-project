import java.awt.Component
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.event.*
import javax.swing.*


fun main() {
    var pai = JSONArray()

    pai.addObserver(object : JSONArrayObserver {
        override fun elementAdded(value: JSONElement) {
            println(">:(")
        }
    })
    Editor(pai).open()
}

class Editor(val jsonSource: JSONArray) {

    val srcArea = JTextArea()

    val frame = JFrame("Josue - JSON Object Editor").apply {
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        layout = GridLayout(0, 2)
        size = Dimension(600, 600)

        val left = JPanel()
        left.layout = GridLayout()
        val scrollPane = JScrollPane(testPanel()).apply {
            horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
            verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        }
        left.add(scrollPane)
        add(left)

        val right = JPanel()
        right.layout = GridLayout()
        //val srcArea = JTextArea()
        srcArea.tabSize = 2
        srcArea.text = "TODO"
        right.add(srcArea)
        add(right)
    }

    fun open() {
        frame.isVisible = true
    }

    fun testPanel(): JPanel =
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            alignmentX = Component.LEFT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT

            border = BorderFactory.createEmptyBorder(0, 10, 0, 10)

            // menu
            addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        val menu = JPopupMenu("Message")

                        /* ---------------- ADD ----------------  */

                        val add = JButton("add")

                        add.addActionListener {
                            add(testWidget("N/A"))
                            val jsonElement = JSONNull
                            jsonSource.addElement(jsonElement)
                            srcArea.text = jsonSource.toString()
                            menu.isVisible = false
                            revalidate()
                            frame.repaint()
                        }


                        /* ---------------- DELETE ----------------  */

                        val del = JButton("delete all")

                        del.addActionListener {
                            components.forEach {
                                remove(it)
                            }
                            menu.isVisible = false
                            revalidate()
                            frame.repaint()
                        }
                        menu.add(add);
                        menu.add(del)
                        menu.show(this@apply, 100, 100);
                    }
                }
            })
        }


    fun testWidget(value: String): JPanel =
        JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = Component.LEFT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT

            val label = JLabel()
            label.text = "N/A"
            label.addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    val textField = JTextField("");
                    textField.addFocusListener(object : FocusAdapter() {



                        override fun focusLost(e: FocusEvent) {
                            jsonSource.addElement(JSONGenerator().generate(textField.text))
                            srcArea.text = jsonSource.toString()
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






