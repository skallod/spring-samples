#!/bin/bash

# Переменная для хранения значения счетчика
COUNTER=0

# Интервал между выполнениями команды (в секундах)
INTERVAL=1

while true; do
    COUNTER=$((COUNTER+1)) # Увеличиваем счетчик на единицу
    TIMESTAMP="$(date '+%Y-%m-%d %H:%M:%S')" # Получаем текущее время

    echo "=========== [$TIMESTAMP] № $COUNTER ==============="
    curl -X GET "localhost:8081/api/v1/subjects/sampleApplication/versions/2" -H 'content-type: application/json' \

#    echo "Ожидание $INTERVAL секунд."
#    sleep "$INTERVAL"
done