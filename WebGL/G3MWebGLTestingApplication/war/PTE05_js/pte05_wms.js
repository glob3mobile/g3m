window.onload = function () {
	$("#g3mWidgetHolder").css("height",$(window).height());
	$("#activationContainer").css("height",$(window).height());
	
	$( "#globeMarkDialog").dialog({
		autoOpen: false,
		open: function(event,ui) { configureClosingX()},
		height: 300,
		width: 500
	});
	
	
	var content = '<div style="padding-right:1em;"> <p style="padding-left: 1em; padding-top:1em;">Para manejar el globo se usan estos controles:</p><ul><li><p>Botón izquierdo apretado del ratón para trasladar el terreno</p></li><li><p>Boton izquierdo del ratón apretado + tecla Shift para abatir la cámara (moviendo ratón en vertical)</p></li><li><p>Boton izquierdo del ratón apretado + tecla Alt para orbitar alrededor de nuestra posición</p></li><li><p>Rueda del ratón para hacer zoom adelante y atrás</p></li><br></ul></div>';
	var title = 'Usando el globo';
	
	$("#globeMarkDialog").dialog("option", "title", title);
	$("#globeMarkDialog").html(content);
	$("#globeMarkDialog").dialog("open");
	
	
};

window.onresize = function () {
	$("#g3mWidgetHolder").css("height",$(window).height());
	$("#activationContainer").css("height",$(window).height());
};

function configureClosingX (){
	$(".ui-widget-overlay").css('background', "none");
	$(".ui-dialog-titlebar-close").text("X");
	$(".ui-dialog-titlebar-close").css("color","#1c94c4");
	$(".ui-dialog-titlebar-close").css("font-size","12px");
}