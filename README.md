- Cài đặt nexus
- Run nexus
- Cấu hình file settings.xml trong thư mục m2:
    <?xml version="1.0" encoding="UTF-8"?>
    <settings xmlns="http://maven.apache.org/SETTINGS/1.1.0"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.1.0 http://maven.apache.org/xsd/settings-1.1.0.xsd">
    
      <!-- <servers>
        <server>
          <id>nexus-release</id>
          <username>admin</username>
          <password>123456</password>
        </server>
        <server>
          <id>nexus-snapshot</id>
          <username>admin</username>
          <password>123456</password>
        </server>
        <server>
          <id>nexus-maven-central</id>
          <username>admin</username>
          <password>123456</password>
        </server>
      </servers> -->
    
      <servers>
        <server>
          <id>nexus-release</id>
          <username>core-downloader</username>
          <password>SwyOLxB1192L</password>
        </server>
      </servers>
    
      <!-- <mirrors>
        <mirror>
          <id>central</id>
          <name>central</name>
          <url>http://localhost:2222/repository/maven-public/</url>
          <mirrorOf>*</mirrorOf>
        </mirror>
      </mirrors> -->
    
    </settings>
    
- Cấu hình file pom.xml trong code.
