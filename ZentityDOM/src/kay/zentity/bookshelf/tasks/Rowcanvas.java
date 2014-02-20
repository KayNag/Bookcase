package kay.zentity.bookshelf.tasks;

import kay.zentity.bookshelf.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.GridView;

public class Rowcanvas extends GridView {
	  // Constructors etc
Bitmap mWoodPanelImage;
	  public Rowcanvas(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		rowbackground();
		
	}
	  public Rowcanvas(Context context, AttributeSet attrs) {
			super(context, attrs);
			rowbackground();
		}

	  public  Rowcanvas(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			rowbackground();
		}


	public void rowbackground() {
		// TODO Auto-generated method stub
		mWoodPanelImage = BitmapFactory.decodeResource(getResources(), R.drawable.shelfcell_bgr);
	}


	 @Override
	    protected void dispatchDraw(Canvas canvas) {
	        final int count = getChildCount();
	        final int top = count > 0 ? getChildAt(0).getTop() : 0;
	        final int shelfWidth = mWoodPanelImage.getWidth();
	        final int shelfHeight = mWoodPanelImage.getHeight();
	        final int width = getWidth();
	        final int height = getHeight();
	        final Bitmap background = mWoodPanelImage;

	        for (int x = 0; x < width; x += shelfWidth) {
	            for (int y = top; y < height; y += shelfHeight) {
	                canvas.drawBitmap(background, x, y, null);
	            }
	        }

	       
	        super.dispatchDraw(canvas);
	    }
	}
