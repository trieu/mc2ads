

class Greet {

	def name
	Greet(who) { name = who[0].toUpperCase() +
						who[1..-1] }
	def salute() {
		println "222 Hello $name!"
		return "Hello $name is capitalized!"
	}
	def capitalize() {
		println(("Hello $name is capitalized mmmm!"))
		return "Hello $name is capitalized!"
	}
}
g = new Greet('world')  // create object
g.salute()               // output "Hello World!"
g.capitalize()               // output "Hello World!"

output = "Hello by outputmmm, ${input}!"

def getList() {
	def list = []
	list << "element 1"
	list << "element 2"
	list << "element 3"
}

getList().each({ el ->
	println el.toUpperCase()
})
//println getList()[0].toUpperCase()

new File("D:/" ).eachFile{file ->
	println file
}


