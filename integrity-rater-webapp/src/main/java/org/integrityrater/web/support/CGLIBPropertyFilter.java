package org.integrityrater.web.support;

import net.sf.json.util.PropertyFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CGLIBPropertyFilter implements PropertyFilter {

    private Logger logger = LoggerFactory.getLogger(CGLIBPropertyFilter.class);
    
    public boolean apply(Object source, String name, Object value) {
        //logger.debug(String.format("Testing property %s on %s", name, source.getClass().getName()));
        if (name.startsWith("hibernateLazyInitializer") || name.startsWith("CGLIB$")) {
            logger.debug(String.format("Skipping CGLIB property %s on %s", name, source.getClass().getName()));
            return true;
        }
        return false;
    }

}
