class JSONArray: JSONStructure {

    private val children : MutableList<JSONElement> = mutableListOf()
    private val observers: MutableList<JSONArrayObserver> = mutableListOf()

    fun addObserver(observer: JSONArrayObserver) = observers.add(observer)

    fun removeObserver(observer: JSONArrayObserver) = observers.remove(observer)

    override fun prettyPrint(indent: String): String {
        val childIndent = "$indent\t"
        return children.joinToString(
            prefix = "[\n$childIndent",
            postfix = "\n$indent\t]",
            separator = ",\n$childIndent",
            transform = { it.prettyPrint(childIndent) }
        )
    }

    override fun toString(): String {
        return this.prettyPrint()
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

    fun deleteElement(index: Int) {
        children.removeAt(index)

        observers.forEach {
            it.elementRemoved(index)
        }
    }

    fun getIndex(element: JSONElement): Int {
        return children.indexOf(element)
    }

    fun getChildren() = children
}

class JSONObject: JSONStructure {

    private val children: MutableMap<String,JSONElement> = mutableMapOf()
    private val observers: MutableList<JSONObjectObserver> = mutableListOf()

    fun addObserver(observer: JSONObjectObserver) = observers.add(observer)

    fun removeObserver(observer: JSONObjectObserver) = observers.remove(observer)

    override fun prettyPrint(indent: String): String {
        val childIndent = "$indent\t"
        return children.entries.joinToString(
            prefix = "{\n$childIndent",
            postfix = "\n$indent}",
            separator = ",\n$childIndent",
            transform = { "\"${it.key}\": ${it.value.prettyPrint()}" }
        )
    }

    override fun toString(): String {
        return this.prettyPrint()
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

    fun deleteElement(key: String) {
        children.remove(key)

        observers.forEach {
            it.elementRemoved(key)
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

    override fun prettyPrint(indent: String): String {
        return this.toString()
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
