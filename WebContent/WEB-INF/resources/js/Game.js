function allowDrop(ev) {
		if (!ev.target.hasChildNodes()
				&& ev.target.getAttribute("class") == "box_piece") {
			ev.preventDefault();
		}
	}

	function swapElements(obj1, obj2) {
    // save the location of obj2
    var parent2 = obj2.parentNode;
    var next2 = obj2.nextSibling;
    // special case for obj1 is the next sibling of obj2
    if (next2 === obj1) {
        // just put obj1 before obj2
        parent2.insertBefore(obj1, obj2);
    } else {
        // insert obj2 right before obj1
        obj1.parentNode.insertBefore(obj2, obj1);

        // now insert obj1 where obj2 was
        if (next2) {
            // if there was an element after obj2, then insert obj1 right before that
            parent2.insertBefore(obj1, next2);
        } else {
            // otherwise, just append as last child
            parent2.appendChild(obj1);
        }
    }
}
	function drag(ev) {
		ev.dataTransfer.setData("pieceMoved", ev.target.id);
		ev.dataTransfer.setData("old_location", ev.target.parentElement.parentElement.id);
		ev.dataTransfer.setData("old_position",$(ev.target.parentElement).prevAll(".box_piece").length);
		}

	function drop(ev) {
		ev.preventDefault();
		var data = ev.dataTransfer.getData("pieceMoved");
		var old=ev.dataTransfer.getData("old_location");
		var old_position = ev.dataTransfer.getData("old_position");
		ev.target.appendChild(document.getElementById(data));
		$.ajax({
			url: "move_piece",
	     	type: "POST",
			data: ({
				"old_location": old,
				"old_position": old_position,
				"new_location": ev.target.parentElement.getAttribute("id"),
				"new_position": $(ev.target).prevAll(".box_piece").length,
				"piece": data
	           }),
			success: function(resultData){
						console.log("ok"+resultData);
			          },
		    error : function(e) {

					 	  alert(
					 			 "old position"+old_position+"\n"+
								"new_location   "+ev.target.parentElement.getAttribute("id")+"\n"+
								"old_location   "+ old+"\n"+
								"piece  "+ data+"\n"+
								"new_position   "+$(ev.target).prevAll(".box_piece").length
					         );
					       alert(e.responseText);

					console.log("ERROR: ", e);
					}
				});

	}


	$(document).ready(function() {
		//$('#base').addClass($('#base').attr('value'));
	});
