package br.ufsm.topicos.model;

import br.ufsm.topicos.cbr.TrucoDescription;
/*import br.ufsm.topicos.deception.JogadaPlayer1;
import br.ufsm.topicos.deception.JogadaPlayer2;*/
import br.ufsm.topicos.deck.Card;
import br.ufsm.topicos.deck.Deck;
import br.ufsm.topicos.enuns.*;
import br.ufsm.topicos.exceptions.ExceptionEnvido;
import br.ufsm.topicos.exceptions.ExceptionFlor;
import br.ufsm.topicos.exceptions.ExceptionTruco;
import br.ufsm.topicos.hibernate.HibernateConfig;
import br.ufsm.topicos.log.Log;
import org.hibernate.Session;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Universidade Federal de Santa Maria
 * Pós-Graduação em Ciência da Computação
 * Tópicos em Computação Aplicada
 * Daniel Pinheiro Vargas
 * Criado em 20/07/2018.
 */

public class Game {

    private String uid;
    private Player player1;
    private Player player2;
    private Deck deck;

    private Player playerTurn;
    private Player playerHand;
    private Player playerToken;

    private Player playerRobo;

    private Envido envido;
    private Truco truco;
    private Flor flor;

    private int player1Points = 0;
    private int player2Points = 0;

    private int trucoValue = 1;

    private LinkedList<Round> rounds;
    private LinkedList<Card> dealtCards;
    private LinkedHashMap<Card, Player> dealtCards2;

    private Round round1;
    private Round round2;
    private Round round3;

    /*JogadaPlayer1 jogada1;
    JogadaPlayer2 jogada2;*/

    private LinkedList<Card> playedCardsPlayer1;
    private LinkedList<Card> playedCardsPlayer2;

    private TrucoDescription trucoDescription;

    /*private StatesDecision statesDecision;*/

