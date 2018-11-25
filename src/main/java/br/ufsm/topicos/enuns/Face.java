package br.ufsm.topicos.enuns;

/**
 * Universidade Federal de Santa Maria
 * Pós-Graduação em Ciência da Computação
 * Tópicos em Computação Aplicada
 * Daniel Pinheiro Vargas
 * Criado em 19/07/2018.
 */

public enum Face {
    AS(1), DOIS(2), TRES(3), QUATRO(4), CINCO(5), SEIS(6), SETE(7), DEZ(0), VALETE(0), REI(0);

    private int value;

    Face(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
