/**
 * 
 */
package vuosilomalaskuri;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import lomalaki.TyoMerkinnanTyyppi;
import lomalaki.VuosilomaEhdot;
import lomalaki.VuosilomaLaki;
import tyosuhdetiedot.TyoHistoria;
import tyosuhdetiedot.TyoSuhdeTiedot;
import tyosuhdetiedot.TyosuhdeTiedotIF;

/**
 * @author Joona1
 * @version 26.7.2022
 *
 */
public class VuosilomaLaskuri {

    /**
     * 
     */
    private TyosuhdeTiedotIF tyoSuhdeTiedot;

    /**
     * 
     */
    private VuosilomaLaki vuosilomaEhdot;

    /**
     * 
     */
    private VuosilomaPalkkalaskelma vuosilomaPalkkalaskelma;

    /**
     * @param ts
     * @param vle
     */
    public VuosilomaLaskuri(TyoSuhdeTiedot ts, VuosilomaEhdot vle) {
        this.tyoSuhdeTiedot = ts;
        this.vuosilomaEhdot = vle;
        this.vuosilomaPalkkalaskelma = new VuosilomaPalkkalaskelma();
    }


    /**
     * @param vuosi
     * @return
     * 
     * Vuosilomalaki 5§
     * 
     * Työntekijällä on oikeus saada lomaa kaksi ja puoli arkipäivää kultakin täydeltä 
     * lomanmääräytymiskuukaudelta. Jos työsuhde on lomanmääräytymisvuoden loppuun 
     * mennessä jatkunut yhdenjaksoisesti alle vuoden, työntekijällä on kuitenkin 
     * oikeus saada lomaa kaksi arkipäivää kultakin täydeltä lomanmääräytymiskuukaudelta. 
     * Loman pituutta laskettaessa päivän osa pyöristetään täyteen lomapäivään.
     * 
     * Vuosilomalaki 4§1)
     * lomanmääräytymisvuodella 1 päivän huhtikuuta ja 31 päivän maaliskuuta 
     * välistä aikaa nämä päivät mukaan luettuina;
     */
    public int laskeLomapaivat(int vuosi) {
        LocalDate lomakaudenViimeinenPaiva = LocalDate.of(vuosi, 3, 31);
        LocalDate[] lomavuodenKuukaudet = VuosilomaLaki
                .getLomaVuodenKuukaudet(vuosi);
        int lomanMaaraytymiskk = 0;
        for (var kk : lomavuodenKuukaudet)
            if (tyoSuhdeTiedot.onkoTaysiLomanMaaraytymisiKK(kk))
                lomanMaaraytymiskk++;
        boolean sopimuksenKestoYliVuoden = tyoSuhdeTiedot
                .onkoSopimusKestanytYliVuoden(lomakaudenViimeinenPaiva);
        BigDecimal lomapaivaKerroin = vuosilomaEhdot
                .getLomapaivaKerroin(sopimuksenKestoYliVuoden);
        vuosilomaPalkkalaskelma.setLomaPaivaKerroin(lomapaivaKerroin);
        vuosilomaPalkkalaskelma.setTaysiaTyoKuukausia(lomanMaaraytymiskk);
        return (int) (lomapaivaKerroin
                .multiply(new BigDecimal(lomanMaaraytymiskk)))
                        .setScale(0, RoundingMode.HALF_UP).longValue();
    }


