# CS102 - Social Magnet (the command-line facebook)
#### cs102-team-project

$ROOT: represents the folder this project is in.

### SQL INSTRUCTIONS
* CHANGE LOAD DIRECTORY IN load.sql
```
default_dir: /Users/hartonotjakrawinata/schoolCode/102/CS102--g1-t13/sql/data/friends.csv
actual_dir: $ROOT/CS102--g1-t13/sql/data/friends.csv
```
* Run tables.sql
* Run load.sql


### Running the program

* Start from project's root folder
* Initiating and Running Database (Follow Sql Instructions)
```
// Run MAMP -- localhost:8889
/Applications/MAMP/Library/bin/mysql -uroot -proot
source $ROOT_FOLDER/sql/tables.sql
source $ROOT_FOLDER/sql/load.sql
```
* Run Tomcat Server
```
// Compile server
cd ./tomcat/webapps/server/WEB-INF/classes
./user.bat && ./post.bat && ./farmers.bat

// Run server -- localhost:9999
cd $ROOT_FOLDER
./tomcat/bin/catalina.sh run
```
* Run Application
```
./compile.bat
./run.bat
```

### Junit Testing (compiled with maven)
* Go to ```https://maven.apache.org/download.cgi```
* Download apache-maven-<\version number>-bin.zip
* Put it in the directory /usr/local/bin
* EXPORT PATH: ```export PATH="$PATH:/usr/local/bin/apache-maven-3.6.3/bin"```
* Go to project directory
* Run maven test ```mvn test```