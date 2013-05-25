package projet;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class ButtonEditor extends Table
{
	private ButtonEditorListener mListener;
	private TextButton mButton;
	private boolean mSaveStat;
	
	public ButtonEditor(String text,BitmapFont font,Drawable up,Drawable down,Drawable checked)
	{
		initButton(text,font, up, down, checked);
		add(mButton).expand().fill();
		mSaveStat = mButton.isChecked();
	}
	private void initButton(String text,BitmapFont font,Drawable up,Drawable down,Drawable checked)
	{
		TextButtonStyle style = new TextButtonStyle(up, down, checked);
		style.font = font;
		style.fontColor = Color.BLACK;
		mButton = new TextButton(text,style)
		{
			@Override
			public float getMinHeight() 
			{
				return 32;
			}
			@Override
			public float getMinWidth()
			{
				return 0;
			}
			@Override
			public float getPrefWidth()
			{
				return 500;
			}
			@Override
			public void act(float arg0)
			{
				super.act(arg0);
				if (mListener != null && isPressed() && isChecked() == mSaveStat)
				{
					mSaveStat = !mSaveStat;
					mListener.onCLick();
				}
			}
		};
	}
	public void setText(String text)
	{
		
	}
	public void setButtonEditorListener(ButtonEditorListener listener)
	{
		mListener = listener;
	}
	public interface ButtonEditorListener
	{
		public void onCLick();
	}
}
