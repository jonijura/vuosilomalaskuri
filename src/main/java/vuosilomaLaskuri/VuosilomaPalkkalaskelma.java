/**
 * 
 */
package vuosilomalaskuri;

import java.math.BigDecimal;
import java.math.RoundingMode;

import tyosuhdetiedot.TyosuhdeTiedotIF;
import tyosuhdetiedot.TyoSuhdeTiedot.AnsaintaSaanto;
import tyosuhdetiedot.TyoSuhdeTiedot.LomapalkanLaskutapa;
import tyosuhdetiedot.TyoSuhdeTiedot.LomarahanMaksuedellytys;

/**
 * @author Joona1
 * @version 31.7.2022
 *
 */
public class VuosilomaPalkkalaskelma {

    /**
     * 
     */
    private BigDecimal lomaVuodenAnsiot = BigDecimal.ZERO;

    /**
     * 
     */
    private BigDecimal tyossaOlonVeroisenAjanPalkka = BigDecimal.ZERO;

    /**
     * 
     */
    private BigDecimal korvausProsentti = BigDecimal.ZERO;

    /**
     * 
     */
    private BigDecimal lomaPaivaKerroin = BigDecimal.ZERO;

    /**
     * 
     */
    private int taysiaTyoKuukausia = 0;

    /**
     * 
     */
    private BigDecimal viikkoPalkka;

    /**
     * 
     */
    private BigDecimal bonukset;

    /**
     * 
     */
    private TyosuhdeTiedotIF tyoSuhdeTiedot;

    /**
     * 
     */
    private BigDecimal lomaPalkka;

    /**
     * 
     */
    private int lomaPaivia;

    /**
     * 
     */
    private BigDecimal ansiotIlmanYliTaiHata;

    /**
     * 
     */
    private BigDecimal laskennallisiaTyopaivia;

    /**
     * 
     */
    private BigDecimal tuntipalkkaisenLomapalkkaKerroin;

    /**
     * 
     */
    private AnsaintaSaanto ansaintaSaanto;

    /**
     * @return
     */
    public BigDecimal getAnsiotIlmanYliTaiHata() {
        return ansiotIlmanYliTaiHata;
    }


    /**
     * @param ansiotIlmanYliTaiHata
     */
    public void setAnsiotIlmanYliTaiHata(BigDecimal ansiotIlmanYliTaiHata) {
        this.ansiotIlmanYliTaiHata = ansiotIlmanYliTaiHata;
    }


    /**
     * @return
     */
    public BigDecimal getLaskennallisiaTyopaivia() {
        return laskennallisiaTyopaivia;
    }


    /**
     * @param laskennallisiaTyopaivia
     */
    public void setLaskennallisiaTyopaivia(BigDecimal laskennallisiaTyopaivia) {
        this.laskennallisiaTyopaivia = laskennallisiaTyopaivia;
    }


    /**
     * @return
     */
    public BigDecimal getTuntipalkkaisenLomapalkkaKerroin() {
        return tuntipalkkaisenLomapalkkaKerroin;
    }


    /**
     * @param tuntipalkkaisenLomapalkkaKerroin
     */
    public void setTuntipalkkaisenLomapalkkaKerroin(
            BigDecimal tuntipalkkaisenLomapalkkaKerroin) {
        this.tuntipalkkaisenLomapalkkaKerroin = tuntipalkkaisenLomapalkkaKerroin;
    }


    /**
     * @return
     */
    public BigDecimal getLomaVuodenAnsiot() {
        return lomaVuodenAnsiot;
    }


    /**
     * @return
     */
    public BigDecimal getTyossaOlonVeroisenAjanPalkka() {
        return tyossaOlonVeroisenAjanPalkka;
    }


    /**
     * @return
     */
    public BigDecimal getKorvausProsentti() {
        return korvausProsentti;
    }


    /**
     * @return
     */
    public BigDecimal getLomaPaivaKerroin() {
        return lomaPaivaKerroin;
    }


    /**
     * @return
     */
    public int getTaysiaTyoKuukausia() {
        return taysiaTyoKuukausia;
    }


