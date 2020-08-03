package com.kangyonggan.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * @author kyg
 */
@SpringBootApplication
@RestController("/")
public class Application {

    /**
     * redis键的前缀
     */
    private static final String REDIS_KEY_PREFIX = "demo:";

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(com.kangyonggan.demo.Application.class, args);
    }

    /**
     * 根据用户id查询用户信息（优先走redis缓存）
     *
     * @param id 用户id
     * @return 返回用户信息
     */
    @GetMapping("{id:[\\d]+}")
    public User getUserById(@PathVariable Long id) {
        // 先看缓存有没有
        User user = (User) redisTemplate.opsForValue().get(REDIS_KEY_PREFIX + id);
        if (user != null) {
            log.info("redis中数据，直接返回:{}", user);
            return user;
        }

        // 缓存没有就查询数据库
        user = userRepository.findById(id).orElse(null);
        log.info("查询数据库，返回结果：{}", user);

        // 放入缓存
        if (user != null) {
            redisTemplate.opsForValue().set(REDIS_KEY_PREFIX + id, user);
        }

        return user;
    }

    /**
     * 测试文件读写
     *
     * @param str
     * @return
     */
    @GetMapping("fileReadWrite")
    public String fileReadWrite(String str) {
        String filename = System.getProperty("user.home") + File.separator + "demo001.txt";
        try (FileWriter writer = new FileWriter(filename);
             BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            writer.write("字符串~" + str);
            writer.flush();
            log.info("字符串写入成功，文件位置:{}", System.getProperty("user.home"));

            str = reader.readLine();
            log.info("读取文件内容：{}", str);
            return str;
        } catch (Exception e) {
            log.error("文件读写异常", e);
        }

        return "err";
    }

    @Bean
    RedisTemplate redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());

        return redisTemplate;
    }
}