    /**
     * @param vuosi
     * @return
     * 
     * Vuosilomalaki 10§
     * Työntekijällä, jonka palkka on sovittu viikolta tai sitä pidemmältä ajalta, on oikeus 
     * saada tämä palkkansa myös vuosiloman ajalta. Jos työntekijälle on lomanmääräytymisvuoden 
     * aikana työssäolon ajalta viikko- tai kuukausipalkan lisäksi maksettu tai erääntynyt 
     * maksettavaksi muuta palkkaa, joka ei määräydy tilapäisten olosuhteiden perusteella, tämän 
     * palkan osuus lasketaan vuosilomapalkkaan siten kuin 11 §:n 1 ja 2 momentissa säädetään.
     * 
     * Lomapalkka lasketaan 12 §:n mukaan myös silloin, kun työntekijän työaika ja vastaavasti 
     * palkka on muuttunut lomanmääräytymisvuoden aikana. Jos muutokset tapahtuvat vasta 
     * lomanmääräytymisvuoden päättymisen jälkeen ennen vuosiloman tai sen osan alkamista, 
     * lomapalkka lasketaan lomanmääräytymisvuoden aikaisen työajan perusteella määräytyvän 
     * viikko- tai kuukausipalkan mukaan.
     * 
     * TES 20§6) Vuosilomapäivän palkka saadaan kuukausipalkasta jakajalla 25.
     * Lomapalkka provision osalta lasketaan vuosilomalain mukaan.
     * Jos työntekijän työaika ja vastaavasti palkka on muuttunut lomanmääräytymisvuoden 
     * aikana ja hän on kuukausipalkkainen lomanmääräytymisvuoden lopussa
     * (31.3.), hänen lomapalkkansa lasketaan tämän pykälän 8.–11. kohdan mukaan.
     * 
     * 8.-11. vastaa vuosilomalain prosenttiperusteista lomapalkkaa.
     */
    public BigDecimal laskeLomaPalkka(int vuosi) {
        switch (tyoSuhdeTiedot.getLomapalkanLaskutapa()) {
        case Prosenttiperusteinen:
            return prosenttiperusteinenLomapalkka(vuosi);
        case ViikkoPalkka:
            return viikkoPalkkaperusteinenLomapalkka(vuosi);
        case TuntiPalkka:
            return tuntiPalkkaperusteinenLomapalkka(vuosi);
        default:
            throw new RuntimeException("lomapalkan laskutapaa "
                    + tyoSuhdeTiedot.getLomapalkanLaskutapa()
                    + " ei ole toteutettu!");
        }
    }


    /**
     * 11 §
     * Keskipäiväpalkkaan perustuva vuosilomapalkka
     * Muun kuin viikko- tai kuukausipalkalla työskentelevän 
     * sellaisen työntekijän vuosilomapalkka, joka sopimuksen 
     * mukaan työskentelee vähintään 14 päivänä kalenterikuukaudessa, 
     * lasketaan kertomalla hänen keskipäiväpalkkansa lomapäivien määrän 
     * perusteella määräytyvällä kertoimella:
     * 
     * Keskipäiväpalkka lasketaan siten, että lomanmääräytymisvuoden 
     * aikana työssäolon ajalta työntekijälle maksettu tai maksettavaksi 
     * erääntynyt palkka, hätätyöstä ja lain tai sopimuksen mukaisesta 
     * ylityöstä peruspalkan lisäksi maksettavaa korotusta lukuun ottamatta, 
     * jaetaan lomanmääräytymisvuoden aikana tehtyjen työpäivien määrällä, 
     * johon lisätään laissa säädetyn vuorokautisen säännöllisen työajan tai, 
     * jos laissa ei ole säädetty säännöllisen vuorokautisen työajan enimmäismäärää, 
     * sopimuksessa sovitun säännöllisen työajan lisäksi tehtyjen työtuntien kahdeksasosa.
     * 
     * Jos työntekijän viikoittaisten työpäivien määrä on sopimuksen mukaan pienempi tai 
     * suurempi kuin viisi, keskipäiväpalkka kerrotaan viikoittaisten työpäivien määrällä 
     * ja jaetaan viidellä.
     * @param vuosi
     * @return
     */
    private BigDecimal tuntiPalkkaperusteinenLomapalkka(int vuosi) {
        LocalDate lomakaudenViimeinenPaiva = LocalDate.of(vuosi, 3, 31);
        LocalDate lomakaudenEnsimmainenPaiva = LocalDate.of(vuosi - 1, 4, 1);
        TyoHistoria valinMerkinnat = tyoSuhdeTiedot.getValinMerkinnat(
                lomakaudenEnsimmainenPaiva, lomakaudenViimeinenPaiva);
        BigDecimal kerroin = vuosilomaEhdot
                .getTuntipalkkaisenLomapalkkaKerroin(laskeLomapaivat(vuosi));
        vuosilomaPalkkalaskelma.setTuntipalkkaisenLomapalkkaKerroin(kerroin);
        BigDecimal tyossaOloajanPalkka = valinMerkinnat.getPalkka()
                .add(valinMerkinnat.getBonukset());
        BigDecimal hataToistaSaatuPalkka = valinMerkinnat
                .getTyyppisetMerkinnat(TyoMerkinnanTyyppi.hatatyo).getPalkka();
        BigDecimal ylitoistaSaatuPalkka = valinMerkinnat
                .getTyyppisetMerkinnat(TyoMerkinnanTyyppi.ylityo).getPalkka();
        int tehtyjenTyopaivienMaara = valinMerkinnat.getTyoPaivienLkm();
        BigDecimal ylityoTunteja = valinMerkinnat
                .getTyyppisetMerkinnat(TyoMerkinnanTyyppi.ylityo)
                .getTehdytTunnit();
        BigDecimal tyoPaiviaViikossa = tyoSuhdeTiedot.getTyopaiviaViikossa();
        BigDecimal palkkaIlmanYliHata = tyossaOloajanPalkka
                .subtract(hataToistaSaatuPalkka.add(ylitoistaSaatuPalkka));
        vuosilomaPalkkalaskelma.setAnsiotIlmanYliTaiHata(palkkaIlmanYliHata);
        BigDecimal laskennallisiaTyopaivia = new BigDecimal(
                tehtyjenTyopaivienMaara)
                        .add(ylityoTunteja.multiply(new BigDecimal("0.125")));
        vuosilomaPalkkalaskelma
                .setLaskennallisiaTyopaivia(laskennallisiaTyopaivia);
        BigDecimal keskimaarainenViikkotyoAika = tyoPaiviaViikossa
                .multiply(new BigDecimal("0.2"));
        return palkkaIlmanYliHata
                .divide(laskennallisiaTyopaivia, 6, RoundingMode.HALF_UP)
                .multiply(keskimaarainenViikkotyoAika).multiply(kerroin);
    }


