FROM mcr.microsoft.com/dotnet/sdk:8.0

RUN dotnet tool install --global GitVersion.Tool 

# install openjdk (mandatory for groovy)
RUN apt update

RUN apt install -y default-jre
ENV PATH="$PATH:/usr/lib/jvm/java-17-openjdk-amd64/bin"
ENV JAVA_HOME /usr/lib/jvm/java-17-openjdk-amd64/
RUN export JAVA_HOME

# install groovy
RUN apt install zip unzip  
RUN wget https://www.apache.org/dyn/closer.lua/groovy/4.0.9/distribution/apache-groovy-binary-4.0.9.zip?action=download
RUN unzip apache-groovy-binary-4.0.9.zip\?action\=download -d /opt
ENV PATH="$PATH:/opt/groovy-4.0.9/bin"
RUN rm -rf apache-groovy-binary-4.0.9.zip\?action\=download

# Install SQLite JDBC Driver (grab via Maven Central) for Groovy Unit Tests
RUN wget https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.47.0.0/sqlite-jdbc-3.47.0.0.jar -O /opt/sqlite-jdbc.jar

# Install Spock dependencies for Groovy Unit Tests
RUN wget https://repo1.maven.org/maven2/org/spockframework/spock-core/2.3-groovy-4.0/spock-core-2.3-groovy-4.0.jar -O /opt/spock-core.jar
RUN wget https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/2.0.9/slf4j-simple-2.0.9.jar -O /opt/slf4j-simple.jar

# Set GROOVY_CLASSPATH for SQLite and Spock for Groovy Unit Tests
ENV CLASSPATH="/opt/sqlite-jdbc.jar:/opt/spock-core.jar:/opt/slf4j-simple.jar"

# Download the PowerShell package file
RUN wget https://github.com/PowerShell/PowerShell/releases/download/v7.4.2/powershell_7.4.2-1.deb_amd64.deb

# Install the PowerShell package
RUN dpkg -i powershell_7.4.2-1.deb_amd64.deb

# Resolve missing dependencies and finish the install (if necessary)
RUN apt-get install -f

# Delete the downloaded package file
RUN rm powershell_7.4.2-1.deb_amd64.deb

# Install nuget
RUN apt install -y nuget

ENTRYPOINT ["/bin/bash"]
