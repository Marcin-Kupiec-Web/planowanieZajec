function fakePassw(pass){
	   $(pass).val("");
	    input = "";
	    $(".hiddenId").val(input);
	    
	    $(pass).off().on("keypress", function(event) {
	    event.preventDefault();

	    if (event.key !== "Enter" && event.key.match(/^[0-9a-z!@#\$%&*-_]/)) {
	        $(this).val( $(this).val() + "•" );
	        input += event.key;
	        $(".hiddenId").val(input);
	       
	    }
	   

	}).on("keyup", function(event) {
	    if (event.key == "Backspace") {
	        var length = $(this).val().length - 1 > 0 ? $(this).val().length : 0;
	        input = input.substring(0, length);
	        $(".hasloReal").val(input);
	    }
	});
};