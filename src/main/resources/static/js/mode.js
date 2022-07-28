///////////////////loading area////////////////////
///////////////////////////////////////////////////
window.addEventListener("DOMContentLodaded",()=>{
	consol.log("lodading complete");


	/**************************************/
	let dark = document.querySelector("#dark");
	let mode_change = document.querySelector(".mode_change");
	/**************************************/
	
	mode_change.onclick = ()=>{
		console.log("click click click");
		dark.classList.toggle("on");
	};


});////////////////loading area////////////////////
///////////////////////////////////////////////////


