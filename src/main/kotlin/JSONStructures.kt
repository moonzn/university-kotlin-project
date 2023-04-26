class JSONArray(): JSONStructure {

    private val children : MutableList<JSONElement> = mutableListOf()

    override fun toString(): String {
        return children.joinToString(prefix = "[", postfix = "]", separator = ", ")
    }

    override fun accept(visitor: Visitor) {
        if (visitor.visit(this)) {
            children.forEach {
                it.accept(visitor)
            }
        }
        visitor.endVisit(this)
    }

    fun addElement(value: JSONElement){
        children.add(value)
    }

    fun getChildren() = children

}

class JSONObject(): JSONStructure {

    private val children : MutableMap<String,JSONElement> = mutableMapOf()

    override fun toString(): String {
        return children.map { "\"${it.key}\": ${it.value}" }.joinToString(prefix = "{", postfix = "}", separator = ", ")
    }

    override fun accept(visitor: Visitor) {
        if (visitor.visit(this)) {
            children.forEach {
                it.accept(visitor)
            }
        }
        visitor.endVisit(this)
    }

    fun addElement(key: String, value: JSONElement) {
        children[key] = value
    }

    fun containsKeys(keys: List<String>): Boolean = keys.all {children.containsKey(it)}

    fun hasStructure(keys: List<String>): Boolean = keys.size == children.keys.size && containsKeys(keys)
}

fun Map.Entry<String, JSONElement>.accept(visitor: Visitor) {
    if (visitor.visit(this)) {
        this.value.accept(visitor)
    }
    visitor.endVisit(this)
}
