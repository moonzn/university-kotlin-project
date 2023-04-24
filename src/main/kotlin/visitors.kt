interface Visitor {
    fun visit(c: JSONElement) : Boolean = true
    fun visit(c: JSONArray) : Boolean = true
    fun visit(c: JSONObject) : Boolean = true
    fun visit(c: Map.Entry<String, JSONElement>) : Boolean = true
    fun endVisit(c: JSONElement) {}
    fun endVisit(c: Map.Entry<String, JSONElement>) {}

}

class GetValuesOfKeyVisitor(val key: String) : Visitor {
    val values = mutableListOf<JSONElement?>()

    override fun visit(c: Map.Entry<String, JSONElement>): Boolean {
        if (c.key == key){
            values.add(c.value)
        }
        return true
    }
}

// TODO Permitir que sejam dadas keys para serem verificadas
class GetObjectVisitor() : Visitor {
    var objs = mutableListOf<JSONObject?>()

    override fun visit(c: JSONObject): Boolean {
        if (c.containsKey("numero") && c.containsKey("nome")){
            objs.add(c)
        }
        return true
    }
}

// TODO: Permitir que seja dada um classe como parametro para ser comparada
class VerifyValueTypeVisitor(val key: String): Visitor {
    var integrity = true
    var offenders = mutableListOf<Map.Entry<String, JSONElement>>()

    override fun visit(c: Map.Entry<String, JSONElement>): Boolean {
        if (c.key == key && c.value !is JSONInt){
            integrity = false
            offenders.add(c)

        }
        return true
    }
}

// TODO: a propriedade inscritos consiste num array onde todos os objetos têm a mesma estutura
