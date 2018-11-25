package br.ufsm.topicos.model;

import br.ufsm.topicos.cbr.TrucoDescription;
import br.ufsm.topicos.deck.Card;
import br.ufsm.topicos.deck.Deck;
import br.ufsm.topicos.enuns.*;
import br.ufsm.topicos.exceptions.ExceptionEnvido;
import br.ufsm.topicos.exceptions.ExceptionFlor;
import br.ufsm.topicos.exceptions.ExceptionTruco;
import br.ufsm.topicos.hibernate.HibernateConfig;
import br.ufsm.topicos.log.Log;
import org.hibernate.Session;


import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
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

    private LinkedList<Card> playedCardsPlayer1;
    private LinkedList<Card> playedCardsPlayer2;

    private TrucoDescription trucoDescription;

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
    }

    public void setCardsToCase() {
        Deck deckRobo;
        Deck deckHumano;
        if (playerRobo == getPlayer1()) {
            deckRobo = sortCards(getPlayer1());
            deckHumano = sortCards(getPlayer2());
        } else {
            deckRobo = sortCards(getPlayer2());
            deckHumano = sortCards(getPlayer1());
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
        trucoDescription.setNaipeCartaBaixaHumano(deckHumano.getCard(2).getSuit().toString());
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
        trucoDescription.setJogadorMao(playerRobo == playerHand ? TrucoData.ROBO : TrucoData.HUMANO);
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
        trucoDescription.setPontosEnvidoRobo(playerRobo == getPlayer1() ? ptsEnvidoPlayer1 : ptsEnvidoPlayer2);
        trucoDescription.setPontosEnvidoHumano(playerRobo == getPlayer1() ? ptsEnvidoPlayer2 : ptsEnvidoPlayer1);

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
        shiftHand();
        init();
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

    public void deceptiveBehaviorTruco(Player origem, Player destino, int trucoLevel) {
        Deck sortedDeck = sortCards(origem);
        int handSum = getHandSum(origem);
        boolean cartaMaior = false;
        LinkedList<Card> handCards = new LinkedList<>();
        for(Card card : origem.getCards().getCards()) {
            if (origem == getPlayer1()) {
                if (!playedCardsPlayer1.contains(card)) {
                    handCards.add(card);
                }
            } else {
                if (!playedCardsPlayer2.contains(card)) {
                    handCards.add(card);
                }
            }
        }
        switch (dealtCards2.size()) {
            case 0:
                if (sortedDeck.getCard(2).getCbrCode() < 24 && sortedDeck.getCard(1).getCbrCode() < 16) {
                    if (playerRobo == origem) {
                        trucoDescription.setRoboMentiuTruco(TrucoData.DECEPTION_TRUCO_MAO_RUIM);
                    } else {
                        trucoDescription.setHumanoMentiuTruco(TrucoData.DECEPTION_TRUCO_MAO_RUIM);
                    }
                }
                break;
            case 1:
                //TODO: canta truco mesmo sem ter carta para matar a primeira.
                for(Card card : origem.getCards().getCards()) {
                    if (card.getCbrCode() > (destino == getPlayer1() ? playedCardsPlayer1.getFirst().getCbrCode() :
                            playedCardsPlayer2.getFirst().getCbrCode())) {
                        cartaMaior = true;
                    }
                }
                if (!cartaMaior) {
                    if (playerRobo == origem) {
                        trucoDescription.setRoboMentiuTruco(TrucoData.DECEPTION_TRUCO_CARTA_RUIM);
                    } else {
                        trucoDescription.setHumanoMentiuTruco(TrucoData.DECEPTION_TRUCO_CARTA_RUIM);
                    }
                }
                break;
            case 2:
                //TODO: perdeu a primeira rodada e tem mão ruim, mas pede truco para ver se adversario foge
                if (origem != round1.getWinner() && round1.getResult() != Result.EMPATE) {

                    if (handCards.getFirst().getCbrCode() < 16 && handCards.getLast().getCbrCode() < 16) {
                        if (playerRobo == origem) {
                            trucoDescription.setRoboMentiuTruco(TrucoData.DECEPTION_TRUCO_MAO_RUIM);
                        } else {
                            trucoDescription.setHumanoMentiuTruco(TrucoData.DECEPTION_TRUCO_MAO_RUIM);
                        }
                    }
                }
                break;
            case 3:
                //TODO: perdeu a primeira rodada e não tem carta para fazer a segunda.
                if (origem != round1.getWinner() && round1.getResult() != Result.EMPATE) {
                    for (Card card : handCards) {
                        if (card.getCbrCode() > (destino == getPlayer1() ? round2.getPlayer1Card().getCbrCode() :
                                round2.getPlayer2Card().getCbrCode())) {
                            cartaMaior = true;
                        }

                    }
                    if (!cartaMaior) {
                        if (playerRobo == origem) {
                            trucoDescription.setRoboMentiuTruco(TrucoData.DECEPTION_TRUCO_CARTA_RUIM);
                        } else {
                            trucoDescription.setHumanoMentiuTruco(TrucoData.DECEPTION_TRUCO_CARTA_RUIM);
                        }
                    }
                }
                break;
            case 4:
                //TODO: tem uma carta muito baixa para vencer
                if (handCards.getLast().getCbrCode() < 12) {
                    if (playerRobo == origem) {
                        trucoDescription.setRoboMentiuTruco(TrucoData.DECEPTION_TRUCO_MAO_RUIM);
                    } else {
                        trucoDescription.setHumanoMentiuTruco(TrucoData.DECEPTION_TRUCO_MAO_RUIM);
                    }
                }
                break;
            case 5:
                //TODO: não tem carta para ganhar
                if (handCards.getLast().getCbrCode() > (destino == getPlayer1() ? round3.getPlayer1Card().getCbrCode() :
                        round3.getPlayer2Card().getCbrCode())) {
                    cartaMaior = true;
                }
                break;
        }

        //TODO: com mão boa, não canta truco para poder aumentar
    }

    public void deceptiveBehaviorEnvido(Player origem, Player destino, int envidoLevel) {

        if (getEnvidoPoints(origem.getCards()) < 21) {
            //TODO: pediu envido sem ter pontos;
            if (playerRobo == origem) {
                trucoDescription.setRoboMentiuEnvido(TrucoData.DECEPTION_ENVIDO_SEM_PONTO);
            } else {
                trucoDescription.setHumanoMentiuEnvido(TrucoData.DECEPTION_ENVIDO_SEM_PONTO);
            }
        }

        //TODO: era mão com bastante pontos e não pediu envido para poder aumentar
    }

    public void deceptiveBehaviorPlayCard(Player origem, Player destino) {
        //TODO: joga carta virada
        //TODO: tem mão boa e deixa o adversario fazer a primeira.
        //TODO: era mão com bastante pontos e não pediu envido para poder aumentar
        //TODO: tem mão ruim mas tenta fazer a primeira.
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
                .add("round", getRodada(round))
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




}
