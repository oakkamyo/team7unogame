
package cardgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {

    private String gameid;
    private Integer numberofplayers;
    private String name;
    private String status;
    private List<Player> listofplayers = new ArrayList<Player>();
    private Card DiscardCard;
    private Deck theDeck = new Deck();

    public Game() {

    }

    public Game(String gameid, String name, String status) {
        this.gameid = gameid;
        this.name = name;
        this.status = status;
    }
    
    

    public void CreateGame() {
        getTheDeck().NewDeck();
        Collections.shuffle(getTheDeck().getThedeck());

    }

    public void AddPlayer(Player p) {

        setListofplayers(p);

    }

    public void DistributeCard() {
        
        for(Player p:listofplayers){
            p.setCardinhandlist(new ArrayList<Card>());
        }
        
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < getListofplayers().size(); j++) {
                getListofplayers().get(j).addcard(getTheDeck().DrawCard());
                getTheDeck().getThedeck().remove(getTheDeck().DrawCard());
            }
        }
    }

    public Card DiscardCard() {
        Card c = new Card();
        c = getTheDeck().getThedeck().get(0);
        getTheDeck().getThedeck().remove(0);
        return c;

    }

    /**
     * @return the listofplayers
     */
    public List<Player> getListofplayers() {
        return listofplayers;
    }

    /**
     * @param listofplayers the listofplayers to set
     */
    public void setListofplayers(Player p) {
        this.getListofplayers().add(p);
    }

    @Override
    public String toString() {
        return "Game :" + "Game ID =" + getGameid() + "\n " + "Discard " + DiscardCard() + "\n" + "Cards On Deck : " + getTheDeck().getThedeck().size() + getListofplayers();
    }

    /**
     * @return the gameid
     */
    public String getGameid() {
        return gameid;
    }

    /**
     * @param gameid the gameid to set
     */
    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    /**
     * @return the numberofplayers
     */
    public Integer getNumberofplayers() {
        return numberofplayers;
    }

    /**
     * @param numberofplayers the numberofplayers to set
     */
    public void setNumberofplayers(Integer numberofplayers) {
        this.numberofplayers = numberofplayers;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setListofplayers(List<Player> listofplayers) {
        this.listofplayers = listofplayers;
    }

    public Deck getTheDeck() {
        return theDeck;
    }

    public void setTheDeck(Deck theDeck) {
        this.theDeck = theDeck;
    }

    /**
     * @return the DiscardCard
     */
    public Card getDiscardCard() {
        return DiscardCard;
    }

    /**
     * @param DiscardCard the DiscardCard to set
     */
    public void setDiscardCard(Card DiscardCard) {
        this.DiscardCard = DiscardCard;
    }

}
