package br.ufsm.topicos.model;

import br.ufsm.topicos.enuns.TrucoLevel;
import br.ufsm.topicos.exceptions.ExceptionTruco;

import java.util.LinkedList;

/**
 * Universidade Federal de Santa Maria
 * Pós-Graduação em Ciência da Computação
 * Tópicos em Computação Aplicada
 * Daniel Pinheiro Vargas
 * Criado em 23/07/2018.
 */

public class Truco {

    private LinkedList<TrucoLevel> trucoChain = new LinkedList<TrucoLevel>();

    public Truco() {
    }

    public LinkedList<TrucoLevel> getTrucoChain() {
        return trucoChain;
    }

    public void resetTrucoChain() {
        trucoChain.clear();
    }

    public void agregarTruco(TrucoLevel trucoLevel) throws ExceptionTruco {

        if (trucoChain.size() == 3)
            throw new ExceptionTruco("Não se pode mais cantar truco, retruco ou vale 4.");

        if (!trucoChain.isEmpty()) {
            if (trucoChain.getLast().ordinal() > trucoLevel.ordinal() )
                throw new ExceptionTruco("Truco tem uma ordem : truco --> retruco --> Vale4.");
        }
        if (trucoChain.contains(trucoLevel))
            throw new ExceptionTruco(trucoLevel.toString() + " já foi cantado.");

        trucoChain.add(trucoLevel);
    }

    public int getPontosGanhosAceitos() {
        int ptsGanhos = trucoChain.getLast().getPointsWorth();
        return ptsGanhos;
    }

    public int getPontosGanhosNaoAceitos() {
        int ptsGanhos = trucoChain.getLast().getPointsWhenOtherGivesUp();
        return ptsGanhos;
    }
}
