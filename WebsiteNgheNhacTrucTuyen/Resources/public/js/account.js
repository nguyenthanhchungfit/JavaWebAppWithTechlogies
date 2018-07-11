$(document).ready(function () {
    $("#target").submit(function (event) {
        alert("Handler for .submit() called.");
        event.preventDefault();
    });
});