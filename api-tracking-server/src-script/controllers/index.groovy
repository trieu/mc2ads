
import groovyx.gpars.GParsPool;



import java.util.concurrent.atomic.AtomicInteger;

def s = "";

def getList() {
	def list = []
	list << "element 1"
	list << "element 2"
	list << "element 3"
	list << "element 4"
	list << "element 5"
}

getList().each({ el ->
	s += ( el.toUpperCase() + "<br>");
})


def myNumbers = 0;
GParsPool.withPool {
     final AtomicInteger result = new AtomicInteger(0)
     [1, 2, 3, 4, 5].eachParallel {result.addAndGet(it)}
     assert 15 == result
	 myNumbers = [1, 2, 3, 4, 5,6,7,8,9].parallel.reduce {a, b -> a + b}
 }
output = s + params['p']?.getAt(0) + params['b']?.getAt(0) + "<br>myNumbers: "+ myNumbers;
