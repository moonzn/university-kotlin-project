import kotlin.test.Test
import kotlin.test.assertEquals

class ObjectsWithPropertyNameTest {

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

        val objectsVisitor = GetObjectsWithKeysVisitor()
        mainObject.accept(objectsVisitor)

        assertEquals("[{\"numero\": 101101, \"nome\": \"Dave Farley\", \"internacional\": true}, {\"numero\": 101102, \"nome\": \"Martin Fowler\", \"internacional\": true}, {\"numero\": 101103, \"nome\": \"André Santos\", \"internacional\": false}]", objectsVisitor.output().toString())
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

        val objectsVisitor = GetObjectsWithKeysVisitor()
        mainObject.accept(objectsVisitor)

        assertEquals("[{\"numero\": 101102, \"nome\": \"Martin Fowler\", \"internacional\": true}, {\"numero\": 101103, \"nome\": \"André Santos\", \"internacional\": false}]", objectsVisitor.output().toString())
    }
}