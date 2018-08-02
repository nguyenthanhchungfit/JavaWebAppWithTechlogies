// Global variable and constant
var globalHTMLLogs = "";
var isConnectedWebSocket = false;
var filter = {
    server_type: {
        all: true,
        mp3_server: false,
        data_server: false,
        user_server: false
    },
    level_log: {
        all: true,
        info: false,
        warning: false,
        error: false,
        fatal: false,
        debug: false
    }
};
var isViewingLogs = false;
var webSocket;
var globalTbodyTag;
var globalDataCharts = {
    mp3_song: undefined,
    mp3_singer: undefined,
    mp3_lyric: undefined,
    mp3_search: undefined,
    mp3_login: undefined
};

var globalCharts = {
    mp3_song_chart : undefined,
    mp3_singer_chart : undefined,
    mp3_lyric_chart : undefined,
    mp3_search_chart : undefined,
    mp3_login_chart : undefined
};

var globalIntervalCharts = undefined;

const max_point_chart = 15;


/*--------------------------------------------------------------------------------------------------------------*/


// Events
$(document).ready(function () {
    // logs
    connectWebSocket();
    
    // charts
    initDataCharts();
    workerChart();
});

$("#button_logs").on('click', function (event) {
    event.preventDefault();
    var htmlContainer = createElementForLogsEvent();
    isViewingLogs = true;
    document.getElementById("content_right_panel").innerHTML = htmlContainer.innerHTML;
    globalTbodyTag = document.getElementById("tbody_logs");
    globalTbodyTag.innerHTML = globalHTMLLogs;
});

$("#button_statistic").on('click', function (event) {
    isViewingLogs = false;
    event.preventDefault();
    var typeChart = 1;
    var docStatistic = createElementForChartEvent(typeChart);
    document.getElementById("content_right_panel").innerHTML = parseDomToHTML(docStatistic);
    addChartsToLayout(typeChart);
});

$("#dbtest").on('click', function(event){
    //testAddData();
    //console.log(globalCharts);
});



// Functional method

