
Kart-Service  
======  

[![Build Status](https://travis-ci.com/hiroinamine/kart-service.svg?branch=master)](https://travis-ci.com/hiroinamine/kart-service)

API de Serviço de kart para coleta de resultados.  
  
### Desenvolvimento  
  
Para executar no ambiente de desenvolvimento utilizar comando `sbt` e abrir no navegador o endereço [http://localhost:9000](http://localhost:9000)  
  
```shell  
$ sbt run  
# ...  
[info] p.c.s.NettyServer - Listening for HTTP on /0:0:0:0:0:0:0:0:9000  
```  
  
### Testes unitários  
  
```shell  
$ sbt test  
```  
  
## Documentação da API  
  
#### GET /  
  
Retorna informações sobre a api.  
  
```json  
{  
  "builtAtMillis": "1570304458577",  
  "name": "kart-service",  
  "scalaVersion": "2.13.0",  
  "version": "1.0-SNAPSHOT",  
  "builtAtString": "2019-10-05 19:40:58.577"  
}  
```  
  
#### POST /races  
  
Cria uma nova corrida e retorna informações sobre cada volta de cada piloto.  
  
**Request:**  
  
Enviar um arquivo `.log` através através do formato `multipart/form-data`:  
  
```html  
<form action="/races" method="POST" enctype="multipart/form-data">  
    <input type="file" name="laps.log">  
    <button>Submit</button>  
</form>  
```  
  
**Response:**  
  
```json  
{  
  "id": "7cead28b-db99-4e6d-882a-c3f2de312dad",  
  "drivers": [  
    {  
      "id": "015",  
      "name": "F.ALONSO",  
      "laps": [  
        {  
          "timestamp": "23:49:30.976",  
          "driverId": "015",  
          "driverName": "F.ALONSO",  
          "lapNumber": 1,  
          "lapTime": "00:01:18.456",  
          "averageSpeed": 35.47  
        }  
      ]  
    }  
  ]  
}  
```  
  
#### GET /races/:id  
  
Retorna o resultado de uma corrida.  
  
  
```json  
{  
  "id": "7cead28b-db99-4e6d-882a-c3f2de312dad",  
  "results": [  
    {  
      "position": 0,  
      "driverId": "038",  
      "driverName": "F.MASSA",  
      "lapNumber": 4,  
      "duration": "00:04:11.578",  
      "gapTime": "-"  
    },  
    {  
      "position": 1,  
      "driverId": "002",  
      "driverName": "K.RAIKKONEN",  
      "lapNumber": 4,  
      "duration": "00:04:15.153",  
      "gapTime": "00:00:05.117"  
    }  
  ]  
}  
```  
  
#### GET /races/:id/fastest_lap  
  
Retorna a melhor volta de uma corrida.  
  
  
```json  
{  
  "timestamp": "23:51:14.216",  
  "driverId": "038",  
  "driverName": "F.MASSA",  
  "lapNumber": 3,  
  "lapTime": "00:01:02.769",  
  "averageSpeed": 44.334  
}  
```  
  
#### GET /races/:id/drivers/fastest_lap  
  
Retorna a melhor volta de cada piloto de uma corrida.  
  
  
```json  
{  
  "fastestLaps": [  
    {  
      "timestamp": "23:50:37.987",  
      "driverId": "015",  
      "driverName": "F.ALONSO",  
      "lapNumber": 2,  
      "lapTime": "00:01:07.011",  
      "averageSpeed": 41.528  
    },  
    {  
      "timestamp": "23:51:18.576",  
      "driverId": "033",  
      "driverName": "R.BARRICHELLO",  
      "lapNumber": 3,  
      "lapTime": "00:01:03.716",  
      "averageSpeed": 43.675  
    }  
  ]  
}  
```  
  
#### GET /races/:id/drivers/average_speed  
  
Retorna a velocidade média de cada piloto de uma corrida.  
  
  
```json  
{  
  "averageSpeed": [  
    {  
      "driverId": "015",  
      "driverName": "F.ALONSO",  
      "averageSpeed": 38.06625  
    },  
    {  
      "driverId": "033",  
      "driverName": "R.BARRICHELLO",  
      "averageSpeed": 43.467999999999996  
    }  
  ]  
}  
```  
  
## Observações  
  
* O arquivo de log contendo as corridas deve estar separados por `tab` e com o seguinte layout
```
|Hora        |Piloto     |NºVolta|TempoVolta|VelocidadeMediaVolta|
|------------|-----------|-------|----------|--------------------|
|10:50:00.850|032 - Senna|      1|  1:09.009|               50,60|
```
* A corrida deve começar e terminar no mesmo dia  
* O vencedor é o piloto que completar primeiro a 4ª volta  
* O piloto com mesmo código, mas com nome diferente, não é considerado um outro piloto