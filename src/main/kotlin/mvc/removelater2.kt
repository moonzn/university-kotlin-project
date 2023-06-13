import java.awt.*
import java.awt.event.*
import javax.swing.*

fun main(){
    Editor2(JSONArray()).open()
}

data class NewPanel(val key: String, val value: String, val jsonElement: JSONElement, val parentJsonElement: JSONElement, val parentPanel: JPanel)

class Editor2(private val jsonSource: JSONStructure) {

    private var frame = JFrame("JSON Object Editor")
    private var srcArea = JTextArea()

    private var currentPanel = JPanel()
    private var panelKey = ""
    private var panelValue = ""

    init {

        frame = frame.apply {
            defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            layout = GridLayout(0, 2)
            size = Dimension(800, 600)


            /****** Left panel -> JSON controller ******/
            val leftPanel = JPanel()
            leftPanel.layout = GridLayout()
            val scrollPane = JScrollPane(emptyPanel()).apply {
                horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
                verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
            }
            leftPanel.add(scrollPane)

            /****** Right panel -> JSON viewer ******/
            val rightPanel = JPanel()
            rightPanel.layout = GridLayout()
            srcArea.tabSize = 2
            srcArea.text = ""
            rightPanel.add(srcArea)

            // Add both panels
            add(leftPanel)
            add(rightPanel)

            addObservers(jsonSource)
        }
    }

    fun open() {
        frame.isVisible = true
    }

