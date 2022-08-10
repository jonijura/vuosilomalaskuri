package testvuosilomalaskuri;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lomalaki.VuosilomaEhdot;
import tiedostonkasittely.TiedostonKasittelija;
import tyosuhdetiedot.TyoHistoria;
import tyosuhdetiedot.TyoSuhdeTiedot;
import tyosuhdetiedot.TyoSuhdeTiedot.AnsaintaSaanto;
import tyosuhdetiedot.TyoSuhdeTiedot.LomapalkanLaskutapa;
import tyosuhdetiedot.TyoSuhdeTiedot.LomarahanMaksuedellytys;
import tyosuhdetiedot.TyoSuhdeTiedot.SopimusTyyppi;
import vuosilomalaskuri.VuosilomaLaskuri;

/**
 * @author Joona1
 * @version 10.8.2022
 *
 */
public class TestVuosilomaLaskelma {

    /**
     * 
     */
    private TyoHistoria th;

    /**
     * 
     */
    private VuosilomaEhdot vle;

    /**
     * 
     */
    @BeforeEach
    public void alusta() {
        th = TiedostonKasittelija.lueTyoHistoria("vuosiloma_vuositunnit2.txt");

        /**
         * kaupanalan TES 20§8)
         * Lomapalkka tai -korvaus on sekä tuntipalkkaisella että suhteutettua kuukausipalkkaa
         *  saavalla jäljempänä esitetystä lomanmääräytymisvuoden ansiosta:
         * • 10 % työsuhteen kestettyä lomanmääräytymisvuoden loppuun (31.3.) mennessä alle vuoden
         * • 12,5 % työsuhteen kestettyä lomanmääräytymisvuoden loppuun (31.3.)
         * mennessä vähintään vuoden.
         */
        vle = new VuosilomaEhdot();
        vle.setLomapalkkaKerroinAlleVuosi(new BigDecimal("0.1"));
        vle.setLomapalkkaKerroinYliVuosi(new BigDecimal("0.125"));
    }


