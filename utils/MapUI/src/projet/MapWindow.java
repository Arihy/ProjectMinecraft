package projet;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import map.Map;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MapWindow implements ApplicationListener
{
	private static MapWindow mInstance;

	private AssetManager mManager;
	private Stage mPrincipalStage;
	private Stage mStageUI;
	private Formular mFormular;
	private PreviewMap mMapReader;
	private CountDownLatch mLatch;
	private InputMultiplexer mMultiPlexer;

	Drawable mCursor,mSelector,mBackground, mUp, mDown, mChecked;
	PanelTabStyle mPanelStyle;
	BitmapFont mFont;

	public static MapWindow getInstance()
	{
		if (mInstance == null)
		{
			LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
			config.width = 800;
			config.height = 600;
			config.useGL20 = true;
			mInstance = new MapWindow();
			new LwjglApplication(mInstance,config);
		}
		return mInstance;
	}
	private MapWindow()
	{
		mLatch = new CountDownLatch(1);
	}
	public Formular getFormular()
	{
		try
		{
			mLatch.await();
		} 
		catch (InterruptedException e)
		{
			e.printStackTrace();
			new RuntimeException("Thread error");
		}
		return mFormular;
	}

	@Override
	public void create()
	{
		mMultiPlexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(mMultiPlexer);
		mPrincipalStage = new Stage();
		mStageUI = new Stage();
		mManager = new AssetManager();
		loadAssets();
		mFormular = new Formular(mFont,mPanelStyle,mCursor,mSelector,mBackground,mUp,mDown,mChecked);
		mStageUI.addActor(mFormular);
		mFormular.setWidth(Gdx.graphics.getWidth()/3);
		mFormular.setHeight(Gdx.graphics.getHeight());
		mMultiPlexer.addProcessor(mStageUI);
		mMultiPlexer.addProcessor(mPrincipalStage);
		mLatch.countDown();
	}
	public void setMap(Map map)
	{
		if (mPrincipalStage.getActors().size > 0)
			mPrincipalStage.getActors().get(0).remove();
		if (map != null)
		{
			try
			{
				mMapReader = new PreviewMap(map, 0, 0, (int)mPrincipalStage.getWidth(),(int) mPrincipalStage.getHeight());
				mPrincipalStage.addActor(mMapReader);
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	@Override
	public void resize(int width, int height)
	{
		mStageUI.setViewport(width, height, false);
		mFormular.update(width, height);
	}

	@Override
	public void render()
	{
		Gdx.gl.glClear(GL11.GL_COLOR_BUFFER_BIT);
		mStageUI.act();
		mPrincipalStage.act();
		mPrincipalStage.draw();
		mStageUI.draw();
	}

	@Override
	public void pause()
	{

	}

	@Override
	public void resume()
	{

	}

	@Override
	public void dispose()
	{

	}
	private void loadAssets()
	{
		String uiPath = "ui/";
		String left_pan = uiPath+"panels/left_pan.png";
		String right_pan = uiPath+"panels/right_pan.png";
		String top_pan = uiPath+"panels/top_pan.png";
		String bottom_pan = uiPath+"panels/bottom_pan.png";
		String arrowLeft = uiPath+"icons/arrowleft.png";
		String arrowRight = uiPath+"icons/arrowright.png";
		String arrowUp = uiPath+"icons/arrowup.png";
		String arrowDown = uiPath+"icons/arrowdown.png";
		String arrowLittleLeft = uiPath+"icons/arrowlittle-left.png";
		String arrowLittleRight = uiPath+"icons/arrowlittle-right.png";
		String arrowLittleUp = uiPath+"icons/arrowlittle-up.png";
		String arrowLittleDown = uiPath+"icons/arrowlittle-down.png";
		String scrollV = uiPath+"scrolls/scroller_v.png";
		String scrollBV = uiPath+"scrolls/whitebar_v.png";
		String scrollH = uiPath+"scrolls/scroller_h.png";
		String scrollBH = uiPath+"scrolls/whitebar_h.png";
		String buttonUp = uiPath+"other/up.png";
		String buttonDown = uiPath+"other/down.png";
		String buttonChecked = uiPath+"other/up.png";
		TextureParameter parameter = new TextureParameter();
		parameter.minFilter = TextureFilter.Linear;
		parameter.genMipMaps = true;
		mManager.load(left_pan,Texture.class,parameter);
		mManager.load(right_pan,Texture.class,parameter);
		mManager.load(top_pan,Texture.class,parameter);
		mManager.load(bottom_pan,Texture.class,parameter);
		mManager.load(arrowLeft,Texture.class,parameter);
		mManager.load(arrowRight,Texture.class,parameter);
		mManager.load(arrowUp,Texture.class,parameter);
		mManager.load(arrowDown,Texture.class,parameter);
		mManager.load(arrowLittleLeft,Texture.class,parameter);
		mManager.load(arrowLittleRight,Texture.class,parameter);
		mManager.load(arrowLittleUp,Texture.class,parameter);
		mManager.load(arrowLittleDown,Texture.class,parameter);
		mManager.load(scrollV,Texture.class,parameter);
		mManager.load(scrollBV,Texture.class,parameter);
		mManager.load(scrollH,Texture.class,parameter);
		mManager.load(scrollBH,Texture.class,parameter);
		mManager.load(buttonUp,Texture.class,parameter);
		mManager.load(buttonDown,Texture.class,parameter);
		mManager.load(buttonChecked,Texture.class,parameter);
		String font = "fonts/14B.fnt";
		mManager.load(font,BitmapFont.class);

		String blackPixel = uiPath+"other/black_pixel.jpg";
		mManager.load(blackPixel,Texture.class,parameter);
		String whitePixel = uiPath+"other/white_pixel.jpg";
		mManager.load(whitePixel,Texture.class,parameter);
		String bluePixelT = uiPath+"other/blue_pixel_T.png";
		mManager.load(bluePixelT,Texture.class,parameter);


		mManager.finishLoading();

		PanelTabStyle tabStyle = new PanelTabStyle();
		tabStyle.panelLeft = new NinePatch(mManager.get(left_pan,Texture.class),19,64,147,22);
		tabStyle.panelRight = new NinePatch(mManager.get(right_pan,Texture.class),63,19,151,18);
		tabStyle.panelTop = new NinePatch(mManager.get(top_pan,Texture.class),14,210,59,6);
		tabStyle.panelBottom = new NinePatch(mManager.get(bottom_pan,Texture.class),14,210,59,6);
		tabStyle.arrowHorizontalUp = new TextureRegionDrawable(new TextureRegion(mManager.get(arrowUp,Texture.class)));
		tabStyle.arrowHorizontalDown = new TextureRegionDrawable(new TextureRegion(mManager.get(arrowDown,Texture.class)));
		tabStyle.arrowVerticalLeft = new TextureRegionDrawable(new TextureRegion(mManager.get(arrowLeft,Texture.class)));
		tabStyle.arrowVerticalRight = new TextureRegionDrawable(new TextureRegion(mManager.get(arrowRight,Texture.class)));
		tabStyle.smallArrowHorizontalUp = new TextureRegionDrawable(new TextureRegion(mManager.get(arrowLittleUp,Texture.class)));
		tabStyle.smallArrowHorizontalDown = new TextureRegionDrawable(new TextureRegion(mManager.get(arrowDown,Texture.class)));
		tabStyle.smallArrowVerticalLeft = new TextureRegionDrawable(new TextureRegion(mManager.get(arrowLittleLeft,Texture.class)));
		tabStyle.smallArrowVerticalRight = new TextureRegionDrawable(new TextureRegion(mManager.get(arrowLittleRight,Texture.class)));
		tabStyle.scrollVertical = new NinePatch(mManager.get(scrollV,Texture.class),0,0,5,3);
		tabStyle.scrollBackVertical = new NinePatch(mManager.get(scrollBV,Texture.class),0,0,4,5);
		tabStyle.scrollHorizontal = new NinePatch(mManager.get(scrollH,Texture.class),3,5,0,0);
		tabStyle.scrollBackHorizontal = new NinePatch(mManager.get(scrollBH,Texture.class),5,5,0,0);
		mPanelStyle = tabStyle;
		mFont = mManager.get(font);
		Texture textureWhite = mManager.get(whitePixel);
		mBackground = new NinePatchDrawable(new NinePatch(new TextureRegion(textureWhite,1,1,textureWhite.getWidth()-2,textureWhite.getHeight()-2)));
		Texture textureBlack = mManager.get(blackPixel);
		mCursor = new NinePatchDrawable(new NinePatch(new TextureRegion(textureBlack,1,1,textureBlack.getWidth()-2,textureBlack.getHeight()-2)));
		Texture textureBlueT = mManager.get(bluePixelT);
		mSelector = new NinePatchDrawable(new NinePatch(new TextureRegion(textureBlueT,1,1,textureBlueT.getWidth()-2,textureBlueT.getHeight()-2)));
		Texture up = mManager.get(buttonUp, Texture.class);
		mUp = new NinePatchDrawable(new NinePatch(new TextureRegion(up,2,2,up.getWidth()-4,up.getHeight()-4),1,1,1,1));
		Texture down = mManager.get(buttonDown, Texture.class);
		mDown = new NinePatchDrawable(new NinePatch(new TextureRegion(down,2,2,down.getWidth()-4,down.getHeight()-4),1,1,1,1));
		Texture checked = mManager.get(buttonChecked, Texture.class);
		mChecked = new NinePatchDrawable(new NinePatch(new TextureRegion(checked,2,2,checked.getWidth()-4,checked.getHeight()-4),1,1,1,1));
	}
}
