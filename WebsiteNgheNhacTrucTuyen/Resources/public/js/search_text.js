const host = "http://localhost:8000/"

$(document).ready(function () {

});

function autocomplete(inp) {
  /*the autocomplete function takes two arguments,
  the text field element and an array of possible autocompleted values:*/
  var currentFocus;
  /*execute a function when someone writes in the text field:*/
  inp.addEventListener("input", function (e) {
    var a, b, i, val = this.value;
    var xmlHttpRequest = new XMLHttpRequest();
    var thisElement = this;
    var linkRequest = host + "search?name=" + val;
    xmlHttpRequest.onreadystatechange = function () {
      if (this.readyState == 4 && this.status == 200) {
        var arr = JSON.parse(this.responseText);

        /*close any already open lists of autocompleted values*/
        closeAllLists();
        if (!val) {
          return false;
        }

        currentFocus = -1;
        /*create a DIV element that will contain the items (values):*/
        a = document.createElement("DIV");
        a.setAttribute("id", thisElement.id + "autocomplete-list");
        a.setAttribute("class", "autocomplete-items");
        /*append the DIV element as a child of the autocomplete container:*/
        thisElement.parentNode.appendChild(a);
        /*for each item in the array...*/
        for (i = 0; i < arr.length; i++) {
          /*check if the item starts with the same letters as the text field value:*/
          var srcText = arr[i].name.toUpperCase();
          var searchText = val.toUpperCase();
          var index = srcText.search(searchText);
          if (index != -1) {
            /*create a DIV element for each matching element:*/
            b = document.createElement("DIV");
            var title = document.createElement("p");
            var aLink = document.createElement("a");
            aLink.setAttribute("class", "my_link");
            aLink.href = host + "song?id=" + arr[i].id;
            aLink.innerHTML = arr[i].name.substr(0, index);
            aLink.innerHTML += "<span class='my_match_word'>" + arr[i].name.substr(index, searchText.length) + "</span>";
            aLink.innerHTML += arr[i].name.substr(index + searchText.length, srcText.length);

            /*insert a input field that will hold the current array item's value:*/
            aLink.innerHTML += "<input type='hidden' value='" + arr[i].name + "'>";
            title.appendChild(aLink);

            var singers = document.createElement("p");
            singers.innerHTML = formatStrings(arr[i].singers);


            b.appendChild(title);
            b.appendChild(singers);
            /*execute a function when someone clicks on the item value (DIV element):*/
            b.addEventListener("click", function (e) {
              /*insert the value for the autocomplete text field:*/
              inp.value = this.getElementsByTagName("input")[0].value;
              /*close the list of autocompleted values,
              (or any other open lists of autocompleted values:*/
              closeAllLists();
            });
            a.appendChild(b);
          }
        }
      }
    };
    xmlHttpRequest.open("GET", linkRequest, true);
    xmlHttpRequest.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xmlHttpRequest.send();



  });
  /*execute a function presses a key on the keyboard:*/
  inp.addEventListener("keydown", function (e) {
    var x = document.getElementById(this.id + "autocomplete-list");
    if (x) x = x.getElementsByTagName("div");
    if (e.keyCode == 40) {
      /*If the arrow DOWN key is pressed,
      increase the currentFocus variable:*/
      currentFocus++;
      /*and and make the current item more visible:*/
      addActive(x);
    } else if (e.keyCode == 38) { //up
      /*If the arrow UP key is pressed,
      decrease the currentFocus variable:*/
      currentFocus--;
      /*and and make the current item more visible:*/
      addActive(x);
    } else if (e.keyCode == 13) {
      /*If the ENTER key is pressed, prevent the form from being submitted,*/
      //e.preventDefault();
      if (currentFocus > -1) {
        /*and simulate a click on the "active" item:*/
        if (x) x[currentFocus].click();
      }
    }
  });

  function addActive(x) {
    /*a function to classify an item as "active":*/
    if (!x) return false;
    /*start by removing the "active" class on all items:*/
    removeActive(x);
    if (currentFocus >= x.length) currentFocus = 0;
    if (currentFocus < 0) currentFocus = (x.length - 1);
    /*add class "autocomplete-active":*/
    x[currentFocus].classList.add("autocomplete-active");
  }

  function removeActive(x) {
    /*a function to remove the "active" class from all autocomplete items:*/
    for (var i = 0; i < x.length; i++) {
      x[i].classList.remove("autocomplete-active");
    }
  }

  function closeAllLists(elmnt) {
    /*close all autocomplete lists in the document,
    except the one passed as an argument:*/
    var x = document.getElementsByClassName("autocomplete-items");
    for (var i = 0; i < x.length; i++) {
      if (elmnt != x[i] && elmnt != inp) {
        x[i].parentNode.removeChild(x[i]);
      }
    }

  }

  function formatStrings(arrString) {
    var str = arrString[0];
    for (var i = 1; i < arrString.length; i++) {
      str += ", " + arrString[i];
    }
    return str;
  }

  /*execute a function when someone clicks in the document:*/
  document.addEventListener("click", function (e) {
    closeAllLists(e.target);
  });
}

$("#formLogin").submit(function(e) {
  e.preventDefault();
  var username = $("input[name=username]").val();
  var password = $("input[name=password]").val();
  var data = `username=${username}&password=${password}`;
  var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
              var c_user = this.responseText;
              if(c_user.length > 49){
                $("#login_eles").css('display', 'none');
                $("#btnLoginClick").css('display', 'none');
                $("#btnAccount").css('display', 'block');
              }else{
                alert("Wrong Account!");
              }
            }
        };
  var query = `/login`;
  xhttp.open("POST", query, true);
  xhttp.send(data);
});