package com.miqroera.biosensor.infra.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MinIoConfig {

    @Bean
    @ConfigurationProperties(prefix = "minio")
    public MinIoProperties minIoProperties() {
        return new MinIoProperties();
    }

    @SneakyThrows
    @Bean
    public MinioClient minioClient(MinIoProperties minIoProperties) {
        MinioClient c = MinioClient.builder().endpoint(minIoProperties.getUrl())
                .credentials(minIoProperties.getAccessKey(), minIoProperties.getSecretKey())
                .build();
        try {
            if (!c.bucketExists(BucketExistsArgs.builder().bucket(minIoProperties.getBucketName()).build())) {
                c.makeBucket(MakeBucketArgs.builder().bucket(minIoProperties.getBucketName()).build());
            }
        } catch (Exception e) {
            log.error("创建MinIO桶 `{}` 失败: {}", minIoProperties.getBucketName(), e.getMessage());
        }
        return c;
    }


    @Data
    public static class MinIoProperties {
        /**
         * minio地址+端口号
         */
        private String url;

        private String region;

        /**
         * minio 用户名
         */
        private String accessKey;

        /**
         * minio密码
         */
        private String secretKey;

        /**
         * 文件桶的名称
         */
        private String bucketName;
    }

}

