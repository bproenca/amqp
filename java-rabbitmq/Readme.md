## Topic

* Start message broker
  * `docker start rabbitmq`
* Run `ReceiveLogsTopic`
* Run `SendLogsTopic`

Output example:
```
br.com.bcp.topic.SendLogsTopic 
Enter routingKey (aa|bb): aa
Enter message (or exit): 123
 [x] Sent 'aa':'123'

br.com.bcp.topic.ReceiveLogsTopic 
 [*] Waiting for messages. To exit press CTRL+C
 [x] Received 'aa':'123'
```

## Pull API

* Start message broker
  * `docker start rabbitmq`
* Run **SendPullApi**
* Run **RecvPullApi**
  * `mvn clean package`
  * Start two instances like this:
  * `java -jar target/java-rabbitmq-1.0-jar-with-dependencies.jar`