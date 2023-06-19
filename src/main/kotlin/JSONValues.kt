
/*** JSON Values ***/

data class JSONString(
    private val value: String
): JSONValue {
    /** This class represents a JSON string **/

    /**
     * Gets this JSONString content
     *
     * @return The string associated with this JSONString
     */
    override fun toString(): String{
        return value
    }

    /**
     * Gets this JSONString content in a properly formatted JSON string
     *
     * @param indent Custom indentation we want for the pretty print: can be a tab, empty string, etc
     * @return The string associated with this JSONString, but with the given indent
     */
    override fun prettyToString(indent: String): String {
        return indent + "\"$value\""
    }
}

data class JSONBool(
    private val value: Boolean
): JSONValue {
    /** This class represents a JSON boolean **/

    /**
     * Gets this JSONBool content in string format
     *
     * @return The boolean value (converted to string) associated with this JSONBool
     */
    override fun toString(): String {
        return value.toString()
    }

    /**
     * Gets this JSONBool content in a properly formatted JSON string
     *
     * @param indent Custom indentation we want for the pretty print: can be a tab, empty string, etc
     * @return The boolean value (converted to string) associated with this JSONBool, but with the given indent
     */
    override fun prettyToString(indent: String): String {
        return indent + "$value"
    }
}

data class JSONInt(
    private val value: Int
): JSONValue {
    /** This class represents a JSON integer **/

    /**
     * Gets this JSONInt content in string format
     *
     * @return The integer value (converted to string) associated with this JSONInt
     */
    override fun toString(): String {
        return value.toString()
    }

    /**
     * Gets this JSONInt content in a properly formatted JSON string
     *
     * @param indent Custom indentation we want for the pretty print: can be a tab, empty string, etc
     * @return The integer value (converted to string) associated with this JSONInt, but with the given indent
     */
    override fun prettyToString(indent: String): String {
        return indent + "$value"
    }
}

data class JSONDouble(
    private val value: Double
): JSONValue {
    /** This class represents a JSON double **/

    /**
     * Gets this JSONDouble content in string format
     *
     * @return The double value (converted to string) associated with this JSONDouble
     */
    override fun toString(): String {
        return value.toString()
    }

    /**
     * Gets this JSONDouble content in a properly formatted JSON string
     *
     * @param indent Custom indentation we want for the pretty print: can be a tab, empty string, etc
     * @return The double value (converted to string) associated with this JSONDouble, but with the given indent
     */
    override fun prettyToString(indent: String): String {
        return indent + "$value"
    }
}

object JSONNull: JSONValue {
    private val value = null
    /** This class represents a JSON null **/

    /**
     * Gets this JSONNull content in string format
     *
     * @return "null"
     */
    override fun toString(): String {
        return "null"
    }

    /**
     * Gets this JSONNull content in a properly formatted JSON string
     *
     * @param indent Custom indentation we want for the pretty print: can be a tab, empty string, etc
     * @return "null", but with the given indent
     */
    override fun prettyToString(indent: String): String {
        return indent + this.toString()
    }
}