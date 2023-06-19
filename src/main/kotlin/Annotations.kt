
/** Data classes possible annotations **/

// Exclude tag from final JSONObject
@Target(AnnotationTarget.PROPERTY)
annotation class Exclude

// Use the provided identifier as the tag name
@Target(AnnotationTarget.PROPERTY)
annotation class Identifier(val identifier: String)

// Force tag value to be a JSONString
@Target(AnnotationTarget.PROPERTY)
annotation class ForceString