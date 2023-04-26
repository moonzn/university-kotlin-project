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
    mainObject.addElement("data-exame", JSONNull)
    mainObject.addElement("inscritos", array)

    println(mainObject.toString())

    val valuesVisitor = GetJSONElementsVisitor(key="numero")
    mainObject.accept(valuesVisitor)
    println(valuesVisitor.getJSONElements())

    val vi3 = VerifyJSONElementTypeVisitor(key="numero", clazz=JSONInt::class)
    mainObject.accept(vi3)
    println(vi3.integrity())
    println(vi3.offenders())

    val teste = JSONObject()
    teste.addElement("ola", JSONInt(1))
    teste.addElement("ola2", JSONInt(2))
    teste.addElement("ola3", JSONInt(3))
    //teste.addElement("ola4", JSONInt(4))

    println(teste.hasStructure(listOf("ola", "ola2", "ola3", "ola4")))

    val vi4 = VerifyJSONObjectsStructureVisitor(key="inscritos", structure = listOf("numero", "nome", "internacional2"))
    mainObject.accept(vi4)
    println(vi4.integrity())
    println(vi4.offenders())

    /*val vi2 = GetObjectsWithKeysVisitor()
    mainObject.accept(vi2)
    println(vi2.objs)



    val vi4 = VerifyObjectsInArrayVisitor(key="inscritos", structure = listOf("numero", "nome", "internacional"))
    mainObject.accept(vi4)
    println(vi4.integrity)
    println(vi4.offenders)*/
}
