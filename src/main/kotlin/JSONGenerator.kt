import java.lang.IllegalArgumentException
import kotlin.reflect.*
import kotlin.reflect.full.*

class JSONGenerator {

    fun generate(initiator: Any?): JSONElement {
        if (initiator == null) return JSONNull

        if (initiator::class.isEnum) return JSONString((initiator as Enum<*>).name)

        if (Collection::class in (initiator::class as KClass<*>).allSuperclasses) return generateJSONArray(initiator)

        if (Map::class in (initiator::class as KClass<*>).allSuperclasses) return generateJSONObject(initiator)

        if (initiator::class.isData) return generateFromDataClass(initiator)

        return when (initiator::class) {
            String::class -> JSONString(initiator as String)
            Int::class -> JSONInt(initiator as Int)
            Double::class -> JSONDouble(initiator as Double)
            Boolean::class -> JSONBool(initiator as Boolean)
            else -> throw IllegalArgumentException("Type not supported")
        }
    }

    private fun generateJSONArray(initiator: Any): JSONArray {
        val array = JSONArray()
        (initiator as Collection<*>).forEach {
            array.addElement(generate(it))
        }

        return array
    }

    private fun generateJSONObject(initiator: Any): JSONObject {
        val objekt = JSONObject()
        (initiator as Map<*,*>).forEach {
            objekt.addElement(it.key.toString(), generate(it.value))
        }

        return objekt
    }

    private fun generateFromDataClass(initiator: Any): JSONObject {
        val objekt = JSONObject()
        val properties = initiator::class.dataClassFields

        properties.filter{ !it.hasAnnotation<Exclude>() }.forEach {
            objekt.addElement(filterAnnotations(it, initiator).first, generate(filterAnnotations(it, initiator).second))
        }

        return objekt
    }

    private fun filterAnnotations(kProperty: KProperty<*>, initiator: Any): Pair<String, Any?> {
        var name = kProperty.name
        var value = kProperty.call(initiator)
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