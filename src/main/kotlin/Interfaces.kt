
/*** Interfaces ***/

interface JSONElement {
    override fun toString(): String
    fun prettyToString(indent: String = ""): String
    fun accept(visitor: Visitor)
}

interface JSONValue: JSONElement {
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
    fun visit(k: String, v: JSONElement) : Boolean = true
    fun endVisit(c: JSONElement) {}
    fun endVisit(k: String, v: JSONElement) {}
}

interface Command {
    fun execute()
    fun undo()
}