    /**
     * Vuosilomalaki 10§
     * Työntekijällä, jonka palkka on sovittu viikolta tai sitä pidemmältä ajalta, on oikeus 
     * saada tämä palkkansa myös vuosiloman ajalta. Jos työntekijälle on lomanmääräytymisvuoden 
     * aikana työssäolon ajalta viikko- tai kuukausipalkan lisäksi maksettu tai erääntynyt 
     * maksettavaksi muuta palkkaa, joka ei määräydy tilapäisten olosuhteiden perusteella, tämän 
     * palkan osuus lasketaan vuosilomapalkkaan siten kuin 11 §:n 1 ja 2 momentissa säädetään.
     * 
     * TES 20§ 7)
     * Lomapalkkaan ja -korvaukseen lisätään lomanmääräytymisvuoden aikana maksetuista lisistä:
     * • 10 % työsuhteen kestettyä lomanmääräytymisvuoden loppuun (31.3.) mennessä alle 1 vuoden
     * • 12,5 % työsuhteen kestettyä lomanmääräytymisvuoden loppuun (31.3.)
     * mennessä vähintään 1 vuoden.
     * @param vuosi
     * @return
     */
    private BigDecimal viikkoPalkkaperusteinenLomapalkka(int vuosi) {
        LocalDate lomakaudenViimeinenPaiva = LocalDate.of(vuosi, 3, 31);
        LocalDate lomakaudenEnsimmainenPaiva = LocalDate.of(vuosi - 1, 4, 1);
        TyoHistoria valinMerkinnat = tyoSuhdeTiedot.getValinMerkinnat(
                lomakaudenEnsimmainenPaiva, lomakaudenViimeinenPaiva);
        BigDecimal bonukset = valinMerkinnat.getBonukset();
        BigDecimal tuntiPalkka = tyoSuhdeTiedot
                .viimeisinTuntipalkka(lomakaudenViimeinenPaiva);
        int lomaPaivia = laskeLomapaivat(vuosi);
        boolean sopimuksenKestoYliVuoden = tyoSuhdeTiedot
                .onkoSopimusKestanytYliVuoden(lomakaudenViimeinenPaiva);
        BigDecimal lomapalkkaKerroin = vuosilomaEhdot
                .getLomapalkkaKerroin(sopimuksenKestoYliVuoden);
        BigDecimal viikkoTyoAika = tyoSuhdeTiedot.getViikkotyoAika();
        vuosilomaPalkkalaskelma
                .setViikkopalkka(viikkoTyoAika.multiply(tuntiPalkka));
        vuosilomaPalkkalaskelma.setKorvausProsentti(lomapalkkaKerroin);
        vuosilomaPalkkalaskelma.setBonukset(bonukset);
        return (new BigDecimal(lomaPaivia).multiply(viikkoTyoAika)
                .multiply(new BigDecimal("0.2")).multiply(tuntiPalkka))
                        .add(bonukset.multiply(lomapalkkaKerroin));
    }


