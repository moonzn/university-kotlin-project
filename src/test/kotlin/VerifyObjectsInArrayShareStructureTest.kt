import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class VerifyObjectsInArrayShareStructureTest {

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

        val objectStructureVisitor = VerifyObjectsInArrayVisitor(key="inscritos", structure=listOf("numero", "nome", "internacional"))
        mainObject.accept(objectStructureVisitor)

        assertTrue(objectStructureVisitor.integrity())
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

        val objectStructureVisitor = VerifyObjectsInArrayVisitor(key="inscritos", structure=listOf("numero", "nome", "internacional"))
        mainObject.accept(objectStructureVisitor)

        assertFalse(objectStructureVisitor.integrity())
        assertEquals("[{\"numerX\": 101101, \"nome\": \"Dave Farley\", \"internacional\": true}]", objectStructureVisitor.offenders().toString())
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

        val objectStructureVisitor = VerifyObjectsInArrayVisitor(key="inscritos", structure=listOf("numero", "nome", "internacional"))
        mainObject.accept(objectStructureVisitor)

        assertFalse(objectStructureVisitor.integrity())
        assertEquals("[{\"numerX\": 101101, \"nome\": \"Dave Farley\", \"internacional\": true}, {\"numerX\": 101102, \"nome\": \"Martin Fowler\", \"internacional\": true}]", objectStructureVisitor.offenders().toString())
    }
}