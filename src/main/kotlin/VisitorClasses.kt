import kotlin.properties.Delegates

class GetValuesWithKeyVisitor(val key: String) : Visitor {
    private val values = mutableListOf<JSONElement>()

    fun output(): MutableList<JSONElement> = values

    override fun visit(c: Map.Entry<String, JSONElement>): Boolean {
        if (c.key == key){
            values.add(c.value)
        }
        return true
    }
}

// TODO Permitir que sejam passadas keys para serem verificadas
class GetObjectsWithKeysVisitor() : Visitor {
    private val objs = mutableListOf<JSONObject>()

    fun output(): MutableList<JSONObject> = objs

    override fun visit(c: JSONObject): Boolean {
        if (c.containsKey("numero") && c.containsKey("nome")){
            objs.add(c)
        }
        return true
    }
}

// TODO: Permitir que seja dada um classe como parametro para ser comparada
class VerifyValueTypeVisitor(val key: String): Visitor {
    private var integrity = true
    private val offenders = mutableListOf<Map.Entry<String, JSONElement>>()

    fun integrity(): Boolean = integrity

    fun offenders(): MutableList<Map.Entry<String, JSONElement>> = offenders

    override fun visit(c: Map.Entry<String, JSONElement>): Boolean {
        if (c.key == key && c.value !is JSONInt){
            integrity = false
            offenders.add(c)

        }
        return true
    }
}

// TODO: a propriedade inscritos consiste num array onde todos os objetos têm a mesma estrutura
class VerifyObjectsInArrayVisitor(val key: String, val structure: List<String>): Visitor {
    private var integrity by Delegates.notNull<Boolean>()
    private lateinit var offenders: MutableList<JSONElement>

    fun integrity(): Boolean = integrity

    fun offenders(): MutableList<JSONElement> = offenders

    override fun visit(c: Map.Entry<String, JSONElement>): Boolean {
        if (c.key == key && c.value is JSONArray){
            val output = (c.value as JSONArray).checkAllObjectsStructure(structure)
            integrity = output.first
            offenders = output.second
        }
        return true
    }
}