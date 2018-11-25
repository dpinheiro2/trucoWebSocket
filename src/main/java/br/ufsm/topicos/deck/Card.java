package br.ufsm.topicos.deck;

import br.ufsm.topicos.enuns.Face;
import br.ufsm.topicos.enuns.Suit;

import java.util.Comparator;

/**
 * Universidade Federal de Santa Maria
 * Pós-Graduação em Ciência da Computação
 * Tópicos em Computação Aplicada
 * Daniel Pinheiro Vargas
 * Criado em 19/07/2018.
 */

public class Card {

    private Face face;
    private Suit suit;
    private int cbrCode;

    public Card(Face face, Suit suit, int cbrCode) {
        this.face = face;
        this.cbrCode = cbrCode;
        this.suit = suit;
    }

    public Face getFace() {
        return face;
    }

    public void setFace(Face face) {
        this.face = face;
    }

    public int getCbrCode() {
        return cbrCode;
    }


    public void setCbrCode(int cbrCode) {
        this.cbrCode = cbrCode;
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public static Comparator<Card> compareCards() {
        Comparator<Card> comp = new Comparator<Card>() {
            @Override
            public int compare(Card c1, Card c2) {
                if(c1 == null || c2 == null) {
                    return -1;
                }
                return new Integer(c1.getCbrCode()).compareTo(new Integer(c2.getCbrCode()));
            }
        };
        return comp;
    }

    @Override
    public String toString() {
        return "Card{" +
                "face=" + face +
                ", suit=" + suit +
                ", cbrCode=" + cbrCode +
                '}';
    }
}
