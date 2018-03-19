package org.esupportail.cas.config;

import org.apereo.cas.audit.spi.DefaultDelegatingAuditTrailManager;
import org.apereo.cas.audit.spi.DelegatingAuditTrailManager;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.inspektr.audit.AuditTrailManager;
import org.esupportail.cas.audit.AgimusServicesAuditTrailManager;
import org.esupportail.cas.util.CasAgimusAuthAuditLogger;
import org.esupportail.cas.util.CasAgimusServicesAuditLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This is {@link CasAgimusAuditConfiguration}.
 *
 * @author Julien Marchal
 * @since 5.2.0
 */
@Configuration("casAgimusLogsAuditConfiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
public class CasAgimusAuditConfiguration {
	private static final Logger LOGGER = LoggerFactory.getLogger(CasAgimusAuditConfiguration.class);

    @Autowired
    private CasConfigurationProperties casProperties;

    @Bean
    public CasAgimusServicesAuditLogger agimusServicesAuditLogger() {
    	LOGGER.debug("CasAgimusAuditConfiguration::agimusServicesAuditLogger : Create bean agimusServicesAuditLogger");    	    
        return new CasAgimusServicesAuditLogger();
    }
    
    @Bean
    public CasAgimusAuthAuditLogger agimusAuthAuditLogger() {
    	LOGGER.debug("CasAgimusAuditConfiguration::agimusAuthAuditLogger : Create bean agimusAuthAuditLogger");    	    
        return new CasAgimusAuthAuditLogger();
    }
    
    @Bean
    public AuditTrailManager agimusServicesAuditTrailManager() {
        LOGGER.debug("CasAgimusAuditConfiguration::agimusServicesAuditTrailManager constructor");
		return new AgimusServicesAuditTrailManager();
    }

    @Bean
    public DelegatingAuditTrailManager auditTrailManager() {
        LOGGER.debug("CasAgimusAuditConfiguration::auditTrailManager create object agimusServicesAuditTrailManager");
		return new DefaultDelegatingAuditTrailManager(agimusServicesAuditTrailManager());
    }

}
