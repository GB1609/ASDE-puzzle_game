var overview, overviewDiv;
var settings, settingsDiv;
var matchHistory, matchHistoryDiv;
var showPasswordLabel;
var password;
var chooseAvatarDiv;
var chooseAvatarButton;

var statisticsDiv;
var statisticsButton;
var chartMatchDiv;

var newPassword, newFirstName, newLastName, repeatNewPassword;

$(document).ready(function () {
	overview = $('#overview-button');
	overviewDiv = $('#overview-div');
	overview.click(overviewClick);
	settings = $('#settings-button');
	settingsDiv = $('#settings-div');
	settings.click(settingsClick);
	matchHistory = $('li[class = matchHistory-button]');
	matchHistoryDiv = $('#matchHistory-div');
	matchHistory.click(matchClick);
	chooseAvatarDiv = $('#choose-avatar-div');
	chooseAvatarButton = $('#changeAvatar-button');
	chooseAvatarButton.click(avatarClick);
	statisticsButton = $('#matchStatistics-button');
	statisticsDiv = $('#matchStatistics-div');
	statisticsButton.click(statisticsClick);
	chartMatchDiv = $('#chart-match-div');

	showPasswordLabel = $('#showPassLabel');
	password = document.getElementById("password").value;
	getFields();
	google.charts.load('current', {
		'packages': ['corechart']
	});
	google.charts.setOnLoadCallback(drawChart);

	google.charts.load('current', {
		packages: ['corechart', 'line']
	});
	google.charts.setOnLoadCallback(drawCurveTypes);

});

function overviewClick(e) {
	hideAll();
	overviewDiv.toggleClass('hidden');
	overview.addClass('active');
	e.preventDefault();
}

function settingsClick(e) {
	hideAll();
	settings.addClass('active');
	settingsDiv.toggleClass('hidden');
	e.preventDefault();
}

function matchClick(e) {
	hideAll();
	matchHistory.addClass('active');
	matchHistoryDiv.toggleClass('hidden');
	e.preventDefault();
}

function avatarClick(e) {
	hideAll();
	chooseAvatarButton.addClass('active');
	chooseAvatarDiv.toggleClass('hidden');
	e.preventDefault();
}

function statisticsClick(e) {
	hideAll();
	statisticsButton.addClass('active');
	statisticsDiv.toggleClass('hidden');
	chartMatchDiv.toggleClass('hidden');
	e.preventDefault();
}

function hideAll() {
	overviewDiv.addClass('hidden');
	overview.removeClass('active');
	settingsDiv.addClass('hidden');
	settings.removeClass('active');
	matchHistoryDiv.addClass('hidden');
	matchHistory.removeClass('active');
	chooseAvatarDiv.addClass('hidden');
	chooseAvatarButton.removeClass('active');
	statisticsDiv.addClass('hidden');
	statisticsButton.removeClass('active');
	chartMatchDiv.addClass('hidden');

}

function selectAvatar(e) {
	document.getElementById("avatar").value = e.id;
	$('.avatar_grid p').removeClass("avatar_selected");
	var p = e.closest('p');
	$(p).addClass("avatar_selected");
	// $('#imageSection').addClass('no-visible');
}

function showPassword() {
	document.getElementById("showPassLabel").innerHTML = password;
}

function hidePassword() {
	document.getElementById("showPassLabel").innerHTML = "*******";
}

function checkPasswordField() {

	if (newPassword.value != "") {
		newPassword.classList.add("alert");
		newPassword.classList.add("alert-success");
		document.getElementById('reenter-password-div').classList
			.remove("hidden");
	} else {
		document.getElementById('reenter-password-div').classList.add("hidden");
		repeatNewPassword.value = "";
	}

	if (newPassword.value != "" && repeatNewPassword.value != "" &&
		newPassword.value == repeatNewPassword.value) {
		repeatNewPassword.classList.add("alert");
		repeatNewPassword.classList.add("alert-success");
		repeatNewPassword.classList.remove("alert-danger");

	} else if (newPassword.value != "" && repeatNewPassword.value != "" &&
		newPassword.value != repeatNewPassword.value) {
		repeatNewPassword.classList.add("alert");
		repeatNewPassword.classList.remove("alert-success");
		repeatNewPassword.classList.add("alert-danger");
	} else if (newPassword.value != "" && repeatNewPassword.value == "") {
		repeatNewPassword.classList.add("alert");
		repeatNewPassword.classList.remove("alert-success");
		repeatNewPassword.classList.add("alert-danger");
	}

}

