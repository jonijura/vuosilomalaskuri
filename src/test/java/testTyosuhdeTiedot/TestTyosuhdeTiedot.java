package testTyosuhdeTiedot;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import org.junit.Test;

import tiedostonKasittely.TiedostonKasittelija;
import tyosuhdeTiedot.TyoHistoria;
import tyosuhdeTiedot.TyoSuhdeTiedot;
import tyosuhdeTiedot.TyoSuhdeTiedot.AnsaintaSaanto;

/**
 * @author Joona1
 * @version 31.7.2022
 *
 */
public class TestTyosuhdeTiedot {

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
    public void testaaOnkoTaysiLomanMaaraytymisKuukausi() {
        String[] testikk = new String[] { "1.10.2009|||9-15|6|6|6|",
                "5.10.2009|||9-15|6|6|6|", "7.10.2009|||9-16:30|7,5|7,5|7,5|",
                "8.10.2009|||9-16:30|7,5|7,5|7,5|",
                "10.10.2009|||9-16:30|7,5|7,5|7,5|", "12.10.2009|||9-15|6|6|6|",
                "13.10.2009|||9-16:30|7,5|7,5|7,5|",
                "14.10.2009|||9-16:30|7,5|7,5|7,5|",
                "15.10.2009|||9-16:30|7,5|7,5|7,5|",
                "16.10.2009|||9-16:30|7,5|7,5|7,5|",
                "17.10.2009|||9-16:30|7,5|7,5|7,5|", "18.10.2009|||9-15|6|6|6|",
                "19.10.2009|||9-16:30|7,5|7,5|7,5|",
                "20.10.2009|||9-16:30|7,5|7,5|7,5|",
                "24.10.2009|||9-16:30|7,5|7,5|7,5|186,00 €|",
                "25.10.2009|||9-16:30|7,5|7,5|7,5|",
                "26.10.2009|||9-16:30|7,5|7,5|7,5|",
                "27.10.2009|||9-16:30|7,5|7,5|7,5|",
                "28.10.2009|||9-16:30|7,5|7,5|7,5|",
                "31.10.2009|||9-16:30|7,5|7,5|7,5|" };
        TyoSuhdeTiedot tst = new TyoSuhdeTiedot(LocalDate.of(2009, 10, 1),
                new TyoHistoria(testikk));
        tst.setAnsaintaSaanto(AnsaintaSaanto.Yli14PvKuukaudessa);
        boolean b = tst.onkoTaysiLomanMaaraytymisiKK(LocalDate.of(2009, 10, 1));
        assertEquals(true, b);

        testikk = new String[] { "1.10.2009|||9-15|6|6|6|",
                "5.10.2009|||9-15|6|6|6|", "7.10.2009|||9-16:30|7,5|7,5|7,5|",
                "8.10.2009|||9-16:30|7,5|7,5|7,5|",
                "10.10.2009|||9-16:30|7,5|7,5|7,5|", "12.10.2009|||9-15|6|6|6|",
                "13.10.2009|||9-16:30|7,5|7,5|7,5|",
                "14.10.2009|||9-16:30|7,5|7,5|7,5|",
                "15.10.2009|||9-16:30|7,5|7,5|7,5|",
                "16.10.2009|||9-16:30|7,5|7,5|7,5|",
                "17.11.2009|||9-16:30|7,5|7,5|7,5|", "18.10.2009|||9-15|6|6|6|",
                "19.11.2009|||9-16:30|7,5|7,5|7,5|",
                "20.11.2009|||9-16:30|7,5|7,5|7,5|",
                "24.11.2009|||9-16:30|7,5|7,5|7,5|186,00 €|" };
        tst = new TyoSuhdeTiedot(LocalDate.of(2009, 10, 1),
                new TyoHistoria(testikk));
        b = tst.onkoTaysiLomanMaaraytymisiKK(LocalDate.of(2009, 10, 1));
        assertEquals(false, b);

        tst.setAnsaintaSaanto(AnsaintaSaanto.Yli35TuntiaKuukaudessa);
        b = tst.onkoTaysiLomanMaaraytymisiKK(LocalDate.of(2009, 10, 1));
        assertEquals(true, b);

        b = tst.onkoTaysiLomanMaaraytymisiKK(LocalDate.of(2009, 11, 1));
        assertEquals(false, b);
    }


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
     * 
     * Vuosilomalaki 7§
     * ei sisällä hoitovapaata työssäolon veroisena.
     */
    @Test
    public void testaaHoitovapaaLomaMaarittelyKuukausissa() {
        String[] testikk = new String[] { "10.8.2009|Hoitovapaa|",
                "11.8.2009|Hoitovapaa|", "12.8.2009|Hoitovapaa|",
                "13.8.2009|Hoitovapaa|", "14.8.2009|||9-16:30|7,5|7,5|7,5|",
                "17.8.2009|||9-16:30|7,5|7,5|7,5|", "20.8.2009|||9-15|6|6|6|",
                "21.8.2009|||9-16:30|7,5|7,5|7,5|",
                "22.8.2009|||9-16:30|7,5|7,5|7,5|",
                "23.8.2009|||9-13:30|4,5|4,5|4,5|", "24.8.2009|||9-13|4|4|4|",
                "25.8.2009|||9-15:30|6,5|6,5|6,5|", "27.8.2009|||9-16|7|7|7|",
                "28.8.2009|||9-13|4|4|4|",
                "29.8.2009|||9-15:15|6,25|6,25|6,25|" };
        TyoSuhdeTiedot tst = new TyoSuhdeTiedot(LocalDate.of(2009, 10, 1),
                new TyoHistoria(testikk));
        tst.setAnsaintaSaanto(AnsaintaSaanto.Yli14PvKuukaudessa);
        boolean b = tst.onkoTaysiLomanMaaraytymisiKK(LocalDate.of(2009, 8, 1));
        assertEquals(false, b);

        tst.setAnsaintaSaanto(AnsaintaSaanto.Yli35TuntiaKuukaudessa);
        b = tst.onkoTaysiLomanMaaraytymisiKK(LocalDate.of(2009, 8, 1));
        assertEquals(true, b);
    }


