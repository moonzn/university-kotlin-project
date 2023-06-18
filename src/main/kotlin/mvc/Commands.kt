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

class DeleteElementCommand(parent: JSONStructure, key: String?, index: Int?): Command {

    private var innerParent: JSONStructure = parent
    private var innerKey: String? = key
    private var innerIndex: Int? = index
    private var innerElement: JSONElement? = null

    override fun execute() {
        if (innerParent is JSONObject) {
            if (innerKey != null) {
                innerElement = (innerParent as JSONObject).getChildren()[innerKey]
                (innerParent as JSONObject).deleteElement(innerKey!!)
            }
        } else if (innerParent is JSONArray) {
            if (innerIndex != null) {
                innerElement = (innerParent as JSONArray).getChildren()[innerIndex!!]
                (innerParent as JSONArray).deleteElement(innerIndex!!)
            }
        }
    }

    override fun undo() {
        if (innerParent is JSONObject) {
            if (innerElement != null && innerKey != null) {
                (innerParent as JSONObject).addElement(innerKey!!, innerElement!!)
            }
        } else if (innerParent is JSONArray) {
            if (innerElement != null) {
                (innerParent as JSONArray).addElement(innerElement!!)
            }
        }
    }
}

class ReplaceElementCommand(parent: JSONStructure, key: String?, index: Int?, element: JSONElement): Command {

    private var innerParent: JSONStructure = parent
    private var innerKey: String? = key
    private var innerIndex: Int? = index
    private var innerElement: JSONElement = element
    private var previousElement: JSONElement? = null

    override fun execute() {
        if (innerParent is JSONObject) {
            if (innerKey != null) {
                previousElement = (innerParent as JSONObject).getChildren()[innerKey]
                (innerParent as JSONObject).replaceElement(innerKey!!, innerElement)
            }
        } else if (innerParent is JSONArray) {
            if (innerIndex != null) {
                previousElement = (innerParent as JSONArray).getChildren()[innerIndex!!]
                (innerParent as JSONArray).replaceElement(innerIndex!!, innerElement)
            }
        }
    }

    override fun undo() {
        if (innerParent is JSONObject) {
            if (innerKey != null && previousElement != null) {
                (innerParent as JSONObject).replaceElement(innerKey!!, previousElement!!)
            }
        } else if (innerParent is JSONArray) {
            if (innerIndex != null && previousElement != null) {
                (innerParent as JSONArray).replaceElement(innerIndex!!, previousElement!!)
            }
        }
    }
}