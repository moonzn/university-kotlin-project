val mainObject = JSONObject()
val array = JSONArray()
val arrayObject1 = JSONObject()
val arrayObject2 = JSONObject()
val arrayObject3 = JSONObject()

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
    mainObject.addElement("data-exame", JSONNull())
    mainObject.addElement("inscritos", array)

    println(mainObject.toString())

    val valuesVisitor = GetValuesWithKeyVisitor(key="numero")
    mainObject.accept(valuesVisitor)
    println(valuesVisitor.output())

    /*val vi2 = GetObjectsWithKeysVisitor()
    mainObject.accept(vi2)
    println(vi2.objs)

    val vi3 = VerifyValueTypeVisitor(key="numero")
    mainObject.accept(vi3)
    println(vi3.integrity)
    println(vi3.offenders)

    val vi4 = VerifyObjectsInArrayVisitor(key="inscritos", structure = listOf("numero", "nome", "internacional"))
    mainObject.accept(vi4)
    println(vi4.integrity)
    println(vi4.offenders)*/
}
