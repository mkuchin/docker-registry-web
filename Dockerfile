FROM    ubuntu

ENV DEBIAN_FRONTEND noninteractive
#prevent apt from installing recommended packages
RUN echo 'APT::Install-Recommends "false";' > /etc/apt/apt.conf.d/docker-no-recommends && \
    echo 'APT::Install-Suggests "false";' >> /etc/apt/apt.conf.d/docker-no-recommends

# Install java and tomcat
RUN     apt-get update && apt-get install -y tomcat7 git libyaml-perl libfile-slurp-perl && \
        rm -rf /var/lib/tomcat7/webapps/* && \
        rm -rf /var/lib/apt/lists/*

ENV     JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
ENV     CATALINA_HOME /usr/share/tomcat7
ENV     CATALINA_BASE /var/lib/tomcat7

ENV CATALINA_OPTS=" -Djava.security.egd=file:/dev/./urandom"
ENV PATH $CATALINA_HOME/bin:$PATH

COPY tomcat/server.xml $CATALINA_BASE/conf/
COPY web-app/WEB-INF/config.yml /conf/config.yml
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
RUN     ./grailsw refresh-dependencies

# Building app

ADD . ./
# copy master in case of master branch and HEAD in case of tag


RUN ./grailsw test-app unit: -echoOut && \
    ./grailsw war ROOT.war && \
    cp application.properties $CATALINA_BASE/ && \
    cp ROOT.war $CATALINA_BASE/webapps/ && \
# clean up
    rm -rf /usr/local/app && \
    rm -rf /root/.grails  && \
    rm -rf /root/.m2

WORKDIR $CATALINA_BASE
VOLUME /data
EXPOSE 8080

CMD ["start.sh"]
