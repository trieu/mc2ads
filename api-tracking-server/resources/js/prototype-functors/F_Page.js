var F_Page = {};

F_Page.model = function(){
    this.title = '';
    this.description = '';
    this.tags = [];
    this.url = '';
}

// TODO process how this functor: present information

F_Page.view = function(){

}

// TODO process how this functor: find related functor (simple predicate here)

F_Page.relation = function(){

}

jQuery(document).ready(function(){
		
	var ft = new F_Page.model();
	ft.title = document.title;
		
	var list = document.getElementsByTagName('meta');
	var size = list.length;
	for ( var i = 0; i < size; i++) {
	     var meta = list[i];
	     var metaname = meta.name;
	     if (metaname) {
	         if (metaname === 'description') {                   
	             if (meta.content) {
	            	 ft.description = (meta.content);
	             }
	         }
	     }
	}	
	
	ft.url = location.href;	
	
	//jQuery.data(document.body, 'functors', {'f_Page': (ft) } );
	jQuery("body").attr('functors', JSON.stringify( { F_Page :ft } ) );
});

