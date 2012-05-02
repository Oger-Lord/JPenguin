
package de.jpenguin.game;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.ActionListener;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.renderer.Camera;
import java.util.List;
import java.util.ArrayList;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.shadow.*;
import com.jme3.shadow.PssmShadowRenderer.CompareMode;
import com.jme3.shadow.PssmShadowRenderer.FilterMode;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.math.Vector2f;
import com.jme3.terrain.heightmap.*;
import jme3tools.converters.ImageToAwt;
import com.jme3.export.binary.BinaryImporter;
import com.jme3.export.xml.XMLImporter;
import java.io.*;
import de.jpenguin.gui.GUI;
import com.jme3.asset.AssetKey;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FogFilter;
import com.jme3.system.AppSettings;
import com.jme3.scene.BatchNode;

import de.jpenguin.network.*;
import de.jpenguin.engine.*;
import de.jpenguin.input.*;
import de.jpenguin.fog.FogOfWarModels;
import de.jpenguin.loader.Loader;

public class GameApplication extends SimpleApplication{
    
    private Node clickableNode;
    
    private Game game;
    private TerrainQuad terrain;
    private Node doodadNode;
    
   // private PssmShadowRenderer pssmRenderer;
    
    private PlayerInput playerInput;
    private RTSCamera camera;
    
    private int loading_frames=-1;
    private boolean loading=true;
    private LoadingScreen loadingScreen;
    
    private GUI gui;
    
    private String map;
    
    private GameClient client;
    private GameServer server;
    
    private final int TICKS_PER_SECOND = 25;
    private final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
    private final int MAX_FRAMESKIP = 5;
    private final float GAME_TPF = (1/((float)TICKS_PER_SECOND));
    
    private int loops;
    private long next_game_tick;
    
    private FilterPostProcessor postWater;
    private Water water;
    
    public GameApplication(Game game, boolean multiplayer, String joinAdress,String playerId,int playerNumber,String map)
    {
        this.game=game;
        this.map=map;
        
        setPauseOnLostFocus(false); 
        setShowSettings(false);
        
            if(joinAdress.isEmpty())
            {
                server = new GameServer();
                if(server.isStarted()==false)
                {
                    System.out.println("Error cannot start server");
                    System.exit(0);
                }
                
                joinAdress="localhost";
            }

            if(multiplayer)
            {
                try{
                    client = new GameClient(game,joinAdress,playerId,playerNumber);
                }catch(Exception e){
                    if(server != null)
                    {
                        server.close();
                        System.out.println("Client error " + e.getMessage());
                        System.exit(0);
                    }
                }
            }
        
        try{
            FileInputStream fis = new FileInputStream(new File("settings.txt"));
            AppSettings settings = new AppSettings(true);
            settings.load(new BufferedInputStream(fis));
            setSettings(settings);
        }catch(Exception e){
            AppSettings settings = new AppSettings(true);
            settings.setResolution(1024,768);
            settings.setFrameRate(50);
            settings.setFullscreen(false);
            settings.setTitle("JPenguin");

            setSettings(settings);
        }

    }

    
    
