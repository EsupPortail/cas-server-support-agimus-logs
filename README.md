# cas-server-support-agimus-logs
![Esup Portail](https://www.esup-portail.org/sites/esup-portail.org/files/logo-esup%2Baccroche_2.png "Esup Portail")

cas-server-support-agimus-logs est une extension du serveur CAS pour produire un fichier de log contenant un ligne pour chaque délivrance de ticket CAS pour un service à un utilisateur

Le fichier produit est de la forme : 

> [DATE] [IP:XXX.XXX.XXX.XXX] [TICKET:ST-X-YYYYYYYYYYYYY] [SERVICE:http://service.univ.fr] [USER-AGENT:Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:58.0) Gecko/20100101 Firefox/58.0]


## Compatibilité

Tester sur 

 - CAS V5.2.2


## Installation

-------------------------

    git clone https://github.com/EsupPortail/cas-server-support-agimus-logs.git
    cd cas-server-support-agimus-logs
    mvn clean package install

-------------------------

Ceci va avoir pour effet de compiler le projet et de le mettre dans votre repository local maven.

## Intégration dans CAS

Ajouter la dépendance dans pom.xml de cas-overlay-template (https://github.com/apereo/cas-overlay-template) : 

    <dependencies>
      ...
    	<dependency>
    		<groupId>org.esupportail.cas</groupId>
    		<artifactId>cas-server-support-agimus-logs</artifactId>
    		<version>${cas.version}</version>
    	</dependency>
	</dependencies>

Ajouter le logger dans cas-overlay-template/etc/cas/config/log4j2.xml

    <Appenders>
		...
		<File name="agimusStatslogfile" fileName="${sys:cas.log.dir}/serviceStats.log" append="true" immediateFlush="true">
			<PatternLayout>
				<Pattern>%m%n</Pattern>
			</PatternLayout>
		</File>
    	...
    	...
    	<CasAppender name="casStatsAgimus">
			<AppenderRef ref="agimusStatslogfile" />
		</CasAppender>
    	...
    </Appenders>
    <Loggers>
		...
		<AsyncLogger name="org.esupportail.cas.audit" level="info" additivity="false" includeLocation="true">
			<AppenderRef ref="casStatsAgimus"/>
		</AsyncLogger>
		...
    </Loggers>
    
Il suffit ensuite de lancer la compilation est le déploiement de CAS.