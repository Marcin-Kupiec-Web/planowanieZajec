
function myFunction() {
  var x = document.getElementById("myTopnav");
  if (x.className === "topnav") {
    x.className += " responsive";
      document.getElementById("mySidebar").className = "sidebar";
  } else {
    x.className = "topnav";
  }
}



function closeOpenNav() {
	if (document.getElementById("mySidebar").style.width==""){
	if(window.innerHeight>600){
		document.getElementById("mySidebar").style.width="250px"
	}else{
		document.getElementById("mySidebar").style.width="0px"
	}
	}
	
	if(document.getElementById("mySidebar").style.width=="250px"){
			  document.getElementById("mySidebar").style.width = "0";
			  document.getElementById("content").style.marginLeft= "0";
}else{
			  document.getElementById("mySidebar").style.width = "250px";
	//if(window.innerHeight>600)
		      document.getElementById("content").style.marginLeft = "250px";
}
}