package vuosilomaLaskuriTest;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import org.junit.Test;

import lomalaki.VuosilomaEhdot;
import tiedostonKasittely.TiedostonKasittelija;
import tyosuhdeTiedot.TyoHistoria;
import tyosuhdeTiedot.TyoSuhdeTiedot;
import tyosuhdeTiedot.TyoSuhdeTiedot.AnsaintaSaanto;
import tyosuhdeTiedot.TyoSuhdeTiedot.LomapalkanLaskutapa;
import tyosuhdeTiedot.TyoSuhdeTiedot.SopimusTyyppi;
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
                SopimusTyyppi.tuntiPalkkainen, new BigDecimal("37.5"));
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
    public void testaaProsenttiperusteinenVuosilomaPalkka() {
        TyoHistoria th = TiedostonKasittelija
                .lueTyoHistoria("vuosiloma_vuositunnit2.txt");
        TyoSuhdeTiedot tst = new TyoSuhdeTiedot(LocalDate.of(2008, 6, 1), th,
                SopimusTyyppi.tuntiPalkkainen, new BigDecimal("37.5"));
        tst.setAnsaintaSaanto(AnsaintaSaanto.Yli14PvKuukaudessa);
        tst.setLomapalkanLaskutapa(LomapalkanLaskutapa.Prosenttiperusteinen);

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

        tst = new TyoSuhdeTiedot(LocalDate.of(2008, 6, 1), th,
                SopimusTyyppi.tuntiPalkkainen);
        tst.setAnsaintaSaanto(AnsaintaSaanto.Yli35TuntiaKuukaudessa);
        tst.setLomapalkanLaskutapa(LomapalkanLaskutapa.Prosenttiperusteinen);
        vll = new VuosilomaLaskuri(tst, vle);

        palkka = vll.laskeLomaPalkka(2010);
        laskennallinenPalkka = new BigDecimal("1579.14");
        lomaPalkka = (ansiot.add(bonukset).add(laskennallinenPalkka))
                .multiply(new BigDecimal("0.125"));
        assertEquals(lomaPalkka.setScale(2, RoundingMode.HALF_UP),
                palkka.setScale(2, RoundingMode.HALF_UP));
    }


    /**
     * Kaupanalan TES 20§11)
     * Laskennallinen palkka määräytyy poissaolon alkamishetken tuntipalkan 
     * ja sovitun keskimääräisen viikkotyöajan mukaan tai kuukausipalkkaisella 
     * poissaolon alkamishetken kuukausipalkan mukaan.
     * Jos tuntipalkkaisella ei ole sovittu keskimääräistä viikkotyöaikaa, laskennallinen
     * palkka määräytyy poissaoloa edeltävän 12 viikon keskimääräisen viikkotyöajan
     * mukaan
     * 
     * Vuosilomalaki 12§
     * Jos työntekijä on lomanmääräytymisvuoden aikana ollut estynyt tekemästä 
     * työtä 7 §:n 2 momentin 1–4 tai 7 kohdassa tarkoitetusta syystä, vuosilomapalkan 
     * perusteena olevaan palkkaan lisätään laskennallisesti poissaoloajalta saamatta 
     * jäänyt palkka enintään 7 §:n 3 momentissa säädetyltä ajalta. Poissaoloajan palkka 
     * lasketaan, jollei muusta ole sovittu, työntekijän keskimääräisen viikkotyöajan 
     * ja poissaolon alkamishetken palkan mukaan ottaen huomioon poissaoloaikana toteutetut 
     * palkankorotukset. Jos keskimääräisestä viikkotyöajasta ei ole sovittu, laskennallinen 
     * palkka määräytyy poissaoloa edeltävän 12 viikon keskimääräisen viikkotyöajan mukaan.
     * 
     * Tehtävän annon oletuksien mukainen tuntipalkka on 10€/h.
     */
    @Test
    public void testaaLaskennallinenPalkkaTavallisiltaTyopaivilta() {
        String[] testiKausiData = new String[] {
                "17.8.2009|||9-16:30|7,5|7,5|7,5|", "20.8.2009|||9-15|6|6|6|",
                "21.8.2009|||9-16:30|7,5|7,5|7,5|",
                "22.8.2009|||9-16:30|7,5|7,5|7,5|",
                "23.8.2009|||9-13:30|4,5|4,5|4,5|", "24.8.2009|||9-13|4|4|4|",
                "25.8.2009|||9-15:30|6,5|6,5|6,5|", "27.8.2009|||9-16|7|7|7|",
                "28.8.2009|||9-13|4|4|4|",
                "29.8.2009|||9-15:15|6,25|6,25|6,25|" };
        var testiKausi = new TyoHistoria(testiKausiData);
        TyoSuhdeTiedot tst = new TyoSuhdeTiedot(LocalDate.of(2009, 10, 1),
                testiKausi, SopimusTyyppi.tuntiPalkkainen);
        tst.setAnsaintaSaanto(AnsaintaSaanto.Yli14PvKuukaudessa);
        var vle = new VuosilomaEhdot();
        var vll = new VuosilomaLaskuri(tst, vle);
        BigDecimal tulos = vll.laskennallinenPalkka(testiKausi);
        assertEquals(BigDecimal.ZERO, tulos);
    }


    /**
     * 
     */
    @Test
    public void testaaLaskennallinenPalkkaVanhempainvapaalta14pvSaannolla() {

        var testiKausiData = new String[] { "17.8.2009|vanhempainvapaa||",
                "20.8.2009|vanhempainvapaa||", "21.8.2009|vanhempainvapaa||",
                "22.8.2009|vanhempainvapaa||", "23.8.2009|vanhempainvapaa||",
                "24.8.2009|vanhempainvapaa||", "25.8.2009|vanhempainvapaa||",
                "27.8.2009|vanhempainvapaa||", "28.8.2009|vanhempainvapaa||",
                "29.8.2009|vanhempainvapaa||" };
        var testiKausi = new TyoHistoria(testiKausiData);
        BigDecimal viikkoTyoAika = new BigDecimal("37.5");
        BigDecimal tuntiPalkka = new BigDecimal(10);
        var tst = new TyoSuhdeTiedot(LocalDate.of(2009, 10, 1),
                new TyoHistoria(testiKausiData), SopimusTyyppi.tuntiPalkkainen,
                viikkoTyoAika);
        tst.setAnsaintaSaanto(AnsaintaSaanto.Yli14PvKuukaudessa);
        var vle = new VuosilomaEhdot();
        var vll = new VuosilomaLaskuri(tst, vle);
        var tulos = vll.laskennallinenPalkka(testiKausi);
        assertEquals("Laskennallinen palkka viikkotyöajalla",
                new BigDecimal(testiKausiData.length).multiply(viikkoTyoAika)
                        .multiply(new BigDecimal("0.2")).multiply(tuntiPalkka),
                tulos);

        TyoHistoria th = TiedostonKasittelija
                .lueTyoHistoria("vuosiloma_vuositunnit2.txt");
        tst = new TyoSuhdeTiedot(LocalDate.of(2009, 10, 1), th,
                SopimusTyyppi.tuntiPalkkainen);

        testiKausiData = new String[] { "1.12.2009|vanhempainvapaa|",
                "2.12.2009|vanhempainvapaa||", "3.12.2009|vanhempainvapaa||",
                "4.12.2009|vanhempainvapaa||", "5.12.2009|vanhempainvapaa||",
                "8.12.2009|vanhempainvapaa||", "9.12.2009|vanhempainvapaa||",
                "10.12.2009|vanhempainvapaa|", "11.12.2009|vanhempainvapaa|",
                "12.12.2009|vanhempainvapaa|", "14.12.2009|vanhempainvapaa|",
                "15.12.2009|vanhempainvapaa|", "16.12.2009|vanhempainvapaa|",
                "17.12.2009|vanhempainvapaa|", "19.12.2009|vanhempainvapaa|",
                "21.12.2009|vanhempainvapaa|", "22.12.2009|vanhempainvapaa|",
                "23.12.2009|vanhempainvapaa|", "24.12.2009|vanhempainvapaa|",
                "25.12.2009|vanhempainvapaa|", "29.12.2009|vanhempainvapaa|",
                "30.12.2009|vanhempainvapaa|", "31.12.2009|vanhempainvapaa|" };
        testiKausi = new TyoHistoria(testiKausiData);
        tst = new TyoSuhdeTiedot(LocalDate.of(2009, 10, 1), testiKausi,
                SopimusTyyppi.tuntiPalkkainen, viikkoTyoAika);
        tst.setAnsaintaSaanto(AnsaintaSaanto.Yli14PvKuukaudessa);
        vll = new VuosilomaLaskuri(tst, vle);
        tulos = vll.laskennallinenPalkka(testiKausi);
        tuntiPalkka = new BigDecimal(11);
        assertEquals("Laskennallinen palkka viikkotyöajalla pohjatiedostosta",
                new BigDecimal(testiKausiData.length).multiply(viikkoTyoAika)
                        .multiply(new BigDecimal("0.2")).multiply(tuntiPalkka)
                        .setScale(2, RoundingMode.HALF_UP),
                tulos.setScale(2, RoundingMode.HALF_UP));
    }


    /**
     * 
     */
    @Test
    public void testaaLaskennallinenPalkkaVanhempainvapaalta35Saannolla() {

        var testiKausiData = new String[] { "17.8.2009|vanhempainvapaa||",
                "20.8.2009|vanhempainvapaa||", "21.8.2009|vanhempainvapaa||",
                "22.8.2009|vanhempainvapaa||", "23.8.2009|vanhempainvapaa||",
                "24.8.2009|vanhempainvapaa||", "25.8.2009|vanhempainvapaa||",
                "27.8.2009|vanhempainvapaa||", "28.8.2009|vanhempainvapaa||",
                "29.8.2009|vanhempainvapaa||" };
        var testiKausi = new TyoHistoria(testiKausiData);

        BigDecimal tuntiPalkka = new BigDecimal(10);
        TyoHistoria th = TiedostonKasittelija
                .lueTyoHistoria("vuosiloma_vuositunnit2.txt");
        var tst = new TyoSuhdeTiedot(LocalDate.of(2009, 10, 1), th,
                SopimusTyyppi.tuntiPalkkainen);
        tst.setAnsaintaSaanto(AnsaintaSaanto.Yli35TuntiaKuukaudessa);
        var vle = new VuosilomaEhdot();
        var vll = new VuosilomaLaskuri(tst, vle);
        var tulos = vll.laskennallinenPalkka(testiKausi);
        BigDecimal edellisen12vkTyotunnit = new BigDecimal("205.75");
        assertEquals("Laskennallinen palkka ilman määrättyä viikkotyöaikaa",
                new BigDecimal(testiKausiData.length)
                        .multiply(edellisen12vkTyotunnit).multiply(tuntiPalkka)
                        .divide(new BigDecimal(5 * 12), 3, RoundingMode.HALF_UP)
                        .setScale(2, RoundingMode.HALF_UP),
                tulos.setScale(2, RoundingMode.HALF_UP));

        testiKausiData = new String[] { "1.12.2009|vanhempainvapaa|",
                "2.12.2009|vanhempainvapaa||", "3.12.2009|vanhempainvapaa||",
                "4.12.2009|vanhempainvapaa||", "5.12.2009|vanhempainvapaa||",
                "8.12.2009|vanhempainvapaa||", "9.12.2009|vanhempainvapaa||",
                "10.12.2009|vanhempainvapaa|", "11.12.2009|vanhempainvapaa|",
                "12.12.2009|vanhempainvapaa|", "14.12.2009|vanhempainvapaa|",
                "15.12.2009|vanhempainvapaa|", "16.12.2009|vanhempainvapaa|",
                "17.12.2009|vanhempainvapaa|", "19.12.2009|vanhempainvapaa|",
                "21.12.2009|vanhempainvapaa|", "22.12.2009|vanhempainvapaa|",
                "23.12.2009|vanhempainvapaa|", "24.12.2009|vanhempainvapaa|",
                "25.12.2009|vanhempainvapaa|", "29.12.2009|vanhempainvapaa|",
                "30.12.2009|vanhempainvapaa|", "31.12.2009|vanhempainvapaa|" };

        testiKausi = new TyoHistoria(testiKausiData);
        tst = new TyoSuhdeTiedot(LocalDate.of(2008, 10, 1), th,
                SopimusTyyppi.tuntiPalkkainen);
        tst.setAnsaintaSaanto(AnsaintaSaanto.Yli35TuntiaKuukaudessa);
        vll = new VuosilomaLaskuri(tst, vle);
        tulos = vll.laskennallinenPalkka(testiKausi);
        tuntiPalkka = new BigDecimal(11);
        edellisen12vkTyotunnit = new BigDecimal("374.5");
        assertEquals(
                "Laskennallinen palkka ilman määrättyä viikkotyöaikaa pohjatiedostosta",
                new BigDecimal(testiKausiData.length)
                        .multiply(edellisen12vkTyotunnit).multiply(tuntiPalkka)
                        .divide(new BigDecimal(5 * 12), 5, RoundingMode.HALF_UP)
                        .setScale(2, RoundingMode.HALF_UP),
                tulos.setScale(2, RoundingMode.HALF_UP));
    }

}
