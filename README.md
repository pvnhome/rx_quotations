# Пример использования IRIS API
В примере показано получение котировок для фондового рынка с помощью библиотеки `iris-api-java-client`. Использование `iris-api-java-client` является необязательным, но сильно упрощает работу с IRIS API.

## Запуск приложения

Поместите предоставленный вам файл `mqtt_servers.conf` в папку `conf`. Запустите приложение из командной строки, используя Maven:

```
mvn -Dmaven.test.skip=true clean compile exec:java
```

## Настройки

Файлы настроек (папка conf):

* rx_quotations_extended_props.conf - настройки приложения.

* mqtt_servers.conf - параметры для доступа к серверам (файл предоставляется отдельно);

* rx_quotations_log4j.properties - настройки логирования (Log4j);

* ssl/letsencrypt_root_1.jks - корневые сертификаты (при работе по протоколу wss);

## Используемые библиотеки

Библиотеки в локальном репозитарии (папка lib):
* base_utils - базовые классы используемые в других библиотеках (обязательная);
* hocon_conf - работа с файлами настроек в формате [hocon](https://github.com/lightbend/config/blob/main/HOCON.md) (требуется только при работе с настройками из файлов, альтернативно параметры соединения могут быть заданы непосредственно в java-коде);
* iris-api-java - классы для работы с сообщеними IRIS API в формате [Protocol Buffers](https://developers.google.com/protocol-buffers) (обязательная);
* iris-api-java-client - клиентская библиотека для работы c IRIS API, использующая [RxJava](https://github.com/ReactiveX/RxJava) (необязательная);
* iris-api-java-utils - вспомогательная библиотека для преобразования типов IRIS API к типам Java (необязательная);

Общедоступные библиотеки:
* config - только при работе с форматом [hocon](https://github.com/lightbend/config/blob/main/HOCON.md);
* org.eclipse.paho.client.mqttv3 - клиентская библиотека [Paho](https://www.eclipse.org/paho/) для работы с [MQTT](https://mqtt.org/) (обязательная);
* protobuf-java - библиотека [Protocol Buffers](https://developers.google.com/protocol-buffers) (обязательная);
* reactive-streams и rxjava - библиотека [RxJava](https://github.com/ReactiveX/RxJava) (обязательна при использовании `iris-api-java-client`);
