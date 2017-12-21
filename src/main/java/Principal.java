import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.io.*;
import java.net.*;

public class Principal {
    public static void main(String[] args) {
        //yND1D9uLGflpK271kqs5qmSiRzgnca-UGa8G-8Ab
        HTTPHandler.switchlight();

        String topic        = "switchTopic";
        String content      = "J'ai switché";
        int qos             = 2;
        String broker       = "tcp://192.168.1.45:1883";
        String clientId     = "JavaSample";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: "+broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            System.out.println("Publishing message: "+content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            System.out.println("Message published");
            sampleClient.subscribe(topic);
            while(true) {
                MqttTopic test = sampleClient.getTopic("switchlight");
+            }
            /*sampleClient.disconnect();
            System.out.println("Disconnected");
            System.exit(0);*/
        } catch(MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }
}
