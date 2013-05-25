package projet;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class LabelEditor extends Table
{
	private Label mLabel;
	private Label mLabelValue;

	public LabelEditor(String name, String value,BitmapFont font,Color colorName,Color colorValue)
	{
		super();
		initLabeLname(name,font,colorName);
		initLabelValue(value,font,colorValue);
		add(mLabel).expandX().left();
		add(mLabelValue).expandX().right();
	}
	private void initLabeLname(String name,BitmapFont font,Color color)
	{
		LabelStyle style = new LabelStyle(font, color);
		mLabel = new Label(name, style)
		{
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
	private void initLabelValue(String value,BitmapFont font,Color color)
	{
		if (value == null)
			value = "";
		LabelStyle style = new LabelStyle(font, color);
		mLabelValue = new Label(value, style)
		{
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
}
