interface JSONElement {
    override fun toString(): String
    fun accept(visitor: Visitor)
}

interface JSONValue: JSONElement {
    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }
}

interface JSONStructure: JSONElement {

}
