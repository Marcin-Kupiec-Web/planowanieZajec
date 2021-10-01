

function customExtender(){

	this.cfg.seriesColors=['#73b578','#e7e8b7','#e9c18d','#00ffff','#5D99C3','#ff0000','#800040','#ff0000','#8080c0','#ff8000','#b4b467','#5D99C3','#ff0000','#00ffff','#008000','#ff80ff','#804040','#f2f200','#808080'];
	this.cfg.grid={
		background:'transparent',
		borderColor:'#d8d8d8',
		borderWidth:"0",
		shadow:false
	};

}

$(document).ready(function(){
	$(window).resize(function(){
		PF('dch').trigger('configure',{"width":90});
	})
})