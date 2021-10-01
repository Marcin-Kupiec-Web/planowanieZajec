$(document).ready(function(){
	var wpisyGodziny={};
	var wpisyGodzinyOut={};
	var godz,idSluchacz,toJson;
	$(".checkboxObecnoscGodz").change('click',function(){	
	
		
	godz=$(this).attr("godz");
	var godztd=$("[godzTd="+godz+"]");
	if(!$(this).is(":checked"))
	{	
		wpisyGodziny={};
		godztd.find(".wpisane").show();
		godztd.find(".wpisObecnoscTdSelect").hide();	
		$(".reset").css({"display":"none"});
		godztd.removeClass("selectedShow");
		}
	else{		
		godztd.find(".wpisane").hide();
		godztd.find(".wpisObecnoscTdSelect").show();	
		$(".reset").css({"display":"inline-block"});
		godztd.addClass("selectedShow");
	}
})

		$(".tableObecnosc").selectable({
				filter:".selected, td",
				selected: function(e,ui){
					var select=$(ui.selected);
					select.find(".wpisane").hide();
					select.find(".wpisObecnoscTdSelect").show().addClass("selectedSave");						
					if(select.attr("idSluchacz")!=null)
					$(".reset").css({"display":"inline-block"});
					select.addClass("selectedShow");
					$(".selectedSave:visible").change(function(){						
						var idS=$(this).parent().attr("idSluchacz");						
						$("[idSluchacz="+idS+"]").find(".selectedSave").val($(this).val());			
					})	
				},
				unselected: function(e,ui){
				},
				stop:function(){	
				}
			})
 
		
	$(".reset").click(function(){	
		wpisyGodziny={};
		
		$(".wpisObecnoscTdSelect:visible").each(function(){	
			$(this).parent().find(".wpisane").show();
			$(this).hide();
			$(this).parent().removeClass("selectedShow");		
			if($(this).parent().attr("poprzedniWpis")!="brak")
				niezmieniam=$(this).parent().attr("poprzedniWpis");
			else
				niezmieniam="Ob";
			$(this).val(niezmieniam);
		})
		
		$(".reset").css({"display":"none"});		
			
		$(".checkboxObecnoscGodz").prop("checked",false);
		toJson=null;
		wpisyGodziny={};
		wpisSzczegol={};	
		wpisy={};
	})
	
	
	
	$("#zapiszObecnosc").click(function(){		
		var wpisSzczegol={};	
		var wpisy={};
		var wpis;
		var poprzedniWpis;	
		var idPoprzedniWpis;
		var idSluchacz;
		var godz;
		$(".wpisObecnoscTdSelect:visible").each(function(){			
			wpisSzczegol={};	
			wpisy={};
			poprzedniWpis=null;
			idPoprzedniWpis=null;			
			poprzedniWpis=$(this).parent().attr("poprzedniWpis");		
			 	
			wpis=$(this).val();				
			
			if(wpis!=null && poprzedniWpis!=wpis){
			wpisSzczegol["wpis"]=wpis;
			wpisSzczegol["poprzedniWpis"]=poprzedniWpis;
			idPoprzedniWpis=$(this).parent().attr("idWpis");
			wpisSzczegol["poprzedniWpisId"]=idPoprzedniWpis;
			godz=$(this).parent().attr("godzTd");
			wpisSzczegol["data"]=$("#datepicker").val();
			idSluchacz=$(this).parent().attr("idSluchacz");
	
			wpisy[idSluchacz]=wpisSzczegol;	
			wpisyGodziny[godz+"#"+idSluchacz]=wpisy;
	
			}
		})
		
		
		$(function(){$(".content").LoadingOverlay("show",{fade:false});})
			
		toJson=JSON.stringify(wpisyGodziny); 
		
		if(toJson.length!=0){
		$("input[name='przekazDane']").val(toJson); 
		$("#przekazDane").submit();
		
	}				
})
})