    /**
     * Vuosilomalaki 7§ loppu
     * 
     * Työssäolon veroisiksi tunneiksi lasketaan ne tunnit, 
     * jotka työntekijä sopimuksen mukaan ilman poissaoloa olisi ollut työssä. (18.3.2016/182)
     */
    @Test
    public void testaaTyossaolonVeroisetTunnitLomanmaarittelyKuukauksissa() {
        String[] testikk = new String[] { "10.8.2009|vanhempainvapaa|",
                "11.8.2009|vanhempainvapaa|", "12.8.2009|vanhempainvapaa|",
                "13.8.2009|vanhempainvapaa|",
                "14.8.2009|||9-16:30|7,5|7,5|7,5|" };

        TyoSuhdeTiedot tst = new TyoSuhdeTiedot(LocalDate.of(2009, 10, 1),
                new TyoHistoria(testikk), new BigDecimal("0"));
        tst.setAnsaintaSaanto(AnsaintaSaanto.Yli35TuntiaKuukaudessa);
        boolean b = tst.onkoTaysiLomanMaaraytymisiKK(LocalDate.of(2009, 8, 1));
        assertEquals("nollatuntisopimus", false, b);

        tst = new TyoSuhdeTiedot(LocalDate.of(2009, 10, 1),
                new TyoHistoria(testikk), new BigDecimal("37.5"));
        tst.setAnsaintaSaanto(AnsaintaSaanto.Yli35TuntiaKuukaudessa);
        b = tst.onkoTaysiLomanMaaraytymisiKK(LocalDate.of(2009, 8, 1));
        assertEquals("sovittu viikkotyöaika", true, b);
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
                testiKausi);
        tst.setAnsaintaSaanto(AnsaintaSaanto.Yli14PvKuukaudessa);
        BigDecimal tulos = tst.laskennallinenPalkka(testiKausi);
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
                new TyoHistoria(testiKausiData), viikkoTyoAika);
        tst.setAnsaintaSaanto(AnsaintaSaanto.Yli14PvKuukaudessa);
        var tulos = tst.laskennallinenPalkka(testiKausi);
        assertEquals("Laskennallinen palkka viikkotyöajalla",
                new BigDecimal(testiKausiData.length).multiply(viikkoTyoAika)
                        .multiply(new BigDecimal("0.2")).multiply(tuntiPalkka),
                tulos);

        TyoHistoria th = TiedostonKasittelija
                .lueTyoHistoria("vuosiloma_vuositunnit2.txt");
        tst = new TyoSuhdeTiedot(LocalDate.of(2009, 10, 1), th);

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
                viikkoTyoAika);
        tst.setAnsaintaSaanto(AnsaintaSaanto.Yli14PvKuukaudessa);
        tulos = tst.laskennallinenPalkka(testiKausi);
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
        var tst = new TyoSuhdeTiedot(LocalDate.of(2009, 10, 1), th);
        tst.setAnsaintaSaanto(AnsaintaSaanto.Yli35TuntiaKuukaudessa);
        var tulos = tst.laskennallinenPalkka(testiKausi);
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
        tst = new TyoSuhdeTiedot(LocalDate.of(2008, 10, 1), th);
        tst.setAnsaintaSaanto(AnsaintaSaanto.Yli35TuntiaKuukaudessa);
        tulos = tst.laskennallinenPalkka(testiKausi);
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
