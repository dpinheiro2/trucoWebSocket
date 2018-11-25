package br.ufsm.topicos.enuns;

/**
 * Universidade Federal de Santa Maria
 * Pós-Graduação em Ciência da Computação
 * Tópicos em Computação Aplicada
 * Daniel Pinheiro Vargas
 * Criado em 29/07/2018.
 */

public enum FlorLevel {

    FLOR                (3, 0),
    FLOR_FLOR           (6, 0),
    CONTRA_FLOR         (6, 4),
    CONTRA_FLOR_FALTA   (24, 4),
    CONTRA_FLOR_RESTO   (24, 4);


    private int pointsWorth;
    private int pointsWhenOtherGivesUp;

    public int getPointsWorth() {
        return pointsWorth;
    }
    public int getPointsWhenOtherGivesUp() {
        return pointsWhenOtherGivesUp;
    }

    FlorLevel(int pointsWorth, int pointsWhenOtherGivesUp) {
        this.pointsWorth = pointsWorth;
        this.pointsWhenOtherGivesUp = pointsWhenOtherGivesUp;
    }


}