    public Game() {
        this.uid = UUID.randomUUID().toString();
        this.deck = new Deck();
        this.deck.createDeck();
        init();
        HibernateConfig.buildSessionFactory();
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Player getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(Player playerTurn) {
        this.playerTurn = playerTurn;
    }

    public Player getPlayerHand() {
        return playerHand;
    }

    public void setPlayerHand(Player playerHand) {
        this.playerHand = playerHand;
    }

    public Player getPlayerToken() {
        return playerToken;
    }

    public void setPlayerToken(Player playerToken) {
        this.playerToken = playerToken;
    }

    public Envido getEnvido() {
        return envido;
    }

    public int getPlayer2Points() {
        return player2Points;
    }

    public void setPlayer2Points(int player2Points) {
        this.player2Points = player2Points;
    }

    public int getPlayer1Points() {
        return player1Points;
    }

    public void setPlayer1Points(int player1Points) {
        this.player1Points = player1Points;
    }

    public int getTrucoValue() {
        return trucoValue;
    }

    public void setTrucoValue(int trucoValue) {
        this.trucoValue = trucoValue;
    }

    public void setEnvido(Envido envido) {
        this.envido = envido;
    }

    public Flor getFlor() {
        return flor;
    }

    public void setFlor(Flor flor) {
        this.flor = flor;
    }

    public Truco getTruco() {
        return truco;
    }

    public void setTruco(Truco truco) {
        this.truco = truco;
    }

    public LinkedList<Card> getDealtCards() {
        return dealtCards;
    }

    public void setDealtCards(LinkedList<Card> dealtCards) {
        this.dealtCards = dealtCards;
    }

    public LinkedList<Round> getRounds() {
        return rounds;
    }

    public void setRounds(LinkedList<Round> rounds) {
        this.rounds = rounds;
    }

    public TrucoDescription getTrucoDescription() {
        return trucoDescription;
    }

    public void setTrucoDescription(TrucoDescription trucoDescription) {
        this.trucoDescription = trucoDescription;
    }

    public Player getPlayerRobo() {
        return playerRobo;
    }

    public void setPlayerRobo(Player playerRobo) {
        this.playerRobo = playerRobo;
    }

    public LinkedHashMap<Card, Player> getDealtCards2() {
        return dealtCards2;
    }

    public void setDealtCards2(LinkedHashMap<Card, Player> dealtCards2) {
        this.dealtCards2 = dealtCards2;
    }

    public LinkedList<Card> getPlayedCardsPlayer1() {
        return playedCardsPlayer1;
    }

    public void setPlayedCardsPlayer1(LinkedList<Card> playedCardsPlayer1) {
        this.playedCardsPlayer1 = playedCardsPlayer1;
    }

    public LinkedList<Card> getPlayedCardsPlayer2() {
        return playedCardsPlayer2;
    }

    public void setPlayedCardsPlayer2(LinkedList<Card> playedCardsPlayer2) {
        this.playedCardsPlayer2 = playedCardsPlayer2;
    }

    /*public StatesDecision getStatesDecision() {
        return statesDecision;
    }

    public void setStatesDecision(StatesDecision statesDecision) {
        this.statesDecision = statesDecision;
    }*/

    public void init() {
        deck.suffle();
        envido = new Envido();
        truco = new Truco();
        flor = new Flor();
        rounds = new LinkedList<>();
        dealtCards = new LinkedList<>();
        dealtCards2 = new LinkedHashMap<>();

        playedCardsPlayer1 = new LinkedList<>();
        playedCardsPlayer2 = new LinkedList<>();

        trucoDescription = new TrucoDescription();
        trucoDescription.setIdPartida(uid);
        trucoDescription.setTentosAnterioresRobo(playerRobo == getPlayer1() ? getPlayer1Points() : getPlayer2Points());
        trucoDescription.setTentosAnterioresHumano(playerRobo == getPlayer1() ? getPlayer2Points() : getPlayer1Points());
        /*statesDecision = StatesDecision.START_HAND;*/
    }

    public void setCardsToCase() {
        Deck deckRobo;
        Deck deckHumano;
        int ptsEnvidoHumano;
        int ptsEnvidoRobo;

        if (playerRobo == getPlayer1()) {
            deckRobo = sortCards(getPlayer1());
            deckHumano = sortCards(getPlayer2());
            ptsEnvidoRobo = getEnvidoPoints(getPlayer1().getCards());
            ptsEnvidoHumano = getEnvidoPoints(getPlayer2().getCards());
        } else {
            deckRobo = sortCards(getPlayer2());
            deckHumano = sortCards(getPlayer1());
            ptsEnvidoRobo = getEnvidoPoints(getPlayer2().getCards());
            ptsEnvidoHumano = getEnvidoPoints(getPlayer1().getCards());
        }
        trucoDescription.setCartaAltaRobo(deckRobo.getCard(2).getCbrCode());
        trucoDescription.setNaipeCartaAltaRobo(deckRobo.getCard(2).getSuit().toString());
        trucoDescription.setCartaMediaRobo(deckRobo.getCard(1).getCbrCode());
        trucoDescription.setNaipeCartaMediaRobo(deckRobo.getCard(1).getSuit().toString());
        trucoDescription.setCartaBaixaRobo(deckRobo.getCard(0).getCbrCode());
        trucoDescription.setNaipeCartaBaixaRobo(deckRobo.getCard(0).getSuit().toString());

        trucoDescription.setCartaAltaHumano(deckHumano.getCard(2).getCbrCode());
        trucoDescription.setNaipeCartaAltaHumano(deckHumano.getCard(2).getSuit().toString());
        trucoDescription.setCartaMediaHumano(deckHumano.getCard(1).getCbrCode());
        trucoDescription.setNaipeCartaMediaHumano(deckHumano.getCard(1).getSuit().toString());
        trucoDescription.setCartaBaixaHumano(deckHumano.getCard(0).getCbrCode());
        trucoDescription.setNaipeCartaBaixaHumano(deckHumano.getCard(0).getSuit().toString());

        trucoDescription.setPontosEnvidoHumano(ptsEnvidoHumano);
        trucoDescription.setPontosEnvidoRobo(ptsEnvidoRobo);
    }

    public Deck sortCards(Player player) {
        Deck sortDeck = player.getCards();
        Collections.sort(sortDeck.getCards(), Card.compareCards());
        return sortDeck;
    }

    public void darAsCartas(Player playerHand) {
        if (playerHand == player1) {
            for (int i = 0; i < 6; i++) {
                if (i % 2 == 0) {
                    player1.getCards().draw(deck, i);
                } else {
                    player2.getCards().draw(deck, i);
                }
            }
        } else {
            for (int i = 0; i < 6; i++) {
                if (i % 2 == 0) {
                    player2.getCards().draw(deck, i);
                } else {
                    player1.getCards().draw(deck, i);
                }
            }
        }
    }

    public void darAsCartasBothFlor() {
        Deck tmpBaralho = deck;
        int naipe1, naipe2;
        int count1 = 0;
        int count2 = 0;
        Random r = new Random();
        naipe1 = r.nextInt((3) + 1);
        naipe2 = r.nextInt((3) + 1);

        for (int i = 0; i < tmpBaralho.getCards().size(); i++) {
            if (count1 < 3 && tmpBaralho.getCards().get(i).getSuit().ordinal() == naipe1) {
                player1.getCards().draw(tmpBaralho, i);
                tmpBaralho.removeCard(i);
                count1++;
            }
            if (count2 < 3 && tmpBaralho.getCards().get(i).getSuit().ordinal() == naipe2) {
                player2.getCards().draw(tmpBaralho, i);
                tmpBaralho.removeCard(i);
                count2++;
            }
        }

    }

    public void darAsCartasOnlyOneFlor() {
        Deck tmpBaralho = deck;
        int naipe1;
        int count1 = 0;
        int count2 = 0;
        Random r = new Random();
        naipe1 = r.nextInt((3) + 1);


        for (int i = 0; i < tmpBaralho.getCards().size(); i++) {
            if (count1 < 3 && tmpBaralho.getCards().get(i).getSuit().ordinal() == naipe1) {
                if (player1 != playerHand) {
                    player1.getCards().draw(tmpBaralho, i);
                } else {
                    player2.getCards().draw(tmpBaralho, i);
                }

                tmpBaralho.removeCard(i);
                count1++;
            }
        }

        for (int i = 0; i < tmpBaralho.getCards().size(); i++) {
            if (count2 < 3) {
                if (player1 != playerHand) {
                    player2.getCards().draw(tmpBaralho, i);
                } else {
                    player1.getCards().draw(tmpBaralho, i);
                }

                tmpBaralho.removeCard(i);
                count2++;
            }
        }
    }

    public void darAsCartasBothManyPoints() {
        Deck tmpBaralho = deck;
        int naipe1, naipe2;
        int count1 = 0;
        int count2 = 0;
        Random r = new Random();
        naipe1 = r.nextInt((3) + 1);
        naipe2 = r.nextInt((3) + 1);

        for (int i = 0; i < tmpBaralho.getCards().size(); i++) {
            if (count1 < 2 && tmpBaralho.getCards().get(i).getSuit().ordinal() == naipe1 &&
                    tmpBaralho.getCards().get(i).getCbrCode() < 5) {
                player1.getCards().draw(tmpBaralho, i);
                tmpBaralho.removeCard(i);
                count1++;
            }
            if (count2 < 2 && tmpBaralho.getCards().get(i).getSuit().ordinal() == naipe2 &&
                    tmpBaralho.getCards().get(i).getCbrCode() < 5) {
                player2.getCards().draw(tmpBaralho, i);
                tmpBaralho.removeCard(i);
                count2++;
            }
        }

        player1.getCards().draw(tmpBaralho, 0);
        tmpBaralho.removeCard(0);
        player2.getCards().draw(tmpBaralho, 0);
    }

    public void turnHandFirst() {
        Random r     = new Random();
        playerRobo = (r.nextBoolean() ? getPlayer1() : getPlayer2());
        playerHand  = (r.nextBoolean() ? getPlayer1() : getPlayer2());
        playerTurn  = playerHand;
        playerToken = playerHand;

        System.out.println("Player 1 - " + getPlayer1().getName());
        System.out.println("Player 2 - " + getPlayer2().getName());
        System.out.println("Player Robô (1) - " + getPlayerRobo().getName());
        System.out.println("Hand Player - " + getPlayerHand().getName());


        //playerRobo = playerHand;
        //trucoDescription.setJogadorMao((playerRobo == playerHand) ? TrucoData.ROBO : TrucoData.HUMANO);
        //trucoDescription.setJogadorMao(playerRobo.getId() == playerHand.getId() ? TrucoData.ROBO : TrucoData.HUMANO);
        trucoDescription.setJogadorMao((playerRobo.equals(playerHand)) ? TrucoData.ROBO : TrucoData.HUMANO);
    }

    public void shiftTurn() {
        playerTurn = playerTurn == getPlayer1() ? getPlayer2() : getPlayer1();
        playerToken = playerTurn;
    }

    public void shiftHand() {
        playerHand = playerHand == getPlayer1() ? getPlayer2() : getPlayer1();
        playerTurn  = playerHand;
        playerToken = playerHand;
        trucoDescription.setJogadorMao((playerRobo.equals(playerHand)) ? TrucoData.ROBO : TrucoData.HUMANO);
    }

    public void shiftToken() {
        playerToken = playerToken == getPlayer1() ? getPlayer2() : getPlayer1();
    }

    public void resetToken() {
        playerToken = playerTurn;
    }

    public void resetEnvidoChain() {
        envido.resetEnvidoChain();
    }

    public void resetTrucoChain() {
        truco.resetTrucoChain();
    }

    public void resetFlorChain(){
        flor.resetFlorChain();
    }

    public void florCantada(FlorLevel florLevel) throws ExceptionFlor {
        flor.agregarFlor(florLevel);
    }

    public void envidoCantado(EnvidoLevel envidoLevel) throws ExceptionEnvido {
        envido.agregarEnvido(envidoLevel);
    }

    public void trucoCantado(TrucoLevel trucoLevel) throws ExceptionTruco {
        truco.agregarTruco(trucoLevel);
    }

    public boolean hasFlor(Deck deck) {

        return ( deck.getCards().get(0).getSuit() == deck.getCards().get(1).getSuit() &&
                deck.getCards().get(1).getSuit() == deck.getCards().get(2).getSuit() );
    }

    public int getEnvidoPoints(Deck deck)
    {
        HashMap<Suit, ArrayList<Card>> cardsBySuit = new HashMap<>();

        deck.getCards().forEach(card -> {
            if (!cardsBySuit.containsKey(card.getSuit())) {
                cardsBySuit.put(card.getSuit(), new ArrayList<>());
            }
            cardsBySuit.get(card.getSuit()).add(card);
        });

        int modeLength = 0;
        Suit modeSuit = Suit.ESPADAS;

        for(Suit suit : cardsBySuit.keySet()){
            if(cardsBySuit.get(suit).size() > modeLength){
                modeLength = cardsBySuit.get(suit).size();
                modeSuit = suit;
            }
        }
        int envidoPoints = 0;
        //System.out.println("modeLenght: " + modeLength);
        //System.out.println(cardsBySuit.get(modeSuit).toString());
        if(modeLength > 1) {
            ArrayList<Card> envidoCards = cardsBySuit.get(modeSuit);
            envidoPoints = 20;

            for(Card card : envidoCards){
                envidoPoints += card.getFace().getValue();
            }
            return envidoPoints;
        } else {
            for(Card card : deck.getCards()) {
                if(card.getFace().getValue() > envidoPoints)
                {
                    envidoPoints = card.getFace().getValue();
                }
            }
            return envidoPoints;
        }
    }

    public int getEnvidoPoints(LinkedList<Card> cards)
    {
        HashMap<Suit, ArrayList<Card>> cardsBySuit = new HashMap<>();

        cards.forEach(card -> {
            if (!cardsBySuit.containsKey(card.getSuit())) {
                cardsBySuit.put(card.getSuit(), new ArrayList<>());
            }
            cardsBySuit.get(card.getSuit()).add(card);
        });

        int modeLength = 0;
        Suit modeSuit = Suit.ESPADAS;

        for(Suit suit : cardsBySuit.keySet()){
            if(cardsBySuit.get(suit).size() > modeLength){
                modeLength = cardsBySuit.get(suit).size();
                modeSuit = suit;
            }
        }
        int envidoPoints = 0;
        //System.out.println("modeLenght: " + modeLength);
        //System.out.println(cardsBySuit.get(modeSuit).toString());
        if(modeLength > 1) {
            ArrayList<Card> envidoCards = cardsBySuit.get(modeSuit);
            envidoPoints = 20;

            for(Card card : envidoCards){
                envidoPoints += card.getFace().getValue();
            }
            return envidoPoints;
        } else {
            for(Card card : cards) {
                if(card.getFace().getValue() > envidoPoints)
                {
                    envidoPoints = card.getFace().getValue();
                }
            }
            return envidoPoints;
        }
    }

    public void updatePlacarFlorAccept(Player origem) {
        if (origem == getPlayer1()) {
            player1Points += 3;
        } else {
            player2Points += 3;
        }
    }

    public void updatePlacarFlorAccept() {

        int ptsFlorPlayer1 = getEnvidoPoints(getPlayer1().getCards());
        int ptsFlorPlayer2 = getEnvidoPoints(getPlayer2().getCards());
        int ptsGanhos;
        int winner;

        if (ptsFlorPlayer1 > ptsFlorPlayer2) {
            player1Points += flor.getPontosGanhosAceitos(player2Points);
            ptsGanhos = flor.getPontosGanhosAceitos(player2Points);
            winner = playerRobo == getPlayer1() ? TrucoData.ROBO : TrucoData.HUMANO;
        } else if (ptsFlorPlayer2 > ptsFlorPlayer1) {
            player2Points += flor.getPontosGanhosAceitos(player1Points);
            ptsGanhos = flor.getPontosGanhosAceitos(player1Points);
            winner = playerRobo == getPlayer2() ? TrucoData.ROBO : TrucoData.HUMANO;
        } else { // envidos iguales
            if (getPlayerHand() == getPlayer1()) {
                player1Points += flor.getPontosGanhosAceitos(player2Points);
                ptsGanhos = flor.getPontosGanhosAceitos(player2Points);
                winner = playerRobo == getPlayer1() ? TrucoData.ROBO : TrucoData.HUMANO;
            } else {
                player2Points += flor.getPontosGanhosAceitos(player1Points);
                ptsGanhos = flor.getPontosGanhosAceitos(player1Points);
                winner = playerRobo == getPlayer2() ? TrucoData.ROBO : TrucoData.HUMANO;
            }
        }
        resultFlorLog(winner, ptsGanhos, playerRobo == getPlayer1() ? ptsFlorPlayer1 : ptsFlorPlayer2,
                playerRobo == getPlayer1() ? ptsFlorPlayer2 : ptsFlorPlayer1);
        trucoDescription.setTentosFlor(ptsGanhos);
        trucoDescription.setTentosFlor(ptsGanhos);
        trucoDescription.setQuemGanhouFlor(winner);
        trucoDescription.setPontosFlorRobo(playerRobo == getPlayer1() ? ptsFlorPlayer1 : ptsFlorPlayer2);
        trucoDescription.setPontosFlorHumano(playerRobo == getPlayer1() ? ptsFlorPlayer2 : ptsFlorPlayer1);
    }

    public void updatePlacarFlorDeclined(Player origem) {
        if (origem == getPlayer1()) {
            player2Points += flor.getPontosGanhosNaoAceitos();
        } else {
            player1Points += flor.getPontosGanhosNaoAceitos();
        }
        resultFlorLog(playerRobo == origem ? TrucoData.HUMANO : TrucoData.ROBO, envido.getPontosGanhosNaoAceitos());
        trucoDescription.setTentosFlor(flor.getPontosGanhosNaoAceitos());
        trucoDescription.setQuemGanhouFlor(playerRobo == origem ? TrucoData.HUMANO : TrucoData.ROBO);
    }

    public void updatePlacarEnvidoDeclined(Player origem) {
        if (origem == getPlayer1()) {
            player2Points += envido.getPontosGanhosNaoAceitos();
        } else {
            player1Points += envido.getPontosGanhosNaoAceitos();
        }
        resultEnvidoLog(playerRobo == origem ? TrucoData.HUMANO : TrucoData.ROBO, envido.getPontosGanhosNaoAceitos());
        trucoDescription.setTentosEnvido(envido.getPontosGanhosNaoAceitos());
        trucoDescription.setQuemGanhouEnvido(playerRobo == origem ? TrucoData.HUMANO : TrucoData.ROBO);
    }

    public void updatePlacarEnvidoAccept(Player playerEscodeuPontos) {
        int ptsEnvidoPlayer1 = getEnvidoPoints(getPlayer1().getCards());
        int ptsEnvidoPlayer2 = getEnvidoPoints(getPlayer2().getCards());
        int ptsGanhos = playerEscodeuPontos.equals(getPlayer1()) ? envido.getPontosGanhosAceitos(player1Points) :
                envido.getPontosGanhosAceitos(player2Points);
        int winner = playerEscodeuPontos.equals(playerRobo) ? TrucoData.HUMANO : TrucoData.ROBO;

        if (playerEscodeuPontos.equals(getPlayer1())) {
            player2Points += ptsGanhos;
        } else {
            player1Points += ptsGanhos;
        }

        resultEnvidoLog(winner, ptsGanhos,
                playerEscodeuPontos.equals(playerRobo) ? -1 : getEnvidoPoints(playerHand.getCards()),
                playerEscodeuPontos.equals(playerRobo) ? getEnvidoPoints(playerHand.getCards()) : -1);
        trucoDescription.setTentosEnvido(ptsGanhos);
        trucoDescription.setQuemGanhouEnvido(winner);
        trucoDescription.setQuemEscondeuPontosEnvido(playerEscodeuPontos.equals(playerRobo) ? TrucoData.ROBO : TrucoData.HUMANO);
        //trucoDescription.setPontosEnvidoRobo(playerEscodeuPontos.equals(playerRobo) ? -1 : getEnvidoPoints(playerHand.getCards()));
        //trucoDescription.setPontosEnvidoHumano(playerEscodeuPontos.equals(playerRobo) ? getEnvidoPoints(playerHand.getCards()) : -1);
    }

    public void updatePlacarEnvidoAccept() {
        int ptsEnvidoPlayer1 = getEnvidoPoints(getPlayer1().getCards());
        int ptsEnvidoPlayer2 = getEnvidoPoints(getPlayer2().getCards());
        int ptsGanhos;
        int winner;

        if (ptsEnvidoPlayer1 > ptsEnvidoPlayer2) {
            player1Points += envido.getPontosGanhosAceitos(player2Points);
            ptsGanhos = envido.getPontosGanhosAceitos(player2Points);
            winner = playerRobo == getPlayer1() ? TrucoData.ROBO : TrucoData.HUMANO;
        } else if (ptsEnvidoPlayer2 > ptsEnvidoPlayer1) {
            player2Points += envido.getPontosGanhosAceitos(player1Points);
            ptsGanhos = envido.getPontosGanhosAceitos(player1Points);
            winner = playerRobo == getPlayer2() ? TrucoData.ROBO : TrucoData.HUMANO;
        } else {
            if (getPlayerHand() == getPlayer1()) {
                player1Points += envido.getPontosGanhosAceitos(player2Points);
                ptsGanhos = envido.getPontosGanhosAceitos(player2Points);
                winner = playerRobo == getPlayer1() ? TrucoData.ROBO : TrucoData.HUMANO;
            } else {
                player2Points += envido.getPontosGanhosAceitos(player1Points);
                ptsGanhos = envido.getPontosGanhosAceitos(player1Points);
                winner = playerRobo == getPlayer2() ? TrucoData.ROBO : TrucoData.HUMANO;
            }
        }
        resultEnvidoLog(winner, ptsGanhos, playerRobo == getPlayer1() ? ptsEnvidoPlayer1 : ptsEnvidoPlayer2,
                playerRobo == getPlayer1() ? ptsEnvidoPlayer2 : ptsEnvidoPlayer1);
        trucoDescription.setTentosEnvido(ptsGanhos);
        trucoDescription.setQuemGanhouEnvido(winner);
        //trucoDescription.setPontosEnvidoRobo(playerRobo == getPlayer1() ? ptsEnvidoPlayer1 : ptsEnvidoPlayer2);
        //trucoDescription.setPontosEnvidoHumano(playerRobo == getPlayer1() ? ptsEnvidoPlayer2 : ptsEnvidoPlayer1);

    }

    public void updatePlacarTruco(Player origem) {
        if (origem == getPlayer1()) {
            player2Points += trucoValue;
            trucoDescription.setQuemGanhouTruco(playerRobo == getPlayer2() ? TrucoData.ROBO : TrucoData.HUMANO);
            resultTrucoLog(playerRobo == getPlayer2() ? TrucoData.ROBO : TrucoData.HUMANO, trucoValue);
        } else {
            player1Points += trucoValue;
            trucoDescription.setQuemGanhouTruco(playerRobo == getPlayer1() ? TrucoData.ROBO : TrucoData.HUMANO);
            resultTrucoLog(playerRobo == getPlayer1() ? TrucoData.ROBO : TrucoData.HUMANO, trucoValue);
        }
        trucoDescription.setTentosTruco(trucoValue);
    }

    public void updatePlacarRoundWinnerTruco(Player player) {
        if (player == getPlayer1()) {
            player1Points += trucoValue;
            trucoDescription.setQuemGanhouTruco(playerRobo == getPlayer1() ? TrucoData.ROBO : TrucoData.HUMANO);
            resultTrucoLog(playerRobo == getPlayer1() ? TrucoData.ROBO : TrucoData.HUMANO, trucoValue);
        } else {
            player2Points += trucoValue;
            trucoDescription.setQuemGanhouTruco(playerRobo == getPlayer2() ? TrucoData.ROBO : TrucoData.HUMANO);
            resultTrucoLog(playerRobo == getPlayer2() ? TrucoData.ROBO : TrucoData.HUMANO, trucoValue);
        }
        trucoDescription.setTentosTruco(trucoValue);

    }

    public void updateValorTruco(boolean isAccept) {
        trucoValue = isAccept ? truco.getPontosGanhosAceitos() : truco.getPontosGanhosNaoAceitos();
    }

    public void playedCard(Player player, Card card, boolean faceDown) {
        if (getPlayer1() == player) {
            playedCardsPlayer1.add(card);
        } else {
            playedCardsPlayer2.add(card);
        }
        switch (dealtCards2.size()) {
            case 1:
                round1 = new Round(1);
                if (faceDown) {
                    if (getPlayer1() == player) {
                        round1.setFaceDownCardPlayer1(true);
                    } else {
                        round1.setFaceDownCardPlayer2(true);
                    }
                    if (playerRobo == player) {
                        trucoDescription.setRoboCartaVirada(1);
                    } else {
                        trucoDescription.setHumanoCartaVirada(1);
                    }
                } else {
                    if (getPlayer1() == player) {
                        round1.setPlayer1Card(card);
                    } else {
                        round1.setPlayer2Card(card);
                    }
                    if (playerRobo == player) {
                        trucoDescription.setPrimeiraCartaRobo(card.getCbrCode());
                        trucoDescription.setNaipePrimeiraCartaRobo(card.getSuit().toString());

                    } else {
                        trucoDescription.setPrimeiraCartaHumano(card.getCbrCode());
                        trucoDescription.setNaipePrimeiraCartaHumano(card.getSuit().toString());
                        //playedCardsHumano.addCard(card);
                    }
                }
                verifyDeceptionTrucoAtPlayCardRound1(player, card);
                verifyDeceptionEnvidoAtPlayCard(player);
                shiftTurn();
                break;
            case 2:
                if (faceDown) {
                    if (getPlayer1() == player) {
                        round1.setFaceDownCardPlayer1(true);
                    } else {
                        round1.setFaceDownCardPlayer2(true);
                    }
                    if (playerRobo == player) {
                        trucoDescription.setRoboCartaVirada(1);
                    } else {
                        trucoDescription.setHumanoCartaVirada(1);
                    }
                } else {
                    if (getPlayer1() == player) {
                        round1.setPlayer1Card(card);
                    } else {
                        round1.setPlayer2Card(card);
                    }

                }
                verifyDeceptionTrucoAtPlayCardRound1(player, card);
                compareCards(round1);
                rounds.add(round1);
                resultRoundLog(round1.getResult(), 1);
                if (round1.getResult() != Result.EMPATE) {
                    trucoDescription.setGanhadorPrimeiraRodada(round1.getWinner() == playerRobo ? TrucoData.ROBO : TrucoData.HUMANO);
                } else {
                    trucoDescription.setGanhadorPrimeiraRodada(0);
                }
                if (playerRobo == player) {
                    trucoDescription.setPrimeiraCartaRobo(card.getCbrCode());
                    trucoDescription.setNaipePrimeiraCartaRobo(card.getSuit().toString());

                } else {
                    trucoDescription.setPrimeiraCartaHumano(card.getCbrCode());
                    trucoDescription.setNaipePrimeiraCartaHumano(card.getSuit().toString());
                    //playedCardsHumano.addCard(card);
                }
                break;
            case 3:
                round2 = new Round(2);
                if (faceDown) {
                    verifyDeceptionTrucoCartaViradaRound2(player);
                    if (getPlayer1() == player) {
                        round2.setFaceDownCardPlayer1(true);
                    } else {
                        round2.setFaceDownCardPlayer2(true);
                    }
                    if (playerRobo == player) {
                        trucoDescription.setRoboCartaVirada(2);
                    } else {
                        trucoDescription.setHumanoCartaVirada(2);
                    }
                } else {
                    if (getPlayer1() == player) {
                        round2.setPlayer1Card(card);
                    } else {
                        round2.setPlayer2Card(card);
                    }
                    if (playerRobo == player) {
                        trucoDescription.setSegundaCartaRobo(card.getCbrCode());
                        trucoDescription.setNaipeSegundaCartaRobo(card.getSuit().toString());
                    } else {
                        trucoDescription.setSegundaCartaHumano(card.getCbrCode());
                        trucoDescription.setNaipeSegundaCartaHumano(card.getSuit().toString());
                        //playedCardsHumano.addCard(card);
                    }
                }
                shiftTurn();
                break;
            case 4:
                if (faceDown) {
                    verifyDeceptionTrucoCartaViradaRound2(player);
                    if (getPlayer1() == player) {
                        round2.setFaceDownCardPlayer1(true);
                    } else {
                        round2.setFaceDownCardPlayer2(true);
                    }
                    if (playerRobo == player) {
                        trucoDescription.setRoboCartaVirada(2);
                    } else {
                        trucoDescription.setHumanoCartaVirada(2);
                    }
                } else {
                    if (getPlayer1() == player) {
                        round2.setPlayer1Card(card);
                    } else {
                        round2.setPlayer2Card(card);
                    }
                }
                compareCards(round2);
                rounds.add(round2);
                resultRoundLog(round2.getResult(), 2);
                if (round2.getResult() != Result.EMPATE) {
                    trucoDescription.setGanhadorSegundaRodada(round2.getWinner() == playerRobo ? TrucoData.ROBO : TrucoData.HUMANO);
                } else {
                    trucoDescription.setGanhadorSegundaRodada(0);
                }

                if (playerRobo == player) {
                    trucoDescription.setSegundaCartaRobo(card.getCbrCode());
                    trucoDescription.setNaipeSegundaCartaRobo(card.getSuit().toString());
                } else {
                    trucoDescription.setSegundaCartaHumano(card.getCbrCode());
                    trucoDescription.setNaipeSegundaCartaHumano(card.getSuit().toString());
                    //playedCardsHumano.addCard(card);
                }
                break;
            case 5:
                round3 = new Round(3);
                if (faceDown) {
                    if (getPlayer1() == player) {
                        round3.setFaceDownCardPlayer1(true);
                    } else {
                        round3.setFaceDownCardPlayer2(true);
                    }
                    if (playerRobo == player) {
                        trucoDescription.setRoboCartaVirada(3);
                    } else {
                        trucoDescription.setHumanoCartaVirada(3);
                    }
                } else {
                    if (getPlayer1() == player) {
                        round3.setPlayer1Card(card);
                    } else {
                        round3.setPlayer2Card(card);
                    }
                    if (playerRobo == player) {
                        trucoDescription.setTerceiraCartaRobo(card.getCbrCode());
                        trucoDescription.setNaipeTerceiraCartaRobo(card.getSuit().toString());
                    } else {
                        trucoDescription.setTerceiraCartaHumano(card.getCbrCode());
                        trucoDescription.setNaipeTerceiraCartaHumano(card.getSuit().toString());
                        //playedCardsHumano.addCard(card);
                    }
                }
                shiftTurn();
                break;
            case 6:
                if (faceDown) {
                    if (getPlayer1() == player) {
                        round3.setFaceDownCardPlayer1(true);
                    } else {
                        round3.setFaceDownCardPlayer2(true);
                    }
                    if (playerRobo == player) {
                        trucoDescription.setRoboCartaVirada(3);
                    } else {
                        trucoDescription.setHumanoCartaVirada(3);
                    }
                } else {
                    if (getPlayer1() == player) {
                        round3.setPlayer1Card(card);
                    } else {
                        round3.setPlayer2Card(card);
                    }
                }
                compareCards(round3);
                rounds.add(round3);
                resultRoundLog(round3.getResult(), 3);
                if (round3.getResult() != Result.EMPATE) {
                    trucoDescription.setGanhadorTerceiraRodada(round3.getWinner() == playerRobo ? TrucoData.ROBO : TrucoData.HUMANO);
                } else {
                    trucoDescription.setGanhadorTerceiraRodada(0);
                }

                if (playerRobo == player) {
                    trucoDescription.setTerceiraCartaRobo(card.getCbrCode());
                    trucoDescription.setNaipeTerceiraCartaRobo(card.getSuit().toString());
                } else {
                    trucoDescription.setTerceiraCartaHumano(card.getCbrCode());
                    trucoDescription.setNaipeTerceiraCartaHumano(card.getSuit().toString());
                    //playedCardsHumano.addCard(card);
                }
                break;
        }
    }

    public void compareCards(Round round) {
        if (round.isFaceDownCardPlayer1() || round.isFaceDownCardPlayer2()) {
            if (round.isFaceDownCardPlayer1() && round.isFaceDownCardPlayer2()) {
                round.setWinner(null);
                round.setResult(Result.EMPATE);
                playerTurn = getPlayerHand();
                playerToken = getPlayerHand();
            } else if (!round.isFaceDownCardPlayer1() && round.isFaceDownCardPlayer2()) {
                round.setWinner(getPlayer1());
                round.setResult(Result.PLAYER1);
                playerTurn = getPlayer1();
                playerToken = getPlayer1();
            } else {
                round.setWinner(getPlayer2());
                round.setResult(Result.PLAYER2);
                playerTurn = getPlayer2();
                playerToken = getPlayer2();
            }
        } else {
            if (round.getPlayer1Card().getCbrCode() > round.getPlayer2Card().getCbrCode()) {
                round.setWinner(getPlayer1());
                round.setResult(Result.PLAYER1);
                playerTurn = getPlayer1();
                playerToken = getPlayer1();
            } else if (round.getPlayer1Card().getCbrCode() < round.getPlayer2Card().getCbrCode()) {
                round.setWinner(getPlayer2());
                round.setResult(Result.PLAYER2);
                playerTurn = getPlayer2();
                playerToken = getPlayer2();
            } else {
                round.setWinner(null);
                round.setResult(Result.EMPATE);
                playerTurn = getPlayerHand();
                playerToken = getPlayerHand();
            }
        }

    }

    /**
     * Se empatar na primeira rodada, quem ganhar a segunda vence a mão;
     * Se empatar na segunda rodada, quem ganhou a primeira vence a mão;
     * Se empatar na primeira e segunda rodadas, quem ganhar a terceira vence a mão;
     * Se empatar na terceira rodada, quem ganhou a primeira vence a mão;
     * Se todas as três rodadas empatarem, quem iniciou a mão vence a mão.  */

    public Boolean isHandFinish(int round) {
        Boolean isHandFinish = false;
        if (round == 2) {
            if (rounds.getFirst().getResult() != Result.EMPATE && rounds.getFirst().getResult() == rounds.getLast().getResult()) {
                updatePlacarRoundWinnerTruco(rounds.getLast().getWinner());
                isHandFinish = true;
            } else if (rounds.getFirst().getResult() != Result.EMPATE && rounds.getLast().getResult() == Result.EMPATE) {
                updatePlacarRoundWinnerTruco(rounds.getFirst().getWinner());
                isHandFinish = true;
            } else if (rounds.getFirst().getResult() == Result.EMPATE && rounds.getLast().getResult() != Result.EMPATE) {
                updatePlacarRoundWinnerTruco(rounds.getLast().getWinner());
                isHandFinish = true;
            }
        }

        if (round == 3) {
            if (rounds.getFirst().getResult() == Result.EMPATE && rounds.get(1).getResult() == Result.EMPATE) {
                updatePlacarRoundWinnerTruco((rounds.getLast().getResult() == Result.EMPATE) ? getPlayerHand() :
                        rounds.getLast().getWinner());
            } else {
                updatePlacarRoundWinnerTruco((rounds.getLast().getResult() == Result.EMPATE) ?  rounds.getFirst().getWinner() :
                        rounds.getLast().getWinner());
            }
            isHandFinish = true;
        }
        return isHandFinish;
    }

    public Boolean isGameFinish() {
        return (player1Points >= 24 || player2Points >= 24);
    }

    public void finishedHand() {
        matchScoreLog();
        finishHandLog(trucoDescription.getTentosEnvido() != null ? trucoDescription.getTentosEnvido(): 0,
                trucoDescription.getTentosFlor() != null ? trucoDescription.getTentosFlor() : 0 ,
                trucoDescription.getTentosTruco() != null ? trucoDescription.getTentosTruco() : 0);
        player1.getCards().giveBackCardsDeck();
        player2.getCards().giveBackCardsDeck();
        trucoDescription.setTentosPosterioresRobo(playerRobo == getPlayer1() ? getPlayer1Points() : getPlayer2Points());
        trucoDescription.setTentosPosterioresHumano(playerRobo == getPlayer1() ? getPlayer2Points() : getPlayer1Points());
        saveCase();
        init();
        shiftHand();
        trucoValue = 1;
        darAsCartas(playerHand);
        //darAsCartasBothFlor();
        //darAsCartasOnlyOneFlor();
        //darAsCartasBothManyPoints();
        setCardsToCase();
        startHandLog();
        matchScoreLog();
        dealtCardsLog(getPlayerRobo());
        dealtCardsLog(getPlayerRobo() == getPlayer1() ? getPlayer2() : getPlayer1());
    }

    public void finishedGame() {
        matchScoreLog();
        finishHandLog(trucoDescription.getTentosEnvido() != null ? trucoDescription.getTentosEnvido(): 0,
                trucoDescription.getTentosFlor() != null ? trucoDescription.getTentosFlor() : 0 ,
                trucoDescription.getTentosTruco() != null ? trucoDescription.getTentosTruco() : 0);
        trucoDescription.setTentosPosterioresRobo(playerRobo == getPlayer1() ? getPlayer1Points() : getPlayer2Points());
        trucoDescription.setTentosPosterioresHumano(playerRobo == getPlayer1() ? getPlayer2Points() : getPlayer1Points());
        finishMatchLog();
        saveCase();
        player1.getCards().giveBackCardsDeck();
        player2.getCards().giveBackCardsDeck();
        init();
        trucoValue = 1;
        player1Points = 0;
        player2Points = 0;
        uid = UUID.randomUUID().toString();
    }

    /*private void setPostHandPlayer1() {

        boolean isPlayerRobo = player1.equals(playerRobo);

        Iterator i = trucoDescription.getJogadasPlayer1().iterator();
        while(i.hasNext()) {

            JogadaPlayer1 jogada = (JogadaPlayer1) i.next();

            switch (jogada.getIndJogada()) {
                case 1:
                    switch (jogada.getTypeJogada()) {
                        case 1:
                            //TODO: fazer para flor
                            break;
                        case 2:
                            *//** Call Envido/Real/Falta
                             *  Agressive *//*
                            if (jogada.getPontosEnvido() > 29) {
                                *//** Tight *//*
                                jogada.setIndStrategy(13);
                            } else {
                                *//** Loose *//*
                                jogada.setIndStrategy(23);
                            }
                            break;
                        case 3:
                            *//** Call Truco/Retruco/Vale4
                             *  Agressive *//*
                            if (jogada.getQualidadeMao() == 1) {
                                *//** Tight *//*
                                jogada.setIndStrategy(13);
                            } else {
                                *//** Loose *//*
                                jogada.setIndStrategy(23);
                            }
                            break;
                    }
                    break;
                case 2:
                    switch (jogada.getTypeJogada()) {
                        case 2:
                            *//** No Call Envido/Real/Falta *//*
                            if (jogada.getPontosEnvido() > 29) {
                                *//** Tight Agressive - slowPlay *//*
                                jogada.setIndStrategy(13);
                            } else {
                                *//** Tight Passive *//*
                                jogada.setIndStrategy(14);
                            }
                            break;
                        case 3:
                            *//** No Call Truco/Retruco/Vale4 *//*
                            if (jogada.getQualidadeMao() == 1) {
                                *//** Tight Agressive - slowPlay *//*
                                jogada.setIndStrategy(13);
                            } else {
                                *//** Tight Passive *//*
                                jogada.setIndStrategy(14);
                            }
                            break;
                    }
                    break;
                case 3:
                    switch (jogada.getTypeJogada()) {
                        case 1:
                            //TODO: fazer para flor
                            break;
                        case 2:
                            *//** Accept Envido/Real/Falta
                             * Passive *//*
                            if (jogada.getPontosEnvido() > 29) {
                                *//** Tight *//*
                                jogada.setIndStrategy(14);
                            } else {
                                *//** Loose *//*
                                jogada.setIndStrategy(24);
                            }
                            break;
                        case 3:
                            *//** Accept Truco/Retruco/Vale4
                             *  Passive *//*
                            if (jogada.getQualidadeMao() == 1) {
                                *//** Tight *//*
                                jogada.setIndStrategy(14);
                            } else {
                                *//** Loose *//*
                                jogada.setIndStrategy(24);
                            }
                            break;
                    }
                    break;
                case 4:
                    switch (jogada.getTypeJogada()) {
                        case 1:
                            //TODO: fazer para flor
                            break;
                        case 2:
                            *//** Declined Envido/Real/Falta
                             * Tight Passive *//*
                            jogada.setIndStrategy(14);
                            break;
                        case 3:
                            *//** Declined Truco/Retruco/Vale4
                             * Tight Passive *//*
                            jogada.setIndStrategy(14);
                            break;
                    }
                    break;
                case 5:
                    switch (jogada.getTypeJogada()) {
                        case 1:
                            //TODO: fazer para flor
                            break;
                        case 2:
                            *//** Raise Envido/Real/Falta
                             *  Agressive *//*
                            if (jogada.getPontosEnvido() > 29) {
                                *//** Tight *//*
                                jogada.setIndStrategy(13);
                            } else {
                                *//** Loose *//*
                                jogada.setIndStrategy(23);
                            }
                            break;
                        case 3:
                            *//** Raise Truco/Retruco/Vale4
                             *  Agressive *//*
                            if (jogada.getQualidadeMao() == 1) {
                                *//** Tight *//*
                                jogada.setIndStrategy(13);
                            } else {
                                *//** Loose *//*
                                jogada.setIndStrategy(23);
                            }
                            break;
                    }
                    break;
                case 6:
                    *//** Tight Passive *//*
                    jogada.setIndStrategy(14);
                    break;
                case 7:
                    *//** Tight Passive *//*
                    jogada.setIndStrategy(14);
                    break;
                case 8:
                    //TODO:
                    break;
                case 9:
                    //TODO:
                    break;
            }

            if (jogada.getIndBlefe() == 1) {
                if (jogada.getTypeBlefe() == 2 || jogada.getTypeBlefe() == 11 || jogada.getTypeBlefe() == 13 ||
                        jogada.getTypeBlefe() == 21) {
                    jogada.setIndStrategy(13);
                } else {
                    jogada.setIndStrategy(23);
                }

                switch (jogada.getTypeBlefe()) {
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 11:
                        break;
                    case 12:
                        break;
                    case 13:
                        break;
                    case 14:
                        break;
                    case 15:
                        break;
                    case 21:
                        break;
                    case 22:
                        break;
                    case 23:
                        break;
                    case 24:
                        break;
                    case 31:
                        break;
                    case 32:
                        break;
                }

            }

        }
    }*/

    public void saveCase() {

        try {
            Session session = HibernateConfig.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(trucoDescription);
            session.getTransaction().commit();
            System.out.println("Caso inserido com sucesso!");

            session.close();
            //HibernateConfig.shutdown();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public int getRound(int dealtCards) {
        int round = 1;

        if (dealtCards < 2) {
            round = 1;
        } else if (dealtCards >= 2 && dealtCards < 4) {
            round = 2;
        } else {
            round = 3;
        }

        return round;
    }

    ///DECEPTION

    public void verifyDeceptionEnvidoAtCallPoints(Player player) {
        if (playerHand.equals(player)) {
            //TODO: Definir o que é poucos pontos como mão
            // #1 - Jogador mão canta ENVIDO/REAL/FALTA sem ter muitos pontos;
            if (getEnvidoPoints(player.getCards()) < 24) {
                trucoDescription.setHasDeception(TrucoData.TRUE);
                if (player.equals(playerRobo)) {
                    trucoDescription.setRoboMentiuEnvido(TrucoData.DECEPTION_ENVIDO_MAO_SEM_PONTO);
                } else {
                    trucoDescription.setHumanoMentiuEnvido(TrucoData.DECEPTION_ENVIDO_MAO_SEM_PONTO);
                }
            }
        } else {
            // #3 - Jogador pé, sem ter muitos pontos, canta ENVIDO/REAL/FALTA porque o oponente não cantou;
            if (getEnvidoPoints(player.getCards()) < 24) {
                trucoDescription.setHasDeception(TrucoData.TRUE);
                if (player.equals(playerRobo)) {
                    trucoDescription.setRoboMentiuEnvido(TrucoData.DECEPTION_ENVIDO_PE_SEM_PONTO);
                } else {
                    trucoDescription.setHumanoMentiuEnvido(TrucoData.DECEPTION_ENVIDO_PE_SEM_PONTO);
                }
            }

        }

    }

    public void verifyDeceptionEnvidoAtPlayCard(Player player) {
        if (playerHand.equals(player) && envido.getEnvidoChain().size() == 0) {
            //TODO: Definir o que é muitos pontos como mão
            if (getEnvidoPoints(player.getCards()) > 29) {
                //#2 - Jogador mão deixa de cantar ENVIDO/REAL/FALTA mesmo com bastantes pontos;
                trucoDescription.setHasDeception(TrucoData.TRUE);
                if (player.equals(playerRobo)) {
                    trucoDescription.setRoboMentiuEnvido(TrucoData.DECEPTION_ENVIDO_MAO_MUITO_PONTO);
                } else {
                    trucoDescription.setHumanoMentiuEnvido(TrucoData.DECEPTION_ENVIDO_MAO_MUITO_PONTO);
                }
            }
        }
    }

    public void verifyDeceptionEnvidoAtHidePoints(Player player) {
        //TODO: Definir o que é muitos pontos como mão
        //#4 - Jogador pé, não canta os pontos para não entregar suas cartas;
        trucoDescription.setHasDeception(TrucoData.TRUE);
        if (player.equals(playerRobo)) {
            trucoDescription.setRoboMentiuEnvido(TrucoData.DECEPTION_ENVIDO_PE_E_BOM);
        } else {
            trucoDescription.setHumanoMentiuEnvido(TrucoData.DECEPTION_ENVIDO_PE_E_BOM);
        }

    }

    public void verifyDeceptionTrucoBeforePlayCardRound1(Player player, int countPlayedCards) {
        Deck tmpDeck = sortCards(player);
        if (countPlayedCards == 0) {
            //#15 - Jogador com mão ruim chama TRUCO antes de mostrar as cartas;
            if (tmpDeck.getCard(2).getCbrCode() < 40 && tmpDeck.getCard(1).getCbrCode() < 12) {
                trucoDescription.setHasDeception(TrucoData.TRUE);
                if (player.equals(playerRobo)) {
                    trucoDescription.setRoboMentiuRound1(TrucoData.DECEPTION_TRUCO_ROUND_1_CANTA_TRUCO);
                } else {
                    trucoDescription.setHumanoMentiuRound1(TrucoData.DECEPTION_TRUCO_ROUND_1_CANTA_TRUCO);
                }
            }
        }
    }

    public void verifyDeceptionTrucoAtPlayCardRound1(Player player, Card playedCard ){
        Deck tmpDeck = sortCards(player);

        // #5 - Jogador mão, deixa de cantar ENVIDO/REAL/FALTA sem ter muitos pontos, porém joga um 6 ou 7 para o oponente não cantar ;
        if (playerHand.equals(player)) {
            if (getEnvidoPoints(player.getCards()) < 24) {
                if (envido.getEnvidoChain().size() == 0) {
                    if (playedCard.getFace().equals(Face.SETE) || playedCard.getFace().equals(Face.SEIS)) {
                        trucoDescription.setHasDeception(TrucoData.TRUE);
                        if (player.equals(playerRobo)) {
                            trucoDescription.setRoboMentiuEnvido(TrucoData.DECEPTION_ENVIDO_MAO_JOGA_SETE);
                        } else {
                            trucoDescription.setHumanoMentiuEnvido(TrucoData.DECEPTION_ENVIDO_MAO_JOGA_SETE);
                        }
                    }
                }
            }
        }

        if (tmpDeck.getCard(2).getCbrCode() > 24 && tmpDeck.getCard(1).getCbrCode() > 12) {

            //#11 - Jogador com mão boa larga a carta mais baixa;
            if (tmpDeck.getCard(0).equals(playedCard)) {
                trucoDescription.setHasDeception(TrucoData.TRUE);
                if (player.equals(playerRobo)) {
                    trucoDescription.setRoboMentiuRound1(TrucoData.DECEPTION_TRUCO_ROUND_1_MAO_BOA_CARTA_BAIXA);
                } else {
                    trucoDescription.setHumanoMentiuRound1(TrucoData.DECEPTION_TRUCO_ROUND_1_MAO_BOA_CARTA_BAIXA);
                }
            }

            //#13 - Caso jogador com uma mão boa tenha cantado seus pontos de envido, joga carta para despitar as cartas que compoem os pontos;
            if (player.equals(playerRobo)) {
                if (trucoDescription.getPontosEnvidoRobo() != null) {
                    if (!getSuitEnvido(player.getCards()).equals(playedCard.getSuit())) {
                        trucoDescription.setHasDeception(TrucoData.TRUE);
                        trucoDescription.setRoboMentiuRound1(TrucoData.DECEPTION_TRUCO_ROUND_1_MAO_BOA_CARTA_NAIPE_DIFERENTE);
                    }
                }
            } else {
                if (trucoDescription.getPontosEnvidoHumano() != null) {
                    if (!getSuitEnvido(player.getCards()).equals(playedCard.getSuit())) {
                        trucoDescription.setHasDeception(TrucoData.TRUE);
                        trucoDescription.setHumanoMentiuRound1(TrucoData.DECEPTION_TRUCO_ROUND_1_MAO_BOA_CARTA_NAIPE_DIFERENTE);
                    }
                }
            }
        }


        if (tmpDeck.getCard(2).getCbrCode() < 40 && tmpDeck.getCard(1).getCbrCode() < 12) {

            //#12 - Jogador com mão ruim larga a carta mais alta;
            if (tmpDeck.getCard(2).equals(playedCard)) {
                trucoDescription.setHasDeception(TrucoData.TRUE);
                if (player.equals(playerRobo)) {
                    trucoDescription.setRoboMentiuRound1(TrucoData.DECEPTION_TRUCO_ROUND_1_MAO_RUIM_CARTA_ALTA);
                } else {
                    trucoDescription.setHumanoMentiuRound1(TrucoData.DECEPTION_TRUCO_ROUND_1_MAO_RUIM_CARTA_ALTA);
                }
            }

            //#14 - Caso jogador com uma mão ruim tenha cantado seus pontos de envido, joga carta para despitar as cartas que compoem os pontos;
            if (player.equals(playerRobo)) {

                if (trucoDescription.getPontosEnvidoRobo() != null) {
                    if (!getSuitEnvido(player.getCards()).equals(playedCard.getSuit())) {
                        trucoDescription.setHasDeception(TrucoData.TRUE);
                        trucoDescription.setRoboMentiuRound1(TrucoData.DECEPTION_TRUCO_ROUND_1_MAO_RUIM_CARTA_NAIPE_DIFERENTE);
                    }
                }
            } else {
                if (trucoDescription.getPontosEnvidoHumano() != null) {
                    if (!getSuitEnvido(player.getCards()).equals(playedCard.getSuit())) {
                        trucoDescription.setHasDeception(TrucoData.TRUE);
                        trucoDescription.setHumanoMentiuRound1(TrucoData.DECEPTION_TRUCO_ROUND_1_MAO_RUIM_CARTA_NAIPE_DIFERENTE);
                    }
                }
            }
        }
    }

    public void verifyDeceptionTrucoCartaViradaRound2(Player player) {
        Deck tmpDeck = sortCards(player);
        if (dealtCards2.size() >= 2 && round1.getWinner().equals(player)) {

            //#21 - Jogador ganhou primeira e tem uma mão boa, joga a segunda carta virada;
            if (tmpDeck.getCard(2).getCbrCode() > 24 && tmpDeck.getCard(1).getCbrCode() > 12) {
                trucoDescription.setHasDeception(TrucoData.TRUE);
                if (player.equals(playerRobo)) {
                    trucoDescription.setRoboMentiuRound2(TrucoData.DECEPTION_TRUCO_ROUND_2_MAO_BOA_FEZ_PRIMEIRA_CARTA_VIRADA);
                } else {
                    trucoDescription.setHumanoMentiuRound2(TrucoData.DECEPTION_TRUCO_ROUND_2_MAO_BOA_FEZ_PRIMEIRA_CARTA_VIRADA);
                }
            }

            //#22 - Jogador ganhou primeira e tem uma mão ruim, joga a segunda carta virada;
            if (tmpDeck.getCard(2).getCbrCode() < 40 && tmpDeck.getCard(1).getCbrCode() < 12) {
                trucoDescription.setHasDeception(TrucoData.TRUE);
                if (player.equals(playerRobo)) {
                    trucoDescription.setRoboMentiuRound2(TrucoData.DECEPTION_TRUCO_ROUND_2_MAO_RUIM_FEZ_PRIMEIRA_CARTA_VIRADA);
                } else {
                    trucoDescription.setHumanoMentiuRound2(TrucoData.DECEPTION_TRUCO_ROUND_2_MAO_RUIM_FEZ_PRIMEIRA_CARTA_VIRADA);
                }
            }

        }
    }

    public void verifyDeceptionTrucoBeforePlayCardRound2(Player player, LinkedList playedCards) {
        Deck tmpDeck = sortCards(player);
        if (dealtCards2.size() >= 2 && playedCards.size() == 1) {
            //#23 - Tem uma mão ruim, chama TRUCO antes de largar a segunda carta;
            if (tmpDeck.getCard(2).getCbrCode() < 40 && tmpDeck.getCard(1).getCbrCode() < 12) {
                trucoDescription.setHasDeception(TrucoData.TRUE);
                if (player.equals(playerRobo)) {
                    trucoDescription.setRoboMentiuRound2(TrucoData.DECEPTION_TRUCO_ROUND_2_MAO_RUIM_CANTA_TRUCO);
                } else {
                    trucoDescription.setHumanoMentiuRound2(TrucoData.DECEPTION_TRUCO_ROUND_2_MAO_RUIM_CANTA_TRUCO);
                }
            }

            //#24 - Jogador perdeu primeira, é o pé e não tem carta para fazer a segunda, chama TRUCO antes de largar a segunda carta;
            if (round1.getResult()!= Result.EMPATE ) {
                if (!round1.getWinner().equals(player)) {
                    Card cartaJogadaOponente = null;

                    if (player.equals(player1)) {
                        if (playedCardsPlayer2.size() == 2) {
                            cartaJogadaOponente = round2.getPlayer2Card();
                        }
                    } else {
                        if (playedCardsPlayer1.size() == 2) {
                            cartaJogadaOponente = round2.getPlayer1Card();
                        }
                    }

                    if (cartaJogadaOponente != null) {
                        boolean hasCardToWin = false;
                        for (Card card: player.getCards().getCards()) {
                            if (!playedCards.contains(card) && card.getCbrCode() > cartaJogadaOponente.getCbrCode()) {
                                hasCardToWin = true;
                            }
                        }
                        if (!hasCardToWin) {
                            trucoDescription.setHasDeception(TrucoData.TRUE);
                            if (player.equals(playerRobo)) {
                                trucoDescription.setRoboMentiuRound2(TrucoData.DECEPTION_TRUCO_ROUND_2_PERDEU_NAO_MATA_SEGUNDA_CANTA_TRUCO);
                            } else {
                                trucoDescription.setHumanoMentiuRound2(TrucoData.DECEPTION_TRUCO_ROUND_2_PERDEU_NAO_MATA_SEGUNDA_CANTA_TRUCO);
                            }
                        }
                    }

                }
            }

        }
    }

    public void verifyDeceptionTrucoBeforePlayCardRound3(Player player, LinkedList playedCards) {

        if (dealtCards2.size() >= 4 && playedCards.size() == 2) {

            if (dealtCards2.size() == 4) {
                //#31 - Jogador possui terceira carta baixa, chama TRUCO/RETRUCO/VALE4 antes de largar a carta;
                player.getCards().getCards().forEach(card -> {
                    System.out.println(card);
                    if (!playedCards.contains(card)) {
                        if (card.getCbrCode() < 10) {
                            trucoDescription.setHasDeception(TrucoData.TRUE);
                            if (player.equals(playerRobo)) {
                                trucoDescription.setRoboMentiuRound3(TrucoData.DECEPTION_TRUCO_ROUND_3_CARTA_RUIM_CANTA_TRUCO);
                            } else {
                                trucoDescription.setHumanoMentiuRound3(TrucoData.DECEPTION_TRUCO_ROUND_3_CARTA_RUIM_CANTA_TRUCO);
                            }
                        }
                    }
                });
            } else {
                //#32 - Jogador é o pé e não tem carta para fazer a terceira, chama TRUCO antes de largar a terceira carta;
                Card cartaJogadaOponente = null;

                if (player.equals(player1)) {
                    if (playedCardsPlayer2.size() == 3) {
                        cartaJogadaOponente = round3.getPlayer2Card();
                    }
                } else {
                    if (playedCardsPlayer1.size() == 3) {
                        cartaJogadaOponente = round3.getPlayer1Card();
                    }
                }

                if (cartaJogadaOponente != null) {
                    boolean hasCardToWin = false;
                    for (Card card : player.getCards().getCards()) {
                        if (!playedCards.contains(card) && card.getCbrCode() > cartaJogadaOponente.getCbrCode()) {
                            hasCardToWin = true;
                        }
                    }
                    if (!hasCardToWin) {
                        trucoDescription.setHasDeception(TrucoData.TRUE);
                        if (player.equals(playerRobo)) {
                            trucoDescription.setRoboMentiuRound3(TrucoData.DECEPTION_TRUCO_ROUND_3_CARTA_NAO_MATA_TERCEIRA_CANTA_TRUCO);
                        } else {
                            trucoDescription.setHumanoMentiuRound3(TrucoData.DECEPTION_TRUCO_ROUND_3_CARTA_NAO_MATA_TERCEIRA_CANTA_TRUCO);
                        }
                    }
                }
            }

        }
    }

    public int getHandSum(Player player) {
        int sum = 0;

        for(Card card : player.getCards().getCards()){
            sum += card.getCbrCode();
        }

        return sum;
    }

    /////////////Logs

    public void startMatchLog() {
        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "START_MATCH")
                .add("player1", playerRobo == getPlayer1() ? TrucoData.ROBO: TrucoData.HUMANO)
                .add("player2", playerRobo == getPlayer2() ?  TrucoData.ROBO: TrucoData.HUMANO)
                .build();

        addLog(jsonMessage.toString(), "START_MATCH");

    }

    public void startHandLog() {
        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "START_HAND")
                .add("handPlayer", playerRobo.equals(getPlayerHand()) ? TrucoData.ROBO: TrucoData.HUMANO)
                .build();

        addLog(jsonMessage.toString(), "START_HAND");

    }

