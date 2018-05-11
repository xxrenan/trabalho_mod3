/* Trabalho Final - Apresentado por:
 *  Renan Marques dos Santos
 *  Thiago Rodrigues e Rodrigues
 *  
 *  Código fonte para o NodeMCU que se subscreve a um determinado tópico MQTT
 *  Caso receba o valor 1: Ascende o LED e Liga o buzz no pino D6
 *  Caso receba o valor 0: Apaga o LED e desliga o buzz no pino D6
 *   *  Qualquer outro valor será ignorado.
 */
#include <ESP8266WiFi.h>
#include <PubSubClient.h>

// Atualizar valores para configurar a conexão com o Wifi e Broker MQTT.
const char* ssid = "Renan_Camila";
const char* password = "casa587a";
const char* mqtt_server = "iot.eclipse.org";

WiFiClient espClient;
PubSubClient client(espClient);
long lastMsg = 0;
char msg[50];
int value = 0;

void setup_wifi() {

  delay(10);
  // Iniciando conexão com Wifi
  Serial.println();
  Serial.print("Conectando ao Wifi: ");
  Serial.println(ssid);

  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  randomSeed(micros());

  Serial.println("");
  Serial.println("WiFi conectado");
  Serial.println("IP: ");
  Serial.println(WiFi.localIP());

}

void callback(char* topic, byte* payload, unsigned int length) {
  Serial.print("Comando recebido [");
  Serial.print(topic);
  Serial.print("] ");
  for (int i = 0; i < length; i++) {
    Serial.print((char)payload[i]);
  }
  Serial.println();

  // Ascende o Led e liga o buzz
  if ((char)payload[0] == '0') {
    digitalWrite(BUILTIN_LED, HIGH);   // LED desligad0 = HIGH
    digitalWrite(D6, LOW);
  } 
  if ((char)payload[0] == '1'){
    digitalWrite(BUILTIN_LED, LOW);  // LED ligado = LOW
    digitalWrite(D6, HIGH);
  }

}

void reconnect() {
  // Loop até reconectar
  while (!client.connected()) {
    Serial.print("Conectando ao Broker MQTT...");
    // Cria um client ID randomico
    String clientId = "ESP8266Client-";
    clientId += String(random(0xffff), HEX);
    //  Se conectado, subscreve o tópico
    if (client.connect(clientId.c_str())) {
      Serial.println("Conectado!");
      client.subscribe("thiago-renan/alarme");
    } else {
      Serial.print("Falhou, rc=");
      Serial.print(client.state());
      Serial.println(" Tentando se conectar em 5 segundos");
      // Aguarda 5 segundos até a próxima tentativa.
      delay(5000);
    }
  }
}

void setup() {
  pinMode(BUILTIN_LED, OUTPUT);     // Inicializa o pino BUILTIN_LED como output
  pinMode(D6,OUTPUT);               // Inicializa o pino D6 como output
  Serial.begin(115200);
  setup_wifi();
  client.setServer(mqtt_server, 1883);
  client.setCallback(callback);
}

void loop() {

  if (!client.connected()) {
    reconnect();
  }
  client.loop();
}
