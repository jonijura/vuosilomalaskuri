/**
 * 
 */
package vuosilomaLaskuriTest;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
     * Vuosilomalaki 5§
     * Työntekijällä on oikeus saada lomaa kaksi ja puoli arkipäivää 
     * kultakin täydeltä lomanmääräytymiskuukaudelta. Jos työsuhde on 
     * lomanmääräytymisvuoden loppuun mennessä jatkunut yhdenjaksoisesti 
     * alle vuoden, työntekijällä on kuitenkin oikeus saada lomaa kaksi 
     * arkipäivää kultakin täydeltä lomanmääräytymiskuukaudelta. Loman pituutta 
     * laskettaessa päivän osa pyöristetään täyteen lomapäivään.
     */
    @Test
    public void testaaLomapaivienLkm() {
        assertEquals("Lomapäiväkerroin testitapaus A", new BigDecimal("2.5"),
                vlplA.getLomaPaivaKerroin());
        assertEquals("Lomapäiväkerroin testitapaus B", new BigDecimal("2.5"),
                vlplB.getLomaPaivaKerroin());
        assertEquals("Lomapäivien lukumäärä testitapaus A", 25,
                vlplA.getLomapaivienLkm());
        assertEquals("Lomapäivien lukumäärä testitapaus B", 25,
                vlplB.getLomapaivienLkm());
    }


    /**
     * 
     */
    @Test
    public void testaaTyoPaivienLkm() {
        // tyopaivia
        // tyonveroisiaPaivia
        // ei koske testitapauksia
    }


    /**
     * 
     */
    @Test
    public void testaaYlitoidenJaHatatoidenLkm() {
        // ei koske testitapauksia
    }


    /**
     * 
     */
    @Test
    public void testaaYhdisttyTyopaivatJaHatatyot() {
        // ei koske testitapauksia
    }


    /**
     * 
     */
    @Test
    public void testaaViikottaisienTyopaivienMaaraJaettunaViidella() {
        // ei koske testitapauksia
    }


    /**
     * 
     */
    @Test
    public void testaaKeskimaarainenPaivapalkka() {
        // ei koske testitapauksia
    }


    /**
     * 
     */
    @Test
    public void testaaVuosilomalainMukainenKerroin() {
        // ei koske testitapauksia
    }


    /**
     * Vuosilomalaki 12§
     * Jos työntekijä on lomanmääräytymisvuoden aikana ollut 
     * estynyt tekemästä työtä 7 §:n 2 momentin 1–4 tai 7 kohdassa 
     * tarkoitetusta syystä, vuosilomapalkan perusteena olevaan palkkaan 
     * lisätään laskennallisesti poissaoloajalta saamatta jäänyt palkka 
     * enintään 7 §:n 3 momentissa säädetyltä ajalta. Poissaoloajan palkka 
     * lasketaan, jollei muusta ole sovittu, työntekijän keskimääräisen 
     * viikkotyöajan ja poissaolon alkamishetken palkan mukaan ottaen 
     * huomioon poissaoloaikana toteutetut palkankorotukset. Jos keskimääräisestä 
     * viikkotyöajasta ei ole sovittu, laskennallinen palkka määräytyy poissaoloa 
     * edeltävän 12 viikon keskimääräisen viikkotyöajan mukaan.
     */
    @Test
    public void testaaTyossaolonVeroiseltaAjaltaSaamattaJaanytPalkka() {
        assertEquals(
                "testitapaus A laskennallinen palkka työssäolon veroiselta ajalta",
                new BigDecimal("1579.14"),
                vlplA.getTyossaOlonVeroisenAjanPalkka().setScale(2,
                        RoundingMode.HALF_UP));
        assertEquals(
                "testitapaus B laskennallinen palkka työssäolon veroiselta ajalta",
                new BigDecimal("1897.50"),
                vlplB.getTyossaOlonVeroisenAjanPalkka().setScale(2,
                        RoundingMode.HALF_UP));
    }


    /**
     * TES 20§8)
     * Lomapalkka tai -korvaus on sekä tuntipalkkaisella että suhteutettua kuukausipalkkaa 
     * saavalla jäljempänä esitetystä lomanmääräytymisvuoden ansiosta:
     * • 10 % työsuhteen kestettyä lomanmääräytymisvuoden loppuun (31.3.) mennessä alle vuoden
     * • 12,5 % työsuhteen kestettyä lomanmääräytymisvuoden loppuun (31.3.)
     * mennessä vähintään vuoden.
     */
    @Test
    public void testaaKorvausProsentti() {
        assertEquals("korvausprosentti, testitapaus A", new BigDecimal("0.125"),
                vlplA.getKorvausProsentti());
        assertEquals("korvausprosentti, testitapaus B", new BigDecimal("0.125"),
                vlplB.getKorvausProsentti());
    }


    /**
     * 
     */
    @Test
    public void testaaLomapalkka() {
        assertEquals("Lomapalkka testitapaus A", new BigDecimal("2067.14"),
                vlplA.getLomaPalkka());
        assertEquals("Lomapalkka testitapaus A", new BigDecimal("2106.94"),
                vlplB.getLomaPalkka());
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
