fun main() {

    val obj1 = JSONObject()
    obj1.addElement("numero", JSONInt(101101))
    obj1.addElement("nome", JSONString("Dave Farley"))
    obj1.addElement("internacional", JSONBool(true))

    val obj2 = JSONObject()
    obj2.addElement("numero", JSONInt(101102))
    obj2.addElement("nome", JSONString("Martin Fowler"))
    obj2.addElement("internacional", JSONBool(true))

    val obj3 = JSONObject()
    obj3.addElement("numero", JSONInt(101103))
    obj3.addElement("nome", JSONString("André Santos"))
    obj3.addElement("internacional", JSONBool(false))

    val insquitos = JSONArray()
    insquitos.addElement(obj1)
    insquitos.addElement(obj2)
    insquitos.addElement(obj3)

    val obj = JSONObject()
    obj.addElement("uc", JSONString("PA"))
    obj.addElement("ects", JSONDouble(6.0))
    obj.addElement("data-exame", JSONNull())
    obj.addElement("inscritos", insquitos)

    println(obj.toString())

    val vi = GetValuesOfKeyVisitor(key="numero")
    obj.accept(vi)
    println(vi.values)

    val vi2 = GetObjectVisitor()
    obj.accept(vi2)
    println(vi2.objs)

    val vi3 = VerifyValueTypeVisitor(key="numero")
    obj.accept(vi3)
    println(vi3.integrity)
    println(vi3.offenders)
}