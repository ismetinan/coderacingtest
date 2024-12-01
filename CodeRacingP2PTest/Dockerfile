# Base Image
FROM openjdk:21.0.1-jdk-slim

# Uygulama dizinine git (projenin kök dizini)
WORKDIR /CodeRacing

# Proje dosyalarını kopyala
COPY . .

# Java dosyalarını derle ve çıktıları 'out' dizinine gönder
RUN mkdir -p out \
    && javac -d out $(find ./src -name "*.java")

# Uygulamayı başlat
CMD ["java", "-cp", "out", "network.Host"]
