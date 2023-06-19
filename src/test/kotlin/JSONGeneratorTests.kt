import kotlin.test.Test
import kotlin.test.assertEquals

enum class TestEnum {
    Bachelor, Master, Doctoral
}

class JSONGeneratorTests {

    @Test
    fun jsonBoolTest() {
        assertEquals(JSONBool(true), JSONGenerator().generate(true))
        assertEquals(JSONBool(false), JSONGenerator().generate(false))
    }

    @Test
    fun jsonStringTest() {
        assertEquals(JSONString("internacional"), JSONGenerator().generate("internacional"))
        assertEquals(JSONString("Bachelor"), JSONGenerator().generate(StudentType.Bachelor))
    }

    @Test
    fun jsonIntTest() {
        assertEquals(JSONInt(10), JSONGenerator().generate(10))
    }

    @Test
    fun jsonDoubleTest() {
        assertEquals(JSONDouble(3.2), JSONGenerator().generate(3.2))
    }

    @Test
    fun jsonNullTest() {
        assertEquals(JSONNull, JSONGenerator().generate(null))
    }

    @Test
    fun jsonObjectTest() {
        data class TestClass1(
            @ForceString
            val number: Int,
            @Identifier("nome")
            val name: String,
            @Exclude
            val type: TestEnum? = null
        )
        val expected1 = JSONObject()
        expected1.addElement("number", JSONString("10"))
        expected1.addElement("nome", JSONString("Jesse"))

        // Annotations test
        assertEquals(expected1.toString(), JSONGenerator().generate(TestClass1(10, "Jesse", TestEnum.Doctoral)).toString())

        data class TestClass2(
            val number: Int,
            val name: String,
            val type: TestEnum? = null
        )
        val expected2 = JSONObject()
        expected2.addElement("number", JSONInt(10))
        expected2.addElement("name", JSONString("Jesse"))
        expected2.addElement("type", JSONString("Doctoral"))

        // Non-annotations test
        assertEquals(expected2.toString(), JSONGenerator().generate(TestClass2(10, "Jesse", TestEnum.Doctoral)).toString())

        val mapTest = mapOf(
            "number" to 10,
            "name" to "Jesse",
            "type" to TestEnum.Doctoral,
            "height" to 1.70,
            "brown eyes" to false,
            "is chef" to null
        )

        val expected3 = JSONObject()
        expected3.addElement("number", JSONInt(10))
        expected3.addElement("name", JSONString("Jesse"))
        expected3.addElement("type", JSONString("Doctoral"))
        expected3.addElement("height", JSONDouble(1.70))
        expected3.addElement("brown eyes", JSONBool(false))
        expected3.addElement("is chef", JSONNull)

        // Map/dictionary test
        assertEquals(expected3.toString(), JSONGenerator().generate(mapTest).toString())

    }

    @Test
    fun jsonArrayTest() {

        val collectionTest = listOf("Hey", 1.0, 10, false, null, TestEnum.Master)

        val expected = JSONArray()
        expected.addElement(JSONString("Hey"))
        expected.addElement(JSONDouble(1.0))
        expected.addElement(JSONInt(10))
        expected.addElement(JSONBool(false))
        expected.addElement(JSONNull)
        expected.addElement(JSONString("Master"))

        assertEquals(expected.toString(), JSONGenerator().generate(collectionTest).toString())
    }
}