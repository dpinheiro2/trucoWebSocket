package br.ufsm.topicos.deck;

import br.ufsm.topicos.enuns.Face;
import br.ufsm.topicos.enuns.Suit;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Universidade Federal de Santa Maria
 * Pós-Graduação em Ciência da Computação
 * Tópicos em Computação Aplicada
 * Daniel Pinheiro Vargas
 * Criado em 19/07/2018.
 */

public class Deck {

    private ArrayList<Card> cards;

    public Deck() {
        this.cards = new ArrayList<Card>();
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public void createDeck() {
        /*int i = 0;*/
        for(Suit suit : Suit.values()) {
            for(Face face : Face.values()) {
                /*i++;*/
                this.cards.add(new Card(face, suit, getCbrCode(face, suit)));
                /*System.out.println("INSERT into cards values (" + i + "," + "\"" + face.name() + "\"" + "," + "\"" +
                        suit.name() + "\""+ "," + getCbrCode(face, suit) + ");");*/
            }
        }
    }

    public void suffle() {
        Collections.shuffle(this.cards);
    }

    public void removeCard(int i){
        this.cards.remove(i);
    }

    public Card getCard(int i){
        return this.cards.get(i);
    }

    public void addCard(Card card){
        this.cards.add(card);
    }

    public void draw(Deck monte, int index){
        this.cards.add(monte.getCard(index));
    }

    public void giveBackCardsDeck(){
        int thisDeckSize = this.cards.size();
        //empty out the deck
        for(int i = 0; i < thisDeckSize; i++){
            this.removeCard(0);
        }
    }

    private int getCbrCode(Face face, Suit suit) {
        int cbrCode = 0;
        switch (face) {
            case AS:
                switch (suit) {
                    case ESPADAS:
                        cbrCode = 52;
                        break;
                    case BASTOS:
                        cbrCode = 50;
                        break;
                    default:
                        cbrCode = 12;
                }
                break;
            case DOIS:
                cbrCode = 16;
                break;
            case TRES:
                cbrCode = 24;
                break;
            case QUATRO:
                cbrCode = 1;
                break;
            case CINCO:
                cbrCode = 2;
                break;
            case SEIS:
                cbrCode = 3;
                break;
            case SETE:
                switch (suit) {
                    case ESPADAS:
                        cbrCode = 42;
                        break;
                    case OURO:
                        cbrCode = 40;
                        break;
                    default:
                        cbrCode = 4;
                }
                break;
            case DEZ:
                cbrCode = 6;
                break;
            case VALETE:
                cbrCode = 7;
                break;
            case REI:
                cbrCode = 8;
                break;
        }

        return cbrCode;
    }

}
