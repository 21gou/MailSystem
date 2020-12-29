package reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Configuration of MailSystem annotations
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME) // Allow runtime lecture
public @interface Config {
    /**
     * Specifies the MailStore to be loaded
     * @return
     */
    String store();

    /**
     * Specifies if MailStore invocation have to be hooked
     * @return
     */
    boolean log();
}