function updateSettings() {

	checkPasswordField();
	if (newPassword.value != "" && repeatNewPassword.value != "" &&
		repeatNewPassword.value != newPassword.value) {
		swal("Sorry", "Passwords must be the same!!!", "error");

		return;
	}
	if (newFirstName.value == "" && newLastName.value == "" &&
		newPassword.value == "" && newAvatar.value == "") {
		swal("INFO", "There is nothing to update", "info");
		return;
	}
	update();

}

function getFields() {
	newFirstName = document.getElementById('firstname-field');
	newLastName = document.getElementById('lastname-field');
	newPassword = document.getElementById('password-field');
	repeatNewPassword = document.getElementById('reenter-password-field');
	newAvatar = document.getElementById('avatar');
}

function update() {

	$.ajax({
		url: "updateUserInformation",
		type: "POST",
		async: false,
		data: ({
			"firstname": newFirstName.value,
			"lastname": newLastName.value,
			"password": newPassword.value,
			"avatar": newAvatar.value
		}),
		success: function (data) {
			if (data.status == "success")
				swal("Congrats", "Your information were correctly updated!", "success");
			else
				swal("Sorry", "There was an error", "error");
			window.location.href = "userProfile";
		},
		error: function (data) {
			swal("Sorry", "There was an error", "error");

		}
	});

}

function drawChart() {

	var donutChart = $('#donutChart').val();
	var array = donutChart.split(/,/);
	var data = google.visualization.arrayToDataTable([
		['Match', 'number'],
		['Win', Number(array[0])],
		['Lose', Number(array[1])]
	]);

	var options = {
		pieHole: 1.5,
		pieSliceTextStyle: {
			color: 'black',
			fontSize: 18,
		},
		'width': '400',
		'height': '300',
		'margin': 0,
		'padding-left': 0,
		padding: '0',
		legend: 'none',
		is3D: true,
		colors: ['#00ff00', 'red']

	};

	var chart = new google.visualization.PieChart(document
		.getElementById('donut_single'));
	chart.draw(data, options);

}

function drawCurveTypes() {
	var data = new google.visualization.DataTable();
	data.addColumn('string', 'X');
	data.addColumn('number', 'Win');
	data.addColumn('number', 'Lose');

	var lineChart = $('#lineChart').val();
	var array = lineChart.split(/[,]+/);
	var count = 0;
	for (var i = 0; i < array.length; i += 3) {
		data.addRow([array[i], Number(array[i + 1]), Number(array[i + 2])]);
		count++;
	}
	// data.addRow([ '03/02', 2, 1 ]);
	// data.addRow([ '04/02', 8, 5 ]);
	// data.addRow([ '05/02', 10, 4 ]);
	// data.addRow([ '06/02', 25, 20 ]);
	// data.addRow([ '07/02', 7, 17 ]);
	// data.addRow([ '08/02', 40, 9 ]);
	// data.addRow([ '09/02', 100, 9 ]);
	// data.addRow([ '13/02', 2, 1 ]);
	// data.addRow([ '14/02', 8, 5 ]);
	// data.addRow([ '15/02', 10, 4 ]);
	// data.addRow([ '16/02', 25, 20 ]);
	// data.addRow([ '17/02', 7, 17 ]);
	// data.addRow([ '18/02', 40, 9 ]);
	// data.addRow([ '19/02', 100, 9 ]);

	var options = {
		color: 'white',
		'width': (count < 3) ? 300 : count * 100,
		'height': '200',
		explorer: {
			// axis : 'horizontal',
			axis: 'vertical',
			keepInBounds: true,
		},
		hAxis: {
			title: 'date',
			titleTextStyle: {
				color: 'white'
			},
			textStyle: {
				color: 'white'
			}
		},
		vAxis: {
			title: 'Matches frequency',
			titleTextStyle: {
				color: 'white'
			},
			textStyle: {
				color: 'white'
			}
		},
		pointsVisible: true,
		series: {
			1: {
				curveType: 'function'
			}
		},
		legend: {
			textStyle: {
				color: 'white'
			}

		}
	};

	var chart = new google.visualization.LineChart(document
		.getElementById('chart_div'));
	chart.draw(data, options);
}