    public void matchScoreLog() {
        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "MATCH_SCORE")
                .add("agentPoints", playerRobo == getPlayer1() ? getPlayer1Points() : getPlayer2Points())
                .add("humanoPoints", playerRobo == getPlayer1() ? getPlayer2Points() : getPlayer1Points())
                .build();

        addLog(jsonMessage.toString(), "MATCH_SCORE");
    }

    public void dealtCardsLog(Player player) {
        Deck tmpDeck = sortCards(player);
        int handRanking = tmpDeck.getCard(0).getCbrCode() + tmpDeck.getCard(1).getCbrCode() + tmpDeck.getCard(2).getCbrCode();
        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "DEALT_CARDS")
                .add("player", playerRobo == player ? TrucoData.ROBO : TrucoData.HUMANO)
                .add("highCard", tmpDeck.getCard(2).getFace() +"-"+ tmpDeck.getCard(2).getSuit()+"- cbrCode:"+ tmpDeck.getCard(2).getCbrCode())
                .add("mediumCard", tmpDeck.getCard(1).getFace() +"-"+ tmpDeck.getCard(1).getSuit()+"- cbrCode:"+ tmpDeck.getCard(1).getCbrCode())
                .add("lowCard", tmpDeck.getCard(0).getFace() +"-"+ tmpDeck.getCard(0).getSuit()+"- cbrCode:"+ tmpDeck.getCard(0).getCbrCode())
                .add("envidoRanking", getEnvidoPoints(player.getCards()))
                .add("handRanking", handRanking+"/144")
                .build();

        addLog(jsonMessage.toString(), "DEALT_CARDS");
    }

    public void playCardLog(Player origem, Card card, int round) {
        String typeCard;
        Deck tmpDeck = sortCards(origem);
        if (card.getFace() == tmpDeck.getCard(2).getFace() && card.getSuit() == tmpDeck.getCard(2).getSuit()) {
            typeCard = "highCard";
        } else if (card.getFace() == tmpDeck.getCard(1).getFace() && card.getSuit() == tmpDeck.getCard(1).getSuit()) {
            typeCard = "mediumCard";
        } else {
            typeCard = "lowCard";
        }
        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "PLAY_CARD")
                .add("origem", playerRobo == origem ? TrucoData.ROBO: TrucoData.HUMANO)
                .add("round", getRodada(round))
                .add("card", card.getFace()+"-"+card.getSuit()+"- cbrCode:"+ card.getCbrCode())
                .add("typeCard", typeCard)
                .add("hand", origem == getPlayerHand() ? "Mão" : "Pé")
                .build();

        addLog(jsonMessage.toString(), "PLAY_CARD", origem);

    }

    public void playCardLog(Player origem, String card, int round) {

        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "PLAY_CARD")
                .add("origem", playerRobo == origem ? TrucoData.ROBO: TrucoData.HUMANO)
                .add("round", getRodada(round))
                .add("card", card)
                .add("hand", origem == getPlayerHand() ? "Mão" : "Pé")
                .build();

        addLog(jsonMessage.toString(), "PLAY_CARD", origem);

    }

    public void callGameLog(Player origem, String tipoTruco, int round) {

        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "CALL_GAME")
                .add("origem", playerRobo == origem ? TrucoData.ROBO: TrucoData.HUMANO)
                .add("round", getRodada(round))
                .add("tipo", tipoTruco)
                .add("hand", origem == getPlayerHand() ? "Mão" : "Pé")
                .build();

        addLog(jsonMessage.toString(), "CALL_GAME", origem);

    }

    public void raiseGameLog(Player origem, String tipoTruco, int round) {

        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "RAISE_GAME")
                .add("origem", playerRobo == origem ? TrucoData.ROBO: TrucoData.HUMANO)
                .add("round", getRodada(round))
                .add("tipo", tipoTruco)
                .add("hand", origem == getPlayerHand() ? "Mão" : "Pé")
                .build();

        addLog(jsonMessage.toString(), "RAISE_GAME", origem);

    }

    public void callPointsLog(Player origem, String tipoEnvido) {

        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "CALL_POINTS")
                .add("origem", playerRobo == origem ? TrucoData.ROBO: TrucoData.HUMANO)
                .add("tipo", tipoEnvido)
                .add("hand", origem == getPlayerHand() ? "Mão" : "Pé")
                .build();

        addLog(jsonMessage.toString(), "CALL_POINTS", origem);

    }

    public void raisePointsLog(Player origem, String tipoEnvido) {

        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "RAISE_POINTS")
                .add("origem", playerRobo == origem ? TrucoData.ROBO: TrucoData.HUMANO)
                .add("tipo", tipoEnvido)
                .add("hand", origem == getPlayerHand() ? "Mão" : "Pé")
                .build();

        addLog(jsonMessage.toString(), "RAISE_POINTS", origem);

    }

    public void callFlorLog(Player origem, String tipoFlor) {

        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "CALL_FLOR")
                .add("origem", playerRobo == origem ? TrucoData.ROBO: TrucoData.HUMANO)
                .add("tipo", tipoFlor)
                .add("hand", origem == getPlayerHand() ? "Mão" : "Pé")
                .build();

        addLog(jsonMessage.toString(), "CALL_FLOR", origem);

    }

    public void raiseFlorLog(Player origem, String tipoFlor) {

        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "RAISE_FLOR")
                .add("origem", playerRobo == origem ? TrucoData.ROBO: TrucoData.HUMANO)
                .add("tipo", tipoFlor)
                .add("hand", origem == getPlayerHand() ? "Mão" : "Pé")
                .build();

        addLog(jsonMessage.toString(), "RAISE_FLOR", origem);

    }

    public void acceptDeclinedGameLog(Player origem, TrucoLevel tipo, Boolean isAccept) {

        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", isAccept ? "ACCEPTED_GAME": "DECLINED_GAME")
                .add("origem", playerRobo == origem ? TrucoData.ROBO: TrucoData.HUMANO)
                .add("tipo", tipo.toString())
                .add("hand", origem == getPlayerHand() ? "Mão" : "Pé")
                .build();

        addLog(jsonMessage.toString(), isAccept ? "ACCEPTED_GAME": "DECLINED_GAME", origem);

    }

    public void acceptDeclinedPointsLog(Player origem, EnvidoLevel tipo, Boolean isAccept) {

        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", isAccept ? "ACCEPTED_POINTS": "DECLINED_POINTS")
                .add("origem", playerRobo == origem ? TrucoData.ROBO: TrucoData.HUMANO)
                .add("tipo", tipo.toString())
                .add("hand", origem == getPlayerHand() ? "Mão" : "Pé")
                .build();

        addLog(jsonMessage.toString(), isAccept ? "ACCEPTED_POINTS": "DECLINED_POINTS", origem);

    }

    public void acceptDeclinedPointsLog(Player origem, FlorLevel tipo, Boolean isAccept) {

        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", isAccept ? "ACCEPTED_POINTS": "DECLINED_POINTS")
                .add("origem", playerRobo == origem ? TrucoData.ROBO: TrucoData.HUMANO)
                .add("tipo", tipo.toString())
                .add("hand", origem == getPlayerHand() ? "Mão" : "Pé")
                .build();

        addLog(jsonMessage.toString(), isAccept ? "ACCEPTED_POINTS": "DECLINED_POINTS", origem);

    }

    public void irBaralhoLog(Player origem, int round) {

        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "IR_BARALHO")
                .add("origem", playerRobo == origem ? TrucoData.ROBO: TrucoData.HUMANO)
                .add("round", round)
                .add("hand", origem == getPlayerHand() ? "Mão" : "Pé")
                .build();

        addLog(jsonMessage.toString(), "IR_BARALHO", origem);

    }

    public void resultFlorLog(int winner, int tentosGanhos, int pointsAgent, int pointsHumano) {

        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "RESULT_FLOR")
                .add("pointsAgent", pointsAgent)
                .add("pointsHumano", pointsHumano)
                .add("tentosGanhos", tentosGanhos)
                .add("winner", winner)
                .build();

        addLog(jsonMessage.toString(), "RESULT_FLOR");

    }

    public void resultFlorLog(int winner, int tentosGanhos) {

        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "RESULT_FLOR")
                .add("tentosGanhos", tentosGanhos)
                .add("winner", winner)
                .build();

        addLog(jsonMessage.toString(), "RESULT_FLOR");

    }

    public void resultEnvidoLog(int winner, int tentosGanhos, int pointsAgent, int pointsHumano) {

        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "RESULT_ENVIDO")
                .add("pointsAgent", pointsAgent)
                .add("pointsHumano", pointsHumano)
                .add("tentosGanhos", tentosGanhos)
                .add("winner", winner)
                .build();

        addLog(jsonMessage.toString(), "RESULT_ENVIDO");

    }

    public void resultEnvidoLog(int winner, int tentosGanhos) {

        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "RESULT_ENVIDO")
                .add("tentosGanhos", tentosGanhos)
                .add("winner", winner)
                .build();

        addLog(jsonMessage.toString(), "RESULT_ENVIDO");

    }

    public void resultTrucoLog(int winner, int tentosGanhos) {

        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "RESULT_TRUCO")
                .add("tentosGanhos", tentosGanhos)
                .add("winner", winner)
                .build();

        addLog(jsonMessage.toString(), "RESULT_TRUCO");

    }

    public void resultRoundLog(Result result, int round) {

        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "RESULT_ROUND")
                .add("round", round)
                .add("winner", getResult(result))
                .build();

        addLog(jsonMessage.toString(), "RESULT_ROUND");

    }

    public void finishMatchLog() {
        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "FINISH_MATCH")
                .add("winner", getPlayer1Points() > getPlayer2Points() ? playerRobo == getPlayer1() ? TrucoData.ROBO :
                        TrucoData.HUMANO : playerRobo == getPlayer2() ? TrucoData.ROBO : TrucoData.HUMANO)
                .add("agentPoints", playerRobo == getPlayer1() ? getPlayer1Points() : getPlayer2Points())
                .add("humanoPoints", playerRobo == getPlayer1() ? getPlayer2Points() : getPlayer1Points())
                .build();

        addLog(jsonMessage.toString(), "FINISH_MATCH");

    }

    public void finishHandLog(int tentosEnvido, int tentosFlor, int tentosTruco) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add("action", "FINISH_HAND")
                .add("tentosEnvido", tentosEnvido)
                .add("tentosFlor", tentosFlor)
                .add("tentosTruco", tentosTruco)
                .build();

        addLog(jsonMessage.toString(), "FINISH_HAND");

    }

    public void addLog(String log, String jogada) {

        Log log1 = new Log();
        log1.setIdPartida(uid);
        log1.setJogada(jogada);
        log1.setLog(log);
        log1.setTrucoDescription(trucoDescription);
        trucoDescription.getLogs().add(log1);
    }

    /*public void addJogadaPlayer1(int indJogada, int typeJogada, int detailJogada, Suit naipe){
        jogada1 = new JogadaPlayer1();
        jogada1.setIdPartida(trucoDescription.getIdPartida());
        jogada1.setIndJogada(indJogada);
        jogada1.setTypeJogada(typeJogada);
        jogada1.setDetailJogada(detailJogada);
        jogada1.setStateDecision(statesDecision.name());
        jogada1.setJogadorMao(player1.equals(playerHand) ? 1 : 2);
        Deck deckPlayer = sortCards(player1);
        jogada1.setCartaAlta(deckPlayer.getCard(2).getCbrCode());
        jogada1.setNaipeCartaAlta(deckPlayer.getCard(2).getSuit().name());
        jogada1.setCartaMedia(deckPlayer.getCard(1).getCbrCode());
        jogada1.setNaipeCartaMedia(deckPlayer.getCard(1).getSuit().name());
        jogada1.setCartaBaixa(deckPlayer.getCard(0).getCbrCode());
        jogada1.setNaipeCartaBaixa(deckPlayer.getCard(0).getSuit().name());
        jogada1.setQualidadeMao(getQualidadeMao(deckPlayer));
        jogada1.setPontosEnvido(getEnvidoPoints(player1.getCards()));
        if (hasFlor(player1.getCards())) {
            jogada1.setPontosFlor(getEnvidoPoints(player1.getCards()));
        }
        jogada1.setTentos(player1.equals(playerRobo) ? trucoDescription.getTentosAnterioresRobo() :
                trucoDescription.getTentosAnterioresHumano());
        jogada1.setTentosOponente(player1.equals(playerRobo) ? trucoDescription.getTentosAnterioresHumano() :
                trucoDescription.getTentosAnterioresRobo());

        Set<Card> keys = dealtCards2.keySet();
        int i = 0;
        for (Card card : keys){
            i++;
            switch (i) {
                case 1:
                    jogada1.setPrimeiraCartaJogada(card.getCbrCode());
                    jogada1.setNaipePrimeiraCarta(card.getSuit().name());
                    jogada1.setQuemPrimeiraCarta(dealtCards2.get(card).equals(player1) ? 1 : 2);
                    break;
                case 2:
                    jogada1.setSegundaCartaJogada(card.getCbrCode());
                    jogada1.setNaipeSegundaCarta(card.getSuit().name());
                    jogada1.setQuemSegundaCarta(dealtCards2.get(card).equals(player1) ? 1 : 2);
                    break;
                case 3:
                    jogada1.setTerceiraCartaJogada(card.getCbrCode());
                    jogada1.setNaipeTerceiraCarta(card.getSuit().name());
                    jogada1.setQuemTerceiraCarta(dealtCards2.get(card).equals(player1) ? 1 : 2);
                    break;
                case 4:
                    jogada1.setQuartaCartaJogada(card.getCbrCode());
                    jogada1.setNaipeQuartaCarta(card.getSuit().name());
                    jogada1.setQuemQuartaCarta(dealtCards2.get(card).equals(player1) ? 1 : 2);
                    break;
                case 5:
                    jogada1.setQuintaCartaJogada(card.getCbrCode());
                    jogada1.setNaipeQuintaCarta(card.getSuit().name());
                    jogada1.setQuemQuintaCarta(dealtCards2.get(card).equals(player1) ? 1 : 2);
                    break;
                case 6:
                    jogada1.setSextaCartaJogada(card.getCbrCode());
                    jogada1.setNaipeSextaCarta(card.getSuit().name());
                    jogada1.setQuemSextaCarta(dealtCards2.get(card).equals(player1) ? 1 : 2);
                    break;
            }
        }

        switch (rounds.size()) {
            case 1:
                if (rounds.get(0).getResult() != Result.EMPATE) {
                    jogada1.setGanhadorPrimeiraRodada(rounds.get(0).getWinner().equals(player1) ? 1 : 2);
                } else {
                    jogada1.setGanhadorPrimeiraRodada(0);
                }
                break;
            case 2:
                if (rounds.get(0).getResult() != Result.EMPATE) {
                    jogada1.setGanhadorPrimeiraRodada(rounds.get(0).getWinner().equals(player1) ? 1 : 2);
                } else {
                    jogada1.setGanhadorPrimeiraRodada(0);
                }

                if (rounds.get(1).getResult() != Result.EMPATE) {
                    jogada1.setGanhadorSegundaRodada(rounds.get(1).getWinner().equals(player1) ? 1 : 2);
                } else {
                    jogada1.setGanhadorSegundaRodada(0);
                }
                break;
            case 3:
                if (rounds.get(0).getResult() != Result.EMPATE) {
                    jogada1.setGanhadorPrimeiraRodada(rounds.get(0).getWinner().equals(player1) ? 1 : 2);
                } else {
                    jogada1.setGanhadorPrimeiraRodada(0);
                }

                if (rounds.get(1).getResult() != Result.EMPATE) {
                    jogada1.setGanhadorSegundaRodada(rounds.get(1).getWinner().equals(player1) ? 1 : 2);
                } else {
                    jogada1.setGanhadorSegundaRodada(0);
                }

                if (rounds.get(2).getResult() != Result.EMPATE) {
                    jogada1.setGanhadorTerceiraRodada(rounds.get(2).getWinner().equals(player1) ? 1 : 2);
                } else {
                    jogada1.setGanhadorTerceiraRodada(0);
                }
                break;
        }

        setBluffPlayer1(naipe);
        if (playedCardsPlayer2.size() > 0) {
            jogada1.setPontosEnvidoOponente(getEnvidoPoints(playedCardsPlayer2));
        }

        jogada1.setTrucoDescription(trucoDescription);
        trucoDescription.getJogadasPlayer1().add(jogada1);
    }*/

    /*public void addJogadaPlayer2(int indJogada, int typeJogada, int detailJogada, Suit naipe){
        jogada2 = new JogadaPlayer2();
        jogada2.setIdPartida(trucoDescription.getIdPartida());
        jogada2.setIndJogada(indJogada);
        jogada2.setTypeJogada(typeJogada);
        jogada2.setDetailJogada(detailJogada);
        jogada2.setStateDecision(statesDecision.name());
        jogada2.setJogadorMao(player2.equals(playerHand) ? 1 : 2);
        Deck deckPlayer = sortCards(player2);
        jogada2.setCartaAlta(deckPlayer.getCard(2).getCbrCode());
        jogada2.setNaipeCartaAlta(deckPlayer.getCard(2).getSuit().name());
        jogada2.setCartaMedia(deckPlayer.getCard(1).getCbrCode());
        jogada2.setNaipeCartaMedia(deckPlayer.getCard(1).getSuit().name());
        jogada2.setCartaBaixa(deckPlayer.getCard(0).getCbrCode());
        jogada2.setNaipeCartaBaixa(deckPlayer.getCard(0).getSuit().name());
        jogada2.setQualidadeMao(getQualidadeMao(deckPlayer));
        jogada2.setPontosEnvido(getEnvidoPoints(player2.getCards()));
        if (hasFlor(player2.getCards())) {
            jogada2.setPontosFlor(getEnvidoPoints(player2.getCards()));
        }
        jogada2.setTentos(player2.equals(playerRobo) ? trucoDescription.getTentosAnterioresRobo() :
                trucoDescription.getTentosAnterioresHumano());
        jogada2.setTentosOponente(player2.equals(playerRobo) ? trucoDescription.getTentosAnterioresHumano() :
                trucoDescription.getTentosAnterioresRobo());

        Set<Card> keys = dealtCards2.keySet();
        int i = 0;
        for (Card card : keys){
            i++;
            switch (i) {
                case 1:
                    jogada2.setPrimeiraCartaJogada(card.getCbrCode());
                    jogada2.setNaipePrimeiraCarta(card.getSuit().name());
                    jogada2.setQuemPrimeiraCarta(dealtCards2.get(card).equals(player2) ? 1 : 2);
                    break;
                case 2:
                    jogada2.setSegundaCartaJogada(card.getCbrCode());
                    jogada2.setNaipeSegundaCarta(card.getSuit().name());
                    jogada2.setQuemSegundaCarta(dealtCards2.get(card).equals(player2) ? 1 : 2);
                    break;
                case 3:
                    jogada2.setTerceiraCartaJogada(card.getCbrCode());
                    jogada2.setNaipeTerceiraCarta(card.getSuit().name());
                    jogada2.setQuemTerceiraCarta(dealtCards2.get(card).equals(player2) ? 1 : 2);
                    break;
                case 4:
                    jogada2.setQuartaCartaJogada(card.getCbrCode());
                    jogada2.setNaipeQuartaCarta(card.getSuit().name());
                    jogada2.setQuemQuartaCarta(dealtCards2.get(card).equals(player2) ? 1 : 2);
                    break;
                case 5:
                    jogada2.setQuintaCartaJogada(card.getCbrCode());
                    jogada2.setNaipeQuintaCarta(card.getSuit().name());
                    jogada2.setQuemQuintaCarta(dealtCards2.get(card).equals(player2) ? 1 : 2);
                    break;
                case 6:
                    jogada2.setSextaCartaJogada(card.getCbrCode());
                    jogada2.setNaipeSextaCarta(card.getSuit().name());
                    jogada2.setQuemSextaCarta(dealtCards2.get(card).equals(player2) ? 1 : 2);
                    break;
            }
        }

        switch (rounds.size()) {
            case 1:
                if (rounds.get(0).getResult() != Result.EMPATE) {
                    jogada2.setGanhadorPrimeiraRodada(rounds.get(0).getWinner().equals(player2) ? 1 : 2);
                } else {
                    jogada2.setGanhadorPrimeiraRodada(0);
                }
                break;
            case 2:
                if (rounds.get(1).getResult() != Result.EMPATE) {
                    jogada2.setGanhadorSegundaRodada(rounds.get(1).getWinner().equals(player2) ? 1 : 2);
                } else {
                    jogada2.setGanhadorSegundaRodada(0);
                }
                break;
            case 3:
                if (rounds.get(2).getResult() != Result.EMPATE) {
                    jogada2.setGanhadorTerceiraRodada(rounds.get(2).getWinner().equals(player2) ? 1 : 2);
                } else {
                    jogada2.setGanhadorTerceiraRodada(0);
                }
                break;

        }

        setBluffPlayer2(naipe);
        if (playedCardsPlayer1.size() > 0) {
            jogada2.setPontosEnvidoOponente(getEnvidoPoints(playedCardsPlayer1));
        }
        jogada2.setTrucoDescription(trucoDescription);
        trucoDescription.getJogadasPlayer2().add(jogada2);
    }*/

    public void addLog(String log, String jogada, Player player) {

        Log log1 = new Log();
        log1.setIdPartida(uid);
        log1.setJogada(jogada);
        log1.setLog(log);
        log1.setPlayer(playerRobo == player ? TrucoData.ROBO: TrucoData.HUMANO);
        log1.setTrucoDescription(trucoDescription);
        trucoDescription.getLogs().add(log1);
    }

    public int getResult(Result result) {
        int resultado = 0;
        if (result == Result.PLAYER1) {
            resultado = getPlayerRobo() == getPlayer1() ? TrucoData.ROBO : TrucoData.HUMANO;
        } else if (result == Result.PLAYER2) {
            resultado = getPlayerRobo() == getPlayer2() ? TrucoData.ROBO : TrucoData.HUMANO;
        } else {
            resultado = 0;
        }

        return resultado;
    }

    /////////////Jogadas

    /*public void setBluffPlayer1(Suit naipe) {
        if (jogada1.getIndJogada() == 1 || jogada1.getIndJogada() == 5) {

            if (jogada1.getTypeJogada() == 2) {

                *//**#1 - Jogador mão canta ENVIDO/REAL/FALTA sem ter muitos pontos;*//*
                if (jogada1.getJogadorMao() == 1) {
                    if (jogada1.getPontosEnvido() <= 24) {
                        jogada1.setIndBlefe(1);
                        jogada1.setTypeBlefe(1);
                    }
                }  else {
                    if (jogada1.getPontosEnvido() <= 24) {
                        jogada1.setIndBlefe(1);
                        if (getEnvido().getEnvidoChain().size() > 0) {
                            *//**#6 - Jogador pé, sem ter muitos pontos, aumenta ENVIDO/REAL para oponente fugir;*//*
                            jogada1.setTypeBlefe(6);
                        } else {
                            *//**#3 - Jogador pé, sem ter muitos pontos, canta ENVIDO/REAL/FALTA porque o oponente não cantou;*//*
                            jogada1.setTypeBlefe(3);
                        }
                    }
                }
            }


            if (jogada1.getTypeJogada() == 3) {

                if (jogada1.getQualidadeMao() == 3) {

                    *//** #15 - Jogador com mão ruim chama TRUCO antes de mostrar as cartas;*//*
                    if (dealtCards.size() == 0 || dealtCards.size() == 1) {
                        jogada1.setIndBlefe(1);
                        jogada1.setTypeBlefe(15);
                    }

                    *//** #23 - Tem uma mão ruim, chama TRUCO antes de largar a segunda carta; *//*
                    if (dealtCards.size() == 2) {
                        jogada1.setIndBlefe(1);
                        jogada1.setTypeBlefe(23);
                    }

                    *//** #31 - Jogador possui terceira carta baixa, chama TRUCO/RETRUCO/VALE4 antes de largar a carta; *//*
                    if (dealtCards.size() == 4) {
                        jogada1.setIndBlefe(1);
                        jogada1.setTypeBlefe(31);
                    }
                }

                *//** #24 - Jogador perdeu primeira, é o pé e não tem carta para fazer a segunda, chama TRUCO antes de largar a segunda carta;*//*
                if (dealtCards.size() == 3) {
                    if (jogada1.getGanhadorPrimeiraRodada() == 2) {

                        boolean mata = false;

                        if (!isPlayedCard(playedCardsPlayer1, player1.getCards().getCard(0))) {
                            mata = player1.getCards().getCard(0).getCbrCode() > jogada1.getTerceiraCartaJogada();
                        } else if (!isPlayedCard(playedCardsPlayer1, player1.getCards().getCard(1))) {
                            mata = player1.getCards().getCard(1).getCbrCode() > jogada1.getTerceiraCartaJogada();
                        } else {
                            mata = player1.getCards().getCard(2).getCbrCode() > jogada1.getTerceiraCartaJogada();
                        }

                        if (!mata) {
                            jogada1.setIndBlefe(1);
                            jogada1.setTypeBlefe(24);
                        }

                    }

                }

                *//** #32 - Jogador é o pé e não tem carta para fazer a terceira, chama TRUCO antes de largar a terceira carta;*//*
                if (dealtCards.size() == 5) {
                    boolean mata = false;

                    if (!isPlayedCard(playedCardsPlayer1, player1.getCards().getCard(0))) {
                        mata = player1.getCards().getCard(0).getCbrCode() > jogada1.getQuintaCartaJogada();
                    } else if (!isPlayedCard(playedCardsPlayer1, player1.getCards().getCard(1))) {
                        mata = player1.getCards().getCard(1).getCbrCode() > jogada1.getQuintaCartaJogada();
                    } else {
                        mata = player1.getCards().getCard(2).getCbrCode() > jogada1.getQuintaCartaJogada();
                    }

                    if (!mata) {
                        jogada1.setIndBlefe(1);
                        jogada1.setTypeBlefe(32);
                    }

                }

            }

        }

        if (jogada1.getIndJogada() == 2) {
            if (jogada1.getTypeJogada() == 2) {
                if (jogada1.getJogadorMao() == 1) {
                    *//** #2 - Jogador mão deixa de cantar ENVIDO/REAL/FALTA mesmo com bastantes pontos; *//*
                    if (jogada1.getPontosEnvido() > 29) {
                        jogada1.setIndBlefe(1);
                        jogada1.setTypeBlefe(2);
                    }
                }
            }
        }

        if (jogada1.getIndJogada() == 7) {
            if (jogada1.getTypeJogada() == 2) {
                if (jogada1.getJogadorMao() == 2) {
                    *//** #4 - Jogador pé, não canta os pontos para não entregar suas cartas; *//*
                    jogada1.setIndBlefe(1);
                    jogada1.setTypeBlefe(4);
                }
            }
        }

        if (jogada1.getIndJogada() == 8) {
            if (jogada1.getTypeJogada() == 4) {

                switch (dealtCards2.size()) {
                    case 0:
                        if (jogada1.getJogadorMao() == 1) {
                            *//** #5 – Jogador mão, com poucos pontos, não canta envido e larga um 6 ou 7 para o adversário não cantar envido; *//*
                            if (jogada1.getPontosEnvido() <= 24) {
                                if (jogada1.getDetailJogada() == 42 || jogada1.getDetailJogada() == 40 ||
                                        jogada1.getDetailJogada() == 4) {
                                    jogada1.setIndBlefe(1);
                                    jogada1.setTypeBlefe(5);
                                }
                            }
                        }


                        if (jogada1.getQualidadeMao() == 1) {

                            *//**#11 - Jogador com mão boa larga a carta mais baixa;*//*
                            if (player1.getCards().getCard(0).getCbrCode() == jogada1.getDetailJogada()) {
                                jogada1.setIndBlefe(1);
                                jogada1.setTypeBlefe(11);
                            }

                            *//** #13 - Caso jogador com uma mão boa tenha cantado seus pontos de envido, joga carta para despitar as cartas que compoem os pontos; *//*
                            if (!getSuitEnvido(player1.getCards()).equals(naipe) && getEnvido().getEnvidoChain().size() > 0 &&
                                    (trucoDescription.getQuemEscondeuPontosEnvido() == null || (trucoDescription.getQuemEscondeuPontosEnvido() != null &&
                                            trucoDescription.getQuemEscondeuPontosEnvido() != 1)) &&
                                    isEnvidoPossible(jogada1.getDetailJogada(), jogada1.getPontosEnvido())) {
                                jogada1.setIndBlefe(1);
                                jogada1.setTypeBlefe(13);
                            }

                        }


                        if (jogada1.getJogadorMao() == 3) {

                            *//**#12 - Jogador com mão ruim larga a carta mais alta;*//*
                            if (player1.getCards().getCard(0).getCbrCode() == jogada1.getDetailJogada()) {
                                jogada1.setIndBlefe(1);
                                jogada1.setTypeBlefe(12);
                            }

                            *//** #14 - Caso jogador com uma mão ruim tenha cantado seus pontos de envido, joga carta para despitar as cartas que compoem os pontos; *//*
                            if (!getSuitEnvido(player1.getCards()).equals(naipe) && getEnvido().getEnvidoChain().size() > 0 &&
                                    (trucoDescription.getQuemEscondeuPontosEnvido() == null || (trucoDescription.getQuemEscondeuPontosEnvido() != null &&
                                            trucoDescription.getQuemEscondeuPontosEnvido() != 1)) &&
                                    isEnvidoPossible(jogada1.getDetailJogada(), jogada1.getPontosEnvido())) {
                                jogada1.setIndBlefe(1);
                                jogada1.setTypeBlefe(14);
                            }
                        }
                        break;
                    case 1:

                        if (jogada1.getQualidadeMao() == 1) {

                            *//**#11 - Jogador com mão boa larga a carta mais baixa;*//*
                            if (player1.getCards().getCard(0).getCbrCode() == jogada1.getDetailJogada()) {
                                jogada1.setIndBlefe(1);
                                jogada1.setTypeBlefe(11);
                            }

                            *//** #13 - Caso jogador com uma mão boa tenha cantado seus pontos de envido, joga carta para despitar as cartas que compoem os pontos; *//*
                            if (!getSuitEnvido(player1.getCards()).equals(naipe) && getEnvido().getEnvidoChain().size() > 0 &&
                                    (trucoDescription.getQuemEscondeuPontosEnvido() == null || (trucoDescription.getQuemEscondeuPontosEnvido() != null &&
                                            trucoDescription.getQuemEscondeuPontosEnvido() != 1)) &&
                                    isEnvidoPossible(jogada1.getDetailJogada(), jogada1.getPontosEnvido())) {
                                jogada1.setIndBlefe(1);
                                jogada1.setTypeBlefe(13);
                            }

                        }


                        if (jogada1.getJogadorMao() == 3) {

                            *//**#12 - Jogador com mão ruim larga a carta mais alta;*//*
                            if (player1.getCards().getCard(0).getCbrCode() == jogada1.getDetailJogada()) {
                                jogada1.setIndBlefe(1);
                                jogada1.setTypeBlefe(12);
                            }

                            *//** #14 - Caso jogador com uma mão ruim tenha cantado seus pontos de envido, joga carta para despitar as cartas que compoem os pontos; *//*
                            if (!getSuitEnvido(player1.getCards()).equals(naipe) && getEnvido().getEnvidoChain().size() > 0 &&
                                    (trucoDescription.getQuemEscondeuPontosEnvido() == null || (trucoDescription.getQuemEscondeuPontosEnvido() != null &&
                                            trucoDescription.getQuemEscondeuPontosEnvido() != 1)) &&
                                    isEnvidoPossible(jogada1.getDetailJogada(), jogada1.getPontosEnvido())) {
                                jogada1.setIndBlefe(1);
                                jogada1.setTypeBlefe(14);
                            }
                        }
                        break;
                }
            }
        }

        if (jogada1.getIndJogada() == 8 && jogada1.getTypeJogada() == 5 &&
                jogada1.getGanhadorPrimeiraRodada() == 1) {

            *//**#21 - Jogador ganhou primeira e tem uma mão boa, joga a segunda carta virada;*//*
            if (jogada1.getJogadorMao() == 1) {
                jogada1.setIndBlefe(1);
                jogada1.setTypeBlefe(21);
            }

            *//**#22 - Jogador ganhou primeira e tem uma mão ruim, joga a segunda carta virada;*//*
            if (jogada1.getJogadorMao() == 3) {
                jogada1.setIndBlefe(1);
                jogada1.setTypeBlefe(22);
            }
        }
    }*/

    /*public void setBluffPlayer2(Suit naipe) {
        if (jogada2.getIndJogada() == 1 || jogada2.getIndJogada() == 5) {

            if (jogada2.getTypeJogada() == 2) {

                *//**#1 - Jogador mão canta ENVIDO/REAL/FALTA sem ter muitos pontos;*//*
                if (jogada2.getJogadorMao() == 1) {
                    if (jogada2.getPontosEnvido() <= 24) {
                        jogada2.setIndBlefe(1);
                        jogada2.setTypeBlefe(1);
                    }
                }  else {
                    if (jogada2.getPontosEnvido() <= 24) {
                        jogada2.setIndBlefe(1);
                        if (getEnvido().getEnvidoChain().size() > 0) {
                            *//**#6 - Jogador pé, sem ter muitos pontos, aumenta ENVIDO/REAL para oponente fugir;*//*
                            jogada2.setTypeBlefe(6);
                        } else {
                            *//**#3 - Jogador pé, sem ter muitos pontos, canta ENVIDO/REAL/FALTA porque o oponente não cantou;*//*
                            jogada2.setTypeBlefe(3);
                        }
                    }
                }
            }


            if (jogada2.getTypeJogada() == 3) {

                if (jogada2.getQualidadeMao() == 3) {

                    *//** #15 - Jogador com mão ruim chama TRUCO antes de mostrar as cartas;*//*
                    if (dealtCards.size() == 0 || dealtCards.size() == 1) {
                        jogada2.setIndBlefe(1);
                        jogada2.setTypeBlefe(15);
                    }

                    *//** #23 - Tem uma mão ruim, chama TRUCO antes de largar a segunda carta; *//*
                    if (dealtCards.size() == 2) {
                        jogada2.setIndBlefe(1);
                        jogada2.setTypeBlefe(23);
                    }

                    *//** #31 - Jogador possui terceira carta baixa, chama TRUCO/RETRUCO/VALE4 antes de largar a carta; *//*
                    if (dealtCards.size() == 4) {
                        jogada2.setIndBlefe(1);
                        jogada2.setTypeBlefe(31);
                    }
                }

                *//** #24 - Jogador perdeu primeira, é o pé e não tem carta para fazer a segunda, chama TRUCO antes de largar a segunda carta;*//*
                if (dealtCards.size() == 3) {
                    if (jogada2.getGanhadorPrimeiraRodada() == 2) {

                        boolean mata = false;

                        if (!isPlayedCard(playedCardsPlayer2, player2.getCards().getCard(0))) {
                            mata = player2.getCards().getCard(0).getCbrCode() > jogada2.getTerceiraCartaJogada();
                        } else if (!isPlayedCard(playedCardsPlayer2, player2.getCards().getCard(1))) {
                            mata = player2.getCards().getCard(1).getCbrCode() > jogada2.getTerceiraCartaJogada();
                        } else {
                            mata = player2.getCards().getCard(2).getCbrCode() > jogada2.getTerceiraCartaJogada();
                        }

                        if (!mata) {
                            jogada2.setIndBlefe(1);
                            jogada2.setTypeBlefe(24);
                        }

                    }

                }

                *//** #32 - Jogador é o pé e não tem carta para fazer a terceira, chama TRUCO antes de largar a terceira carta;*//*
                if (dealtCards.size() == 5) {
                    boolean mata = false;

                    if (!isPlayedCard(playedCardsPlayer2, player2.getCards().getCard(0))) {
                        mata = player2.getCards().getCard(0).getCbrCode() > jogada2.getQuintaCartaJogada();
                    } else if (!isPlayedCard(playedCardsPlayer2, player2.getCards().getCard(1))) {
                        mata = player2.getCards().getCard(1).getCbrCode() > jogada2.getQuintaCartaJogada();
                    } else {
                        mata = player2.getCards().getCard(2).getCbrCode() > jogada2.getQuintaCartaJogada();
                    }

                    if (!mata) {
                        jogada2.setIndBlefe(1);
                        jogada2.setTypeBlefe(32);
                    }

                }

            }

        }

        if (jogada2.getIndJogada() == 2) {
            if (jogada2.getTypeJogada() == 2) {
                if (jogada2.getJogadorMao() == 1) {
                    *//** #2 - Jogador mão deixa de cantar ENVIDO/REAL/FALTA mesmo com bastantes pontos; *//*
                    if (jogada2.getPontosEnvido() > 29) {
                        jogada2.setIndBlefe(1);
                        jogada2.setTypeBlefe(2);
                    }
                }
            }
        }

        if (jogada2.getIndJogada() == 7) {
            if (jogada2.getTypeJogada() == 2) {
                if (jogada2.getJogadorMao() == 2) {
                    *//** #4 - Jogador pé, não canta os pontos para não entregar suas cartas; *//*
                    jogada2.setIndBlefe(1);
                    jogada2.setTypeBlefe(4);
                }
            }
        }

        if (jogada2.getIndJogada() == 8) {
            if (jogada2.getTypeJogada() == 4) {

                switch (dealtCards2.size()) {
                    case 0:
                        if (jogada2.getJogadorMao() == 1) {
                            *//** #5 – Jogador mão, com poucos pontos, não canta envido e larga um 6 ou 7 para o adversário não cantar envido; *//*
                            if (jogada2.getPontosEnvido() <= 24) {
                                if (jogada2.getDetailJogada() == 42 || jogada2.getDetailJogada() == 40 ||
                                        jogada2.getDetailJogada() == 4) {
                                    jogada2.setIndBlefe(1);
                                    jogada2.setTypeBlefe(5);
                                }
                            }
                        }


                        if (jogada2.getQualidadeMao() == 1) {

                            *//**#11 - Jogador com mão boa larga a carta mais baixa;*//*
                            if (player2.getCards().getCard(0).getCbrCode() == jogada2.getDetailJogada()) {
                                jogada2.setIndBlefe(1);
                                jogada2.setTypeBlefe(11);
                            }

                            *//** #13 - Caso jogador com uma mão boa tenha cantado seus pontos de envido, joga carta para despitar as cartas que compoem os pontos; *//*
                            if (!getSuitEnvido(player2.getCards()).equals(naipe) && getEnvido().getEnvidoChain().size() > 0 &&
                                    (trucoDescription.getQuemEscondeuPontosEnvido() == null || (trucoDescription.getQuemEscondeuPontosEnvido() != null &&
                                            trucoDescription.getQuemEscondeuPontosEnvido() != 1)) &&
                                    isEnvidoPossible(jogada2.getDetailJogada(), jogada2.getPontosEnvido())) {
                                jogada2.setIndBlefe(1);
                                jogada2.setTypeBlefe(13);
                            }

                        }


                        if (jogada2.getJogadorMao() == 3) {

                            *//**#12 - Jogador com mão ruim larga a carta mais alta;*//*
                            if (player2.getCards().getCard(0).getCbrCode() == jogada2.getDetailJogada()) {
                                jogada2.setIndBlefe(1);
                                jogada2.setTypeBlefe(12);
                            }

                            *//** #14 - Caso jogador com uma mão ruim tenha cantado seus pontos de envido, joga carta para despitar as cartas que compoem os pontos; *//*
                            if (!getSuitEnvido(player2.getCards()).equals(naipe) && getEnvido().getEnvidoChain().size() > 0 &&
                                    (trucoDescription.getQuemEscondeuPontosEnvido() == null || (trucoDescription.getQuemEscondeuPontosEnvido() != null &&
                                            trucoDescription.getQuemEscondeuPontosEnvido() != 1)) &&
                                    isEnvidoPossible(jogada2.getDetailJogada(), jogada2.getPontosEnvido())) {
                                jogada2.setIndBlefe(1);
                                jogada2.setTypeBlefe(14);
                            }
                        }
                        break;
                    case 1:

                        if (jogada2.getQualidadeMao() == 1) {

                            *//**#11 - Jogador com mão boa larga a carta mais baixa;*//*
                            if (player2.getCards().getCard(0).getCbrCode() == jogada2.getDetailJogada()) {
                                jogada2.setIndBlefe(1);
                                jogada2.setTypeBlefe(11);
                            }

                            *//** #13 - Caso jogador com uma mão boa tenha cantado seus pontos de envido, joga carta para despitar as cartas que compoem os pontos; *//*
                            if (!getSuitEnvido(player2.getCards()).equals(naipe) && getEnvido().getEnvidoChain().size() > 0 &&
                                    (trucoDescription.getQuemEscondeuPontosEnvido() == null || (trucoDescription.getQuemEscondeuPontosEnvido() != null &&
                                            trucoDescription.getQuemEscondeuPontosEnvido() != 1)) &&
                                    isEnvidoPossible(jogada2.getDetailJogada(), jogada2.getPontosEnvido())) {
                                jogada2.setIndBlefe(1);
                                jogada2.setTypeBlefe(13);
                            }

                        }


                        if (jogada2.getJogadorMao() == 3) {

                            *//**#12 - Jogador com mão ruim larga a carta mais alta;*//*
                            if (player2.getCards().getCard(0).getCbrCode() == jogada2.getDetailJogada()) {
                                jogada2.setIndBlefe(1);
                                jogada2.setTypeBlefe(12);
                            }

                            *//** #14 - Caso jogador com uma mão ruim tenha cantado seus pontos de envido, joga carta para despitar as cartas que compoem os pontos; *//*
                            if (!getSuitEnvido(player2.getCards()).equals(naipe) && getEnvido().getEnvidoChain().size() > 0 &&
                                    (trucoDescription.getQuemEscondeuPontosEnvido() == null || (trucoDescription.getQuemEscondeuPontosEnvido() != null &&
                                            trucoDescription.getQuemEscondeuPontosEnvido() != 1)) &&
                                    isEnvidoPossible(jogada2.getDetailJogada(), jogada2.getPontosEnvido())) {
                                jogada2.setIndBlefe(1);
                                jogada2.setTypeBlefe(14);
                            }
                        }
                        break;
                }
            }
        }

        if (jogada2.getIndJogada() == 8 && jogada2.getTypeJogada() == 5 &&
                jogada2.getGanhadorPrimeiraRodada() == 1) {

            *//**#21 - Jogador ganhou primeira e tem uma mão boa, joga a segunda carta virada;*//*
            if (jogada2.getJogadorMao() == 1) {
                jogada2.setIndBlefe(1);
                jogada2.setTypeBlefe(21);
            }

            *//**#22 - Jogador ganhou primeira e tem uma mão ruim, joga a segunda carta virada;*//*
            if (jogada2.getJogadorMao() == 3) {
                jogada2.setIndBlefe(1);
                jogada2.setTypeBlefe(22);
            }
        }
    }*/

    public boolean isEnvidoPossible(int cbrCode, int pontos) {

        boolean isPossible = false;

        if (pontos == 33) {
            if (cbrCode == 4 || cbrCode == 42 || cbrCode == 40 || cbrCode == 3)
                isPossible = true;
        } else if (pontos == 32) {
            if (cbrCode == 2 || cbrCode == 4 || cbrCode == 42 || cbrCode == 40)
                isPossible = true;
        } else if (pontos == 31) {
            if (cbrCode == 1 || cbrCode == 4 || cbrCode == 42 || cbrCode == 40 || cbrCode == 2 || cbrCode == 3)
                isPossible = true;
        } else if (pontos == 30) {
            if (cbrCode == 24 || cbrCode == 4 || cbrCode == 42 || cbrCode == 40 || cbrCode == 1 || cbrCode == 3)
                isPossible = true;
        } else if (pontos == 29) {
            if (cbrCode == 16 || cbrCode == 4 || cbrCode == 42 || cbrCode == 40 || cbrCode == 24 || cbrCode == 3 ||
                    cbrCode == 1 || cbrCode == 2 )
                isPossible = true;
        } else if (pontos == 28) {
            if (cbrCode == 12 || cbrCode == 52 || cbrCode == 50 || cbrCode == 4 || cbrCode == 42 || cbrCode == 40 ||
                    cbrCode == 16 || cbrCode == 3 || cbrCode == 24 || cbrCode == 2 )
                isPossible = true;
        } else if (pontos == 27) {
            if (cbrCode == 12 || cbrCode == 52 || cbrCode == 50 || cbrCode == 6 || cbrCode == 7 || cbrCode == 8 ||
                    cbrCode == 16 || cbrCode == 24 || cbrCode == 1 || cbrCode == 2 || cbrCode == 3 || cbrCode == 4 ||
                    cbrCode == 42 || cbrCode == 40)
                isPossible = true;
        } else if (pontos == 26) {
            if (cbrCode == 12 || cbrCode == 52 || cbrCode == 50 || cbrCode == 6 || cbrCode == 7 || cbrCode == 8 ||
                    cbrCode == 16 || cbrCode == 1 || cbrCode == 2 || cbrCode == 3)
                isPossible = true;
        } else if (pontos == 25) {
            if (cbrCode == 12 || cbrCode == 52 || cbrCode == 50 || cbrCode == 6 || cbrCode == 7 || cbrCode == 8 ||
                    cbrCode == 16 || cbrCode == 24 || cbrCode == 1 || cbrCode == 2)
                isPossible = true;
        } else if (pontos == 24) {
            if (cbrCode == 12 || cbrCode == 52 || cbrCode == 50 || cbrCode == 6 || cbrCode == 7 || cbrCode == 8 ||
                    cbrCode == 24 || cbrCode == 1)
                isPossible = true;
        } else if (pontos == 23) {
            if (cbrCode == 12 || cbrCode == 52 || cbrCode == 50 || cbrCode == 6 || cbrCode == 7 || cbrCode == 8 ||
                    cbrCode == 16 || cbrCode == 24)
                isPossible = true;
        } else if (pontos == 22) {
            if (cbrCode == 12 || cbrCode == 52 || cbrCode == 50 || cbrCode == 6 || cbrCode == 7 || cbrCode == 8 || cbrCode == 16)
                isPossible = true;
        } else if (pontos == 21) {
            if (cbrCode == 12 || cbrCode == 52 || cbrCode == 50 || cbrCode == 6 || cbrCode == 7 || cbrCode == 8)
                isPossible = true;
        } else if (pontos == 20) {
            if (cbrCode == 6 || cbrCode == 7 || cbrCode == 8)
                isPossible = true;
        } else if (pontos == 7) {
            if (cbrCode == 4 || cbrCode == 42 || cbrCode == 40)
                isPossible = true;
        } else if (pontos == 6) {
            if (cbrCode == 3)
                isPossible = true;
        } else if (pontos == 5) {
            if (cbrCode == 2)
                isPossible = true;
        } else if (pontos == 4) {
            if (cbrCode == 1)
                isPossible = true;
        } else if (pontos == 3) {
            if (cbrCode == 24)
                isPossible = true;
        } else if (pontos == 2) {
            if (cbrCode == 16)
                isPossible = true;
        } else if (pontos == 1) {
            if (cbrCode == 12 || cbrCode == 52 || cbrCode == 50)
                isPossible = true;
        }

        return isPossible;
    }

    public int getQualidadeMao(Deck sortedDeck) {

        if ((sortedDeck.getCard(2).getCbrCode() > 24 && sortedDeck.getCard(1).getCbrCode() > 16) ||
                (sortedDeck.getCard(2).getCbrCode() > 24 && sortedDeck.getCard(1).getCbrCode() > 12 && sortedDeck.getCard(0).getCbrCode() > 4)){
            return 1;
        } else if (sortedDeck.getCard(2).getCbrCode() < 12 || sortedDeck.getCard(1).getCbrCode() < 6) {
            return 3;
        } else {
            return 2;
        }

    }

    public boolean isPlayedCard(LinkedList<Card> list, Card card) {

        for (Card c : list) {
            if (c.getFace() == card.getFace() && c.getSuit()==card.getSuit()) {
                return true;
            }
            /*if (c.equals(card)) {
                return true;
            }*/
        }


        return false;
    }


    public int getHandRanking(Deck deck) {
        return deck.getCard(0).getCbrCode() + deck.getCard(1).getCbrCode() + deck.getCard(2).getCbrCode();
    }

    public String getRodada(int round) {
        String rodada = "";

        switch (round) {
            case 0:
                rodada = "1";
                break;
            case 1:
                rodada = "1";
                break;
            case 2:
                rodada = "2";
                break;
            case 3:
                rodada = "2";
                break;
            case 4:
                rodada = "3";
                break;
            case 5:
                rodada = "3";
                break;
           /* case 6:
                rodada = "3";
                break;*/
        }

        return rodada;
    }

    public Suit getSuitEnvido(Deck deck) {
        HashMap<Suit, ArrayList<Card>> cardsBySuit = new HashMap<>();

        deck.getCards().forEach(card -> {
            if (!cardsBySuit.containsKey(card.getSuit())) {
                cardsBySuit.put(card.getSuit(), new ArrayList<>());
            }
            cardsBySuit.get(card.getSuit()).add(card);
        });

        int modeLength = 0;
        Suit modeSuit = Suit.ESPADAS;

        for (Suit suit : cardsBySuit.keySet()) {
            if (cardsBySuit.get(suit).size() > modeLength) {
                modeLength = cardsBySuit.get(suit).size();
                modeSuit = suit;
            }
        }

        return modeSuit;
    }

    private void loadTestHandJson() {
        try {

            JSONParser parser = new JSONParser();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("maosExperimentos.json");

            JSONArray jsonArray = (JSONArray) parser.parse(new InputStreamReader(inputStream));

            Deck deckMaos = new Deck();
            for (Object object : jsonArray) {

                JSONObject mao = (JSONObject) object;

                Face faceCard1 = getFaceByCbrCode((int) (long) mao.get("carta1"));
                Suit suitCard1 = getSuitByString((String) mao.get("naipeCarta1"));
                int cbrCodeCard1 = (int) (long) mao.get("carta1");

                Face faceCard2 = getFaceByCbrCode((int) (long) mao.get("carta2"));
                Suit suitCard2 = getSuitByString((String) mao.get("naipeCarta2"));
                int cbrCodeCard2 = (int) (long) mao.get("carta2");

                Face faceCard3 = getFaceByCbrCode((int) (long) mao.get("carta3"));
                Suit suitCard3 = getSuitByString((String) mao.get("naipeCarta3"));
                int cbrCodeCard3 = (int) (long) mao.get("carta3");

                Face faceCard4 = getFaceByCbrCode((int) (long) mao.get("carta4"));
                Suit suitCard4 = getSuitByString((String) mao.get("naipeCarta4"));
                int cbrCodeCard4 = (int) (long) mao.get("carta4");

                Face faceCard5 = getFaceByCbrCode((int) (long) mao.get("carta5"));
                Suit suitCard5 = getSuitByString((String) mao.get("naipeCarta5"));
                int cbrCodeCard5 = (int) (long) mao.get("carta5");

                Face faceCard6 = getFaceByCbrCode((int) (long) mao.get("carta6"));
                Suit suitCard6 = getSuitByString((String) mao.get("naipeCarta6"));
                int cbrCodeCard6 = (int) (long) mao.get("carta6");

                Card card1 = new Card(faceCard1, suitCard1, cbrCodeCard1);
                Card card2 = new Card(faceCard2, suitCard2, cbrCodeCard2);
                Card card3 = new Card(faceCard3, suitCard3, cbrCodeCard3);
                Card card4 = new Card(faceCard4, suitCard4, cbrCodeCard4);
                Card card5 = new Card(faceCard5, suitCard5, cbrCodeCard5);
                Card card6 = new Card(faceCard6, suitCard6, cbrCodeCard6);

                deckMaos.addCard(card1);
                deckMaos.addCard(card2);
                deckMaos.addCard(card3);
                deckMaos.addCard(card4);
                deckMaos.addCard(card5);
                deckMaos.addCard(card6);
            }

            System.out.println(deckMaos.getCards().toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private Suit getSuitByString(String suitString) {
        Suit suit = null;

        switch (suitString) {
            case "ESPADAS":
                suit = Suit.ESPADAS;
                break;
            case "BASTOS":
                suit = Suit.BASTOS;
                break;
            case "OURO":
                suit = Suit.OURO;
                break;
            case "COPAS":
                suit = Suit.COPAS;
                break;
        }

        return suit;
    }

    private Face getFaceByCbrCode(int cbrCode) {
        Face face = null;
        switch (cbrCode) {
            case 52:
                face = Face.AS;
                break;
            case 50:
                face = Face.AS;
                break;
            case 42:
                face = Face.SETE;
                break;
            case 40:
                face = Face.SETE;
                break;
            case 24:
                face = Face.TRES;
                break;
            case 16:
                face = Face.DOIS;
                break;
            case 12:
                face = Face.AS;
                break;
            case 8:
                face = Face.REI;
                break;
            case 7:
                face = Face.VALETE;
                break;
            case 6:
                face = Face.DEZ;
                break;
            case 4:
                face = Face.SETE;
                break;
            case 3:
                face = Face.SEIS;
                break;
            case 2:
                face = Face.CINCO;
                break;
            case 1:
                face = Face.QUATRO;
                break;
        }

        return face;

    }




}
