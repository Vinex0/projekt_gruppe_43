./gradlew assemble
& docker build --tag=moneymanager:latest .
& docker compose up