package br.ufsm.topicos.enuns;

/**
 * Universidade Federal de Santa Maria
 * Pós-Graduação em Ciência da Computação
 * Tópicos em Computação Aplicada
 * Daniel Pinheiro Vargas
 * Criado em 21/07/2018.
 */

public enum TrucoLevel {
    TRUCO(2, 1),
    RETRUCO(3, 2),
    VALE4(4, 3);

    private int pointsWorth;
    private int pointsWhenOtherGivesUp;

    public int getPointsWorth() {
        return pointsWorth;
    }
    public int getPointsWhenOtherGivesUp() {
        return pointsWhenOtherGivesUp;
    }

    TrucoLevel(int pointsWorth, int pointsWhenOtherGivesUp) {
        this.pointsWorth = pointsWorth;
        this.pointsWhenOtherGivesUp = pointsWhenOtherGivesUp;
    }
}
