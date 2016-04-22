FROM    ubuntu:14.04

ENV DEBIAN_FRONTEND noninteractive
#prevent apt from installing recommended packages
RUN echo 'APT::Install-Recommends "false";' > /etc/apt/apt.conf.d/docker-no-recommends && \
    echo 'APT::Install-Suggests "false";' >> /etc/apt/apt.conf.d/docker-no-recommends

# Install java and tomcat
RUN     apt-get update && apt-get install -y tomcat7 openjdk-7-jdk libyaml-perl && \
        rm -rf /var/lib/tomcat7/webapps/* && \
        rm -rf /var/lib/apt/lists/*

ENV     JAVA_HOME /usr/lib/jvm/java-7-openjdk-amd64
ENV     CATALINA_HOME /usr/share/tomcat7
ENV     CATALINA_BASE /var/lib/tomcat7

ENV CATALINA_OPTS=" -Djava.security.egd=file:/dev/./urandom"
ENV PATH $CATALINA_HOME/bin:$PATH

COPY tomcat/server.xml $CATALINA_BASE/conf/
COPY grails-app/conf/config.yml /conf/config.yml
WORKDIR /usr/local/bin/
COPY tomcat/yml.pl ./
COPY tomcat/start.sh ./

# fix missing folders in tomcat
RUN     mkdir $CATALINA_BASE/temp && \
        mkdir -p $CATALINA_HOME/common/classes && \
        mkdir -p $CATALINA_HOME/server/classes && \
        mkdir -p $CATALINA_HOME/shared/classes

# Run grails wrapper to install grails and project dependencies
WORKDIR /usr/local/app
COPY     grailsw application.properties ./
COPY     wrapper ./wrapper
COPY     grails-app/conf/BuildConfig.groovy ./grails-app/conf/
RUN     ./grailsw prod clean

# Building app

ADD . ./
RUN ./grailsw test-app unit: && \
    ./grailsw war ROOT.war && \
    cp ROOT.war $CATALINA_BASE/webapps/ && \
# clean up
    rm -rf /usr/local/app && \
    rm -rf /root/.grails  && \
    rm -rf /root/.m2

WORKDIR $CATALINA_BASE

CMD ["start.sh"]