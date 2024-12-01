# Temel Java 17 imajı
FROM openjdk:21.0.1-jdk-slim

# Çalışma dizinini ayarla
WORKDIR /CodeRacingP2PTest

# Uygulama kodunu kopyala
COPY . /CodeRacingP2PTest


# Çalıştırılabilir JAR dosyasını çalıştır
CMD ["java", "-jar", "target/Host.java"]
