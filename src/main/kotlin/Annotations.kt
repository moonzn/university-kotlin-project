@Target(AnnotationTarget.PROPERTY)
annotation class Exclude

@Target(AnnotationTarget.PROPERTY)
annotation class Identifier(val identifier: String)

@Target(AnnotationTarget.PROPERTY)
annotation class ForceString