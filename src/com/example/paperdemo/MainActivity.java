package com.example.paperdemo;

import com.example.paperdemo.DummyShadowBuilder;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


/*displays 3 balls in top layout
displays tennis racket in bottom layout
can drag balls down - only tennis ball will remain in bottom layout
can also drag tennis ball to return to top layout

created using Android Studio (Beta) 0.8.6

www.101apps.co.za

*/
public class MainActivity extends Activity implements OnDragListener, View.OnLongClickListener, OnTouchListener {

    private static final String TAG = "TEST";
    public static TextView tv, tvDrag;
    View ball;  ImageView ball1;
    LinearLayout canvas;
    RelativeLayout rl;
    private LayoutParams layoutParams;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView)findViewById(R.id.tv);
        tvDrag = (TextView)findViewById(R.id.tvDrag);
        ball = (View) findViewById(R.id.tennis);
        canvas = (LinearLayout) findViewById(R.id.canvas);
        rl = (RelativeLayout) findViewById(R.id.top_container);
        
        ball1=new ImageView(this);
        ball1= (ImageView) ball;
        
        // register a long click listener for the balls

        findViewById(R.id.tennis).setOnLongClickListener(this);
        
        //  register drag event listeners for the target layout containers
        findViewById(R.id.top_container).setOnDragListener(this);
        
        
        canvas.setOnTouchListener(this);
    }

    //    called when ball has been touched and held
    @Override
    public boolean onLongClick(View imageView) {
        // the ball has been touched
    	// create clip data holding data of the type MIMETYPE_TEXT_PLAIN
        ClipData clipData = ClipData.newPlainText("", "");

        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(imageView);
        /*start the drag - contains the data to be dragged,
        metadata for this data and callback for drawing shadow*/
        imageView.startDrag(clipData, shadowBuilder, imageView, 0);
        // we're dragging the shadow so make the view invisible
        imageView.setVisibility(View.INVISIBLE);
        return true;
    }
    

    //    called when the ball starts to be dragged
    //    used by top and bottom layout containers
    @Override
    public boolean onDrag(View receivingLayoutView, DragEvent dragEvent) {
        View draggedImageView = (View) dragEvent.getLocalState();
        
        
        draggedImageView.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (50+(100*(dragEvent.getY()/500))), (int) (50+(100*(dragEvent.getY()/500))));
 
        params.leftMargin = (int) (50 + dragEvent.getX());
        params.topMargin = (int) (dragEvent.getY());
        rl.removeView(ball1);
        rl.addView(ball1, params);
        
        //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) (30+(50*(dragEvent.getY()/1000))), (int) (30+(50*(dragEvent.getY()/1000))));
        //ball1.setLayoutParams(layoutParams);
        
        
        //draggedImageView.setScaleX(1+(dragEvent.getY()/1000));
        //draggedImageView.setScaleY(1+(dragEvent.getY()/1000));
        
        tvDrag.setText("Drag = "+ Float.toString(dragEvent.getX()) + "  ... "+ Float.toString(dragEvent.getY()));
        
        // Handles each of the expected events
        switch (dragEvent.getAction()) {

            case DragEvent.ACTION_DRAG_STARTED:
                Log.i(TAG, "drag action started");
             // Determines if this View can accept the dragged data
                if (dragEvent.getClipDescription()
                        .hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    Log.i(TAG, "Can accept this data");

                    // returns true to indicate that the View can accept the dragged data.
                    return true;

                } else {
                    Log.i(TAG, "Can not accept this data");

                }

                // Returns false. During the current drag and drop operation, this View will
                // not receive events again until ACTION_DRAG_ENDED is sent.
                return false;

            case DragEvent.ACTION_DRAG_ENTERED:
                Log.i(TAG, "drag action entered");
                // the drag point has entered the bounding box
                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                Log.i(TAG, "drag action location");                
                
                /*triggered after ACTION_DRAG_ENTERED
                stops after ACTION_DRAG_EXITED*/
                return true;

            case DragEvent.ACTION_DRAG_EXITED:
                Log.i(TAG, "drag action exited");
                // the drag shadow has left the bounding box
                return true;

            case DragEvent.ACTION_DROP:
                  /* the listener receives this action type when
                  drag shadow released over the target view
            the action only sent here if ACTION_DRAG_STARTED returned true
            return true if successfully handled the drop else false*/
                switch (draggedImageView.getId()) {
                    
                    case R.id.tennis:
                        Log.i(TAG, "Tennis ball");
                        //tv.setText(Float.toString(draggedImageView.getY()));
                        ViewGroup draggedImageViewParentLayout
                                = (ViewGroup) draggedImageView.getParent();
                        draggedImageViewParentLayout.removeView(draggedImageView);
                        RelativeLayout bottomRelativeLayout = (RelativeLayout) receivingLayoutView;                        
                        bottomRelativeLayout.addView(draggedImageView);
                        //RelativeLayout.LayoutParams paramsCurr = new RelativeLayout.LayoutParams((int) (50+(200*(dragEvent.getY()/1000))), (int) (50+(200*(dragEvent.getY()/1000))));
                        //bottomRelativeLayout.addView(draggedImageView, paramsCurr);
                        draggedImageView.setVisibility(View.VISIBLE);
                        return true;
                    
                    default:
                        Log.i(TAG, "in default");
                        return false;
                }

            case DragEvent.ACTION_DRAG_ENDED:
                Log.i(TAG, "drag action ended");
                Log.i(TAG, "getResult: " + dragEvent.getResult());
                
                draggedImageView.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
                params2.leftMargin = 0;
                params2.topMargin = 0;
                rl.removeView(ball1);
                rl.addView(ball1, params2);

                //   if the drop was not successful, set the ball to visible
                if (!dragEvent.getResult()) {
                    Log.i(TAG, "setting visible");
                    draggedImageView.setVisibility(View.VISIBLE);
                }

                return true;
            // An unknown action type was received.
            default:
                Log.i(TAG, "Unknown action type received by OnDragListener.");
                break;
        }
        return false;
    }

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
	    int x; 
        int y;    

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
                x = (int) event.getX();
                y = (int) event.getY();   
                //method draws circle at x and y coordinate
                tv.setText("X = "+ x + "   ... Y = "+ y);
                break;
            }
            case MotionEvent.ACTION_MOVE:
            {
                x = (int) event.getX();
                y = (int) event.getY();
                tv.setText("X = "+ x + "   ... Y = "+ y);

                break;
            }
        } 
		return true;
	}
}
