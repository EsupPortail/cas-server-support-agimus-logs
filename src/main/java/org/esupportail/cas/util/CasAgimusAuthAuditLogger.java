package org.esupportail.cas.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is {@link CasAgimusAuthAuditLogger}.
 *
 * @author Julien Marchal
 * @since 5.0.0
 */
public class CasAgimusAuthAuditLogger {
	private static final Logger LOGGER = LoggerFactory.getLogger(CasAgimusAuthAuditLogger.class);
   
	
	public CasAgimusAuthAuditLogger() {		
		LOGGER.debug("CasAgimusAuthAuditLogger::CasAgimusAuthAuditLogger : create bean CasAgimusAuthAuditLogger");    	
	}
	
	public void log(String out) {
		LOGGER.info(out);
	}
}
