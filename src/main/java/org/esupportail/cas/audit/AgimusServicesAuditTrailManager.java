package org.esupportail.cas.audit;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apereo.inspektr.audit.AuditActionContext;
import org.apereo.inspektr.audit.AuditTrailManager;
import org.esupportail.cas.util.CasAgimusAuthAuditLogger;
import org.esupportail.cas.util.CasAgimusServicesAuditLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Autowired
    @Qualifier("agimusServicesAuditLogger")
    private CasAgimusServicesAuditLogger agimusServicesAuditLogger;   
    
    @Autowired
    @Qualifier("agimusAuthAuditLogger")
    private CasAgimusAuthAuditLogger agimusAuthAuditLogger;   
    
    
    public AgimusServicesAuditTrailManager() {
    }

    @Override
    public void record(final AuditActionContext audit) {		
		LOGGER.debug("AgimusAuditTrailManager::record receive record type : [" + audit.getActionPerformed() + "]");
		if(("SERVICE_TICKET_CREATED").equals(audit.getActionPerformed())) {    	
			
    		String resourceOperatedUpon = audit.getResourceOperatedUpon();
    		Map<String,String> resourceOperatedUponMap = splitIntoMap(resourceOperatedUpon);
    		
    		if (resourceOperatedUponMap.containsKey("service")) {

				String ticket = "";
				/* key depends on CAS version :
				 - cas<6.6.10 -> return
				 - 6.6.9<cas<7.x -> ticket
				 - 7.x<cas -> ticketId
				 */
				for(String ticketKeyFromCas: Arrays.asList("return", "ticket", "ticketId")) {
					if(resourceOperatedUponMap.containsKey(ticketKeyFromCas)) {
						ticket = resourceOperatedUponMap.get(ticketKeyFromCas);
						break;
					}
				}
        		String service = resourceOperatedUponMap.get("service");  	
        		
        		ServletRequestAttributes sra = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        	    HttpServletRequest req = sra.getRequest();
        	    String useragent = req.getHeader("User-Agent");
        	    
				
				if(ticket.startsWith("ST-")) {
					String outStr = "";
					outStr+= "["+ audit.getWhenActionWasPerformed() + "] ";
					outStr+= "[IP:"+ audit.getClientIpAddress() + "] ";
					outStr+= "[ID:"+ audit.getPrincipal() + "] ";
					outStr+= "[TICKET:"+ ticket + "] ";
					outStr+= "[SERVICE:"+ service + "] ";
					outStr+= "[USER-AGENT:" + useragent +"]";
					agimusServicesAuditLogger.log(outStr);
				}        		
    		}
    	}
		else if(("AUTHENTICATION_SUCCESS").equals(audit.getActionPerformed()) || ("AUTHENTICATION_FAILED").equals(audit.getActionPerformed())) {			
			agimusAuthAuditLogger.log(audit.getWhenActionWasPerformed() + " - " + audit.getActionPerformed() + " for '[username:"+audit.getPrincipal() + "]' from '" + audit.getClientIpAddress() + "'");
		}   	
    }
    
    protected  Map<String,String> splitIntoMap(String value) {
	    value = value.substring(1, value.length()-1);           
	    String[] keyValuePairs = value.split(",");              
	    Map<String,String> map = new HashMap<>();               	
	    for(String pair : keyValuePairs)                       
	    {
	        String[] entry = pair.split("=");                   
	        map.put(entry[0].trim(), entry[1].trim());          
	    }
	    return map;
    }
    
	
	@Override
    public Set<AuditActionContext> getAuditRecordsSince(final LocalDate localDate) {
        LOGGER.debug("AgimusAuditTrailManager::getAuditRecordsSince");
				
        return new HashSet<>(0);
    }

	@Override
	public void removeAll() {
	}
}
