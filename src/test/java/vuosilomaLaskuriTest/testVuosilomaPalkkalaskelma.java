package vuosilomaLaskuriTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lomaLakiJaEhdot.VuosilomaEhdot;
import tiedostonKasittely.TiedostonKasittelija;
import tyosuhdeTiedot.TyoHistoria;
import tyosuhdeTiedot.TyoSuhdeTiedot;
import tyosuhdeTiedot.TyoSuhdeTiedot.AnsaintaSaanto;
import tyosuhdeTiedot.TyoSuhdeTiedot.LomapalkanLaskutapa;
import vuosilomaLaskuri.VuosilomaLaskuri;

/**
 * @author Joona1
 * @version 31.7.2022
 *
 */
public class testVuosilomaPalkkalaskelma {

    /**
     * 
     */
    private TyoSuhdeTiedot tstA;

    /**
     * 
     */
    private TyoSuhdeTiedot tstB;

    /**
     * 
     */
    private VuosilomaEhdot vle;

    /**
     * 
     */
    @BeforeEach
    public void alusta() {
        TyoHistoria th = TiedostonKasittelija
                .lueTyoHistoria("vuosiloma_vuositunnit2.txt");
        tstA = new TyoSuhdeTiedot(LocalDate.of(2008, 6, 1), th);
        tstB = new TyoSuhdeTiedot(LocalDate.of(2008, 6, 1), th,
                new BigDecimal("37.5"));

        vle = new VuosilomaEhdot();
        /**
         * kaupanalan TES 20§8)
         * Lomapalkka tai -korvaus on sekä tuntipalkkaisella että suhteutettua kuukausipalkkaa
         *  saavalla jäljempänä esitetystä lomanmääräytymisvuoden ansiosta:
         * • 10 % työsuhteen kestettyä lomanmääräytymisvuoden loppuun (31.3.) mennessä alle vuoden
         * • 12,5 % työsuhteen kestettyä lomanmääräytymisvuoden loppuun (31.3.)
         * mennessä vähintään vuoden.
         */
        vle.setLomapalkkaKerroinAlleVuosi(new BigDecimal("0.1"));
        vle.setLomapalkkaKerroinYliVuosi(new BigDecimal("0.125"));
    }


    /**
     * TES 20§6) Jos työntekijän työaika ja vastaavasti palkka on muuttunut lomanmääräytymisvuoden
     * aikana ja hän on kuukausipalkkainen lomanmääräytymisvuoden lopussa
     * (31.3.), hänen lomapalkkansa lasketaan tämän pykälän 8.–11. kohdan mukaan.
     * 
     * => prosenttiperusteinen lomapalkka
     * 
     * TES 20§2) Lomaa ansaitaan 35 tunnin säännön perusteella työntekijän työskennellessä
     * työsopimuksen mukaan alle 14 päivää kuukaudessa.
     * 
     * vuosilomalaki 6§) Jos työntekijä on sopimuksen mukaisesti työssä niin harvoina päivinä, 
     * että hänelle ei tästä syystä kerry ainoatakaan 14 työssäolopäivää sisältävää kalenterikuukautta 
     * tai vain osa kalenterikuukausista sisältää 14 työssäolopäivää, täydeksi lomanmääräytymiskuukaudeksi 
     * katsotaan sellainen kalenterikuukausi, jonka aikana työntekijälle on kertynyt vähintään 35 työtuntia 
     * tai 7 §:ssä tarkoitettua työssäolon veroista tuntia.
     * 
     * => ei sovelleta 14pv sääntöä
     */
    @Test
    public void testitapausA() {
        tstA.setLomapalkanLaskutapa(LomapalkanLaskutapa.Prosenttiperusteinen);
        tstA.setAnsaintaSaanto(AnsaintaSaanto.Yli35TuntiaKuukaudessa);
        var vll = new VuosilomaLaskuri(tstA, vle);
        var vlpl = vll.laskePalkkalaskelma(2010);

        int lomaPaivia = vlpl.getLomaPaivaKerroin()
                .multiply(new BigDecimal(vlpl.getTaysiaTyoKuukausia()))
                .shortValue();
        assertEquals(lomaPaivia, vll.laskeLomapaivat(2010));

        BigDecimal ansiot = vlpl.getLomaVuodenAnsiot();
        BigDecimal laskennallinenPalkka = vlpl
                .getTyossaOlonVeroisenAjanPalkka();
        BigDecimal kerroin = vlpl.getKorvausProsentti();
        BigDecimal lomaPalkka = (ansiot.add(laskennallinenPalkka))
                .multiply(kerroin);
        assertEquals(lomaPalkka, vll.laskeLomaPalkka(2010));
    }


    /**
     * TES 20§6) Jos työntekijän työaika ja vastaavasti palkka on muuttunut lomanmääräytymisvuoden
     * aikana ja hän on kuukausipalkkainen lomanmääräytymisvuoden lopussa
     * (31.3.), hänen lomapalkkansa lasketaan tämän pykälän 8.–11. kohdan mukaan.
     * 
     * => prosenttiperusteinen lomapalkka?
     */
    @Test
    public void testitapausB() {
        tstB.setAnsaintaSaanto(AnsaintaSaanto.Yli14PvKuukaudessa);
        var vll = new VuosilomaLaskuri(tstB, vle);
        var vlpl = vll.laskePalkkalaskelma(2010);

        int lomaPaivia = vlpl.getLomaPaivaKerroin()
                .multiply(new BigDecimal(vlpl.getTaysiaTyoKuukausia()))
                .shortValue();
        assertEquals(lomaPaivia, vll.laskeLomapaivat(2010));

        BigDecimal ansiot = vlpl.getLomaVuodenAnsiot();
        BigDecimal laskennallinenPalkka = vlpl
                .getTyossaOlonVeroisenAjanPalkka();
        BigDecimal kerroin = vlpl.getKorvausProsentti();
        BigDecimal lomaPalkka = (ansiot.add(laskennallinenPalkka))
                .multiply(kerroin);
        assertEquals(lomaPalkka, vll.laskeLomaPalkka(2010));
    }


    /**
     * 
     */
    @Test
    public void testitapausBViikkoPalkka() {
        tstB.setAnsaintaSaanto(AnsaintaSaanto.Yli14PvKuukaudessa);
        tstB.setLomapalkanLaskutapa(LomapalkanLaskutapa.ViikkoPalkka);
        var vll = new VuosilomaLaskuri(tstB, vle);
        var vlpl = vll.laskePalkkalaskelma(2010);

        int lomaPaivia = vlpl.getLomaPaivaKerroin()
                .multiply(new BigDecimal(vlpl.getTaysiaTyoKuukausia()))
                .shortValue();
        assertEquals(lomaPaivia, vll.laskeLomapaivat(2010));

        BigDecimal viikkoPalkka = vlpl.getViikkoPalkka();
        BigDecimal bonukset = vlpl.getBonukset();
        BigDecimal kerroin = vlpl.getKorvausProsentti();
        BigDecimal lomaPalkka = (viikkoPalkka.multiply(new BigDecimal("0.2"))
                .multiply(new BigDecimal(lomaPaivia)))
                        .add(bonukset.multiply(kerroin));
        assertEquals(lomaPalkka, vll.laskeLomaPalkka(2010));
    }
}
