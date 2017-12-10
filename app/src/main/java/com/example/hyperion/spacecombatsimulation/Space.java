package com.example.hyperion.spacecombatsimulation;

import android.util.Log;
import java.lang.reflect.Field;

class Space {

    public enum Sector {

        Sector1 ("Level 1", "atlantis_nebula", 500, 0xFF7595CD, 10, 10),
        Sector2 ("Level 2", "atlantis_nebula2", 500, 0xFF3B8BCC, 15, 15),
        Sector3 ("Level 3", "atlantis_nebula3", 500, 0xFFFFFFFF, 12, 10),
        Sector4 ("Level 4", "gas_giant", 500, 0xFFFFFFFF, 15, 6),
        Sector5 ("Level 5", "kepler_186f", 500, 0xFFFFFFFF, 10, 10),
        Sector6 ("Level 6", "ngc3190", 500, 0xFFFFFFFF, 10, 10);
        //Sector7 ("Level 7", "atlantis_nebula", 500, 0xFFFFFFFF, 10, 10),
        //Sector8 ("Level 8", "atlantis_nebula", 500, 0xFFFFFFFF, 10, 10);

        protected final String name;
        protected final int background, thumb, dustCount, sectorColor, width, height;

        Sector (String name, String background, int dustCount, int sectorColor, int width, int height) {
            this.name = name;
            this.dustCount = dustCount;
            this.sectorColor = sectorColor;
            this.width = width;
            this.height = height;

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
