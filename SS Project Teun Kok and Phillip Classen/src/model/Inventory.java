package src.model;

/**
 * The type Inventory.
 */
public class Inventory {

    private Ring[] inv;
    private Colour colour;

    /**
     * Instantiates a new Inventory with a colour.
     *
     * @param colour the colour
     */
    public Inventory(Colour colour) {
        this.colour = colour;
        Ring[] inv = new Ring[15];
        int id = -1;
        for (int i = 0; i < 5; i++) {
            for (int x = 0; x < 3; x++) {
                Ring ring = new Ring(colour, i);
                id++;
                inv[id] = ring;
            }
        }
        this.inv = inv;
    }

    /**
     * Change toSpecial inv inventory.
     * <p>
     * Used with 3 players to split up the last inventory for blocking
     *
     * @return the inventory
     */
    public Inventory changeToSpecialInv() {
        Inventory special = new Inventory(Colour.YELLOW);
        Ring empty = new Ring(Colour.EMPTY, 0);
        for (int f = 0; f <= special.getArray().length - 1; f++) {
            special.getArray()[f] = empty;
        }
        for (int x = 0; x < 5; x++) {
            Ring ring = new Ring(Colour.YELLOW, x);
            special.getArray()[x] = ring;
        }
        return special;
    }

    /**
     * Get array ring [].
     *
     * @return the ring []
     */
    public synchronized Ring[] getArray() {
        return inv;
    }

    /**
     * Remove ring.
     *
     * @param removeRing the ring
     */
    public void removeRing(Ring removeRing) {
        if (!removeRing.getColour().equals(Colour.SPECIAL)) {
            for (int i = 0; i < this.getArray().length; i++) {
                if (this.getArray()[i].getSize() == removeRing.getSize() && this.getArray()[i].getColour().equals(removeRing.getColour())) {
                    this.getArray()[i] = new Ring(Colour.EMPTY, 0);
                    break;
                }
            }
        }
    }

    /**
     * Gets inv colour.
     *
     * @return the inv colour
     */
    public Colour getInvColour() {
        return colour;
    }

}