    /**
     * @return
     */
    public BigDecimal getViikkoPalkka() {
        return viikkoPalkka;
    }


    /**
     * @return
     */
    public BigDecimal getBonukset() {
        return bonukset;
    }


    /**
     * @return
     */
    public AnsaintaSaanto getAnsaintaSaanto() {
        return ansaintaSaanto;
    }


    /**
     * @return
     */
    public LomapalkanLaskutapa getLomapalkanLaskutapa() {
        return tyoSuhdeTiedot.getLomapalkanLaskutapa();
    }


    /**
     * @return
     */
    public int getLomapaivienLkm() {
        return lomaPaivia;
    }


    /**
     * @return
     */
    public BigDecimal getLomaPalkka() {
        return lomaPalkka;
    }


    /**
     * @return
     */
    public BigDecimal getViikottaisienTyopaivienMaaraJaettunaViidella() {
        return tyoSuhdeTiedot.getTyopaiviaViikossa().divide(new BigDecimal(5));
    }


    /**
     * @return
     */
    public BigDecimal getKeskimaarainenPaivapalkka() {
        return ansiotIlmanYliTaiHata.divide(laskennallisiaTyopaivia, 5,
                RoundingMode.HALF_UP);
    }


    /**
     * @return
     */
    public LomarahanMaksuedellytys getLomarahanMaksuedellytys() {
        return tyoSuhdeTiedot.getLomarahanMaksuedellytys();
    }


    /**
     * TES 21??1)
     * Lomaraha on 50???% vuosilomalain mukaan ansaittua lomaa vastaavasta lomapalkasta.
     * @return
     */
    public BigDecimal getLomarahanSuuruus() {
        return lomaPalkka.multiply(new BigDecimal("0.5")).setScale(2,
                RoundingMode.HALF_UP);
    }


    /**
     * @param lomaVuodenAnsiot
     */
    public void setLomaVuodenAnsiot(BigDecimal lomaVuodenAnsiot) {
        this.lomaVuodenAnsiot = lomaVuodenAnsiot;
    }


    /**
     * @param tyossaOlonVeroisenAjanPalkka
     */
    public void setTyossaOlonVeroisenAjanPalkka(
            BigDecimal tyossaOlonVeroisenAjanPalkka) {
        this.tyossaOlonVeroisenAjanPalkka = tyossaOlonVeroisenAjanPalkka;
    }


    /**
     * @param korvausProsentti
     */
    public void setKorvausProsentti(BigDecimal korvausProsentti) {
        this.korvausProsentti = korvausProsentti;
    }


    /**
     * @param lomaPaivaKerroin
     */
    public void setLomaPaivaKerroin(BigDecimal lomaPaivaKerroin) {
        this.lomaPaivaKerroin = lomaPaivaKerroin;
    }


    /**
     * @param taysiaTyoKuukausia
     */
    public void setTaysiaTyoKuukausia(int taysiaTyoKuukausia) {
        this.taysiaTyoKuukausia = taysiaTyoKuukausia;
    }


    /**
     * @param bigDecimal
     */
    public void setViikkopalkka(BigDecimal bigDecimal) {
        viikkoPalkka = bigDecimal;
    }


    /**
     * @param bonukset
     */
    public void setBonukset(BigDecimal bonukset) {
        this.bonukset = bonukset;
    }


    /**
     * @param tyoSuhdeTiedot2
     */
    public void setTyosuhdeTiedot(TyosuhdeTiedotIF tyoSuhdeTiedot2) {
        tyoSuhdeTiedot = tyoSuhdeTiedot2;
    }


    /**
     * @param lomaPalkka
     */
    public void setLomaPalkka(BigDecimal lomaPalkka) {
        this.lomaPalkka = lomaPalkka.setScale(2, RoundingMode.HALF_UP);
    }


    /**
     * @param lomaPaivienLkm
     */
    public void setLomaPaivat(int lomaPaivienLkm) {
        lomaPaivia = lomaPaivienLkm;
    }


    /**
     * @param saanto
     */
    public void setAnsaintaSaanto(AnsaintaSaanto saanto) {
        ansaintaSaanto = saanto;
    }


