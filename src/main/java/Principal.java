import org.eclipse.paho.client.mqttv3.*;

public class Principal {
    public static void main(String[] args) throws MqttException {
        //yND1D9uLGflpK271kqs5qmSiRzgnca-UGa8G-8Ab
        HTTPHandler.switchlight();

        String switchTopic        = "switchTopic";
        String content      = "J'ai switché";
        String[] topics = {"switchTopic", "lumTopic"};
        CloudMQTT.init();
        RaspberryBroker.init();
        RaspberryBroker.subscribe(topics);
        CloudMQTT.subscribe(topics);
    }
}
