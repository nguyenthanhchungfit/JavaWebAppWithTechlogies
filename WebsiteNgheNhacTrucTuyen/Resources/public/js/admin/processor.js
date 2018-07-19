// Global variable and constant
var globalHTMLMP3Server = "";
var globalHTMLDataServer = "";
var globalHTMLAdminServer = "";
var isConnectedWebSocket = false;
var filter = {server_type: {all : true, mp3_server : false, data_server: false, user_server: false}, 
                level_log : {all: true, info: false, warning: false, error: false, fatal: false, debug: false}};
var webSocket;

// document.body.onload = () =>{
//     alert("Hihi");
// }


// Events
$(document).ready(function(){
    //connectWebSocket();
});

$("#button_logs").on('click', function(event){
    event.preventDefault();
    var htmlContainer = createElementForLogsEvent();
    document.getElementById("content_right_panel").innerHTML = htmlContainer.innerHTML;
});

$("#content_right_panel").on('click', '#btnMp3Server', function(){
    if(isConnectedWebSocket == false){
        connectWebSocket();
    }
    document.getElementById("screen_logs").innerHTML = globalHTMLMP3Server;

});

$("#content_right_panel").on('click', '#btnDataServer', function(){
    if(isConnectedWebSocket == false){
        connectWebSocket();
    }
    document.getElementById("screen_logs").innerHTML = globalHTMLDataServer;
});

$("#content_right_panel").on('click', '#btnAdminServer', function(){
    if(isConnectedWebSocket == false){
        connectWebSocket();
    }
    document.getElementById("screen_logs").innerHTML = globalHTMLAdminServer;
});





// $(document).ready(function() {
//     // Handler for .ready() called.
//     alert("Hihi");
// });


// Functional method

/*---------------------- Xử lý giao diện chính   --------------------------------*/
function createElementForLogsEvent(){
    var divContainer = document.createElement("div");

    var htmlBtnMp3 = document.createElement("button");
    htmlBtnMp3.classList = "col-sm-6 col-lg-4";
    htmlBtnMp3.innerText = "Mp3 Server";
    htmlBtnMp3.setAttribute("id", "btnMp3Server");

    var htmlBtnData = document.createElement("button");
    htmlBtnData.classList = "col-sm-6 col-lg-4";
    htmlBtnData.innerText = "Data Server";
    htmlBtnData.setAttribute("id", "btnDataServer");

    var htmlBtnAdmin = document.createElement("button");
    htmlBtnAdmin.classList = "col-sm-6 col-lg-4";
    htmlBtnAdmin.innerText = "Admin Server";
    htmlBtnAdmin.setAttribute("id", "btnAdminServer");

    var htmlScreenLog = document.createElement("div");
    htmlScreenLog.className = "col-lg-12";
    htmlScreenLog.setAttribute("id", "screen_logs");

    divContainer.appendChild(htmlBtnMp3);
    divContainer.appendChild(htmlBtnData);
    divContainer.appendChild(htmlBtnAdmin);
    divContainer.appendChild(htmlScreenLog);
    return divContainer;
}





/*---------------------- End Xử lý giao diện chính  --------------------------------*/

/*----------------Xử lý logs-----------------*/
function connectWebSocket() {
    // open the connection if one does not exist
    if (webSocket !== undefined &&
        webSocket.readyState !== WebSocket.CLOSED) {
        return;
    }
    // Create a websocket
    webSocket = new WebSocket("ws://localhost:8003/getlogs");

    webSocket.onopen = function (event) {
        isConnectedWebSocket = true;
        worker();
    };

    webSocket.onmessage = function (event) {
        console.log(event.data);
        processingMessageReceived(event.data);
    };

    webSocket.onclose = function (event) {
        alert("Close connect to server");
        isConnectedWebSocket = false;
    };

    alert("Connected to Server!");  
}

function sendToWebSocket() {
    webSocket.send("admin_client_browser");
}

function closeWebSocket() {
    isConnectedWebSocket = false;
    alert("Close Websocket");
    webSocket.close();
}


function processingMessageReceived(message) {
    var messageParts = message.split("***");
    var htmlDocumentLog = createHTMLLog(messageParts[1], messageParts[2], messageParts[3]);
    var htmlLog = "<p>" + htmlDocumentLog.innerHTML + "</p>";
    if(messageParts[0] == "mp3_server"){
        globalHTMLMP3Server += htmlLog
    }else if(messageParts[0] == "data_server"){
        globalHTMLDataServer += htmlLog;
    }else if(messageParts[0] == "admin_server"){
        globalHTMLAdminServer += htmlLog;
    }

}


function worker() {
    var interval = setInterval(function () {
        if (isConnectedWebSocket) {
            webSocket.send("admin_client_browser");
        } else {
            clearInterval(interval);
        }
    }, 1000);
}

function createHTMLLog(type, date, message){
    var log = document.createElement("p");
    var dateHTML = document.createElement("span");
    dateHTML.className = "date_log";
    dateHTML.innerText = "<" + date + ">";

    var messageHTML = document.createElement("span");
    messageHTML.className = "message_log";
    messageHTML.innerHTML = " : " + message;

    log.appendChild(dateHTML);
    log.appendChild(messageHTML)
    return log;
}
/*----------------End Xử lý logs-----------------*/