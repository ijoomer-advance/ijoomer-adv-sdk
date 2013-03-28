package com.ijoomer.customviews;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.ijoomer.src.R;

/**
 * This Class Used To Play Gif Image.
 * 
 * @author tasol
 * 
 */
public class IjoomerGifView extends View {
	Movie movie;
	InputStream is;
	long moviestart;

	public IjoomerGifView(Context context) throws IOException {
		this(context, null);

	}

	public IjoomerGifView(Context context, AttributeSet attr) throws IOException {
		super(context, attr);

		is = getResources().openRawResource(R.drawable.ijoomer_loading_gif);
		movie = Movie.decodeStream(is);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		setMeasuredDimension(getResources().getDrawable(R.drawable.ijoomer_loading_gif).getIntrinsicWidth(), getResources().getDrawable(R.drawable.ijoomer_loading_gif)
				.getIntrinsicHeight());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		long now = android.os.SystemClock.uptimeMillis();
		Paint p = new Paint();
		p.setAntiAlias(true);
		if (moviestart == 0) { // first time
			moviestart = now;

		}
		int relTime = (int) ((now - moviestart) % movie.duration());
		movie.setTime(relTime);
		movie.draw(canvas, 0, 0);
		this.invalidate();
	}

}