    /**
     * tehtävänannon testitapaus A
     */
    @Test
    public void testitapausA() {
        var tstA = new TyoSuhdeTiedot(LocalDate.of(2008, 6, 1),
                th.ilmanTyoajanTasauksia(), SopimusTyyppi.tuntiPalkkainen);
        var vllA = new VuosilomaLaskuri(tstA, vle);
        var vlplA = vllA.laskePalkkalaskelma(2010);

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
         * kuukaudessa 7.2009 merkintöjä on vain 11, joten vain osa kalenteri-
         * kuukauksista sisältää 14 työssäolopäivää
         */
        assertEquals("ansaintasääntö", vlplA.getAnsaintaSaanto(),
                AnsaintaSaanto.Yli35TuntiaKuukaudessa);
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
         * 
         * Tapaus A on tuntipalkkainen, joten se päätyy prosenttiperusteiseen lomapalkan laskutapaan.
         */
        assertEquals("lomapalkan laskutapa",
                LomapalkanLaskutapa.Prosenttiperusteinen,
                vlplA.getLomapalkanLaskutapa());
        /**
         * Vuosilomalaki 5§
         * Työntekijällä on oikeus saada lomaa kaksi ja puoli arkipäivää 
         * kultakin täydeltä lomanmääräytymiskuukaudelta. Jos työsuhde on 
         * lomanmääräytymisvuoden loppuun mennessä jatkunut yhdenjaksoisesti 
         * alle vuoden, työntekijällä on kuitenkin oikeus saada lomaa kaksi 
         * arkipäivää kultakin täydeltä lomanmääräytymiskuukaudelta. Loman pituutta 
         * laskettaessa päivän osa pyöristetään täyteen lomapäivään.
         * 
         * Täysiä työkuukausia on 35-tunnin säännöllä 10kpl.
         */
        assertEquals("Lomapäiväkerroin", new BigDecimal("2.5"),
                vlplA.getLomaPaivaKerroin());
        assertEquals("Lomapäivien lukumäärä", 25, vlplA.getLomapaivienLkm());
        /**
         * Vuosilomalaki 12§
         *  Muun kuin viikko- tai kuukausipalkalla alle 14 päivänä kalenterikuukaudessa 
         * työtä tekevän työntekijän vuosilomapalkka on 9 prosenttia taikka työsuhteen 
         * jatkuttua lomakautta edeltävän lomanmääräytymisvuoden loppuun mennessä 
         * vähintään vuoden 11,5 prosenttia lomanmääräytymisvuoden aikana työssäolon 
         * ajalta maksetusta tai maksettavaksi erääntyneestä palkasta lukuun ottamatta 
         * hätätyöstä ja lain tai sopimuksen mukaisesta ylityöstä maksettavaa korotusta.
         * 
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
         *
         *
         * laskennallista palkkaa kertyi 23 vanhempainvapaa päivältä välillä
         * 1.12.2009-31.12-2009 tätä edeltävän 12 vk aikana oli 374.5 työtuntia,
         * tuntipalkka 11€
         * 
         * => 23*374.5/(5*12)*11=1579.14€
         */
        assertEquals("laskennallinen palkka työssäolon veroiselta ajalta",
                new BigDecimal("1579.14"),
                vlplA.getTyossaOlonVeroisenAjanPalkka().setScale(2,
                        RoundingMode.HALF_UP));
        /**
         * testitiedoston bonus-sarakkeen summa lomanmääräytymiskauden aikana 1013€        
         *
         * tyotunteja 10€ tuntipalkalla 687.75, 11€ tuntipalkalla 642.5
         * 10*687.75+11*642.5+1013=14958
         */
        assertEquals("palkka työssäolon ajalta ilman hätä tai ylitöitä",
                new BigDecimal("14958.00"),
                vlplA.getLomaVuodenAnsiot().setScale(2, RoundingMode.HALF_UP));
        /**
         * TES 20§8)
         * Lomapalkka tai -korvaus on sekä tuntipalkkaisella että suhteutettua kuukausipalkkaa 
         * saavalla jäljempänä esitetystä lomanmääräytymisvuoden ansiosta:
         * • 10 % työsuhteen kestettyä lomanmääräytymisvuoden loppuun (31.3.) mennessä alle vuoden
         * • 12,5 % työsuhteen kestettyä lomanmääräytymisvuoden loppuun (31.3.)
         * mennessä vähintään vuoden.
         */
        assertEquals("korvausprosentti", new BigDecimal("0.125"),
                vlplA.getKorvausProsentti());
        /**
         * TES 20§8) 
         * Lomapalkka tai -korvaus on sekä tuntipalkkaisella että suhteutettua kuukausipalkkaa 
         * saavalla jäljempänä esitetystä lomanmääräytymisvuoden ansiosta:
         * • 10 % työsuhteen kestettyä lomanmääräytymisvuoden loppuun (31.3.) mennessä alle vuoden
         * • 12,5 % työsuhteen kestettyä lomanmääräytymisvuoden loppuun (31.3.)
         * mennessä vähintään vuoden.
         * 
         * TES20§9). Lomanmääräytymisvuoden (1.4.–31.3.) ansio muodostuu:
         * • työssäoloajan palkasta lukuun ottamatta hätä- ja ylityön ajalta maksettua
         * palkkaa
         * (• poissaoloajalta maksetusta palkasta (7 § 5. kohta ja 17–18 §) ja)
         * • laskennallisesta palkasta.
         * 
         * Lomanmääräytymisvuoden ansio työssäolon ajalta ilman yli- ja
         * hätätöiden korotusosia sisältäen bonukset 14958.00 € Työssäolon
         * veroiselta ajalta saamatta jäänyt palkka (laskennallinen palkka) 1579.14 € Korvausprosentti
         * 0.125 % Prosenttiperusteinen lomapalkka ( 14958.00 + 1579.14 ) * 0.125 = 2067.14
         * Poissaoloajan palkkaa ei ole käsitelty, sillä se koskee mm sairauspoissaoloja, joita
         * ei testitapauksissa ole.
         */
        assertEquals("Lomapalkka", new BigDecimal("2067.14"),
                vlplA.getLomaPalkka());
        /**
         * mahdolliset lomarahan maksuedellytykset:
         * TES 21§ 2-9 
         * 
         * tehtävänannon tiedot eivät riitä lomarahaoikeuden selvittämiseen
         */
        assertEquals("Lomarahan maksuedellytys testitapaus A",
                LomarahanMaksuedellytys.eiAsetettu,
                vlplA.getLomarahanMaksuedellytys());
        /**
         * TES 21§1)
         * Lomaraha on 50 % vuosilomalain mukaan ansaittua lomaa vastaavasta lomapalkasta.
         */
        assertEquals("Lomarahan suuruus testitapaus A",
                new BigDecimal("1033.57"), vlplA.getLomarahanSuuruus());
    }


