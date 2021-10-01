

	function myFunction() {
		  var x = document.getElementById("myTopnav");
		  if (x.className.search("responsive") ===-1) {
		    x.className += " responsive";
		  } else {
		    x.className = x.className.replace("responsive","");
		  }
		}  
		