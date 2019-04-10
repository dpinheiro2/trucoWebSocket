package br.ufsm.topicos.websocket;

import br.ufsm.topicos.deck.Card;
import br.ufsm.topicos.enuns.*;
import br.ufsm.topicos.exceptions.ExceptionEnvido;
import br.ufsm.topicos.exceptions.ExceptionFlor;
import br.ufsm.topicos.exceptions.ExceptionTruco;
import br.ufsm.topicos.model.Game;
import br.ufsm.topicos.model.Player;
import br.ufsm.topicos.model.Round;
import br.ufsm.topicos.model.TrucoData;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.spi.JsonProvider;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;

/**
 * Universidade Federal de Santa Maria
 * Pós-Graduação em Ciência da Computação
 * Tópicos em Computação Aplicada
 * Daniel Pinheiro Vargas
 * Criado em 19/07/2018.
 */

@ServerEndpoint(value = "/truco/cbr/{username}")
public class DealerEndpoint {

    private static final Logger LOGGER = Logger.getLogger("trucoWebSocket");

    private static final List<Game> activeGames = new LinkedList<>();
    private static final Map<String, Session> sessions = new HashMap<String, Session>();
    private static Game lastGame = new Game();
    private static Gson gson = new Gson();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {

        LOGGER.info("Opening Endpoint for session " + session);

        synchronized (lastGame) {
            synchronized (activeGames) {
                String sid = session.getId();
                sessions.put(sid, session);
                if (lastGame.getPlayer1() == null || !lastGame.getPlayer1().getSession().isOpen()) {
                    //if (lastGame.getPlayer1() == null) {
                    Player player1= new Player(Integer.parseInt(sid), username, session);
                    lastGame.setPlayer1(player1);
                    LOGGER.log(Level.INFO, "Partida nº {0} - Jogador1: {1}", new Object[]{lastGame.getUid(),
                            lastGame.getPlayer1().getName()});
                    waitingOpponentMessage(lastGame.getPlayer1());
                } else {
                    Player player2= new Player(Integer.parseInt(sid), username, session);
                    lastGame.setPlayer2(player2);
                    LOGGER.log(Level.INFO, "Partida nº {0} - Jogador2: {1}", new Object[]{lastGame.getUid(),
                            lastGame.getPlayer2().getName()});
                    initGame(lastGame);
                    initMessage(lastGame.getPlayer1(), lastGame);
                    initMessage(lastGame.getPlayer2(), lastGame);
                    activeGames.add(lastGame);
                    lastGame = new Game();
                }
            }
        }

    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        JsonReader reader = Json.createReader(new StringReader(message));
        LOGGER.log(Level.INFO, "MSG RECEBIDA: {0} - Session: {1}", new Object[]{message, session});
        JsonObject messageJson = reader.readObject();


        activeGames.forEach(candidate -> {

            synchronized (candidate) {

                Player player1 = candidate.getPlayer1();
                Player player2 = candidate.getPlayer2();

                if (session == player1.getSession()) {
                    switch (messageJson.getString("action")) {
                        case "FLOR":
                            int tipoFlor = Integer.parseInt(messageJson.getString("tipo"));
                            //setStateDecision(1, tipoFlor, candidate);
                            /*if (candidate.getFlor().getFlorChain().size() == 0) {
                                candidate.addJogadaPlayer1(1, 1, tipoFlor+10, null);
                            } else {
                                candidate.addJogadaPlayer1(5, 1, tipoFlor+10, null);
                            }*/
                            this.replyFlor(player1, player2, tipoFlor, candidate);
                            break;
                        case "ENVIDO":
                            int tipoEnvido = Integer.parseInt(messageJson.getString("tipo"));
                            //setStateDecision(2, tipoEnvido, candidate);
                            /*if (candidate.getEnvido().getEnvidoChain().size() == 0) {
                                candidate.addJogadaPlayer1(1, 2, tipoEnvido+20, null);
                            } else {
                                candidate.addJogadaPlayer1(5, 2, tipoEnvido+20, null);
                            }*/
                            this.replyEnvido(player1, player2, tipoEnvido, candidate);
                            break;
                        case "TRUCO":
                            int tipoTruco = Integer.parseInt(messageJson.getString("tipo"));
                            //setStateDecision(3, tipoTruco, candidate);
                            /*if (candidate.getEnvido().getEnvidoChain().size() == 0 && candidate.getFlor().getFlorChain().size() == 0) {
                                candidate.addJogadaPlayer1(2, 2, -1, null);
                            }
                            if (candidate.getTruco().getTrucoChain().size() == 0) {
                                candidate.addJogadaPlayer1(1, 3, tipoTruco+30, null);
                            } else {
                                candidate.addJogadaPlayer1(5, 3, tipoTruco+30, null);
                            }*/
                            this.replyTruco(player1, player2, tipoTruco, candidate);
                            break;
                        case "PLAY_CARD":
                            Card card = getCardFromJson(messageJson.getString("card"));
                            candidate.getDealtCards2().put(card, player1);

                           /* if (candidate.getEnvido().getEnvidoChain().size() == 0 && candidate.getFlor().getFlorChain().size() == 0
                                    && candidate.getDealtCards2().size() < 2) {
                                candidate.addJogadaPlayer1(2, 2, -1, null);
                            }
                            if (candidate.getTruco().getTrucoChain().size() == 0) {
                                candidate.addJogadaPlayer1(2, 3, -1, null);
                            }
                            candidate.addJogadaPlayer1(8, 4, card.getCbrCode(), card.getSuit());
                            setStateDecision(candidate.getDealtCards2().size(), candidate);
                            candidate.playCardLog(candidate.getPlayer1(), card, candidate.getDealtCards2().size());*/
                            candidate.playedCard(player1, card, false);
                            shiftTurnMessage(player1, candidate);
                            playCardMessage(player1, player1, card, candidate.getDealtCards2().size(), candidate.hasFlor(player1.getCards()),
                                    player1==candidate.getPlayerHand(), candidate.getTruco().getTrucoChain().size(),
                                    candidate.getEnvido().getEnvidoChain().size(), candidate.getFlor().getFlorChain().size());
                            shiftTurnMessage(player2, candidate);
                            playCardMessage(player1, player2, card,candidate.getDealtCards2().size(), candidate.hasFlor(player2.getCards()),
                                    player2==candidate.getPlayerHand(), candidate.getTruco().getTrucoChain().size(),
                                    candidate.getEnvido().getEnvidoChain().size(), candidate.getFlor().getFlorChain().size());
                            processaRound(candidate);
                            break;
                        case "FACE_DOWN_CARD":
                            Card faceDownCard = getCardFromJson(messageJson.getString("card"));
                            //candidate.getDealtCards().add(faceDownCard);
                            candidate.getDealtCards2().put(faceDownCard, player1);
                            /*if (candidate.getEnvido().getEnvidoChain().size() == 0  && candidate.getFlor().getFlorChain().size() == 0
                                    && candidate.getDealtCards2().size() < 2) {
                                candidate.addJogadaPlayer1(2, 2, -1, null);
                            }
                            if (candidate.getTruco().getTrucoChain().size() == 0) {
                                candidate.addJogadaPlayer1(2, 3, -1, null);
                            }
                            candidate.addJogadaPlayer1(8, 5, -1, null);
                            setStateDecision(candidate.getDealtCards2().size(), candidate);*/
                            candidate.playCardLog(candidate.getPlayer1(), "FACE_DOWN_CARD",  candidate.getDealtCards2().size());
                            candidate.playedCard(player1, faceDownCard, true);
                            shiftTurnMessage(player1, candidate);
                            playCardMessage(player1, player1, candidate.getDealtCards2().size(),
                                    candidate.hasFlor(player1.getCards()),
                                    player1==candidate.getPlayerHand(), candidate.getTruco().getTrucoChain().size(),
                                    candidate.getEnvido().getEnvidoChain().size(), candidate.getFlor().getFlorChain().size());
                            shiftTurnMessage(player2, candidate);
                            playCardMessage(player1, player2, candidate.getDealtCards2().size(),
                                    candidate.hasFlor(player2.getCards()),
                                    player2==candidate.getPlayerHand(), candidate.getTruco().getTrucoChain().size(),
                                    candidate.getEnvido().getEnvidoChain().size(), candidate.getFlor().getFlorChain().size());
                            processaRound(candidate);
                            break;
                        case "QUERO":
                            int tipo = Integer.parseInt(messageJson.getString("tipo"));
                            //candidate.resetToken();
                            //shiftTokenMessage(player1, candidate);
                            //shiftTokenMessage(player2, candidate);
                            switch(tipo) {
                                case 1:
                                    /*candidate.addJogadaPlayer1(3, 2,
                                            candidate.getEnvido().getEnvidoChain().getLast().ordinal()+21, null);*/
                                    infoEnvidoMessage(player1, player1, " aceitou.");
                                    infoEnvidoMessage(player2, player1, " aceitou.");
                                    candidate.setPlayerToken(player1.equals(candidate.getPlayerHand()) ? player2 : player1);
                                    shiftTokenMessage(player1, candidate);
                                    shiftTokenMessage(player2, candidate);

                                    candidate.acceptDeclinedPointsLog(candidate.getPlayer1(), candidate.getEnvido().getEnvidoChain().getLast(), true);

                                    //candidate.setStatesDecision(StatesDecision.SHOW_POINTS);
                                    pontosEnvidoMessage(candidate.getPlayerHand(), player1.equals(candidate.getPlayerHand())
                                            ? player2 : player1, candidate);

                                    //candidate.updatePlacarEnvidoAccept();
                                    //resultEnvidoMessage(player1, player2, candidate);
                                    //resultEnvidoMessage(player2, player1, candidate);
                                    break;
                                case 2:
                                    /*candidate.addJogadaPlayer1(3, 3,
                                            candidate.getTruco().getTrucoChain().getLast().ordinal()+31, null);*/
                                    candidate.resetToken();
                                    shiftTokenMessage(player1, candidate);
                                    shiftTokenMessage(player2, candidate);

                                    candidate.acceptDeclinedGameLog(candidate.getPlayer1(), candidate.getTruco().getTrucoChain().getLast(), true);
                                    candidate.updateValorTruco(true);
                                    avisoInfoTruco("RESPONSE_TRUCO", player1,
                                            player1.getName() + " aceitou " + candidate.getTruco().getTrucoChain().getLast().toString(),
                                            candidate.getTruco().getTrucoChain().getLast().ordinal()+1, true, 0, player1==candidate.getPlayerHand(), false);
                                    avisoInfoTruco("RESPONSE_TRUCO", player2,
                                            player1.getName() + " aceitou " + candidate.getTruco().getTrucoChain().getLast().toString(),
                                            candidate.getTruco().getTrucoChain().getLast().ordinal()+1, false, 0, player2==candidate.getPlayerHand(), false);
                                    break;
                                case 3:
                                    /*candidate.addJogadaPlayer1(3, 1,
                                            candidate.getFlor().getFlorChain().getLast().ordinal()+11, null);*/
                                    candidate.resetToken();
                                    shiftTokenMessage(player1, candidate);
                                    shiftTokenMessage(player2, candidate);

                                    candidate.acceptDeclinedPointsLog(candidate.getPlayer1(), candidate.getFlor().getFlorChain().getLast(), true);
                                    candidate.updatePlacarFlorAccept();
                                    resultFlorMessage(player1, player2, candidate);
                                    resultFlorMessage(player2, player1, candidate);
                                    break;
                            }
                            if (tipo == 3) {
                                updatePlacarMessage(player1, candidate.getPlayer1Points(), candidate.getPlayer2Points(),
                                        player2.getName(), tipo, candidate, getWinner(tipo, candidate));
                                updatePlacarMessage(player2, candidate.getPlayer2Points(), candidate.getPlayer1Points(),
                                        player1.getName(), tipo, candidate, getWinner(tipo, candidate));
                            }
                            break;
                        case "NAO_QUERO":
                            int tipoEnv = Integer.parseInt(messageJson.getString("tipo"));
                            candidate.resetToken();
                            shiftTokenMessage(player1, candidate);
                            shiftTokenMessage(player2, candidate);
                            switch(tipoEnv) {
                                case 1:
                                    /*candidate.addJogadaPlayer1(4, 2,
                                            candidate.getEnvido().getEnvidoChain().getLast().ordinal()+21, null);*/
                                    candidate.getTrucoDescription().setQuemNegouEnvido(candidate.getPlayerRobo() == player1
                                            ? TrucoData.ROBO : TrucoData.HUMANO);
                                    candidate.acceptDeclinedPointsLog(candidate.getPlayer1(), candidate.getEnvido().getEnvidoChain().getLast(), false);
                                    candidate.updatePlacarEnvidoDeclined(player1);
                                    resultEnvidoMessage(player1, player1);
                                    resultEnvidoMessage(player2, player1);
                                    updatePlacarMessage(player1, candidate.getPlayer1Points(), candidate.getPlayer2Points(), player2.getName(),
                                            tipoEnv, candidate, getWinner(tipoEnv, candidate));
                                    updatePlacarMessage(player2, candidate.getPlayer2Points(), candidate.getPlayer1Points(), player1.getName(),
                                            tipoEnv, candidate, getWinner(tipoEnv, candidate));
                                    break;
                                case 2:
                                    /*candidate.addJogadaPlayer1(4, 3,
                                            candidate.getTruco().getTrucoChain().getLast().ordinal()+31, null);*/
                                    resultTrucoMessage(player1, player1);
                                    resultTrucoMessage(player2, player1);
                                    candidate.getTrucoDescription().setQuemNegouTruco(candidate.getPlayerRobo() == player1
                                            ? TrucoData.ROBO : TrucoData.HUMANO);
                                    candidate.getTrucoDescription().setQuemGanhouTruco(candidate.getPlayerRobo() == player1
                                            ? TrucoData.HUMANO : TrucoData.ROBO);
                                    candidate.acceptDeclinedGameLog(candidate.getPlayer1(), candidate.getTruco().getTrucoChain().getLast(), false);
                                    candidate.updateValorTruco(false);
                                    candidate.getTrucoDescription().setTentosTruco(candidate.getTrucoValue());
                                    candidate.updatePlacarTruco(player1);
                                    updatePlacarMessage(player1, candidate.getPlayer1Points(), candidate.getPlayer2Points(), player2.getName(),
                                            tipoEnv, candidate, getWinner(tipoEnv, candidate));
                                    updatePlacarMessage(player2, candidate.getPlayer2Points(), candidate.getPlayer1Points(), player1.getName(),
                                            tipoEnv, candidate, getWinner(tipoEnv, candidate));
                                    if (!candidate.isGameFinish()) {
                                        finishHand(candidate);
                                    } else {
                                        finishGame(candidate);
                                    }
                                    break;
                                case 3:
                                    /*candidate.addJogadaPlayer1(4, 1,
                                            candidate.getFlor().getFlorChain().getLast().ordinal()+11, null);*/
                                    candidate.getTrucoDescription().setQuemNegouFlor(candidate.getPlayerRobo() == player1
                                            ? TrucoData.ROBO : TrucoData.HUMANO);
                                    candidate.acceptDeclinedPointsLog(candidate.getPlayer1(), candidate.getFlor().getFlorChain().getLast(), false);
                                    candidate.updatePlacarFlorDeclined(player1);
                                    resultFlorMessage(player1, player1);
                                    resultFlorMessage(player2, player1);
                                    updatePlacarMessage(player1, candidate.getPlayer1Points(), candidate.getPlayer2Points(), player2.getName(),
                                            tipoEnv, candidate, getWinner(tipoEnv, candidate));
                                    updatePlacarMessage(player2, candidate.getPlayer2Points(), candidate.getPlayer1Points(), player1.getName(),
                                            tipoEnv, candidate, getWinner(tipoEnv, candidate));
                                    break;
                            }

                            break;
                        case "IR_BARALHO":
                            /*candidate.addJogadaPlayer1(6, -1, -1, null);*/
                            irBaralhoMessage(player1, player1);
                            irBaralhoMessage(player1, player2);
                            int round = candidate.getRound(candidate.getDealtCards2().size());
                            candidate.irBaralhoLog(candidate.getPlayer1(), round);
                            candidate.updatePlacarTruco(player1);
                            candidate.getTrucoDescription().setQuemBaralho(candidate.getPlayerRobo() == player1 ? TrucoData.ROBO : TrucoData.HUMANO);
                            candidate.getTrucoDescription().setQuandoBaralho(round);
                            finishGameHand(candidate);
                            break;
                        case "SHOW_POINTS":
                            boolean isShowPoints = messageJson.getBoolean("isShowPoints");
                            candidate.resetToken();
                            shiftTokenMessage(player1, candidate);
                            shiftTokenMessage(player2, candidate);
                            if (isShowPoints) {
                                /*candidate.addJogadaPlayer1(9, 2, -1, null);*/
                                candidate.updatePlacarEnvidoAccept();
                                resultEnvidoMessage(player1, player2, candidate);
                                resultEnvidoMessage(player2, player1, candidate);
                            } else {
                                /*candidate.addJogadaPlayer1(7, 2, -1, null);*/
                                candidate.updatePlacarEnvidoAccept(player1);
                                resultEnvidoMessage(player1, player2, player1, candidate);
                                resultEnvidoMessage(player2, player2, player1, candidate);
                                candidate.verifyDeceptionEnvidoAtHidePoints(player1);
                            }
                            updatePlacarMessage(player1, candidate.getPlayer1Points(), candidate.getPlayer2Points(), player2.getName(),
                                    1, candidate, getWinner(1, candidate));
                            updatePlacarMessage(player2, candidate.getPlayer2Points(), candidate.getPlayer1Points(), player1.getName(),
                                    1, candidate, getWinner(1, candidate));
                            break;
                    }
                }

                if (session == player2.getSession()) {
                    switch (messageJson.getString("action")) {
                        case "FLOR":
                            int tipoFlor = Integer.parseInt(messageJson.getString("tipo"));
                            //setStateDecision(1, tipoFlor, candidate);
                            /*if (candidate.getFlor().getFlorChain().size() == 0) {
                                candidate.addJogadaPlayer2(1, 1, tipoFlor+10, null);
                            } else {
                                candidate.addJogadaPlayer2(5, 1, tipoFlor+10, null);
                            }*/
                            this.replyFlor(player2, player1, tipoFlor, candidate);
                            break;
                        case "ENVIDO":
                            int tipoEnvido = Integer.parseInt(messageJson.getString("tipo"));
                            //setStateDecision(2, tipoEnvido, candidate);
                            /*if (candidate.getEnvido().getEnvidoChain().size() == 0) {
                                candidate.addJogadaPlayer2(1, 2, tipoEnvido+20, null);
                            } else {
                                candidate.addJogadaPlayer2(5, 2, tipoEnvido+20, null);
                            }*/
                            this.replyEnvido(player2, player1, tipoEnvido, candidate);
                            break;
                        case "TRUCO":
                            int tipoTruco = Integer.parseInt(messageJson.getString("tipo"));
                            //setStateDecision(3, tipoTruco, candidate);
                            /*if (candidate.getEnvido().getEnvidoChain().size() == 0 && candidate.getFlor().getFlorChain().size() == 0) {
                                candidate.addJogadaPlayer2(2, 2, -1, null);
                            }
                            if (candidate.getTruco().getTrucoChain().size() == 0) {
                                candidate.addJogadaPlayer2(1, 3, tipoTruco+30, null);
                            } else {
                                candidate.addJogadaPlayer2(5, 3, tipoTruco+30, null);
                            }*/
                            this.replyTruco(player2, player1, tipoTruco, candidate);
                            break;
                        case "PLAY_CARD":
                            Card card = getCardFromJson(messageJson.getString("card"));
                            //candidate.getDealtCards().add(card);
                            candidate.getDealtCards2().put(card, player2);

                            /*if (candidate.getEnvido().getEnvidoChain().size() == 0  && candidate.getFlor().getFlorChain().size() == 0
                                    && candidate.getDealtCards2().size() < 2) {
                                candidate.addJogadaPlayer2(2, 2, -1, null);
                            }
                            if (candidate.getTruco().getTrucoChain().size() == 0) {
                                candidate.addJogadaPlayer2(2, 3, -1, null);
                            }
                            candidate.addJogadaPlayer2(8, 4, card.getCbrCode(), card.getSuit());
                            setStateDecision(candidate.getDealtCards2().size(), candidate);*/
                            candidate.playCardLog(candidate.getPlayer2(), card, candidate.getDealtCards2().size());
                            candidate.playedCard(player2, card, false);
                            shiftTurnMessage(player1, candidate);
                            playCardMessage(player2, player1, card, candidate.getDealtCards2().size(), candidate.hasFlor(player1.getCards()),
                                    player1 == candidate.getPlayerHand(), candidate.getTruco().getTrucoChain().size(),
                                    candidate.getEnvido().getEnvidoChain().size(), candidate.getFlor().getFlorChain().size());
                            shiftTurnMessage(player2, candidate);
                            playCardMessage(player2, player2, card,
                                    candidate.getDealtCards2().size(), candidate.hasFlor(player2.getCards()),
                                    player2 == candidate.getPlayerHand(), candidate.getTruco().getTrucoChain().size(),
                                    candidate.getEnvido().getEnvidoChain().size(), candidate.getFlor().getFlorChain().size());
                            processaRound(candidate);
                            break;
                        case "FACE_DOWN_CARD":
                            Card faceDownCard = getCardFromJson(messageJson.getString("card"));
                            //candidate.getDealtCards().add(faceDownCard);
                            candidate.getDealtCards2().put(faceDownCard, player2);

                            /*if (candidate.getEnvido().getEnvidoChain().size() == 0  && candidate.getFlor().getFlorChain().size() == 0
                                    && candidate.getDealtCards2().size() < 2) {
                                candidate.addJogadaPlayer2(2, 2, -1, null);
                            }
                            if (candidate.getTruco().getTrucoChain().size() == 0) {
                                candidate.addJogadaPlayer2(2, 3, -1, null);
                            }
                            setStateDecision(candidate.getDealtCards2().size(), candidate);
                            candidate.addJogadaPlayer2(8, 5, -1, null);*/
                            candidate.playCardLog(candidate.getPlayer2(), "FACE_DOWN_CARD",  candidate.getDealtCards2().size());
                            candidate.playedCard(player2, faceDownCard, true);
                            shiftTurnMessage(player1, candidate);
                            playCardMessage(player2, player1, candidate.getDealtCards2().size(),
                                    candidate.hasFlor(player1.getCards()),
                                    player1 == candidate.getPlayerHand(), candidate.getTruco().getTrucoChain().size(),
                                    candidate.getEnvido().getEnvidoChain().size(), candidate.getFlor().getFlorChain().size());
                            shiftTurnMessage(player2, candidate);
                            playCardMessage(player2, player2, candidate.getDealtCards2().size(),
                                    candidate.hasFlor(player2.getCards()),
                                    player2 == candidate.getPlayerHand(), candidate.getTruco().getTrucoChain().size(),
                                    candidate.getEnvido().getEnvidoChain().size(), candidate.getFlor().getFlorChain().size());
                            processaRound(candidate);
                            break;
                        case "QUERO":
                            int tipo = Integer.parseInt(messageJson.getString("tipo"));
                            //candidate.resetToken();
                            //shiftTokenMessage(player1, candidate);
                            //shiftTokenMessage(player2, candidate);
                            switch (tipo) {
                                case 1:
                                    /*candidate.addJogadaPlayer2(3, 2,
                                            candidate.getEnvido().getEnvidoChain().getLast().ordinal()+21, null);*/
                                    infoEnvidoMessage(player1, player2, " + aceitou.");
                                    infoEnvidoMessage(player2, player2, " + aceitou.");
                                    candidate.setPlayerToken(player2.equals(candidate.getPlayerHand()) ? player1 : player2);
                                    shiftTokenMessage(player1, candidate);
                                    shiftTokenMessage(player2, candidate);

                                    candidate.acceptDeclinedPointsLog(candidate.getPlayer2(), candidate.getEnvido().getEnvidoChain().getLast(), true);

                                    //candidate.setStatesDecision(StatesDecision.SHOW_POINTS);
                                    pontosEnvidoMessage(candidate.getPlayerHand(), player2.equals(candidate.getPlayerHand())
                                            ? player1 : player2, candidate);

                                    //candidate.updatePlacarEnvidoAccept();
                                    //resultEnvidoMessage(player2, player1, candidate);
                                    //resultEnvidoMessage(player1, player2, candidate);
                                    break;
                                case 2:
                                    /*candidate.addJogadaPlayer2(3, 3,
                                            candidate.getTruco().getTrucoChain().getLast().ordinal()+31, null);*/
                                    candidate.resetToken();
                                    shiftTokenMessage(player1, candidate);
                                    shiftTokenMessage(player2, candidate);

                                    candidate.acceptDeclinedGameLog(candidate.getPlayer2(), candidate.getTruco().getTrucoChain().getLast(), true);
                                    candidate.updateValorTruco(true);
                                    avisoInfoTruco("RESPONSE_TRUCO", player1,
                                            player2.getName() + " aceitou " + candidate.getTruco().getTrucoChain().getLast().toString(),
                                            candidate.getTruco().getTrucoChain().getLast().ordinal() + 1, false, 0, player1 == candidate.getPlayerHand(), false);
                                    avisoInfoTruco("RESPONSE_TRUCO", player2,
                                            player2.getName() + " aceitou " + candidate.getTruco().getTrucoChain().getLast().toString(),
                                            candidate.getTruco().getTrucoChain().getLast().ordinal() + 1, true, 0, player2 == candidate.getPlayerHand(), false);
                                    break;
                                case 3:
                                    /*candidate.addJogadaPlayer2(3, 1,
                                            candidate.getFlor().getFlorChain().getLast().ordinal()+11, null);*/
                                    candidate.resetToken();
                                    shiftTokenMessage(player1, candidate);
                                    shiftTokenMessage(player2, candidate);

                                    candidate.acceptDeclinedPointsLog(candidate.getPlayer2(), candidate.getFlor().getFlorChain().getLast(), true);
                                    candidate.updatePlacarFlorAccept();
                                    resultFlorMessage(player1, player2, candidate);
                                    resultFlorMessage(player2, player1, candidate);
                                    break;
                            }
                            if (tipo == 3) {
                                updatePlacarMessage(player1, candidate.getPlayer1Points(), candidate.getPlayer2Points(), player2.getName(),
                                        tipo, candidate, getWinner(tipo, candidate));
                                updatePlacarMessage(player2, candidate.getPlayer2Points(), candidate.getPlayer1Points(), player1.getName(),
                                        tipo, candidate, getWinner(tipo, candidate));
                            }
                            break;
                        case "NAO_QUERO":
                            int tipoEnv = Integer.parseInt(messageJson.getString("tipo"));
                            candidate.resetToken();
                            shiftTokenMessage(player1, candidate);
                            shiftTokenMessage(player2, candidate);
                            switch (tipoEnv) {
                                case 1:
                                    /*candidate.addJogadaPlayer2(4, 2,
                                            candidate.getEnvido().getEnvidoChain().getLast().ordinal()+21, null);*/
                                    candidate.getTrucoDescription().setQuemNegouEnvido(candidate.getPlayerRobo() == player2
                                            ? TrucoData.ROBO : TrucoData.HUMANO);
                                    candidate.acceptDeclinedPointsLog(candidate.getPlayer2(), candidate.getEnvido().getEnvidoChain().getLast(), false);
                                    candidate.updatePlacarEnvidoDeclined(player2);
                                    resultEnvidoMessage(player1, player2);
                                    resultEnvidoMessage(player2, player2);
                                    updatePlacarMessage(player1, candidate.getPlayer1Points(), candidate.getPlayer2Points(), player2.getName(),
                                            tipoEnv, candidate, getWinner( tipoEnv, candidate));
                                    updatePlacarMessage(player2, candidate.getPlayer2Points(), candidate.getPlayer1Points(), player1.getName(),
                                            tipoEnv, candidate, getWinner( tipoEnv, candidate));
                                    break;
                                case 2:
                                    /*candidate.addJogadaPlayer2(4, 3,
                                            candidate.getTruco().getTrucoChain().getLast().ordinal()+31, null);*/
                                    resultTrucoMessage(player1, player2);
                                    resultTrucoMessage(player2, player2);
                                    candidate.getTrucoDescription().setQuemNegouTruco(candidate.getPlayerRobo() == player2
                                            ? TrucoData.ROBO : TrucoData.HUMANO);
                                    candidate.getTrucoDescription().setQuemGanhouTruco(candidate.getPlayerRobo() == player2
                                            ? TrucoData.HUMANO : TrucoData.ROBO);
                                    candidate.acceptDeclinedGameLog(candidate.getPlayer2(), candidate.getTruco().getTrucoChain().getLast(), false);
                                    candidate.updateValorTruco(false);
                                    candidate.getTrucoDescription().setTentosTruco(candidate.getTrucoValue());
                                    candidate.updatePlacarTruco(player2);
                                    updatePlacarMessage(player1, candidate.getPlayer1Points(), candidate.getPlayer2Points(), player2.getName(),
                                            tipoEnv, candidate, getWinner( tipoEnv, candidate));
                                    updatePlacarMessage(player2, candidate.getPlayer2Points(), candidate.getPlayer1Points(), player1.getName(),
                                            tipoEnv, candidate, getWinner( tipoEnv, candidate));
                                    if (!candidate.isGameFinish()) {
                                        finishHand(candidate);
                                    } else {
                                        finishGame(candidate);
                                    }
                                    break;
                                case 3:
                                    /*candidate.addJogadaPlayer2(4, 1,
                                            candidate.getFlor().getFlorChain().getLast().ordinal()+11, null);*/
                                    candidate.getTrucoDescription().setQuemNegouFlor(candidate.getPlayerRobo() == player2
                                            ? TrucoData.ROBO : TrucoData.HUMANO);
                                    candidate.acceptDeclinedPointsLog(candidate.getPlayer2(), candidate.getFlor().getFlorChain().getLast(), false);
                                    candidate.updatePlacarFlorDeclined(player2);
                                    resultFlorMessage(player1, player1);
                                    resultFlorMessage(player2, player1);
                                    updatePlacarMessage(player1, candidate.getPlayer1Points(), candidate.getPlayer2Points(), player2.getName(),
                                            tipoEnv, candidate, getWinner( tipoEnv, candidate));
                                    updatePlacarMessage(player2, candidate.getPlayer2Points(), candidate.getPlayer1Points(), player1.getName(),
                                            tipoEnv, candidate, getWinner( tipoEnv, candidate));
                                    break;
                            }

                            break;
                        case "IR_BARALHO":
                            /*candidate.addJogadaPlayer2(6, -1, -1, null);*/
                            irBaralhoMessage(player2, player1);
                            irBaralhoMessage(player2, player2);
                            int round = candidate.getRound(candidate.getDealtCards2().size());
                            candidate.irBaralhoLog(candidate.getPlayer2(), round);
                            candidate.updatePlacarTruco(player2);
                            candidate.getTrucoDescription().setQuemBaralho(candidate.getPlayerRobo() == player2 ? TrucoData.ROBO : TrucoData.HUMANO);
                            candidate.getTrucoDescription().setQuandoBaralho(round);
                            finishGameHand(candidate);
                            break;
                        case "SHOW_POINTS":
                            boolean isShowPoints = messageJson.getBoolean("isShowPoints");
                            candidate.resetToken();
                            shiftTokenMessage(player1, candidate);
                            shiftTokenMessage(player2, candidate);
                            if (isShowPoints) {
                                /*candidate.addJogadaPlayer2(9, 2, -1, null);*/
                                candidate.updatePlacarEnvidoAccept();
                                resultEnvidoMessage(player2, player1, candidate);
                                resultEnvidoMessage(player1, player2, candidate);

                            } else {
                                /*candidate.addJogadaPlayer2(7, 2, -1, null);*/
                                candidate.updatePlacarEnvidoAccept(player2);
                                resultEnvidoMessage(player1, player1, player2, candidate);
                                resultEnvidoMessage(player2, player1, player2, candidate);
                                candidate.verifyDeceptionEnvidoAtHidePoints(player2);
                            }
                            updatePlacarMessage(player1, candidate.getPlayer1Points(), candidate.getPlayer2Points(), player2.getName(),
                                    1, candidate, getWinner(1, candidate));
                            updatePlacarMessage(player2, candidate.getPlayer2Points(), candidate.getPlayer1Points(), player1.getName(),
                                    1, candidate, getWinner(1, candidate));
                            break;
                    }
                }
            }
        });
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) throws IOException, EncodeException {
        LOGGER.info("CLOSED! " + session);
        LOGGER.info("reason: " + reason.getCloseCode() + "-- " + reason.getReasonPhrase());
        sessions.remove(session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable){
        LOGGER.severe(throwable.getMessage() + "-- " + throwable.getCause() + "-- " + throwable.getLocalizedMessage());
        StringWriter errors = new StringWriter();
        throwable.printStackTrace(new PrintWriter(errors));
        LOGGER.severe(errors.toString());
    }

    private void initGame(Game game) {
        game.turnHandFirst();
        game.darAsCartas(game.getPlayerHand());
        game.setCardsToCase();
        game.resetEnvidoChain();
        game.resetTrucoChain();
        game.resetFlorChain();

        game.startMatchLog();
        game.startHandLog();
        game.matchScoreLog();
        game.dealtCardsLog(game.getPlayerRobo());
        game.dealtCardsLog(game.getPlayerRobo() == game.getPlayer1() ? game.getPlayer2() : game.getPlayer1());

        /*game.setStatesDecision(StatesDecision.START_HAND);*/

    }

    /////Mensagens a serem enviadas aos jogadores

    private void waitingOpponentMessage(Player player){
        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "WAIT")
                .add("content", "Aguardando oponente conectar!")
                .build();
        sendMessage(player, jsonMessage.toString());
    }

