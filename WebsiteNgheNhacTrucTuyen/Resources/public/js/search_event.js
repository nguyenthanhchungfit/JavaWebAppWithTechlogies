const host = "http://localhost:8000/"

$( document ).ready(function() {
    alert("OK");
    $("#search_text").on('input', function(e){
        var text = $("#search_text").val();
        if(text == ""){

        }else{
            
        }
    })
});

function getSongsSuggestion(name){
    if(name == "") return;
    var xmlHttpRequest = new XMLHttpRequest();

    var linkRequest = host + "search?name=" + name;
    xmlHttpRequest.onreadystatechange = function(){
        if(this.readyState == 4 && this.status == 200){
            console.log(html);
        }
    };
    xmlHttpRequest.open("GET", linkRequest, true);
    xmlHttpRequest.send();

}