package mvc

import Command
import JSONArray
import JSONElement
import JSONObject
import JSONStructure

class AddElementCommand(parent: JSONStructure, key: String?, element: JSONElement): Command {

    private var innerParent: JSONStructure = parent
    private var innerKey: String? = key
    private var innerIndex: Int? = null
    private var innerElement: JSONElement = element

    override fun execute() {
        if (innerParent is JSONObject) {
            if (innerKey != null) {
                (innerParent as JSONObject).addElement(innerKey!!, innerElement)
            }
        } else if (innerParent is JSONArray) {
            (innerParent as JSONArray).addElement(innerElement)
            innerIndex = (innerParent as JSONArray).getIndex(innerElement)
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

class DeleteElementCommand(): Command {

    override fun execute() {
        TODO("Not yet implemented")
    }

    override fun undo() {
        TODO("Not yet implemented")
    }
}