call mvn clean install -q
java -jar target\smg-scrapper-1.0-jar-with-dependencies.jar %1
call mvn clean -q