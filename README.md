# cas-server-support-agimus-logs

cas-server-support-agimus-logs est une extension du serveur CAS version 5 pour produire un fichier de log contenant un ligne pour chaque délivrance de ticket CAS pour un service à un utilisateur

Le fichier produit est de la forme : 
[DATE] [IP:XXX.XXX.XXX.XXX] [TICKET:ST-X-YYYYYYYYYYYYY] [SERVICE:http://service.univ.fr] [USER-AGENT:Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:58.0) Gecko/20100101 Firefox/58.0]

## installation
git clone https://github.com/EsupPortail/cas-server-support-agimus-logs.git
cd cas-server-support-agimus-logs
mvn clean package install

## intégration

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
    <File name="agimuslogfile" fileName="${sys:cas.log.dir}/serviceStats.log" append="true" immediateFlush="true">
      <PatternLayout>
        <Pattern>%m%n</Pattern>
      </PatternLayout>
    </File>
    ...
    ...
    <CasAppender name="casAgimus">
      <AppenderRef ref="agimuslogfile" />
    </CasAppender>
    ...
</Appenders>
<Loggers>
    ...
    <AsyncLogger name="org.esupportail.cas.audit" level="info" additivity="false" includeLocation="true">
      <AppenderRef ref="casAgimus"/>
    </AsyncLogger>
    ...
</Loggers>