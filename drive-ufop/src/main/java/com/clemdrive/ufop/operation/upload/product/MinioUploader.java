package com.clemdrive.ufop.operation.upload.product;

import com.clemdrive.ufop.config.MinioConfig;
import com.clemdrive.ufop.constant.StorageTypeEnum;
import com.clemdrive.ufop.constant.UploadFileStatusEnum;
import com.clemdrive.ufop.exception.operation.UploadException;
import com.clemdrive.ufop.operation.upload.Uploader;
import com.clemdrive.ufop.operation.upload.domain.UploadFile;
import com.clemdrive.ufop.operation.upload.domain.UploadFileResult;
import com.clemdrive.ufop.operation.upload.request.DriveMultipartFile;
import com.clemdrive.ufop.util.RedisUtil;
import com.clemdrive.ufop.util.UFOPUtils;
import io.minio.*;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class MinioUploader extends Uploader {

    private MinioConfig minioConfig;

    @Resource
    RedisUtil redisUtil;

    public MinioUploader() {

    }

    public MinioUploader(MinioConfig minioConfig) {
        this.minioConfig = minioConfig;
    }

    @Override
    public void cancelUpload(UploadFile uploadFile) {
    }

    protected UploadFileResult doUploadFlow(DriveMultipartFile driveMultipartFile, UploadFile uploadFile) {
        UploadFileResult uploadFileResult = new UploadFileResult();
        try {
            driveMultipartFile.getFileUrl(uploadFile.getIdentifier());
            String fileUrl = UFOPUtils.getUploadFileUrl(uploadFile.getIdentifier(), driveMultipartFile.getExtendName());

            File tempFile = UFOPUtils.getTempFile(fileUrl);
            File processFile = UFOPUtils.getProcessFile(fileUrl);

            byte[] fileData = driveMultipartFile.getUploadBytes();

            writeByteDataToFile(fileData, tempFile, uploadFile);

            //判断是否完成文件的传输并进行校验与重命名
            boolean isComplete = checkUploadStatus(uploadFile, processFile);
            uploadFileResult.setFileUrl(fileUrl);
            uploadFileResult.setFileName(driveMultipartFile.getFileName());
            uploadFileResult.setExtendName(driveMultipartFile.getExtendName());
            uploadFileResult.setFileSize(uploadFile.getTotalSize());
            uploadFileResult.setStorageType(StorageTypeEnum.MINIO);

            if (uploadFile.getTotalChunks() == 1) {
                uploadFileResult.setFileSize(driveMultipartFile.getSize());
            }
            uploadFileResult.setIdentifier(uploadFile.getIdentifier());
            if (isComplete) {

                minioUpload(fileUrl, tempFile, uploadFile);
                uploadFileResult.setFileUrl(fileUrl);
                tempFile.delete();

                if (UFOPUtils.isImageFile(uploadFileResult.getExtendName())) {
                    InputStream inputStream = null;
                    try {
                        MinioClient minioClient =
                                MinioClient.builder().endpoint(minioConfig.getEndpoint())
                                        .credentials(minioConfig.getAccessKey(), minioConfig.getSecretKey()).build();

                        inputStream = minioClient.getObject(GetObjectArgs.builder().bucket(minioConfig.getBucketName()).object(uploadFileResult.getFileUrl()).build());

                        BufferedImage src = ImageIO.read(inputStream);
                        uploadFileResult.setBufferedImage(src);
                    } catch (IOException | InternalException | XmlParserException | InvalidResponseException |
                             InvalidKeyException | NoSuchAlgorithmException | ErrorResponseException |
                             InsufficientDataException | ServerException e) {
                        e.printStackTrace();
                    } finally {
                        IOUtils.closeQuietly(inputStream);
                    }

                }

                uploadFileResult.setStatus(UploadFileStatusEnum.SUCCESS);
            } else {
                uploadFileResult.setStatus(UploadFileStatusEnum.UNCOMPLATE);
            }
        } catch (IOException e) {
            throw new UploadException(e);
        }


        return uploadFileResult;
    }

    @Override
    protected void doUploadFileChunk(DriveMultipartFile driveMultipartFile, UploadFile uploadFile) {

    }

    @Override
    protected UploadFileResult organizationalResults(DriveMultipartFile driveMultipartFile, UploadFile uploadFile) {
        return null;
    }


    private void minioUpload(String fileUrl, File file, UploadFile uploadFile) {
        InputStream inputStream = null;
        try {
            MinioClient minioClient =
                    MinioClient.builder().endpoint(minioConfig.getEndpoint())
                            .credentials(minioConfig.getAccessKey(), minioConfig.getSecretKey()).build();
            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioConfig.getBucketName()).build());
            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioConfig.getBucketName()).build());
            }

            inputStream = new FileInputStream(file);
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(minioConfig.getBucketName()).object(fileUrl).stream(
                                    inputStream, uploadFile.getTotalSize(), 1024 * 1024 * 5)
//                            .contentType("video/mp4")
                            .build());
        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }

    }


}
