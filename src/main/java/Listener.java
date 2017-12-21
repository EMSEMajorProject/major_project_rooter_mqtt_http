/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.eclipse.paho.client.mqttv3.*;

import java.sql.Timestamp;
import java.util.concurrent.CountDownLatch;

/**
 * A Mqtt topic subscriber
 *
 */
public class Listener {

    
    public void run() {
        System.out.println("TopicSubscriber initializing...");

        String host = "tcp://192.168.1.45:1883";


        try {
            // Create an Mqtt client
            MqttClient mqttClient = new MqttClient(host, "HelloWorldSub");
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            
            // Connect the client
            System.out.println("Connecting to Solace messaging at "+host);
            mqttClient.connect(connOpts);
            System.out.println("Connected");

            // Latch used for synchronizing b/w threads
            final CountDownLatch latch = new CountDownLatch(1);
            
            // Topic filter the client will subscribe to
            final String subTopic = "switchTopic";

            final String lumTopic = "lumTopic";
            
            // Callback - Anonymous inner-class for receiving messages

            mqttClient.setCallback(new MqttCallback() {

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    // Called when a message arrives from the server that
                    // matches any subscription made by the client
                    String time = new Timestamp(System.currentTimeMillis()).toString();

                    if (topic.equalsIgnoreCase("switchTopic")) {
                        System.out.println("\nReceived a Message!" +
                                "\n\tTime:    " + time +
                                "\n\tTopic:   " + topic +
                                "\n\tMessage: " + new String(message.getPayload()) +
                                "\n\tQoS:     " + message.getQos() + "\n");
                        HTTPHandler.switchlight();
                    }
                    if(topic.equalsIgnoreCase("lumTopic")) {
                        Util.hue_value = Long.parseLong(message.toString())*65536/1024;
                        HTTPHandler.setColor();
                    }

                }

                public void connectionLost(Throwable cause) {
                    System.out.println("Connection to Solace messaging lost!" + cause.getMessage());
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                }

            });
            
            // Subscribe client to the topic filter and a QoS level of 0
            System.out.println("Subscribing client to topic: " + subTopic);
            mqttClient.subscribe(subTopic, 0);
            mqttClient.subscribe(lumTopic, 0);
            System.out.println("Subscribed");

            // Wait for the message to be received
            try {
                latch.await(); // block here until message received, and latch will flip
            } catch (InterruptedException e) {
                System.out.println("I was awoken while waiting");
            }


            System.exit(0);
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Check command line arguments
        new Listener().run();

    }
}
