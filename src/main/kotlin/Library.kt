sealed interface JSONElement {
    val parent: JSONElement?
    fun depth(): Int
    fun toText(): String
}

sealed interface JSONStructure: JSONElement {
    fun addElement(): JSONStructure
}

class JSONValue: JSONElement {

    val value: Any?
    override val parent: JSONStructure

    constructor(value: String? = null, parent: JSONStructure) {
        this.value = value
        this.parent = parent
    }

    constructor(value: Int, parent: JSONStructure) {
        this.value = value
        this.parent = parent
    }

    constructor(value: Double, parent: JSONStructure) {
        this.value = value
        this.parent = parent
    }

    constructor(value: Boolean, parent: JSONStructure) {
        this.value = value
        this.parent = parent
    }

    override fun depth(): Int {
        TODO("Not yet implemented")
    }

    override fun toText(): String {
        TODO("Not yet implemented")
    }
}

data class JSONObject(
    val name: String? = null,
    override val parent: JSONStructure? = null,
    val children: MutableList<JSONElement> = mutableListOf()
): JSONStructure {

    override fun addElement(): JSONStructure {
        TODO("Not yet implemented")
    }

    override fun depth(): Int {
        TODO("Not yet implemented")
    }

    override fun toText(): String {
        TODO("Not yet implemented")
    }
}

data class JSONArray(
    override val parent: JSONStructure? = null,
    val children: MutableList<JSONElement> = mutableListOf()
): JSONStructure {

    override fun addElement(): JSONStructure {
        TODO("Not yet implemented")
    }

    override fun depth(): Int {
        TODO("Not yet implemented")
    }

    override fun toText(): String {
        TODO("Not yet implemented")
    }
}

fun main() {
    val cu = JSONObject(name = "object")
    val cu2 = JSONObject()
    val cu3 = JSONObject(name = "yes", parent = cu)
    val nullvalue = JSONValue(parent = cu)
    val integer = JSONValue(parent = cu, value = 2)
    val double = JSONValue(parent = cu, value = 2.0)
    val bool = JSONValue(parent = cu, value = "2")
    val string = JSONValue(parent = cu, value = true)
    val nul = JSONValue(parent = cu, value = null)
    val test = JSONValue(null, cu)

    print(nullvalue.value)
    print("\n")
    print(integer.value)
    print("\n")
    print(test.value)
}