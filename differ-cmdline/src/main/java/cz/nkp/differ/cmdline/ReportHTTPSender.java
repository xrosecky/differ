package cz.nkp.differ.cmdline;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: stavel
 * Date: 30.1.13
 * Time: 6:51
 * To change this template use File | Settings | File Templates.
 */
public class ReportHTTPSender {
    private String url;
    private String user;
    private String password;

    public void setUrl(String url){
        this.url = url;
    }
    public String getUrl(){
        return this.url;
    }
    public void setUser(String user){
         this.user = user;
    }
    public String getUser(){
         return this.user;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return this.password;
    }

    public HttpResponse sendReport(File report){
        HttpResponse response = null;
        HttpPost httpPost = new HttpPost(this.url);
        HttpClient client = new DefaultHttpClient();
        FileEntity input = new FileEntity(report);
	    input.setContentType("application/xml");
		httpPost.setEntity(input);
        try {
            response = client.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.getConnectionManager().shutdown();
        return response;
    }
}
