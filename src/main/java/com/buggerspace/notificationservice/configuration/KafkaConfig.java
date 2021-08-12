package com.buggerspace.notificationservice.configuration;

import com.buggerspace.notificationservice.model.ProductStockChangeNotification;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.buggerspace.notificationservice.model.ProductPriceChangeNotification;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

    private String bootstrapServer = "http://localhost:29092";

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProductPriceChangeNotification> productPriceChangeQueueKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ProductPriceChangeNotification> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new StringDeserializer(),
                new JsonDeserializer<>(ProductPriceChangeNotification.class,false)));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProductStockChangeNotification> productStockChangeQueueKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ProductStockChangeNotification> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new StringDeserializer(),
                new JsonDeserializer<>(ProductStockChangeNotification.class,false)));
        return factory;
    }

    private Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonSerializerWithJTM.class);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 25000);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 35000);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "100");
        return props;
    }


}
