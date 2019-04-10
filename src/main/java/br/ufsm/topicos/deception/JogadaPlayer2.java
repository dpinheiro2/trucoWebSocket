package br.ufsm.topicos.deception;

import br.ufsm.topicos.cbr.TrucoDescription;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Universidade Federal de Santa Maria
 * Pós-Graduação em Ciência da Computação
 * Tópicos em Computação Aplicada
 * Daniel Pinheiro Vargas
 * Criado em 27/03/2019.
 */

@Entity
@Table(name="jogadas_player2")
public class JogadaPlayer2  implements Serializable {

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Column(name = "id")
    private Integer id;

    @Column(name = "idPartida", nullable = true)
    private String idPartida;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idMao", nullable = true)
    private TrucoDescription trucoDescription;

    //1-call; 2-noCall; 3-accept; 4-decline; 5-raise; 6-fold; 7-hidePoints; 8-playCard
    @Column(name = "indJogada", nullable = true)
    private Integer indJogada;

    //1-flor; 2-envido; 3-truco; 4-playCard; 5-faceDownCard;
    @Column(name = "typeJogada", nullable = true)
    private Integer typeJogada;

    //11-flor; 12-florFlor; 13-contraFlor; 14-contraFlorResto; 15-contraFlorFalta;
    //21-envido; 22-envidoEnvido; 23-realEnvido; 24-faltaEnvido;
    //31-truco; 32-retruco; 33-vale4; cbrCode;
    @Column(name = "detailJogada", nullable = true)
    private Integer detailJogada;


    @Column(name = "stateDecision", nullable = true)
    private String stateDecision;

    @Column(name = "indBlefe", nullable = true)
    private Integer indBlefe;

    @Column(name = "typeBlefe", nullable = true)
    private Integer typeBlefe;

    @Column(name = "indSucesso", nullable = true)
    private Integer indSucesso;

    @Column(name = "tentosGanhos", nullable = true)
    private Integer tentosGanhos;

    @Column(name = "tentosPerdidos", nullable = true)
    private Integer tentosPerdidos;

    @Column(name = "jogadorMao", nullable = true)
    private Integer jogadorMao;

    @Column(name = "cartaAlta", nullable = true)
    private Integer cartaAlta;

    @Column(name = "naipeCartaAlta", nullable = true)
    private String naipeCartaAlta;

    @Column(name = "cartaMedia", nullable = true)
    private Integer cartaMedia;

    @Column(name = "naipeCartaMedia", nullable = true)
    private String naipeCartaMedia;

    @Column(name = "cartaBaixa", nullable = true)
    private Integer cartaBaixa;

    @Column(name = "naipeCartaBaixa", nullable = true)
    private String naipeCartaBaixa;

    @Column(name = "qualidadeMao", nullable = true)
    private Integer qualidadeMao;

    @Column(name = "pontosEnvido", nullable = true)
    private Integer pontosEnvido;

    @Column(name = "pontosFlor", nullable = true)
    private Integer pontosFlor;

    @Column(name = "primeiraCartaJogada", nullable = true)
    private Integer primeiraCartaJogada;

    @Column(name = "naipePrimeiraCarta", nullable = true)
    private String naipePrimeiraCarta;

    @Column(name = "quemPrimeiraCarta", nullable = true)
    private Integer quemPrimeiraCarta;

    @Column(name = "segundaCartaJogada", nullable = true)
    private Integer segundaCartaJogada;

    @Column(name = "naipeSegundaCarta", nullable = true)
    private String naipeSegundaCarta;

    @Column(name = "quemSegundaCarta", nullable = true)
    private Integer quemSegundaCarta;

    @Column(name = "terceiraCartaJogada", nullable = true)
    private Integer terceiraCartaJogada;

    @Column(name = "naipeTerceiraCarta", nullable = true)
    private String naipeTerceiraCarta;

    @Column(name = "quemTerceiraCarta", nullable = true)
    private Integer quemTerceiraCarta;

    @Column(name = "quartaCartaJogada", nullable = true)
    private Integer quartaCartaJogada;

    @Column(name = "naipeQuartaCarta", nullable = true)
    private String naipeQuartaCarta;

    @Column(name = "quemQuartaCarta", nullable = true)
    private Integer quemQuartaCarta;

    @Column(name = "quintaCartaJogada", nullable = true)
    private Integer quintaCartaJogada;

    @Column(name = "naipeQuintaCarta", nullable = true)
    private String naipeQuintaCarta;

    @Column(name = "quemQuintaCarta", nullable = true)
    private Integer quemQuintaCarta;

    @Column(name = "sextaCartaJogada", nullable = true)
    private Integer sextaCartaJogada;

    @Column(name = "naipeSextaCarta", nullable = true)
    private String naipeSextaCarta;

    @Column(name = "quemSextaCarta", nullable = true)
    private Integer quemSextaCarta;

