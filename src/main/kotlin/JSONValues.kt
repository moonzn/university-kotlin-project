data class JSONString(
    private val value: String
): JSONValue {

    override fun toString(): String{
        return value
    }

    override fun prettyPrint(indent: String): String {
        return indent + "\"$value\""
    }
}

data class JSONBool(
    private val value: Boolean
): JSONValue {

    override fun toString(): String {
        return value.toString()
    }

    override fun prettyPrint(indent: String): String {
        return indent + "$value"
    }
}

data class JSONInt(
    private val value: Int
): JSONValue {

    override fun toString(): String {
        return value.toString()
    }

    override fun prettyPrint(indent: String): String {
        return indent + "$value"
    }
}

data class JSONDouble(
    private val value: Double
): JSONValue {

    override fun toString(): String {
        return value.toString()
    }

    override fun prettyPrint(indent: String): String {
        return indent + "$value"
    }
}

object JSONNull: JSONValue {
    private val value = null

    override fun toString(): String {
        return "null"
    }

    override fun prettyPrint(indent: String): String {
        return indent + this.toString()
    }
}