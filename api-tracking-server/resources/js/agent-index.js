function AgentHelloA() {
	var name = "my name AgentHelloA";
	this.model = {
		message : "tôi là A, xin chào bạn! "
	};
	this.sayHi = function() // Define Method
	{
		return this.model.message;
	}
	this.getName = function() // Define Method
	{
		return name;
	}
}

function AgentHelloB() {
	var name = "my name AgentHelloB";
	this.model = {
		message : "tôi là B, xin chào bạn! "
	};
	this.sayHi = function() // Define Method
	{
		return this.model.message;
	}
	this.getName = function() // Define Method
	{
		return name;
	}
}

var crossHttpPost = function(postUrl, data , callback ) {
	var theIframeId = '__handler_' + (new Date().getTime());
	if (jQuery('#'+theIframeId).length === 0) {
		var targetIframe = jQuery("<iframe/>").attr({
			'style' : 'display:none',
			'id' : theIframeId
		});
		jQuery('body').append(targetIframe);
	}

	var form = jQuery("<form/>").attr({
		'method' : 'POST',
		'action' : postUrl,
		'target' : theIframeId
	});

	var field;
	for(var k in data){
		field = jQuery("<input/>").attr({
			'type' : 'hidden',
			'name' : k,
			'value' : encodeURIComponent(data[k])
		});
		form.append(field);
	}
	
	jQuery('body').append(form);
	form.submit();
}

var liveDataStatus = true;
function requestLiveDataStatus(){
	jQuery.getScript("http://localhost:10001/?keep-alive=true&keep-time=2500", function() {
		if(liveDataStatus){
			requestLiveDataStatus();	
		}
	});
}


var a = new AgentHelloA();
a.model.message = "I'm a instance of AgentHelloA";
a.prototype.newFunction = function(){
	alert('hi');
}

var postData = {'agent-class': AgentHelloA.toString() , 'agent-instance' : JSON.stringify(a) };
//crossHttpPost("http://localhost:10001/notify/postAgent/postmessage", postData , function(rs){ console.log(rs); });

//postMessage HTML5
window.addEventListener("message", function(event) {	
	if (event.origin !== "http://localhost:10001")
		return;	
	if(event.data)
		//requestLiveDataStatus();
	console.log("crossHttpPostCallback: "+ event.data);
}, false);


var functorsCallback = function(obj){
    console.log(obj);
};
