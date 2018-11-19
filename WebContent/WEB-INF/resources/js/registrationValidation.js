/*$(document).ready(new function() {
	console.log("serafino", $('#signup-username'));
	$('#signup-username').keyup(function (elem){
		console.log("sdsgdsdf", elem);
		checkUsername(elem);
	});
});*/

function validation() {
	var loginForm = $('#login-form'), loginemail = $('#signin-email'), loginPass = $('#signin-password');

	console.log(loginemail.val());
	console.log(loginPass.val());

	// controlla nome utente e password
}

function registration() {
	var newEmail = $('#signup-email'), newUser = $('#signup-username'), newPassword = $('#signup-password'), form = $('#reg_form');
	console.log("nuovo utente ", newUser.val());
	console.log("nuova email ", newEmail.val());
	console.log("nuova password ", newPassword.val());
	// se i dati inseriti sono validi registra il nuovo utente
	$.ajax({
		url : "AddUser",
		method : "post",
		data : {
			'user' : newUser.val(),
			'email' : newEmail.val(),
			'password' : newPassword.val()
		},
		success : function(data, textStatus, jqXHR) {
			console.log("ajax success");
			// console.log(data);
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log("ajax error");
		}

	});

	/*
	 * console.log(newEmail.val()); console.log(newUser.val());
	 * console.log(newPassword.val()); console.log(form.val());
	 * console.log(form.attr('action')); console.log(form.attr('method'));
	 */
	// form.submit();
}

function checkUsername() {
	// console.log("execute check username");
	var input = $('#signup-username');
	input.removeClass("form-valid");
	input.removeClass("form-invalid");
	if (input.val() != "") {

		$.ajax({
			url : "Validator",
			method : "post",
			data : {
				'newUser' : input.val()
			},
			success : function(data, textStatus, jqXHR) {
				// console.log("check username success");
				// console.log("textstatus " + textStatus);
				// console.log("data " + data);

				if (data == "OK") {
					input.addClass("form-valid");
				} else {
					input.addClass("form-invalid");
				}

			},
			error : function() {
				console.log("ajax error");
			}
		});
	} else {
		input.removeClass("form-valid");
		input.removeClass("form-invalid");
	}
}

function checkEmail() {
	var input = $('#signup-email');
	input.removeClass("form-valid");
	input.removeClass("form-invalid");
	if (input.val() != "") {

		if (emailRegex(input.val())) {
			$.ajax({
				url : "Validator",
				method : "post",
				data : {
					'newEmail' : input.val()
				},
				success : function(data, textStatus, jqXHR) {
					// console.log("check username success");
					// console.log("textstatus " + textStatus);
					// console.log("data " + data);

					if (data == "OK") {
						input.addClass("form-valid");
					} else {
						input.addClass("form-invalid");
					}

					// $('#signup-username').next().toggleClass('is-visible');
					// $('#signup-username').next().css('background-color','green');
				},
				error : function() {
					console.log("ajax error");
				}
			});
		} else {
			console.log("not an email");
		}
	} else {
		input.removeClass("form-valid");
		input.removeClass("form-invalid");
	}
}

function emailRegex(email) {
	var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	return re.test(email);
}
