package br.ufsm.topicos.model;

import br.ufsm.topicos.enuns.EnvidoLevel;
import br.ufsm.topicos.exceptions.ExceptionEnvido;

import java.util.LinkedList;

/**
 * Universidade Federal de Santa Maria
 * Pós-Graduação em Ciência da Computação
 * Tópicos em Computação Aplicada
 * Daniel Pinheiro Vargas
 * Criado em 23/07/2018.
 */

public class Envido {

    private LinkedList<EnvidoLevel> envidoChain = new LinkedList<EnvidoLevel>();

    public Envido() {
    }

    public LinkedList<EnvidoLevel> getEnvidoChain() {
        return envidoChain;
    }

    public void resetEnvidoChain() {
        envidoChain.clear();
    }

    public void agregarEnvido(EnvidoLevel envidoLevel) throws ExceptionEnvido {
        if (envidoChain.size() == 4)
            throw new ExceptionEnvido("Não se pode mais cantar envido.");

        if (envidoChain.isEmpty()) {
            if (envidoLevel == EnvidoLevel.ENVIDO_ENVIDO)
                throw new ExceptionEnvido("Só pode cantar envido envido se envido tiver tiver sido cantado.");
        } else {
            if (envidoChain.getLast().ordinal() > envidoLevel.ordinal() )
                throw new ExceptionEnvido("Envido tem uma ordem : envido, envido envido, real envido, falta envido.");
        }
        if (envidoChain.contains(envidoLevel))
            throw new ExceptionEnvido(envidoLevel.toString() + " já foi cantado.");

        envidoChain.add(envidoLevel);
    }

    public int getPontosGanhosAceitos(int ptsOther) {
        int ptsGanhos = 0;
        if (envidoChain.getLast().ordinal() == 3) {
            ptsGanhos = 24 - ptsOther;
        } else {
            for(EnvidoLevel envidoLevel : envidoChain) {
                if (envidoLevel.ordinal() < 3) {
                    ptsGanhos += envidoLevel.getPointsWorth();
                }
            }
        }

        return ptsGanhos;
    }

    public int getPontosGanhosNaoAceitos() {
        int ptsGanhos = 0;
        if (envidoChain.size() == 1) {
            return 1;
        }
        for(EnvidoLevel envidoLevel : envidoChain) {
            if (envidoChain.getLast() != envidoLevel) {
                ptsGanhos += envidoLevel.getPointsWorth();
            }
        }
        return ptsGanhos;
    }
}
