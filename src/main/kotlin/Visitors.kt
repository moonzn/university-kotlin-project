import kotlin.reflect.KClass

class GetJSONElementsVisitor(val key: String): Visitor {
    /** Search for elements associated to the given key **/

    private val jsonElements = mutableListOf<JSONElement>()

    /**
     * Retrieves the list of JSONElements that are associated with the given key
     *
     * @return JSONElement's list
     */
    fun getJSONElements(): MutableList<JSONElement> = jsonElements

    /**
     * Visits JSONObject entry and if that entry has the key we are looking for
     * then add the associated JSONElement to the list
     *
     * @return true
     */
    override fun visit(k: String, v: JSONElement): Boolean {
        if (k == key) {  // Has key
            jsonElements.add(v)
        }
        return true
    }
}

class GetJSONObjectsVisitor(val containsKeys: List<String>): Visitor {
    /** Get every object that contains the given keys **/

    private val jsonObjects = mutableListOf<JSONObject>()

    /**
     * Retrieves the list of JSONObjects that contain the given keys
     *
     * @return JSONObject's list
     */
    fun getJSONObjects(): MutableList<JSONObject> = jsonObjects

    /**
     * Visits JSONObject if it contains all the provided keys,
     * then add it to the list
     *
     * @return true
     */
    override fun visit(c: JSONObject): Boolean {
        if (c.containsKeys(containsKeys)) {  // Contains all the keys
            jsonObjects.add(c)
        }
        return true
    }
}

class VerifyJSONElementTypeVisitor(val key: String, val clazz: KClass<*>): Visitor {
    /** Check if the JSONElement associated to the given key respects the given clazz **/

    private var integrity = true  // Output of the validation
    private val offenders = mutableListOf<Pair<String, JSONElement>>()

    /**
     * Gets the output of the validation
     *
     * @return true if everything is correct; false otherwise
     */
    fun integrity(): Boolean = integrity

    /**
     * Retrieves the list of JSONObjects entries that don't respect the premise
     *
     * @return Pair's list
     */
    fun offenders(): MutableList<Pair<String, JSONElement>> = offenders

    /**
     * Visits JSONObject entry and checks if the rule is obeyed
     * If not, add the entry to the offender's list
     *
     * @return true
     */
    override fun visit(k: String, v: JSONElement): Boolean {
        if (k == key && !clazz.isInstance(v)) {  // JSONElement has a different class
            integrity = false
            offenders.add(Pair(k, v))
        }
        return true
    }
}

class VerifyJSONObjectsStructureVisitor(val key: String, val structure: List<String>): Visitor {
    /** Check if all the JSONObjects inside a JSONArray, associated to the given key, ONLY have the provided keys **/

    private var integrity = true  // Output of the validation
    private var offenders = mutableListOf<JSONElement>()

    /**
     * Gets the output of the validation
     *
     * @return true if everything is correct; false otherwise
     */
    fun integrity(): Boolean = integrity

    /**
     * Retrieves the list of JSONElements that don't respect the premise
     *
     * @return JSONElement's list
     */
    fun offenders(): MutableList<JSONElement> = offenders

    /**
     * Visits JSONObject entry
     * If the entry has the same key, and it is a JSONArray, it proceeds to validate that JSONArray
     * If any JSONElement inside that JSONArray is not a JSONObject, or doesn't follow the
     * given structure, that add it to the offender's list
     *
     * @return true
     */
    override fun visit(k: String, v: JSONElement): Boolean {
        if (k == key && v is JSONArray){
            v.getChildren().forEach {
                if (it !is JSONObject) {  // Not a JSONObject
                    integrity = false
                    offenders.add(it)
                }
                else if(!it.hasStructure(structure)) {  // Has a different structure
                    integrity = false
                    offenders.add(it)
                }
            }
        }
        return true
    }

}