package br.ufsm.topicos.model;

import br.ufsm.topicos.deck.Deck;

import javax.websocket.Session;

/**
 * Universidade Federal de Santa Maria
 * Pós-Graduação em Ciência da Computação
 * Tópicos em Computação Aplicada
 * Daniel Pinheiro Vargas
 * Criado em 19/07/2018.
 */

public class Player {

    private Deck cards = new Deck();
    private String name;
    private int id;
    private Session session;

    public Player(int id, String name, Session session) {
        this.id = id;
        this.name = name;
        this.session = session;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Deck getCards() {
        return cards;
    }

    public void setCards(Deck cards) {
        this.cards = cards;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
