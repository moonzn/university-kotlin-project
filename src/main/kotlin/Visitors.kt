import kotlin.reflect.KClass

class GetJSONElementsVisitor(val key: String): Visitor {
    private val jsonElements = mutableListOf<JSONElement>()

    fun getJSONElements(): MutableList<JSONElement> = jsonElements

    override fun visit(c: Map.Entry<String, JSONElement>): Boolean {
        if (c.key == key) {
            jsonElements.add(c.value)
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
    private val offenders = mutableListOf<Map.Entry<String, JSONElement>>()

    fun integrity(): Boolean = integrity

    fun offenders(): MutableList<Map.Entry<String, JSONElement>> = offenders

    override fun visit(c: Map.Entry<String, JSONElement>): Boolean {

        if (c.key == key && !clazz.isInstance(c.value)) {
            integrity = false
            offenders.add(c)
        }
        return true
    }
}

class VerifyJSONObjectsStructureVisitor(val key: String, val structure: List<String>): Visitor {
    private var integrity = true
    private var offenders = mutableListOf<JSONElement>()

    fun integrity(): Boolean = integrity

    fun offenders(): MutableList<JSONElement> = offenders

    override fun visit(c: Map.Entry<String, JSONElement>): Boolean {
        if (c.key == key && c.value is JSONArray){
            (c.value as JSONArray).getChildren().forEach {
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