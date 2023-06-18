# JSON Editor

university-kotlin-project is a Kotlin library for dealing with JSON. Your JSON models are represented through a set of **JSON elements**
With this package you will be able to:
- Create your own JSON hierarchies through JSON elements
- Manipulate your JSON elements
- Make basic JSON searches
- Convert collections, data classes, primitive types into JSON elements

## Dependencies

```kotlin
dependencies {
    implementation 'org.example:university-kotlin-project:1.0-SNAPSHOT'
}
```

## Usage

### Create your own JSON elements
<br>
Let's first create your first "Hello World" program using the university-kotlin-project package!
You can create a JSON string introducing yourself to the world... and print it!

```kotlin
fun main() {
    val helloWorld = JSONString("Hello World! I am a JSON String!")
    println(helloWorld.toString())
}
```

Output:
```console
Hello World! I am a JSON String!
```

<br>

JSON strings are not the only type you can create. You may create JSON elements for **integers**, **doubles**, **null** values and **booleans** as well:

```kotlin
fun main() {
    val age = JSONInt(50)
    val height = JSONDouble(1.80)
    val alias = JSONNull
    val hasBrownEyes = JSONBool(false)

    println(age.toString())
    println(height.toString())
    println(id.toString())
    println(hasBrownEyes.toString())
}
```

Output:
```console
50
1.8
null
false
```

<br>

It is great being to instantiate a bunch of JSON variables, but how do we couple them together into an actual JSON hierarchy?
We can do that through... JSON Objects and JSON Arrays! Let's take a look.

<br>

#### Create an object

<br>

Create a profile for Mr. White, through a JSON Object:

```kotlin
fun main() {
    val mrWhite = JSONObject()

    val name = JSONString("Walter White")
    val age = JSONInt(50)
    val height = JSONDouble(1.80)
    val alias = JSONNull
    val hasBrownEyes = JSONBool(false)

    mrWhite.addElement("Name", name)
    mrWhite.addElement("Age", age)
    mrWhite.addElement("Height", height)
    mrWhite.addElement("Alias", alias)
    mrWhite.addElement("Has brown eyes?", hasBrownEyes)

    println(mrWhite.toString())
}
```

Output:
```console
{
	"Name": "Walter White",
	"Age": 50,
	"Height": 1.8,
	"Alias": null,
	"Has brown eyes?": false
}
```

<br>

#### Create an array

<br>
We may also create a list of any JSON elements we want, for example, a list of jobs:

```kotlin
fun main() {
    val primaryJob = JSONString("High school teacher")
    val secondaryJob = JSONString("Car wash employee")

    val jobs = JSONArray()
    jobs.addElement(primaryJob)
    jobs.addElement(secondaryJob)

    println(jobs.toString())
}
```

Output:

```console
[
		"High school teacher",
		"Car wash employee"
	]
```

### Replace JSON elements

We may manipulate any element of an existent JSONObject or JSONArray, through the *replaceElement* function, providing the key and the index of the element we want to change, respectively.

```kotlin
fun main() {
    val mrWhite = JSONObject()

    val primaryJob = JSONString("High school teacher")
    val secondaryJob = JSONString("Car wash employee")
    val jobs = JSONArray()
    jobs.addElement(primaryJob)
    jobs.addElement(secondaryJob)

    mrWhite.addElement("Name", JSONString("Walter White"))
    mrWhite.addElement("Alias", JSONNull)
    mrWhite.addElement("Jobs", jobs)

    println("Before:")
    println(mrWhite.toString())

    jobs.replaceElement(1, JSONString("Chef"))
    mrWhite.replaceElement("Alias", JSONString("Heisenberg"))

    println("After:")
    println(mrWhite.toString())
}
```

Output:

```console
Before:
{
	"Name": "Walter White",
	"Alias": null,
	"Jobs": [
		"High school teacher",
		"Car wash employee"
	]
}
After:
{
	"Name": "Walter White",
	"Alias": "Heisenberg",
	"Jobs": [
		"High school teacher",
		"Chef"
	]
}
```

### Delete JSON elements

<br>

Everything described in the modification of JSON elements applies to the *deleteElement* function.


