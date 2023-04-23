interface JSONElement {
    val parent: JSONElement?
    fun depth(): Int
    fun toText(): String
}

interface JSONStructure: JSONElement {
    val children: MutableList<JSONElement>
    fun addElement(child: JSONElement)
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

data class JSONArray(
    override val parent: JSONStructure? = null,
    override val children: MutableList<JSONElement> = mutableListOf()
): JSONStructure {

    init {
        parent?.children?.add(this)
    }

    override fun addElement(child: JSONElement) {
        TODO()
    }

    override fun depth(): Int {
        TODO("Not yet implemented")
    }

    override fun toText(): String {
        TODO("Not yet implemented")
    }
}

data class JSONObject(
    override val parent: JSONStructure? = null,
    override val children: MutableList<JSONElement> = mutableListOf()
): JSONStructure {

    init {

    }

    override fun addElement(child: JSONElement) {
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
    val cu = JSONObject()
    val cu2 = JSONObject()
    val cu3 = JSONObject(parent = cu)
    val nullvalue = JSONValue(parent = cu)
    val integer = JSONValue(parent = cu, value = 2)
    val double = JSONValue(parent = cu, value = 2.0)
    val bool = JSONValue(parent = cu, value = "2")
    val string = JSONValue(parent = cu, value = true)
    val nul = JSONValue(parent = cu, value = null)
    val test = JSONValue(null, cu)
    val array = JSONArray()
    val child1 = JSONValue(parent = array, value = "Child")
    val child2 = JSONValue(parent = array, value = 27)
    val obj2 = JSONObject()
    array.addElement(cu2)
    array.addElement(obj2)

    print(array)
}

// TODO RETIRAR PARENT DO CONSTRUTOR E ATUALIZA LO NO ADD ELEMENT