'use strict';

// recupero elementi dei campi password dal DOM

let passwordInput = document.getElementById("password");
let showPasswordButton = document.getElementById("showPasswordButton");
let validatePasswordInput = document.getElementById("validatePassword");
let showValidatePasswordButton = document.getElementById("showValidatePasswordButton");

// aggiunta eventi con funzioni per cambiare il type degli input delle password da "password" a "text", e viceversa

showPasswordButton.addEventListener("mousedown", function() {

	passwordInput.setAttribute("type", "text");

});

document.addEventListener("mouseup", function() {

	passwordInput.setAttribute("type", "password");

});

showValidatePasswordButton.addEventListener("mousedown", function() {

	validatePasswordInput.setAttribute("type", "text");

});

document.addEventListener("mouseup", function() {

	validatePasswordInput.setAttribute("type", "password");

});