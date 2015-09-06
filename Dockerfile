FROM    ubuntu:14.04

ENV DEBIAN_FRONTEND noninteractive
#prevent apt from installing recommended packages
RUN echo 'APT::Install-Recommends "false";' > /etc/apt/apt.conf.d/docker-no-recommends && \
    echo 'APT::Install-Suggests "false";' >> /etc/apt/apt.conf.d/docker-no-recommends

# Install java and tomcat
RUN     apt-get update && apt-get install -y tomcat7 openjdk-7-jdk && \
        rm -rf /var/lib/tomcat7/webapps/*
ENV     JAVA_HOME /usr/lib/jvm/java-7-openjdk-amd64/

# Run grails wrapper to install grails and project dependencies
WORKDIR /usr/local/app
COPY     grailsw application.properties ./
COPY     wrapper ./wrapper
COPY     grails-app/conf/BuildConfig.groovy ./grails-app/conf/
RUN     ./grailsw clean

# Building app
ENV CATALINA_HOME /usr/share/tomcat7
ENV CATALINA_BASE /var/lib/tomcat7

ADD . ./
RUN ./grailsw war ROOT.war && \
    cp ROOT.war $CATALINA_BASE/webapps/ && \
    rm -rf /usr/local/app

ENV CATALINA_OPTS=" -Djava.security.egd=file:/dev/./urandom"
ENV PATH $CATALINA_HOME/bin:$PATH
ENV REGISTRY_HOST=localhost
ENV REGISTRY_PORT=5000
WORKDIR $CATALINA_BASE
RUN mkdir temp

EXPOSE  8080
CMD ["catalina.sh", "run"]