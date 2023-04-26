import kotlin.test.Test
import kotlin.test.assertEquals

class ValidJSONTests {

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

        assertEquals(
            "{\"uc\": \"PA\", \"ects\": 6.0, \"data-exame\": null, \"inscritos\": [{\"numero\": 101101, \"nome\": \"Dave Farley\", \"internacional\": true}, {\"numero\": 101102, \"nome\": \"Martin Fowler\", \"internacional\": true}, {\"numero\": 101103, \"nome\": \"André Santos\", \"internacional\": false}]}",
            mainObject.toString()
        )
    }
}

