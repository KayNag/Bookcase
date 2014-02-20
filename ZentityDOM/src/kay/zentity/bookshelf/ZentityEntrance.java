package kay.zentity.bookshelf;

import java.util.ArrayList;

import kay.zentity.bookshelf.adapters.ZentityDataAdapter;
import kay.zentity.bookshelf.data.ZentityDataFetch;
import kay.zentity.bookshelf.tasks.Rowcanvas;

import kay.zentity.bookshelf.tasks.ZentityXmlTasker;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Fetch & display tickets from the Zentity
 * 
 * @author Kay Nag
 * 
 */
public class ZentityEntrance extends Activity{
	

	private ArrayList<ZentityDataFetch> bookdata;
	//private ListView ticket_list;
	Rowcanvas canvas;
	
	String Url = null;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.gridmain);
		

		
		this.canvas = (Rowcanvas)findViewById(R.id.gridViewbase);
		
		 
		
		
		try {
			 new ZentityXmlTasker(ZentityEntrance.this,canvas).execute(Url);

			
		} catch (Exception e) {
			
			alert(getResources().getString(R.string.no_books));
		}

		// Restore any already fetched data on orientation change.

	}

	/**
	 * alerter.
	 * 
	 * @param msg
	 *            - the message to toast.
	 */

	public void alert(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}

	/**
	 * Bundle to hold refs to row items views.
	 * 
	 */

	public static class MyViewHolder {
		public TextView subject, ticket_no, ticket_status, ticket_description;
		public ImageView icon;
		public ZentityDataFetch bookdata;
		
	
		
	}

	public void settickets(ArrayList<ZentityDataFetch> bookdata) {
		this.bookdata = bookdata;
		
		this.canvas.setAdapter(new ZentityDataAdapter(this,
				this.bookdata));
		
	}
	
	void call(){
		
		
	}
	
}

