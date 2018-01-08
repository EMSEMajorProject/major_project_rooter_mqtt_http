import org.eclipse.paho.client.mqttv3.*;

public class Principal {
    public static void main(String[] args) throws MqttException {
        HTTPHandler.initColor();
        CloudMQTT.init();
        RaspberryBroker.init();
        RaspberryBroker.subscribe(Util.topics);
        CloudMQTT.subscribe(Util.topics);
    }
}
