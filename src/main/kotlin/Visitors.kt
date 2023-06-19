import kotlin.reflect.KClass

class GetJSONElementsVisitor(val key: String): Visitor {
    private val jsonElements = mutableListOf<JSONElement>()

    fun getJSONElements(): MutableList<JSONElement> = jsonElements

    override fun visit(k: String, v: JSONElement): Boolean {
        if (k == key) {
            jsonElements.add(v)
        }
        return true
    }
}

class GetJSONObjectsVisitor(val containsKeys: List<String>): Visitor {
    private val jsonObjects = mutableListOf<JSONObject>()

    fun getJSONObjects(): MutableList<JSONObject> = jsonObjects

    override fun visit(c: JSONObject): Boolean {
        if (c.containsKeys(containsKeys)) {
            jsonObjects.add(c)
        }
        return true
    }
}

class VerifyJSONElementTypeVisitor(val key: String, val clazz: KClass<*>): Visitor {
    private var integrity = true
    private val offenders = mutableListOf<Pair<String, JSONElement>>()

    fun integrity(): Boolean = integrity

    fun offenders(): MutableList<Pair<String, JSONElement>> = offenders

    override fun visit(k: String, v: JSONElement): Boolean {

        if (k == key && !clazz.isInstance(v)) {
            integrity = false
            offenders.add(Pair(k, v))
        }
        return true
    }
}

class VerifyJSONObjectsStructureVisitor(val key: String, val structure: List<String>): Visitor {
    private var integrity = true
    private var offenders = mutableListOf<JSONElement>()

    fun integrity(): Boolean = integrity

    fun offenders(): MutableList<JSONElement> = offenders

    override fun visit(k: String, v: JSONElement): Boolean {
        if (k == key && v is JSONArray){
            v.getChildren().forEach {
                if (it !is JSONObject) {
                    integrity = false
                    offenders.add(it)
                }
                else if(!it.hasStructure(structure)) {
                    integrity = false
                    offenders.add(it)
                }
            }
        }
        return true
    }

}