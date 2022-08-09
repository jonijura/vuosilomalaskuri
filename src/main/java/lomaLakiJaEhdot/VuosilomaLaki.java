package lomaLakiJaEhdot;

import java.math.BigDecimal;

import tyosuhdeTiedot.TyoHistoria;

/**
 * @author Joona1
 * @version 27.7.2022
 *
 */
public class VuosilomaLaki {

    /**
     * Vuosilomalaki 7§ 1) vanhempainvapaata pidetään työssäolopäivien veroisena
     * 
     * Työssäolopäivien veroisina pidetään myös niitä työpäiviä tai työtunteja, 
     * jolloin työntekijä työsuhteen kestäessä on estynyt tekemästä työtä:
     * 
     * 1) työsopimuslain (55/2001) 4 luvun 1 §:ssä tarkoitetun erityisraskausvapaan, 
     * 6 §:ssä tarkoitetun tilapäisen hoitovapaan, 7 §:ssä tarkoitetun pakottavista 
     * perhesyistä johtuvan poissaolon tai 7 b §:ssä tarkoitetun omaishoitovapaan 
     * aikana taikka yhtä synnytyskertaa tai adoptiota kohden raskausvapaaseen 
     * oikeutetun työntekijän yhteensä enintään 160 raskaus- ja vanhempainvapaapäivän 
     * ja vastaavasti muun vanhempainvapaaseen oikeutetun työntekijän 160 
     * vanhempainvapaapäivän aikana; (14.1.2022/34)
     */
    private static TyoMerkinnanTyyppi[] tyossaOloPaivienVeroisia = {
            TyoMerkinnanTyyppi.vanhempainvapaa,
            TyoMerkinnanTyyppi.tavallinenTyopaiva };

    /**
     * Vuosilomalaki 12§ vanhempainvapaata pidetään laskennalliseen palkkaan oikeuttavana
     * poissaolona.
     * 
     * Jos työntekijä on lomanmääräytymisvuoden aikana ollut estynyt tekemästä työtä 
     * 7 §:n 2 momentin 1–4 tai 7 kohdassa tarkoitetusta syystä, vuosilomapalkan perusteena 
     * olevaan palkkaan lisätään laskennallisesti poissaoloajalta saamatta jäänyt palkka 
     * enintään 7 §:n 3 momentissa säädetyltä ajalta.
     */
    private static TyoMerkinnanTyyppi[] laskennalliseenPalkkaanOikeuttavatPoissaolot = {
            TyoMerkinnanTyyppi.vanhempainvapaa };

    /**
     * Vuosilomalaki 5§
     * 
     * Jos työsuhde on lomanmääräytymisvuoden loppuun mennessä jatkunut yhdenjaksoisesti 
     * alle vuoden, työntekijällä on kuitenkin oikeus saada lomaa kaksi arkipäivää kultakin 
     * täydeltä lomanmääräytymiskuukaudelta. 
     */
    protected BigDecimal lomaPaivaKerroinAlleVuosi = new BigDecimal(2);

    /**
     * Vuosilomalaki 5§
     * 
     * Työntekijällä on oikeus saada lomaa kaksi ja puoli arkipäivää kultakin täydeltä lomanmääräytymiskuukaudelta. 
     */
    protected BigDecimal lomaPaivaKerroinYliVuosi = new BigDecimal("2.5");

    /**
     * Vuosilomalaki 12§
     * 
     * Muun kuin viikko- tai kuukausipalkalla alle 14 päivänä kalenterikuukaudessa 
     * työtä tekevän työntekijän vuosilomapalkka on 9 prosenttia taikka työsuhteen 
     * jatkuttua lomakautta edeltävän lomanmääräytymisvuoden loppuun mennessä 
     * vähintään vuoden 11,5 prosenttia
     */
    protected BigDecimal lomapalkkaKerroinAlleVuosi = new BigDecimal("0.09");

    /**
     * Vuosilomalaki 12§
     * 
     * Muun kuin viikko- tai kuukausipalkalla alle 14 päivänä kalenterikuukaudessa 
     * työtä tekevän työntekijän vuosilomapalkka on 9 prosenttia taikka työsuhteen 
     * jatkuttua lomakautta edeltävän lomanmääräytymisvuoden loppuun mennessä 
     * vähintään vuoden 11,5 prosenttia
     */
    protected BigDecimal lomapalkkaKerroinYliVuosi = new BigDecimal("0.115");

