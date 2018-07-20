// Global variable and constant
var globalHTMLLogs = "";
var isConnectedWebSocket = false;
var filter = {server_type: {all : true, mp3_server : false, data_server: false, user_server: false}, 
                level_log : {all: true, info: false, warning: false, error: false, fatal: false, debug: false}};
var isViewingLogs = false;
var webSocket;
var globalTbodyTag;


/*--------------------------------------------------------------------------------------------------------------*/


// Events
$(document).ready(function(){
    connectWebSocket();
});

$("#button_logs").on('click', function(event){
    event.preventDefault();
    var htmlContainer = createElementForLogsEvent();
    isViewingLogs = true;
    document.getElementById("content_right_panel").innerHTML = htmlContainer.innerHTML;
    globalTbodyTag = document.getElementById("tbody_logs");
    globalTbodyTag.innerHTML = globalHTMLLogs;
});



// Functional method

/*---------------------- Xử lý giao diện chính   --------------------------------*/
function createElementForLogsEvent(){
    var divContainer = document.createElement("div");

    // Tạo bảng
    var tableLogs = document.createElement("table");
    tableLogs.classList = "table table-hover table-bordered"; 
    var theadLogs = document.createElement("thead");
    theadLogs.className = "thead-dark";
    var tBodyLogs = document.createElement("tbody");
    tBodyLogs.setAttribute("id", "tbody_logs");

    var trowLogs = document.createElement("tr");
    trowLogs.className = "d-flex";

    var thTimeStamp = document.createElement("th");
    thTimeStamp.classList = "col-2 text-center";
    thTimeStamp.innerHTML = "timestamp";

    var thHost = document.createElement("th");
    thHost.classList = "col-2 text-center";
    thHost.innerHTML = "host";

    var thLevel = document.createElement("th");
    thLevel.classList = "col-1 text-center";
    thLevel.innerHTML = "level";

    var thMessage = document.createElement("th");
    thMessage.classList = "col-5 text-center";
    thMessage.innerHTML = "message";

    var thTimeExecute = document.createElement("th");
    thTimeExecute.classList = "col-2 text-center";
    thTimeExecute.innerHTML = "time-execution";

    trowLogs.appendChild(thTimeStamp);
    trowLogs.appendChild(thHost);
    trowLogs.appendChild(thLevel);
    trowLogs.appendChild(thMessage);
    trowLogs.appendChild(thTimeExecute);

    theadLogs.appendChild(trowLogs);
    tableLogs.appendChild(theadLogs);
    tableLogs.appendChild(tBodyLogs);

    divContainer.appendChild(tableLogs);

    return divContainer;
}

function parseDomToHTML(obj){
    var divContainer = document.createElement("div");
    div.appendChild(obj);
    return div.innerHTML;
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
        processMessageReceived(event.data);
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

function worker() {
    var interval = setInterval(function () {
        if (isConnectedWebSocket) {
            webSocket.send("admin_client_browser");
        } else {
            clearInterval(interval);
        }
    }, 1000);
}


function processMessageReceived(receivedMessage){
    var messageObj = getMessageObjectFromMessageReceived(receivedMessage);
    var domLogs = createElementLog(messageObj);
    if(globalTbodyTag == undefined){
        globalTbodyTag = document.getElementById("tbody_logs");
    }

    // insert node
    if(isViewingLogs){
        globalTbodyTag = document.getElementById("tbody_logs");
        globalTbodyTag.insertBefore(domLogs, globalTbodyTag[0]);
    }else{
        globalHTMLLogs = parseDomToHTML(domLogs) + globalHTMLLogs;
    }
    
}

function getMessageObjectFromMessageReceived(message) {
    var messageObj = {};
    var messageParts = message.split("***");
    // time_stamp
    messageObj.timestamp = messageParts[0].trim();
    // host
    messageObj.host = messageParts[3].trim();
    // level
    messageObj.level = messageParts[1].trim();
    // message
    messageObj.message = messageParts[5].trim() + "   " + messageParts[2].trim();
    //time_executed
    messageObj.time_executed = messageParts[4].trim();

    return messageObj;

}

function getClassNameForLevel(level){
    text_color_class = "";
    switch(level)
    {
        case "TRACE":
                    text_color_class = 'text_trace_color';
                    break;
        case "DEBUG":
                    text_color_class = 'text_debug_color';
                    break;
        case "INFO":
                    text_color_class = 'text_info_color';
                    break;
        case "WARN":
                    text_color_class = 'text_warm_color';
                    break;
        case "ERROR":
                    text_color_class = 'text_error_color';
                    break;
        case "FATAL":
                    text_color_class = 'text_fatal_color';
                    break;
        case "OFF":
                    text_color_class = 'text_off_color';
                    break;            
    }
    return text_color_class;
}

function createElementLog(messageObj){

    var classTextLevel = getClassNameForLevel(messageObj.level);

    var trowLogs = document.createElement("tr");
    trowLogs.className = "d-flex";

    var thTimeStamp = document.createElement("th");
    thTimeStamp.classList = "col-2";
    thTimeStamp.innerHTML = messageObj.timestamp;

    var thHost = document.createElement("th");
    thHost.classList = "col-2 text-center";
    thHost.innerHTML = messageObj.host;

    var thLevel = document.createElement("th");
    thLevel.classList = "col-1 text-center" + " " + classTextLevel;
    thLevel.innerHTML = messageObj.level;

    var thMessage = document.createElement("th");
    thMessage.classList = "col-5" + " " + classTextLevel;
    thMessage.innerHTML = messageObj.message;

    var thTimeExecute = document.createElement("th");
    thTimeExecute.classList = "col-2 text-center";
    thTimeExecute.innerHTML = messageObj.time_executed;

    trowLogs.appendChild(thTimeStamp);
    trowLogs.appendChild(thHost);
    trowLogs.appendChild(thLevel);
    trowLogs.appendChild(thMessage);
    trowLogs.appendChild(thTimeExecute);

    return trowLogs;
}

/*----------------End Xử lý logs-----------------*/