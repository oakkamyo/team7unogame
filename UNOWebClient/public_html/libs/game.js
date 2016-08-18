$(function () {

    var currentgid;
    var currentpid;

    var connection = null;

    var displayConnection = function (msg) {
        $("#socketconnect").prepend(
                $("<div>").text(msg)
                );
    }
    

    /*--------------- Create Game ---------------*/
    $("#createbtn").on("singletap", function () {

        $.post("http://localhost:8080/UNOWebAPI/api/game/creategame", {
            gamename: $("#gamename").val()

        }).done(function (result) {
            var json = $.parseJSON(result);
            console.info(">>>>> Create Game" + result);
            alert(json.gid);


            var json = $.parseJSON(result);
            currentgid = json.gid.toString();

            connection = new WebSocket("ws://localhost:8080/UNOWebAPI/table/" + currentgid);
            connection.onopen = function () {
                displayConnection("websocket is connected");
            };

            connection.onclose = function () {
                displayConnection("websocket is closed");
            }

            connection.onmessage = function (msg) {
                console.info("incoming: ", msg.data);
                var flag = false;
                var newMsg = JSON.parse(msg.data);
                
                displayConnection(" Player Name : [" + newMsg.pname + "]");

                flag = newMsg.flag;
                if (flag)
                {
                    $("#pile-cards").append('<img src="' + newMsg.image + '" alt="' + newMsg.image + '" title="' + newMsg.image + '" class="peppable3" />');
                }

            }

            $("#gameid").text(json.gid);
            $("#gname").val(json.name);
            $.UIGoToArticle("#gamelists");
        });

    });

    /*--------------- Start Game ---------------*/
    $("#startbtn").on("singletap", function () {
        var promise = $.getJSON("http://localhost:8080/UNOWebAPI/api/game/playerlist");

        promise.done(function (result) {

            for (var i = 0; i < result.length; i++) {
                $("#droppable").append('<span class="user-img"><img class="user" src="images/user.png" />' + result[i].pname + '</span>');

                currentpid = result[i].pid;
            }

        });

        var promise = $.getJSON("http://localhost:8080/UNOWebAPI/api/game/discardcard/" + $("#gameid").text());

        promise.done(function (result) {

            $("#pile-cards").append("<img src='" + result.image + "' class='peppable3' />");

        });

        $.getJSON("http://localhost:8080/UNOWebAPI/api/game/drawpilelist/" + $("#gameid").text())
                .done(function (result) {
                    
                    console.info("BackCard List Lenght" + result.length);
                    for (var i = 0; i < result.length; i++) {
                        $("#deck-cards").append('<img src="images/back.png" alt="' + result[i].image + '" title="' + result[i].cid + '" class="peppable2"/>');
                        $('#deck-cards img').draggable();
                    }
                });

        var sendmessage = {
            gid: currentgid,
            status: "start"
        }
        connection.send(JSON.stringify(sendmessage));

        $.UIGoToArticle("#show-players");

    });

    //Draw Card to Player 
    $("#deck-cards").on("singletap", "img", function () {
        var currentDiscardCardid = $(this).attr("title");
        var currentDiscardImg = $(this).attr("alt");

        //Sending Msg to WebSocket
        var message = {
            gid: currentgid,
            pid: currentpid,
            cid: currentDiscardCardid,
            image: currentDiscardImg,
            serverflag: true
        }
        connection.send(JSON.stringify(message));
        console.info(">>>>>Player's ID Draw Card " + currentpid);

        console.info(">>>>>> Discard Card In Hand " + currentgid + " " + currentpid + " " + currentDiscardCardid);
        $.post("http://localhost:8080/UNO/api/game/drawcardtoplayer/" + currentgid + "/" + currentpid + "/" + currentDiscardCardid);

    });

});