jQuery(document)
		.ready(
				function($) {
					var imageSection = $('#imageSection');
					var formModal = $('.cd-user-modal'), formLogin = formModal
							.find('#cd-login'), formSignup = formModal
							.find('#cd-signup'), formForgotPassword = formModal
							.find('#cd-reset-password'), formModalTab = $('.cd-switcher'), tabLogin = formModalTab
							.children('li').eq(0).children('a'), tabSignup = formModalTab
							.children('li').eq(1).children('a'), forgotPasswordLink = formLogin
							.find('.cd-form-bottom-message a'), backToLoginLink = formForgotPassword
							.find('.cd-form-bottom-message a'), mainNav = $('#signlog');

					// formRegistration = formSignup.find('#reg_form');
					// formRegistration.submit(check);
					// mainNav.on('click', '.cd-signup', signup_selected);
					imageSection.addClass('no-visible');
					var creationFailed = document.getElementById("failed").value;
					var loginFailed = document.getElementById("failedLogin").value;
					login_selected();

					if (creationFailed != "") {
						signup_selected();
						alert(creationFailed);
					}
					if (loginFailed != "")
						alert(loginFailed);

					// open modal
					mainNav.on('click', function(event) {
						$(event.target).is(mainNav)
								&& mainNav.children('ul').toggleClass(
										'is-visible');
					});

					// open sign-up form
					mainNav.on('click', '.cd-signup', signup_selected);
					// open login-form form
					mainNav.on('click', '.cd-signin', login_selected);
					mainNav.on('click', '.cd-login', login_selected);

					// switch from a tab to another
					formModalTab.on('click', function(event) {
						event.preventDefault();
						($(event.target).is(tabLogin)) ? login_selected()
								: signup_selected();
					});

					// hide or show password
					$('.hide-password')
							.on(
									'click',
									function() {
										var togglePass = $(this), passwordField = togglePass
												.prev('input');

										('password' == passwordField
												.attr('type')) ? passwordField
												.attr('type', 'text')
												: passwordField.attr('type',
														'password');
										('Hide' == togglePass.text()) ? togglePass
												.text('Show')
												: togglePass.text('Hide');
										// focus and move cursor to the end of
										// input field
										passwordField.putCursorAtEnd();
									});

					// show forgot-password form
					forgotPasswordLink.on('click', function(event) {
						event.preventDefault();
						forgot_password_selected();
					});

					// back to login from the forgot-password form
					backToLoginLink.on('click', function(event) {
						event.preventDefault();
						login_selected();
					});

					function login_selected() {
						mainNav.children('ul').removeClass('is-visible');
						formModal.addClass('is-visible');
						formLogin.addClass('is-selected');
						formSignup.removeClass('is-selected');
						formForgotPassword.removeClass('is-selected');
						tabLogin.addClass('selected');
						tabSignup.removeClass('selected');

					}

					function signup_selected() {
						mainNav.children('ul').removeClass('is-visible');
						formModal.addClass('is-visible');
						formLogin.removeClass('is-selected');
						formSignup.addClass('is-selected');
						formForgotPassword.removeClass('is-selected');
						tabLogin.removeClass('selected');
						tabSignup.addClass('selected');


					}

					function forgot_password_selected() {
						formLogin.removeClass('is-selected');
						formSignup.removeClass('is-selected');
						formForgotPassword.addClass('is-selected');
					}

					// IE9 placeholder fallback
					// credits
					// http://www.hagenburger.net/BLOG/HTML5-Input-Placeholder-Fix-With-jQuery.html
					if (!Modernizr.input.placeholder) {
						$('[placeholder]').focus(function() {
							var input = $(this);
							if (input.val() == input.attr('placeholder')) {
								input.val('');
							}
						}).blur(
								function() {
									var input = $(this);
									if (input.val() == ''
											|| input.val() == input
													.attr('placeholder')) {
										input.val(input.attr('placeholder'));
									}
								}).blur();
						$('[placeholder]').parents('form').submit(function() {
							$(this).find('[placeholder]').each(function() {
								var input = $(this);
								if (input.val() == input.attr('placeholder')) {
									input.val('');
								}
							})
						});
					}

				});

function selectAvatar(e) {
	document.getElementById("avatar").value = e.id;
	$('.avatar_grid p').removeClass("avatar_selected");
	var p = e.closest('p');
	$(p).addClass("avatar_selected");
//	$('#imageSection').addClass('no-visible');
}

function checkSignupFields() {
	var check = true;
	var signupFirstName = $('#signup-firstname');
	var signupLastName = $('#signup-lastname');
	var signupUsername = $('#signup-username');
	var signupPassword = $('#signup-password');
	if (signupFirstName.val() == "") {
		fieldEmptyError(signupFirstName.next('span'));
		check = false;
	} else {
		fieldValid(signupFirstName.next('span'));
	}
	if (signupLastName.val() == "") {
		fieldEmptyError(signupLastName.next('span'));
		check = false;
	} else {
		fieldValid(signupLastName.next('span'));
	}
	if (signupUsername.val() == "") {
		fieldEmptyError(signupUsername.next('span'));
		check = false;
	} else {
		fieldValid(signupUsername.next('span'));
	}
	if (signupPassword.val() == "") {
		fieldEmptyError(signupPassword.next('a').next('span'));
		check = false;
	} else {
		fieldValid(signupPassword.next('a').next('span'));
	}
	return check;
}
function checkSigninFields() {
	var check = true;

	var signinUsername = $('#signin-username');
	var signinPassword = $('#signin-password');

	if (signinUsername.val() == "") {
		fieldEmptyError(signinUsername.next('span'));
		check = false;
	} else {
		fieldValid(signinUsername.next('span'));
	}
	if (signinPassword.val() == "") {
		fieldEmptyError(signinPassword.next('a').next('span'));
		check = false;
	} else {
		fieldValid(signinPassword.next('a').next('span'));
	}

	return check;
}

function fieldEmptyError(e) {
	e.text("Fields cannot be empty!");
	e.addClass("is-visible");
}
function fieldValid(e) {
	e.text("");
	e.removeClass("is-visible");
}


function openAvatarGrid() {
	
	
	$('#imageSection').removeClass('no-visible');
	
	
	
	
	
	
	
}

// credits
// http://css-tricks.com/snippets/jquery/move-cursor-to-end-of-textarea-or-input/
jQuery.fn.putCursorAtEnd = function() {
	return this.each(function() {
		// If this function exists...
		if (this.setSelectionRange) {
			// ... then use it (Doesn't work in IE)
			// Double the length because Opera is inconsistent about whether a
			// carriage return is one character or two. Sigh.
			var len = $(this).val().length * 2;
			this.focus();
			this.setSelectionRange(len, len);
		} else {
			// ... otherwise replace the contents with itself
			// (Doesn't work in Google Chrome)
			$(this).val($(this).val());
		}
	});
};