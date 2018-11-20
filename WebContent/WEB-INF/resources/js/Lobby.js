$(document).ready(function() {
		//$('#base').addClass($('#base').attr('value'));
});

function joinLobby(ev) {
	//ev.preventDefault();
	//ev.target.appendChild(document.getElementById(data));
	var lobby_name = $(document.activeElement).closest('#lobby_row').children('#lobby_name_div').text();
	console.log();
	$.ajax({
		url: "join_lobby",
     	type: "POST",
		data: ({
			"lobby_name": lobby_name
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