function ligaatuador() {
	document.getElementById("status1").innerHTML = "Ligado";
	document.getElementById("botao_liga").className = "btn btn-success disabled";
	document.getElementById("botao_desliga").className = "btn btn-danger";
	document.getElementById("linha2").className = "danger";
}

function desligaatuador() {
	document.getElementById("status1").innerHTML = "Desligado";
	document.getElementById("botao_desliga").className = "btn btn-danger disabled";
	document.getElementById("botao_liga").className = "btn btn-success";
	document.getElementById("linha2").className = "success";
}

function atua(){
if (document.getElementById("status2").innerHTML == "Ligado")
	{
	document.getElementById("status2").innerHTML = "Desligado";
	document.getElementById("botao_atua").innerHTML = "Liga";
	document.getElementById("botao_atua").className = "btn btn-success";
	}
else
	{
	document.getElementById("status2").innerHTML = "Ligado";
	document.getElementById("botao_atua").innerHTML = "Desliga";
	document.getElementById("botao_atua").className = "btn btn-danger";
	}
}