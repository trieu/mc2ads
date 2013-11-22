
output = input + " Hello by output ,"


Calendar can = Calendar.getInstance(); 
can.setTime(new Date())

hour = can.get(Calendar.HOUR_OF_DAY)
minute = can.get(Calendar.MINUTE)
second = can.get(Calendar.SECOND)

output = output + " ${hour}:${minute}:${second} !"
