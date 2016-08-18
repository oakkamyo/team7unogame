
package cardgame;

import java.util.ArrayList;
import java.util.List;

public class Deck {

    private final static Integer NUMBEROFCARDS = 108;

    private List<Card> thedeck = new ArrayList<Card>();
    String[] color = {"red", "yellow", "green", "blue"};
    String[] type = {"Number", "skip", "reverse", "draw2", "Change Color", "wilddraw4"};
    private int count = 0;

    public List<Card> NewDeck() {

        for (int i = 0; i < 4; i++) {
            Card c = new Card();
            c.setColor(color[i]);
            c.setType(type[0]);
            c.setValue(0);
            c.setImage("images/"+ color[i] + 0 + ".png");
            c.setCardID(count+"");
            setThedeck(c);
            count++;

        }

        for (int i = 1; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 1; k < 10; k++) {
                    Card c = new Card();
                    c.setColor(color[j]);
                    c.setValue(k);
                    c.setType(type[0]);
                    c.setImage("images/"+ color[j] + k + ".png");
                    c.setCardID(count+"");
                    setThedeck(c);
                    count++;
                }
                for (int l = 0; l <= 2; l++) {
                    Card c = new Card();
                    c.setColor(color[j]);
                    c.setValue(20);
                    c.setType(type[l + 1]);
                    c.setImage("images/"+ color[j] + type[l+1] + ".png");
                    c.setCardID(count+"");
                    setThedeck(c);
                    count++;
                }

            }
        }

        for (int i = 0; i < 4; i++) {
            Card c = new Card();
            c.setColor("Wild");
            c.setType(type[4]);
            c.setValue(20);
            c.setImage("images/"+ "wild" + ".png");
            c.setCardID(count+"");
            setThedeck(c);
            count++;

        }

        for (int i = 0; i < 4; i++) {
            Card c = new Card();
            c.setColor("Wild");
            c.setType(type[5]);
            c.setValue(20);
            c.setImage("images/"+ "wilddraw4" + ".png");
            c.setCardID(count+"");
            setThedeck(c);
            count++;

        }

        return thedeck;
    }

    public Card DrawCard() {

        return getThedeck().get(0);

    }

    
    public List<Card> getThedeck() {
        return thedeck;
    }

    
    public void setThedeck(Card c) {
        this.thedeck.add(c);
    }

    @Override
    public String toString() {
        return thedeck + "\n";
    }

}
