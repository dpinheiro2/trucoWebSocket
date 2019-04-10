package br.ufsm.topicos.cbr;




import br.ufsm.topicos.deception.JogadaPlayer1;
import br.ufsm.topicos.deception.JogadaPlayer2;
import br.ufsm.topicos.log.Log;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Universidade Federal de Santa Maria
 * Pós-Graduação em Ciência da Computação
 * Tópicos em Computação Aplicada
 * Daniel Pinheiro Vargas
 * Criado em 07/08/2018.
 */

@Entity
@Table(name="maos")
public class TrucoDescription implements Serializable {

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Column(name = "idMao")
    private Integer idMao;

    @Column(name = "idPartida", nullable = true)
    private String idPartida;

    @Column(name = "jogadorMao", nullable = true)
    private Integer jogadorMao;

    @Column(name = "cartaAltaRobo", nullable = true)
    private Integer cartaAltaRobo;

    @Column(name = "cartaMediaRobo", nullable = true)
    private Integer cartaMediaRobo;

    @Column(name = "cartaBaixaRobo", nullable = true)
    private Integer cartaBaixaRobo;

    @Column(name = "cartaAltaHumano", nullable = true)
    private Integer cartaAltaHumano;

    @Column(name = "cartaMediaHumano", nullable = true)
    private Integer cartaMediaHumano;

    @Column(name = "cartaBaixaHumano", nullable = true)
    private Integer cartaBaixaHumano;

    @Column(name = "primeiraCartaRobo", nullable = true)
    private Integer primeiraCartaRobo;

    @Column(name = "primeiraCartaHumano", nullable = true)
    private Integer primeiraCartaHumano;

    @Column(name = "segundaCartaRobo", nullable = true)
    private Integer segundaCartaRobo;

    @Column(name = "segundaCartaHumano", nullable = true)
    private Integer segundaCartaHumano;

    @Column(name = "terceiraCartaRobo", nullable = true)
    private Integer terceiraCartaRobo;

    @Column(name = "terceiraCartaHumano", nullable = true)
    private Integer terceiraCartaHumano;

    @Column(name = "ganhadorPrimeiraRodada", nullable = true)
    private Integer ganhadorPrimeiraRodada;

    @Column(name = "ganhadorSegundaRodada", nullable = true)
    private Integer ganhadorSegundaRodada;

    @Column(name = "ganhadorTerceiraRodada", nullable = true)
    private Integer ganhadorTerceiraRodada;

    @Column(name = "roboCartaVirada", nullable = true)
    private Integer roboCartaVirada;

    @Column(name = "humanoCartaVirada", nullable = true)
    private Integer humanoCartaVirada;

    @Column(name = "quemPediuEnvido", nullable = true)
    private Integer quemPediuEnvido;

    @Column(name = "quemPediuFaltaEnvido", nullable = true)
    private Integer quemPediuFaltaEnvido;

    @Column(name = "quemPediuRealEnvido", nullable = true)
    private Integer quemPediuRealEnvido;

    @Column(name = "pontosEnvidoRobo", nullable = true)
    private Integer pontosEnvidoRobo;

    @Column(name = "pontosEnvidoHumano", nullable = true)
    private Integer pontosEnvidoHumano;

    @Column(name = "quemNegouEnvido", nullable = true)
    private Integer quemNegouEnvido;

    @Column(name = "quemGanhouEnvido", nullable = true)
    private Integer quemGanhouEnvido;

    @Column(name = "tentosEnvido", nullable = true)
    private Integer tentosEnvido;

    @Column(name = "quemFlor", nullable = true)
    private Integer quemFlor;

    @Column(name = "quemContraFlor", nullable = true)
    private Integer quemContraFlor;

    @Column(name = "quemContraFlorResto", nullable = true)
    private Integer quemContraFlorResto;

    @Column(name = "quemNegouFlor", nullable = true)
    private Integer quemNegouFlor;

    @Column(name = "pontosFlorRobo", nullable = true)
    private Integer pontosFlorRobo;

    @Column(name = "pontosFlorHumano", nullable = true)
    private Integer pontosFlorHumano;

    @Column(name = "quemGanhouFlor", nullable = true)
    private Integer quemGanhouFlor;

    @Column(name = "tentosFlor", nullable = true)
    private Integer tentosFlor;

