$( document ).ready(function() {
    var idLyric = $(".lyric_control").attr("id");
    alert(loadLyricById(idLyric, 0));
});

function loadLyricById(idLyric, page){
    if(page < 0) return;
    var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                return this.responseText;
            }
        };
    var query = "/lyric?id=" + idLyric +"&page=" + page;
    xhttp.open("GET", query, true);
    xhttp.send();
}

$("div.button_left").on("click",function(){
    var text = $("#page_lyrics").text();
    var current_page = text.substring(0,1) - 0;
    var total_page = text.substring(2,3) - 0;
    var id = $("div.song_inf").attr("id");
    console.log("text: ", text);
    console.log("current: ",current_page);
    console.log("total: ", total_page);
    console.log("id", id);

    if(current_page > 1){
        current_page--;
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            $('#lyrics_song').text(this.responseText);
        }
        };
        var real_page = current_page - 1;
        var query = "/lyric?id=" + id +"&page=" + real_page;
        xhttp.open("GET", query, true);
        xhttp.send();
        var str_page = current_page + "/" + total_page;
        $("#page_lyrics").text(str_page);
    }
});

$("div.button_right").on("click",function(){
    var text = $("#page_lyrics").text();
    var current_page = text.substring(0,1) - 0;
    var total_page = text.substring(2,3) - 0;
    var id = $("div.song_inf").attr("id");
    console.log("text: ", text);
    console.log("current: ",current_page);
    console.log("total: ", total_page);
    console.log("id", id);

    if(current_page < total_page){
        current_page++;
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                $('#lyrics_song').text(this.responseText);
            }
        };

        var real_page = current_page - 1;
        var query = "/lyric?id=" + id +"&page=" + real_page;
        xhttp.open("GET", query, true);
        xhttp.send();
        var str_page = current_page + "/" + total_page;
        $("#page_lyrics").text(str_page);
    }
    
});