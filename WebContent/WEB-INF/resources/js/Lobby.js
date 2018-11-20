$(document).ready(function() {
		//$('#base').addClass($('#base').attr('value'));
});

function joinLobby(ev) {
	//ev.preventDefault();
	//ev.target.appendChild(document.getElementById(data));
	$.ajax({
		url: "join_lobby",
     	type: "POST",
		data: ({
			"lobby_name": document.getElementById("lobby_name_div").innerHTML
           }),
		success: function(resultData){
					console.log("join ok: "+resultData);
		          },
	    error : function(e) {
				       alert(e.responseText);
				       console.log("JOIN ERROR: ", e);
				}
			});		
	$("#lobbies_div").load(location.href+" #lobbies_div>*","");
}