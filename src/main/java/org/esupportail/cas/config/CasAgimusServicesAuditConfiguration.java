package org.esupportail.cas.config;

import org.apereo.cas.audit.spi.DefaultDelegatingAuditTrailManager;
import org.apereo.cas.audit.spi.DelegatingAuditTrailManager;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.inspektr.audit.AuditTrailManager;
import org.esupportail.cas.audit.AgimusServicesAuditTrailManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This is {@link CasSupportAgimusAuditConfiguration}.
 *
 * @author Julien Marchal
 * @since 5.2.0
 */
@Configuration("casAgimusLogsAuditConfiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
public class CasAgimusServicesAuditConfiguration {
	private static final Logger LOGGER = LoggerFactory.getLogger(CasAgimusServicesAuditConfiguration.class);

    @Autowired
    private CasConfigurationProperties casProperties;

    @Bean
    public AuditTrailManager agimusServicesAuditTrailManager() {
        LOGGER.debug("CasAgimusServicesAuditConfiguration::agimusServicesAuditTrailManager constructor");
		return new AgimusServicesAuditTrailManager();
    }

    @Bean
    public DelegatingAuditTrailManager auditTrailManager() {
        LOGGER.debug("CasAgimusServicesAuditConfiguration::auditTrailManager create object agimusServicesAuditTrailManager");
		return new DefaultDelegatingAuditTrailManager(agimusServicesAuditTrailManager());
    }

}
