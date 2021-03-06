package chavevalor;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.ThreadLocalRandom;

public class MqttPublisher {

	public static void main(String[] args) {
		String topic = "temperature";
		int qos = 2;
		String broker = "tcp://localhost:1883";
		String clientId = "PubisherId";
		MemoryPersistence persistence = new MemoryPersistence();

		try {
			MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			System.out.println("Connecting to broker: " + broker);
			sampleClient.connect(connOpts);
			System.out.println("Connected");
			
			boolean again = true;
			while(again) {
				try {
					Thread.sleep(1000);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				Integer temperature = ThreadLocalRandom.current().nextInt(15, 45 + 1); 
				System.out.println("Message: " + temperature);
				MqttMessage message = new MqttMessage(temperature.toString().getBytes());
				message.setQos(qos);
				sampleClient.publish(topic, message);
				System.out.println("Message published");
			}
			
			sampleClient.disconnect();
			System.out.println("Disconnected");
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
}