    @Column(name = "pontosEnvidoOponente", nullable = true)
    private Integer pontosEnvidoOponente;

    @Column(name = "pontosFlorOponente", nullable = true)
    private Integer pontosFlorOponente;

    @Column(name = "ganhadorPrimeiraRodada", nullable = true)
    private Integer ganhadorPrimeiraRodada;

    @Column(name = "ganhadorSegundaRodada", nullable = true)
    private Integer ganhadorSegundaRodada;

    @Column(name = "ganhadorTerceiraRodada", nullable = true)
    private Integer ganhadorTerceiraRodada;

    @Column(name = "tentos", nullable = true)
    private Integer tentos;

    @Column(name = "tentosOponente", nullable = true)
    private Integer tentosOponente;

    @Column(name = "indStrategy", nullable = true)
    private Integer indStrategy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getIndJogada() {
        return indJogada;
    }

    public void setIndJogada(Integer indJogada) {
        this.indJogada = indJogada;
    }

    public Integer getTypeJogada() {
        return typeJogada;
    }

    public void setTypeJogada(Integer typeJogada) {
        this.typeJogada = typeJogada;
    }

    public Integer getDetailJogada() {
        return detailJogada;
    }

    public void setDetailJogada(Integer detailJogada) {
        this.detailJogada = detailJogada;
    }

    public String getStateDecision() {
        return stateDecision;
    }

    public void setStateDecision(String stateDecision) {
        this.stateDecision = stateDecision;
    }

    public Integer getIndBlefe() {
        return indBlefe;
    }

    public void setIndBlefe(Integer indBlefe) {
        this.indBlefe = indBlefe;
    }

    public Integer getTypeBlefe() {
        return typeBlefe;
    }

    public void setTypeBlefe(Integer typeBlefe) {
        this.typeBlefe = typeBlefe;
    }

    public Integer getIndSucesso() {
        return indSucesso;
    }

    public void setIndSucesso(Integer indSucesso) {
        this.indSucesso = indSucesso;
    }

    public Integer getTentosGanhos() {
        return tentosGanhos;
    }

    public void setTentosGanhos(Integer tentosGanhos) {
        this.tentosGanhos = tentosGanhos;
    }

    public Integer getTentosPerdidos() {
        return tentosPerdidos;
    }

    public void setTentosPerdidos(Integer tentosPerdidos) {
        this.tentosPerdidos = tentosPerdidos;
    }

    public Integer getJogadorMao() {
        return jogadorMao;
    }

    public void setJogadorMao(Integer jogadorMao) {
        this.jogadorMao = jogadorMao;
    }

    public Integer getCartaAlta() {
        return cartaAlta;
    }

    public void setCartaAlta(Integer cartaAlta) {
        this.cartaAlta = cartaAlta;
    }

    public String getNaipeCartaAlta() {
        return naipeCartaAlta;
    }

    public void setNaipeCartaAlta(String naipeCartaAlta) {
        this.naipeCartaAlta = naipeCartaAlta;
    }

    public Integer getCartaMedia() {
        return cartaMedia;
    }

    public void setCartaMedia(Integer cartaMedia) {
        this.cartaMedia = cartaMedia;
    }

    public String getNaipeCartaMedia() {
        return naipeCartaMedia;
    }

    public void setNaipeCartaMedia(String naipeCartaMedia) {
        this.naipeCartaMedia = naipeCartaMedia;
    }

    public Integer getCartaBaixa() {
        return cartaBaixa;
    }

    public void setCartaBaixa(Integer cartaBaixa) {
        this.cartaBaixa = cartaBaixa;
    }

    public String getNaipeCartaBaixa() {
        return naipeCartaBaixa;
    }

    public void setNaipeCartaBaixa(String naipeCartaBaixa) {
        this.naipeCartaBaixa = naipeCartaBaixa;
    }

    public Integer getQualidadeMao() {
        return qualidadeMao;
    }

    public void setQualidadeMao(Integer qualidadeMao) {
        this.qualidadeMao = qualidadeMao;
    }

    public Integer getPontosEnvido() {
        return pontosEnvido;
    }

    public void setPontosEnvido(Integer pontosEnvido) {
        this.pontosEnvido = pontosEnvido;
    }

    public Integer getPontosFlor() {
        return pontosFlor;
    }

    public void setPontosFlor(Integer pontosFlor) {
        this.pontosFlor = pontosFlor;
    }

    public Integer getPrimeiraCartaJogada() {
        return primeiraCartaJogada;
    }

    public void setPrimeiraCartaJogada(Integer primeiraCartaJogada) {
        this.primeiraCartaJogada = primeiraCartaJogada;
    }

    public String getNaipePrimeiraCarta() {
        return naipePrimeiraCarta;
    }

