import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class VerifyJSONElementTypeTests {

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
        mainObject.addElement("data-exame", JSONNull)
        mainObject.addElement("inscritos", array)

        val valueTypeVisitor = VerifyJSONElementTypeVisitor(key="numero", clazz=JSONInt::class)
        mainObject.accept(valueTypeVisitor)

        val expected = mutableListOf<Map.Entry<String, JSONElement>>()

        assertTrue(valueTypeVisitor.integrity())
        assertEquals(expected, valueTypeVisitor.offenders())
    }

    @Test
    fun variantExample1() {
        arrayObject1.addElement("numero", JSONInt(101101))
        arrayObject1.addElement("nome", JSONString("Dave Farley"))
        arrayObject1.addElement("internacional", JSONBool(true))

        arrayObject2.addElement("numero", JSONInt(101102))
        arrayObject2.addElement("nome", JSONString("Martin Fowler"))
        arrayObject2.addElement("internacional", JSONBool(true))

        arrayObject3.addElement("numero", JSONDouble(3.0))
        arrayObject3.addElement("nome", JSONString("André Santos"))
        arrayObject3.addElement("internacional", JSONBool(false))

        array.addElement(arrayObject1)
        array.addElement(arrayObject2)
        array.addElement(arrayObject3)

        mainObject.addElement("uc", JSONString("PA"))
        mainObject.addElement("ects", JSONDouble(6.0))
        mainObject.addElement("data-exame", JSONNull)
        mainObject.addElement("inscritos", array)

        val valueTypeVisitor = VerifyJSONElementTypeVisitor(key="numero", clazz=JSONInt::class)
        mainObject.accept(valueTypeVisitor)

        val expected = mapOf<String, JSONElement>(Pair("numero", JSONDouble(3.0)))

        assertFalse(valueTypeVisitor.integrity())
        assertEquals(expected.entries.toMutableList(), valueTypeVisitor.offenders())
    }
}