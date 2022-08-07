/**
 * 
 */
package lomaLakiJaEhdot;

/**
 * @author Joona1
 * @version 5.8.2022
 *
 */
public enum TyoMerkinnanTyyppi {

    /**
     * 
     */
    tavallinenTyopaiva,
    /**
     * 
     */
    vanhempainvapaa,
    /**
     * 
     */
    Arkipyhakorvaukset,
    /**
     * 
     */
    Hoitovapaa,
    /**
     * 
     */
    tasausPaiva;

    /**
     * @param arvo
     * @return
     */
    public static TyoMerkinnanTyyppi parsi(String arvo) {
        switch (arvo) {
        case "vanhempainvapaa":
            return vanhempainvapaa;
        case "Arkipyhäkorvaukset":
            return Arkipyhakorvaukset;
        case "Hoitovapaa":
            return Hoitovapaa;
        case "tavallinenTyopaiva":
            return tavallinenTyopaiva;
        case "":
            return tavallinenTyopaiva;
        case "tasausPaiva":
            return tasausPaiva;
        default:
            throw new RuntimeException("tuntematon työmerkinnän tyyppi");
        }
    }
}
