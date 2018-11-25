package br.ufsm.topicos.model;

import br.ufsm.topicos.enuns.FlorLevel;
import br.ufsm.topicos.exceptions.ExceptionFlor;

import java.util.LinkedList;

/**
 * Universidade Federal de Santa Maria
 * Pós-Graduação em Ciência da Computação
 * Tópicos em Computação Aplicada
 * Daniel Pinheiro Vargas
 * Criado em 29/07/2018.
 */

public class Flor {

    private LinkedList<FlorLevel> florChain = new LinkedList<FlorLevel>();

    public Flor() {
    }

    public LinkedList<FlorLevel> getFlorChain() {
        return florChain;
    }

    public void resetFlorChain() {
        florChain.clear();
    }

    public void agregarFlor(FlorLevel florLevel) throws ExceptionFlor {
        if (florChain.size() == 5)
            throw new ExceptionFlor("Não se pode mais cantar flor.");

        if (florChain.isEmpty()) {
            if (florLevel == FlorLevel.FLOR_FLOR)
                throw new ExceptionFlor("Só pode cantar flor flor se flor tiver tiver sido cantada.");
        } else {
            if (florChain.getLast().ordinal() > florLevel.ordinal())
                throw new ExceptionFlor("Flor tem uma ordem : Flor --> Flor Flor --> Contra-Flor --> Contra-Flor Resto ou Contra-Flor Falta.");
        }
        if (florChain.contains(florLevel))
            throw new ExceptionFlor(florLevel.toString() + " já foi cantada.");

        florChain.add(florLevel);
    }

    public int getPontosGanhosAceitos(int ptsOther) {
        int ptsGanhos = 0;
        for(FlorLevel florLevel : florChain) {
            if (florLevel.ordinal() == 3) {
                ptsGanhos = (florLevel.getPointsWorth() - ptsOther);
            } else {
                ptsGanhos = florLevel.getPointsWorth();
            }
        }
        return ptsGanhos;
    }

    public int getPontosGanhosNaoAceitos() {
        return florChain.getLast().getPointsWhenOtherGivesUp();
    }
}
