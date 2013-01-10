function simpleAuthCheck(){
	//stupid check, add security later
	var name=prompt("Please enter the code","");
	if (name === "GreengarMail@2012" )	{
	  	$("#theBodyNode").slideDown();	  	
	} else {
		document.write("Wrong pass code!");
		//setTimeout(location.reload, 1000);
	}
}

jQuery(document).ready(function(){
	initLeftMenu();
	getTotalContacts();
	getTotalSavedContacts();	
	simpleAuthCheck();
});