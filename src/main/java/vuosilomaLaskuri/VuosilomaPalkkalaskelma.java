/**
 * 
 */
package vuosilomaLaskuri;

import java.math.BigDecimal;
import java.math.RoundingMode;

import tyosuhdeTiedot.TyoSuhdeTiedot.AnsaintaSaanto;
import tyosuhdeTiedot.TyoSuhdeTiedot.LomapalkanLaskutapa;
import tyosuhdeTiedot.TyosuhdeTiedotIF;

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
        return tyoSuhdeTiedot.getAnsaintaSaanto();
    }


    /**
     * @return
     */
    public LomapalkanLaskutapa getLomapalkanLaskutapa() {
        return tyoSuhdeTiedot.getLomapalkanLaskutapa();
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


    @Override
    public String toString() {
        switch (tyoSuhdeTiedot.getLomapalkanLaskutapa()) {
        case Prosenttiperusteinen:
            return "Prosenttiperusteinen lomapalkka\r\n\r\n"
                    + "Lomanmääräytymisvuoden ansio työssäolon ajalta ilman yli- ja hätätöiden korotusosia\r\n"
                    + "\t" + lomaVuodenAnsiot.setScale(2, RoundingMode.HALF_UP)
                    + " €\r\n\r\n"
                    + "Työssäolon veroiselta ajalta saamatta jäänyt palkka\r\n"
                    + "\t"
                    + tyossaOlonVeroisenAjanPalkka.setScale(2,
                            RoundingMode.HALF_UP)
                    + " €\r\n\r\n" + "Korvausprosentti\r\n" + "\t"
                    + korvausProsentti + " %\r\n\r\n" + "Lomapalkka\r\n"
                    + "\t( "
                    + lomaVuodenAnsiot.setScale(2, RoundingMode.HALF_UP) + " + "
                    + tyossaOlonVeroisenAjanPalkka.setScale(2,
                            RoundingMode.HALF_UP)
                    + " ) * " + korvausProsentti + " = "
                    + (lomaVuodenAnsiot.add(tyossaOlonVeroisenAjanPalkka))
                            .multiply(korvausProsentti)
                            .setScale(2, RoundingMode.HALF_UP);
        case ViikkoPalkka:
            int lomaPaivia = (int) Math
                    .round(lomaPaivaKerroin.doubleValue() * taysiaTyoKuukausia
                            + 0.001);
            return "Viikkopalkkaperusteinen lomapalkka\r\n\r\n"
                    + "Viikkopalkka loman alkaessa\r\n" + "\t"
                    + viikkoPalkka.setScale(2, RoundingMode.HALF_UP)
                    + " €\r\n\r\n" + "Lomapäivien lukumäärä\r\n" + "\t"
                    + lomaPaivia + "\r\n\r\n" + "Muu palkka\r\n" + "\t"
                    + bonukset.setScale(2, RoundingMode.HALF_UP) + " €\r\n\r\n"
                    + "Korvausprosentti\r\n" + "\t" + korvausProsentti
                    + " %\r\n\r\n" + "Lomapalkka\r\n" + "\t "
                    + viikkoPalkka.setScale(2, RoundingMode.HALF_UP) + " / 5 * "
                    + lomaPaivia + " + " + bonukset + " * " + korvausProsentti
                    + " = "
                    + ((viikkoPalkka.multiply(new BigDecimal(lomaPaivia))
                            .multiply(new BigDecimal("0.2")))
                                    .add(bonukset.multiply(korvausProsentti)))
                                            .setScale(2, RoundingMode.HALF_UP);
        default:
            return "";
        }
    }

}
