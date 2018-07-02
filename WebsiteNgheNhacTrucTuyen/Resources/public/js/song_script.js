var dataLyrics = [];
var current_page = 0;
var total_page = 0;

$( document ).ready(function() {
    var idLyric = $(".lyric_control").attr("id");
    loadLyricById(idLyric);
});

function loadLyricById(idLyric){
    var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                dataLyrics = JSON.parse(this.responseText);
                total_page = dataLyrics.length;
                $("#lyrics_song").html(dataLyrics[current_page].content);
                $("#page_lyrics").text(current_page + 1 + "/" + total_page);
                $("#contributor").text(dataLyrics[current_page].contributor);
            }
        };
    var query = "/lyric?id=" + idLyric;
    xhttp.open("GET", query, true);
    xhttp.send();
}

$("div.button_left").on("click",function(){
    if(current_page > 0){
        current_page--;
        $("#lyrics_song").html(dataLyrics[current_page].content);
        $("#page_lyrics").text(current_page + 1 + "/" + total_page);
        $("#contributor").text(dataLyrics[current_page].contributor);
    }
});

$("div.button_right").on("click",function(){
    if(current_page < total_page - 1){
        current_page++;
        $("#lyrics_song").html(dataLyrics[current_page].content);
        $("#page_lyrics").text(current_page + 1 + "/" + total_page);
        $("#contributor").text(dataLyrics[current_page].contributor);
    } 

});