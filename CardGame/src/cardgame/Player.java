
package cardgame;

import java.util.ArrayList;
import java.util.List;

public class Player {
    
    private String id;
    private String name;
    private int cardinhand = 0;
    private List<Card> cardinhandlist = new ArrayList<Card>();
    

    public Player(String id, String name){
        this.id = id;
        this.name = name;
    }

    public void addcard(Card c) {

        setCardinhandlist(c);
    }

    public Card removecard(int i) {
        return cardinhandlist.remove(i);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCardinhand() {
        return cardinhand;
    }

    public void setCardinhand(int cardinhand) {
        this.cardinhand = cardinhand;
    }

    public List<Card> getCardinhandlist() {
        return cardinhandlist;
    }

    public void setCardinhandlist(Card c) {
        this.getCardinhandlist().add(c);
    }
    
//    public void setCardinhandlist(List<Card> cardinhandlist) {
//        this.cardinhandlist = cardinhandlist;
//    }

    @Override
    public String toString() {
        return " Player : " + "name=" + getName() + " Card In Hand : " + getCardinhandlist();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCardinhandlist(List<Card> cardinhandlist) {
        this.cardinhandlist = cardinhandlist;
    }

}