    // First panel that will be initialized (empty panel inside the left panel)
    fun emptyPanel(): JPanel =
        JPanel().apply {
            val thisPanel = this
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            alignmentX = Component.LEFT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT
            border = BorderFactory.createEmptyBorder(20, 20, 20, 20)


            // menu
            addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        menuPanel(thisPanel, jsonSource).show(e.component, e.x, e.y)
                    }
                }
            })
        }


    // Menu Panel
    fun menuPanel(clickedPanel: JPanel, jsonElement: JSONElement): JPopupMenu {
        currentPanel = clickedPanel
        panelValue = ""
        panelKey = ""
        val menu = JPopupMenu("Message")
        val add = JButton("Add")
        add.addActionListener(addValue(jsonElement = jsonElement, menu = menu))
        val del = JButton("delete all")
        del.addActionListener(deleteAll(clickedPanel = clickedPanel, menu = menu))
        menu.add(add)
        menu.add(del)
        return menu
    }


    /****** Delete everything inside the clicked panel ******/
    private fun deleteAll(clickedPanel: JPanel, menu: JPopupMenu): ActionListener {
        return ActionListener {
            clickedPanel.components.forEach {
                clickedPanel.remove(it)
            }
            menu.isVisible = false
            clickedPanel.revalidate()
            frame.repaint()
        }
    }


    /****** Add JSON Value ******/
    private fun addValue(jsonElement: JSONElement, menu: JPopupMenu): ActionListener {
        return ActionListener {
            val key = JTextField(10)
            val value = JTextField(10)

            // Create the input dialog
            val panel = JPanel().apply {
                add(JLabel("Key:"))
                add(key)
                add(Box.createHorizontalStrut(15)) // a spacer
                add(JLabel("Value:"))
                add(value)
            }

            // Show the input dialog and get its result
            val result = JOptionPane.showConfirmDialog(null, panel,
                "Enter two values", JOptionPane.OK_CANCEL_OPTION)

            // User clicked "Ok"
            if (result == JOptionPane.OK_OPTION) {

                panelKey = key.text
                panelValue = value.text

                // Convert the input into an actual JSONElement
                val jsonInput = getJSONElement(value.text)
                addObservers(jsonInput)

                // If parent is a JSONArray
                if (jsonElement is JSONArray) {

                    // User provided a key
                    if (key.text != "") {

                        val jsonObj = JSONObject()
                        jsonObj.addElement(panelKey, jsonInput)
                        addObservers(jsonObj)
                        jsonElement.addElement(jsonObj)

                    }
                    else
                        jsonElement.addElement(jsonInput)

                }
                // If parent is a JSONObject
                else if (jsonElement is JSONObject) {

                    if (jsonElement.containsKeys(listOf(panelKey)))
                        JOptionPane.showMessageDialog(frame, "Key already exists");
                    else
                        jsonElement.addElement(panelKey, jsonInput)
                }
                menu.isVisible = false
            }
        }
    }

    /****** Modify JSON Value ******/
    private fun modifyValue(newPanel: NewPanel, text: JTextField): FocusAdapter {
        return object : FocusAdapter() {
            override fun focusLost(e: FocusEvent?) {
                super.focusLost(e)
                val inputVal = getJSONElement(text.text)
                if (newPanel.parentJsonElement is JSONArray) {
                    val index = newPanel.parentPanel.components.indexOf(e?.source)
                    println(index)
                    newPanel.parentJsonElement.replaceElement(index, inputVal)
                } else if(newPanel.parentJsonElement is JSONObject) {
                    newPanel.parentJsonElement.replaceElement(newPanel.key, inputVal)
                }
            }
        }
    }

    /****** Show Menu ******/
    private fun showMenu(clickedPanel: JPanel, newPanel: NewPanel): MouseAdapter {
        return object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                super.mouseClicked(e)
                if (e != null) {
                    menuPanel(clickedPanel, newPanel.jsonElement).show(e.component, e.x, e.y)
                }
            }
        }
    }

    /****** Add a TextField to the clicked panel ******/
    private fun addTextField(newPanel: NewPanel) {
        val text = JTextField(newPanel.value)
        text.addFocusListener(modifyValue(newPanel, text))
        newPanel.parentPanel.add(text)
    }

    /****** Add another panel to the clicked panel ******/
    private fun addPanel(newPanel: NewPanel) {

        var panel = if (newPanel.key != "")
            JPanel(GridLayout(0, 2))
        else
            JPanel(GridLayout(0, 1))

        panel.background = Color.BLACK
        panel.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

        if (newPanel.parentJsonElement is JSONObject)
            panel = newPanel.parentPanel

        if (newPanel.key != "" || newPanel.parentJsonElement is JSONObject) {
            // Create key label
            val labelPanel = JPanel()
            labelPanel.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
            labelPanel.layout = GridLayout(0, 1)
            labelPanel.background = Color.BLACK
            val label = JLabel(newPanel.key)
            label.foreground = Color.WHITE
            label.horizontalAlignment = SwingConstants.RIGHT
            labelPanel.add(label)
            panel.add(labelPanel)
        }

        // Create value text field
        val textPanel = JPanel()
        textPanel.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
        textPanel.layout = GridLayout(0, 1)
        textPanel.background = Color.DARK_GRAY
        val text = JTextField(newPanel.value)
        var tempPan = NewPanel(
            key = newPanel.key,
            value = newPanel.value,
            jsonElement = JSONNull,
            parentJsonElement = newPanel.jsonElement,
            parentPanel = textPanel)
        text.addFocusListener(modifyValue(tempPan, text))
        textPanel.add(text)
        panel.add(textPanel)

        // menu
        if (newPanel.jsonElement is JSONObject || newPanel.parentJsonElement is JSONObject) {
            if (newPanel.parentJsonElement is JSONObject) {
                tempPan = NewPanel(
                    key = newPanel.key,
                    value = newPanel.value,
                    jsonElement = newPanel.parentJsonElement,
                    parentJsonElement = newPanel.parentJsonElement,
                    parentPanel = textPanel)
                panel.addMouseListener(showMenu(clickedPanel = panel, newPanel = tempPan))
            }
            else
                panel.addMouseListener(showMenu(clickedPanel = panel, newPanel = newPanel))
        }
        else
            panel.addMouseListener(showMenu(clickedPanel = textPanel, newPanel = newPanel))

        if (newPanel.parentJsonElement !is JSONObject)
            newPanel.parentPanel.add(panel)
    }


    private fun getJSONElement(text: String): JSONElement {
        return when(text){
            "true" -> JSONBool(true)
            "false" -> JSONBool(false)
            "null" -> JSONNull
            "" -> JSONNull
            else -> JSONString(text)
        }
    }

    fun addElementUI(parentJsonElement: JSONElement, jsonElement: JSONElement) {
        val newPanel = NewPanel(
            key = panelKey,
            value = panelValue,
            jsonElement = jsonElement,
            parentJsonElement = parentJsonElement,
            parentPanel = currentPanel)

        if(newPanel.key != "" || newPanel.parentJsonElement is JSONObject)
            addPanel(newPanel) // We are going to add a new object -> Add a new panel
        else
            addTextField(newPanel) // We are going to add a value -> Just add another text field

        currentPanel.validate()
        frame.repaint()
    }


    fun addObservers(jsonElement: JSONElement){
        (jsonElement as? JSONArray)?.addObserver(object: JSONArrayObserver {
            override fun elementAdded(value: JSONElement) {
                addElementUI(jsonElement, value)
                srcArea.text = jsonSource.toString()
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
                addElementUI(jsonElement, value)
                srcArea.text = jsonSource.toString()
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

}









