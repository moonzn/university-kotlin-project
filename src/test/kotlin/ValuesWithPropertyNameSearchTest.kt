import kotlin.test.Test
import kotlin.test.assertEquals

class ValuesWithPropertyNameSearchTest {

    val mainObject = JSONObject()
    val array = JSONArray()
    val arrayObject1 = JSONObject()
    val arrayObject2 = JSONObject()
    val arrayObject3 = JSONObject()

    @Test
    fun outlinedExample() {
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

        val valuesVisitor = GetValuesWithKeyVisitor(key="numero")
        mainObject.accept(valuesVisitor)

        assertEquals("[101101, 101102, 101103]", valuesVisitor.output().toString())
    }

    @Test
    fun variantExample1() {
        // TODO "numero" -> "numerX"
        arrayObject1.addElement("numerX", JSONInt(101101))
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

        val valuesVisitor = GetValuesWithKeyVisitor(key="numero")
        mainObject.accept(valuesVisitor)

        assertEquals("[101102, 101103]", valuesVisitor.output().toString())
    }

    @Test
    fun variantExample2() {
        // TODO "numero" -> "numerX"
        arrayObject1.addElement("numerX", JSONInt(101101))
        arrayObject1.addElement("nome", JSONString("Dave Farley"))
        arrayObject1.addElement("internacional", JSONBool(true))

        // TODO "numero" -> "numerX"
        arrayObject2.addElement("numerX", JSONInt(101102))
        arrayObject2.addElement("nome", JSONString("Martin Fowler"))
        arrayObject2.addElement("internacional", JSONBool(true))

        // TODO "numero" -> "numerX"
        arrayObject3.addElement("numerX", JSONInt(101103))
        arrayObject3.addElement("nome", JSONString("André Santos"))
        arrayObject3.addElement("internacional", JSONBool(false))

        array.addElement(arrayObject1)
        array.addElement(arrayObject2)
        array.addElement(arrayObject3)

        mainObject.addElement("uc", JSONString("PA"))
        mainObject.addElement("ects", JSONDouble(6.0))
        mainObject.addElement("data-exame", JSONNull())
        mainObject.addElement("inscritos", array)

        val valuesVisitor = GetValuesWithKeyVisitor(key="numero")
        mainObject.accept(valuesVisitor)

        assertEquals("[]", valuesVisitor.output().toString())
    }

    @Test
    fun variantExample3() {
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

        // TODO key="numero" -> key="nome"
        val valuesVisitor = GetValuesWithKeyVisitor(key="nome")
        mainObject.accept(valuesVisitor)

        assertEquals("[\"Dave Farley\", \"Martin Fowler\", \"André Santos\"]", valuesVisitor.output().toString())
    }

    @Test
    fun variantExample4() {
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

        // TODO "uc" -> "nome"
        mainObject.addElement("nome", JSONString("PA"))
        mainObject.addElement("ects", JSONDouble(6.0))
        mainObject.addElement("data-exame", JSONNull())
        mainObject.addElement("inscritos", array)

        // TODO key="numero" -> key="nome"
        val valuesVisitor = GetValuesWithKeyVisitor(key="nome")
        mainObject.accept(valuesVisitor)

        assertEquals("[\"PA\", \"Dave Farley\", \"Martin Fowler\", \"André Santos\"]", valuesVisitor.output().toString())
    }
}