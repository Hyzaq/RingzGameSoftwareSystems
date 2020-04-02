package src.model;


public class Ring {

    Colour colour = Colour.EMPTY;
    int size = 0;

    public Ring(Colour colour, int size) {
        this.colour = colour;
        this.size = size;
    }


    public int getSize() {
        return size;
    }

    public Colour getColour() {
        return colour;
    }

    public String toStringColour() {

        String string = this.colour.toString().substring(0, 1);
        return string;
    }

    public String toStringRing() {
        String string = toStringColour() + " " + getSize();
        return string;
    }

}

