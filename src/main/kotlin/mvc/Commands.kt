package mvc

import Command
import JSONArray
import JSONElement
import JSONObject
import JSONStructure

class AddElementCommand: Command {

    private var innerParent: JSONStructure? = null
    private var innerKey: String? = null
    private var innerIndex: Int? = null
    private var innerElement: JSONElement? = null

    override fun execute(parent: JSONStructure, key: String?, element: JSONElement) {
        innerParent = parent
        innerKey = key
        innerIndex = null
        innerElement = element

        if (parent is JSONObject) {
            if (key != null) {
                parent.addElement(key, element)
            }
        } else if (parent is JSONArray) {
            parent.addElement(element)
            innerIndex = parent.getIndex(element)
        }
    }

    override fun undo() {
        if (innerParent is JSONObject) {
            if (innerKey != null) {
                (innerParent as JSONObject).deleteElement(innerKey!!)
            }
        } else if (innerParent is JSONArray) {
            if (innerIndex != null) {
                (innerParent as JSONArray).deleteElement(innerIndex!!)
            }
        }
    }
}