    @Column(name = "quemEscondeuPontosEnvido", nullable = true)
    private Integer quemEscondeuPontosEnvido;

    @Column(name = "quemEscondeuPontosFlor", nullable = true)
    private Integer quemEscondeuPontosFlor;

    @Column(name = "quemTruco", nullable = true)
    private Integer quemTruco;

    @Column(name = "quandoTruco", nullable = true)
    private Integer quandoTruco;

    @Column(name = "quemRetruco", nullable = true)
    private Integer quemRetruco;

    @Column(name = "quandoRetruco", nullable = true)
    private Integer quandoRetruco;

    @Column(name = "quemValeQuatro", nullable = true)
    private Integer quemValeQuatro;

    @Column(name = "quandoValeQuatro", nullable = true)
    private Integer quandoValeQuatro;

    @Column(name = "quemNegouTruco", nullable = true)
    private Integer quemNegouTruco;

    @Column(name = "quemGanhouTruco", nullable = true)
    private Integer quemGanhouTruco;

    @Column(name = "tentosTruco", nullable = true)
    private Integer tentosTruco;

    @Column(name = "tentosAnterioresRobo", nullable = true)
    private Integer tentosAnterioresRobo;

    @Column(name = "tentosAnterioresHumano", nullable = true)
    private Integer tentosAnterioresHumano;

    @Column(name = "tentosPosterioresRobo", nullable = true)
    private Integer tentosPosterioresRobo;

    @Column(name = "tentosPosterioresHumano", nullable = true)
    private Integer tentosPosterioresHumano;

    @Column(name = "roboMentiuEnvido", nullable = true)
    private Integer roboMentiuEnvido;

    @Column(name = "humanoMentiuEnvido", nullable = true)
    private Integer humanoMentiuEnvido;

    @Column(name = "roboMentiuFlor", nullable = true)
    private Integer roboMentiuFlor;

    @Column(name = "humanoMentiuFlor", nullable = true)
    private Integer humanoMentiuFlor;

    @Column(name = "roboMentiuTruco", nullable = true)
    private Integer roboMentiuTruco;

    @Column(name = "humanoMentiuTruco", nullable = true)
    private Integer humanoMentiuTruco;

    @Column(name = "quemBaralho", nullable = true)
    private Integer quemBaralho;

    @Column(name = "quandoBaralho", nullable = true)
    private Integer quandoBaralho;

    @Column(name = "quemContraFlorFalta", nullable = true)
    private Integer quemContraFlorFalta;

    @Column(name = "quemEnvidoEnvido", nullable = true)
    private Integer quemEnvidoEnvido;

    @Column(name = "quemFlorFlor", nullable = true)
    private Integer quemFlorFlor;

    @Column(name = "quandoCartaVirada", nullable = true)
    private Integer quandoCartaVirada;

    @Column(name = "naipeCartaAltaRobo", nullable = true)
    private String naipeCartaAltaRobo;

    @Column(name = "naipeCartaMediaRobo", nullable = true)
    private String naipeCartaMediaRobo;

    @Column(name = "naipeCartaBaixaRobo", nullable = true)
    private String naipeCartaBaixaRobo;

    @Column(name = "naipeCartaAltaHumano", nullable = true)
    private String naipeCartaAltaHumano;

    @Column(name = "naipeCartaMediaHumano", nullable = true)
    private String naipeCartaMediaHumano;

    @Column(name = "naipeCartaBaixaHumano", nullable = true)
    private String naipeCartaBaixaHumano;

    @Column(name = "naipePrimeiraCartaRobo", nullable = true)
    private String naipePrimeiraCartaRobo;

    @Column(name = "naipePrimeiraCartaHumano", nullable = true)
    private String naipePrimeiraCartaHumano;

    @Column(name = "naipeSegundaCartaRobo", nullable = true)
    private String naipeSegundaCartaRobo;

    @Column(name = "naipeSegundaCartaHumano", nullable = true)
    private String naipeSegundaCartaHumano;

    @Column(name = "naipeTerceiraCartaRobo", nullable = true)
    private String naipeTerceiraCartaRobo;

    @Column(name = "naipeTerceiraCartaHumano", nullable = true)
    private String naipeTerceiraCartaHumano;

    @Column(name = "hasDeception", nullable = true)
    private Integer hasDeception;

