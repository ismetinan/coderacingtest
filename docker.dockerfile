# Temel Java 17 imajı
FROM openjdk:21.0.1-jdk-slim

# Çalışma dizinini ayarla
WORKDIR /Main

# Uygulama kodunu kopyala
COPY . .

RUN javac -d out $(find . -name "*.java")
# Çalıştırılabilir JAR dosyasını çalıştır
CMD ["java", "-cp", "out", "com.example.Host"]
