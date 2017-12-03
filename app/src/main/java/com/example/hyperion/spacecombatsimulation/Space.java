package com.example.hyperion.spacecombatsimulation;


import android.util.Log;
import java.lang.reflect.Field;

class Space {

    public enum Sector {

        Sector1 ("Level 1", "atlantis_nebula", 500, 10, 10, 255),
        Sector2 ("Level 2", "atlantis_nebula2", 500, 15, 15, 255),
        Sector3 ("Level 3", "atlantis_nebula3", 500, 12, 10, 255),
        Sector4 ("Level 4", "gas_giant", 500, 15, 6, 255),
        Sector5 ("Level 5", "kepler_186f", 500, 10, 10, 255),
        Sector6 ("Level 6", "ngc3190", 500, 10, 10, 255);
        //Sector7 ("Level 7", "atlantis_nebula", 500, 10, 10, 255),
        //Sector8 ("Level 8", "atlantis_nebula", 500, 10, 10, 255);

        protected final String name;
        protected final int background, thumb, stardust, width, height, brightness;

        Sector (String name, String background, int stardust, int width, int height, int brightness) {
            this.name = name;
            this.stardust = stardust;
            this.width = width;
            this.height = height;
            this.brightness = brightness;

            try {
                Class res = R.drawable.class;
                Field field = res.getField(background);
                this.background = field.getInt(null);
            } catch (Exception e) {
                Log.e("ERROR", "Error getting resource ID: " + background);
                throw new RuntimeException("Error getting resource ID", e);
            }

            try {
                Class res = R.drawable.class;
                Field field = res.getField(background + "_thumb");
                this.thumb = field.getInt(null);
            } catch (Exception e) {
                Log.e("ERROR", "Error getting resource ID: " + background);
                throw new RuntimeException("Error getting resource ID", e);
            }
        }
    }
}
