
package iss.nus.edu;

import cardgame.Game;
import cardgame.Player;
import cardgame.Card;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@RequestScoped
@Path("/game")
public class NewGame {
    public static Map<String, Game> hm = new HashMap<String, Game>();
    public static String currentGameId = "";
    
    public static String playerId = "";
    public static Player player = null;
    
    @POST
    @Path("/creategame")
    @Produces("text/plain")
    public Response createGameName(@FormParam("gamename") String gname){
        JsonObject jo;
        String name = gname;
        currentGameId = UUID.randomUUID().toString().substring(0,8);
        Game game = new Game(currentGameId, name, "Waiting");
        hm.put(currentGameId, game);
        
         jo = Json.createObjectBuilder()
            .add("gid",game.getGameid())
            .add("name", name)
            .add("status", game.getStatus())
            .build();

        Response resp = Response.ok(jo.toString())
                .header("Access-Control-Allow-Origin", "*")
                .build();
        
        return resp;   
    }    
    
//    @GET
//    @Path("/newgame")
//    @Produces("text/plain")
//    public Response newGame(){
//        JsonObject jo;
//
//        Game g = hm.get(currentGameId);
//        
//        jo = Json.createObjectBuilder()
//            .add("gid",g.getGameid())
//            .add("name", g.getName())
//            .add("status", g.getStatus())
//            .build();
//
//        Response resp = Response.ok(jo.toString())
//                .header("Access-Control-Allow-Origin", "*")
//                .build();
//        
//        return resp;
//    }
    
//    @GET
//    @Path("/newgame")
//    @Produces("text/plain")
//    public Response newGame(){
//        JsonObject jo;
//        JsonArrayBuilder jab = Json.createArrayBuilder();
//        
//        Game g = hm.get(currentGameId);
//        
//        jo = Json.createObjectBuilder()
//            .add("gid",g.getGameid())
//            .add("name", g.getName())
//            .add("status", g.getStatus())
//            .build();
//        
//        Iterator entries = hm.entrySet().iterator();
//        while (entries.hasNext()) {
//          Map.Entry thisEntry = (Map.Entry) entries.next();
//          Object key = thisEntry.getKey();
//          Object value = thisEntry.getValue();
//          
//           jo = Json.createObjectBuilder()
//            .add("gid",thisEntry.getKey().toString())
//            .add("gname", ((Game)thisEntry.getValue()).getName())
//            .add("status",((Game)thisEntry.getValue()).getStatus())
//            .build();
//           
//           jab.add(jo);
//        }
//
//        Response resp = Response.ok(jo.toString())
//                .header("Access-Control-Allow-Origin", "*")
//                .build();
//        
//        return resp;
//    }    
    