```kotlin
fun main() {
    val mrWhite = JSONObject()

    val primaryJob = JSONString("High school teacher")
    val secondaryJob = JSONString("Car wash employee")
    val jobs = JSONArray()
    jobs.addElement(primaryJob)
    jobs.addElement(secondaryJob)

    mrWhite.addElement("Name", JSONString("Walter White"))
    mrWhite.addElement("Alias", JSONNull)
    mrWhite.addElement("Jobs", jobs)

    println("Before:")
    println(mrWhite.toString())

    jobs.deleteElement(1)
    mrWhite.deleteElement("Alias")

    println("After:")
    println(mrWhite.toString())
}
```

Output:

```console
Before:
{
	"Name": "Walter White",
	"Alias": null,
	"Jobs": [
		"High school teacher",
		"Car wash employee"
	]
}
After:
{
	"Name": "Walter White",
	"Jobs": [
		"High school teacher"
	]
}
```


### Basic searches

Searches are made through *Visitor* classes.

#### Search for elements with key X

If you want to search for every element with the "Alias" tag, you may accomplish that with the *GetJSONElementsVisitor* class:

```kotlin
fun main() {
    val mrWhite = JSONObject()

    val job = JSONObject()
    job.addElement("Alias", JSONString("Mr. White"))
    job.addElement("Job", JSONString("High school teacher"))

    mrWhite.addElement("Name", JSONString("Walter White"))
    mrWhite.addElement("Alias", JSONString("Heisenberg"))
    mrWhite.addElement("Job", job)

    val valuesVisitor = GetJSONElementsVisitor(key="Alias")
    mrWhite.accept(valuesVisitor)

    println(valuesVisitor.getJSONElements())
}
```
Output:

```console
[Heisenberg, Mr. White]
```

#### Get every object that contains tags X, Y...
<br>
The *GetJSONObjectsVisitor* class will search for every JSONObject that contains all the specified tags, in this example, only the "Alias" tag:

```kotlin
fun main() {
    val mrWhite = JSONObject()

    val job = JSONObject()
    job.addElement("Alias", JSONString("Mr. White"))
    job.addElement("Job", JSONString("High school teacher"))

    mrWhite.addElement("Name", JSONString("Walter White"))
    mrWhite.addElement("Alias", JSONString("Heisenberg"))
    mrWhite.addElement("Job", job)

    val valuesVisitor = GetJSONObjectsVisitor(containsKeys = listOf("Alias"))
    mrWhite.accept(valuesVisitor)

    println(valuesVisitor.getJSONObjects())
}
```
Output:

```console
[{
	"Name": "Walter White",
	"Alias": "Heisenberg",
	"Job": {
	"Alias": "Mr. White",
	"Job": "High school teacher"
}
}, {
	"Alias": "Mr. White",
	"Job": "High school teacher"
}]

```

#### Check if a specified tag respects the desired type

To check if all tags with name "Alias" are all JSONInt, use the *VerifyJSONElementTypeVisitor* class:
```kotlin
fun main() {
    val mrWhite = JSONObject()

    val job = JSONObject()
    job.addElement("Alias", JSONString("Mr. White"))
    job.addElement("Job", JSONString("High school teacher"))

    mrWhite.addElement("Name", JSONString("Walter White"))
    mrWhite.addElement("Alias", JSONString("Heisenberg"))
    mrWhite.addElement("Job", job)

    val valuesVisitor = VerifyJSONElementTypeVisitor(key = "Alias", clazz = JSONInt::class)
    mrWhite.accept(valuesVisitor)

    println(valuesVisitor.integrity())
    println(valuesVisitor.offenders())
}
```

Output:
```console
false
[Alias=Heisenberg, Alias=Mr. White]
```

#### Verify if an array objects have the same structure

```kotlin
fun main() {

    val student1 = JSONObject()
    val student2 = JSONObject()
    student1.addElement("number", JSONInt(101101))
    student1.addElement("name", JSONString("Jesse"))

    student2.addElement("number", JSONInt(101102))
    student2.addElement("name", JSONString("Chad"))

    val students = JSONArray()
    students.addElement(student1)
    students.addElement(student2)

    val mrWhite = JSONObject()
    mrWhite.addElement("Age", JSONInt(50))
    mrWhite.addElement("Job", JSONString("High school teacher"))
    mrWhite.addElement("Students", students)

    val valuesVisitor = VerifyJSONObjectsStructureVisitor(key = "Students", structure = listOf("number"))
    mrWhite.accept(valuesVisitor)

    println(valuesVisitor.integrity())
    println(valuesVisitor.offenders())
}
```

