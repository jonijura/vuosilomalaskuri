/**
 * 
 */
package tyosuhdetiedot;

import java.math.BigDecimal;
import java.time.LocalDate;

import lomalaki.TyoMerkinnanTyyppi;

/**
 * @author Joona1
 * @version 26.7.2022
 *
 */
/**
 * @author Joona1
 * @version 26.7.2022
 *
 */
public class TyoPaivaMerkinta {

    /**
     * 
     */
    private LocalDate pvm;

    /**
     * 
     */
    private TyoMerkinnanTyyppi paivanTyyppi = TyoMerkinnanTyyppi.tavallinenTyopaiva;

    /**
     * 
     */
    private String tehtavaRyhma = "";

    /**
     * 
     */
    private String tyoPaiva = "";

    /**
     * 
     */
    private BigDecimal tyoTunteja = BigDecimal.ZERO;

    /**
     * 
     */
    private BigDecimal tyoAikaa = BigDecimal.ZERO;

    /**
     * 
     */
    private BigDecimal normaaliTyoAika = BigDecimal.ZERO;

    /**
     * 
     */
    private BigDecimal provisioPalkka = BigDecimal.ZERO;

    /**
     * 
     */
    private BigDecimal tuntiPalkka = BigDecimal.ZERO;

    /**
     * @param sisalto
     */
    public TyoPaivaMerkinta(String sisalto) {
        String[] kentat = sisalto.replace(",", ".").split("\\|");
        for (int i = 0; i < kentat.length; i++)
            taytaKentta(kentat[i], i);
        if (kentat.length == 1)
            paivanTyyppi = TyoMerkinnanTyyppi.tasausPaiva;
    }


    /**
     * @param arvo 
     * @param kentta
     */
    private void taytaKentta(String arvo, int kentta) {
        switch (kentta) {
        case 0:
            String[] pvm_s = arvo.split("\\.");
            this.pvm = LocalDate.of(Integer.parseInt(pvm_s[2]),
                    Integer.parseInt(pvm_s[1]), Integer.parseInt(pvm_s[0]));
            if (pvm.isAfter(LocalDate.of(2009, 10, 14))) {
                this.tuntiPalkka = new BigDecimal(11);
            } else {
                this.tuntiPalkka = new BigDecimal(10);
            }
            break;
        case 1:
            this.paivanTyyppi = TyoMerkinnanTyyppi.parsi(arvo);
            break;
        case 2:
            this.tehtavaRyhma = arvo;
            break;
        case 3:
            this.tyoPaiva = arvo;
            break;
        case 4:
            this.tyoTunteja = new BigDecimal(arvo);
            break;
        case 5:
            this.tyoAikaa = new BigDecimal(arvo);
            break;
        case 6:
            this.normaaliTyoAika = new BigDecimal(arvo);
            break;
        case 7:
            if (arvo.length() > 2)
                this.provisioPalkka = new BigDecimal(
                        arvo.substring(0, arvo.length() - 2));
            break;
        default:
            break;
        }
    }


    /**
     * @param pvm
     * @param tyyppi 
     * @param tehtavaRyhma 
     * @param tyoPaiva
     * @param tyoTunteja
     * @param tyoAikaa
     * @param normaaliTyoAika
     * @param provisioPalkka
     * @param tuntiPalkka
     */
    public TyoPaivaMerkinta(LocalDate pvm, String tyyppi, String tehtavaRyhma,
            String tyoPaiva, String tyoTunteja, String tyoAikaa,
            String normaaliTyoAika, String provisioPalkka, String tuntiPalkka) {
        this.pvm = pvm;
        this.paivanTyyppi = TyoMerkinnanTyyppi.parsi(tyyppi);
        this.tehtavaRyhma = tehtavaRyhma;
        this.tyoPaiva = tyoPaiva;
        this.tyoTunteja = new BigDecimal(tyoTunteja);
        this.tyoAikaa = new BigDecimal(tyoAikaa);
        this.normaaliTyoAika = new BigDecimal(normaaliTyoAika);
        this.provisioPalkka = new BigDecimal(provisioPalkka);
        this.tuntiPalkka = new BigDecimal(tuntiPalkka);
    }


    /**
     * @return
     */
    public LocalDate getPvm() {
        return pvm;
    }


    /**
     * @return
     */
    public TyoMerkinnanTyyppi getPaivanTyyppi() {
        return paivanTyyppi;
    }


    /**
     * @return
     */
    public BigDecimal getTehdytTunnit() {
        return tyoTunteja;
    }


    /**
     * @return
     */
    public BigDecimal getTyoAika() {
        return tyoAikaa;
    }


    /**
     * @return
     */
    public BigDecimal getBonukset() {
        return provisioPalkka;
    }


    /**
     * @return
     */
    public BigDecimal getTuntipalkka() {
        return tuntiPalkka;
    }


    /**
     * @param t
     * @return
     */
    public boolean equals(TyoPaivaMerkinta t) {
        return pvm.equals(t.pvm) && paivanTyyppi.equals(t.paivanTyyppi)
                && tehtavaRyhma.equals(t.tehtavaRyhma)
                && tyoPaiva.equals(t.tyoPaiva)
                && tyoTunteja.equals(t.tyoTunteja)
                && tyoAikaa.equals(t.tyoAikaa)
                && normaaliTyoAika.equals(t.normaaliTyoAika)
                && provisioPalkka.equals(t.provisioPalkka)
                && tuntiPalkka.equals(t.tuntiPalkka);
    }


    /**
     * @param localDate
     * @return
     */
    public boolean sisaltyyKuukauteen(LocalDate localDate) {
        return pvm.getMonth() == localDate.getMonth()
                && pvm.getYear() == localDate.getYear();
    }


    /**
     * @param alku
     * @param loppu
     * @return
     */
    public boolean sisaltyyValille(LocalDate alku, LocalDate loppu) {
        if (alku.equals(pvm) || loppu.equals(pvm))
            return true;
        return alku.isBefore(pvm) && loppu.isAfter(pvm);
    }


    /**
     * @return
     */
    public boolean onkoMahdollinenTyopaiva() {
        return paivanTyyppi.onkoMahdollinenTyopaiva();
    }

}
