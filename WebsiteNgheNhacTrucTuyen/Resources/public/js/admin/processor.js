// Global variable and constant
var globalHTMLLogs = "";
var isConnectedWebSocket = false;
var filter = {server_type: {all : true, mp3_server : false, data_server: false, user_server: false}, 
                level_log : {all: true, info: false, warning: false, error: false, fatal: false, debug: false}};
var isViewingLogs = false;
var webSocket;
var globalTbodyTag;
var globalCharts =  {mp3_song : undefined, mp3_singer : undefined, mp3_lyric : undefined, mp3_search : undefined, mp3_login : undefined};
const max_point_chart = 30;


/*--------------------------------------------------------------------------------------------------------------*/


// Events
$(document).ready(function(){
    connectWebSocket();
    initDataCharts();
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
        globalTbodyTag.insertBefore(domLogs, globalTbodyTag.firstChild);
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



/*---------------- Xử lý chart request ------------- */

function createElementForChartEvent(){
    var divContainer = document.createElement("div");
    divContainer.className = "container";

    // row option
    var rowOption = document.createElement("option");

    var selNoneServer = document.createElement("select");
    selNoneServer.setAttribute("value", "none_server");

    var selMp3Server = document.createElement("select");
    selMp3Server.setAttribute("value", "mp3_server");

    var selDataServer = document.createElement("select");
    selDataServer.setAttribute("value", "data_server");

    var selUserServer = document.createElement("select");
    selUserServer.setAttribute("value", "user_server");

    rowOption.appendChild(selNoneServer);
    rowOption.appendChild(selMp3Server);
    rowOption.appendChild(selDataServer);
    rowOption.appendChild(selUserServer);

    // ol server

    
    return divContainer;
}

function createListChart(typeChart){
    var olList = document.createElement("ol");
    if(typeChart == 1){     // mp3_server

        // Song Chart
        var liSong = document.createElement("li");
        var divSongTitle = document.createElement("div");
        divSongTitle.innerText = "Song API";
        var divSongChart = document.createElement("div");
        var canvasSongChart = document.createElement("canvas");
        canvasSongChart.id = "cv_song_chart";
        canvasSongChart.height = 300;
        new Chart(canvasSongChart, globalCharts.mp3_song);

        liSong.appendChild(divSongTitle);
        divSongChart.appendChild(canvasSongChart);
        liSong.appendChild(divSongChart);
        
        // Singer Chart
        var liSinger = document.createElement("li");
        var divSingerTitle = document.createElement("div");
        divSingerTitle.innerText = "Singer API";
        var divSingerChart = document.createElement("div");
        var canvasSingerChart = document.createElement("canvas");
        canvasSingerChart.id = "cv_singer_chart";
        canvasSingerChart.height = 300;
        new Chart(canvasSingerChart, globalCharts.mp3_singer);

        liSinger.appendChild(divSingerTitle);
        divSingerChart.appendChild(canvasSingerChart);
        liSinger.appendChild(divSingerChart);

        // Lyric Chart
        var liLyric = document.createElement("li");
        var divLyricTitle = document.createElement("div");
        divLyricTitle.innerText = "Lyric API";
        var divLyricChart = document.createElement("div");
        var canvasLyricChart = document.createElement("canvas");
        canvasLyricChart.id = "cv_lyric_chart";
        canvasLyricChart.height = 300;
        new Chart(canvasLyricChart, globalCharts.mp3_lyric);

        liLyric.appendChild(divLyricTitle);
        divLyricChart.appendChild(canvasLyricChart);
        liLyric.appendChild(divLyricChart);

        // Search Chart
        var liSearch = document.createElement("li");
        var divSearchTitle = document.createElement("div");
        divSearchTitle.innerText = "Search API";
        var divSearchChart = document.createElement("div");
        var canvasSearchChart = document.createElement("canvas");
        canvasSearchChart.id = "cv_search_chart";
        canvasSearchChart.height = 300;
        new Chart(canvasSearchChart, globalCharts.mp3_search);

        liSearch.appendChild(divSearchTitle);
        divSearchChart.appendChild(canvasSearchChart);
        liSearch.appendChild(divSearchChart);

        // Login Chart
        var liLogin = document.createElement("li");
        var divLoginTitle = document.createElement("div");
        divLoginTitle.innerText = "Login API";
        var divLoginChart = document.createElement("div");
        var canvasLoginChart = document.createElement("canvas");
        canvasLoginChart.id = "cv_login_chart";
        canvasLoginChart.height = 300;
        new Chart(canvasLoginChart, globalCharts.mp3_login);

        liLogin.appendChild(divLoginTitle);
        divLoginChart.appendChild(canvasLoginChart);
        liLogin.appendChild(divLoginChart);


        liSong.appendChild(divTitle);
        divChart.appendChild(canvasChart);
        liSong.appendChild(divChart);

        // add child
        olList.appendChild(liSong);
        olList.appendChild(liSinger);
        olList.appendChild(liLyric);
        olList.appendChild(liSearch);
        olList.appendChild(liLogin);

    }else if(typeChart == 2){   // data_server

    }else if(typeChart == 3){   // user_server

    }

    return olList;
}

// Hàm init data cho các dataCharts
function initDataCharts(){
    globalCharts.mp3_song = {
        type : 'line',
        data : {
            labels: [],
            type : 'line',
            datasets: [{
                data: [],
                label: 'requests/min',
                backgroundColor : '#63c2de',
                borderColor : 'rgba(255,255,255,.55)'
            }]
        },
        options: {
            maintainAspectRatio: false,
            legend: {
                display: false
            },
            responsive: true,
            tooltips: {
                mode: 'index',
                titleFontSize: 12,
                titleFontColor: '#000',
                bodyFontColor: '#000',
                backgroundColor: '#fff',
                titleFontFamily: 'Montserrat',
                bodyFontFamily: 'Montserrat',
                cornerRadius: 3,
                intersect: false,
            },
            scales: {
                xAxes: [ {
                    gridLines: {
                        color: 'transparent',
                        zeroLineColor: 'transparent'
                    },
                    ticks: {
                        fontSize: 2,
                        fontColor: 'transparent'
                    }
                } ],
                yAxes: [ {
                    display:false,
                    ticks: {
                        display: false,
                    }
                } ]
            },
            title: {
                display: false,
            },
            elements: {
                line: {
                    tension: 0.00001,
                    borderWidth: 1
                },
                point: {
                    radius: 4,
                    hitRadius: 10,
                    hoverRadius: 4
                }
            }
        }
    };

    globalCharts.mp3_singer = {
        type : 'line',
        data : {
            labels: [],
            type : 'line',
            datasets: [{
                data: [],
                label: 'requests/min',
                backgroundColor : '#63c2de',
                borderColor : 'rgba(255,255,255,.55)'
            }]
        },
        options: {
            maintainAspectRatio: false,
            legend: {
                display: false
            },
            responsive: true,
            tooltips: {
                mode: 'index',
                titleFontSize: 12,
                titleFontColor: '#000',
                bodyFontColor: '#000',
                backgroundColor: '#fff',
                titleFontFamily: 'Montserrat',
                bodyFontFamily: 'Montserrat',
                cornerRadius: 3,
                intersect: false,
            },
            scales: {
                xAxes: [ {
                    gridLines: {
                        color: 'transparent',
                        zeroLineColor: 'transparent'
                    },
                    ticks: {
                        fontSize: 2,
                        fontColor: 'transparent'
                    }
                } ],
                yAxes: [ {
                    display:false,
                    ticks: {
                        display: false,
                    }
                } ]
            },
            title: {
                display: false,
            },
            elements: {
                line: {
                    tension: 0.00001,
                    borderWidth: 1
                },
                point: {
                    radius: 4,
                    hitRadius: 10,
                    hoverRadius: 4
                }
            }
        }
    };

    globalCharts.mp3_lyric = {
        type : 'line',
        data : {
            labels: [],
            type : 'line',
            datasets: [{
                data: [],
                label: 'requests/min',
                backgroundColor : '#63c2de',
                borderColor : 'rgba(255,255,255,.55)'
            }]
        },
        options: {
            maintainAspectRatio: false,
            legend: {
                display: false
            },
            responsive: true,
            tooltips: {
                mode: 'index',
                titleFontSize: 12,
                titleFontColor: '#000',
                bodyFontColor: '#000',
                backgroundColor: '#fff',
                titleFontFamily: 'Montserrat',
                bodyFontFamily: 'Montserrat',
                cornerRadius: 3,
                intersect: false,
            },
            scales: {
                xAxes: [ {
                    gridLines: {
                        color: 'transparent',
                        zeroLineColor: 'transparent'
                    },
                    ticks: {
                        fontSize: 2,
                        fontColor: 'transparent'
                    }
                } ],
                yAxes: [ {
                    display:false,
                    ticks: {
                        display: false,
                    }
                } ]
            },
            title: {
                display: false,
            },
            elements: {
                line: {
                    tension: 0.00001,
                    borderWidth: 1
                },
                point: {
                    radius: 4,
                    hitRadius: 10,
                    hoverRadius: 4
                }
            }
        }
    };

    globalCharts.mp3_search = {
        type : 'line',
        data : {
            labels: [],
            type : 'line',
            datasets: [{
                data: [],
                label: 'requests/min',
                backgroundColor : '#63c2de',
                borderColor : 'rgba(255,255,255,.55)'
            }]
        },
        options: {
            maintainAspectRatio: false,
            legend: {
                display: false
            },
            responsive: true,
            tooltips: {
                mode: 'index',
                titleFontSize: 12,
                titleFontColor: '#000',
                bodyFontColor: '#000',
                backgroundColor: '#fff',
                titleFontFamily: 'Montserrat',
                bodyFontFamily: 'Montserrat',
                cornerRadius: 3,
                intersect: false,
            },
            scales: {
                xAxes: [ {
                    gridLines: {
                        color: 'transparent',
                        zeroLineColor: 'transparent'
                    },
                    ticks: {
                        fontSize: 2,
                        fontColor: 'transparent'
                    }
                } ],
                yAxes: [ {
                    display:false,
                    ticks: {
                        display: false,
                    }
                } ]
            },
            title: {
                display: false,
            },
            elements: {
                line: {
                    tension: 0.00001,
                    borderWidth: 1
                },
                point: {
                    radius: 4,
                    hitRadius: 10,
                    hoverRadius: 4
                }
            }
        }
    };

    globalCharts.mp3_login = {
        type : 'line',
        data : {
            labels: [],
            type : 'line',
            datasets: [{
                data: [],
                label: 'requests/min',
                backgroundColor : '#63c2de',
                borderColor : 'rgba(255,255,255,.55)'
            }]
        },
        options: {
            maintainAspectRatio: false,
            legend: {
                display: false
            },
            responsive: true,
            tooltips: {
                mode: 'index',
                titleFontSize: 12,
                titleFontColor: '#000',
                bodyFontColor: '#000',
                backgroundColor: '#fff',
                titleFontFamily: 'Montserrat',
                bodyFontFamily: 'Montserrat',
                cornerRadius: 3,
                intersect: false,
            },
            scales: {
                xAxes: [ {
                    gridLines: {
                        color: 'transparent',
                        zeroLineColor: 'transparent'
                    },
                    ticks: {
                        fontSize: 2,
                        fontColor: 'transparent'
                    }
                } ],
                yAxes: [ {
                    display:false,
                    ticks: {
                        display: false,
                    }
                } ]
            },
            title: {
                display: false,
            },
            elements: {
                line: {
                    tension: 0.00001,
                    borderWidth: 1
                },
                point: {
                    radius: 4,
                    hitRadius: 10,
                    hoverRadius: 4
                }
            }
        }
    };

}

// Hàm gọi request tới server và xử lý cập nhật dataChart
function updateDataCharts(){
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            dataStatistic = JSON.parse(this.responseText);
            if(dataStatistic.success == true){
                processDataCharts(globalCharts.mp3_song, dataStatistic.date, dataStatistic.datas[0].mp3_song);
                processDataCharts(globalCharts.mp3_singer, dataStatistic.date, dataStatistic.datas[1].mp3_singer);
                processDataCharts(globalCharts.mp3_lyric, dataStatistic.date, dataStatistic.datas[2].mp3_lyric);
                processDataCharts(globalCharts.mp3_search, dataStatistic.date, dataStatistic.datas[3].mp3_search);
                processDataCharts(globalCharts.mp3_login, dataStatistic.date, dataStatistic.datas[4].mp3_login);
            }
        }
    };
    var query = "/../statistic?pid=0000";
    xhttp.open("GET", query, true);
    xhttp.send();
}

// Cập nhật dữ liệu của phần tử dataChart
function processDataCharts(chart, newDate, newCounter){
    var labels = chart.data.labels;
    var datas = chart.data.datasets.data;

    if(labels == undefined || datas == undefined) return;
    if(labels.length == max_point_chart){
        labels.shift();
        datas.shift();
    }
    labels.push(newDate);
    datas.push(newCounter);
}








/*---------------- End Xử lý chart request ------------- */