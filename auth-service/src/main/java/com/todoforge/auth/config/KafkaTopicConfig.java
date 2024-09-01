package com.todoforge.auth.config;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.Node;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public NewTopic lastLoginTopic() throws ExecutionException, InterruptedException {
        String topicName = "last-login";
        int numPartitions = 3;
        int numReplications = determineReplicationFactor();

        return TopicBuilder.name(topicName)
                .partitions(numPartitions)
                .replicas(numReplications)
                .build();
    }

    private int determineReplicationFactor() throws ExecutionException, InterruptedException {
        Map<String, Object> conf = Map.of(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        try (AdminClient adminClient = AdminClient.create(conf)) {
            Collection<Node> nodes = adminClient.describeCluster().nodes().get();
            int numberOfBrokers = nodes.size();
            // set replication factor as 2 or the number of brokers, whichever is smaller
            return Math.min(2, numberOfBrokers);
        }
    }

}
