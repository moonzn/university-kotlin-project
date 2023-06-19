
/*** JSON Structures - more complex structures that may contain other JSONElements ***/

class JSONArray: JSONStructure {
    /** This class represents a JSON array **/

    private val children : MutableList<JSONElement> = mutableListOf()
    private val observers: MutableList<JSONArrayObserver> = mutableListOf()

    /**
     * Adds an observer to this JSONArray observer's list
     *
     * @param observer Observer we want to add
     */
    fun addObserver(observer: JSONArrayObserver) = observers.add(observer)

    /**
     * Removes an observer to this JSONArray observer's list
     *
     * @param observer Observer we want to remove
     */
    fun removeObserver(observer: JSONArrayObserver) = observers.remove(observer)

    /**
     * Gets this JSONArray (and its children) in a human-readable JSON string
     *
     * [
     *      elem1,
     *      elem2
     * ]
     *
     * @param indent Custom indentation we want for the pretty print to be initialized with: can be a tab, empty string, etc
     * @return Properly formatted (and human-readable) JSON string
     */
    override fun prettyToString(indent: String): String {
        val childIndent = "$indent\t"  // It will always add a tab
        return children.joinToString(
            prefix = "[\n$childIndent",
            postfix = "\n$indent\t]",
            separator = ",\n$childIndent",
            transform = { it.prettyToString(childIndent) }  // Pretty print child
        )
    }

    /**
     * Gets this JSONArray in string format. The output should be the same as the prettyPrint function,
     * so we just return the prettyPrint function
     *
     * @return Properly formatted (and human-readable) JSON string
     */
    override fun toString(): String {
        return this.prettyToString()
    }

    /**
     * Accepts a visitor; to do a search, validation or other upon this JSONArray
     * and its children
     *
     * @param visitor Visitor object we want to run over this JSONArray and its children
     */
    override fun accept(visitor: Visitor) {
        if (visitor.visit(this)) {  // Successful visit
            children.forEach {
                it.accept(visitor)  // Visit every child
            }
        }
        visitor.endVisit(this)
    }

    /**
     * Adds the given JSONElement to this JSONArray's children's list
     * It will trigger the observers, because the JSONArray's content was changed
     *
     * @param value JSONElement object we want to add
     */
    fun addElement(value: JSONElement) {
        if(children.add(value))  // The add was successful
            observers.forEach {
                it.elementAdded(value)  // Trigger observers
            }
    }

    /**
     * Replaces one JSONElement by another, in this JSONArray's children's list
     * It will trigger the observers, because the JSONArray's content was changed
     *
     * @param index Index, in the children's list, of the JSONElement we want to replace
     * @param new JSONElement that will replace the old one
     */
    fun replaceElement(index: Int, new: JSONElement) {
        children[index] = new  // Replace JSONElement
        observers.forEach {
            it.elementReplaced(index, new)  // Trigger observers
        }
    }

    /**
     * Deletes an existent JSONElement, in this JSONArray's children's list
     * It will trigger the observers, because the JSONArray's content was changed
     *
     * @param index Index, in the children's list, of the JSONElement we want to remove
     */
    fun deleteElement(index: Int) {
        children.removeAt(index)  // Remove JSONElement
        observers.forEach {
            it.elementRemoved(index)  // Trigger observers
        }
    }

    /**
     * Gets the index of the given JSONElement, in this JSONArray's children's list
     *
     * @param element The JSONElement we want to search the index for
     * @return Index of the given element, in the children's list
     */
    fun getIndex(element: JSONElement): Int {
        return children.indexOf(element)
    }

    /**
     * Gets this JSONArray's children's list
     *
     * @return Children's list
     */
    fun getChildren() = children
}

class JSONObject: JSONStructure {
    /** This class represents a JSON object **/

    private val children: MutableMap<String,JSONElement> = mutableMapOf()
    private val observers: MutableList<JSONObjectObserver> = mutableListOf()

    /**
     * Adds an observer to this JSONObject observer's list
     *
     * @param observer Observer we want to add
     */
    fun addObserver(observer: JSONObjectObserver) = observers.add(observer)

