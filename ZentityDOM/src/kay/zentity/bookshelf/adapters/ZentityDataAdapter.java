package kay.zentity.bookshelf.adapters;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import kay.zentity.bookshelf.R;
import kay.zentity.bookshelf.ZentityEntrance;
import kay.zentity.bookshelf.ZentityEntrance.MyViewHolder;
import kay.zentity.bookshelf.data.ZentityDataFetch;
import kay.zentity.bookshelf.tasks.Rowcanvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adapter for tickets to fill in the rows in list view
 * 
 * @author Kay Nag
 * 
 */
public class ZentityDataAdapter extends BaseAdapter implements OnClickListener {

	private static final String debugTag = "ZentityDataAdapter";
	ImageView status ;
	
	private ZentityEntrance activity;
	
	
	private ArrayList<ZentityDataFetch> bookdata;

	public ZentityDataAdapter(ZentityEntrance a, 
			ArrayList<ZentityDataFetch> data) {
		this.activity = a;
		
		
		this.bookdata = data;
		
	}

	@Override
	public int getCount() {
		return this.bookdata.size();
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		MyViewHolder holder;
		
		View overlay;
				
		if (view == null) {
			
			overlay = new View(activity);
			
			overlay =  inflater.inflate(R.layout.rowsgird, parent,false);
			holder = new MyViewHolder();
			
			holder.ticket_no = (TextView)overlay
					.findViewById(R.id.text);
			
			holder.icon = (ImageView)overlay.findViewById(R.id.image);
			status = (ImageView)overlay.findViewById(R.id.newbook);

			overlay.setTag(holder);
		
		
			overlay.setOnClickListener(this);

		ZentityDataFetch position = bookdata.get(pos);
		holder.bookdata = position;
		if (position.getID() != null) {
			holder.ticket_no.setText(position.getTicketno());

		} else {
			holder.subject.setText("No Title");
		}

		
		if (position.getImageUrl()!= null) {
			Bitmap icon;
			try {
				icon = BitmapFactory.decodeStream((new URL(position.getImageUrl())).openConnection().getInputStream());
				holder.icon.setImageBitmap(icon);
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		else
		{
		
			holder.icon.setImageResource(R.drawable.filler_icon);
		
		}
			
		if (position.getTicketstatus().equals("TRUE")) {
					
			status.setImageResource(R.drawable.book_new);
			}
		 else {
			 status.setVisibility(View.INVISIBLE);
		}
		} else {
			//holder = (MyViewHolder) view.getTag();
			overlay = (View) view;
		}
		
		return overlay;
	}

	@Override
	public void onClick(View v) {

		Log.d(debugTag, "OnClick pressed.");

	}
	
}