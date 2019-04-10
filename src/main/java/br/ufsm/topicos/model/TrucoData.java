package br.ufsm.topicos.model;

/**
 * Universidade Federal de Santa Maria
 * Pós-Graduação em Ciência da Computação
 * Tópicos em Computação Aplicada
 * Daniel Pinheiro Vargas
 * Criado em 23/07/2018.
 */

public class TrucoData {

    public final static int PLAYER_1 = 1;
    public final static int PLAYER_2 = 2;

    public final static int ROBO = 1;
    public final static int HUMANO = 2;

    public final static int FALSE = 0;
    public final static int TRUE = 1;

    public final static int DECEPTION_ENVIDO_MAO_SEM_PONTO = 1;
    public final static int DECEPTION_ENVIDO_MAO_MUITO_PONTO = 2;
    public final static int DECEPTION_ENVIDO_PE_SEM_PONTO = 3;
    public final static int DECEPTION_ENVIDO_PE_E_BOM = 4;
    public final static int DECEPTION_ENVIDO_MAO_JOGA_SETE = 5;

    public final static int DECEPTION_TRUCO_ROUND_1_MAO_RUIM_CARTA_ALTA = 11;
    public final static int DECEPTION_TRUCO_ROUND_1_MAO_BOA_CARTA_BAIXA = 12;
    public final static int DECEPTION_TRUCO_ROUND_1_MAO_BOA_CARTA_NAIPE_DIFERENTE = 13;
    public final static int DECEPTION_TRUCO_ROUND_1_MAO_RUIM_CARTA_NAIPE_DIFERENTE = 14;
    public final static int DECEPTION_TRUCO_ROUND_1_CANTA_TRUCO = 15;

    public final static int DECEPTION_TRUCO_ROUND_2_MAO_BOA_FEZ_PRIMEIRA_CARTA_VIRADA = 21;
    public final static int DECEPTION_TRUCO_ROUND_2_MAO_RUIM_FEZ_PRIMEIRA_CARTA_VIRADA = 22;
    public final static int DECEPTION_TRUCO_ROUND_2_MAO_RUIM_CANTA_TRUCO = 23;
    public final static int DECEPTION_TRUCO_ROUND_2_PERDEU_NAO_MATA_SEGUNDA_CANTA_TRUCO = 24;

    public final static int DECEPTION_TRUCO_ROUND_3_CARTA_RUIM_CANTA_TRUCO = 31;
    public final static int DECEPTION_TRUCO_ROUND_3_CARTA_NAO_MATA_TERCEIRA_CANTA_TRUCO = 32;

    /**
     Types of Deception

     ENVIDO
     #1 - Jogador mão canta ENVIDO/REAL/FALTA sem ter muitos pontos;
     #2 - Jogador mão deixa de cantar ENVIDO/REAL/FALTA mesmo com bastantes pontos;
     #3 - Jogador pé, sem ter muitos pontos, canta ENVIDO/REAL/FALTA porque o oponente não cantou;
     #4 - Jogador pé, não canta os pontos para não entregar suas cartas;
     #5 – Jogador mão, com poucos pontos, não canta envido e larga um 6 ou 7 para o adversário não cantar envido;
     #6 - Jogador pé, sem ter muitos pontos, aumenta ENVIDO/REAL para oponente fugir;

     TRUCO - Rodada 1
     #11 - Jogador com mão boa larga a carta mais baixa;
     #12 - Jogador com mão ruim larga a carta mais alta;
     #13 - Caso jogador com uma mão boa tenha cantado seus pontos de envido, joga carta para despitar as cartas que compoem os pontos;
     #14 - Caso jogador com uma mão ruim tenha cantado seus pontos de envido, joga carta para despitar as cartas que compoem os pontos;
     #15 - Jogador com mão ruim chama TRUCO antes de mostrar as cartas;
     #16 - Jogador com uma mão boa deixa de chamar TRUCO;


     TRUCO - Rodada 2
     #21 - Jogador ganhou primeira e tem uma mão boa, joga a segunda carta virada;
     #---------22 - Jogador ganhou primeira e tem uma mão ruim, joga a segunda carta virada;
     #23 - Tem uma mão ruim, chama TRUCO antes de largar a segunda carta;
     #24 - Jogador perdeu primeira, é o pé e não tem carta para fazer a segunda, chama TRUCO antes de largar a segunda carta;

     TRUCO - Rodada 3
     #31 - Jogador possui terceira carta baixa, chama TRUCO/RETRUCO/VALE4 antes de largar a carta;
     #32 - Jogador é o pé e não tem carta para fazer a terceira, chama TRUCO antes de largar a terceira carta;


     TODO: deixa que cantar truco para poder aumentar a aposta
     */

}
