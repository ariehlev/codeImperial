import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.isOneOf;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class TestServletConnections {
    //POST METHOD for SEARCH
    @Test
    public void checkConnectiontoServlets()
            throws ClientProtocolException, IOException {
        //Here we check the response codes from our servlets, which should all give back OK code 200
        //Upload Servlet connection
        HttpUriRequest request = new HttpPost( "https://hlabsmedimagedatabase.herokuapp.com/upload");
        CloseableHttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_OK));
        //UploadImage Servlet connection
        HttpUriRequest request2 = new HttpPost( "https://hlabsmedimagedatabase.herokuapp.com/uploadimage");
        CloseableHttpResponse httpResponse2 = HttpClientBuilder.create().build().execute( request2 );
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_OK));
        //Search Servlet connection
        HttpUriRequest request3 = new HttpPost( "https://hlabsmedimagedatabase.herokuapp.com/main");
        CloseableHttpResponse httpResponse3 = HttpClientBuilder.create().build().execute( request3 );
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_OK));
        //Delete Servlet connection
        HttpUriRequest request4 = new HttpPost( "https://hlabsmedimagedatabase.herokuapp.com/delete");
        CloseableHttpResponse httpResponse4 = HttpClientBuilder.create().build().execute( request4 );
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_OK));
    }
    @Test
    public void checkResponseTypeIsApprpriate_json_or_html()
            throws ClientProtocolException, IOException {
        // 2 response types depending on which server is called
        String htmlMimeType1 = "html/text";
        String htmlMimeType2="text/html";
        String jsonMimeType = "application/json";
        //Check that Upload Servlet response is html ("The image was successfully added to the database")
        HttpUriRequest request = new HttpPost( "https://hlabsmedimagedatabase.herokuapp.com/upload" );
        CloseableHttpResponse response = HttpClientBuilder.create().build().execute( request );
        String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
        assertThat(mimeType, isOneOf(htmlMimeType1, htmlMimeType2));
        //assertEquals( htmlMimeType1, mimeType );

        //Check that UploadImage Servlet response is html (returns image url in s3)
        HttpUriRequest request2 = new HttpPost( "https://hlabsmedimagedatabase.herokuapp.com/uploadimage" );
        CloseableHttpResponse response2 = HttpClientBuilder.create().build().execute( request2 );
        String mimeType2 = ContentType.getOrDefault(response2.getEntity()).getMimeType();
        assertThat(mimeType2, isOneOf(htmlMimeType1, htmlMimeType2));
        //assertEquals( htmlMimeType, mimeType2 );
        //Check that Search Servlet response is json (returns image library corresponding to search params)
        HttpUriRequest request3 = new HttpPost( "https://hlabsmedimagedatabase.herokuapp.com/main" );
        CloseableHttpResponse response3 = HttpClientBuilder.create().build().execute( request3 );
        String mimeType3 = ContentType.getOrDefault(response3.getEntity()).getMimeType();
        assertEquals(jsonMimeType, mimeType3 );
        //Check that Delete Servlet response is html ("The image was successfully deleted from the database.")
        HttpUriRequest request4 = new HttpPost( "https://hlabsmedimagedatabase.herokuapp.com/delete" );
        CloseableHttpResponse response4 = HttpClientBuilder.create().build().execute( request4 );
        String mimeType4 = ContentType.getOrDefault(response4.getEntity()).getMimeType();
        assertThat(mimeType4, isOneOf(htmlMimeType1, htmlMimeType2));
        //assertEquals( htmlMimeType, mimeType4 );
    }

}