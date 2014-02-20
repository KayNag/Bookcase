package kay.zentity.bookshelf.tasks;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.security.KeyStore;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import kay.zentity.bookshelf.ZentityEntrance;
import kay.zentity.bookshelf.data.ZentityDataFetch;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.ProgressDialog;
import android.content.Context;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * AsyncTask for fetching tickets from Zendesk (very important part of app,do
 * changes carefully )
 * 
 * @author Kay Nag
 */
public class ZentityXmlTasker extends AsyncTask<String, Integer, String>  {
	private ProgressDialog progDialog;
	private Context context;
	Rowcanvas canvas;
	private ZentityEntrance activity;
	private static final int HTTP_STATUS_OK = 200;
	private static byte[] buff = new byte[1024];
	private static final String logTag = "ZentityXmlPull";
	
	private static final String debugTag = "ZentityXmlTasker";

	/**
	 * Construct a task
	 * 
	 * @param activity
	 */
	public ZentityXmlTasker(ZentityEntrance c,Rowcanvas canvas) {
		super();
		
		this.activity = c;
		this.canvas = canvas;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progDialog = new ProgressDialog(this.activity);
		progDialog.setMessage("Please , Wait . Loading Books");
		progDialog.show();
		
	}

	@Override
	protected String doInBackground(String... params) {
		String result = null;
		
		try {
			
			Log.d(debugTag, "Background:" + Thread.currentThread().getName());
			 
			
			String url = "http://lukaspetrik.cz/filemanager/tmp/reader/data.xml";

			Log.d(logTag, "Fetching " + url);

			// create an http client and a request object.
			HttpGet request = new HttpGet(url);
			
			HttpClient client =  getNewHttpClient();

		

				// execute the request
				HttpResponse response = client.execute(request);

				StatusLine status = response.getStatusLine();
				if (status.getStatusCode() != HTTP_STATUS_OK) {
					// handle error here
				
					progDialog.setMessage("Invalid response from zendesk"
							+ status.toString());
				
				}

				// process the content.
				HttpEntity entity = response.getEntity();
				InputStream ist = entity.getContent();
				ByteArrayOutputStream content = new ByteArrayOutputStream();

				int readCount = 0;
				while ((readCount = ist.read(buff)) != -1) {
					content.write(buff, 0, readCount);
				}
				result = new String(content.toByteArray());

			} catch (Exception e) {
				progDialog.setMessage("Problem connecting to the server "
						+ e.getMessage());
				
					e.printStackTrace();
				}
			
		return result;
				
			
		
	}

	@Override
	protected void onPostExecute(String result) {

		ArrayList<ZentityDataFetch> ticketsdata = new ArrayList<ZentityDataFetch>();
		

		progDialog.dismiss();
		if (result.length() == 0) {
			this.activity.alert("Unable to find books. Try again later.");
			return;
		}

		Document doc = XMLfromString(result);
		int numResults = numResults(doc);
		 if((numResults >= 0)){
		    	Toast.makeText(this.context, "There is no data in the xml file", Toast.LENGTH_LONG).show();  
		    	
		    }
		 NodeList books = doc.getElementsByTagName("BOOK");
		
		
		for (int i = 0; i < books.getLength(); i++) {
			Element e = (Element)books.item(i);
			String ID = getValue(e, "ID");
			String TITLE = getValue(e, "TITLE");
			String imageUrl = getValue(e,"THUMBNAIL");
			String STATUS = getValue(e,"NEW");
			

			ticketsdata.add(new ZentityDataFetch(ID, TITLE, imageUrl,STATUS));
		}

		this.activity.settickets(ticketsdata);
		
		

		
	}
public final static Document XMLfromString(String xml){
		
		Document doc = null;
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
        	
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			InputSource is = new InputSource();
	        is.setCharacterStream(new StringReader(xml));
	        doc = db.parse(is); 
	        
		} catch (ParserConfigurationException e) {
			System.out.println("XML parse error: " + e.getMessage());
			return null;
		} catch (SAXException e) {
			System.out.println("Wrong XML file structure: " + e.getMessage());
            return null;
		} catch (IOException e) {
			System.out.println("I/O exeption: " + e.getMessage());
			return null;
		}
        return doc;
	}

public static int numResults(Document doc){		
	Node results = doc.getDocumentElement();
	int res = -1;
	try{
		res = Integer.valueOf(results.getAttributes().getNamedItem("count").getNodeValue());
	}catch(Exception e ){
		res = -1;
	}
	return res;
}
public static HttpClient getNewHttpClient() {
	try {
		KeyStore trustStore = KeyStore.getInstance(KeyStore
				.getDefaultType());
		trustStore.load(null, null);

		SSLSocketFactory sf = new SSLSocket(trustStore);
		sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		registry.register(new Scheme("https", sf, 443));

		ClientConnectionManager ccm = new ThreadSafeClientConnManager(
				params, registry);

		return new DefaultHttpClient(ccm, params);
	} catch (Exception e) {
		return new DefaultHttpClient();
	}
}


public static String getValue(Element item, String str) {		
NodeList n = item.getElementsByTagName(str);		
return getElementValue(n.item(0));
}
public final static String getElementValue( Node elem ) {
Node kid;
if( elem != null){
    if (elem.hasChildNodes()){
        for( kid = elem.getFirstChild(); kid != null; kid = kid.getNextSibling() ){
            if( kid.getNodeType() == Node.TEXT_NODE  ){
                return kid.getNodeValue();
            }
        }
    }
}
return "";
}

}