    /**
     * vuosilomalaki 11§
     */
    protected BigDecimal[] tuntipalkkaistenLomapalkkakertoimet = {
            BigDecimal.ZERO, BigDecimal.ZERO, new BigDecimal("1.8"),
            new BigDecimal("2.7"), new BigDecimal("3.6"), new BigDecimal("4.5"),
            new BigDecimal("5.4"), new BigDecimal("6.3"), new BigDecimal("7.2"),
            new BigDecimal("8.1"), new BigDecimal("9.0"), new BigDecimal("9.9"),
            new BigDecimal("10.8"), new BigDecimal("11.8"),
            new BigDecimal("12.7"), new BigDecimal("13.6"),
            new BigDecimal("14.5"), new BigDecimal("15.5"),
            new BigDecimal("16.4"), new BigDecimal("17.4"),
            new BigDecimal("18.3"), new BigDecimal("19.3"),
            new BigDecimal("20.3"), new BigDecimal("21.3"),
            new BigDecimal("22.2"), new BigDecimal("23.2"),
            new BigDecimal("24.1"), new BigDecimal("25.0"),
            new BigDecimal("25.9"), new BigDecimal("26.9"),
            new BigDecimal("27.8") };

    /**
     * @param sopimuksenKestoYliVuoden
     * @return
     */
    public BigDecimal getLomapaivaKerroin(boolean sopimuksenKestoYliVuoden) {
        if (sopimuksenKestoYliVuoden)
            return lomaPaivaKerroinYliVuosi;
        return lomaPaivaKerroinAlleVuosi;
    }


    /**
     * @param sopimuksenKestoYliVuoden
     * @return
     */
    public BigDecimal getLomapalkkaKerroin(boolean sopimuksenKestoYliVuoden) {
        if (sopimuksenKestoYliVuoden)
            return lomapalkkaKerroinYliVuosi;
        return lomapalkkaKerroinAlleVuosi;
    }


    /**
     * @param tyyppi
     * @return
     */
    private static boolean onkoTyonVeroinen(TyoMerkinnanTyyppi tyyppi) {
        for (var syy : tyossaOloPaivienVeroisia)
            if (syy.equals(tyyppi))
                return true;
        return false;
    }


    /**
     * Vuosilomalaki 7 §
     * Työssäolon veroisena pidetään työstä poissaoloaikaa, 
     * jolta työnantaja on lain mukaan velvollinen maksamaan 
     * työntekijälle palkan. Työssäolon veroisena pidetään myös aikaa, 
     * jolloin työntekijä on poissa työstä sellaisen työajan tasaamiseksi 
     * annetun vapaan vuoksi, jolla hänen keskimääräinen viikkotyöaikansa 
     * tasataan laissa säädettyyn enimmäismäärään. Saman kalenterikuukauden 
     * aikana viikkotyöajan tasaamiseksi annetuista vapaapäivistä työssäolon 
     * veroisina pidetään kuitenkin vain neljä päivää ylittäviä vapaapäiviä, 
     * jollei vapaata ole annettu yli kuuden arkipäivän pituisena yhtenäisenä 
     * vapaana.
     *
     * @param th
     * @return
     */
    public static int tyonVeroisiaPaivia(TyoHistoria th) {
        if (th.merkintojenLkm() == 0)
            return 0;
        int paivia = 0;
        int tasausPaivia = 0;
        var palaset = th.paloittelePaivanTyypinMukaan();
        for (var pala : palaset) {
            if (onkoTyonVeroinen(pala.merkintojenTyyppi()))
                paivia += pala.merkintojenLkm();
            else if (pala
                    .merkintojenTyyppi() == TyoMerkinnanTyyppi.tasausPaiva) {
                if (pala.merkintojenLkm() >= 6)
                    tasausPaivia += pala.merkintojenLkm() + 4;
                else
                    tasausPaivia += pala.merkintojenLkm();

            }
        }
        if (tasausPaivia > 4)
            paivia += tasausPaivia - 4;
        return paivia;
    }


    /**
     * @param tyyppi
     * @return
     */
    public static boolean onkoOikeutettuLaskennalliseenPalkkaan(
            TyoMerkinnanTyyppi tyyppi) {
        for (var syy : laskennalliseenPalkkaanOikeuttavatPoissaolot)
            if (syy.equals(tyyppi))
                return true;
        return false;
    }


    /**
     * Vuosilomalaki 11§ taulukko
     * Jos lomapäivien määrä on suurempi kuin 30, kerrointa korotetaan luvulla 0,9 lomapäivää kohden.
     * @param lomaPaivia
     * @return
     */
    public BigDecimal getTuntipalkkaisenLomapalkkaKerroin(int lomaPaivia) {
        if (lomaPaivia >= tuntipalkkaistenLomapalkkakertoimet.length)
            return tuntipalkkaistenLomapalkkakertoimet[30].add(
                    new BigDecimal("0.9").multiply(new BigDecimal(lomaPaivia
                            - tuntipalkkaistenLomapalkkakertoimet.length + 1)));
        return tuntipalkkaistenLomapalkkakertoimet[lomaPaivia];
    }

}
