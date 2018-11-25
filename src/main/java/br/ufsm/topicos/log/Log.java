package br.ufsm.topicos.log;

import br.ufsm.topicos.cbr.TrucoDescription;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Universidade Federal de Santa Maria
 * Pós-Graduação em Ciência da Computação
 * Tópicos em Computação Aplicada
 * Daniel Pinheiro Vargas
 * Criado em 29/09/2018.
 */

@Entity
@Table(name="logs")
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Column(name = "idLog")
    private Integer idLog;

    @Column(name = "idPartida", nullable = true)
    private String idPartida;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idMao", nullable = true)
    private TrucoDescription trucoDescription;

    @Lob
    @Column(name = "log", columnDefinition="LONGTEXT")
    private String log;

    @Column(name = "player", nullable = true)
    private Integer player;

    @Column(name = "jogada", nullable = true)
    private String jogada;

    public Integer getIdLog() {
        return idLog;
    }

    public void setIdLog(Integer idLog) {
        this.idLog = idLog;
    }

    public String getIdPartida() {
        return idPartida;
    }

    public void setIdPartida(String idPartida) {
        this.idPartida = idPartida;
    }

    public TrucoDescription getTrucoDescription() {
        return trucoDescription;
    }

    public void setTrucoDescription(TrucoDescription trucoDescription) {
        this.trucoDescription = trucoDescription;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Integer getPlayer() {
        return player;
    }

    public void setPlayer(Integer player) {
        this.player = player;
    }

    public String getJogada() {
        return jogada;
    }

    public void setJogada(String jogada) {
        this.jogada = jogada;
    }
}