/*---------------------- Xử lý giao diện chính   --------------------------------*/
function createElementForLogsEvent() {
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

function parseDomToHTML(obj) {
    var divContainer = document.createElement("div");
    divContainer.appendChild(obj);
    return divContainer.innerHTML;
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


function processMessageReceived(receivedMessage) {
    var messageObj = getMessageObjectFromMessageReceived(receivedMessage);
    var domLogs = createElementLog(messageObj);
    if (globalTbodyTag == undefined) {
        globalTbodyTag = document.getElementById("tbody_logs");
    }

    // insert node
    if (isViewingLogs) {
        globalTbodyTag = document.getElementById("tbody_logs");
        globalTbodyTag.insertBefore(domLogs, globalTbodyTag.firstChild);
    } else {
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

function getClassNameForLevel(level) {
    text_color_class = "";
    switch (level) {
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
            text_color_class = 'text_warn_color';
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

function createElementLog(messageObj) {

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

function createElementForChartEvent(typeChart) {
    var divContainer = document.createElement("div");
    divContainer.className = "container";

    // row option
    var rowOption = document.createElement("row");
    var selServer = document.createElement("select");

    var optNoneServer = document.createElement("option");
    optNoneServer.setAttribute("value", "none_server");
    optNoneServer.innerText = "Choose Server";

    var optMp3Server = document.createElement("option");
    optMp3Server.setAttribute("value", "mp3_server");
    optMp3Server.innerText = "MP3 SERVER";

    var optDataServer = document.createElement("option");
    optDataServer.setAttribute("value", "data_server");
    optDataServer.innerText = "DATA SERVER";

    var optUserServer = document.createElement("option");
    optUserServer.setAttribute("value", "user_server");
    optUserServer.innerText = "USER SERVER";

    selServer.appendChild(optNoneServer);
    selServer.appendChild(optMp3Server);
    selServer.appendChild(optDataServer);
    selServer.appendChild(optUserServer);

    rowOption.appendChild(selServer);

    // row titleServer
    var rowTitleServer = document.createElement("row");
    var divTitleServer = document.createElement("div");
    divTitleServer.id = "title_server_chart";
    divTitleServer.innerText = "MP3 SERVER";
    rowTitleServer.appendChild(divTitleServer);

    // list chart
    var listChart = createListChart(1);

    // add to parant element
    divContainer.appendChild(rowOption);
    divContainer.appendChild(rowTitleServer);
    divContainer.appendChild(listChart);
    return divContainer;
}

function createListChart(typeChart) {

    Chart.defaults.global.defaultFontColor = '#000000';
    Chart.defaults.global.defaultFontFamily = 'Arial';

    var divListCharts = document.createElement("div");
    divListCharts.id = "list_charts";
    if (typeChart == 1) {
        // Song Chart
        var divSongChart = document.createElement("div");
        divSongChart.id = "song_chart";
        divSongChart.classList = "chart-container mt-5 mb-5";

        var titleSongChart = document.createElement("div");
        titleSongChart.innerText = "1. Song Chart";

        var canvasSongChart = document.createElement("canvas");
        canvasSongChart.id = "cv_song_chart";

        divSongChart.appendChild(titleSongChart);
        divSongChart.appendChild(canvasSongChart);

        // Singer Chart
        var divSingerChart = document.createElement("div");
        divSingerChart.id = "singer_chart";
        divSingerChart.classList = "chart-container mt-5 mb-5";

        var titleSingerChart = document.createElement("div");
        titleSingerChart.innerText = "2. Singer Chart";

        var canvasSingerChart = document.createElement("canvas");
        canvasSingerChart.id = "cv_singer_chart";

        divSingerChart.appendChild(titleSingerChart);
        divSingerChart.appendChild(canvasSingerChart);


        // Lyric Chart
        var divLyricChart = document.createElement("div");
        divLyricChart.id = "lyric_chart";
        divLyricChart.classList = "chart-container mt-5 mb-5";

        var titleLyricChart = document.createElement("div");
        titleLyricChart.innerText = "3. Lyric Chart";

        var canvasLyricChart = document.createElement("canvas");
        canvasLyricChart.id = "cv_lyric_chart";

        divLyricChart.appendChild(titleLyricChart);
        divLyricChart.appendChild(canvasLyricChart);

        // Search Chart
        var divSearchChart = document.createElement("div");
        divSearchChart.id = "search_chart";
        divSearchChart.classList = "chart-container mt-5 mb-5";

        var titleSearchChart = document.createElement("div");
        titleSearchChart.innerText = "4. Search Chart";

        var canvasSearchChart = document.createElement("canvas");
        canvasSearchChart.id = "cv_search_chart";

        divSearchChart.appendChild(titleSearchChart);
        divSearchChart.appendChild(canvasSearchChart);

        // Login Chart
        var divLoginChart = document.createElement("div");
        divLoginChart.id = "login_chart";
        divLoginChart.classList = "chart-container mt-5 mb-5";

        var titleLoginChart = document.createElement("div");
        titleLoginChart.innerText = "5. Login Chart";

        var canvasLoginChart = document.createElement("canvas");
        canvasLoginChart.id = "cv_login_chart";

        divLoginChart.appendChild(titleLoginChart);
        divLoginChart.appendChild(canvasLoginChart);


        // add child chart
        divListCharts.appendChild(divSongChart);
        divListCharts.appendChild(divSingerChart);
        divListCharts.appendChild(divLyricChart);
        divListCharts.appendChild(divSearchChart);
        divListCharts.appendChild(divLoginChart);
    }

    return divListCharts;
}

function addChartsToLayout(typeChart) {

    if (typeChart == 1) {
        var canvasSongChart = document.getElementById("cv_song_chart");
        globalCharts.mp3_song_chart =  new Chart(canvasSongChart, globalDataCharts.mp3_song);

        var canvasSingerChart = document.getElementById("cv_singer_chart");
        globalCharts.mp3_singer_chart = new Chart(canvasSingerChart, globalDataCharts.mp3_singer);

        var canvasLyricChart = document.getElementById("cv_lyric_chart");
        globalCharts.mp3_lyric_chart = new Chart(canvasLyricChart, globalDataCharts.mp3_lyric);

        var canvasSearchChart = document.getElementById("cv_search_chart");
        globalCharts.mp3_search_chart =  new Chart(canvasSearchChart, globalDataCharts.mp3_search);

        var canvasLoginChart = document.getElementById("cv_login_chart");
        globalCharts.mp3_login_chart = new Chart(canvasLoginChart, globalDataCharts.mp3_login);
    }

    console.log(globalCharts);

}

// Hàm init data cho các dataCharts
function initDataCharts() {

    Chart.defaults.global.defaultFontColor = '#000000';
    Chart.defaults.global.defaultFontFamily = 'Arial';

    globalDataCharts.mp3_song = {
        type: 'line',
        data: {
            labels: [],
            datasets: [{
                label: "requests/0.5min",
                backgroundColor: "rgba(255,99,132,0.2)",
                borderColor: "rgba(255,99,132,1)",
                borderWidth: 2,
                hoverBackgroundColor: "rgba(255,99,132,0.4)",
                hoverBorderColor: "rgba(255,99,132,1)",
                data: [],
            }]
        },
        options: {
            maintainAspectRatio: false,
            scales: {
                yAxes: [{
                    stacked: true,
                    gridLines: {
                        display: true,
                        color: "rgba(255,99,132,0.2)"
                    }
                }],
                xAxes: [{
                    gridLines: {
                        display: false
                    }
                }]
            }
        }
    };

    globalDataCharts.mp3_singer = {
        type: 'line',
        data: {
            labels: [],
            datasets: [{
                label: "requests/0.5min",
                backgroundColor: "rgba(205, 220, 57, 0.2)",
                borderColor: "rgba(205, 220, 57, 1)",
                borderWidth: 2,
                hoverBackgroundColor: "rgba(205, 220, 57, 0.4)",
                hoverBorderColor: "rgba(205, 220, 57, 1)",
                data: [],
            }]
        },
        options: {
            maintainAspectRatio: false,
            scales: {
                yAxes: [{
                    stacked: true,
                    gridLines: {
                        display: true,
                        color: "rgba(205, 220, 57, 0.2)"
                    }
                }],
                xAxes: [{
                    gridLines: {
                        display: false
                    }
                }]
            }
        }
    };

    globalDataCharts.mp3_lyric = {
        type: 'line',
        data: {
            labels: [],
            datasets: [{
                label: "requests/0.5min",
                backgroundColor: "rgba(104, 159, 56, 0.2)",
                borderColor: "rgba(104, 159, 56, 1)",
                borderWidth: 2,
                hoverBackgroundColor: "rgba(104, 159, 56, 0.4)",
                hoverBorderColor: "rgba(104, 159, 56, 1)",
                data: [],
            }]
        },
        options: {
            maintainAspectRatio: false,
            scales: {
                yAxes: [{
                    stacked: true,
                    gridLines: {
                        display: true,
                        color: "rgba(104, 159, 56, 0.2)"
                    }
                }],
                xAxes: [{
                    gridLines: {
                        display: false
                    }
                }]
            }
        }
    };

    globalDataCharts.mp3_search = {
        type: 'line',
        data: {
            labels: [],
            datasets: [{
                label: "requests/0.5min",
                backgroundColor: "rgba(13, 71, 161, 0.2)",
                borderColor: "rgba(13, 71, 161, 1)",
                borderWidth: 2,
                hoverBackgroundColor: "rgba(13, 71, 161, 0.4)",
                hoverBorderColor: "rgba(13, 71, 161, 1)",
                data: [],
            }]
        },
        options: {
            maintainAspectRatio: false,
            scales: {
                yAxes: [{
                    stacked: true,
                    gridLines: {
                        display: true,
                        color: "rgba(13, 71, 161, 0.2)"
                    }
                }],
                xAxes: [{
                    gridLines: {
                        display: false
                    }
                }]
            }
        }
    };

    globalDataCharts.mp3_login = {
        type: 'line',
        data: {
            labels: [],
            datasets: [{
                label: "requests/0.5min",
                backgroundColor: "rgba(191, 54, 12, 0.2)",
                borderColor: "rgba(191, 54, 12, 1)",
                borderWidth: 2,
                hoverBackgroundColor: "rgba(191, 54, 12, 0.4)",
                hoverBorderColor: "rgba(191, 54, 12, 1)",
                data: [],
            }]
        },
        options: {
            maintainAspectRatio: false,
            scales: {
                yAxes: [{
                    stacked: true,
                    gridLines: {
                        display: true,
                        color: "rgba(191, 54, 12, 0.2)"
                    }
                }],
                xAxes: [{
                    gridLines: {
                        display: false
                    }
                }]
            }
        }
    };

}

// Hàm gọi request tới server và xử lý cập nhật dataChart
function updateDataCharts() {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            dataStatistic = JSON.parse(this.responseText);
            if (dataStatistic.success == true) {
                processDataCharts(globalCharts.mp3_song_chart, dataStatistic.date, dataStatistic.datas[0].mp3_song);
                processDataCharts(globalCharts.mp3_singer_chart, dataStatistic.date, dataStatistic.datas[1].mp3_singer);
                processDataCharts(globalCharts.mp3_lyric_chart, dataStatistic.date, dataStatistic.datas[2].mp3_lyric);
                processDataCharts(globalCharts.mp3_search_chart, dataStatistic.date, dataStatistic.datas[3].mp3_search);
                processDataCharts(globalCharts.mp3_login_chart, dataStatistic.date, dataStatistic.datas[4].mp3_login);
            }
        }
    };
    var query = "/../statistic?pid=0000";
    xhttp.open("GET", query, true);
    xhttp.send();
}

// Cập nhật dữ liệu của phần tử dataChart
function processDataCharts(chart, newDate, newCounter) {
    var labels = chart.data.labels;
    var datas = chart.data.datasets[0].data;

    if (labels == undefined || datas == undefined) return;
    if (labels.length == max_point_chart) {
        labels.shift();
        datas.shift();
    }
    labels.push(newDate);
    datas.push(newCounter);
    chart.update();
}

// Worker Interver
function workerChart(){
    globalIntervalCharts = setInterval(function(){
        updateDataCharts();
    }, 20000);
}

function testAddData(){
    var date = new Date().toLocaleTimeString();
    processDataCharts(globalCharts.mp3_song_chart, date, Math.floor(Math.random() * 100));
    processDataCharts(globalCharts.mp3_singer_chart, date, Math.floor(Math.random() * 100));
    processDataCharts(globalCharts.mp3_lyric_chart, date, Math.floor(Math.random() * 100));
    processDataCharts(globalCharts.mp3_search_chart, date, Math.floor(Math.random() * 100));
    processDataCharts(globalCharts.mp3_login_chart, date, Math.floor(Math.random() * 100));
}









/*---------------- End Xử lý chart request ------------- */