package br.ufsm.topicos.enuns;

/**
 * Universidade Federal de Santa Maria
 * Pós-Graduação em Ciência da Computação
 * Tópicos em Computação Aplicada
 * Daniel Pinheiro Vargas
 * Criado em 21/07/2018.
 */

public enum EnvidoLevel {
    ENVIDO         (2),
    ENVIDO_ENVIDO  (2),
    REAL_ENVIDO    (3),
    FALTA_ENVIDO   (24);

    private int pointsWorth;

    public int getPointsWorth() {
        return pointsWorth;
    }

    EnvidoLevel(int pointsWorth) {
        this.pointsWorth = pointsWorth;
    }
}
