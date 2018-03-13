package org.esupportail.cas.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is {@link CasAgimusServicesAuditLogger}.
 *
 * @author Julien Marchal
 * @since 5.0.0
 */
public class CasAgimusServicesAuditLogger {
	private static final Logger LOGGER = LoggerFactory.getLogger(CasAgimusServicesAuditLogger.class);
   
	
	public CasAgimusServicesAuditLogger() {		
		LOGGER.debug("CasAgimusServicesAuditLogger::CasAgimusServicesAuditLogger : create bean CasAgimusServicesAuditLogger");    	
	}
	
	public void log(String out) {
		LOGGER.info(out);
	}
}