    /**
     * @param vuosi
     * @return
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
    private BigDecimal prosenttiperusteinenLomapalkka(int vuosi) {
        LocalDate lomakaudenViimeinenPaiva = LocalDate.of(vuosi, 3, 31);
        LocalDate lomakaudenEnsimmainenPaiva = LocalDate.of(vuosi - 1, 4, 1);
        TyoHistoria valinMerkinnat = tyoSuhdeTiedot.getValinMerkinnat(
                lomakaudenEnsimmainenPaiva, lomakaudenViimeinenPaiva);
        BigDecimal kaudenTyossaoloPalkka = valinMerkinnat.getPalkka();
        BigDecimal bonukset = valinMerkinnat.getBonukset();
        BigDecimal laskennallinenPalkkaVapailta = valinLaskennallinenPalkkaVapailta(
                valinMerkinnat);

        BigDecimal kaudenAnsiot = laskennallinenPalkkaVapailta.add(bonukset)
                .add(kaudenTyossaoloPalkka);
        boolean sopimuksenKestoYliVuoden = tyoSuhdeTiedot
                .onkoSopimusKestanytYliVuoden(lomakaudenViimeinenPaiva);
        BigDecimal lomapalkkaKerroin = vuosilomaEhdot
                .getLomapalkkaKerroin(sopimuksenKestoYliVuoden);
        vuosilomaPalkkalaskelma.setKorvausProsentti(lomapalkkaKerroin);
        vuosilomaPalkkalaskelma
                .setLomaVuodenAnsiot(kaudenTyossaoloPalkka.add(bonukset));
        vuosilomaPalkkalaskelma
                .setTyossaOlonVeroisenAjanPalkka(laskennallinenPalkkaVapailta);
        return kaudenAnsiot.multiply(lomapalkkaKerroin);
    }


    /**
     * @param th 
     * @return
     */
    private BigDecimal valinLaskennallinenPalkkaVapailta(TyoHistoria th) {
        var tyoskentelyKaudet = th.paloittelePaivanTyypinMukaan();
        BigDecimal laskennallinenPalkka = BigDecimal.ZERO;
        for (var kausi : tyoskentelyKaudet) {
            laskennallinenPalkka = laskennallinenPalkka
                    .add(laskennallinenPalkka(kausi));
        }
        return laskennallinenPalkka;
    }


    /**
     * @param kausi
     * @return
     * 
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
     */
    public BigDecimal laskennallinenPalkka(TyoHistoria kausi) {
        if (!VuosilomaLaki.onkoOikeutettuLaskennalliseenPalkkaan(
                kausi.merkintojenTyyppi()))
            return BigDecimal.ZERO;
        BigDecimal paivia = new BigDecimal(kausi.merkintojenLkm());
        if (tyoSuhdeTiedot.getViikkotyoAika() != null) {
            return paivia.multiply(tyoSuhdeTiedot.getViikkotyoAika())
                    .multiply(new BigDecimal("0.2"))
                    .multiply(kausi.getTuntipalkka());
        }
        LocalDate keskiTyoAjanLaskuValiLoppu = kausi.getAlkuPvm().minusDays(1);
        LocalDate keskiTyoAjanLaskuValiAlku = keskiTyoAjanLaskuValiLoppu
                .minusDays(12 * 7 - 1);
        BigDecimal keskimaarainenViikkoTyoAika = tyoSuhdeTiedot
                .getValinMerkinnat(keskiTyoAjanLaskuValiAlku,
                        keskiTyoAjanLaskuValiLoppu)
                .getTyoAika()
                .divide(new BigDecimal(12), 5, RoundingMode.HALF_UP);
        return paivia.multiply(keskimaarainenViikkoTyoAika)
                .multiply(new BigDecimal("0.2"))
                .multiply(kausi.getTuntipalkka());
    }


    /**
     * @param vuosi
     * @return
     */
    public VuosilomaPalkkalaskelma laskePalkkalaskelma(int vuosi) {
        this.vuosilomaPalkkalaskelma = new VuosilomaPalkkalaskelma();
        vuosilomaPalkkalaskelma
                .setAnsaintaSaanto(tyoSuhdeTiedot.selvitaAnsaintaSaanto(vuosi));
        tyoSuhdeTiedot.selvitaLomapalkanLaskutapa();
        vuosilomaPalkkalaskelma.setTyosuhdeTiedot(tyoSuhdeTiedot);
        vuosilomaPalkkalaskelma.setLomaPaivat(laskeLomapaivat(vuosi));
        vuosilomaPalkkalaskelma.setLomaPalkka(laskeLomaPalkka(vuosi));
        return vuosilomaPalkkalaskelma;
    }
}
