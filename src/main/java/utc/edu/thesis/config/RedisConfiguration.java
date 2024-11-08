package utc.edu.thesis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.ReadFrom;
import io.lettuce.core.SocketOptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Configuration for Redis.
 *
 * @author thanbv
 */
@Slf4j
@Configuration
@EnableRedisRepositories(value = "utc.edu.thesis.repository")
public class RedisConfiguration implements CachingConfigurer {
    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory(RedisProperties redisProperties) {
        // Pool config - Apache common Pool2
        final var genericObjectPoolConfig = new GenericObjectPoolConfig<>();
        genericObjectPoolConfig.setMaxIdle(redisProperties.getLettuce().getPool().getMaxIdle());
        genericObjectPoolConfig.setMinIdle(redisProperties.getLettuce().getPool().getMinIdle());
        genericObjectPoolConfig.setMaxTotal(redisProperties.getLettuce().getPool().getMaxActive());
        genericObjectPoolConfig.setMaxWait(redisProperties.getLettuce().getPool().getMaxWait());
        genericObjectPoolConfig.setTimeBetweenEvictionRuns(redisProperties.getLettuce()
                .getPool()
                .getTimeBetweenEvictionRuns());

        final var socketOptions = SocketOptions.builder().connectTimeout(redisProperties.getConnectTimeout()).build();
        final var clientOptions = ClientOptions.builder().socketOptions(socketOptions).build();

        var clientConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(redisProperties.getTimeout())
                .clientOptions(clientOptions)
                .poolConfig(genericObjectPoolConfig)
                .readFrom(ReadFrom.REPLICA_PREFERRED) // Preferentially read data from the replicas.
                .build();

        final var serverConfig = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
        serverConfig.setPassword(redisProperties.getPassword());

        final var lettuceConnectionFactory = new LettuceConnectionFactory(serverConfig, clientConfig);
        lettuceConnectionFactory.setValidateConnection(true);
        lettuceConnectionFactory.setShareNativeConnection(false);

        return new LettuceConnectionFactory(serverConfig, clientConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        final var template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(lettuceConnectionFactory);

        final var objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        // Use Jackson2JsonRedisSerializer to replace the default JdkSerializationRedisSerializer
        // to serialize and deserialize the Redis value.
        final var jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer<>(Object.class);

        final var stringRedisSerializer = new StringRedisSerializer();
        // String serialization of keys
        template.setKeySerializer(stringRedisSerializer);
        // String serialization of hash keys
        template.setHashKeySerializer(stringRedisSerializer);
        // Jackson's serialization of values
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // Jackson's serialization of hash values
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();

        return template;
    }


    @Override
    public CacheErrorHandler errorHandler() {
        return new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
                log.info("==> [{}] Failure getting from cache: {}, exception: ",
                        Thread.currentThread().getId(), cache.getName(), exception);
            }

            @Override
            public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
                log.info("==> [{}] Failure getting from cache: {}, exception: ",
                        Thread.currentThread().getId(), cache.getName(), exception);
            }

            @Override
            public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
                log.info("==> [{}] Failure getting from cache: {}, exception: ",
                        Thread.currentThread().getId(), cache.getName(), exception);
            }

            @Override
            public void handleCacheClearError(RuntimeException exception, Cache cache) {
                log.info("==> [{}] Failure getting from cache: {}, exception: ",
                        Thread.currentThread().getId(), cache.getName(), exception);
            }
        };
    }
}