    /**
     * Removes an observer to this JSONObject observer's list
     *
     * @param observer Observer we want to remove
     */
    fun removeObserver(observer: JSONObjectObserver) = observers.remove(observer)

    /**
     * Gets this JSONObject (and its children) in a human-readable JSON string
     *
     * {
     *      "key1": elem1,
     *      "key2": elem2
     * }
     *
     * @param indent Custom indentation we want for the pretty print to be initialized with: can be a tab, empty string, etc
     * @return Properly formatted (and human-readable) JSON string
     */
    override fun prettyToString(indent: String): String {
        val childIndent = "$indent\t"
        return children.entries.joinToString(
            prefix = "{\n$childIndent",
            postfix = "\n$indent}",
            separator = ",\n$childIndent",
            transform = { "\"${it.key}\": ${it.value.prettyToString()}" }
        )
    }

    /**
     * Gets this JSONObject in string format. The output should be the same as the prettyPrint function,
     * so we just return the prettyPrint function
     *
     * @return Properly formatted (and human-readable) JSON string
     */
    override fun toString(): String {
        return this.prettyToString()
    }

    /**
     * Accepts a visitor; to do a search, validation or other upon this JSONObject
     * and its children
     *
     * @param visitor Visitor object we want to run over this JSONObject and its children
     */
    override fun accept(visitor: Visitor) {
        if (visitor.visit(this)) {  // Successful visit
            children.forEach {
                it.accept(visitor)  // Visit child
            }
        }
        visitor.endVisit(this)
    }

    /**
     * Adds the given JSONElement to this JSONObject's children's dictionary
     * It will trigger the observers, because the JSONObject's content was changed
     *
     * @param key The key/tag associated with the new JSONElement we are going to add
     * @param value JSONElement object we want to add
     */
    fun addElement(key: String, value: JSONElement) {
        children[key] = value  // Add child
        observers.forEach {
            it.elementAdded(value)  // Trigger observers
        }
    }

    /**
     * Replaces one JSONElement by another, in this JSONObject's children's dictionary
     * It will trigger the observers, because the JSONObject's content was changed
     *
     * @param key Key, in the children's dictionary, of the JSONElement we want to replace
     * @param new JSONElement that will replace the old one
     */
    fun replaceElement(key: String, new: JSONElement) {
        children[key] = new  // Replace JSONElement
        observers.forEach {
            it.elementReplaced(key, new)  // Trigger observers
        }
    }

    /**
     * Deletes an existent JSONElement, in this JSONObject's children's dictionary
     * It will trigger the observers, because the JSONObject's content was changed
     *
     * @param key Key, in the children's dictionary, of the JSONElement we want to remove
     */
    fun deleteElement(key: String) {
        children.remove(key)  // Remove JSONElement
        observers.forEach {
            it.elementRemoved(key)  // Trigger observers
        }
    }

    /**
     * Checks if this JSONObject's children's dictionary contains all the provided keys
     *
     * @param keys List of keys
     * @return true if it contains all the keys; false otherwise
     */
    fun containsKeys(keys: List<String>): Boolean = keys.all {children.containsKey(it)}

    /**
     * Checks if this JSONObject's children's dictionary only has the provided keys
     *
     * @param keys Structure / list of keys
     * @return true if it only has the given keys; false otherwise
     */
    fun hasStructure(keys: List<String>): Boolean = keys.size == children.keys.size && containsKeys(keys)

    /**
     * Gets this JSONArray's children's list
     *
     * @return Children's list
     */
    fun getChildren() = children
}


// Entradas de um mapa como um objeto para ser mais facil de lidar
data class JSONObjectEntry(val entry: Map.Entry<String, JSONElement>): JSONElement {

    private val JSONObjectEntry.key: String
        get() = entry.key

    private val JSONObjectEntry.value: JSONElement
        get() = entry.value

    override fun toString(): String {
        return ("$key: $value")
    }

    override fun prettyToString(indent: String): String {
        return this.toString()
    }

    override fun accept(visitor: Visitor) {
    }
}

// Visitante saber visitar as entradas do mapa
fun Map.Entry<String, JSONElement>.accept(visitor: Visitor) {
    if (visitor.visit(this)) {
        this.value.accept(visitor)
    }
    visitor.endVisit(this)
}
