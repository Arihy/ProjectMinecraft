package generateur;

import java.io.IOException;

import org.xml.sax.SAXException;

import openstreetcraft.OscSaxParser;

import map.Map;
import map.MapCached;
import projet.ButtonEditor;
import projet.ButtonEditor.ButtonEditorListener;
import projet.MapWindow;
import projet.TextFieldEditor;

public class Launcher implements ButtonEditorListener
{
	private Generateur mGenerateur;
	private MapCached mMap;
	private MapWindow mWindow;

	private TextFieldEditor mTailleX,mTailleZ,mRegionsX,mRegionZ,mPath,mSeed,mStreetMapPath,mStreetMapSavePath;
	private ButtonEditor mButtonGenerator,mButtonStreetMap;

	public static void main(String[] args)
	{
		new Launcher();
	}

	public Launcher()
	{
		mGenerateur = new Generateur();
		mWindow = MapWindow.getInstance();
		mTailleX = mWindow.getFormular().addTextField("Taille X", "1000");
		mTailleZ = mWindow.getFormular().addTextField("Taille Z", "1000");
		mRegionsX = mWindow.getFormular().addTextField("Nombre Regions X", "5");
		mRegionZ = mWindow.getFormular().addTextField("Nombre Regions Z", "5");
		mPath = mWindow.getFormular().addTextField("Nom map", "TESTMAP");
		mSeed = mWindow.getFormular().addTextField("Seed", "2500");
		mButtonGenerator = mWindow.getFormular().addButton("Generez !");
		mButtonGenerator.setButtonEditorListener(this);
		mStreetMapSavePath = mWindow.getFormular().addTextField("Nom map Streetmap", "STREETMAP");
		mStreetMapPath = mWindow.getFormular().addTextField("Fichier StreetMap", "map.osm");
		mButtonStreetMap = mWindow.getFormular().addButton("Generez StreetMap !");
		mButtonStreetMap.setButtonEditorListener(this);
	}
	public void onCLick(ButtonEditor button)
	{
		if (button == mButtonGenerator)
		{
			try
			{
				mMap = mGenerateur.generate(8 /*HARDCODE*/, Integer.valueOf(mTailleX.getTextFieldValue()),  Integer.valueOf(mTailleZ.getTextFieldValue()), 256,  Integer.valueOf(mRegionsX.getTextFieldValue()),  Integer.valueOf(mRegionZ.getTextFieldValue()), 64, mPath.getTextFieldValue(), Integer.valueOf(mSeed.getTextFieldValue()));
				mMap.flush();
				Map map = new Map();
				map.load(mMap.getURL());
				mMap = null;
				mWindow.setMap(map);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else if (button == mButtonStreetMap)
		{
			try
			{
				String mapURL = mStreetMapSavePath.getTextFieldValue();
				new OscSaxParser(mStreetMapPath.getTextFieldValue(), mapURL);
				Map map = new Map();
				map.load(mapURL);
				mWindow.setMap(map);
			}
			catch (SAXException | IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
