/**
 * 
 */
package tyosuhdeTiedot;

import java.math.BigDecimal;
import java.time.LocalDate;

import tyosuhdeTiedot.TyoSuhdeTiedot.AnsaintaSaanto;
import tyosuhdeTiedot.TyoSuhdeTiedot.LomapalkanLaskutapa;

/**
 * @author Joona1
 * @version 26.7.2022
 *
 */
public interface TyosuhdeTiedotIF {

    /**
     * @return
     */
    public boolean onko14pvKuukaudessa();


    /**
     * @return
     */
    public boolean onkoProsenttiperusteinenLomapalkka();


    /**
     * @param pvm 
     * @return
     */
    public boolean onkoSopimusKestanytYliVuoden(LocalDate pvm);


    /**
     * @param kk
     * @return
     */
    public boolean onkoTaysiLomanMaaraytymisiKK(LocalDate kk);


    /**
     * @return
     */
    public BigDecimal getViikkotyoAika();


    /**
     * @param alku
     * @param loppu
     * @return
     */
    public BigDecimal getValinPalkka(LocalDate alku, LocalDate loppu);


    /**
     * @param alku
     * @param loppu
     * @return
     */
    public BigDecimal getValinBonukset(LocalDate alku, LocalDate loppu);


    /**
     * @param alku
     * @param loppu
     * @return
     */
    public BigDecimal getValinLaskennallinenPalkkaVapailta(LocalDate alku,
            LocalDate loppu);


    /**
     * @param lomakaudenViimeinenPaiva
     * @return
     */
    public BigDecimal viimeisinTuntipalkka(LocalDate lomakaudenViimeinenPaiva);


    /**
     * @return
     */
    public AnsaintaSaanto getAnsaintaSaanto();


    /**
     * @return
     */
    public LomapalkanLaskutapa getLomapalkanLaskutapa();

}
