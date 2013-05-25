package projet;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class TextFieldEditor extends Table
{
	private Label mLabel;
	private TextField mTextField;

	public TextFieldEditor(String name, String value,BitmapFont font,Color colorName,Color colorValue,Drawable cursor,Drawable selector,Drawable background) 
	{
		super();
		initLabel(name,font,colorName);
		initTextField(value,font,colorValue, cursor, selector,  background);
		add(mLabel).expandX().left();
		add(mTextField).expandX().right();
	}
	public String getTextFieldValue()
	{
		return mTextField.getText();
	}
	private void initLabel(String name,BitmapFont font,Color color)
	{
		LabelStyle styleLabel = new LabelStyle(font, color);

		mLabel = new Label(name, styleLabel)
		{
			@Override
			public void draw(SpriteBatch batch, float parentAlpha) {
				//System.out.println(getWidth());
				super.draw(batch, parentAlpha);
			}
			@Override
			public float getMinHeight() 
			{
				return getStyle().font.getCapHeight();
			}
			@Override
			public float getMinWidth() 
			{
				return 0;
			}
		};
	}
	private void initTextField(String value,BitmapFont font,Color color,Drawable cursor,Drawable selector,Drawable background)
	{
		if (value == null)
			value = "";
		TextFieldStyle styleTextField = new TextFieldStyle(font,color, cursor, selector, background);
		mTextField = new TextField(value, styleTextField)
		{
			@Override
			public void draw(SpriteBatch batch, float parentAlpha) 
			{
				//System.out.println(getWidth());
				super.draw(batch, parentAlpha);
			}
			@Override
			public float getMinHeight() 
			{
				return getStyle().font.getCapHeight();
			}
			@Override
			public float getMinWidth() 
			{
				return 0;
			}
		};
		//mTextField.setRightAligned(true);
	}
}
