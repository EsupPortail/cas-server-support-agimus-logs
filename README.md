# cas-server-support-agimus-logs
![Esup Portail](https://www.esup-portail.org/sites/default/files/logo-esupportail_1.png "Esup Portail")

cas-server-support-agimus-logs est une extension du serveur CAS pour produire 2 fichiers de log.

-------------------------

Le premier "serviceStats.log" contenant un ligne pour chaque délivrance de ticket CAS pour un service à un utilisateur

Le fichier produit est de la forme : 

> [DATE] [IP:XXX.XXX.XXX.XXX] [ID:userId] [TICKET:ST-X-YYYYYYYYYYYYY] [SERVICE:http://service.univ.fr] [USER-AGENT:Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:58.0) Gecko/20100101 Firefox/58.0]

-------------------------

Le second "auth.log" contenant un ligne pour chaque authentification d'un utilisateur

Le fichier produit est de la forme : 

> DATE - AUTHENTICATION_SUCCESS|AUTHENTICATION_FAILED for '[username: userId]' from 'XXX.XXX.XXX.XXX'

-------------------------

Le troisième "cas-audit.log" contenant une ligne pour chaque action faite pendant l'authentification à un service

Le fichier produit est de la forme :

> DATE|userID|{service=http://service.univ.fr, ticketId=ST-X-YYYYYYYYYYYYYYYYYYYYYYYYYYY}|SERVICE_TICKET_CREATED|XXX.XXX.XXX.XXX|XXX.XXX.XXX.XXX|
> DATE|userID|{ticket=ST-X-YYYYYYYYYYYYYYYYYYYYYYYYYYY, service=http://service.univ.fr}|SERVICE_TICKET_VALIDATE_SUCCESS|XXX.XXX.XXX.XXX|XXX.XXX.XXX.XXX|

-------------------------

## Compatibilité

Testé sur 

 - CAS V7.1.6


## Installation

-------------------------

    git clone https://github.com/EsupPortail/cas-server-support-agimus-logs.git
    cd cas-server-support-agimus-logs
    mvn clean package install

-------------------------

Ceci va avoir pour effet de compiler le projet et de le mettre dans votre repository local maven.

## Intégration dans CAS

Ajouter la dépendance dans build.gradle de cas-overlay-template (https://github.com/apereo/cas-overlay-template) : 

    dependencies {
      ...
      implementation "org.esupportail.cas:cas-server-support-agimus-logs:${casServerVersion}"
    }

Ajouter le logger dans cas-overlay-template/etc/cas/config/log4j2.xml

    <Appenders>
        ...
        <RollingFile name="agimusStatslogfile" fileName="${baseDir}/serviceStats.log" append="true"
                     filePattern="${baseDir}/serviceStats-%d{yyyy-MM-dd}.log.gz"
                     immediateFlush="false">
                <PatternLayout pattern="%m%n" />
                <Policies>
                        <!-- Rotation quotidienne -->
                        <TimeBasedTriggeringPolicy interval="1" modulate="true"/>
                </Policies>
                <DefaultRolloverStrategy max="5" compressionLevel="9">
                        <Delete basePath="${baseDir}" maxDepth="2">
                                <IfFileName glob="*/*.log.gz" />
                                <IfLastModified age="7d" />
                        </Delete>
                </DefaultRolloverStrategy>
        </RollingFile>

        <RollingFile name="authlogfile" fileName="${baseDir}/auth.log" append="true"
                     filePattern="${baseDir}/auth-%d{yyyy-MM-dd}.log.gz"
                     immediateFlush="false">
                <PatternLayout pattern="%m%n" />
                <Policies>
                        <!-- Rotation quotidienne -->
                        <TimeBasedTriggeringPolicy interval="1" modulate="true"/>
                </Policies>
                <DefaultRolloverStrategy max="5" compressionLevel="9">
                        <Delete basePath="${baseDir}" maxDepth="2">
                                <IfFileName glob="*/*.log.gz" />
                                <IfLastModified age="7d" />
                        </Delete>
                </DefaultRolloverStrategy>
        </RollingFile>

        <RollingFile name="auditlogfile" fileName="${baseDir}/cas-audit.log" append="true"
                 filePattern="${baseDir}/cas-audit-%d{yyyy-MM-dd}.log.gz"
                 immediateFlush="false">
            <PatternLayout pattern="|%d{yyyy-MM-dd HH:mm:ss,Europe/Paris}|%m%n" />
            <Policies>
                <!-- Rotation quotidienne -->
                <TimeBasedTriggeringPolicy interval="1" modulate="true"/>
            </Policies>
            <DefaultRolloverStrategy max="5" compressionLevel="9">
                <Delete basePath="${baseDir}" maxDepth="2">
                    <IfFileName glob="*/*.log.gz" />
                    <IfLastModified age="7d" />
                </Delete>
            </DefaultRolloverStrategy>
        </RollingFile>
        ...
        ...
        <CasAppender name="casStatsAgimus">
            <AppenderRef ref="agimusStatslogfile" />
        </CasAppender>
        <CasAppender name="casAuthlogfile">
            <AppenderRef ref="authlogfile" />
        </CasAppender>
        <CasAppender name="casAudit">
            <AppenderRef ref="auditlogfile" />
        </CasAppender>
        ...
    </Appenders>
    <Loggers>
        ...
        <Logger name="org.esupportail.cas.util.CasAgimusServicesAuditLogger" level="info" additivity="false" includeLocation="true">
                <AppenderRef ref="casStatsAgimus"/>
        </Logger>

        <Logger name="org.esupportail.cas.util.CasAgimusAuthAuditLogger" level="info" additivity="false" includeLocation="true">
                <AppenderRef ref="casAuthlogfile"/>
        </Logger>

        <Logger name="org.apereo.inspektr" additivity="false" level="info">
            <AppenderRef ref="casAudit"/>
        </Logger>
        ...
    </Loggers>
    
Il suffit ensuite de lancer la compilation et le déploiement de CAS.
