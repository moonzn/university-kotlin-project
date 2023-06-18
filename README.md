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
    val id = JSONNull
    val hasBrownEyes = JSONBool(false)

    mrWhite.addElement("Name", name)
    mrWhite.addElement("Age", age)
    mrWhite.addElement("Height", height)
    mrWhite.addElement("Id", id)
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
	"Id": null,
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

<br>

The same applies to the *deleteElement* function.


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

## Conversions to JSON elements

## License
Free to use
