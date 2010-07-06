package demo;

import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;

public class MediaData implements Serializable {

    private static final long serialVersionUID = 1L;

    Integer Width=110;
    Integer Height=50;

    Color Background=new Color(190, 214, 248);
    Color DrawColor=new Color(0,0,0);

    Font font = new Font("Serif", Font.TRUETYPE_FONT, 30);

    /* Corresponding getters and setters */
    ...

}
