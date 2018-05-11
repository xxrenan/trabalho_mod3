var myVar = setInterval(function(){ aleatorio() }, 3000);
function aleatorio() {
	document.getElementById("temp1").innerHTML = Math.floor(Math.random() * 100);
	document.getElementById("temp2").innerHTML = Math.floor(Math.random() * 100);
	document.getElementById("temp3").innerHTML = Math.floor(Math.random() * 100);
	atualiza_grid();
}

function atualiza_grid() {
	if (document.getElementById("temp1").innerHTML > "50")
	{
	document.getElementById("linha1").className = "success";
	document.getElementById("linha2").className = "danger";
	document.getElementById("linha3").className = "warning";
	}
else
	{
	document.getElementById("linha1").className = "warning";
	document.getElementById("linha2").className = "success";
	document.getElementById("linha3").className = "danger";
}}