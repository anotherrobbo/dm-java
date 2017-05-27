(function($){  
	var methods = {
		// Initialise plugin.
		init: function() {
		    $('#search-form').submit(function(event) {
		        methods.go();
                event.preventDefault();
		    });
		},
        
        go: function() {
            var system = $('#system').val();
            var name = $('#name').val();
            var un2 = $('#un2').val();
			window.location = rootUrl + '/match/' + system + '/' + name + '/' + un2;
		}
	};
	
	$(document).ready(function(){
		methods.init();
	});
	
})(jQuery);