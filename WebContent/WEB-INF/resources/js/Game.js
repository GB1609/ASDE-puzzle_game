function allowDrop(ev) {
  if (!ev.target.hasChildNodes() &&
    ev.target.getAttribute("class") == "box_piece") {
    ev.preventDefault();
  }
}
var hintUsed=0;
function allowHint() {
	if(hitUsed<3)
	{
		hintUsed++;
		$('#show-modal').modal("toggle");
	}
}

function drag(ev) {
  ev.dataTransfer.setData("pieceMoved", ev.target.id);
  ev.dataTransfer.setData("old_location", ev.target.parentElement.parentElement.id);
  ev.dataTransfer.setData("old_position", $(ev.target.parentElement).prevAll(".box_piece").length);
}
document.addEventListener("dragenter", function (event) {
  if (!event.target.hasChildNodes() &&
    event.target.getAttribute("class") == "box_piece") {
    event.target.style.background = "#C37500";
  }
}, false);

document.addEventListener("dragleave", function (event) {
  if (!event.target.hasChildNodes() &&
    event.target.getAttribute("class") == "box_piece") {
    event.target.style.background = "";
  }
}, false);

function drop(ev) {
  ev.preventDefault();
  event.target.style.background = "";
  var data = ev.dataTransfer.getData("pieceMoved");
  var old = ev.dataTransfer.getData("old_location");
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
    success: function (resultData) {
      console.log("ok" + resultData);
    },
    error: function (e) {

      alert(
        "old position" + old_position + "\n" +
        "new_location   " + ev.target.parentElement.getAttribute("id") + "\n" +
        "old_location   " + old + "\n" +
        "piece  " + data + "\n" +
        "new_position   " + $(ev.target).prevAll(".box_piece").length
      );
      alert(e.responseText);

      console.log("ERROR: ", e);
    }
  });
}


$(document).ready(function () {
  var bar = new ProgressBar.SemiCircle(dynamic, {
    strokeWidth: 6,
    color: '#FFEA82',
    trailColor: '#eee',
    trailWidth: 1,
    easing: 'easeInOut',
    duration: 1400,
    svgStyle: null,
    text: {
      value: '',
      alignToBottom: false
    },
    from: {
      color: '#FFEA82'
    },
    to: {
      color: '#ED6A5A'
    },
    // Set default step function for all animate calls
    step: (state, bar) => {
      bar.path.setAttribute('stroke', state.color);
      var value = Math.round(bar.value() * 100);
      if (value === 0) {
        bar.setText('');
      } else {
        bar.setText(value);
      }

      bar.text.style.color = state.color;
    }
  });
  bar.text.style.fontSize = '2rem';
  bar.animate(0.7);
});