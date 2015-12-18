call mvn clean install
copy /Y .\security-web\target\security-web-1.0.0-SNAPSHOT.war d:\java\appserv\tomcat-8.0.28\webapps\security-web.war
copy /Y .\security-sample\target\security-sample-1.0.0-SNAPSHOT.war d:\java\appserv\tomcat-8.0.28\webapps\security-sample.war
pause