    private void initMessage(Player player, Game game){
        String jsonString = gson.toJson(player.getCards());
        JsonParser jsonParser = new JsonParser();
        com.google.gson.JsonObject cardsJsonFromString = jsonParser.parse(jsonString).getAsJsonObject();
        System.out.println(cardsJsonFromString.toString());


        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "START")
                .add("isHand", game.getPlayerHand() == player)
                .add("isTurn", game.getPlayerTurn() == player)
                .add("isToken", game.getPlayerToken() == player)
                .add("nameHand", game.getPlayerHand().getName())
                .add("nameTurn", game.getPlayerTurn().getName())
                .add("nameToken", game.getPlayerToken().getName())
                .add("playerName", player.getName())
                .add("hasFlor", game.hasFlor(player.getCards()))
                .add("OpponentName", game.getPlayer1() == player ? game.getPlayer2().getName() : game.getPlayer1().getName())
                //.add("cartas", gson.toJson(player.getCards()))
                .add("cartas", cardsJsonFromString.toString())
                .add("idPartida", game.getUid())
                .build();
        sendMessage(player, jsonMessage.toString());
    }

    private void replyFlor(Player origem, Player destino, int tipoFlor, Game game) {
        FlorLevel florLevel = null;
        if (game.getTruco().getTrucoChain().size() == 1) {
            game.getTruco().resetTrucoChain();
        }
        switch(tipoFlor) {
            case 1:
                florLevel = FlorLevel.FLOR;
                game.getTrucoDescription().setQuemFlor(game.getPlayerRobo() == origem ? TrucoData.ROBO : TrucoData.HUMANO);
                break;
            case 2:
                florLevel = FlorLevel.FLOR_FLOR;
                game.getTrucoDescription().setQuemFlorFlor(game.getPlayerRobo() == origem ? TrucoData.ROBO : TrucoData.HUMANO);
                break;
            case 3:
                florLevel = FlorLevel.CONTRA_FLOR;
                game.getTrucoDescription().setQuemContraFlor(game.getPlayerRobo() == origem ? TrucoData.ROBO : TrucoData.HUMANO);
                break;
            case 4:
                florLevel = FlorLevel.CONTRA_FLOR_FALTA;
                game.getTrucoDescription().setQuemContraFlorFalta(game.getPlayerRobo() == origem ? TrucoData.ROBO : TrucoData.HUMANO);
                break;
            case 5:
                florLevel = FlorLevel.CONTRA_FLOR_RESTO;
                game.getTrucoDescription().setQuemContraFlorResto(game.getPlayerRobo() == origem ? TrucoData.ROBO : TrucoData.HUMANO);
                break;
        }
        try {
            if (game.getFlor().getFlorChain().size() == 0) {
                game.callFlorLog(origem, florLevel.toString());
            } else {
                game.raiseFlorLog(origem, florLevel.toString());
            }
            game.florCantada(florLevel);
            if (game.getEnvido().getEnvidoChain().size() == 1) {
                game.getEnvido().resetEnvidoChain();
            }
            LOGGER.info("Player: " + origem.getName() + " cantou: " + florLevel);
            if (florLevel == FlorLevel.FLOR) {
                if (game.hasFlor(destino.getCards())) {
                    game.shiftToken();
                    LOGGER.info("Troca de token");
                    shiftTokenMessage(game.getPlayer1(), game);
                    shiftTokenMessage(game.getPlayer2(), game);
                    avisoInfoFlor("FLOR", origem, "Aguardando resposta de: " + destino.getName(), tipoFlor, true,
                            game.hasFlor(origem.getCards()), game.getFlor().getFlorChain().size());
                    avisoInfoFlor("FLOR", destino, origem.getName() + " cantou " + florLevel, tipoFlor, false,
                            game.hasFlor(destino.getCards()), game.getFlor().getFlorChain().size());
                } else {
                    game.resetToken();
                    shiftTokenMessage(game.getPlayer1(), game);
                    shiftTokenMessage(game.getPlayer2(), game);
                    avisoInfoFlor("FLOR", origem, origem.getName() + " cantou " + florLevel, tipoFlor, true,
                            game.hasFlor(origem.getCards()), game.getFlor().getFlorChain().size());
                    avisoInfoFlor("FLOR", destino, origem.getName() + " cantou " + florLevel, tipoFlor, false,
                            game.hasFlor(destino.getCards()), game.getFlor().getFlorChain().size());
                    game.updatePlacarFlorAccept(origem);
                    updatePlacarMessage(game.getPlayer1(), game.getPlayer1Points(), game.getPlayer2Points(), game.getPlayer2().getName(),
                            3, 3, origem);
                    updatePlacarMessage(game.getPlayer2(), game.getPlayer2Points(), game.getPlayer1Points(), game.getPlayer1().getName(),
                            3, 3, origem);
                }
            } else if (florLevel == FlorLevel.FLOR_FLOR) {
                game.resetToken();
                shiftTokenMessage(game.getPlayer1(), game);
                shiftTokenMessage(game.getPlayer2(), game);
                avisoInfoFlor("FLOR", origem, origem.getName() + " também cantou " + florLevel, tipoFlor, true,
                        game.hasFlor(origem.getCards()), game.getFlor().getFlorChain().size());
                avisoInfoFlor("FLOR", destino, origem.getName() + " também cantou " + florLevel, tipoFlor, false,
                        game.hasFlor(destino.getCards()), game.getFlor().getFlorChain().size());
                //game.updatePlacarFlorAccept(origem);
                game.updatePlacarFlorAccept();
                updatePlacarMessage(game.getPlayer1(), game.getPlayer1Points(), game.getPlayer2Points(), game.getPlayer2().getName(),
                        3, game, getWinner( 3, game));
                updatePlacarMessage(game.getPlayer2(), game.getPlayer2Points(), game.getPlayer1Points(), game.getPlayer1().getName(),
                        3, game, getWinner( 3, game));
            } else {
                game.shiftToken();
                LOGGER.info("Troca de token");
                shiftTokenMessage(game.getPlayer2(), game);
                shiftTokenMessage(game.getPlayer1(), game);
                avisoInfoFlor("FLOR", origem, "Aguardando resposta de: " + destino.getName(), tipoFlor, true,
                        game.hasFlor(origem.getCards()), game.getFlor().getFlorChain().size());
                avisoInfoFlor("FLOR", destino, origem.getName() + " cantou " + florLevel, tipoFlor, false,
                        game.hasFlor(destino.getCards()), game.getFlor().getFlorChain().size());
            }
        } catch(ExceptionFlor e) {
            avisoInfoFlor("FLOR_ERROR", origem, e.getMessage(), tipoFlor, false, false, 0);
        }
    }

    private void replyEnvido(Player origem, Player destino, int tipoEnvido, Game game) {
        EnvidoLevel envidoLevel = null;
        if (game.getTruco().getTrucoChain().size() == 1) {
            game.getTruco().resetTrucoChain();
        }
        game.verifyDeceptionEnvidoAtCallPoints(origem);
        switch(tipoEnvido) {
            case 1:
                envidoLevel = EnvidoLevel.ENVIDO;
                game.getTrucoDescription().setQuemPediuEnvido(game.getPlayerRobo() == origem ? TrucoData.ROBO : TrucoData.HUMANO);
                break;
            case 2:
                envidoLevel = EnvidoLevel.ENVIDO_ENVIDO;
                game.getTrucoDescription().setQuemEnvidoEnvido(game.getPlayerRobo() == origem ? TrucoData.ROBO : TrucoData.HUMANO);
                break;
            case 3:
                envidoLevel = EnvidoLevel.REAL_ENVIDO;
                game.getTrucoDescription().setQuemPediuRealEnvido(game.getPlayerRobo() == origem ? TrucoData.ROBO : TrucoData.HUMANO);
                break;
            case 4:
                envidoLevel = EnvidoLevel.FALTA_ENVIDO;
                game.getTrucoDescription().setQuemPediuFaltaEnvido(game.getPlayerRobo() == origem ? TrucoData.ROBO : TrucoData.HUMANO);
                break;
        }
        try {
            if (game.getEnvido().getEnvidoChain().size() == 0) {
                game.callPointsLog(origem, envidoLevel.toString());
            } else {
                game.raisePointsLog(origem, envidoLevel.toString());
            }
            game.envidoCantado(envidoLevel);
            LOGGER.info("Player: " + origem.getName() + " pediu: " + envidoLevel);
            game.shiftToken();
            LOGGER.info("Troca de token");
            shiftTokenMessage(game.getPlayer1(), game);
            shiftTokenMessage(game.getPlayer2(), game);
            avisoInfoEnvido("ENVIDO", origem, "Aguardando resposta de: " + destino.getName(), tipoEnvido, true,
                    game.hasFlor(origem.getCards()), game.getEnvido().getEnvidoChain().size());
            avisoInfoEnvido("ENVIDO", destino, origem.getName() + " pediu " + envidoLevel, tipoEnvido, false,
                    game.hasFlor(destino.getCards()), game.getEnvido().getEnvidoChain().size());
        } catch(ExceptionEnvido e) {
            avisoInfoEnvido("ENVIDO_ERROR", origem, e.getMessage(), tipoEnvido, false, false, 0);
        }
    }

    private void replyTruco(Player origem, Player destino, int tipoTruco, Game game) {

        game.verifyDeceptionTrucoBeforePlayCardRound1(origem, origem.equals(game.getPlayer1()) ?
                game.getPlayedCardsPlayer1().size() : game.getPlayedCardsPlayer2().size());

        game.verifyDeceptionTrucoBeforePlayCardRound2(origem, origem.equals(game.getPlayer1()) ?
                game.getPlayedCardsPlayer1() : game.getPlayedCardsPlayer2());

        game.verifyDeceptionTrucoBeforePlayCardRound3(origem, origem.equals(game.getPlayer1()) ?
                game.getPlayedCardsPlayer1() : game.getPlayedCardsPlayer2());

        TrucoLevel trucoLevel = null;
        //int round = game.getRounds().size() < 2 ? 1: game.getRounds().size();
        int round = Integer.parseInt(game.getRodada(game.getDealtCards2().size()));
        switch(tipoTruco) {
            case 1:
                trucoLevel = TrucoLevel.TRUCO;
                game.getTrucoDescription().setQuemTruco(game.getPlayerRobo() == origem ? TrucoData.ROBO : TrucoData.HUMANO);
                game.getTrucoDescription().setQuandoTruco(round);
                game.callGameLog(origem, "TRUCO", game.getDealtCards2().size());
                break;
            case 2:
                trucoLevel = TrucoLevel.RETRUCO;
                game.getTrucoDescription().setQuemRetruco(game.getPlayerRobo() == origem ? TrucoData.ROBO : TrucoData.HUMANO);
                game.getTrucoDescription().setQuandoRetruco(round);
                game.raiseGameLog(origem, "RETRUCO", game.getDealtCards2().size());
                break;
            case 3:
                trucoLevel = TrucoLevel.VALE4;
                game.getTrucoDescription().setQuemValeQuatro(game.getPlayerRobo() == origem ? TrucoData.ROBO : TrucoData.HUMANO);
                game.getTrucoDescription().setQuandoValeQuatro(round);
                game.raiseGameLog(origem, "VALE4", game.getDealtCards2().size());
                break;
        }
        try {
            game.trucoCantado(trucoLevel);
            LOGGER.info("Player: " + origem.getName() + " pediu: " + trucoLevel);
            game.shiftToken();
            LOGGER.info("Troca de token");
            shiftTokenMessage(game.getPlayer1(), game);
            shiftTokenMessage(game.getPlayer2(), game);
            avisoInfoTruco("TRUCO", origem, "Aguardando resposta de: " + destino.getName(), tipoTruco, true, round,
                    origem==game.getPlayerHand(), game.hasFlor(origem.getCards()));
            avisoInfoTruco("TRUCO", destino, origem.getName() + " pediu " + trucoLevel, tipoTruco, false, round,
                    destino==game.getPlayerHand(), game.hasFlor(origem.getCards()));
        } catch(ExceptionTruco e) {
            avisoInfoTruco("TRUCO_ERROR", origem, e.getMessage(), tipoTruco, false, 0, false, false);
        }
    }

    private void finishHand(Game game) {
        game.finishedHand();
        finishHandMessage(game.getPlayer1(), game);
        finishHandMessage(game.getPlayer2(), game);
    }

    private void finishGame(Game game) {
        game.finishedGame();
        finishGameMessage(game.getPlayer1(), game);
        finishGameMessage(game.getPlayer2(), game);
        initGame(game);
        initMessage(game.getPlayer1(), game);
        initMessage(game.getPlayer2(), game);
    }

    private void avisoInfoFlor(String action, Player player, String message, int tipoFlor, boolean isPediu, boolean hasFlor, int florSize) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject msg = provider.createObjectBuilder()
                .add("action",  action)
                .add("content", message)
                .add("tipoFlor", tipoFlor)
                .add("isPediu", isPediu)
                .add("hasFlor", hasFlor)
                .add("florSize", florSize)
                .build();
        sendMessage(player, msg.toString());
    }

    private void avisoInfoEnvido(String action, Player player, String message, int tipoEnvido, boolean isPediu, boolean hasFlor, int envidoSize) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject msg = provider.createObjectBuilder()
                .add("action",  action)
                .add("content", message)
                .add("tipoEnvido", tipoEnvido)
                .add("isPediu", isPediu)
                .add("hasFlor", hasFlor)
                .add("envidoSize", envidoSize)
                .build();
        sendMessage(player, msg.toString());
    }

    private void avisoInfoTruco(String action, Player player, String message, int tipoTruco, boolean isPediu,
                                int round, boolean isHand, boolean hasFlor) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject msg = provider.createObjectBuilder()
                .add("action",  action)
                .add("content", message)
                .add("tipoTruco", tipoTruco)
                .add("isPediu", isPediu)
                .add("round", round)
                .add("isHand", isHand)
                .add("hasFlor", hasFlor)
                .build();
        sendMessage(player, msg.toString());
    }

    private void shiftTurnMessage(Player player, Game game) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject msg = provider.createObjectBuilder()
                .add("action", "SHIFT_TURN")
                .add("isTurn", game.getPlayerTurn() == player)
                .add("nameTurn", game.getPlayerTurn().getName())
                .add("isToken", game.getPlayerTurn() == player)
                .add("nameToken", game.getPlayerTurn().getName())
                .build();
        sendMessage(player, msg.toString());

    }

    private void updatePlacarMessage(Player player, int playerPoints, int otherPoints, String otherName,
                                     int origem, Game game, Player winner) {
        int pontos = 0;
        if (origem == 1) {
            pontos = game.getTrucoDescription().getTentosEnvido();
        } else if (origem == 2) {
            pontos = game.getTrucoDescription().getTentosTruco();
        } else {
            pontos = game.getTrucoDescription().getTentosFlor();
        }
        JsonProvider provider = JsonProvider.provider();
        JsonObject msg = provider.createObjectBuilder()
                .add("action", "UPDATE_PLACAR")
                .add("myPoints", playerPoints)
                .add("playerName", player.getName())
                .add("otherPoints", otherPoints)
                .add("otherName", otherName)
                .add("isWinner", player == winner)
                .add("origem", origem)
                .add("pontos", pontos)
                .build();
        sendMessage(player, msg.toString());
    }

    private void updatePlacarMessage(Player player, int playerPoints, int otherPoints, String otherName,
                                     int origem, int pontos, Player winner) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject msg = provider.createObjectBuilder()
                .add("action", "UPDATE_PLACAR")
                .add("myPoints", playerPoints)
                .add("playerName", player.getName())
                .add("otherPoints", otherPoints)
                .add("otherName", otherName)
                .add("isWinner", player == winner)
                .add("origem", origem)
                .add("pontos", pontos)
                .build();
        sendMessage(player, msg.toString());
    }

    private void resultFlorMessage(Player player, Player otherPlayer, Game game) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject msg = provider.createObjectBuilder()
                .add("action", "RESULT_FLOR")
                .add("result", player.getName() + ": " + game.getEnvidoPoints(player.getCards()) + " X " +
                        otherPlayer.getName() + ": " + game.getEnvidoPoints(otherPlayer.getCards()))
                .build();
        sendMessage(player, msg.toString());
    }

    private void resultFlorMessage(Player player, Player origem) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject msg = provider.createObjectBuilder()
                .add("action", "RESULT_FLOR_DECLINED")
                .add("result", origem.getName() + " se achicou.")
                .add("isDeclined", player==origem)
                .build();
        sendMessage(player, msg.toString());
    }

    private void resultEnvidoMessage(Player destino, Player playerCantouEnvido, Player playerEscondeuPontos, Game game) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject msg = provider.createObjectBuilder()
                .add("action", "RESULT_ENVIDO")
                .add("result", playerCantouEnvido.getName() + ": " + game.getEnvidoPoints(playerCantouEnvido.getCards()) + " X " +
                        playerEscondeuPontos.getName() + ": Não Mostrou os pontos!")
                .add("pontosOponente", destino == playerEscondeuPontos ? -2 : -1)

                .build();
        sendMessage(destino, msg.toString());
    }

    private void resultEnvidoMessage(Player player, Player otherPlayer, Game game) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject msg = provider.createObjectBuilder()
                .add("action", "RESULT_ENVIDO")
                .add("result", player.getName() + ": " + game.getEnvidoPoints(player.getCards()) + " X " +
                        otherPlayer.getName() + ": " + game.getEnvidoPoints(otherPlayer.getCards()))
                .add("pontosOponente", game.getEnvidoPoints(otherPlayer.getCards()))
                .build();
        sendMessage(player, msg.toString());
    }

    private void resultEnvidoMessage(Player player, Player origem) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject msg = provider.createObjectBuilder()
                .add("action", "RESULT_ENVIDO_DECLINED")
                .add("result", origem.getName() + " não quis.")
                .add("isDeclined", player==origem)
                .build();
        sendMessage(player, msg.toString());
    }

    private void resultTrucoMessage(Player player, Player origem) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject msg = provider.createObjectBuilder()
                .add("action", "RESULT_TRUCO_DECLINED")
                .add("isDeclined", player==origem)
                .build();
        sendMessage(player, msg.toString());
    }

    private void infoEnvidoMessage(Player player, Player origem, String mensagem) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject msg = provider.createObjectBuilder()
                .add("action", "INFO_ENVIDO")
                .add("content", origem.getName() + mensagem)
                .build();
        sendMessage(player, msg.toString());
    }

    private void shiftTokenMessage(Player player, Game game) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject msg = provider.createObjectBuilder()
                .add("action", "SHIFT_TOKEN")
                .add("isToken", game.getPlayerToken() == player)
                .add("nameToken", game.getPlayerToken().getName())
                .build();
        sendMessage(player, msg.toString());

    }

    private void playCardMessage(Player player, Player destino, Card card, int round, boolean hasFlor, boolean isHand,
                                 int trucoSize, int envidoSize, int florSize) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject msg = provider.createObjectBuilder()
                .add("action", "PLAY_CARD")
                .add("isPlayed", player == destino)
                .add("card", gson.toJson(card))
                .add("round", round)
                .add("hasFlor", hasFlor)
                .add("isHand", isHand)
                .add("trucoSize", trucoSize)
                .add("envidoSize", envidoSize)
                .add("florSize", florSize)
                .build();
        sendMessage(destino, msg.toString());
    }

    private void playCardMessage(Player player, Player destino, int round, boolean hasFlor, boolean isHand,
                                 int trucoSize, int envidoSize, int florSize) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject msg = provider.createObjectBuilder()
                .add("action", "FACE_DOWN_CARD")
                .add("isPlayed", player == destino)
                .add("round", round)
                .add("hasFlor", hasFlor)
                .add("isHand", isHand)
                .add("trucoSize", trucoSize)
                .add("envidoSize", envidoSize)
                .add("florSize", florSize)
                .build();
        sendMessage(destino, msg.toString());
    }

    private void irBaralhoMessage(Player player, Player destino) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject msg = provider.createObjectBuilder()
                .add("action", "IR_BARALHO")
                .add("content", player.getName() + " Foi ao Baralho")
                .add("isFoiBaralho", player == destino)
                .build();
        sendMessage(destino, msg.toString());
    }

    private void resultRoundMessage(Player player, Round round, Game game) {
        String winner;
        if (round.getResult() == Result.PLAYER1) {
            winner = game.getPlayer1().getName();
        } else if (round.getResult() == Result.PLAYER2) {
            winner = game.getPlayer2().getName();
        } else {
            winner = "Empate";
        }
        JsonProvider provider = JsonProvider.provider();
        JsonObject msg = provider.createObjectBuilder()
                .add("action", "RESULT_ROUND")
                .add("result", "Resultado da Rodada: " + winner)
                .add("isWinner", player == round.getWinner())
                .add("isEmpate", round.getResult() == Result.EMPATE)
                .add("round", round.getNumber())
                .build();
        sendMessage(player, msg.toString());
    }

    private void processaRound(Game game) {
        switch (game.getRounds().size()){
            case 1:
                resultRoundMessage(game.getPlayer1(), game.getRounds().get(0), game);
                resultRoundMessage(game.getPlayer2(), game.getRounds().get(0), game);
                break;
            case 2:
                resultRoundMessage(game.getPlayer1(), game.getRounds().get(1), game);
                resultRoundMessage(game.getPlayer2(), game.getRounds().get(1), game);
                if (!game.isGameFinish()) {
                    if (game.isHandFinish(2)) {

                        updatePlacarMessage(game.getPlayer1(), game.getPlayer1Points(), game.getPlayer2Points(), game.getPlayer2().getName(),
                                2, game, getWinner(2, game));
                        updatePlacarMessage(game.getPlayer2(), game.getPlayer2Points(), game.getPlayer1Points(), game.getPlayer1().getName(),
                                2, game, getWinner(2, game));
                        finishHand(game);

                    }
                } else {
                    finishGame(game);
                }

                break;
            case 3:
                resultRoundMessage(game.getPlayer1(), game.getRounds().get(2), game);
                resultRoundMessage(game.getPlayer2(), game.getRounds().get(2), game);
                if (!game.isGameFinish()) {
                    if (game.isHandFinish(3)) {

                        updatePlacarMessage(game.getPlayer1(), game.getPlayer1Points(), game.getPlayer2Points(), game.getPlayer2().getName(),
                                2, game, getWinner(2, game));
                        updatePlacarMessage(game.getPlayer2(), game.getPlayer2Points(), game.getPlayer1Points(), game.getPlayer1().getName(),
                                2, game, getWinner(2, game));

                        finishHand(game);
                    }
                } else {
                    finishGame(game);
                }

                break;
        }

    }

    private Player getWinner(int origem, Game game) {

        Player player = null;

        if (origem == 1 ) {
            player = game.getTrucoDescription().getQuemGanhouEnvido() == TrucoData.ROBO ?
                    game.getPlayerRobo() == game.getPlayer1() ? game.getPlayer1() : game.getPlayer2() :
                    game.getPlayerRobo() == game.getPlayer1() ? game.getPlayer2() :  game.getPlayer1();
        } else if (origem == 2 ) {
            player = game.getTrucoDescription().getQuemGanhouTruco() == TrucoData.ROBO ?
                    game.getPlayerRobo() == game.getPlayer1() ? game.getPlayer1() : game.getPlayer2() :
                    game.getPlayerRobo() == game.getPlayer1() ? game.getPlayer2() :  game.getPlayer1();
        } else {
            player = game.getTrucoDescription().getQuemGanhouFlor() == TrucoData.ROBO ?
                            game.getPlayerRobo() == game.getPlayer1() ? game.getPlayer1() : game.getPlayer2() :
                            game.getPlayerRobo() == game.getPlayer1() ? game.getPlayer2() :  game.getPlayer1();
        }

        return player;
    }

    private void finishGameMessage(Player player, Game game) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "FINISH_GAME")
                .add("content", "NOVA PARTIDA INICIANDO!!!")
                .add("idPartida", game.getUid())
                .build();
        sendMessage(player, jsonMessage.toString());
    }

    private void finishHandMessage(Player player, Game game){
        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "FINISH_HAND")
                .add("isHand", game.getPlayerHand() == player)
                .add("isTurn", game.getPlayerTurn() == player)
                .add("isToken", game.getPlayerToken() == player)
                .add("nameHand", game.getPlayerHand().getName())
                .add("nameTurn", game.getPlayerTurn().getName())
                .add("nameToken", game.getPlayerToken().getName())
                .add("playerName", player.getName())
                .add("hasFlor", game.hasFlor(player.getCards()))
                .add("OpponentName", game.getPlayer1() == player ? game.getPlayer2().getName() : game.getPlayer1().getName())
                .add("cartas", gson.toJson(player.getCards()))
                .add("idPartida", game.getUid())
                .build();
        sendMessage(player, jsonMessage.toString());
    }

    private Card getCardFromJson(String card) {
        return gson.fromJson(card, Card.class);
    }

    private void sendMessage(Player player, String jsonMessage) {
        try {
            player.getSession().getBasicRemote().sendText(jsonMessage);
            LOGGER.log(Level.INFO, "ENVIANDO : {0}", jsonMessage);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void finishGameHand(Game game) {
        if (!game.isGameFinish()) {

            updatePlacarMessage(game.getPlayer1(), game.getPlayer1Points(), game.getPlayer2Points(), game.getPlayer2().getName(),
                    2, game, getWinner(2, game));
            updatePlacarMessage(game.getPlayer2(), game.getPlayer2Points(), game.getPlayer1Points(), game.getPlayer1().getName(),
                    2, game, getWinner(2, game));
            finishHand(game);
        } else {
            finishGame(game);
        }
    }


    private void pontosEnvidoMessage(Player player, Player destino, Game game) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject msg = provider.createObjectBuilder()
                .add("action", "ENVIDO_POINTS")
                .add("opponentEnvidoPoints", game.getEnvidoPoints(player.getCards()))
                .add("content", player.getName() + " cantou " + game.getEnvidoPoints(player.getCards()) + " pontos.")
                .build();
        sendMessage(destino, msg.toString());
    }

    /*private void setStateDecision(int dealtCards, Game game) {

        switch (dealtCards) {
            case 1:
                game.setStatesDecision(StatesDecision.PLAY_CARD_2);
                break;
            case 2:
                game.setStatesDecision(StatesDecision.PLAY_CARD_3);
                break;
            case 3:
                game.setStatesDecision(StatesDecision.PLAY_CARD_4);
                break;
            case 4:
                game.setStatesDecision(StatesDecision.PLAY_CARD_5);
                break;
            case 5:
                game.setStatesDecision(StatesDecision.PLAY_CARD_6);
                break;
        }

    }

    private void setStateDecision(int tipo, int nivel, Game game) {
       switch (tipo) {
           case 1:
               switch (nivel) {
                   case 1:
                       game.setStatesDecision(StatesDecision.FLOR);
                       break;
                   case 2:
                       game.setStatesDecision(StatesDecision.FLOR_FLOR);
                       break;
                   case 3:
                       game.setStatesDecision(StatesDecision.CONTRA_FLOR);
                       break;
                   case 4:
                       game.setStatesDecision(StatesDecision.CONTRA_FLOR_FALTA);
                       break;
                   case 5:
                       game.setStatesDecision(StatesDecision.CONTRA_FLOR_RESTO);
                       break;
               }
               break;
           case 2:
               switch (nivel) {
                   case 1:
                       game.setStatesDecision(StatesDecision.ENVIDO);
                       break;
                   case 2:
                       game.setStatesDecision(StatesDecision.ENVIDO_ENVIDO);
                       break;
                   case 3:
                       game.setStatesDecision(StatesDecision.REAL_ENVIDO);
                       break;
                   case 4:
                       game.setStatesDecision(StatesDecision.FALTA_ENVIDO);
                       break;
               }
               break;
           case 3:
               switch (nivel) {
                   case 1:
                       game.setStatesDecision(StatesDecision.TRUCO);
                       break;
                   case 2:
                       game.setStatesDecision(StatesDecision.RETRUCO);
                       break;
                   case 3:
                       game.setStatesDecision(StatesDecision.VALE4);
                       break;
               }
               break;
       }
    }*/

}
