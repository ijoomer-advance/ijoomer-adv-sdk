package com.ijoomer.customviews;

import java.util.Map;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.KeyListener;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;

/**
 * This Class Contains All Method Related To IjoomerEditText.
 * 
 * @author tasol
 * 
 */
public class IjoomerEditText extends EditText implements KeyListener {

	private boolean isDecodeEmojis() {
		return isDecodeEmojis;
	}

	public void setDecodeEmojis(boolean decodeEmojis) {
		isDecodeEmojis = decodeEmojis;
	}

	private boolean isDecodeEmojis;

	public IjoomerEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public IjoomerEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public IjoomerEditText(Context context) {
		super(context);
		init(context);
	}

	private void init(Context mContext) {

		try {
			if (IjoomerApplicationConfiguration.getFontFace() != null) {
				setTypeface(IjoomerApplicationConfiguration.getFontFace());
			} else {
				Typeface tf = Typeface.createFromAsset(mContext.getAssets(), IjoomerApplicationConfiguration.getFontNameWithPath());
				setTypeface(tf);
				IjoomerApplicationConfiguration.setFontFace(tf);
			}
		} catch (Throwable e) {
		}

		if (isDecodeEmojis()) {
			setKeyListener(this);
		}
	}

	@Override
	public void setText(CharSequence text, BufferType type) {

		if (isDecodeEmojis) {

			if (text != null && text.length() > 0) {
				try {
					StringBuffer buffer = new StringBuffer(super.getText().toString());
					buffer.insert(super.getSelectionStart(), text);
					super.setText(getSmiledText(buffer.toString()), type);
					super.setSelection(super.getText().length());
				} catch (Exception e) {
					super.setText(text, type);
				}
			} else {
				super.setText(text, type);
			}
		} else {
			super.setText(text, type);
		}
	}

	private Spannable getSmiledText(String text) {
		SpannableStringBuilder builder = new SpannableStringBuilder(text);
		if (IjoomerUtilities.getEmojisHashMap().size() > 0) {
			int index;
			for (index = 0; index < builder.length(); index++) {
				if (Character.toString(builder.charAt(index)).equals(":")) {
					for (Map.Entry<String, Integer> entry : IjoomerUtilities.getEmojisHashMap().entrySet()) {
						int length = entry.getKey().length();
						if (index + length > builder.length())
							continue;
						if (builder.subSequence(index, index + length).toString().equals(entry.getKey())) {
							builder.setSpan(new ImageSpan(getContext(), entry.getValue()), index, index + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							index += length - 1;
							break;
						}
					}
				}
			}
		}
		return builder;
	}

	@Override
	public int getInputType() {
		return InputType.TYPE_CLASS_TEXT;
	}

	@Override
	public boolean onKeyDown(View view, Editable editable, int keyCode, KeyEvent keyEvent) {
		if (keyCode == KeyEvent.KEYCODE_DEL) {
			if (getText().toString().length() >= 3) {
				if (getSelectionStart() != getText().toString().length()
						&& IjoomerUtilities.getEmojisHashMap().containsKey(getText().toString().substring(getSelectionStart() - 3, getSelectionStart()))) {
					StringBuffer temp = new StringBuffer(getText().toString().substring(0, getSelectionStart() - 3));
					temp.append(getText().toString().substring(getSelectionStart(), getText().toString().length()));
					setText(temp.toString());
				} else {
					setText(getText().toString().substring(0, getText().toString().length() - 3));
				}
				return true;
			} else if (getText().toString().length() == 2) {
				setText(getText().subSequence(0, 1));
				return true;
			} else {
				setText("");
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onKeyUp(View view, Editable editable, int i, KeyEvent keyEvent) {
		return false;
	}

	@Override
	public boolean onKeyOther(View view, Editable editable, KeyEvent keyEvent) {
		return false;
	}

	@Override
	public void clearMetaKeyState(View view, Editable editable, int i) {

	}
}
