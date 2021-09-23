

public class EventHandler implements RequestHandler<ScheduledEvent, String> {

    /**
     * Shipment events for a carrier are uploaded to separate S3 buckets based on the source of events. E.g., events originating from
     * the hand-held scanner are stored in a separate bucket than the ones from mobile App. The Lambda processes events from multiple
     * sources and updates the latest status of the package in a summary S3 bucket every 15 minutes.
     * 
     * The events are stored in following format:
     * - Each status update is a file, where the name of the file is tracking number + random id.
     * - Each file has status and time-stamp as the first 2 lines respectively.
     * - The time at which the file is stored in S3 is not an indication of the time-stamp of the event.
     * - Once the status is marked as DELIVERED, we can stop tracking the package.
     * 
     * A Sample files looks as below:
     *  FILE-NAME-> '8787323232232332--55322798-dd29-4a04-97f4-93e18feed554'
     *   >status:IN TRANSIT
     *   >timestamp: 1573410202
     *   >Other fields like...tracking history and address
     */
    public String handleRequest(ScheduledEvent scheduledEvent, Context context) {
      String bucketName = System.getenv("S3_BUCKET_NAME");
        String fileName = scheduledEvent.getRecords().get(0).getS3().getObject().getKey();
        String trackingNumber = fileName.split("--")[0];
        String randomId = fileName.split("--")[1];
        String status = "";
        String timestamp = "";
        StringBuilder sb = new StringBuilder();
        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
            S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, fileName));
            BufferedReader reader = new BufferedReader(new InputStreamReader(s3Object.getObjectContent()));
            status = reader.readLine();
            timestamp = reader.readLine();
            String line = reader.readLine();
            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder sb1 = new StringBuilder();
        sb1.append("Tracking Number: ").append(trackingNumber).append("\n");
        sb1.append("Status: ").append(status).append("\n");
        sb1.append("Timestamp: ").append(timestamp).append("\n");
        sb1.append("Details: ").append(sb.toString());
        return sb1.toString();
    }
}