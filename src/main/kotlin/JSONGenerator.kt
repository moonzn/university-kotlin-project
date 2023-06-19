import java.lang.IllegalArgumentException
import kotlin.reflect.*
import kotlin.reflect.full.*

/** Convert kotlin models/primitives to JSONElements **/

class JSONGenerator {
    /** API to ease the conversion **/

    /**
     * This function makes the conversion of data classes/enums/primitive types/ etc. to JSONElements
     *
     * @throws IllegalArgumentException if the initiator type is not supported
     * @param initiator Anything we want to convert to JSONElement
     * @return The converted object (JSONElement object)
     */
    fun generate(initiator: Any?): JSONElement {
        // initiator is null
        if (initiator == null) return JSONNull

        // initiator is Enum
        if (initiator::class.isEnum) return JSONString((initiator as Enum<*>).name)

        // initiator is a Collection
        if (Collection::class in (initiator::class as KClass<*>).allSuperclasses) return generateJSONArray(initiator as Collection<*>)

        // initiator is a Map
        if (Map::class in (initiator::class as KClass<*>).allSuperclasses) return generateJSONObject(initiator as Map<*,*>)

        // initiator is data class
        if (initiator::class.isData) return generateFromDataClass(initiator)

        // initiator is a primitive type
        return when (initiator::class) {
            String::class -> JSONString(initiator as String)
            Int::class -> JSONInt(initiator as Int)
            Double::class -> JSONDouble(initiator as Double)
            Boolean::class -> JSONBool(initiator as Boolean)
            else -> throw IllegalArgumentException("Type not supported")
        }
    }

    /**
     * Converts Collection initiator into a JSONArray
     * Converts every child element to the proper JSONElement as well
     *
     * @param initiator Collection we want to convert to JSONArray
     * @return Final JSONArray object
     */
    private fun generateJSONArray(initiator: Collection<*>): JSONArray {
        val array = JSONArray()
        initiator.forEach {
            array.addElement(generate(it))
        }
        return array
    }

    /**
     * Converts Map initiator into a JSONObject
     * Converts every child element to the proper JSONElement as well
     *
     * @param initiator Map we want to convert to JSONObject
     * @return Final JSONObject object
     */
    private fun generateJSONObject(initiator: Map<*,*>): JSONObject {
        val objekt = JSONObject()
        initiator.forEach {
            objekt.addElement(it.key.toString(), generate(it.value))
        }
        return objekt
    }

    /**
     * Converts given data class into a JSONObject
     * It will take annotations into consideration
     * Converts every child element to the proper JSONElement as well
     *
     * @param initiator Data class we want to convert to JSONObject
     * @return Final JSONObject object
     */
    private fun generateFromDataClass(initiator: Any): JSONObject {
        val objekt = JSONObject()
        val properties = initiator::class.dataClassFields  // Get data class fields (keys)
        properties.filter{ !it.hasAnnotation<Exclude>() }.forEach {
            val keyValuePair = filterAnnotations(it, initiator)
            objekt.addElement(keyValuePair.first, generate(keyValuePair.second))
        }
        return objekt
    }

    /**
     * Check for the property annotation
     * If annotation is "Identifier", use the name specified in that annotation as the key
     * If annotation is "ForceString", convert the value to string
     *
     * @param kProperty Data class property
     * @param initiator Data class (so we can get the value associated with the property tag)
     * @return Pair key value after the annotations modifications
     */
    private fun filterAnnotations(kProperty: KProperty<*>, initiator: Any): Pair<String, Any?> {
        var name = kProperty.name  // Get the tag
        var value = kProperty.call(initiator)  // Get the value
        if (kProperty.hasAnnotation<Identifier>()) name = kProperty.findAnnotation<Identifier>()!!.identifier
        if (kProperty.hasAnnotation<ForceString>()) value = value.toString()
        return Pair(name, value)
    }

    //Courtesy: https://andre-santos-pt.github.io/
    private val KClass<*>.dataClassFields: List<KProperty<*>>
        get() {
            require(isData) { "instance must be data class" }
            return primaryConstructor!!.parameters.map { p ->
                declaredMemberProperties.find { it.name == p.name }!!
            }
        }

    //Courtesy: https://andre-santos-pt.github.io/
    private val KClassifier?.isEnum: Boolean
        get() = this is KClass<*> && this.isSubclassOf(Enum::class)
}