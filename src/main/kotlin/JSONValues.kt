class JSONString(
    private val value: String
): JSONValue {

    override fun toString(): String{
        return "\"$value\""
    }
}

class JSONBool(
    private val value: Boolean
): JSONValue {
    override fun toString(): String {
        return "$value"
    }
}

class JSONInt(
    private val value: Int
): JSONValue {
    override fun toString(): String {
        return "$value"
    }
}

class JSONDouble(
    private val value: Double
): JSONValue {
    override fun toString(): String {
        return "$value"
    }
}

class JSONNull(): JSONValue {
    private val value = null
    override fun toString(): String {
        return "null"
    }
}