    /**
     * tehtävänannon testitapaus B
     */
    @Test
    public void testitapausB() {
        var tstB = new TyoSuhdeTiedot(LocalDate.of(2008, 6, 1), th,
                SopimusTyyppi.tuntiPalkkainen, new BigDecimal("37.5"));
        var vllB = new VuosilomaLaskuri(tstB, vle);
        var vlplB = vllB.laskePalkkalaskelma(2010);
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
         * Viikkotuntien lukumäärästä voidaan päätellä, että 14 päivän sääntö
         * toteutuu.
         */
        assertEquals("ansaintasääntö", vlplB.getAnsaintaSaanto(),
                AnsaintaSaanto.Yli14PvKuukaudessa);
        /**
         * TES 20§8) 
         * Lomapalkka tai -korvaus on sekä tuntipalkkaisella että suhteutettua kuukausipalkkaa 
         * saavalla jäljempänä esitetystä lomanmääräytymisvuoden ansiosta:
         * • 10 % työsuhteen kestettyä lomanmääräytymisvuoden loppuun (31.3.) mennessä alle vuoden
         * • 12,5 % työsuhteen kestettyä lomanmääräytymisvuoden loppuun (31.3.)
         * mennessä vähintään vuoden.
         * 
         * (vastaa vuosilomalain prosenttiperusteista
         * lomapalkkaa)
         * 
         * Tuntipalkkaisena tapaus B päätyy siis tessin mukaan prosenttiperusteiseen lomapalkan
         * laskemistapaan. Vuosilomalain mukaisesti tapaus B päätyisi tuntipalkkaiseen, mikä on
         * testitapaus C.
         */
        assertEquals("lomapalkan laskutapa",
                LomapalkanLaskutapa.Prosenttiperusteinen,
                vlplB.getLomapalkanLaskutapa());
        /**
         * Vuosilomalaki 5§
         * Työntekijällä on oikeus saada lomaa kaksi ja puoli arkipäivää 
         * kultakin täydeltä lomanmääräytymiskuukaudelta. Jos työsuhde on 
         * lomanmääräytymisvuoden loppuun mennessä jatkunut yhdenjaksoisesti 
         * alle vuoden, työntekijällä on kuitenkin oikeus saada lomaa kaksi 
         * arkipäivää kultakin täydeltä lomanmääräytymiskuukaudelta. Loman pituutta 
         * laskettaessa päivän osa pyöristetään täyteen lomapäivään.
         * 
         * täysien lomanmääräytymiskuukausien lukumäärä 14pv säännöllä = 10
         */
        assertEquals("Lomapäiväkerroin", new BigDecimal("2.5"),
                vlplB.getLomaPaivaKerroin());
        assertEquals("Lomapäivien lukumäärä", 25, vlplB.getLomapaivienLkm());
        /**
         * Vuosilomalaki 12§
         * Muun kuin viikko- tai kuukausipalkalla alle 14 päivänä kalenterikuukaudessa 
         * työtä tekevän työntekijän vuosilomapalkka on 9 prosenttia taikka työsuhteen 
         * jatkuttua lomakautta edeltävän lomanmääräytymisvuoden loppuun mennessä 
         * vähintään vuoden 11,5 prosenttia lomanmääräytymisvuoden aikana työssäolon 
         * ajalta maksetusta tai maksettavaksi erääntyneestä palkasta lukuun ottamatta 
         * hätätyöstä ja lain tai sopimuksen mukaisesta ylityöstä maksettavaa korotusta.
         * 
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
         *
         *
         * säännöllinen viikkotyöaika 37.5, tehtävänannon mukainen tuntipalkka 11
         * ja työn veroisten vapaapäivien lukumäärä 23
         * 
         * => 23*37.5/5*11=1897.50€
         */
        assertEquals(
                "testitapaus B laskennallinen palkka työssäolon veroiselta ajalta",
                new BigDecimal("1897.50"),
                vlplB.getTyossaOlonVeroisenAjanPalkka().setScale(2,
                        RoundingMode.HALF_UP));
        /**
         * testitiedoston bonus-sarakkeen summa lomanmääräytymiskauden aikana 1013€        
         *
         * tyotunteja 10€ tuntipalkalla 687.75, 11€ tuntipalkalla 642.5
         * 10*687.75+11*642.5+1013=14958
         */
        assertEquals("testitapaus B palkka työssäolon ajalta",
                new BigDecimal("14958.00"),
                vlplB.getLomaVuodenAnsiot().setScale(2, RoundingMode.HALF_UP));
        /**
         * TES 20§8)
         * Lomapalkka tai -korvaus on sekä tuntipalkkaisella että suhteutettua kuukausipalkkaa 
         * saavalla jäljempänä esitetystä lomanmääräytymisvuoden ansiosta:
         * • 10 % työsuhteen kestettyä lomanmääräytymisvuoden loppuun (31.3.) mennessä alle vuoden
         * • 12,5 % työsuhteen kestettyä lomanmääräytymisvuoden loppuun (31.3.)
         * mennessä vähintään vuoden.
         */
        assertEquals("korvausprosentti, testitapaus B", new BigDecimal("0.125"),
                vlplB.getKorvausProsentti());
        /**
         * TES 20§8) 
         * Lomapalkka tai -korvaus on sekä tuntipalkkaisella että suhteutettua kuukausipalkkaa 
         * saavalla jäljempänä esitetystä lomanmääräytymisvuoden ansiosta:
         * • 10 % työsuhteen kestettyä lomanmääräytymisvuoden loppuun (31.3.) mennessä alle vuoden
         * • 12,5 % työsuhteen kestettyä lomanmääräytymisvuoden loppuun (31.3.)
         * mennessä vähintään vuoden.
         * 
         * TES20§9). Lomanmääräytymisvuoden (1.4.–31.3.) ansio muodostuu:
         * • työssäoloajan palkasta lukuun ottamatta hätä- ja ylityön ajalta maksettua
         * palkkaa
         * (• poissaoloajalta maksetusta palkasta (7 § 5. kohta ja 17–18 §) ja)
         * • laskennallisesta palkasta.
         * 
         * Lomanmääräytymisvuoden ansio työssäolon ajalta ilman yli- ja
         * hätätöiden korotusosia sisältäen bonukset 14958.00 € Työssäolon
         * veroiselta ajalta saamatta jäänyt palkka (laskennallinen palkka) 1897.50 € Korvausprosentti
         * 0.125 % Prosenttiperusteinen lomapalkka ( 14958.00 + 1897.50 ) * 0.125 = 2106.94
         * Poissaoloajan palkkaa ei ole käsitelty, sillä se koskee mm sairauspoissaoloja, joita
         * ei testitapauksissa ole.
         */
        assertEquals("Lomapalkka testitapaus B", new BigDecimal("2106.94"),
                vlplB.getLomaPalkka());
        /**
         * mahdolliset lomarahan maksuedellytykset:
         * TES 21§ 2-9 
         * 
         * tehtävänannon tiedot eivät riitä lomarahaoikeuden selvittämiseen
         */
        assertEquals("Lomarahan maksuedellytys testitapaus B",
                LomarahanMaksuedellytys.eiAsetettu,
                vlplB.getLomarahanMaksuedellytys());
        /**
         * TES 21§1)
         * Lomaraha on 50 % vuosilomalain mukaan ansaittua lomaa vastaavasta lomapalkasta.
         */
        assertEquals("Lomarahan suuruus testitapaus B",
                new BigDecimal("1053.47"), vlplB.getLomarahanSuuruus());
    }


