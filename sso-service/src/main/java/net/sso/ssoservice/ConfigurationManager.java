package net.sso.ssoservice;


import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Service;
import static org.commons.kafka.KafkaInfo.*;
import java.util.Properties;

@Service
public class ConfigurationManager {
    public KafkaConsumer<String, String> consumer;
    @PostConstruct
    public void initConfigs(){
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, configGroupName);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        //try (consumer = new KafkaConsumer<String, String>(props))
    }


}
