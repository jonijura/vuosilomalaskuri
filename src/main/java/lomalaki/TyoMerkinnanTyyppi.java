/**
 * 
 */
package lomalaki;

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
    tasausPaiva,
    /**
     * 
     */
    hatatyo,
    /**
     * 
     */
    ylityo;

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
        case "hätätyö":
            return hatatyo;
        default:
            throw new RuntimeException("tuntematon työmerkinnän tyyppi");
        }
    }


    /**
     * @return
     */
    public boolean onkoMahdollinenTyopaiva() {
        switch (this) {
        case vanhempainvapaa:
            return true;
        case Arkipyhakorvaukset:
            return false;
        case Hoitovapaa:
            return true;
        case tavallinenTyopaiva:
            return true;
        case tasausPaiva:
            return true;
        case hatatyo:
            return true;
        default:
            throw new RuntimeException("tuntematon työmerkinnän tyyppi");
        }
    }
}