    @POST
    @Path("/addgame")
    @Produces("text/plain")
    public Response addGame(@FormParam("gameid") String gid,@FormParam("gamename") String gamename)
    {
        JsonObject jo;
        
        currentGameId = gid;
        Game game = hm.get(currentGameId);
        game.CreateGame();
        game.DistributeCard();
        List<Player> p = game.getListofplayers();
        
        for(int i = 0; i < p.size(); i++){
            
            player = p.get(i);
            
            if(player.getId().equals(playerId)){
                break;
            }
            else{
                player = null;
            }
        }
        
        jo = Json.createObjectBuilder()
            .add("gid",game.getGameid())
            .add("name", game.getName())
            .add("status", game.getStatus())
            .build();

        Response resp = Response.ok(jo.toString())
                .header("Access-Control-Allow-Origin", "*")
                .build();
        
        return resp;
        
//        try { 
//            URI redirect = new URI("http://localhost:8383/UNOWebClient/playerlists.html"); 
//            return Response.seeOther(redirect).build(); 
//            
//        } catch (URISyntaxException ex) { 
//            
//            return Response.status(404).entity("Redirect Fail").build(); 
//        }
    }
    
    
    @GET
    @Path("/gamelist")
    @Produces("text/plain")
    public Response getGameList(){
        JsonObject jo;   
        JsonArrayBuilder jab = Json.createArrayBuilder();
        
        Iterator entries = hm.entrySet().iterator();
        while(entries.hasNext()){
            Map.Entry thisEntry = (Map.Entry) entries.next();
            Object key = thisEntry.getKey();
            Object value = thisEntry.getValue();
          
            jo = Json.createObjectBuilder()
                    .add("gid",thisEntry.getKey().toString())
                    .add("gname", ((Game)thisEntry.getValue()).getName())
                    .add("status",((Game)thisEntry.getValue()).getStatus())
                    .build();
           
           jab.add(jo);
        }
        
        Response resp = Response.ok(jab.build().toString())
                .header("Access-Control-Allow-Origin", "*")
                .build();
        return resp;
        
    }
    
    
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/addplayer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPlayer(@FormParam("gameid") String gameId, @FormParam("playername") String playername){
        JsonObject jo;
        
        currentGameId = gameId;
        playerId = UUID.randomUUID().toString().substring(0,8);
        String pname = playername;
        
        Player p = new Player(playerId, pname);

        hm.get(currentGameId).AddPlayer(p);
        
        jo = Json.createObjectBuilder()
            .add("gid", currentGameId)
                .add("pname", pname)
                .add("pid", playerId)
                .build();
        
        Response resp = Response.ok(jo.toString())
                .header("Access-Control-Allow-Origin", "*")
                .build();
        
        return resp;
        
    }
    
    
    @GET
    @Path("/waiting")
    @Produces("text/plain")
    public Response Waiting(){
        JsonObject jo;

        Game game = hm.get(currentGameId);
        
        jo = Json.createObjectBuilder()
            .add("gid", game.getGameid())
            .add("pid", playerId)
            .add("name", game.getName())
            .build();

        Response resp = Response.ok(jo.toString())
                .header("Access-Control-Allow-Origin", "*")
                .build();
        
        return resp;
    }
    
//    @GET
//    @Path("/waitingplayers")
//    @Produces("text/plain")
//    public Response WaitingGame(){
//        JsonObject jo;
//        
//        Game game = hm.get(currentGameId);
//        
//        jo = Json.createObjectBuilder()
//                .add("gid", game.getGameid())
//                .add("name", game.getName())
//                .add("pid", playerId)
//                .build();
//        
//        Response resp = Response.ok(jo.toString())
//                .header("Access-Control-Alow-Origin", "*")
//                .build();
//        
//        return resp;
//    }
    
    
    @GET
    @Path("/playerlist")
    @Produces("text/plain")
    public Response getPlayerLists(){
        JsonObject jo;
        JsonArrayBuilder jab = Json.createArrayBuilder();
        
        //String gname = gamename;
        
        Game game = hm.get(currentGameId);
        ArrayList<Player> playerlist = (ArrayList<Player>) game.getListofplayers();
        System.out.println("Before Loop");
        System.out.println("Player Size: " + playerlist.size());
        
        for (int i = 0; i < playerlist.size(); i++) {
            Player player = playerlist.get(i);
            
            jo = Json.createObjectBuilder()
                    .add("gname", game.getName())
                    .add("pid", player.getId())
                    .add("pname", player.getName())
                    .add("status",game.getStatus())
                    .build();
            
            System.out.println("Player List >>>> " + jo.toString());
            jab.add(jo);
            
        }
        
        Response resp = Response.ok(jab.build().toString())
                .header("Access-Control-Allow-Origin", "*")
                .build();
        return resp;
    }
    
    
    @GET
    @Path("/discardcard/{gid}")
    @Produces("text/plain")
    public Response showTableCards(@PathParam("gid") String gid ){
        JsonObject jso;
        System.out.println(">>>>> Discard Card " + gid);
        //currentGameId=gid;
        Game g = hm.get(gid);
        g.CreateGame();
        g.DistributeCard();
        
        Card c = g.DiscardCard();
        
        System.out.println("Before to JSON Object " + c.toString());
        jso = Json.createObjectBuilder()
            .add("color",c.getColor())
            .add("type", c.getType())
            .add("value",c.getValue())
            .add("image",c.getImage())
            .build();
        System.out.println(">>>>After JSON Discard Card " + c.toString());
        Response resp = Response.ok(jso.toString())
                .header("Access-Control-Allow-Origin", "*")
                .build();
        
        return resp;
    }
    
