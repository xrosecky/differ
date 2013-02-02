package cz.nkp.differ.compare.metadata.external;

import cz.nkp.differ.compare.metadata.ImageMetadata;
import cz.nkp.differ.compare.metadata.MetadataExtractor;
import cz.nkp.differ.compare.metadata.MetadataSource;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: stavel
 * Date: 23.1.13
 * Time: 18:41
 * To change this template use File | Settings | File Templates.
 */
public class DaitssHTTPMetadataExtractor implements MetadataExtractor {
    private String url;
    private ResultTransformer transformer;
    private Map<String, String> units;
    private String source;

    public void setSource(String source){
        this.source = source;
    }
    public String getSource(){
        return this.source;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public String getUrl(){
        return this.url;
    }
    public void setTransformer(ResultTransformer transformer){
        this.transformer = transformer;
    }
    public ResultTransformer getTransformer(){
        return this.transformer;
    }
    public void setUnits(Map<String,String> units){
        this.units = units;
    }
    public Map<String,String> getUnits (){
        return this.units;
    }

    @Override
    public List<ImageMetadata> getMetadata(File imageFile) {
        HttpPost httpPost = new HttpPost(this.url);
        httpPost.setEntity(this.getEntity(imageFile));
        HttpClient client = new DefaultHttpClient();
        String response = "";
        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            response = client.execute(httpPost, responseHandler);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            client.getConnectionManager().shutdown();
        }
        List<ImageMetadata> result = new ArrayList<ImageMetadata>();
        MetadataSource metadataSource = new MetadataSource(0, response.toString(), new String(""), this.source);
        result.add(new ImageMetadata("File name", imageFile.getName(), metadataSource));

        result.add(new ImageMetadata("exit-code", "OK", metadataSource));

		Map<String, MetadataSource> sources = new HashMap<String, MetadataSource>();
        byte[] stderr = new byte[1];
        try{
            List<ResultTransformer.Entry> entries = this.transformer.transform(response.getBytes(), stderr);
            for (ResultTransformer.Entry entry : entries) {
		        ImageMetadata metadata = null;
		        if (entry.getSource() == null) {
			        metadata = new ImageMetadata(entry.getKey(), entry.getValue(), metadataSource);
		        } else {
			        MetadataSource newSource = sources.get(entry.getSource());
			        if (newSource == null) {
			            newSource = new MetadataSource(0, new String(),	new String(), entry.getSource());
			            sources.put(entry.getSource(), newSource);
			        }
			        metadata = new ImageMetadata(entry.getKey(), entry.getValue(), newSource);
		        }
                if (units != null) {
                    String unit = units.get(entry.getKey());
                    metadata.setUnit(unit);
                }
                result.add(metadata);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private MultipartEntity getEntity(File imageFile) {
        MultipartEntity entity = new MultipartEntity();
        try{
            String filename = imageFile.getName();
            int dot = filename.lastIndexOf('.');
            String extension = filename.substring(dot+1);
            entity.addPart("extension", new StringBody(extension));
            entity.addPart("document", new FileBody(imageFile,  "application/octet-stream"));
        } catch (UnsupportedEncodingException ex){
            ex.printStackTrace(System.err);
        }
        return entity;
    }
}
