# Temel Java 21 imajı
FROM openjdk:21.0.1-jdk-slim

# Çalışma dizinini ayarla
WORKDIR /CodeRacingP2PTest

# Uygulama kodunu kopyala
COPY . /CodeRacingP2PTest

RUN javac -d out $(find . -name "*.java")
# Çalıştırılabilir JAR dosyasını çalıştır
CMD ["java", "-cp", "out", "network.Host"]
