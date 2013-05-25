package projet;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public class PanelTab extends Group
{
	public static enum ORIENTATION {left,right,top,bottom,floating};

	private Table mActor;
	private NinePatch mBackgroundActual;
	private Table mGroup;
	private HashMap<String, Table> mWidgets;
	private ScrollPanel mScrollPane;
	private ORIENTATION mOrientation;
	private Button mButton;
	private Image mIcon;
	private Image mIconArrow;
	private boolean mStatActif = true;
	private PanelTabStyle mStyle;
	private int mDecalX,mDecalY;

	public PanelTab(ORIENTATION orientation,int x,int y,int width,int height,PanelTabStyle style)
	{
		super();
		initActor();
		addActor(mActor);
		mStyle = style;
		mWidgets = new HashMap<String, Table>();
		mIcon = new Image();
		mIconArrow = new Image();
		setWidth(width);
		setHeight(height);
		initScrollPane(style);
		initButton();
		mActor.add(mScrollPane);
		setOrientation(orientation,x,y);
	}
	@Override
	public void setWidth(float width)
	{
		mActor.setWidth(width);
		super.setWidth(width);
	}
	@Override
	public void setHeight(float height)
	{
		mActor.setHeight(height);
		super.setHeight(height);
	}
	public void expand()
	{
		float mooveX = 0,mooveY = 0;


		if (mOrientation == ORIENTATION.left)
		{
			mooveX = 0;
			mooveY = getY();
		}
		else if (mOrientation == ORIENTATION.right)
		{
			mooveX =Gdx.graphics.getWidth()-getWidth();
			mooveY = getY();			
		}
		else if (mOrientation == ORIENTATION.top)
		{
			mooveX =getX();
			mooveY = Gdx.graphics.getHeight()-getHeight();			
		}
		else if (mOrientation == ORIENTATION.bottom)
		{
			mooveX =getX();
			mooveY = 0;
		}
		if (!mStatActif)
		{
			MoveToAction action = new MoveToAction();
			action.setDuration(0.5f);
			action.setX(mooveX);
			action.setY(mooveY);
			mStatActif = true;
			mActor.addAction(action);
		}
		mActor.invalidate();
		mDecalX = 0;
		mDecalY = 0;

	}
	public void unexpand()
	{
		float mooveX = 0,mooveY = 0;

		if (mOrientation == ORIENTATION.left)
		{
			mooveX = getX()-getWidth()+49;
			mooveY = getY();
		}
		else if (mOrientation == ORIENTATION.right)
		{
			mooveX = getX()+Gdx.graphics.getWidth()-49; // TODO pb
			mooveY = getY();
		}
		else if (mOrientation == ORIENTATION.top)
		{
			mooveX = getX();
			mooveY = getY()+Gdx.graphics.getHeight()-48; // TODO pb
		}
		else if (mOrientation == ORIENTATION.bottom)
		{
			mooveX = getX();
			mooveY = getY()-getHeight()+45;
		}
		if (mStatActif)
		{
			MoveToAction action = new MoveToAction();
			action.setDuration(0.5f);
			action.setX(mooveX);
			action.setY(mooveY);
			mActor.addAction(action);
			mStatActif = false;
		}
		mActor.invalidate();
		mDecalX = (int) (getX()-mooveX);
		mDecalY = (int) (getY()-mooveY);
	}
	public void setIcon(Drawable icon)
	{
		mIcon.setDrawable(icon);
		updateButton(true);
	}
	private void initScrollPane(PanelTabStyle tabStyle)
	{
		ScrollPaneStyle style = new ScrollPaneStyle(null, new NinePatchDrawable(tabStyle.scrollBackHorizontal), new NinePatchDrawable(tabStyle.scrollHorizontal), new NinePatchDrawable(tabStyle.scrollBackVertical), new NinePatchDrawable(tabStyle.scrollVertical));
		mScrollPane = new ScrollPanel(mGroup,style);
		mGroup.top();
		mScrollPane.setSmoothScrolling(true);
		mScrollPane.setupOverscroll(0, 0, 0);
		mScrollPane.setFadeScrollBars(false);
	}
	public void setScrollable(boolean horizontal,boolean vertical)
	{
		mScrollPane.setScrollingDisabled(!horizontal, !vertical);
	}
	private void updateButton(boolean rebuild)
	{		
		if (mOrientation == ORIENTATION.left)
		{
			if (mIcon.getDrawable() != null)
				if (mStatActif)
					mIconArrow.setDrawable(mStyle.smallArrowVerticalLeft);
				else
					mIconArrow.setDrawable(mStyle.smallArrowVerticalRight);
			else
				if (mStatActif)
					mIconArrow.setDrawable(mStyle.arrowVerticalLeft);
				else
					mIconArrow.setDrawable(mStyle.arrowVerticalRight);
		}
		else if (mOrientation == ORIENTATION.right)
		{
			if (mIcon.getDrawable() != null)
				if (mStatActif)
					mIconArrow.setDrawable(mStyle.smallArrowVerticalRight);
				else
					mIconArrow.setDrawable(mStyle.smallArrowVerticalLeft);
			else
				if (mStatActif)
					mIconArrow.setDrawable(mStyle.arrowVerticalRight);
				else
					mIconArrow.setDrawable(mStyle.arrowVerticalLeft);
		}
		else if (mOrientation == ORIENTATION.top)
		{
			if (mIcon.getDrawable() != null)
				if (mStatActif)
					mIconArrow.setDrawable(mStyle.smallArrowHorizontalUp);
				else
					mIconArrow.setDrawable(mStyle.smallArrowHorizontalDown);
			else
				if (mStatActif)
					mIconArrow.setDrawable(mStyle.arrowHorizontalUp);
				else
					mIconArrow.setDrawable(mStyle.arrowHorizontalDown);
		}
		else if (mOrientation == ORIENTATION.bottom)
		{
			if (mIcon.getDrawable() != null)
				if (mStatActif)
					mIconArrow.setDrawable(mStyle.smallArrowHorizontalDown);
				else
					mIconArrow.setDrawable(mStyle.smallArrowHorizontalUp);
			else
				if (mStatActif)
					mIconArrow.setDrawable(mStyle.arrowHorizontalDown);
				else
					mIconArrow.setDrawable(mStyle.arrowHorizontalUp);
		}
		if (rebuild)
		{
			mButton.reset();
			if (mIcon.getDrawable() != null)
			{
				mButton.add(mIcon).expand();
				if (mOrientation == ORIENTATION.left || mOrientation == ORIENTATION.right)
					mButton.row();
			}
			mButton.add(mIconArrow).expand();
		}
		if (mOrientation == ORIENTATION.left)
		{
			mButton.setWidth(45);
			mButton.setHeight(87);
			mButton.setX(mActor.getWidth()-50);
			mButton.setY(mActor.getHeight()-128);
		}
		else if (mOrientation == ORIENTATION.right)
		{
			mButton.setWidth(45);
			mButton.setHeight(87);
			mButton.setX(5);
			mButton.setY(mActor.getHeight()-125);
		}
		else if (mOrientation == ORIENTATION.top)
		{
			mButton.setWidth(87);
			mButton.setHeight(45);
			mButton.setX(mActor.getWidth()-170);
			mButton.setY(3);
		}
		else if (mOrientation == ORIENTATION.bottom)
		{
			mButton.setWidth(87);
			mButton.setHeight(45);
			mButton.setX(mActor.getWidth()-190);
			mButton.setY(mActor.getHeight()-45);
		}
	}
	private void initButton()
	{
		ButtonStyle style = new ButtonStyle(null, null, null);
		mButton = new Button(style);
		mActor.addActor(mButton);
	}
	public void setOrientation(ORIENTATION orient,int x,int y)
	{
		if (orient == null)
			throw new IllegalArgumentException();
		if (mOrientation == orient)
			return;
		mStatActif = true;
		if (orient == ORIENTATION.left || orient == ORIENTATION.right)
		{
			setY(y);
			if (orient == ORIENTATION.left)
			{
				mActor.getCell(mScrollPane).expand().padLeft(20).padRight(61).padTop(20).padBottom(20);
				mBackgroundActual = mStyle.panelLeft;
				setX(0);
			}
			else
			{
				mActor.getCell(mScrollPane).expand().padLeft(61).padRight(20).padTop(20).padBottom(20);
				mBackgroundActual = mStyle.panelRight;
			}
		}
		else if (orient == ORIENTATION.top || orient == ORIENTATION.bottom)
		{
			if (orient == ORIENTATION.top)
			{
				mActor.getCell(mScrollPane).expand().padLeft(20).padRight(61).padTop(20).padBottom(20);
				mBackgroundActual = mStyle.panelTop;
			}
			else
			{
				mActor.getCell(mScrollPane).expand().padLeft(14).padRight(10).padTop(59).padBottom(10);
				mBackgroundActual = mStyle.panelBottom;
			}
			setX(x);
		}
		else if (orient == ORIENTATION.floating)
		{
			mActor.getCell(mScrollPane).expand().fill();
			setX(x);
			setY(y);
		}
		mOrientation = orient;
		updateButton(true);

	}
	public ORIENTATION getOrientation()
	{
		return mOrientation;
	}
	public boolean widgetExist(String name)
	{
		return (mWidgets.containsKey(name));
	}
	public Table getWidgetByName(String name)
	{
		return mWidgets.get(name);
	}
	public void addWidget(String name, Table widget)
	{
		if (!widgetExist(name))
		{
			//widget.debug();
			mWidgets.put(name, widget);
			mGroup.add(widget).fillX().expandX().padRight(20).padTop(2).padBottom(2);
			mGroup.row();
		}
		mScrollPane.validate();
	}
	
	public void reset()
	{
		mWidgets.clear();
		mGroup.clear();
	}
	public TextFieldEditor addTextField(String name, String text,BitmapFont font,Color colorName,Color colorValue,Drawable cursor,Drawable selector,Drawable background)
	{
		TextFieldEditor field = new TextFieldEditor(name, text,font,colorName,colorValue, cursor, selector, background);
		addWidget(name, field);
		return field;
	}
	public LabelEditor addInformation(String name, String info,BitmapFont font,Color colorName,Color colorValue)
	{
		LabelEditor label = new LabelEditor(name, info,font,colorName,colorValue);
		addWidget(name, label);
		return label;
	}
	public ButtonEditor addButton(String text,BitmapFont font,Drawable up,Drawable down,Drawable checked)
	{
		ButtonEditor button = new ButtonEditor(text,font, up, down, checked);
		addWidget(text, button);
		return button;
	}
	private void initActor()
	{
		mActor = new Table()
		{
			@Override
			public void act(float delta) 
			{
				if (mButton.isPressed() && mActor.getActions().size == 0)
				{
					if (mStatActif)
						unexpand();
					else
						expand();
				}
				super.act(delta);
			}
			@Override
			public void draw(SpriteBatch batch, float parentAlpha) 
			{
				mBackgroundActual.draw(batch, mActor.getX(), mActor.getY(), mActor.getWidth(), mActor.getHeight());
				updateButton(false);
				super.draw(batch, parentAlpha);
			}
		};
		mGroup = new Table()
		{
			@Override
			public float getPrefWidth() 
			{
				return getParent().getWidth();
			}
			@Override
			public float getPrefHeight() 
			{
				float size = 0;
				for (Actor w : getChildren())
				{
					size += w.getHeight();
				}
				return size;
			}
		};
	}
	public void update(float windowsWidth,float windowHeight)
	{
		
		if (mStatActif)
			expand();
		else
			unexpand();
		
		if (mOrientation == ORIENTATION.right)
			mActor.setX(Gdx.graphics.getWidth()-getWidth()-mDecalX);
		else if (mOrientation == ORIENTATION.top)
			mActor.setY(Gdx.graphics.getHeight()-getHeight()-mDecalY);
		else if (mOrientation == ORIENTATION.left)
			mActor.setX(-mDecalX);
		else if (mOrientation == ORIENTATION.bottom)
			mActor.setY(-mDecalY);
			
		mActor.invalidate();

	}
}
