data class JSONString(
    private val value: String
): JSONValue {

    override fun toString(): String{
        return "\"$value\""
    }
}

data class JSONBool(
    private val value: Boolean
): JSONValue {
    override fun toString(): String {
        return "$value"
    }
}

data class JSONInt(
    private val value: Int
): JSONValue {
    override fun toString(): String {
        return "$value"
    }
}

data class JSONDouble(
    private val value: Double
): JSONValue {
    override fun toString(): String {
        return "$value"
    }
}

object JSONNull: JSONValue {
    private val value = null
    override fun toString(): String {
        return "null"
    }
}