$(document).ready(function(){
var resize = function() {
  var screenWidth = parseInt($('body').width()), 
  mySchedule = document.getElementById("scheduleTerm");
  
  if (screenWidth < 600) {
	alert(document.getElementById("scheduleTerm").getAttributeNames);
	  mySchedule.setAttribute("rightHeaderTemplate", "agendaDay");
  }
}

$( window ).resize(function() {
  resize();
});

$( document ).ready(function() {
  resize();
});
})
