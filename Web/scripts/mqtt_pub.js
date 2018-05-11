
    //sample HTML/JS script that will publish/subscribe to topics in the Google Chrome Console
    //by Matthew Bordignon @bordignon on twitter.
    var wsbroker = "iot.eclipse.org";  //mqtt websocket enabled broker
    var wsport = 1883 // port for above
    var client = new Paho.MQTT.Client(wsbroker, wsport,
        "myclientid_" + parseInt(Math.random() * 100, 10));
    client.onConnectionLost = function (responseObject) {
      console.log("connection lost: " + responseObject.errorMessage);
    };
    client.onMessageArrived = function (message) {
      console.log(message.destinationName, ' -- ', message.payloadString);
    };
    var options = {
      timeout: 3,
      onSuccess: function () {
        console.log("mqtt connected");
        // Connection succeeded; subscribe to our topic, you can add multile lines of these
        client.subscribe('/alarme', {qos: 0});
    
        //use the below if you want to publish to a topic on connect
        message = new Paho.MQTT.Message("1");
        message.destinationName = "/alarme";
        client.send(message);
  
      },
      onFailure: function (message) {
        console.log("Connection failed: " + message.errorMessage);
      }
    };
  function init() {
      client.connect(options);
  }
