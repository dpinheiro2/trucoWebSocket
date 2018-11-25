/**
 * Universidade Federal de Santa Maria
 * Pós-Graduação em Ciência da Computação
 * Tópicos em Computação Aplicada
 * Daniel Pinheiro Vargas
 * Criado em 19/07/2018.
 */

var ws;

function connect() {
    var username = $("#username").val();

    var host = document.location.host;
    var pathname = document.location.pathname;

    var turno  = false;
    var token  = false;
    var isPlaying = false;
    var envido = false;
    var flor = false;
    var truco = false;
    var isLastRaise = false;
    var trucoLevel = 0;
    var florLevel = 0;
    var isLastFlorRaise = false;

    //var path = "ws://" + host + "/truco/cbr/" + username;
    //console.log("path: ",path);

    ws = new WebSocket("ws://" + host + "/truco/cbr/" + username);
    //ws = new WebSocket("ws://" + host + pathname + "/truco/cbr/" + username);

    //console.log("host: ",host);
    //console.log("host: ", pathname);

    ws.onmessage = function (event) {

        //console.log(event.data);
        //$("#logs").append(event.data);
        $("#logs").val($("#logs").val() + "\n" + event.data);


        // Tratamento das mensagens recebidas do servidor

        var jsonData = JSON.parse(event.data);
        if (jsonData.action === "WAIT") {
            $('#btnModal').text("Aguardando oponente...");
            $('#btnModal').bind('click', false);
            notifyInfo('Aguardando oponente!')
        }

        if (jsonData.action === "START") {
            beforeInitialHandState();
            notifyInfo(jsonData.OpponentName + ' conectou. Bom jogo!')
            $("#areaJogo").show();
            $("#btnModal").hide();
            $("#myPoints").text(jsonData.playerName + ": 0");
            $("#opponentPoints").text(jsonData.OpponentName + ": 0");
            showInformation(jsonData);

            turno = jsonData.isTurn;
            token = jsonData.isToken;

            //console.log(jsonData.cartas);
            renderizarCartas(jsonData.cartas);

            if (token) {
                initialHandState(jsonData.hasFlor)
            } else {
                idleState();
            }

            //stateButtonsStart(jsonData.hasFlor);
            //enableDisableCards();
        }

        if (jsonData.action === "SHIFT_TURN") {
            $("#indTurn").text("Vez de Jogar: " + jsonData.nameTurn);
            turno = jsonData.isTurn;
            $("#indToken").text("Jogador com token: " + jsonData.nameToken);
            token = jsonData.isToken;
            $("#status").text("Status: Vez de " + jsonData.nameTurn);
        }

        if (jsonData.action === "SHIFT_TOKEN") {
            $("#indToken").text("Jogador com token: " + jsonData.nameToken);
            token = jsonData.isToken;
        }

        if (jsonData.action === "FLOR") {
            $("#status").text("Status: " + jsonData.content);
            notifyInfo(jsonData.content);
            envido = false;
            flor = true;
            isLastFlorRaise = jsonData.isPediu;
            if (token) {
                switch(jsonData.tipoFlor) {
                    case 1:
                        if (!isLastFlorRaise && jsonData.hasFlor) {
                            florState(jsonData.hasFlor);
                        } else {
                            endEnvidoPhaseState();
                        }
                        florLevel = 1;
                        break;
                    case 2:
                        endEnvidoPhaseState();
                        florLevel = 2;
                        break;
                    case 3:
                        contraFlorState();
                        florLevel = 3;
                        break;
                    case 4:
                        contraFlorFaltaState();
                        florLevel = 4;
                        break;
                    case 5:
                        contraFlorRestoState();
                        florLevel = 5;
                        break;
                }
            } else {
                idleState();
            }

        }

        if (jsonData.action === "ENVIDO") {
            $("#status").text("Status: " + jsonData.content);
            notifyInfo(jsonData.content);
            envido = true;
            if (token) {
                switch(jsonData.tipoEnvido) {
                    case 1:
                        envidoState(jsonData.hasFlor);
                        break;
                    case 2:
                        envidoEnvidoState();
                        break;
                    case 3:
                        realEnvidoState(jsonData.hasFlor, jsonData.envidoSize);
                        break;
                    case 4:
                        faltaEnvidoState(jsonData.hasFlor, jsonData.envidoSize);
                        break;
                }
            } else {
                idleState();
            }
        };

        if (jsonData.action === "TRUCO") {
            $("#status").text("Status: " + jsonData.content);
            notifyInfo(jsonData.content);
            truco = true;
            isLastRaise = jsonData.isPediu;
            if (token) {
                switch(jsonData.tipoTruco) {
                    case 1:
                        trucoState(jsonData.isHand, jsonData.hasFlor, jsonData.round, true);
                        trucoLevel = 1;
                        break;
                    case 2:
                        retrucoState(true);
                        trucoLevel = 2;
                        break;
                    case 3:
                        vale4State(true);
                        trucoLevel = 3;
                        break;
                }
            } else {
                idleState();
            }
        }

        if (jsonData.action === "ENVIDO_ERROR") {
            notifyError(jsonData.content);
        }

        if (jsonData.action === "PLAY_CARD") {
            //isPlaying  = false;
            var carta = JSON.parse(jsonData.card);
            var txt = "images/" + carta.suit + "/" + carta.face + ".jpg";
            //console.log('Round: ' + jsonData.round);
            if (jsonData.isPlayed) {
                if (jsonData.round < 3) {
                    $("#myRoundCard1").attr({src: txt});
                    $("#myRoundCard1").show();
                } else if (jsonData.round > 2 && jsonData.round < 5) {
                    $("#myRoundCard2").attr({src: txt});
                    $("#myRoundCard2").show();
                } else {
                    $("#myRoundCard3").attr({src: txt});
                    $("#myRoundCard3").show();
                }
            } else {
                if (jsonData.round < 3) {
                    $("#opponentRoundCard1").attr({src: txt});
                    $("#opponentRoundCard1").show();
                } else if (jsonData.round > 2 && jsonData.round < 5) {
                    $("#opponentRoundCard2").attr({src: txt});
                    $("#opponentRoundCard2").show();
                } else {
                    $("#opponentRoundCard3").attr({src: txt});
                    $("#opponentRoundCard3").show();
                }
            }

            if (token) {
                playCardState(jsonData.isHand, jsonData.hasFlor, jsonData.round, jsonData.trucoSize, jsonData.envidoSize, jsonData.florSize);
            } else {
                idleState();
            }
        }

        if (jsonData.action === "FACE_DOWN_CARD") {
            //isPlaying  = false;
            var txt = "images/verso.jpg";
            //console.log('Round: ' + jsonData.round);
            if (jsonData.isPlayed) {
                if (jsonData.round < 3) {
                    $("#myRoundCard1").attr({src: txt});
                    $("#myRoundCard1").show();
                } else if (jsonData.round > 2 && jsonData.round < 5) {
                    $("#myRoundCard2").attr({src: txt});
                    $("#myRoundCard2").show();
                } else {
                    $("#myRoundCard3").attr({src: txt});
                    $("#myRoundCard3").show();
                }
            } else {
                if (jsonData.round < 3) {
                    $("#opponentRoundCard1").attr({src: txt});
                    $("#opponentRoundCard1").show();
                } else if (jsonData.round > 2 && jsonData.round < 5) {
                    $("#opponentRoundCard2").attr({src: txt});
                    $("#opponentRoundCard2").show();
                } else {
                    $("#opponentRoundCard3").attr({src: txt});
                    $("#opponentRoundCard3").show();
                }
            }
            if (token) {
                playCardState(jsonData.isHand, jsonData.hasFlor, jsonData.round, jsonData.trucoSize, jsonData.envidoSize, jsonData.florSize);
            } else {
                idleState();
            }
        }

        if (jsonData.action === "RESULT_ROUND") {
            $("#status").text("Status: " + jsonData.result);
            if (!jsonData.isEmpate) {
                if (jsonData.isWinner) {
                    switch(jsonData.round) {
                        case 1:
                            $("#myRoundCard1").addClass("winner");
                            $("#opponentRoundCard1").addClass("loser");
                            break;
                        case 2:
                            $("#myRoundCard2").addClass("winner");
                            $("#opponentRoundCard2").addClass("loser");
                            break;
                        case 3:
                            $("#myRoundCard3").addClass("winner");
                            $("#opponentRoundCard3").addClass("loser");
                            break;
                    }
                } else {
                    switch(jsonData.round) {
                        case 1:
                            $("#myRoundCard1").addClass("loser");
                            $("#opponentRoundCard1").addClass("winner");
                            break;
                        case 2:
                            $("#myRoundCard2").addClass("loser");
                            $("#opponentRoundCard2").addClass("winner");
                            break;
                        case 3:
                            $("#myRoundCard3").addClass("loser");
                            $("#opponentRoundCard3").addClass("winner");
                            break;
                    }
                }

            }
            //notifyInfo(jsonData.result);
        }

        if (jsonData.action === "IR_BARALHO") {
            $("#status").text("Status: " + jsonData.content);
            notifyWarning(jsonData.content);
        }

        if (jsonData.action === "RESULT_ENVIDO") {
            $("#status").text("Status: " + jsonData.result);
            notifyInfo(jsonData.result);
            if (token) {
                endEnvidoPhaseState();
            } else {
                idleState();
            }
        }

        if (jsonData.action === "RESULT_FLOR") {
            $("#status").text("Status: " + jsonData.result);
            notifyInfo(jsonData.result);
            if (token) {
                endEnvidoPhaseState();
            } else {
                idleState();
            }
        }

        if (jsonData.action === "UPDATE_PLACAR") {
            $("#myPoints").text(jsonData.playerName + ": " + jsonData.myPoints);
            $("#opponentPoints").text(jsonData.otherName + ": " + jsonData.otherPoints);
        }

        if (jsonData.action === "FINISH_HAND") {
            notifyInfo('Fim da mão!');
            window.setTimeout(function() {
                beforeInitialHandState();
                notifyInfo('Iniciando nova mão!');
                toastr.options.closeButton = true;
                toastr.options.progressBar = true;
                showInformation(jsonData);

                turno = jsonData.isTurn;
                token = jsonData.isToken;

                //console.log(jsonData.cartas);

                renderizarCartas(jsonData.cartas);

                if (token) {
                    initialHandState(jsonData.hasFlor)
                } else {
                    idleState();
                }
            }, 4000);
        }


        if (jsonData.action === "FINISH_GAME") {
            notifyInfo(jsonData.content);
        }

        if (jsonData.action === "RESPONSE_TRUCO") {
            $("#status").text("Status: " + jsonData.content);
            notifyInfo(jsonData.content);
            if (token) {
                enableDisableAllButtons(false);
                if (jsonData.isPediu) {
                    $("#btnTrucoQuero").hide();
                    $("#btnTrucoNaoQuero").hide();
                } else {
                    $("#btnIrBaralho").removeAttr("disabled", "disabled");
                }
                enableDisableMyCards(true);
                /*if (jsonData.isPediu) {
                 showHideAllButtons(false);
                 showHideDefaultButtons(true);
                 enableDisableDefaultButtons(false);
                 } else {
                 switch(jsonData.tipoTruco) {
                 case 1:
                 trucoState(jsonData.isHand, jsonData.hasFlor, jsonData.round, false);
                 break;
                 case 2:
                 retrucoState(false);
                 break;
                 case 3:
                 vale4State(false);
                 break;
                 }
                 }*/

            } else {
                idleState();
            }
        }

    };

    /** Event Handle Buttons */

    $("#btnFlor").click(function() {
        idleState();
        if (!token) { notifyError('Não é sua vez de jogar(token)!'); return; }
        if (!flor) {
            var json = { action: "FLOR", tipo: "1" };
        } else {
            var json = { action: "FLOR", tipo: "2" };
        }
        send(json);
    });

    $("#btnEnvido").click(function() {
        idleState();
        if (!token) { notifyError('Não é sua vez de jogar(token)!'); return; }
        if (!envido) {
            var json = { action: "ENVIDO", tipo: "1" };
        } else {
            var json = { action: "ENVIDO", tipo: "2" };
        }
        send(json);
    });

    $("#btnTruco").click(function() {
        idleState();
        if (!token) { notifyError('Não é sua vez de jogar(token)!'); return; }
        var json = { action: "TRUCO", tipo: "1" };
        send(json);
    });

    $("#btnIrBaralho").click(function() {
        idleState();
        if (!turno) { notifyError('Não é sua vez de jogar!'); return; }
        var json = { action: "IR_BARALHO"};
        send(json);
    });

    $("#btnContraFlor").click(function() {
        idleState();
        if (!token) { notifyError('Não é sua vez de jogar(token)!'); return; }
        var json = { action: "FLOR", tipo: "3" };
        send(json);
    });

    $("#btnFlorFalta").click(function() {
        postAction();
        if (!token) { notifyError('Não é sua vez de jogar(token)!'); return; }
        var json = { action: "FLOR", tipo: "4" };
        send(json);
    });

    $("#btnFlorResto").click(function() {
        idleState();
        if (!token) { notifyError('Não é sua vez de jogar(token)!'); return; }
        var json = { action: "FLOR", tipo: "5" };
        send(json);
    });

    $("#btnRealEnvidor").click(function() {
        idleState();
        if (!token) { notifyError('Não é sua vez de jogar(token)!'); return; }
        var json = { action: "ENVIDO", tipo: "3" };
        send(json);
    });

    $("#btnFaltaEnvido").click(function() {
        idleState();
        if (!token) { notifyError('Não é sua vez de jogar(token)!'); return; }
        var json = { action: "ENVIDO", tipo: "4" };
        send(json);
    });

    $("#btnRetruco").click(function() {
        idleState();
        if (!token) { notifyError('Não é sua vez de jogar(token)!'); return; }
        var json = { action: "TRUCO", tipo: "2" };
        send(json);
    });

    $("#btnVale4").click(function() {
        idleState();
        if (!token) { notifyError('Não é sua vez de jogar(token)!'); return; }
        var json = { action: "TRUCO", tipo: "3" };
        send(json);
    });

    $("#btnFlorQuero").click(function() {
        idleState();
        var json = { action : "QUERO", tipo: "3"};
        send(json);
    });

    $("#btnFlorNaoQuero").click(function() {
        idleState();
        var json = { action : "NAO_QUERO", tipo: "3"};
        send(json);
    });

    $("#btnEnvidoQuero").click(function() {
        idleState();
        var json = { action : "QUERO", tipo: "1"};
        send(json);
    });

    $("#btnEnvidoNaoQuero").click(function() {
        idleState();
        var json = { action : "NAO_QUERO", tipo: "1"};
        send(json);
    });

    $("#btnTrucoQuero").click(function() {
        idleState();
        var json = { action : "QUERO", tipo: "2"};
        send(json);
    });

    $("#btnTrucoNaoQuero").click(function() {
        idleState();
        var json = { action : "NAO_QUERO", tipo: "2"};
        send(json);
    });

    $("#myCard1").dblclick(function() {
        idleState();
        var card = $("#txtMyCard1").val();
        // var txt2 = $("#myCard1").attr("src");
        // alert(txt + "-" + txt2);
        var json = { action : "PLAY_CARD", card: card};
        $("#myCard1").hide();
        send(json);
    });

    $("#myCard2").dblclick(function() {
        idleState();
        var card = $("#txtMyCard2").val();
        var json = { action : "PLAY_CARD", card: card};
        $("#myCard2").hide();
        send(json);
    });

    $("#myCard3").dblclick(function() {
        idleState();
        var card = $("#txtMyCard3").val();
        var json = { action : "PLAY_CARD", card: card};
        $("#myCard3").hide();
        send(json);
    });

    $("#myCard1").bind('contextmenu', function(e){
        idleState();
        e.preventDefault();
        var card = $("#txtMyCard1").val();
        var json = { action : "FACE_DOWN_CARD", card: card};
        $("#myCard1").hide();
        send(json);
        return false;
    });

    $("#myCard2").bind('contextmenu', function(e){
        idleState();
        e.preventDefault();
        var card = $("#txtMyCard2").val();
        var json = { action : "FACE_DOWN_CARD", card: card};
        $("#myCard2").hide();
        send(json);
        return false;
    });

    $("#myCard3").bind('contextmenu', function(e){
        idleState();
        e.preventDefault();
        var card = $("#txtMyCard3").val();
        var json = { action : "FACE_DOWN_CARD", card: card};
        $("#myCard3").hide();
        send(json);
        return false;
    });

    function send(message) {
        ws.send(JSON.stringify(message));
        console.log("ENVIANDO : " + JSON.stringify(message));
    }

    function showInformation(jsonData) {
        $("#indHand").text("Jogador Mão: " + jsonData.nameHand);
        $("#indTurn").text("Vez de Jogar: " + jsonData.nameTurn);
        $("#indToken").text("Jogador com token: " + jsonData.nameToken);
        $("#status").text("Status: Vez de " + jsonData.nameTurn);
    }

    function notifyError(msg) {
        toastr.options.closeButton = true;
        toastr.options.progressBar = true;
        toastr.error(msg);
    }

    function notifyWarning(msg) {
        toastr.options.closeButton = true;
        toastr.options.progressBar = true;
        toastr.warning(msg);
    }

    function notifyInfo(msg) {
        toastr.options.closeButton = true;
        toastr.options.progressBar = true;
        toastr.info(msg);
    }

    function renderizarCartas(cartas) {
        var cartas = JSON.parse(cartas);
        for(i in cartas.cards) {
            var item = cartas.cards[i];
            var txt = "images/" + item.suit + "/" + item.face + ".jpg";
            if (i == 0) {
                $("#myCard1").attr({src:txt});
                $("#myCard1").show();
                $("#txtMyCard1").val(JSON.stringify(item));
            } else if (i == 1) {
                $("#myCard2").attr({src:txt});
                $("#myCard2").show();
                $("#txtMyCard2").val(JSON.stringify(item));
            } else {
                $("#myCard3").attr({src:txt});
                $("#myCard3").show();
                $("#txtMyCard3").val(JSON.stringify(item));
            }

            //console.log(txt);
        }

    }

    function enableDisableCards() {
        if (turno && !isPlaying) {
            $("#myCard1").removeAttr("disabled", "disabled");
            $("#myCard2").removeAttr("disabled", "disabled");
            $("#myCard3").removeAttr("disabled", "disabled");
        } else {
            $("#myCard1").attr("disabled", "disabled");
            $("#myCard2").attr("disabled", "disabled");
            $("#myCard3").attr("disabled", "disabled");
        }
    }

    function enableDisableMyCards(aEnable) {
        if (aEnable) {
            $("#myCard1").removeAttr("disabled", "disabled");
            $("#myCard2").removeAttr("disabled", "disabled");
            $("#myCard3").removeAttr("disabled", "disabled");
        } else {
            $("#myCard1").attr("disabled", "disabled");
            $("#myCard2").attr("disabled", "disabled");
            $("#myCard3").attr("disabled", "disabled");
        }
    }

    function showHideAllButtons(aShow) {

        if (aShow) {
            $("#btnRetruco").show();
            $("#btnVale4").show();
            $("#btnFaltaEnvido").show();
            $("#btnRealEnvidor").show();
            $("#btnContraFlor").show();
            $("#btnFlorResto").show();
            $("#btnFlorFalta").show();
            $("#btnFlor").show();
            $("#btnEnvido").show();
            $("#btnTruco").show();
            $("#btnIrBaralho").show();
            $("#btnEnvidoQuero").show();
            $("#btnEnvidoNaoQuero").show();
            $("#btnTrucoQuero").show();
            $("#btnTrucoNaoQuero").show();
            $("#btnFlorQuero").show();
            $("#btnFlorNaoQuero").show();
        } else {
            $("#btnRetruco").hide();
            $("#btnVale4").hide();
            $("#btnFaltaEnvido").hide();
            $("#btnRealEnvidor").hide();
            $("#btnContraFlor").hide();
            $("#btnFlorResto").hide();
            $("#btnFlorFalta").hide();
            $("#btnFlor").hide();
            $("#btnEnvido").hide();
            $("#btnTruco").hide();
            $("#btnIrBaralho").hide();
            $("#btnEnvidoQuero").hide();
            $("#btnEnvidoNaoQuero").hide();
            $("#btnTrucoQuero").hide();
            $("#btnTrucoNaoQuero").hide();
            $("#btnFlorQuero").hide();
            $("#btnFlorNaoQuero").hide();
        }
    }

    function removeBorderRoundCards() {
        $("#myRoundCard1").removeClass("loser winner");
        $("#opponentRoundCard1").removeClass("loser winner");
        $("#myRoundCard2").removeClass("loser winner");
        $("#opponentRoundCard2").removeClass("loser winner");
        $("#myRoundCard3").removeClass("loser winner");
        $("#opponentRoundCard3").removeClass("loser winner");
    }

    function showHideAllCards(aShow) {

        if (aShow) {
            $("#myRoundCard1").show();
            $("#opponentRoundCard1").show();
            $("#myRoundCard2").show();
            $("#opponentRoundCard2").show();
            $("#myRoundCard3").show();
            $("#opponentRoundCard3").show();
            $("#myCard1").show();
            $("#myCard2").show();
            $("#myCard3").show();
        } else {
            $("#myRoundCard1").attr("src","images/transparent.png");
            $("#myRoundCard1").hide();
            $("#opponentRoundCard1").attr("src","images/transparent.png");
            $("#opponentRoundCard1").hide();
            $("#myRoundCard2").attr("src","images/transparent.png");
            $("#myRoundCard2").hide();
            $("#opponentRoundCard2").attr("src","images/transparent.png");
            $("#opponentRoundCard2").hide();
            $("#myRoundCard3").attr("src","images/transparent.png");
            $("#myRoundCard3").hide();
            $("#opponentRoundCard3").attr("src","images/transparent.png");
            $("#opponentRoundCard3").hide();
            $("#myCard1").attr("src","images/transparent.png");
            $("#myCard1").hide();
            $("#myCard2").attr("src","images/transparent.png");
            $("#myCard2").hide();
            $("#myCard3").attr("src","images/transparent.png");
            $("#myCard3").hide();
        }
    }

    function enableDisableAllButtons(aEnable) {

        if (aEnable) {
            $("#btnRetruco").removeAttr("disabled", "disabled");
            $("#btnVale4").removeAttr("disabled", "disabled");
            $("#btnFaltaEnvido").removeAttr("disabled", "disabled");
            $("#btnRealEnvidor").removeAttr("disabled", "disabled");
            $("#btnContraFlor").removeAttr("disabled", "disabled");
            $("#btnFlorResto").removeAttr("disabled", "disabled");
            $("#btnFlorFalta").removeAttr("disabled", "disabled");
            $("#btnFlor").removeAttr("disabled", "disabled");
            $("#btnEnvido").removeAttr("disabled", "disabled");
            $("#btnTruco").removeAttr("disabled", "disabled");
            $("#btnIrBaralho").removeAttr("disabled", "disabled");
            $("#btnEnvidoQuero").removeAttr("disabled", "disabled");
            $("#btnEnvidoNaoQuero").removeAttr("disabled", "disabled");
            $("#btnTrucoQuero").removeAttr("disabled", "disabled");
            $("#btnTrucoNaoQuero").removeAttr("disabled", "disabled");
            $("#btnFlorQuero").removeAttr("disabled", "disabled");
            $("#btnFlorNaoQuero").removeAttr("disabled", "disabled");
        } else {
            $("#btnRetruco").attr("disabled", "disabled");
            $("#btnVale4").attr("disabled", "disabled");
            $("#btnFaltaEnvido").attr("disabled", "disabled");
            $("#btnRealEnvidor").attr("disabled", "disabled");
            $("#btnContraFlor").attr("disabled", "disabled");
            $("#btnFlorResto").attr("disabled", "disabled");
            $("#btnFlorFalta").attr("disabled", "disabled");
            $("#btnFlor").attr("disabled", "disabled");
            $("#btnEnvido").attr("disabled", "disabled");
            $("#btnTruco").attr("disabled", "disabled");
            $("#btnIrBaralho").attr("disabled", "disabled");
            $("#btnEnvidoQuero").attr("disabled", "disabled");
            $("#btnEnvidoNaoQuero").attr("disabled", "disabled");
            $("#btnTrucoQuero").attr("disabled", "disabled");
            $("#btnTrucoNaoQuero").attr("disabled", "disabled");
            $("#btnFlorQuero").attr("disabled", "disabled");
            $("#btnFlorNaoQuero").attr("disabled", "disabled");
        }

    }

    function showHideDefaultButtons(aShow) {

        if (aShow) {
            $(".default-buttons").show();
        } else {
            $(".default-buttons").hide();
        }
    }

    function enableDisableDefaultButtons(aEnable) {

        if (aEnable) {
            $(".default-buttons").removeAttr("disabled", "disabled");
        } else {
            $(".default-buttons").attr("disabled", "disabled")
        }
    }

    function showHideTrucoButtons(aShow) {

        if (aShow) {
            $(".truco-buttons").show();
        } else {
            $(".truco-buttons").hide();
        }
    }

    function enableDisableTrucoButtons(aEnable) {

        if (aEnable) {
            $(".truco-buttons").removeAttr("disabled", "disabled");
        } else {
            $(".truco-buttons").attr("disabled", "disabled")
        }
    }

    function showHideEnvidoButtons(aShow) {

        if (aShow) {
            $(".envido-buttons").show();
        } else {
            $(".envido-buttons").hide();
        }
    }

    function enableDisableEnvidoButtons(aEnable) {

        if (aEnable) {
            $(".envido-buttons").removeAttr("disabled", "disabled");
        } else {
            $(".envido-buttons").attr("disabled", "disabled")
        }
    }

    function stateButtonsStart(hasFlor) {

        showHideAllButtons(false);
        showHideDefaultButtons(true);
        if (token) {
            enableDisableDefaultButtons(true);
            if (hasFlor) {
                $("#btnFlor").removeAttr("disabled", "disabled");
            } else {
                $("#btnFlor").attr("disabled", "disabled")
            }
        } else {
            enableDisableDefaultButtons(false);
        }
    }

    function stateButtonsEnvido(hasFlor) {

        showHideAllButtons(false);
        showHideDefaultButtons(true);
        if (token) {
            enableDisableDefaultButtons(true);
            $("#btnTruco").attr("disabled", "disabled");
            $("#btnEnvidoQuero").show();
            $("#btnEnvidoNaoQuero").show();
            $("#btnEnvidoQuero").removeAttr("disabled", "disabled");
            $("#btnEnvidoNaoQuero").removeAttr("disabled", "disabled");
            if (hasFlor) {
                $("#btnFlor").removeAttr("disabled", "disabled");
            } else {
                $("#btnFlor").attr("disabled", "disabled")
            }
        } else {
            enableDisableDefaultButtons(false);
        }
    }

    function stateButtonsEnvidoEnvido() {

        showHideAllButtons(false);
        showHideDefaultButtons(true);
        if (token) {
            enableDisableDefaultButtons(true);
            $("#btnEnvidoQuero").show();
            $("#btnEnvidoNaoQuero").show();
            $("#btnEnvidoQuero").removeAttr("disabled", "disabled");
            $("#btnEnvidoNaoQuero").removeAttr("disabled", "disabled");
            $("#btnEnvido").attr("disabled", "disabled");
            $("#btnTruco").attr("disabled", "disabled");
            $("#btnFlor").attr("disabled", "disabled");
        } else {
            enableDisableDefaultButtons(false);
        }
    }

    function stateButtonsRealEnvido(hasFlor, envidoSize) {

        showHideAllButtons(false);
        showHideDefaultButtons(true);
        if (token) {
            enableDisableDefaultButtons(true);
            $("#btnEnvidoQuero").show();
            $("#btnEnvidoNaoQuero").show();
            $("#btnEnvidoQuero").removeAttr("disabled", "disabled");
            $("#btnEnvidoNaoQuero").removeAttr("disabled", "disabled");
            $("#btnEnvido").attr("disabled", "disabled");
            $("#btnRealEnvidor").attr("disabled", "disabled");
            $("#btnTruco").attr("disabled", "disabled");
            if (hasFlor && envidoSize == 1) {
                $("#btnFlor").removeAttr("disabled", "disabled");
            } else {
                $("#btnFlor").attr("disabled", "disabled");
            }
        } else {
            enableDisableDefaultButtons(false);
        }
    }

    function stateButtonsFaltaEnvido(hasFlor, envidoSize) {

        showHideAllButtons(false);
        showHideDefaultButtons(true);
        if (token) {
            enableDisableDefaultButtons(false);
            $("#btnEnvidoQuero").show();
            $("#btnEnvidoNaoQuero").show();
            $("#btnEnvidoQuero").removeAttr("disabled", "disabled");
            $("#btnEnvidoNaoQuero").removeAttr("disabled", "disabled");
            $("#btnIrBaralho").removeAttr("disabled", "disabled");
            if (hasFlor && envidoSize == 1) {
                $("#btnFlor").removeAttr("disabled", "disabled");
            } else {
                $("#btnFlor").attr("disabled", "disabled");
            }
        } else {
            enableDisableDefaultButtons(false);
        }
    }

    function stateButtonsEnvidoFlorEnd() {

        showHideAllButtons(false);
        showHideDefaultButtons(true);
        enableDisableDefaultButtons(false);
        if (token) {
            $("#btnTruco").removeAttr("disabled", "disabled");
            $("#btnIrBaralho").removeAttr("disabled", "disabled");
        }
    }

    function stateButtonsTruco() {

        showHideAllButtons(false);
        showHideDefaultButtons(true);
        enableDisableDefaultButtons(false);
        if (token) {
            enableDisableDefaultButtons(false);
            $("#btnTrucoQuero").show();
            $("#btnTrucoNaoQuero").show();
            $("#btnTrucoQuero").removeAttr("disabled", "disabled");
            $("#btnTrucoNaoQuero").removeAttr("disabled", "disabled");
            $("#btnTruco").hide();
            $("#btnRetruco").show();
            $("#btnRetruco").removeAttr("disabled", "disabled");
            $("#btnIrBaralho").removeAttr("disabled", "disabled");
        }
    }

    function stateButtonsRetruco() {

        showHideAllButtons(false);
        showHideDefaultButtons(true);
        enableDisableDefaultButtons(false);
        if (token) {
            enableDisableDefaultButtons(false);
            $("#btnTrucoQuero").show();
            $("#btnTrucoNaoQuero").show();
            $("#btnTrucoQuero").removeAttr("disabled", "disabled");
            $("#btnTrucoNaoQuero").removeAttr("disabled", "disabled");
            $("#btnTruco").hide();
            $("#btnVale4").show();
            $("#btnVale4").removeAttr("disabled", "disabled");
            $("#btnIrBaralho").removeAttr("disabled", "disabled");
        }
    }

    function stateButtonsVale4() {

        showHideAllButtons(false);
        showHideDefaultButtons(true);
        enableDisableDefaultButtons(false);
        if (token) {
            enableDisableDefaultButtons(false);
            $("#btnTrucoQuero").show();
            $("#btnTrucoNaoQuero").show();
            $("#btnTrucoQuero").removeAttr("disabled", "disabled");
            $("#btnTrucoNaoQuero").removeAttr("disabled", "disabled");
            $("#btnIrBaralho").removeAttr("disabled", "disabled");
        }
    }

    function stateButtonsTrucoEnd() {

        showHideAllButtons(false);
        showHideDefaultButtons(true);
        enableDisableDefaultButtons(false);
        if (token) {
            $("#btnIrBaralho").removeAttr("disabled", "disabled");
        }
    }

    function stateButtonsFlor(hasFlor, isPediu) {

        showHideAllButtons(false);
        showHideDefaultButtons(true);
        enableDisableDefaultButtons(false);
        if (token) {
            if (isPediu) {
                $("#btnTruco").removeAttr("disabled", "disabled");
                $("#btnIrBaralho").removeAttr("disabled", "disabled");
            } else {
                if (hasFlor) {
                    $("#btnEnvido").hide();
                    $("#btnFaltaEnvido").hide();
                    $("#btnRealEnvidor").hide();
                    $("#btnContraFlor").show();
                    $("#btnFlorResto").show();
                    $("#btnFlorFalta").show();
                    $("#btnFlor").removeAttr("disabled", "disabled");
                    $("#btnContraFlor").removeAttr("disabled", "disabled");
                    $("#btnFlorResto").removeAttr("disabled", "disabled");
                    $("#btnFlorFalta").removeAttr("disabled", "disabled");
                    $("#btnIrBaralho").removeAttr("disabled", "disabled");
                }
            }
        }
    }

    function stateButtonsFlorFlor() {

        showHideAllButtons(false);
        showHideDefaultButtons(true);
        enableDisableDefaultButtons(false);
        if (token) {
            $("#btnTruco").removeAttr("disabled", "disabled");
            $("#btnIrBaralho").removeAttr("disabled", "disabled");
        }
    }

    function stateButtonsContraFlor() {
        showHideAllButtons(false);
        showHideDefaultButtons(true);
        enableDisableDefaultButtons(false);
        if (token) {
            $("#btnEnvido").hide();
            $("#btnFaltaEnvido").hide();
            $("#btnRealEnvidor").hide();
            $("#btnFlorResto").show();
            $("#btnFlorFalta").show();
            $("#btnFlorResto").removeAttr("disabled", "disabled");
            $("#btnFlorFalta").removeAttr("disabled", "disabled");
            $("#btnFlorQuero").show();
            $("#btnFLorNaoQuero").show();
            $("#btnFlorQuero").removeAttr("disabled", "disabled");
            $("#btnFLorNaoQuero").removeAttr("disabled", "disabled");
            $("#btnIrBaralho").removeAttr("disabled", "disabled");
        }
    }

    function stateButtonsFlorResto() {
        showHideAllButtons(false);
        showHideDefaultButtons(true);
        enableDisableDefaultButtons(false);
        if (token) {
            $("#btnEnvido").hide();
            $("#btnFaltaEnvido").hide();
            $("#btnRealEnvidor").hide();
            $("#btnFlorFalta").show();
            $("#btnFlorFalta").removeAttr("disabled", "disabled");
            $("#btnFlorQuero").show();
            $("#btnFLorNaoQuero").show();
            $("#btnFlorQuero").removeAttr("disabled", "disabled");
            $("#btnFLorNaoQuero").removeAttr("disabled", "disabled");
            $("#btnIrBaralho").removeAttr("disabled", "disabled");
        }
    }

    function stateButtonsFlorFalta() {
        showHideAllButtons(false);
        showHideDefaultButtons(true);
        enableDisableDefaultButtons(false);
        if (token) {
            $("#btnEnvido").hide();
            $("#btnFaltaEnvido").hide();
            $("#btnRealEnvidor").hide();
            $("#btnFlorQuero").show();
            $("#btnFLorNaoQuero").show();
            $("#btnFlorQuero").removeAttr("disabled", "disabled");
            $("#btnFLorNaoQuero").removeAttr("disabled", "disabled");
            $("#btnIrBaralho").removeAttr("disabled", "disabled");
        }
    }

    function stateButtonsPlayCard(round, hasFlor) {
        showHideAllButtons(false);
        showHideDefaultButtons(true);
        enableDisableDefaultButtons(false);
        if (token && !isLastRaise) {
            switch(trucoLevel) {
                case 0:
                    $("#btnTruco").removeAttr("disabled", "disabled");
                    break;
                case 1:
                    $("#btnTruco").hide();
                    $("#btnRetruco").removeAttr("disabled", "disabled");
                    $("#btnRetruco").show();
                    break;
                case 2:
                    $("#btnTruco").hide();
                    $("#btnRetruco").hide();
                    $("#btnVale4").removeAttr("disabled", "disabled");
                    $("#btnVale4").show();
                    break;
            }


            if (!envido && round == 1) {
                $("#btnEnvido").removeAttr("disabled", "disabled");
                $("#btnFaltaEnvido").removeAttr("disabled", "disabled");
                $("#btnRealEnvidor").removeAttr("disabled", "disabled");
            }

            if (hasFlor && round == 1) {
                $("#btnFlor").removeAttr("disabled", "disabled");
            }

        }

        if (token) {
            $("#btnIrBaralho").removeAttr("disabled", "disabled");
        }

    }

    function postAction() {
        //showHideAllButtons(false);
        //showHideDefaultButtons(true);
        //enableDisableAllButtons(false);
        //enableDisableCards();
    }

    ///Novos Métodos EnableDisableComponents

    function idleState() {
        showHideAllButtons(false);
        showHideDefaultButtons(true);
        enableDisableDefaultButtons(false);
        enableDisableMyCards(false);
    }

    function initialHandState(hasFlor) {
        showHideAllButtons(false);
        showHideDefaultButtons(true);
        enableDisableDefaultButtons(true);
        enableDisableMyCards(true);
        if (hasFlor) {
            $("#btnFlor").removeAttr("disabled", "disabled");
        } else {
            $("#btnFlor").attr("disabled", "disabled")
        }
    }

    function envidoState(hasFlor) {
        showHideAllButtons(false);
        showHideDefaultButtons(true);
        enableDisableDefaultButtons(true);
        showHideAcceptEnvidoButtons(true);
        $("#btnTruco").attr("disabled", "disabled");
        if (hasFlor) {
            $("#btnFlor").removeAttr("disabled", "disabled");
        } else {
            $("#btnFlor").attr("disabled", "disabled")
        }
        enableDisableMyCards(false);
    }

    function envidoEnvidoState() {
        showHideAllButtons(false);
        showHideDefaultButtons(true);
        enableDisableDefaultButtons(true);
        showHideAcceptEnvidoButtons(true);
        $("#btnTruco").attr("disabled", "disabled");
        $("#btnEnvido").attr("disabled", "disabled");
        $("#btnFlor").attr("disabled", "disabled");
        enableDisableMyCards(false);
    }

    function realEnvidoState(hasFlor, envidoSize) {
        showHideAllButtons(false);
        showHideDefaultButtons(true);
        enableDisableDefaultButtons(true);
        showHideAcceptEnvidoButtons(true);
        $("#btnTruco").attr("disabled", "disabled");
        $("#btnEnvido").attr("disabled", "disabled");
        $("#btnRealEnvidor").attr("disabled", "disabled");
        if (hasFlor && envidoSize == 1) {
            $("#btnFlor").removeAttr("disabled", "disabled");
        } else {
            $("#btnFlor").attr("disabled", "disabled");
        }
        enableDisableMyCards(false);
    }

    function faltaEnvidoState(hasFlor, envidoSize) {
        showHideAllButtons(false);
        showHideDefaultButtons(true);
        enableDisableDefaultButtons(true);
        showHideAcceptEnvidoButtons(true);
        $("#btnTruco").attr("disabled", "disabled");
        $("#btnEnvido").attr("disabled", "disabled");
        $("#btnRealEnvidor").attr("disabled", "disabled");
        $("#btnFaltaEnvido").attr("disabled", "disabled");
        if (hasFlor && envidoSize == 1) {
            $("#btnFlor").removeAttr("disabled", "disabled");
        } else {
            $("#btnFlor").attr("disabled", "disabled");
        }
        enableDisableMyCards(false);
    }

    function florState(hasFlor) {
        showHideAllButtons(false);
        showHideDefaultButtons(true);
        enableDisableDefaultButtons(false);
        if (hasFlor) {
            $("#btnFlor").removeAttr("disabled", "disabled");
            $("#btnContraFlor").show();
            $("#btnContraFlor").removeAttr("disabled", "disabled");
            //enableDisableMyCards(false);
        } else {
            $("#btnFlor").attr("disabled", "disabled")
            //enableDisableMyCards(true);
        }


        $("#btnIrBaralho").removeAttr("disabled", "disabled");

       enableDisableMyCards(true);
    }

    function florFlorState() {
        showHideAllButtons(false);
        showHideDefaultButtons(true);
        enableDisableDefaultButtons(false);
        $("#btnContraFlor").show();
        $("#btnContraFlor").removeAttr("disabled", "disabled");

        $("#btnIrBaralho").removeAttr("disabled", "disabled");

        enableDisableMyCards(false);
    }

    function contraFlorState() {
        showHideAllButtons(false);
        showHideDefaultButtons(true);
        enableDisableDefaultButtons(false);
        showHideAcceptFlorButtons(true);
        $("#btnFlorResto").show();
        $("#btnFlorFalta").show();
        $("#btnFlorResto").removeAttr("disabled", "disabled");
        $("#btnFlorFalta").removeAttr("disabled", "disabled");

        $("#btnIrBaralho").removeAttr("disabled", "disabled");

        enableDisableMyCards(false);
    }

    function contraFlorRestoState() {
        showHideAllButtons(false);
        showHideDefaultButtons(true);
        enableDisableDefaultButtons(false);
        showHideAcceptFlorButtons(true);
        $("#btnFlorFalta").show();
        $("#btnFlorFalta").removeAttr("disabled", "disabled");

        $("#btnIrBaralho").removeAttr("disabled", "disabled");

        enableDisableMyCards(false);
    }

    function contraFlorFaltaState() {
        showHideAllButtons(false);
        showHideDefaultButtons(true);
        enableDisableDefaultButtons(false);
        showHideAcceptFlorButtons(true);
        $("#btnIrBaralho").removeAttr("disabled", "disabled");

        enableDisableMyCards(false);
    }

    function endEnvidoPhaseState() {
        showHideAllButtons(false);
        showHideDefaultButtons(true);
        enableDisableDefaultButtons(false);
        $("#btnTruco").removeAttr("disabled", "disabled");
        $("#btnIrBaralho").removeAttr("disabled", "disabled");
        enableDisableMyCards(true);
    }

    function trucoState(isHand, hasFlor, round, isAcceptButtons) {
        showHideAllButtons(false);
        showHideDefaultButtons(true);
        enableDisableDefaultButtons(false);
        showHideAcceptTrucoButtons(isAcceptButtons);
        $("#btnTruco").hide();
        $("#btnRetruco").show();
        $("#btnRetruco").removeAttr("disabled", "disabled");
        $("#btnIrBaralho").removeAttr("disabled", "disabled");

        //neste caso o truco foi cantado pelo opponente na primeira rodada, eu sou o pé e tenho pontos ou flor
        //posso chamar os pontos e disputar a fase de envido e após o turno volta para o opponente que então pode
        //chamar truco
        if (!isHand && round == 1) {
            $("#btnEnvido").removeAttr("disabled", "disabled");
            $("#btnFaltaEnvido").removeAttr("disabled", "disabled");
            $("#btnRealEnvidor").removeAttr("disabled", "disabled");
            if (hasFlor) {
                $("#btnFlor").removeAttr("disabled", "disabled");
            } else {
                $("#btnFlor").attr("disabled", "disabled")
            }
        }
        enableDisableMyCards(!isAcceptButtons);
    }

    function retrucoState(isAcceptButtons) {
        showHideAllButtons(false);
        showHideDefaultButtons(true);
        enableDisableDefaultButtons(false);
        showHideAcceptTrucoButtons(isAcceptButtons);
        $("#btnTruco").hide();
        $("#btnVale4").show();
        $("#btnVale4").removeAttr("disabled", "disabled");
        $("#btnIrBaralho").removeAttr("disabled", "disabled");

        enableDisableMyCards(isAcceptButtons);

    }

    function vale4State(isAcceptButtons) {
        showHideAllButtons(false);
        showHideDefaultButtons(true);
        enableDisableDefaultButtons(false);
        showHideAcceptTrucoButtons(isAcceptButtons);
        $("#btnIrBaralho").removeAttr("disabled", "disabled");

        enableDisableMyCards(isAcceptButtons);
    }

    function playCardState(isHand, hasFlor, round, trucoSize, envidoSize, florSize) {
        showHideAllButtons(false);
        showHideDefaultButtons(true);
        enableDisableDefaultButtons(false);
        enableDisableMyCards(true);

        $("#btnIrBaralho").removeAttr("disabled", "disabled");

        if (envidoSize == 0 && florSize == 0 && round == 1 && !isHand) {
            $("#btnEnvido").removeAttr("disabled", "disabled");
            $("#btnFaltaEnvido").removeAttr("disabled", "disabled");
            $("#btnRealEnvidor").removeAttr("disabled", "disabled");
            if (hasFlor) {
                $("#btnFlor").removeAttr("disabled", "disabled");
            } else {
                $("#btnFlor").attr("disabled", "disabled")
            }
        }

        if (!isLastRaise) {

            switch(trucoLevel) {
                case 0:
                    $("#btnTruco").removeAttr("disabled", "disabled");
                    break;
                case 1:
                    $("#btnTruco").hide();
                    $("#btnRetruco").removeAttr("disabled", "disabled");
                    $("#btnRetruco").show();
                    break;
                case 2:
                    $("#btnTruco").hide();
                    $("#btnRetruco").hide();
                    $("#btnVale4").removeAttr("disabled", "disabled");
                    $("#btnVale4").show();
                    break;
            }
        }

    }

    function showHideAcceptEnvidoButtons(aEnable) {
        if (aEnable) {
            $("#btnEnvidoQuero").show();
            $("#btnEnvidoNaoQuero").show();
            $("#btnEnvidoQuero").removeAttr("disabled", "disabled");
            $("#btnEnvidoNaoQuero").removeAttr("disabled", "disabled");
        } else {
            $("#btnEnvidoQuero").hide();
            $("#btnEnvidoNaoQuero").hide();
            $("#btnEnvidoQuero").attr("disabled", "disabled");
            $("#btnEnvidoNaoQuero").attr("disabled", "disabled");
        }
    }

    function beforeInitialHandState() {
        isPlaying  = false;
        envido = false;
        flor = false;
        truco = false;
        isLastRaise = false;
        trucoLevel = 0;
        florLevel = 0;
        isLastFlorRaise = false;
        removeBorderRoundCards();
        showHideAllCards(false);
    }

    function showHideAcceptTrucoButtons(aEnable) {
        if (aEnable) {
            $("#btnTrucoQuero").show();
            $("#btnTrucoNaoQuero").show();
            $("#btnTrucoQuero").removeAttr("disabled", "disabled");
            $("#btnTrucoNaoQuero").removeAttr("disabled", "disabled");
        } else {
            $("#btnTrucoQuero").hide();
            $("#btnTrucoNaoQuero").hide();
            $("#btnTrucoQuero").attr("disabled", "disabled");
            $("#btnTrucoNaoQuero").attr("disabled", "disabled");
        }
    }

    function showHideAcceptFlorButtons(aEnable) {
        if (aEnable) {
            $("#btnFlorQuero").show();
            $("#btnFlorNaoQuero").show();
            $("#btnFlorQuero").removeAttr("disabled", "disabled");
            $("#btnFlorNaoQuero").removeAttr("disabled", "disabled");
        } else {
            $("#btnFlorQuero").hide();
            $("#btnFlorNaoQuero").hide();
            $("#btnFlorQuero").attr("disabled", "disabled");
            $("#btnFlorNaoQuero").attr("disabled", "disabled");
        }
    }

    function getRodada(round) {
        rodada = 0;

        switch (round) {
            case 0:
                rodada = 1;
                break;
            case 1:
                rodada = 1;
                break;
            case 2:
                rodada = 1;
                break;
            case 3:
                rodada = 2;
                break;
            case 4:
                rodada = 2;
                break;
            case 5:
                rodada = 3;
                break;
            case 6:
                rodada = 3;
                break;
        }

        return rodada;
    }

}