    public void setNaipePrimeiraCarta(String naipePrimeiraCarta) {
        this.naipePrimeiraCarta = naipePrimeiraCarta;
    }

    public Integer getQuemPrimeiraCarta() {
        return quemPrimeiraCarta;
    }

    public void setQuemPrimeiraCarta(Integer quemPrimeiraCarta) {
        this.quemPrimeiraCarta = quemPrimeiraCarta;
    }

    public Integer getSegundaCartaJogada() {
        return segundaCartaJogada;
    }

    public void setSegundaCartaJogada(Integer segundaCartaJogada) {
        this.segundaCartaJogada = segundaCartaJogada;
    }

    public String getNaipeSegundaCarta() {
        return naipeSegundaCarta;
    }

    public void setNaipeSegundaCarta(String naipeSegundaCarta) {
        this.naipeSegundaCarta = naipeSegundaCarta;
    }

    public Integer getQuemSegundaCarta() {
        return quemSegundaCarta;
    }

    public void setQuemSegundaCarta(Integer quemSegundaCarta) {
        this.quemSegundaCarta = quemSegundaCarta;
    }

    public Integer getTerceiraCartaJogada() {
        return terceiraCartaJogada;
    }

    public void setTerceiraCartaJogada(Integer terceiraCartaJogada) {
        this.terceiraCartaJogada = terceiraCartaJogada;
    }

    public String getNaipeTerceiraCarta() {
        return naipeTerceiraCarta;
    }

    public void setNaipeTerceiraCarta(String naipeTerceiraCarta) {
        this.naipeTerceiraCarta = naipeTerceiraCarta;
    }

    public Integer getQuemTerceiraCarta() {
        return quemTerceiraCarta;
    }

    public void setQuemTerceiraCarta(Integer quemTerceiraCarta) {
        this.quemTerceiraCarta = quemTerceiraCarta;
    }

    public Integer getQuartaCartaJogada() {
        return quartaCartaJogada;
    }

    public void setQuartaCartaJogada(Integer quartaCartaJogada) {
        this.quartaCartaJogada = quartaCartaJogada;
    }

    public String getNaipeQuartaCarta() {
        return naipeQuartaCarta;
    }

    public void setNaipeQuartaCarta(String naipeQuartaCarta) {
        this.naipeQuartaCarta = naipeQuartaCarta;
    }

    public Integer getQuemQuartaCarta() {
        return quemQuartaCarta;
    }

    public void setQuemQuartaCarta(Integer quemQuartaCarta) {
        this.quemQuartaCarta = quemQuartaCarta;
    }

    public Integer getQuintaCartaJogada() {
        return quintaCartaJogada;
    }

    public void setQuintaCartaJogada(Integer quintaCartaJogada) {
        this.quintaCartaJogada = quintaCartaJogada;
    }

    public String getNaipeQuintaCarta() {
        return naipeQuintaCarta;
    }

    public void setNaipeQuintaCarta(String naipeQuintaCarta) {
        this.naipeQuintaCarta = naipeQuintaCarta;
    }

    public Integer getQuemQuintaCarta() {
        return quemQuintaCarta;
    }

    public void setQuemQuintaCarta(Integer quemQuintaCarta) {
        this.quemQuintaCarta = quemQuintaCarta;
    }

    public Integer getSextaCartaJogada() {
        return sextaCartaJogada;
    }

    public void setSextaCartaJogada(Integer sextaCartaJogada) {
        this.sextaCartaJogada = sextaCartaJogada;
    }

    public String getNaipeSextaCarta() {
        return naipeSextaCarta;
    }

    public void setNaipeSextaCarta(String naipeSextaCarta) {
        this.naipeSextaCarta = naipeSextaCarta;
    }

    public Integer getQuemSextaCarta() {
        return quemSextaCarta;
    }

    public void setQuemSextaCarta(Integer quemSextaCarta) {
        this.quemSextaCarta = quemSextaCarta;
    }

    public Integer getPontosEnvidoOponente() {
        return pontosEnvidoOponente;
    }

    public void setPontosEnvidoOponente(Integer pontosEnvidoOponente) {
        this.pontosEnvidoOponente = pontosEnvidoOponente;
    }

    public Integer getPontosFlorOponente() {
        return pontosFlorOponente;
    }

    public void setPontosFlorOponente(Integer pontosFlorOponente) {
        this.pontosFlorOponente = pontosFlorOponente;
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

    public Integer getTentos() {
        return tentos;
    }

    public void setTentos(Integer tentos) {
        this.tentos = tentos;
    }

    public Integer getTentosOponente() {
        return tentosOponente;
    }

    public void setTentosOponente(Integer tentosOponente) {
        this.tentosOponente = tentosOponente;
    }

    public Integer getIndStrategy() {
        return indStrategy;
    }

    public void setIndStrategy(Integer indStrategy) {
        this.indStrategy = indStrategy;
    }
}
