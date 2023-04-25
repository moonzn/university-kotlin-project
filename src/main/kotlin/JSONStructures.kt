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

    fun checkAllObjectsStructure(structure: List<String>): Pair<Boolean, MutableList<JSONElement>> {
        var integrity = true
        val offenders = mutableListOf<JSONElement>()

        children.forEach { obj ->
            if(obj is JSONObject) {
                if (!obj.containsKeys(structure)) {
                    integrity = false
                    offenders.add(obj)
                }
            } else {
                integrity = false
                offenders.add(obj)
            }
        }
    return Pair(integrity, offenders)
    }

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

    fun containsKey(key: String): Boolean = children.containsKey(key)

    fun containsKeys(keys: List<String>): Boolean = keys.all {children.containsKey(it)}
}

fun Map.Entry<String, JSONElement>.accept(visitor: Visitor) {
    if (visitor.visit(this)) {
        this.value.accept(visitor)
    }
    visitor.endVisit(this)
}