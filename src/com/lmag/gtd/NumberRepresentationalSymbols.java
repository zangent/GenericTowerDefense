package com.lmag.gtd;

import com.lmag.gtd.util.Utils;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

public class NumberRepresentationalSymbols {
    Image representationalSYMBOLSspritesheet;
    public NumberRepresentationalSymbols() {
        /* AYY LMAO XD */ representationalSYMBOLSspritesheet = Utils.getImageFromPath("thiswasabadidea.png");
    }
    public void draw_Number(int num, Graphics straight_UPg,Vector2f posItion) {
        straight_UPg.drawImage(representationalSYMBOLSspritesheet,posItion.x, posItion.y,posItion.x+10, posItion.y+10,Math.min(num*10,representationalSYMBOLSspritesheet.getWidth()-10),0,Math.min(num*10+10,representationalSYMBOLSspritesheet.getWidth()),10);
    }
}
