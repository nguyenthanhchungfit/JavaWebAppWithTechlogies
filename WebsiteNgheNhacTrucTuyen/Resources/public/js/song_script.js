const lyricShow = 15;
var dataLyrics = [];
var current_page = 0;
var total_page = 0;

var buttonXemThem = "<button class='btn btn-success' id='btnShort'>Xem thêm</button>"
var buttonRutGon = "<button class='btn btn-success' id='btnShort'>Rút gọn</button>"

$( document ).ready(function() {
    var idLyric = $(".lyric_control").attr("id");
    loadLyricById(idLyric);
});


$("div.button_left").on("click",function(){
    if(current_page > 0){
        current_page--;
        splitLyric(dataLyrics[current_page].content);
        $("#page_lyrics").text(current_page + 1 + "/" + total_page);
        $("#contributor").text(dataLyrics[current_page].contributor);
    }
});

$("div.button_right").on("click",function(){
    if(current_page < total_page - 1){
        current_page++;
        splitLyric(dataLyrics[current_page].content);
        $("#page_lyrics").text(current_page + 1 + "/" + total_page);
        $("#contributor").text(dataLyrics[current_page].contributor);
    } 

});

$("#lyrics_song").on("click", "#btnShort",function(){
    var content = $(this).text();
    if(content == "Xem thêm"){
        fullLyric();
    }else{
        splitLyric(dataLyrics[current_page].content);
    }
})


function loadLyricById(idLyric){
    var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                dataLyrics = JSON.parse(this.responseText);
                total_page = dataLyrics.length;           
                splitLyric(dataLyrics[current_page].content);
                $("#page_lyrics").text(current_page + 1 + "/" + total_page);
                $("#contributor").text(dataLyrics[current_page].contributor);
            }
        };
    var query = "/lyric?id=" + idLyric;
    xhttp.open("GET", query, true);
    xhttp.send();
}

function splitLyric(lyric){
    var data = lyric.split("<br>");
    var size = (lyricShow < data.length)? lyricShow : data.length;
    var chuoiHTML = "";
    for(var i = 0; i < size; i++){
        chuoiHTML += data[i] + "<br>";
    }
    var htmlLyricContet = chuoiHTML + buttonXemThem;
    $("#lyrics_song").html(htmlLyricContet);
}

function fullLyric(){
    var htmlLyricContet = dataLyrics[current_page].content + "<br>" + buttonRutGon;
    $("#lyrics_song").html(htmlLyricContet);
}