Output:
```console
false
[{
	"number": 101101,
	"name": "Jesse"
}, {
	"number": 101102,
	"name": "Chad"
}]
```

## Conversions to JSON elements

Every conversion will be made through the *JSONGenerator().generate(initiator)* functions where the initiator argument corresponds to the data class/collection/map/type... etc that we want to convert.

### Data class to JSONElement

```kotlin
fun main() {
    data class Student(
        val number: Int,
        val name: String,
    )
    val student = Student(10, "Jesse Pinkman")
    println(JSONGenerator().generate(initiator = student))
}
```

Output:
```console
{
	"number": 10,
	"name": "Jesse Pinkman"
}
```

### Collection to JSONElement

```kotlin
fun main() {
    data class Student(
        val number: Int,
        val name: String
    )
    val student1 = Student(10, "Jesse Pinkman")
    val student2 = Student(11, "Chad")
    println(JSONGenerator().generate(initiator = listOf(student1, student2)))
}
```

Output:
```console
[
	{
		"number": 10,
		"name": "Jesse Pinkman"
	},
	{
		"number": 11,
		"name": "Chad"
	}
	]
```


### Map to JSONElement

```kotlin
fun main() {
    val number = 10
    val name = "Jesse"
    println(JSONGenerator().generate(initiator = mapOf("Number" to number, "Name" to name)))
}
```

Output:
```console
{
	"Number": 10,
	"Name": "Jesse"
}
```

### Primitive types or null to JSONElement
```kotlin
fun main() {
    println(JSONGenerator().generate(initiator = 10)::class)
    println(JSONGenerator().generate(initiator = 1.70)::class)
    println(JSONGenerator().generate(initiator = "Jesse")::class)
    println(JSONGenerator().generate(initiator = false)::class)
    println(JSONGenerator().generate(initiator = null)::class)
}
```

Output:
```console
class JSONInt
class JSONDouble
class JSONString
class JSONBool
class JSONNull
```


### Enumerator to JSONElement
```kotlin
enum class Subject {
    Chemistry, Science
}

fun main() {
    println(JSONGenerator().generate(initiator = Subject.Chemistry))
    println(JSONGenerator().generate(initiator = Subject.Chemistry)::class)
}

```

Output:
```console
Chemistry
class JSONString
```

### Monitor changes through Observers

#### JSONArray
```kotlin
fun main() {
    val subjects = JSONArray()
    subjects.addElement(JSONString("Chemistry"))
    subjects.addElement(JSONString("Math"))

    subjects.addObserver(object: JSONArrayObserver {
        override fun elementAdded(value: JSONElement) {
            println("Element was added!")
        }

        override fun elementRemoved(index: Int) {
            println("Element at index $index was removed!")
        }

        override fun elementReplaced(index: Int, new: JSONElement) {
            println("Element at index $index was changed to ${new}!")
        }

    })

    subjects.addElement(JSONString("History"))
    subjects.replaceElement(1, JSONString("Science"))
    subjects.deleteElement(0)

    println("Final result:")
    println(subjects)
}
```

Output:
```console
Element was added!
Element at index 1 was changed to Science!
Element at index 0 was removed!
Final result:
[
		"Science",
		"History"
	]
```

#### JSONObject
```kotlin
fun main() {
    val mrWhite = JSONObject()
    mrWhite.addElement("Name", JSONString("Walter White"))
    mrWhite.addElement("Age", JSONInt(49))
    mrWhite.addElement("Height", JSONDouble(1.80))
    mrWhite.addElement("Has brown eyes?", JSONBool(false))

    mrWhite.addObserver(object: JSONObjectObserver {
        override fun elementAdded(value: JSONElement) {
            println("Element was added!")
        }

        override fun elementRemoved(key: String) {
            println("Element with tag $key was removed!")
        }

        override fun elementReplaced(key: String, new: JSONElement) {
            println("Element with tag $key was changed to ${new}!")
        }

    })

    mrWhite.addElement("Alias", JSONNull)
    mrWhite.replaceElement("Age", JSONInt(50))
    mrWhite.deleteElement("Has brown eyes?")

    println("Final result:")
    println(mrWhite)
}
```

Output:
```console
Element was added!
Element with tag Age was changed to 50!
Element with tag Has brown eyes? was removed!
Final result:
{
	"Name": "Walter White",
	"Age": 50,
	"Height": 1.8,
	"Alias": null
}
```


## License
Free to use
