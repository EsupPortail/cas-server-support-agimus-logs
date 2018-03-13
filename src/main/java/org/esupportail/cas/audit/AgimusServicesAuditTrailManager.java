package org.esupportail.cas.audit;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apereo.inspektr.audit.AuditActionContext;
import org.apereo.inspektr.audit.AuditTrailManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;



/**
 * This is {@link AgimusServicesAuditTrailManager}.
 *
 * @author Julien Marchal
 * @since 5.2.0
 */
public class AgimusServicesAuditTrailManager implements AuditTrailManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(AgimusServicesAuditTrailManager.class);

    
    public AgimusServicesAuditTrailManager() {
    }

    @Override
    public void record(final AuditActionContext audit) {		
		LOGGER.debug("AgimusAuditTrailManager::record receive record type : [" + audit.getActionPerformed() + "]");
		if(("SERVICE_TICKET_CREATED").equals(audit.getActionPerformed())) {    	
    		String resourceOperatedUpon = audit.getResourceOperatedUpon();
    		if (resourceOperatedUpon.contains(" for ")) {
    			String parts[] = audit.getResourceOperatedUpon().split(" for ");
    			
    			String ticket = parts[0];    		
        		String service = parts[1];
        		
        		ServletRequestAttributes sra = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        	    HttpServletRequest req = sra.getRequest();
        	    String useragent = req.getHeader("User-Agent");
        	    
				
				if(ticket.startsWith("ST-")) {
					String outStr = "";
					outStr+= "["+ audit.getWhenActionWasPerformed() + "] ";
					outStr+= "[IP:"+ audit.getClientIpAddress() + "] ";
					outStr+= "[TICKET:"+ ticket + "] ";
					outStr+= "[SERVICE:"+ service + "] ";
					outStr+= "[USER-AGENT:" + useragent +"]";
					LOGGER.info(outStr);
				}        		
    		}
    	}
    }
	
	@Override
    public Set<AuditActionContext> getAuditRecordsSince(final LocalDate localDate) {
        LOGGER.info("AgimusAuditTrailManager::getAuditRecordsSince");
				
        return new HashSet<>(0);
    }
}
