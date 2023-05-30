import mvc.JSONSourceParser
import java.awt.Dimension
import java.awt.GridLayout
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JScrollPane

val mainObject = JSONObject()
val array = JSONArray()
val courses = JSONArray()
val arrayObject1 = JSONObject()
val arrayObject2 = JSONObject()
val arrayObject3 = JSONObject()

data class Student(
    @ForceString
    val number: Int,
    @Identifier("nome")
    val name: String,
    @Exclude
    val type: StudentType? = null
)

data class Turma(
    val designation: String,
    @ForceString
    val student: Student
)

enum class StudentType {
    Bachelor, Master, Doctoral
}

fun main() {
    arrayObject1.addElement("numero", JSONInt(101101))
    arrayObject1.addElement("nome", JSONString("Dave Farley"))
    arrayObject1.addElement("internacional", JSONBool(true))

    arrayObject2.addElement("numero", JSONInt(101102))
    arrayObject2.addElement("nome", JSONString("Martin Fowler"))
    arrayObject2.addElement("internacional", JSONBool(true))

    arrayObject3.addElement("numero", JSONInt(101103))
    arrayObject3.addElement("nome", JSONString("André Santos"))
    arrayObject3.addElement("internacional", JSONBool(false))

    array.addElement(arrayObject1)
    array.addElement(arrayObject2)
    array.addElement(arrayObject3)

    courses.addElement(JSONString("MEI"))
    courses.addElement(JSONString("MIG"))
    courses.addElement(JSONString("METI"))

    mainObject.addElement("uc", JSONString("PA"))
    mainObject.addElement("ects", JSONDouble(6.0))
    mainObject.addElement("data-exame", JSONNull)
    mainObject.addElement("inscritos", array)
    mainObject.addElement("cursos", courses)

    val parser = JSONSourceParser()
    val panel = parser.parse(jsonSource = mainObject)
    val frame = JFrame().apply {
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        layout = GridLayout(0, 2)
        size = Dimension(700, 700)

        val left = JPanel()
        left.layout = GridLayout()
        val scrollPane = JScrollPane(panel.jPanel).apply {
            horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
            verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        }
        left.add(scrollPane)
        add(left)

        isVisible = true
    }
}
