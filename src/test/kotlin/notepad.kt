val mainObject = JSONObject()
val array = JSONArray()
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

    mainObject.addElement("uc", JSONString("PA"))
    mainObject.addElement("ects", JSONDouble(6.0))
    mainObject.addElement("data-exame", JSONNull)
    mainObject.addElement("inscritos", array)

    val s = Student(7, "Cristiano", StudentType.Doctoral)
    val t = Turma("5ºH", s)
    val cu = JSONGenerator().generate(listOf(t, "ola", 2, "adeus", 3.0, null, false, StudentType.Bachelor,
        listOf("ola", 2, "adeus", 3.0, null, false), mapOf(1 to "x", 2 to 3.0, -1 to true, 3 to mapOf(1 to "x", 2 to 3.0, -1 to true))
    ))
    println(cu)
}
