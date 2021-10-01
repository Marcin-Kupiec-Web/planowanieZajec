/*
	$('[id="obecnoscPanelGridForm"]').selectable({
		
		filter:"tbody tr .sel_ect",
		selected: function(e,ui){
			selectSelected($(ui.selected));
			changeClassSelect($(ui.selected).find("select"),$(ui.selected).find("select").val());
			
		},
					unselected: function(e,ui){
						
		},
		stop:function(){	
		}
	});
	*/
	
	$(".resetButton").click(function(){
		   
			$(".renderText").fadeIn("slow");
			$(".renderInput").hide();
			$(".sel_ect").css({"background":"white"});
			$(".checkObTh").find("input").val("0");
	})
	
	$(".checkObTh").find("input").change(function(){
				var idAll=$(this).parent().parent().parent().parent().parent().parent().parent().find(".checkObTh").attr("id");
				var godzL=idAll.split(":");
				if($(this).val()==="1"){	
					$(".selCol"+godzL[1]).each(function(){
						selectSelected($(this));
						changeClassSelect($(this).find("select"),$(this).find("select").val());
					})
			}else {
					$(".selCol"+godzL[1]).find(".renderInput").hide();
					$(".selCol"+godzL[1]).find(".renderText").show();
					if($(this).val()==="2"){
					$(".selCol"+godzL[1]).not(".ui-widget-header").css({'background-color':'#eeeeee','opacity':'0.5'});
					$(".selCol"+godzL[1]).find(".renderInput").attr("readonly","true");
					}		else
						$(".selCol"+godzL[1]).not(".ui-widget-header").css({'background-color':'white','opacity':'1'});
					
					$(".selCol"+godzL[1]).each(function(){
								$(this).find("select").val($(this).find(".renderText").text());
							
					})
			}
	})
	
	$('[id="obecnoscPanelGridForm"]').find("select").change(function(){
		changeClassSelect($(this),$(this).val())
		 if($(".checkLaczWybor").find("input").is(":checked")==false){
				var idRow=$(this).parent().parent().parent().find("div").attr("class");
				var valu=$(this).val();
				$("."+idRow).each(function(){
							if($(this).find("select").css("display")!="none"){
								changeClassSelect($(this).find("select"),valu)
							$(this).find("select").val(valu);
							}
						})	
		 }
	})

	
		//$(".gridThObecnosc").selectable({disabled:true});
	
	
	function changeClassSelect(znacznik,valu){
				znacznik.removeClass("classSp")
				znacznik.removeClass("classOb")
				znacznik.removeClass("classNb")
				
				if(valu=="Sp"){
					znacznik.addClass("classSp");
				}
				else if(valu=="N")
					znacznik.addClass("classNb")
				else{
					znacznik.addClass("classOb")
				}
	}
	
	function selectSelected(select){
		//$(select).css({"background":" #e8f8f5!important"});
	
		var godzl=select.closest("td").index()-2;
	    var czyUsun=$(".checkObTh"+godzl).find("input").val();
	   
		    if(czyUsun!=2){
					select.find(".renderInput").toggle();
					select.find(".renderText").toggle();
				
					var wartosc=select.find("select").val();	
				
		
		    }
		
	}


