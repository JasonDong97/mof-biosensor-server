package com.miqroera.biosensor.infra.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * MinIo工具类
 */
@Slf4j
@Component
public class MinIoUtil {
    private static final String SEPARATOR = "/";
    private static MinioClient client;

    private MinIoUtil(MinioClient client) {
        MinIoUtil.client = client;
    }

    public static String getPath(String bucket, String object) {
        if (bucket == null || object == null) {
            return null;
        }
        if (bucket.endsWith(SEPARATOR)) {
            bucket = bucket.substring(0, bucket.length() - 1);
        }
        if (object.startsWith(SEPARATOR)) {
            object = object.substring(1);
        }
        return bucket + SEPARATOR + object;
    }

    public static File downloadTempFile(String bucket, String object, String fileName) {
        GetObjectArgs args = GetObjectArgs.builder().bucket(bucket).object(object).build();

        File file = Path.of(System.getProperty("java.io.tmpdir"), System.currentTimeMillis() + "", fileName).toFile();
        FileUtil.mkParentDirs(file);
        try (InputStream in = client.getObject(args);
             BufferedOutputStream out = FileUtil.getOutputStream(file)) {
            IoUtil.copy(in, out);
        } catch (IOException | ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidBucketNameException | InvalidKeyException | InvalidResponseException |
                 NoSuchAlgorithmException | ServerException | XmlParserException e) {
            log.error("下载文件失败", e);
        }
        return file;
    }

    @SneakyThrows
    public static InputStream getObject(String bucket, String object) {
        GetObjectArgs args = GetObjectArgs.builder().bucket(bucket).object(object).build();
        return client.getObject(args);
    }
}
