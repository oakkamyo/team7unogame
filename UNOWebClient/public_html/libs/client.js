$(function () {

    var gid;
    var gname;
    var playerId;
    var pname;

    var connection = null;

    var displayConnection = function (msg) {
        $("#socketconnect").prepend(
                $("<div>").text(msg)
                );
    }

    var promise = $.getJSON("http://localhost:8080/UNOWebAPI/api/game/gamelist");
    promise.done(function (result) {

        for (var i = 0; i < result.length; i++) {
            $("#gamelists").append("<li><h3 class='gname' id='" + result[i].gid + "'>" + result[i].gname + "</h3></li>");
        }

    });

    $("#gamelists").on("singletap", "li", function () {

        gid = $(this).find("h3").attr("id");
        gname = $(this).find("h3").text();

        console.info(">> Game id: " + gid + ", Game Name: " + gname);
        $("#gameid").text(gid);

        $.UIGoToArticle("#addplayer");
    });


    $("#addplayerbtn").on("singletap", function () {

        $("#gname").text(gname);
        pname = $("#playername").val();
        $.post("http://localhost:8080/UNOWebAPI/api/game/addplayer", {
            gameid: gid,
            playername: $("#playername").val()

        }).done(function (result) {

            console.info("Player id :" + result.pid);
            $("#gid").val(result.gid);
            $("#playerid").val(result.pid);

            playerId = result.pid;

            connection = new WebSocket("ws://localhost:8080/UNOWebAPI/table/" + gid);
            connection.onopen = function () {
                displayConnection("websocket is connected" + gid + " " + playerId + " " + pname);
                var sendmessage = {
                    gid: gid,
                    pname: pname,
                    pid: playerId
                }
                connection.send(JSON.stringify(sendmessage));

            };

            connection.onclose = function () {
                displayConnection("websocket is closed");
            }

            connection.onmessage = function (msg) {
                console.info("incoming: ", msg.data);
                var msg = JSON.parse(msg.data);
                displayConnection("[" + msg.pname + "]");
            }

            $.UIGoToArticle("#waiting-game");
        });

    });


    var myTimer = setInterval(myCard, 1000);

    function myCard() {
        //var d = new Date();
        var status = "";
        connection.onmessage = function (msg) {
            console.info("incoming: ", msg.data);
            var newmsg = JSON.parse(msg.data);
            status = newmsg.status;
            if (status == "start") {
                displayCardView();
            }
            
            var serverflag = newmsg.serverflag;

            if (serverflag && newmsg.pid === playerId)
            {
                $('#player-cards').append('<img src="images/' + newmsg.image + '.png" alt="' + newmsg.image + '" title="' + newmsg.cid + '" />');
            }
        }
    }

    function displayCardView() {

        console.info("@>> start play: %s", playerId);
        $.get("http://localhost:8080/UNOWebAPI/api/game/startplayer/" + $("#gid").val() + "/" + $("#playerid").val())
                .done(function (result) {
                    var json = $.parseJSON(result);
                    for (var i = 0; i < json.length; i++)
                    {
                        $("#player-cards").append('<img src="' + json[i].image + '" alt="' + json[i].image + '" title="' + json[i].cid + '" class="absolute' + i + '" />');
                    }
                    $.UIGoToArticle("#deal-cards");
                });
    }
    

    $("#player-cards").on("singletap", "img", function () {
        var DiscardCardid = $(this).attr("title");
        var DiscardImg = $(this).attr("alt");
        console.info(">>>>> DiscardCardID" + DiscardCardid);
        //Sending Msg to WebSocket
        var message = {
            gid: gid,
            cid: DiscardCardid,
            image: DiscardImg,
            flag: true
        }
        connection.send(JSON.stringify(message));

        console.info(">>>>>> Discard Card In Hand " + gid + " " + DiscardCardid);
        $.post("http://localhost:8080/UNOWebAPI/api/game/discardtopile/" + gid + "/" + playerId + "/" + DiscardCardid);

   
    });

});