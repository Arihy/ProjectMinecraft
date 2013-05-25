package projet;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;

public class ScrollPanel extends ScrollPane
{

	public ScrollPanel(Actor widget,ScrollPaneStyle style) 
	{
		super(widget,style);
		setSmoothScrolling(true);
		setupOverscroll(0, 0, 0);
		setFadeScrollBars(false);
	}
	@Override
	public float getPrefWidth() 
	{
		return getParent().getWidth();
	}
	@Override
	public float getPrefHeight() 
	{
		return getParent().getHeight();
	}
	@Override
	public float getMinWidth()
	{
		return 0;
	}
	@Override
	public float getMinHeight()
	{
		return 0;
	}
	@Override
	public float getMaxWidth() 
	{
		return getParent().getWidth();
	}
	@Override
	public float getMaxHeight() 
	{
		return getParent().getHeight();
	}
	@Override
	public void draw(SpriteBatch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);
		batch.setColor(Color.WHITE);
	}	
}