    @Override
    public void simpleInitApp() {
        loadingScreen = new LoadingScreen(this);
        
       // setDisplayFps(false);
        setDisplayStatView(false);
        
        flyCam.setEnabled(false);
        
        //assetManager.registerLoader("de.jpenguin.engine.loader.ImageLoader", "png","jpg","gif");
    }
    
    
    public void loading(int pos)
    {
        if(pos==0)
        {
            camera = new RTSCamera(getCamera());
            camera.registerWithInput(inputManager);
            
            loadingScreen.setProgress(0.1f, "Making Light");
        }else if(pos==1){


            loadLight();
            loadingScreen.setProgress(0.2f, "Loading Terrain");
            
        }else if(pos==2)
        {
            clickableNode = new Node();
            rootNode.attachChild(clickableNode);
          //  clickableNode.attachChild(createTerrain());
            loadTerrain();
            loadWater();
            camera.addTerrain(terrain);
            
            loadingScreen.setProgress(0.6f, "Loading Doodads");
        }else if(pos==3)
        {
            loadDoodads();
            loadingScreen.setProgress(0.8f, "Init Game");
        }else if(pos==4)
        {
            
            
            game.init();
            
            //gui=new GUI(game);
            
            game.getMyGame().init(game);
            
            if(game.isMultiplayer())
            {
                client.finishedLoading();
            }
            
            
            loadingScreen.setProgress(1f, "Finished Loading");
        }else if(pos==5)
        {
            loadingScreen.clear();
            camera.enable();
            
            gui=new GUI(game);
            gui.init();
            
            playerInput= new PlayerInput(game);
             //important input after nifty
            
            next_game_tick = System.currentTimeMillis();
            
            Timer.init();
            loading=false;
        }
    }
    

    
    public MouseStatus getMouseCollision()
    {
        if(gui.isMouseOver())
        {
            return null;
        }
        
        Vector3f origin    = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.0f);
        Vector3f direction = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.3f);
        direction.subtractLocal(origin).normalizeLocal();

        Ray ray = new Ray(origin, direction);
        CollisionResults results = new CollisionResults();
        clickableNode.collideWith(ray, results);
        
        if (results.size() > 0) {
                    
             CollisionResult closest = results.getClosestCollision();
             Geometry g =closest.getGeometry();
             
             Model m = Model.Geometry2Model(g);
                    
             return new MouseStatus(m,closest.getContactPoint().getX(),closest.getContactPoint().getZ());
             
        }
        return null;
    }


    @Override
    public void simpleUpdate(float tpf){
        if(loading)
        {
            if(loading_frames==0)
            {
                if(game.isMultiplayer() && client.allConnected()==false)
                {
                    return;
                }
            }
            
            if(loading_frames==5)
            {
                if(game.isMultiplayer() && client.allLoaded()==false)
                {
                    return;
                }
            }
            
            loading(loading_frames);
            loading_frames++;
        }else{
            
            loops = 0;
            

                while( System.currentTimeMillis()> next_game_tick && loops < MAX_FRAMESKIP && (game.isMultiplayer()==false || client.canContinue())) {

                    if(game.isMultiplayer())
                    {
                        client.update();
                    }
                
                    Timer.update(GAME_TPF);
                    game.update(GAME_TPF);

                    next_game_tick += SKIP_TICKS;
                    loops++;
                    
                }
            
            
            camera.update(tpf);
            listener.setLocation(cam.getLocation());
            listener.setRotation(cam.getRotation());
            gui.update(tpf); //nur wegen Kamera auf Minimap
        }
    }

    
    protected void makeLight() {
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(2));
        rootNode.addLight(al);

        DirectionalLight dl1 = new DirectionalLight();
        dl1.setDirection(new Vector3f(0.98f, -0.98f, 0.94f).normalizeLocal());
        dl1.setColor(new ColorRGBA(0.965f, 0.949f, 0.772f, 1f).mult(0.7f));
        rootNode.addLight(dl1);
    }
    
  
     public void loadTerrain()
    {
        try{
           long start = System.currentTimeMillis();
            // remove the existing terrain and detach it from the root node.
            if (terrain != null) {
                Node existingTerrain = (Node)terrain;
                existingTerrain.removeFromParent();
                existingTerrain.removeControl(TerrainLodControl.class);
                existingTerrain.detachAllChildren();
                terrain = null;
            }


            InputStream fis = game.getGameApplication().getAssetManager().locateAsset(new AssetKey("Scenes/"+map+"/terrainsave.jme")).openStream();
            BinaryImporter imp = BinaryImporter.getInstance();
            imp.setAssetManager(assetManager);

            terrain = (TerrainQuad) imp.load(new BufferedInputStream(fis));
            terrain.setShadowMode(ShadowMode.CastAndReceive);
            
            clickableNode.attachChild((Node)terrain);

            float duration = (System.currentTimeMillis() - start) / 1000.0f;
            System.out.println("Load took " + duration + " seconds");
        }catch(Exception e){}
    }
     
     public void loadDoodads()
     {
        InputStream fis = game.getGameApplication().getAssetManager().locateAsset(new AssetKey("Scenes/"+map+"/doodads.jme")).openStream();
        BinaryImporter importer = BinaryImporter.getInstance();
        importer.setAssetManager(assetManager);

        try {
          doodadNode = (Node)importer.load(new BufferedInputStream(fis));
          
          //this node is for buildings in fog (unsauber usw.....)
          doodadNode.attachChild(new Node());
          
          /*
          Node test =new Node();
          
          List list =doodadNode.getChildren();
            for(int i=0;i<list.size();i++)
            {
                BatchNode batch = new BatchNode();
                batch.attachChild((Node)list.get(i));
                batch.batch();
                test.attachChild(batch);
            }
          rootNode.attachChild(test);
           * 
           */
          
          
           
          rootNode.attachChild(doodadNode);
        } catch (IOException ex) {
        //  Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "No saved node loaded.", ex);
        }
     }
     
     
    public void loadLight()
    {
       
            XMLImporter imp = XMLImporter.getInstance();
            imp.setAssetManager(assetManager);
            
            
            PssmShadowRenderer pssmRenderer = new PssmShadowRenderer(assetManager, 1024*2, 1);
            pssmRenderer.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
            pssmRenderer.setLambda(0.55f);
            pssmRenderer.setShadowIntensity(0.7f);
            pssmRenderer.setCompareMode(CompareMode.Software);
            pssmRenderer.setFilterMode(FilterMode.PCF4    );
           // pssmRenderer.displayDebug();
            viewPort.addProcessor(pssmRenderer);
         
            
            
            
        try{
            InputStream fis = assetManager.locateAsset(new AssetKey("Scenes/"+map+"/directlightsave.xml")).openStream();
            DirectionalLight dl = (DirectionalLight) imp.load(new BufferedInputStream(fis));
            rootNode.addLight(dl);
        }catch(Exception e){}

        try{
            InputStream fis = assetManager.locateAsset(new AssetKey("Scenes/"+map+"/ambientlightsave.xml")).openStream();
            AmbientLight al = (AmbientLight) imp.load(new BufferedInputStream(fis));  
             rootNode.addLight(al);
        }catch(Exception e){}
            
        try{
            InputStream fis = assetManager.locateAsset(new AssetKey("Scenes/"+map+"/fogsave.xml")).openStream();
            FogFilter fog = (FogFilter) imp.load(new BufferedInputStream(fis));

            FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
            fpp.addFilter(fog);
            viewPort.addProcessor(fpp);
        }catch(Exception e){}
        
               

         
             
           /*
        try{
            InputStream fis = assetManager.locateAsset(new AssetKey("Scenes/"+map+"/shadowsave.xml")).openStream();
            PssmShadowRenderer pssmRenderer = (PssmShadowRenderer) imp.load(new BufferedInputStream(fis));  
             viewPort.addProcessor(pssmRenderer);
        }catch(Exception e){} 
         * 
         */


    }
    
    
    public void loadWater()
    {
        water =Loader.load(assetManager, map, "water", false, Water.class);
        if(water == null)
        {
            water = new Water();
        }
        water.init(this, terrain, null, rootNode);
    }
    
    
    @Override
  public void destroy()
  {
      super.destroy();
      
      if(client != null)
      {
          client.close();
      }
      
       if(server != null)
      {
          server.close();
      }
       
      game.getFogOfWar().destroy();
  }
    
     
  public GUI getGUI()
  {
      return gui;
  }
  
  public Node getClickableNode()
  {
      return clickableNode;
  }
  
  public TerrainQuad getTerrain()
  {
      return terrain;
  }
  
  public PlayerInput getPlayerInput()
  {
      return playerInput;
  }
  
  public RTSCamera getRTSCamera()
  {
      return camera;
  }
  
  public Node getDoodadNode()
  {
      return doodadNode;
  }
  
  public String getMap()
  {
      return map;
  }
  
  public Game getGame()
  {
      return game;
  }
  
   public GameClient getClient()
  {
      return client;
  }
    
   public Water getWater()
   {
       return water;
   }
   
   /*
    public GameServer getServer()
  {
     
    return server;
  }
    * 
    */
}

       