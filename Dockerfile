FROM    ubuntu:14.04

ENV DEBIAN_FRONTEND noninteractive
# Install java and tomcat
RUN     apt-get update && apt-get install -y tomcat7 openjdk-7-jdk curl
RUN     rm -rf /var/lib/tomcat7/webapps/*
ENV     JAVA_HOME /usr/lib/jvm/java-7-openjdk-amd64/

# Install gvm and grails
RUN apt-get install unzip
RUN curl -s get.gvmtool.net | bash
RUN /bin/bash -c "source /root/.gvm/bin/gvm-init.sh && gvm install grails 2.4.5 && grails create-app temp && cd temp && grails war && cd .. && rm -rf temp"
# Building app
WORKDIR /usr/local/app
ADD . ./
RUN /bin/bash -c "source /root/.gvm/bin/gvm-init.sh && grails war ROOT.war" && \
    cp ROOT.war /var/lib/tomcat7/webapps/ && \
    rm -rf /usr/local/app
ENV CATALINA_OPTS=" -Djava.security.egd=file:/dev/./urandom"
ENV CATALINA_HOME /usr/share/tomcat7/
ENV CATALINA_BASE /var/lib/tomcat7
ENV PATH $CATALINA_HOME/bin:$PATH
ENV REGISTRY_HOST=localhost
ENV REGISTRY_PORT=5000
WORKDIR $CATALINA_HOME

EXPOSE  8080
CMD ["catalina.sh", "run"]