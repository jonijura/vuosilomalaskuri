/**
 * 
 */
package tyosuhdeTiedot;

import java.math.BigDecimal;
import java.time.LocalDate;

import lomaLakiJaEhdot.TyoMerkinnanTyyppi;
import tyosuhdeTiedot.TyoSuhdeTiedot.AnsaintaSaanto;
import tyosuhdeTiedot.TyoSuhdeTiedot.LomapalkanLaskutapa;
import tyosuhdeTiedot.TyoSuhdeTiedot.LomarahanMaksuedellytys;

/**
 * @author Joona1
 * @version 26.7.2022
 *
 */
public interface TyosuhdeTiedotIF {

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
     * @param tyyppi 
     * @return
     */
    public BigDecimal getValinPalkka(LocalDate alku, LocalDate loppu,
            TyoMerkinnanTyyppi tyyppi);


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
     * @param pvm
     * @return
     */
    public BigDecimal viimeisinTuntipalkka(LocalDate pvm);


    /**
     * @param vuosi 
     * @return
     */
    public LomapalkanLaskutapa getLomapalkanLaskutapa();


    /**
     * @param alku
     * @param loppu
     * @return
     */
    public BigDecimal getValinTyopaivienLkm(LocalDate alku, LocalDate loppu);


    /**
     * @param alku
     * @param loppu
     * @param ylityo
     * @return
     */
    public BigDecimal getValinTuntienLkm(LocalDate alku, LocalDate loppu,
            TyoMerkinnanTyyppi ylityo);


    /**
     * @return
     */
    public BigDecimal getTyopaiviaViikossa();


    /**
     * @return
     */
    public LomarahanMaksuedellytys getLomarahanMaksuedellytys();


    /**
     * @param vuosi
     * @return 
     */
    public AnsaintaSaanto selvitaAnsaintaSaanto(int vuosi);

}
