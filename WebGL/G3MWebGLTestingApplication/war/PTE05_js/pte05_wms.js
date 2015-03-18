var myG3M;
var warWindow;

/* -------------------------
		Cosas de La Palma
	----------------------------	*/
	
	function loadingLaPalmaLayers(){
		$(".layerChecker").prop("checked",false);
		$("#lidarChecker").prop("checked",true);
		$("#pozosChecker").prop("checked",true);
	}
	
		/* ---------------------
		Cosas de paisajes
	* ---------------------- */

window.onload = function () {
	$("#ideContainer").css("height",$(window).height());
	$("#g3m_frame").css("height",$(window).height());
	
	$( "#globeMarkDialog").dialog({
		autoOpen: false,
		open: function(event,ui) { configureClosingX()},
		height: 300,
		width: 500
	});
	
	$("#paisajeFolder").click(function() {
		if ($("#paisajeContent").css("display") == "none") {
			$("#paisajeLock").attr("src","images/menos.gif");
			$("#paisajeContent").css("display","block");
		}
		else {
			$("#paisajeLock").attr("src","images/mas.gif");
			$("#paisajeContent").css("display","none");
		}
	});
	
	$("#AEFolder").click(function() {
		if ($("#AEContent").css("display") == "none") {
			$("#AELock").attr("src","images/menos.gif");
			$("#AEContent").css("display","block");			
		}
		else {
			$("#AELock").attr("src","images/mas.gif");
			$("#AEContent").css("display","none");
		}
	});
	
	$("#pte05Folder").click(function() {
		if ($("#pte05Content").css("display") == "none") {
			$("#pte05Lock").attr("src","images/menos.gif");
			$("#pte05Content").css("display","block");
		}
		else {
			$("#pte05Lock").attr("src","images/mas.gif");
			$("#pte05Content").css("display","none");
		}
	});
	
	$("#VegFolder").click(function() {
		if ($("#VegContent").css("display") == "none") {
			$("#VegLock").attr("src","images/menos.gif");
			$("#VegContent").css("display","block");
		}
		else {
			$("#VegLock").attr("src","images/mas.gif");
			$("#VegContent").css("display","none");
		}
	});
	
	$("#epFolder").click(function() {
		if ($("#epContent").css("display") == "none") {
			$("#epLock").attr("src","images/menos.gif");
			$("#epContent").css("display","block");
		}
		else {
			$("#epLock").attr("src","images/mas.gif");
			$("#epContent").css("display","none");
		}
	});

	var content = '<div style="padding-right:1em;"> <p style="padding-left: 1em; padding-top:1em;">Para manejar el globo se usan estos controles:</p><ul><li><p>Botón izquierdo apretado del ratón para trasladar el terreno</p></li><li><p>Boton izquierdo del ratón apretado + tecla Shift para abatir la cámara (moviendo ratón en vertical)</p></li><li><p>Boton izquierdo del ratón apretado + tecla Alt para orbitar alrededor de nuestra posición</p></li><li><p>Rueda del ratón para hacer zoom adelante y atrás</p></li><br></ul></div>';
	var title = 'Usando el globo';
	
	$("#globeMarkDialog").dialog("option", "title", title);
	$("#globeMarkDialog").html(content);
	$("#globeMarkDialog").dialog("open");
	
	warWindow = document.getElementById("g3m_frame").contentWindow;
	onLoadG3M();
};

function loadingDefaultLayers(){
	myG3M.addWMSLayer("http://ide2.idegrancanaria.es/wms/PTE_05?","O3_APN");
	changeAPNLegendStatus();
	myG3M.addWMSLayer("http://ide2.idegrancanaria.es/wms/PTE_05?","O3_APA");
	changeAPALegendStatus();
	myG3M.addWMSLayer("http://ide2.idegrancanaria.es/wms/PTE_05?","O3_APM");
	myG3M.addWMSLayer("http://ide2.idegrancanaria.es/wms/PTE_05?","O3_API");
}

function onLoadG3M() {
	var interval = setInterval(function() {
		try {
			if ((warWindow.hasOwnProperty('G3M')) && (warWindow.G3M != null)) {
				myG3M = warWindow.G3M;
				//Cargando capas de entrada, sólo y en exclusiva para este caso.
				//loadingDefaultLayers();
				loadingLaPalmaLayers();
				
				clearInterval(interval);
			}
		}
		catch (err) {console.log(err); myG3M = null;}
	},200);
}

window.onresize = function () {
	$("#ideContainer").css("height",$(window).height());
	$("#g3m_frame").css("height",$(window).height());
};

function configureClosingX (){
	$(".ui-widget-overlay").css('background', "none");
	$(".ui-dialog-titlebar-close").text("X");
	$(".ui-dialog-titlebar-close").css("color","#1c94c4");
	$(".ui-dialog-titlebar-close").css("font-size","12px");
}

function changeAPNLegendStatus(){
	if ($("#APNLegend").css('display') == "none") $("#APNLegend").css('display','block');
	else $("#APNLegend").css('display','none');
}
function changeAPALegendStatus(){
	if ($("#APALegend").css('display') == "none") $("#APALegend").css('display','block');
	else $("#APALegend").css('display','none');
}