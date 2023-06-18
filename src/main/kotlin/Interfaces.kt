interface JSONElement {
    override fun toString(): String
    fun accept(visitor: Visitor)
}

interface JSONValue: JSONElement {
    fun getValue(): String
    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }
}

interface JSONStructure: JSONElement

interface JSONArrayObserver {
    fun elementAdded(value: JSONElement) { }
    fun elementRemoved(index: Int) { }
    fun elementReplaced(index: Int, new: JSONElement) { }
}

interface JSONObjectObserver {
    fun elementAdded(value: JSONElement) { }
    fun elementRemoved(key: String) { }
    fun elementReplaced(key: String, new: JSONElement) { }
}

interface Visitor {
    fun visit(c: JSONElement) : Boolean = true
    fun visit(c: JSONArray) : Boolean = true
    fun visit(c: JSONObject) : Boolean = true
    fun visit(c: Map.Entry<String, JSONElement>) : Boolean = true  // chave valor e tirar a especificaçao doobjeto Map.Entry
    fun endVisit(c: JSONElement) {}
    fun endVisit(c: Map.Entry<String, JSONElement>) {}
}