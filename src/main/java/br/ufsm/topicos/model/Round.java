package br.ufsm.topicos.model;

import br.ufsm.topicos.deck.Card;
import br.ufsm.topicos.enuns.Result;

/**
 * Universidade Federal de Santa Maria
 * Pós-Graduação em Ciência da Computação
 * Tópicos em Computação Aplicada
 * Daniel Pinheiro Vargas
 * Criado em 31/07/2018.
 */

public class Round {

    private int number;
    private Card player1Card;
    private Card player2Card;
    private Player winner;
    private Result result;
    private boolean faceDownCardPlayer1;
    private boolean faceDownCardPlayer2;

    public Round(int number) {
        this.number = number;
        this.faceDownCardPlayer1 = false;
        this.faceDownCardPlayer2 = false;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public Card getPlayer2Card() {
        return player2Card;
    }

    public void setPlayer2Card(Card player2Card) {
        this.player2Card = player2Card;
    }

    public Card getPlayer1Card() {
        return player1Card;
    }

    public void setPlayer1Card(Card player1Card) {
        this.player1Card = player1Card;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public boolean isFaceDownCardPlayer1() {
        return faceDownCardPlayer1;
    }

    public void setFaceDownCardPlayer1(boolean faceDownCardPlayer1) {
        this.faceDownCardPlayer1 = faceDownCardPlayer1;
    }

    public boolean isFaceDownCardPlayer2() {
        return faceDownCardPlayer2;
    }

    public void setFaceDownCardPlayer2(boolean faceDownCardPlayer2) {
        this.faceDownCardPlayer2 = faceDownCardPlayer2;
    }
}
