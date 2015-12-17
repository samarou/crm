call mvn clean install
copy /Y .\security-web\target\security-web-1.0.0-SNAPSHOT.war d:\java\appserv\tomcat-7.0.65\webapps\security-web.war
pause