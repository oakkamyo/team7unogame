
package cardgame;


public class Card {

    private String color;
    private String type;
    private Integer value;
    private String image;
    private String cardID;

    public Card() {

    }

    public Card(String color, String type, Integer value, String image, String cardID) {
        this.color = color;
        this.type = type;
        this.value = value;
        this.image = image;
        this.cardID = cardID;
    }

    
    public String getColor() {
        return color;
    }

    
    public void setColor(String color) {
        this.color = color;
    }

    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getValue() {
        return value;
    }


    public void setValue(Integer value) {
        this.value = value;
    }

    public String getImage() {
        return image;
    }


    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "\t Card :" + "Color=" + color + ", type=" + type + ", value=" + value + ", image=" + image + "\n";
    }

    /**
     * @return the cardID
     */
    public String getCardID() {
        return cardID;
    }

    /**
     * @param cardID the cardID to set
     */
    public void setCardID(String cardID) {
        this.cardID = cardID;
    }

}
