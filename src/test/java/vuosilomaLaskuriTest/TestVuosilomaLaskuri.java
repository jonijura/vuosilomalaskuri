package vuosilomaLaskuriTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import org.junit.Test;

import lomaLakiJaEhdot.VuosilomaEhdot;
import tiedostonKasittely.TiedostonKasittelija;
import tyosuhdeTiedot.TyoHistoria;
import tyosuhdeTiedot.TyoSuhdeTiedot;
import tyosuhdeTiedot.TyoSuhdeTiedot.AnsaintaSaanto;
import vuosilomaLaskuri.VuosilomaLaskuri;

/**
 * @author Joona1
 * @version 26.7.2022
 *
 */
public class TestVuosilomaLaskuri {

    /**
     * Vuosilomalaki 6§
     * 
     * Täytenä lomanmääräytymiskuukautena pidetään kalenterikuukautta, 
     * jolloin työntekijälle on kertynyt vähintään 14 työssäolopäivää 
     * tai 7 §:n 1 ja 2 momentissa tarkoitettua työssäolon veroista päivää.
     * 
     * Jos työntekijä on sopimuksen mukaisesti työssä niin harvoina päivinä, 
     * että hänelle ei tästä syystä kerry ainoatakaan 14 työssäolopäivää 
     * sisältävää kalenterikuukautta tai vain osa kalenterikuukausista 
     * sisältää 14 työssäolopäivää, täydeksi lomanmääräytymiskuukaudeksi 
     * katsotaan sellainen kalenterikuukausi, jonka aikana työntekijälle 
     * on kertynyt vähintään 35 työtuntia tai 7 §:ssä tarkoitettua 
     * työssäolon veroista tuntia.
     */
    @Test
    public void testaaLomaPaivienLaskeminen() {
        TyoHistoria th = TiedostonKasittelija
                .lueTyoHistoria("vuosiloma_vuositunnit2.txt");
        TyoSuhdeTiedot tst = new TyoSuhdeTiedot(LocalDate.of(2008, 6, 1), th,
                new BigDecimal("37.5"));
        var vle = new VuosilomaEhdot();
        var vll = new VuosilomaLaskuri(tst, vle);

        tst.setAnsaintaSaanto(AnsaintaSaanto.Yli14PvKuukaudessa);
        int lomaPaivia = vll.laskeLomapaivat(2009);
        assertEquals(lomaPaivia, 4);
        lomaPaivia = vll.laskeLomapaivat(2010);
        assertEquals(lomaPaivia, 25);

        tst.setAnsaintaSaanto(AnsaintaSaanto.Yli35TuntiaKuukaudessa);
        lomaPaivia = vll.laskeLomapaivat(2009);
        assertEquals(lomaPaivia, 6);
        lomaPaivia = vll.laskeLomapaivat(2010);
        assertEquals(lomaPaivia, 28);
    }


    /**
     * Vuosilomalaki 10§
     * Lomapalkka lasketaan 12 §:n mukaan myös silloin, kun työntekijän työaika ja vastaavasti 
     * palkka on muuttunut lomanmääräytymisvuoden aikana. Jos muutokset tapahtuvat vasta 
     * lomanmääräytymisvuoden päättymisen jälkeen ennen vuosiloman tai sen osan alkamista, 
     * lomapalkka lasketaan lomanmääräytymisvuoden aikaisen työajan perusteella määräytyvän 
     * viikko- tai kuukausipalkan mukaan.
     * 
     * 
     * Vuosilomalaki 12§
     * 
     * Muun kuin viikko- tai kuukausipalkalla alle 14 päivänä kalenterikuukaudessa 
     * työtä tekevän työntekijän vuosilomapalkka on 9 prosenttia taikka työsuhteen 
     * jatkuttua lomakautta edeltävän lomanmääräytymisvuoden loppuun mennessä vähintään 
     * vuoden 11,5 prosenttia lomanmääräytymisvuoden aikana työssäolon ajalta maksetusta 
     * tai maksettavaksi erääntyneestä palkasta lukuun ottamatta hätätyöstä ja lain tai 
     * sopimuksen mukaisesta ylityöstä maksettavaa korotusta.
     * 
     * Jos työntekijä on lomanmääräytymisvuoden aikana ollut estynyt tekemästä työtä 
     * 7 §:n 2 momentin 1–4 tai 7 kohdassa tarkoitetusta syystä, vuosilomapalkan 
     * perusteena olevaan palkkaan lisätään laskennallisesti poissaoloajalta saamatta 
     * jäänyt palkka enintään 7 §:n 3 momentissa säädetyltä ajalta. Poissaoloajan palkka 
     * lasketaan, jollei muusta ole sovittu, työntekijän keskimääräisen viikkotyöajan ja 
     * poissaolon alkamishetken palkan mukaan ottaen huomioon poissaoloaikana toteutetut 
     * palkankorotukset. Jos keskimääräisestä viikkotyöajasta ei ole sovittu, laskennallinen 
     * palkka määräytyy poissaoloa edeltävän 12 viikon keskimääräisen viikkotyöajan mukaan.
     * 
     * 
     * kaupanalan TES 20§9)
     * 
     * Lomanmääräytymisvuoden (1.4.–31.3.) ansio muodostuu:
     * • työssäoloajan palkasta lukuun ottamatta hätä- ja ylityön ajalta maksettua palkkaa
     * • poissaoloajalta maksetusta palkasta (7 § 5. kohta ja 17–18 §) ja
     * • laskennallisesta palkasta.
     */
    @Test
    public void testaaVuosilomaPalkka() {
        TyoHistoria th = TiedostonKasittelija
                .lueTyoHistoria("vuosiloma_vuositunnit2.txt");
        TyoSuhdeTiedot tst = new TyoSuhdeTiedot(LocalDate.of(2008, 6, 1), th,
                new BigDecimal("37.5"));
        tst.setAnsaintaSaanto(AnsaintaSaanto.Yli14PvKuukaudessa);

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

        var vll = new VuosilomaLaskuri(tst, vle);
        BigDecimal palkka = vll.laskeLomaPalkka(2010);
        BigDecimal ansiot = (new BigDecimal("687.75")
                .multiply(new BigDecimal(10))).add(
                        new BigDecimal("642.5").multiply(new BigDecimal(11)));
        BigDecimal bonukset = new BigDecimal(1013);
        BigDecimal laskennallinenPalkka = new BigDecimal("1897.5");
        BigDecimal lomaPalkka = (ansiot.add(bonukset).add(laskennallinenPalkka))
                .multiply(new BigDecimal("0.125"));
        assertEquals(lomaPalkka.setScale(2, RoundingMode.HALF_UP),
                palkka.setScale(2, RoundingMode.HALF_UP));

        tst = new TyoSuhdeTiedot(LocalDate.of(2008, 6, 1), th);
        tst.setAnsaintaSaanto(AnsaintaSaanto.Yli35TuntiaKuukaudessa);
        vll = new VuosilomaLaskuri(tst, vle);

        palkka = vll.laskeLomaPalkka(2010);
        laskennallinenPalkka = new BigDecimal("1579.14");
        lomaPalkka = (ansiot.add(bonukset).add(laskennallinenPalkka))
                .multiply(new BigDecimal("0.125"));
        assertEquals(lomaPalkka.setScale(2, RoundingMode.HALF_UP),
                palkka.setScale(2, RoundingMode.HALF_UP));
    }

}
