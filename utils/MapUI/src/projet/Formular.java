package projet;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class Formular extends PanelTab
{
	private BitmapFont mFont;
	private Drawable mCursor,mSelector,mBackground, mUp, mDown, mChecked;

	public Formular(BitmapFont font,PanelTabStyle style,Drawable cursor,Drawable selector,Drawable background,Drawable up,Drawable down,Drawable checked)
	{
		super(ORIENTATION.left, 0,0,0,0, style);
		mFont = font;
		mCursor = cursor;
		mSelector = selector;
		mBackground = background;
		mUp = up;
		mDown = down;
		mChecked = checked;
	}
	public TextFieldEditor addTextField(String name, String text)
	{
		return addTextField(name, text,mFont,Color.WHITE,Color.BLACK, mCursor, mSelector, mBackground);
	}
	public LabelEditor addInformation(String name, String info)
	{
		return addInformation(name, info, mFont,Color.WHITE,Color.WHITE);
	}
	public ButtonEditor addButton(String text)
	{
		return addButton(text,mFont, mUp, mDown, mChecked);
	}
	/*
	public String getTextFieldValue(String name)
	{
		Actor actor = getWidgetByName(name).getChildren().get(0);
		if (actor instanceof TextFieldEditor)
			return ((TextFieldEditor)actor).getTextFieldValue();
		else
			throw new RuntimeException("This field is not a TextField");
	}
	*/
	@Override
	public void update(float windowWidth,float windowHeight)
	{
		setWidth(windowWidth*0.5f);
		setHeight(windowHeight);
		super.update(windowWidth,windowHeight);
	}
}