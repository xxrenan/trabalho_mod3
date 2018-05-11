
#include <ESP8266HTTPClient.h>
#include <ESP8266WiFi.h>
#include <ArduinoJson.h> // Baixada do Gerenciador

StaticJsonBuffer<300> JSONbuffer;
JsonObject& JSONencoder = JSONbuffer.createObject();
HTTPClient http;

const char* ssid ="IoT";
const char* password ="12345678";

char buff[10];
char JSONmessageBuffer[300];

// initializes or defines the output pin of the LM35 temperature sensor
int outputpin= A0;

//this sets the ground pin to LOW and the input voltage pin to high
void setup() {
  Serial.begin(115200); 

  // Connect to WiFi network
  Serial.println();
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);
  
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println("WiFi connected");
  
  // Print the IP address
  Serial.println(WiFi.localIP());
}

void loop()       //main loop
{
  int analogValue = analogRead(outputpin);
  float millivolts = (analogValue/1024.0) * 3300; //3300 is the voltage provided by NodeMCU
  float celsius = millivolts/10;
  Serial.println(celsius);
  dtostrf(celsius, 4, 6, buff);
  
  //JSONencoder["time"]  = "2018-05-05 13:35:00";
  JSONencoder["valor"] = buff;
  JSONencoder.prettyPrintTo(JSONmessageBuffer, sizeof(JSONmessageBuffer));

  http.begin("http://35.198.2.16:3000/temperatura");
  http.addHeader("Content-type", "application/json");

  int httpCode = http.POST(JSONmessageBuffer);
  String payload = http.getString();

  Serial.println(httpCode);
  Serial.println(payload);

  http.end();
  
  delay(5000);
}
