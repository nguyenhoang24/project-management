package utc.edu.thesis.service.impl;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import utc.edu.thesis.domain.dto.S3File;
import utc.edu.thesis.domain.dto.UploadImage;
import utc.edu.thesis.service.AmazonUploadService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AmazonUploadServiceImpl implements AmazonUploadService {

    private final AmazonS3 s3client;

    @Value("${amazonProperties.bucketName}")
    private String bucketName;

    @Override
    public void uploadFileTos3bucket(String fileName, File file) {
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    @Override
    public File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    @Override
    public String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    @Override
    public String uploadFile(MultipartFile multipartFile) {
        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            fileUrl = "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
            uploadFileTos3bucket(fileName, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }

    @Override
    public UploadImage uploadImg(MultipartFile multipartFile) {
        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            fileUrl = "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
            uploadFileTos3bucket(fileName, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new UploadImage(fileUrl);
    }

    @Override
    public List<S3File> listFile() {
        List<S3File> listFile = new ArrayList<S3File>();
        try {
            String fileName, linkDownload;
            System.out.println("Listing objects");
            final ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName).withMaxKeys(4);
            ListObjectsV2Result result;
            do {
                result = s3client.listObjectsV2(req);

                for (S3ObjectSummary objectSummary :
                        result.getObjectSummaries()) {
                    System.out.println(" - " + objectSummary.getKey() + "  " +
                            "(size = " + objectSummary.getSize() +
                            ")");
                    fileName = objectSummary.getKey();
                    linkDownload = "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
                    S3File s3File = new S3File(fileName, linkDownload);
                    listFile.add(s3File);
                }
                req.setContinuationToken(result.getNextContinuationToken());
            } while (result.isTruncated() == true);

        } catch (AmazonServiceException ase) {

        } catch (AmazonClientException ace) {

            System.out.println("Error Message: " + ace.getMessage());
        }
        if (listFile.size() == 0) {
            return null;
        }
        return listFile;
    }
}