package org.esupportail.cas.config;

import org.apereo.cas.audit.AuditTrailExecutionPlanConfigurer;
import org.apereo.inspektr.audit.AuditTrailManager;
import org.esupportail.cas.audit.AgimusServicesAuditTrailManager;
import org.esupportail.cas.util.CasAgimusAuthAuditLogger;
import org.esupportail.cas.util.CasAgimusServicesAuditLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ScopedProxyMode;

/**
 * This is {@link CasAgimusAuditConfiguration}.
 *
 * @author Julien Marchal
 * @since 5.2.0
 */
@Configuration("casAgimusAuditConfiguration")
public class CasAgimusAuditConfiguration {
	private static final Logger LOGGER = LoggerFactory.getLogger(CasAgimusAuditConfiguration.class);

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
    @RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
    @ConditionalOnMissingBean(name = "agimusServicesAuditTrailManager")
    public AuditTrailManager agimusServicesAuditTrailManager() {
        LOGGER.debug("CasAgimusAuditConfiguration::agimusServicesAuditTrailManager constructor");
		return new AgimusServicesAuditTrailManager();
    }
    
    @Bean
    @RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
    public AuditTrailExecutionPlanConfigurer agimusServicesAuditTrailExecutionPlanConfigurer(@Qualifier("agimusServicesAuditTrailManager") final AuditTrailManager agimusServicesAuditTrailManager) {
        return plan -> plan.registerAuditTrailManager(agimusServicesAuditTrailManager);
    }


}
