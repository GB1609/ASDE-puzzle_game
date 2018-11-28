var overview, overviewDiv;
var settings, settingsDiv;
var matchHistory, matchHistoryDiv;
var showPasswordLabel;
var password;
var chooseAvatarDiv;
var chooseAvatarButton;


var newPassword, newFirstName, newLastName, repeatNewPassword;

$(document).ready(function() {
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

	showPasswordLabel = $('#showPassLabel');
	password = document.getElementById("password").value;
	getFields();

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

function hideAll() {
	overviewDiv.addClass('hidden');
	overview.removeClass('active');
	settingsDiv.addClass('hidden');
	settings.removeClass('active');
	matchHistoryDiv.addClass('hidden');
	matchHistory.removeClass('active');
	chooseAvatarDiv.addClass('hidden');
	chooseAvatarButton.removeClass('active');

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

	if (newPassword.value != "" && repeatNewPassword.value != ""
			&& newPassword.value == repeatNewPassword.value) {
		repeatNewPassword.classList.add("alert");
		repeatNewPassword.classList.add("alert-success");
		repeatNewPassword.classList.remove("alert-danger");

	} else if (newPassword.value != "" && repeatNewPassword.value != ""
			&& newPassword.value != repeatNewPassword.value) {
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
	if (newPassword.value != "" && repeatNewPassword.value != ""
			&& repeatNewPassword.value != newPassword.value) {
		alert("Passwords must be the same!!!");
		return;
	}
	if (newFirstName.value == "" && newLastName.value == ""
			&& newPassword.value == "" && newAvatar.value=="") {
		alert("Nothing to update!!!");
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
function openAvatarGrid() {

}

function update() {

	$.ajax({
		url : "updateUserInformation",
		type : "POST",
		async : false,
		data : ({
			"firstname" : newFirstName.value,
			"lastname" : newLastName.value,
			"password" : newPassword.value,
			"avatar": newAvatar.value
		}),
		success : function(data) {
			if (data.status == "success")
				alert("Information correctly updated.");
			else
				alert("Something goes wrong!!!");

			window.location.href = "userProfile";
		},
		error : function(data) {
			alert("Something goes wrong!!!")

		}
	});

}