    @GET
    @Path("/drawpilelist/{gid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDeckList(@PathParam("gid") String gid) {

        JsonObject jso;
        JsonArrayBuilder jsa = Json.createArrayBuilder();

        Game g = hm.get(gid);
        System.out.println(">>>>BackCardList");
        ArrayList<Card> pileList = (ArrayList<Card>) g.getTheDeck().getThedeck();
        
        for (int i = 0; i < pileList.size(); i++) {
            Card card = pileList.get(i);

            jso = Json.createObjectBuilder()
                    .add("cid",card.getCardID())
                    .add("color", card.getColor())
                    .add("type", card.getType())
                    .add("value", card.getValue())
                    .add("image", card.getImage())
                    .build();
            //System.out.println(">>>Each Card of Discard Pile" + jso.toString());
            jsa.add(jso);

        }                                                       

        Response resp = Response.ok(jsa.build().toString())
                .header("Access-Control-Allow-Origin", "*")
                .build();
        System.out.println("The Deck Pile List >>>>" + jsa.toString());
        return resp; 

    }
    
    @POST
    @Path("/startgame")
    @Produces("text/plain")
    public Response StartGame(@FormParam("gameid") String gameid, @FormParam("playerid") String playerid){
        
        currentGameId = gameid;
        playerId = playerid;
        
        System.out.println("Game Id is" +currentGameId +"&"+"Player ID is"+ playerId);        
        
        try { 
            URI redirect = new URI("http://localhost:8383/UNOWebClient/showcards.html"); 
            return Response.seeOther(redirect).build(); 
            
        } catch (URISyntaxException ex) {            
            return Response.status(404).entity("Redirect Fail").build(); 
        }
    }
    
    
    @GET
    @Path("/startplayer/{gid}/{pid}")
    @Produces("text/plain")
    public Response StartPlayer(@PathParam("gid") String gid,@PathParam("pid") String pid){
        JsonObject jo;
        JsonArrayBuilder jab = Json.createArrayBuilder();
        Player player = null;
        System.out.println("Show Player Cards " + gid + "   " + pid);
        Game game = hm.get(gid);
        
        ArrayList<Player> playerlists = (ArrayList<Player>) game.getListofplayers();
        for(int i = 0; i < playerlists.size(); i++){
            
            player = playerlists.get(i);
            
            if(player.getId().equals(pid)){
                break;
            }
            else{
                player = null;
            }
        }  
            
        ArrayList<Card> cardinhand = (ArrayList<Card>) player.getCardinhandlist();
        System.out.println(">>> Player " + pid + " " + cardinhand.size());
        for (int i = 0; i < cardinhand.size(); i++) {
            Card card = cardinhand.get(i);
            
            jo = Json.createObjectBuilder()
            .add("cid", card.getCardID())
            .add("color",card.getColor())
            .add("type", card.getType())
            .add("value",card.getValue())
            .add("image",card.getImage())
            .build();

           jab.add(jo);
        }

        Response resp = Response.ok(jab.build().toString())
                .header("Access-Control-Allow-Origin", "*")
                .build();
        
        return resp;
    }
    
    
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/discardtopile/{gid}/{pid}/{cid}")
    @Produces(MediaType.APPLICATION_JSON)
    public void discardCardFromPlayer(@PathParam("gid") String gid, @PathParam("pid") String pid,@PathParam("cid") String cid) {
        
        JsonObject jso;
        JsonArrayBuilder jsa = Json.createArrayBuilder();
        Player player = null;
        System.out.println(">>>>> In Player Discard "+gid + " " + pid + " " + cid);
        Game g = hm.get(gid);
        
        ArrayList<Player> pList = (ArrayList<Player>) g.getListofplayers();
        for (int i = 0; i < pList.size(); i++) {

            player = pList.get(i);
            if (player.getId().equals(pid)) {
                break;
            } else {
                player = null;
            }

        }

        ArrayList<Card> cardinhand = (ArrayList<Card>) player.getCardinhandlist();

        for (int i = 0; i < cardinhand.size(); i++) {
            Card card = cardinhand.get(i);

            if(card.getCardID().equals(cid)){                
                g.setDiscardCard(player.removecard(i));
            }

        }        
        System.out.println(">>>>Discard Card By Player " + player.getCardinhandlist().size());

       
    }
    
    
}