    @Override
    public String toString() {
        switch (tyoSuhdeTiedot.getLomapalkanLaskutapa()) {
        case Prosenttiperusteinen:
            return "Prosenttiperusteinen lomapalkka\r\n\r\n"
                    + "Lomanm????r??ytymisvuoden ansio ty??ss??olon ajalta ilman yli- ja h??t??t??iden korotusosia\r\n"
                    + "\t" + lomaVuodenAnsiot.setScale(2, RoundingMode.HALF_UP)
                    + " ???\r\n\r\n"
                    + "Ty??ss??olon veroiselta ajalta saamatta j????nyt palkka\r\n"
                    + "\t"
                    + tyossaOlonVeroisenAjanPalkka.setScale(2,
                            RoundingMode.HALF_UP)
                    + " ???\r\n\r\n" + "Korvausprosentti\r\n" + "\t"
                    + korvausProsentti + " %\r\n\r\n" + "Lomapalkka\r\n"
                    + "\t( "
                    + lomaVuodenAnsiot.setScale(2, RoundingMode.HALF_UP) + " + "
                    + tyossaOlonVeroisenAjanPalkka.setScale(2,
                            RoundingMode.HALF_UP)
                    + " ) * " + korvausProsentti + " = " + lomaPalkka;
        case ViikkoPalkka:
            return "Viikkopalkkaperusteinen lomapalkka\r\n\r\n"
                    + "Viikkopalkka loman alkaessa\r\n" + "\t"
                    + viikkoPalkka.setScale(2, RoundingMode.HALF_UP)
                    + " ???\r\n\r\n" + "Lomap??ivien lukum????r??\r\n" + "\t"
                    + lomaPaivia + "\r\n\r\n" + "Muu palkka\r\n" + "\t"
                    + bonukset.setScale(2, RoundingMode.HALF_UP) + " ???\r\n\r\n"
                    + "Korvausprosentti\r\n" + "\t" + korvausProsentti
                    + " %\r\n\r\n" + "Lomapalkka\r\n" + "\t "
                    + viikkoPalkka.setScale(2, RoundingMode.HALF_UP) + " / 5 * "
                    + lomaPaivia + " + " + bonukset + " * " + korvausProsentti
                    + " = " + lomaPalkka;
        case TuntiPalkka:
            return "Tuntipalkkaperusteinen lomapalkka\r\n\r\n"
                    + "Lomanm????r??ytymisvuoden ansio ty??ss??olon ajalta ilman\r\n"
                    + "yli- ja h??t??t??iden korotusosia\r\n" + "\t"
                    + ansiotIlmanYliTaiHata.setScale(2, RoundingMode.HALF_UP)
                    + " ???\r\n\r\n" + "Tehdyt ty??p??iv??t + 1/8 ylit??ist??\r\n"
                    + "\t"
                    + laskennallisiaTyopaivia.setScale(2, RoundingMode.HALF_UP)
                    + "\r\n\r\n" + "Keskim????r??inen p??iv??palkka\r\n" + "\t"
                    + ansiotIlmanYliTaiHata.setScale(2, RoundingMode.HALF_UP)
                    + " : "
                    + laskennallisiaTyopaivia.setScale(2, RoundingMode.HALF_UP)
                    + " = "
                    + getKeskimaarainenPaivapalkka().setScale(2,
                            RoundingMode.HALF_UP)
                    + " ???\r\n\r\n"
                    + "Viikoittaisten ty??p??ivien m????r?? jaettuna viidell??\r\n"
                    + "\t" + getViikottaisienTyopaivienMaaraJaettunaViidella()
                    + "\r\n\r\n" + "Vuosilomalain mukainen kerroin \r\n" + "\t "
                    + tuntipalkkaisenLomapalkkaKerroin + " \r\n\r\n"
                    + "lomaPalkka\r\n" + "\t"
                    + getKeskimaarainenPaivapalkka().setScale(2,
                            RoundingMode.HALF_UP)
                    + " * " + getViikottaisienTyopaivienMaaraJaettunaViidella()
                    + " * " + tuntipalkkaisenLomapalkkaKerroin + " = "
                    + lomaPalkka;
        default:
            return "";
        }
    }

}
