class JSONArray(): JSONStructure {

    private val children : MutableList<JSONElement> = mutableListOf()

    init {
    }

    fun addElement(value: JSONElement){
        children.add(value)
    }

    override fun toString(): String {
        return children.joinToString(prefix = "[", postfix = "]", separator = ",")
    }

    override fun accept(visitor: Visitor) {
        if (visitor.visit(this)) {
            children.forEach {
                it.accept(visitor)
            }
        }
        visitor.endVisit(this)
    }

}

class JSONObject(): JSONStructure {

    private val children : MutableMap<String,JSONElement> = mutableMapOf()

    init {
    }

    fun addElement(key: String, value: JSONElement) {
        children[key] = value
    }

    fun containsKey(key: String): Boolean = children.containsKey(key)

    override fun toString(): String {
        return children.map { "\"${it.key}\": ${it.value.toString()}" }.joinToString(prefix = "{", postfix = "}", separator = ",")
    }

    override fun accept(visitor: Visitor) {
        if (visitor.visit(this)) {
            children.forEach {
                it.accept(visitor)
            }
        }
        visitor.endVisit(this)
    }
}

fun Map.Entry<String, JSONElement>.accept(visitor: Visitor) {
    if (visitor.visit(this)) {
        this.value.accept(visitor)
    }
    visitor.endVisit(this)
}