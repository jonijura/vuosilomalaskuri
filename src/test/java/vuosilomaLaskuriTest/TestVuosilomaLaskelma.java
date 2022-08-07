/**
 * 
 */
package vuosilomaLaskuriTest;

import static org.junit.Assert.assertEquals;

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
import vuosilomaLaskuri.VuosilomaPalkkalaskelma;

/**
 * @author Joona1
 * @version 7.8.2022
 *
 */
/**
 * @author Joona1
 * @version 7.8.2022
 *
 */
public class TestVuosilomaLaskelma {

    /**
     * 
     */
    private VuosilomaPalkkalaskelma vlplA;

    /**
     * 
     */
    private VuosilomaPalkkalaskelma vlplB;

    /**
     * 
     */
    @BeforeEach
    public void alusta() {
        TyoHistoria th = TiedostonKasittelija
                .lueTyoHistoria("vuosiloma_vuositunnit2.txt");
        var tstA = new TyoSuhdeTiedot(LocalDate.of(2008, 6, 1), th);
        var tstB = new TyoSuhdeTiedot(LocalDate.of(2008, 6, 1), th,
                new BigDecimal("37.5"));

        var vle = new VuosilomaEhdot();
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

        var vllA = new VuosilomaLaskuri(tstA, vle);
        var vllB = new VuosilomaLaskuri(tstB, vle);

        vlplA = vllA.laskePalkkalaskelma(2010);
        vlplB = vllB.laskePalkkalaskelma(2010);

    }


    /**
     * Vuosilomalaki 6§ 
     * Jos työntekijä on sopimuksen mukaisesti työssä 
     * niin harvoina päivinä, että hänelle ei tästä syystä kerry 
     * ainoatakaan 14 työssäolopäivää sisältävää kalenterikuukautta tai 
     * vain osa kalenterikuukausista sisältää 14 työssäolopäivää, täydeksi 
     * lomanmääräytymiskuukaudeksi katsotaan sellainen kalenterikuukausi, 
     * jonka aikana työntekijälle on kertynyt vähintään 35 työtuntia tai 
     * 7 §:ssä tarkoitettua työssäolon veroista tuntia.
     * 
     * Eli ansainta sääntö riippuu siitä, miten se on sopimuksessa määritelty.
     * Koska tätä ei kerrota, olen laskenut montako työpäivää kuukaudessa tulee
     * keskimäärin ja katsonut onko se yli 14.
     */
    @Test
    public void testaaAnsaintaSaanto() {
        assertEquals("ansaintasääntö testitapaus A", vlplA.getAnsaintaSaanto(),
                AnsaintaSaanto.Yli14PvKuukaudessa);
        assertEquals("ansaintasääntö testitapaus B", vlplB.getAnsaintaSaanto(),
                AnsaintaSaanto.Yli14PvKuukaudessa);
    }


    /**
     * TES 20§8) 
     * Lomapalkka tai -korvaus on sekä tuntipalkkaisella että suhteutettua kuukausipalkkaa 
     * saavalla jäljempänä esitetystä lomanmääräytymisvuoden ansiosta:
     * • 10 % työsuhteen kestettyä lomanmääräytymisvuoden loppuun (31.3.) mennessä alle vuoden
     * • 12,5 % työsuhteen kestettyä lomanmääräytymisvuoden loppuun (31.3.)
     * mennessä vähintään vuoden.
     * 
     * (tuntipalkkaisten lomapalkan laskutapa vastaa tessissä vuosilomalain prosenttiperusteista
     * lomapalkkaa)
     */
    @Test
    public void testaaLomapalkanLaskentaTapa() {
        assertEquals("lomapalkan lakutapa testitapaus A",
                LomapalkanLaskutapa.Prosenttiperusteinen,
                vlplA.getLomapalkanLaskutapa());
        assertEquals("lomapalkan lakutapa testitapaus B",
                LomapalkanLaskutapa.Prosenttiperusteinen,
                vlplB.getLomapalkanLaskutapa());
    }


    /**
     * 
     */
    @Test
    public void testaaLomapaivienLkm() {
        // testaa kerroin
        // testaa taysien kk lkm
    }


    /**
     * 
     */
    @Test
    public void testaaTyoPaivienLkm() {
        // tyopaivia
        // tyonveroisiaPaivia
    }


    /**
     * 
     */
    @Test
    public void testaaYlitoidenJaHatatoidenLkm() {
        //
    }


    /**
     * 
     */
    @Test
    public void testaaYhdisttyTyopaivatJaHatatyot() {
        //
    }


    /**
     * 
     */
    @Test
    public void testaaViikottaisienTyopaivienMaaraJaettunaViidella() {
        //
    }


    /**
     * 
     */
    @Test
    public void testaaKeskimaarainenPaivapalkka() {
        //
    }


    /**
     * 
     */
    @Test
    public void testaaVuosilomalainMukainenKerroin() {
        //
    }


    /**
     * 
     */
    @Test
    public void testaaTyossaolonVeroiseltaAjaltaSaamattaJaanytPalkka() {
        //
    }


    /**
     * 
     */
    @Test
    public void testaaKorvausProsentti() {
        //
    }


    /**
     * 
     */
    @Test
    public void testaaOikeusLomarahaan() {
        // enum?
    }


    /**
     * 
     */
    @Test
    public void testaaLomarahanMaara() {
        //
    }

}
