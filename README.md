# JSON Editor

university-kotlin-project is a Kotlin library that handles the creation and manipulation of models that specifically represent JSON. Using these models, we define that a JSON hierarchy is represented through a group of **JSON elements**, where...

| JSONElement  | Representation of  |
|---|---|
| JSONString  |  String / Enum instance |
| JSONInt  | Integer |
| JSONDouble | Double |
| JSONBool | Boolean |
| JSONNull | null |
| JSONArray | Collection |
| JSONObject | Data class / Map |

With this package you will be able to:
- Create your own JSON hierarchies
- Manipulate JSON elements
- Make basic JSON searches
- Convert collections, data classes, primitive types into JSON elements
- Output and visualize your hierarchies in String format

## Dependencies
```kotlin
dependencies {
    implementation 'org.example:university-kotlin-project:1.0-SNAPSHOT'
}
```

## Usage

### Create your own JSON elements
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

JSON strings are not the only type you can create. You may create ```JSONElement```s for **integers**, **doubles**, **null** values and **booleans** as well:

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


It is great being able to instantiate a bunch of JSON variables, but how do we couple them together into an actual JSON hierarchy?
We can do that through... ```JSONObject```s and ```JSONArray```s! Let's take a look.


#### Create an object
Create a profile for Mr. White, through a ```JSONObject```:

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

#### Create an array
We may also create a list of any ```JSONElement```s we want, for example, a list of jobs:

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

We may manipulate any element of an existent ```JSONObject``` or ```JSONArray```, through the ```replaceElement``` function, providing the key and the index of the element we want to change, respectively.

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
Everything described in the modification of ```JSONElement```s applies to the ```deleteElement()``` function.


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


### Searches and validations


#### Search for elements with key X

To get all ```JSONElement```s associated with a tag *key*, use the ```GetJSONElementsVisitor(key)``` class.

To start the search, just call the ```accept()``` function. In the end there will be a list will all the matches.

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
The ```GetJSONObjectsVisitor(containsKeys)``` class will search for every ```JSONObject``` that contains all the tags specified in the *containsKeys* argument.

To start the search, just call the ```accept()``` function. In the end there will be a list will all the matches.

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

To check a specific tag types, use the ```VerifyJSONElementTypeVisitor(key, clazz)``` class: it will search for every *key* and check its value class. The value class must be the same as the *clazz* argument provided.

To start the verification, just call the ```accept()``` function. In the end it will provide a boolean value stating if the integrity is valid or not, and a list of offenders (list of ```JSONObject``` that did not follow the structure).

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

#### Verify if all the objects within an array have the same structure
This is accomplished through the ```VerifyJSONObjectsStructureVisitor(key, structure)``` class: it will check if the given *structure* is respected in every ```JSONObject``` inside the ```JSONArray``` specified by the tag *key*.

To start the verification, just call the ```accept()``` function. In the end it will provide a boolean value stating if the integrity is valid or not, and a list of offenders (list of ```JSONObject``` that did not follow the structure).

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

## Convert to JSON element
Every conversion will be made through the ```JSONGenerator().generate(initiator)``` function where the initiator argument corresponds to the data class/collection/map/type... etc you want to convert.

### Data class to JSONElement
Always converted to ```JSONObject```.

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
Always converted to ```JSONArray```.

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
Always converted to ```JSONObject```.
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
An enumerator instance will be always converted to ```JSONString```.

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

### Annotations

You may customize the JSON output of the data classes you will convert to JSON. 
There are three available annotations:

- ```@ForceString``` will force the attribute to be outputed as ```JSONString```
- ```@Identifier(identifier)``` will use the identifier as the tag name
- ```@Exclude``` excludes the attribute from the output


```kotlin
enum class Subject {
    Chemistry, Science
}

fun main() {
    data class Student(
        @ForceString
        val number: Int,
        @Identifier("student name")
        val name: String,
        @Exclude
        val subject: Subject
    )
    println(JSONGenerator().generate(initiator = Student(10, "Jesse Pinkman", Subject.Chemistry)))
}
```

Output:
```console
{
	"number": "10",
	"student name": "Jesse Pinkman"
}
```


## Monitor changes through Observers
If you need to trigger a specific operation everytime a JSON element gets changed/replaced, you can do it through ```JSONArrayObserver``` and ```JSONObjectObserver``` observers. 

**Note:** This is only applies to ```JSONArray``` and ```JSONObject``` as the changes will occur within those elements.

### JSONArray
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

### JSONObject
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
