package fi.academy.springauth.aws;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

@Service
public class AwsRekognitionService {

    @Value("${aws.s3.audio.bucket}")
    private String bucket;

    @Autowired
    private AmazonRekognition client;

    public AwsRekognitionService(AmazonRekognition client) {
        this.client = client;
    }

    /**
     * Uses AWS Rekognition to detect objects (labels) in uploaded picture (max 10, confidence 80),
     * used in local-dev
     * @param imageToCheck Multipartfile to be checked
     * @return List<Label> List of labels detected from picture
     * @throws IOException
     */
    public List<Label> detectUploadedLabelsResult(MultipartFile imageToCheck) throws IOException {
        DetectLabelsRequest request = new DetectLabelsRequest()
                .withImage(new Image().withBytes(ByteBuffer.wrap(imageToCheck.getBytes())))
                .withMaxLabels(10)
                .withMinConfidence(80F);

        DetectLabelsResult dl = client.detectLabels(request);
        return dl.getLabels();
    }

    /**
     * Uses AWS Rekognition to detect objects (labels) from picture in S3 bucket (max 10, confidence 80),
     * used in prod-dev
     * @param photo String, filenime in S3-bucket (as in DB)
     * @return
     */
    public List<Label> detectS3ImageLabelsResults(String photo) {
        DetectLabelsRequest request = new DetectLabelsRequest()
                .withImage(new Image()
                        .withS3Object(new S3Object()
                                .withName(photo).withBucket(bucket)))
                .withMaxLabels(10)
                .withMinConfidence(80F);

        DetectLabelsResult dl = client.detectLabels(request);
        return dl.getLabels();
    }

}