    @Column(name = "roboMentiuRound1", nullable = true)
    private Integer roboMentiuRound1;

    @Column(name = "humanoMentiuRound1", nullable = true)
    private Integer humanoMentiuRound1;

    @Column(name = "roboMentiuRound2", nullable = true)
    private Integer roboMentiuRound2;

    @Column(name = "humanoMentiuRound2", nullable = true)
    private Integer humanoMentiuRound2;

    @Column(name = "roboMentiuRound3", nullable = true)
    private Integer roboMentiuRound3;

    @Column(name = "humanoMentiuRound3", nullable = true)
    private Integer humanoMentiuRound3;

    @OneToMany(mappedBy="trucoDescription", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    private Collection<Log> logs = new LinkedList<Log>();

    @OneToMany(mappedBy="trucoDescription", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    private Collection<JogadaPlayer1> jogadasPlayer1 = new LinkedList<JogadaPlayer1>();

    @OneToMany(mappedBy="trucoDescription", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    private Collection<JogadaPlayer2> jogadasPlayer2 = new LinkedList<JogadaPlayer2>();

    public Integer getIdMao() {
        return idMao;
    }

    public void setIdMao(Integer idMao) {
        this.idMao = idMao;
    }

    public String getIdPartida() {
        return idPartida;
    }

    public void setIdPartida(String idPartida) {
        this.idPartida = idPartida;
    }

    public Integer getJogadorMao() {
        return jogadorMao;
    }

    public void setJogadorMao(Integer jogadorMao) {
        this.jogadorMao = jogadorMao;
    }

    public Integer getCartaAltaRobo() {
        return cartaAltaRobo;
    }

    public void setCartaAltaRobo(Integer cartaAltaRobo) {
        this.cartaAltaRobo = cartaAltaRobo;
    }

    public Integer getCartaMediaRobo() {
        return cartaMediaRobo;
    }

    public void setCartaMediaRobo(Integer cartaMediaRobo) {
        this.cartaMediaRobo = cartaMediaRobo;
    }

    public Integer getCartaBaixaRobo() {
        return cartaBaixaRobo;
    }

    public void setCartaBaixaRobo(Integer cartaBaixaRobo) {
        this.cartaBaixaRobo = cartaBaixaRobo;
    }

    public Integer getCartaAltaHumano() {
        return cartaAltaHumano;
    }

    public void setCartaAltaHumano(Integer cartaAltaHumano) {
        this.cartaAltaHumano = cartaAltaHumano;
    }

    public Integer getCartaMediaHumano() {
        return cartaMediaHumano;
    }

    public void setCartaMediaHumano(Integer cartaMediaHumano) {
        this.cartaMediaHumano = cartaMediaHumano;
    }

    public Integer getCartaBaixaHumano() {
        return cartaBaixaHumano;
    }

    public void setCartaBaixaHumano(Integer cartaBaixaHumano) {
        this.cartaBaixaHumano = cartaBaixaHumano;
    }

    public Integer getPrimeiraCartaRobo() {
        return primeiraCartaRobo;
    }

    public void setPrimeiraCartaRobo(Integer primeiraCartaRobo) {
        this.primeiraCartaRobo = primeiraCartaRobo;
    }

    public Integer getPrimeiraCartaHumano() {
        return primeiraCartaHumano;
    }

    public void setPrimeiraCartaHumano(Integer primeiraCartaHumano) {
        this.primeiraCartaHumano = primeiraCartaHumano;
    }

    public Integer getSegundaCartaRobo() {
        return segundaCartaRobo;
    }

    public void setSegundaCartaRobo(Integer segundaCartaRobo) {
        this.segundaCartaRobo = segundaCartaRobo;
    }

    public Integer getSegundaCartaHumano() {
        return segundaCartaHumano;
    }

    public void setSegundaCartaHumano(Integer segundaCartaHumano) {
        this.segundaCartaHumano = segundaCartaHumano;
    }

    public Integer getTerceiraCartaRobo() {
        return terceiraCartaRobo;
    }

    public void setTerceiraCartaRobo(Integer terceiraCartaRobo) {
        this.terceiraCartaRobo = terceiraCartaRobo;
    }

    public Integer getTerceiraCartaHumano() {
        return terceiraCartaHumano;
    }

    public void setTerceiraCartaHumano(Integer terceiraCartaHumano) {
        this.terceiraCartaHumano = terceiraCartaHumano;
    }

    public Integer getGanhadorPrimeiraRodada() {
        return ganhadorPrimeiraRodada;
    }

    public void setGanhadorPrimeiraRodada(Integer ganhadorPrimeiraRodada) {
        this.ganhadorPrimeiraRodada = ganhadorPrimeiraRodada;
    }

    public Integer getGanhadorSegundaRodada() {
        return ganhadorSegundaRodada;
    }

    public void setGanhadorSegundaRodada(Integer ganhadorSegundaRodada) {
        this.ganhadorSegundaRodada = ganhadorSegundaRodada;
    }

    public Integer getGanhadorTerceiraRodada() {
        return ganhadorTerceiraRodada;
    }

    public void setGanhadorTerceiraRodada(Integer ganhadorTerceiraRodada) {
        this.ganhadorTerceiraRodada = ganhadorTerceiraRodada;
    }

    public Integer getRoboCartaVirada() {
        return roboCartaVirada;
    }

    public void setRoboCartaVirada(Integer roboCartaVirada) {
        this.roboCartaVirada = roboCartaVirada;
    }

    public Integer getHumanoCartaVirada() {
        return humanoCartaVirada;
    }

    public void setHumanoCartaVirada(Integer humanoCartaVirada) {
        this.humanoCartaVirada = humanoCartaVirada;
    }

    public Integer getQuemPediuEnvido() {
        return quemPediuEnvido;
    }

    public void setQuemPediuEnvido(Integer quemPediuEnvido) {
        this.quemPediuEnvido = quemPediuEnvido;
    }

    public Integer getQuemPediuFaltaEnvido() {
        return quemPediuFaltaEnvido;
    }

    public void setQuemPediuFaltaEnvido(Integer quemPediuFaltaEnvido) {
        this.quemPediuFaltaEnvido = quemPediuFaltaEnvido;
    }

    public Integer getQuemPediuRealEnvido() {
        return quemPediuRealEnvido;
    }

    public void setQuemPediuRealEnvido(Integer quemPediuRealEnvido) {
        this.quemPediuRealEnvido = quemPediuRealEnvido;
    }

    public Integer getPontosEnvidoRobo() {
        return pontosEnvidoRobo;
    }

    public void setPontosEnvidoRobo(Integer pontosEnvidoRobo) {
        this.pontosEnvidoRobo = pontosEnvidoRobo;
    }

    public Integer getPontosEnvidoHumano() {
        return pontosEnvidoHumano;
    }

    public void setPontosEnvidoHumano(Integer pontosEnvidoHumano) {
        this.pontosEnvidoHumano = pontosEnvidoHumano;
    }

    public Integer getQuemNegouEnvido() {
        return quemNegouEnvido;
    }

    public void setQuemNegouEnvido(Integer quemNegouEnvido) {
        this.quemNegouEnvido = quemNegouEnvido;
    }

    public Integer getQuemGanhouEnvido() {
        return quemGanhouEnvido;
    }

    public void setQuemGanhouEnvido(Integer quemGanhouEnvido) {
        this.quemGanhouEnvido = quemGanhouEnvido;
    }

    public Integer getTentosEnvido() {
        return tentosEnvido;
    }

    public void setTentosEnvido(Integer tentosEnvido) {
        this.tentosEnvido = tentosEnvido;
    }

    public Integer getQuemFlor() {
        return quemFlor;
    }

    public void setQuemFlor(Integer quemFlor) {
        this.quemFlor = quemFlor;
    }

    public Integer getQuemContraFlor() {
        return quemContraFlor;
    }

    public void setQuemContraFlor(Integer quemContraFlor) {
        this.quemContraFlor = quemContraFlor;
    }

    public Integer getQuemContraFlorResto() {
        return quemContraFlorResto;
    }

    public void setQuemContraFlorResto(Integer quemContraFlorResto) {
        this.quemContraFlorResto = quemContraFlorResto;
    }

    public Integer getQuemNegouFlor() {
        return quemNegouFlor;
    }

    public void setQuemNegouFlor(Integer quemNegouFlor) {
        this.quemNegouFlor = quemNegouFlor;
    }

    public Integer getPontosFlorRobo() {
        return pontosFlorRobo;
    }

    public void setPontosFlorRobo(Integer pontosFlorRobo) {
        this.pontosFlorRobo = pontosFlorRobo;
    }

    public Integer getPontosFlorHumano() {
        return pontosFlorHumano;
    }

    public void setPontosFlorHumano(Integer pontosFlorHumano) {
        this.pontosFlorHumano = pontosFlorHumano;
    }

    public Integer getQuemGanhouFlor() {
        return quemGanhouFlor;
    }

    public void setQuemGanhouFlor(Integer quemGanhouFlor) {
        this.quemGanhouFlor = quemGanhouFlor;
    }

    public Integer getTentosFlor() {
        return tentosFlor;
    }

    public void setTentosFlor(Integer tentosFlor) {
        this.tentosFlor = tentosFlor;
    }

    public Integer getQuemEscondeuPontosEnvido() {
        return quemEscondeuPontosEnvido;
    }

    public void setQuemEscondeuPontosEnvido(Integer quemEscondeuPontosEnvido) {
        this.quemEscondeuPontosEnvido = quemEscondeuPontosEnvido;
    }

    public Integer getQuemEscondeuPontosFlor() {
        return quemEscondeuPontosFlor;
    }

    public void setQuemEscondeuPontosFlor(Integer quemEscondeuPontosFlor) {
        this.quemEscondeuPontosFlor = quemEscondeuPontosFlor;
    }

    public Integer getQuemTruco() {
        return quemTruco;
    }

    public void setQuemTruco(Integer quemTruco) {
        this.quemTruco = quemTruco;
    }

    public Integer getQuandoTruco() {
        return quandoTruco;
    }

    public void setQuandoTruco(Integer quandoTruco) {
        this.quandoTruco = quandoTruco;
    }

    public Integer getQuemRetruco() {
        return quemRetruco;
    }

    public void setQuemRetruco(Integer quemRetruco) {
        this.quemRetruco = quemRetruco;
    }

    public Integer getQuandoRetruco() {
        return quandoRetruco;
    }

    public void setQuandoRetruco(Integer quandoRetruco) {
        this.quandoRetruco = quandoRetruco;
    }

    public Integer getQuemValeQuatro() {
        return quemValeQuatro;
    }

    public void setQuemValeQuatro(Integer quemValeQuatro) {
        this.quemValeQuatro = quemValeQuatro;
    }

    public Integer getQuandoValeQuatro() {
        return quandoValeQuatro;
    }

    public void setQuandoValeQuatro(Integer quandoValeQuatro) {
        this.quandoValeQuatro = quandoValeQuatro;
    }

    public Integer getQuemNegouTruco() {
        return quemNegouTruco;
    }

    public void setQuemNegouTruco(Integer quemNegouTruco) {
        this.quemNegouTruco = quemNegouTruco;
    }

    public Integer getQuemGanhouTruco() {
        return quemGanhouTruco;
    }

    public void setQuemGanhouTruco(Integer quemGanhouTruco) {
        this.quemGanhouTruco = quemGanhouTruco;
    }

    public Integer getTentosTruco() {
        return tentosTruco;
    }

    public void setTentosTruco(Integer tentosTruco) {
        this.tentosTruco = tentosTruco;
    }

    public Integer getTentosAnterioresRobo() {
        return tentosAnterioresRobo;
    }

    public void setTentosAnterioresRobo(Integer tentosAnterioresRobo) {
        this.tentosAnterioresRobo = tentosAnterioresRobo;
    }

    public Integer getTentosAnterioresHumano() {
        return tentosAnterioresHumano;
    }

    public void setTentosAnterioresHumano(Integer tentosAnterioresHumano) {
        this.tentosAnterioresHumano = tentosAnterioresHumano;
    }

    public Integer getTentosPosterioresRobo() {
        return tentosPosterioresRobo;
    }

    public void setTentosPosterioresRobo(Integer tentosPosterioresRobo) {
        this.tentosPosterioresRobo = tentosPosterioresRobo;
    }

    public Integer getTentosPosterioresHumano() {
        return tentosPosterioresHumano;
    }

    public void setTentosPosterioresHumano(Integer tentosPosterioresHumano) {
        this.tentosPosterioresHumano = tentosPosterioresHumano;
    }

    public Integer getRoboMentiuEnvido() {
        return roboMentiuEnvido;
    }

    public void setRoboMentiuEnvido(Integer roboMentiuEnvido) {
        this.roboMentiuEnvido = roboMentiuEnvido;
    }

    public Integer getHumanoMentiuEnvido() {
        return humanoMentiuEnvido;
    }

    public void setHumanoMentiuEnvido(Integer humanoMentiuEnvido) {
        this.humanoMentiuEnvido = humanoMentiuEnvido;
    }

    public Integer getRoboMentiuFlor() {
        return roboMentiuFlor;
    }

    public void setRoboMentiuFlor(Integer roboMentiuFlor) {
        this.roboMentiuFlor = roboMentiuFlor;
    }

    public Integer getHumanoMentiuFlor() {
        return humanoMentiuFlor;
    }

    public void setHumanoMentiuFlor(Integer humanoMentiuFlor) {
        this.humanoMentiuFlor = humanoMentiuFlor;
    }

    public Integer getRoboMentiuTruco() {
        return roboMentiuTruco;
    }

    public void setRoboMentiuTruco(Integer roboMentiuTruco) {
        this.roboMentiuTruco = roboMentiuTruco;
    }

    public Integer getHumanoMentiuTruco() {
        return humanoMentiuTruco;
    }

    public void setHumanoMentiuTruco(Integer humanoMentiuTruco) {
        this.humanoMentiuTruco = humanoMentiuTruco;
    }

    public Integer getQuemBaralho() {
        return quemBaralho;
    }

    public void setQuemBaralho(Integer quemBaralho) {
        this.quemBaralho = quemBaralho;
    }

    public Integer getQuandoBaralho() {
        return quandoBaralho;
    }

    public void setQuandoBaralho(Integer quandoBaralho) {
        this.quandoBaralho = quandoBaralho;
    }

    public Integer getQuemContraFlorFalta() {
        return quemContraFlorFalta;
    }

    public void setQuemContraFlorFalta(Integer quemContraFlorFalta) {
        this.quemContraFlorFalta = quemContraFlorFalta;
    }

    public Integer getQuemEnvidoEnvido() {
        return quemEnvidoEnvido;
    }

    public void setQuemEnvidoEnvido(Integer quemEnvidoEnvido) {
        this.quemEnvidoEnvido = quemEnvidoEnvido;
    }

    public Integer getQuemFlorFlor() {
        return quemFlorFlor;
    }

    public void setQuemFlorFlor(Integer quemFlorFlor) {
        this.quemFlorFlor = quemFlorFlor;
    }

    public Integer getQuandoCartaVirada() {
        return quandoCartaVirada;
    }

    public void setQuandoCartaVirada(Integer quandoCartaVirada) {
        this.quandoCartaVirada = quandoCartaVirada;
    }

    public Collection<Log> getLogs() {
        return logs;
    }

    public void setLogs(Collection<Log> logs) {
        this.logs = logs;
    }

    public String getNaipeCartaAltaRobo() {
        return naipeCartaAltaRobo;
    }

    public void setNaipeCartaAltaRobo(String naipeCartaAltaRobo) {
        this.naipeCartaAltaRobo = naipeCartaAltaRobo;
    }

    public String getNaipeCartaMediaRobo() {
        return naipeCartaMediaRobo;
    }

    public void setNaipeCartaMediaRobo(String naipeCartaMediaRobo) {
        this.naipeCartaMediaRobo = naipeCartaMediaRobo;
    }

    public String getNaipeCartaBaixaRobo() {
        return naipeCartaBaixaRobo;
    }

    public void setNaipeCartaBaixaRobo(String naipeCartaBaixaRobo) {
        this.naipeCartaBaixaRobo = naipeCartaBaixaRobo;
    }

    public String getNaipeCartaAltaHumano() {
        return naipeCartaAltaHumano;
    }

    public void setNaipeCartaAltaHumano(String naipeCartaAltaHumano) {
        this.naipeCartaAltaHumano = naipeCartaAltaHumano;
    }

    public String getNaipeCartaMediaHumano() {
        return naipeCartaMediaHumano;
    }

    public void setNaipeCartaMediaHumano(String naipeCartaMediaHumano) {
        this.naipeCartaMediaHumano = naipeCartaMediaHumano;
    }

    public String getNaipeCartaBaixaHumano() {
        return naipeCartaBaixaHumano;
    }

    public void setNaipeCartaBaixaHumano(String naipeCartaBaixaHumano) {
        this.naipeCartaBaixaHumano = naipeCartaBaixaHumano;
    }

    public String getNaipePrimeiraCartaRobo() {
        return naipePrimeiraCartaRobo;
    }

    public void setNaipePrimeiraCartaRobo(String naipePrimeiraCartaRobo) {
        this.naipePrimeiraCartaRobo = naipePrimeiraCartaRobo;
    }

    public String getNaipePrimeiraCartaHumano() {
        return naipePrimeiraCartaHumano;
    }

    public void setNaipePrimeiraCartaHumano(String naipePrimeiraCartaHumano) {
        this.naipePrimeiraCartaHumano = naipePrimeiraCartaHumano;
    }

    public String getNaipeSegundaCartaRobo() {
        return naipeSegundaCartaRobo;
    }

    public void setNaipeSegundaCartaRobo(String naipeSegundaCartaRobo) {
        this.naipeSegundaCartaRobo = naipeSegundaCartaRobo;
    }

    public String getNaipeSegundaCartaHumano() {
        return naipeSegundaCartaHumano;
    }

    public void setNaipeSegundaCartaHumano(String naipeSegundaCartaHumano) {
        this.naipeSegundaCartaHumano = naipeSegundaCartaHumano;
    }

    public String getNaipeTerceiraCartaRobo() {
        return naipeTerceiraCartaRobo;
    }

    public void setNaipeTerceiraCartaRobo(String naipeTerceiraCartaRobo) {
        this.naipeTerceiraCartaRobo = naipeTerceiraCartaRobo;
    }

    public String getNaipeTerceiraCartaHumano() {
        return naipeTerceiraCartaHumano;
    }

    public void setNaipeTerceiraCartaHumano(String naipeTerceiraCartaHumano) {
        this.naipeTerceiraCartaHumano = naipeTerceiraCartaHumano;
    }

    public Integer getHasDeception() {
        return hasDeception;
    }

    public void setHasDeception(Integer hasDeception) {
        this.hasDeception = hasDeception;
    }

    public Integer getRoboMentiuRound1() {
        return roboMentiuRound1;
    }

    public void setRoboMentiuRound1(Integer roboMentiuRound1) {
        this.roboMentiuRound1 = roboMentiuRound1;
    }

    public Integer getHumanoMentiuRound1() {
        return humanoMentiuRound1;
    }

    public void setHumanoMentiuRound1(Integer humanoMentiuRound1) {
        this.humanoMentiuRound1 = humanoMentiuRound1;
    }

    public Integer getRoboMentiuRound2() {
        return roboMentiuRound2;
    }

    public void setRoboMentiuRound2(Integer roboMentiuRound2) {
        this.roboMentiuRound2 = roboMentiuRound2;
    }

    public Integer getHumanoMentiuRound2() {
        return humanoMentiuRound2;
    }

    public void setHumanoMentiuRound2(Integer humanoMentiuRound2) {
        this.humanoMentiuRound2 = humanoMentiuRound2;
    }

    public Integer getRoboMentiuRound3() {
        return roboMentiuRound3;
    }

    public void setRoboMentiuRound3(Integer roboMentiuRound3) {
        this.roboMentiuRound3 = roboMentiuRound3;
    }

    public Integer getHumanoMentiuRound3() {
        return humanoMentiuRound3;
    }

    public void setHumanoMentiuRound3(Integer humanoMentiuRound3) {
        this.humanoMentiuRound3 = humanoMentiuRound3;
    }

    public Collection<JogadaPlayer1> getJogadasPlayer1() {
        return jogadasPlayer1;
    }

    public void setJogadasPlayer1(Collection<JogadaPlayer1> jogadasPlayer1) {
        this.jogadasPlayer1 = jogadasPlayer1;
    }

    public Collection<JogadaPlayer2> getJogadasPlayer2() {
        return jogadasPlayer2;
    }

    public void setJogadasPlayer2(Collection<JogadaPlayer2> jogadasPlayer2) {
        this.jogadasPlayer2 = jogadasPlayer2;
    }
}