    /**
     * testitapaus B lisäoletuksella että lomapalkka lasketaan 
     * tessin sijaan vuosilomalain mukaan, mikä johtaa vuosilomalain
     * 11§ määrittelemään lomapalkan laskutapaan.
     */
    @Test
    public void testitapausC() {
        var tstC = new TyoSuhdeTiedot(LocalDate.of(2008, 6, 1), th,
                SopimusTyyppi.tuntiPalkkainen, new BigDecimal("37.5"));
        tstC.setLomapalkanLaskutapa(LomapalkanLaskutapa.TuntiPalkka);
        var vllC = new VuosilomaLaskuri(tstC, vle);
        var vlplC = vllC.laskePalkkalaskelma(2010);
        /**
         * Vuosilomalaki 6§ 
         * Jos työntekijä on sopimuksen mukaisesti työssä 
         * niin harvoina päivinä, että hänelle ei tästä syystä kerry 
         * ainoatakaan 14 työssäolopäivää sisältävää kalenterikuukautta tai 
         * vain osa kalenterikuukausista sisältää 14 työssäolopäivää, täydeksi 
         * lomanmääräytymiskuukaudeksi katsotaan sellainen kalenterikuukausi, 
         * jonka aikana työntekijälle on kertynyt vähintään 35 työtuntia tai 
         * 7 §:ssä tarkoitettua työssäolon veroista tuntia.
         */
        assertEquals("ansaintasääntö testitapaus C", vlplC.getAnsaintaSaanto(),
                AnsaintaSaanto.Yli14PvKuukaudessa);
        /**
         * Vuosilomalaki 11§
         * Muun kuin viikko- tai kuukausipalkalla työskentelevän 
         * sellaisen työntekijän vuosilomapalkka, joka sopimuksen 
         * mukaan työskentelee vähintään 14 päivänä kalenterikuukaudessa, 
         * lasketaan kertomalla hänen keskipäiväpalkkansa lomapäivien määrän 
         * perusteella määräytyvällä kertoimella ...
         * 
         * koodissa laskutapa on nimellä TuntiPalkka
         */
        assertEquals("lomapalkan lakutapa testitapaus C",
                LomapalkanLaskutapa.TuntiPalkka,
                vlplC.getLomapalkanLaskutapa());
        /**
         * Vuosilomalaki 5§
         * Työntekijällä on oikeus saada lomaa kaksi ja puoli arkipäivää 
         * kultakin täydeltä lomanmääräytymiskuukaudelta. Jos työsuhde on 
         * lomanmääräytymisvuoden loppuun mennessä jatkunut yhdenjaksoisesti 
         * alle vuoden, työntekijällä on kuitenkin oikeus saada lomaa kaksi 
         * arkipäivää kultakin täydeltä lomanmääräytymiskuukaudelta. Loman pituutta 
         * laskettaessa päivän osa pyöristetään täyteen lomapäivään.
         */
        assertEquals("Lomapäiväkerroin", new BigDecimal("2.5"),
                vlplC.getLomaPaivaKerroin());
        assertEquals("Lomapäivien lukumäärä", 25, vlplC.getLomapaivienLkm());
        /**
         * Vuosilomalaki 11§
         * Keskipäiväpalkka lasketaan siten, että lomanmääräytymisvuoden aikana 
         * työssäolon ajalta työntekijälle maksettu tai maksettavaksi erääntynyt 
         * palkka, hätätyöstä ja lain tai sopimuksen mukaisesta ylityöstä peruspalkan 
         * lisäksi maksettavaa korotusta lukuun ottamatta, jaetaan lomanmääräytymisvuoden 
         * aikana tehtyjen työpäivien määrällä, johon lisätään laissa säädetyn 
         * vuorokautisen säännöllisen työajan tai, jos laissa ei ole säädetty säännöllisen 
         * vuorokautisen työajan enimmäismäärää, sopimuksessa sovitun säännöllisen 
         * työajan lisäksi tehtyjen työtuntien kahdeksasosa.
         * 
         * tyotunteja 10€ tuntipalkalla 687.75, 11€ tuntipalkalla 642.5
         * 10*687.75+11*642.5=13945
         * 
         * tavallisia työpäiviä 196, ylityötunteja 0
         */
        assertEquals("Ansiot ilman yli- ja hätätöiden lisiä",
                new BigDecimal("13945.00"), vlplC.getAnsiotIlmanYliTaiHata()
                        .setScale(2, RoundingMode.HALF_UP));
        assertEquals("Työpäivät ja kahdeksasosa ylitöistä", new BigDecimal(196),
                vlplC.getLaskennallisiaTyopaivia().setScale(0,
                        RoundingMode.HALF_UP));
        assertEquals("Viikottaisien työpäivien määrä jaettuna viidellä",
                new BigDecimal(1),
                vlplC.getViikottaisienTyopaivienMaaraJaettunaViidella()
                        .setScale(0, RoundingMode.HALF_UP));
        /**
         * tyotunteja 10€ tuntipalkalla 687.75, 11€ tuntipalkalla 642.5 =>
         * ansiot 10*687.75+11*642.5=13945 tavallisia työpäiviä 196,
         * ylityötunteja 0
         * 
         * 13945/196=71.15
         */
        assertEquals("Keskimääräinen päiväpalkka", new BigDecimal("71.15"),
                vlplC.getKeskimaarainenPaivapalkka().setScale(2,
                        RoundingMode.HALF_UP));
        /**
         * vuosilomalain 11§ taulukosta kun lomapäiviä on 25 => kerroin 23.2
         */
        assertEquals("Vuosilomalain mukainen kerroin", new BigDecimal("23.2"),
                vlplC.getTuntipalkkaisenLomapalkkaKerroin().setScale(1,
                        RoundingMode.HALF_UP));
        /**
         * Lomanmääräytymisvuoden ansio työssäolon ajalta ilman yli- ja
         * hätätöiden korotusosia tai bonuksia 13945.00 € Tehdyt työpäivät + 1/8
         * ylitöistä 196.00 Keskimääräinen päiväpalkka 13945.00 : 196.00 = 71.15
         * € Viikoittaisten työpäivien määrä jaettuna viidellä 1 Vuosilomalain
         * mukainen kerroin 23.2 tuntipalkkaperusteinen lomaPalkka 71.15 * 1 * 23.2 = 1650.63
         */
        assertEquals("Lomapalkka testitapaus C", new BigDecimal("1650.63"),
                vlplC.getLomaPalkka());
        /**
         * mahdolliset lomarahan maksuedellytykset:
         * TES 21§ 2-9 
         * 
         * tehtävänannon tiedot eivät riitä lomarahaoikeuden selvittämiseen
         */
        assertEquals("Lomarahan maksuedellytys testitapaus C",
                LomarahanMaksuedellytys.eiAsetettu,
                vlplC.getLomarahanMaksuedellytys());
        /**
         * TES 21§1)
         * Lomaraha on 50 % vuosilomalain mukaan ansaittua lomaa vastaavasta lomapalkasta.
         */
        assertEquals("Lomarahan suuruus testitapaus C",
                new BigDecimal("825.32"), vlplC.getLomarahanSuuruus());
    }
}
