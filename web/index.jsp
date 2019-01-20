<%--
  Comando da 3ª Divisão de Exército
  Adjunto da Seção de Informática
  1º Ten Vargas
  Criado em 19/07/2018.
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>UFSM :: IA Group</title>

    <link href="images/favicon.png" type="image/png" rel="icon">

    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="font-awesome/css/font-awesome.css" rel="stylesheet">

    <!-- Sweet Alert -->
    <link href="css/plugins/sweetalert/sweetalert.css" rel="stylesheet">

    <!-- Toastr style -->
    <link href="css/plugins/toastr/toastr.min.css" rel="stylesheet">

    <link href="css/animate.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">

</head>

<body class="top-navigation">

<div id="wrapper">
    <div id="page-wrapper" class="gray-bg">

        <div class="row wrapper border-bottom white-bg page-heading">
            <div class="col-sm-4">
                <h2>UFSM :: IA Group</h2>
            </div>
            <div class="col-sm-8">
                <div class="title-action">
                    <a id="btnModal" data-toggle="modal" class="btn btn-primary" href="#modal-form">Conectar ao Servidor</a>
                </div>

            </div>
        </div>
        <div id="modal-form" class="modal fade" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-sm-12"><h3 class="m-t-none m-b">Jogo de Truco</h3>
                                <p>Junta-se a uma Partida de Mano</p>
                                <form class="m-t" role="form" action="#">
                                    <div class="form-group">
                                        <input type="text" class="form-control" placeholder="Username" required="" id="username">
                                    </div>
                                    <button id="btnConnect" type="submit" onclick="connect();" class="btn btn-primary block full-width m-b" data-dismiss="modal">Conectar</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div id="areaJogo" style="display:none">
            <div class="wrapper wrapper-content">

                <div class="row">
                    <div class="col-lg-8">
                        <div class="row">
                            <div class="col-lg-4">
                                <div class="ibox ">
                                    <div class="ibox-title">
                                        <h5>Rodada 1</h5>
                                    </div>
                                    <div class="ibox-content">
                                        <div class="row">
                                            <div class="col-md-6">
                                                <div class="text-center cartas">
                                                    <small>Minha Carta</small>
                                                    <img id="myRoundCard1" src="" style="display:none"/>
                                                </div>
                                            </div>
                                            <div class="col-md-6">
                                                <div class="text-center cartas">
                                                    <small>Oponente</small>
                                                    <img id="opponentRoundCard1" src="" style="display:none"/>
                                                </div>
                                            </div>
                                        </div>

                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-4">
                                <div class="ibox ">
                                    <div class="ibox-title">
                                        <h5>Rodada 2</h5>
                                    </div>
                                    <div class="ibox-content">
                                        <div class="row">
                                            <div class="col-md-6">
                                                <div class="text-center cartas">
                                                    <small>Minha Carta</small>
                                                    <img id="myRoundCard2" src="" style="display:none"/>
                                                </div>
                                            </div>
                                            <div class="col-md-6">
                                                <div class="text-center cartas">
                                                    <small>Oponente</small>
                                                    <img id="opponentRoundCard2" src="" style="display:none"/>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-4">
                                <div class="ibox ">
                                    <div class="ibox-title">
                                        <h5>Rodada 3</h5>
                                    </div>
                                    <div class="ibox-content">
                                        <div class="row">
                                            <div class="col-md-6">
                                                <div class="text-center cartas">
                                                    <small>Minha Carta</small>
                                                    <img id="myRoundCard3" src="" style="display:none"/>
                                                </div>
                                            </div>
                                            <div class="col-md-6">
                                                <div class="text-center cartas">
                                                    <small>Oponente</small>
                                                    <img id="opponentRoundCard3" src="" style="display:none"/>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-12">
                                <div class="ibox ">
                                    <div class="ibox-title">
                                        <h5>Minhas Cartas</h5>
                                    </div>
                                    <div class="ibox-content">
                                        <div class="row">
                                            <div class="col-md-12">
                                                <div class="text-center cartas">
                                                    <input type="image" id="myCard1" class="cartas" src="" style="display:none"/></a>
                                                    <input type="image" id="myCard2" class="cartas" src="" style="display:none"/></a>
                                                    <input type="image" id="myCard3" class="cartas" src="" style="display:none"/></a>

                                                    <input type="text" id="txtMyCard1" value="" style="display:none"/>
                                                    <input type="text" id="txtMyCard2" value="" style="display:none"/>
                                                    <input type="text" id="txtMyCard3" value="" style="display:none"/>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-12">
                                <div class="ibox ">
                                    <div class="ibox-title">
                                        <h5>Ações</h5>
                                    </div>
                                    <div class="ibox-content float-e-margins">
                                        <!-- Quero quero buttons -->
                                        <div id="queroButtons" class="row">
                                            <div class="col-lg-12">
                                                <div class="text-center">
                                                    <p>
                                                        <button type="button" class="btn btn-w-m btn-success envido-buttons" id="btnEnvidoQuero">Quero</button>
                                                        <button type="button" class="btn btn-w-m btn-danger envido-buttons" id="btnEnvidoNaoQuero">Não Quero</button>

                                                        <button type="button" class="btn btn-w-m btn-success truco-buttons" id="btnTrucoQuero">Quero</button>
                                                        <button type="button" class="btn btn-w-m btn-danger truco-buttons" id="btnTrucoNaoQuero">Não Quero</button>

                                                        <button type="button" class="btn btn-w-m btn-success flor-buttons" id="btnFlorQuero">Quero</button>
                                                        <button type="button" class="btn btn-w-m btn-danger flor-buttons" id="btnFlorNaoQuero">Com Flor me achico</button>
                                                    </p>
                                                </div>
                                            </div>
                                        </div>
                                        <!-- default buttons -->
                                        <div id="defaultButtons" class="row">
                                            <div class="col-lg-12">
                                                <div class="text-center">
                                                    <p>
                                                        <button type="button" class="btn btn-w-m btn-success default-buttons truco-buttons" id="btnTruco">Truco</button>
                                                        <button type="button" class="btn btn-w-m btn-success truco-buttons" id="btnRetruco">Retruco</button>
                                                        <button type="button" class="btn btn-w-m btn-success truco-buttons"  id="btnVale4">Vale 4</button>
                                                        <button type="button" class="btn btn-w-m btn-primary default-buttons" id="btnEnvido">Envido</button>
                                                        <button type="button" class="btn btn-w-m btn-primary default-buttons envido-buttons" id="btnRealEnvidor">Real Envido</button>
                                                        <button type="button" class="btn btn-w-m btn-primary default-buttons envido-buttons" id="btnFaltaEnvido">Falta Envido</button>
                                                        <button type="button" class="btn btn-w-m btn-danger default-buttons" id="btnFlor">Flor</button>
                                                        <button type="button" class="btn btn-w-m btn-danger flor-buttons" id="btnContraFlor">Contra-flor</button>
                                                        <button type="button" class="btn btn-w-m btn-danger flor-buttons" id="btnFlorResto">Contra-flor e o resto</button>
                                                        <button type="button" class="btn btn-w-m btn-danger flor-buttons" id="btnFlorFalta">Contra-flor e a Falta</button>
                                                        <button type="button" class="btn btn-w-m btn-warning default-buttons" id="btnIrBaralho">Ir Baralho</button>
                                                    </p>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <%--<div class="row">
                            <div class="col-lg-12">
                                <div class="ibox ">
                                    <div class="ibox-title">
                                        <h5>Logs</h5>
                                    </div>
                                    <div class="ibox-content">

                                        <textarea id="logs" class="form-control border-left m-t" style="height: 500px"></textarea>

                                    </div>
                                </div>
                            </div>
                        </div>--%>
                    </div>
                    <div class="col-lg-4">
                        <div class="row">
                            <div class="col-lg-12">
                                <div class="ibox ">
                                    <div class="ibox-title">
                                        <h5>Placar</h5>
                                    </div>
                                    <div class="ibox-content">
                                        <div class="row">
                                            <div class="col-md-6">
                                                <div id="myPoints" class="font-bold m-b-xs"></div>
                                            </div>
                                            <div class="col-md-6">
                                                <div id="opponentPoints" class="font-bold m-b-xs"></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-12">
                                <div class="ibox ">
                                    <div class="ibox-title">
                                        <h5>Informações</h5>
                                    </div>
                                    <div class="ibox-content">
                                        <div id="indHand" class="font-bold m-b-xs"></div>
                                        <div id="indTurn" class="font-bold m-b-xs"></div>
                                        <div id="indToken" class="font-bold m-b-xs"></div>
                                        <hr>
                                        <div id="status" class="font-bold m-b-xs"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-12">
                                <div class="ibox ">
                                    <div class="ibox-title">
                                        <h5>Como Jogar</h5>
                                    </div>
                                    <div class="ibox-content">

                                        <p>Jogar Carta: duplo clique</p>
                                        <p>Carta virada: botão direito do mouse</p>

                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="footer">
            <div class="float-right">
                <strong>UFSM</strong>
            </div>
            <div>
                <strong>Copyright</strong> IA Group &copy; 2018
            </div>
        </div>

    </div>
</div>



<!-- Mainly scripts -->
<script src="js/jquery-3.1.1.min.js"></script>
<script src="js/popper.min.js"></script>
<script src="js/bootstrap.js"></script>
<script src="js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

<!-- Custom and plugin javascript -->
<script src="js/inspinia.js"></script>
<script src="js/plugins/pace/pace.min.js"></script>

<!-- Sweet alert -->
<script src="js/plugins/sweetalert/sweetalert.min.js"></script>

<!-- Toastr script -->
<script src="js/plugins/toastr/toastr.min.js"></script>

<script>
</script>
<script src="websocket.js"></script>

</body>

</html>

