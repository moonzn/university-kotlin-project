class JSONArray: JSONStructure {

    private val children : MutableList<JSONElement> = mutableListOf()
    private val observers: MutableList<JSONArrayObserver> = mutableListOf()

    fun addObserver(observer: JSONArrayObserver) = observers.add(observer)

    fun removeObserver(observer: JSONArrayObserver) = observers.remove(observer)


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

    fun addElement(value: JSONElement) {
        if(children.add(value))
            observers.forEach {
                it.elementAdded(value)
            }
    }

    fun replaceElement(index: Int, new: JSONElement) {
        children[index] = new

        observers.forEach {
            it.elementReplaced(index, new)
        }
    }

    fun elementRemoved(index: Int) {
        children.removeAt(index)

        observers.forEach {
            it.elementRemoved(index)
        }
    }

    fun getChildren() = children
}

class JSONObject: JSONStructure {

    private val children : MutableMap<String,JSONElement> = mutableMapOf()
    private val observers: MutableList<JSONObjectObserver> = mutableListOf()

    fun addObserver(observer: JSONObjectObserver) = observers.add(observer)

    fun removeObserver(observer: JSONObjectObserver) = observers.remove(observer)

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
        observers.forEach {
            it.elementAdded(value)
        }
    }

    fun replaceElement(key: String, new: JSONElement) {
        children[key] = new

        observers.forEach {
            it.elementReplaced(key, new)
        }
    }

    fun containsKeys(keys: List<String>): Boolean = keys.all {children.containsKey(it)}

    fun hasStructure(keys: List<String>): Boolean = keys.size == children.keys.size && containsKeys(keys)

    fun getChildren() = children
}

data class JSONObjectEntry(val entry: Map.Entry<String, JSONElement>): JSONElement {

    private val JSONObjectEntry.key: String
        get() = entry.key

    private val JSONObjectEntry.value: JSONElement
        get() = entry.value

    override fun toString(): String {
        return ("$key: $value")
    }

    override fun accept(visitor: Visitor) {
    }
}

fun Map.Entry<String, JSONElement>.accept(visitor: Visitor) {
    if (visitor.visit(this)) {
        this.value.accept(visitor)
    }
    visitor.endVisit(this)
}
