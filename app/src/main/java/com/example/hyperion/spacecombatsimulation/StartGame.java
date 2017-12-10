package com.example.hyperion.spacecombatsimulation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.hyperion.spacecombatsimulation.Space.Sector;
import com.example.hyperion.spacecombatsimulation.Ship.ShipClass;

public class StartGame extends Activity {

    static List<Sector> maps = new ArrayList<>();
    static List<ShipClass> ships = new ArrayList<>();
    static Sector selectedMap;
    static ShipClass selectedShip;
    private RecyclerView recyclerView;
    //private ViewGroup containerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("StartGame", "selectedMap: " + selectedMap + " selectedShip: " + selectedShip +"\n"+
              "maps size: " + maps.size() + " ships size: " + ships.size());

        if (ships.isEmpty() || selectedMap == null)
            createMapLayout();
        else
            createShipLayout();

        //LayoutTransition transition = new LayoutTransition();
        //recyclerView.setLayoutTransition(transition);
    }

    protected void createMapLayout() {
        setContentView(R.layout.select_map);

        if (maps.isEmpty())
            maps.addAll(Arrays.asList(Sector.values()));

        final Button butSelect = findViewById(R.id.butSelect);

        recyclerView = findViewById(R.id.recyclerView);
        AdapterMaps adapterMaps = new AdapterMaps(maps, new AdapterMaps.OnItemClickListener() {
            @Override
            public void onItemClick(Sector sector) {
                butSelect.setEnabled(true);
                Log.d("StartGame", "Selected sector: " + sector.name);
            }
        });
        recyclerView.setAdapter(adapterMaps);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    protected void createShipLayout() {
        setContentView(R.layout.select_ship);

        if (ships.isEmpty())
            ships.addAll(Arrays.asList(ShipClass.values()));

        final Button butSelect = findViewById(R.id.butSelect);

        recyclerView = findViewById(R.id.recyclerView);
        AdapterShips adapterShips = new AdapterShips(ships, new AdapterShips.OnItemClickListener() {
            @Override
            public void onItemClick(ShipClass ship) {
                butSelect.setEnabled(true);
                Log.d("StartGame", "Selected ship: " + ship.name);
            }
        });
        recyclerView.setAdapter(adapterShips);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    public void button_click(View v) {
        switch (v.getId()) {

            case R.id.butSelect:
                if (ships.isEmpty() || selectedShip == null)
                    createShipLayout();
                else {
                    startActivity(new Intent(this, Game.class));
                    finish();
                }
                break;

            case R.id.butBack:
                if (ships.isEmpty()) {
                    Main.active = true;
                    finish();
                } else {
                    ships.clear();
                    selectedMap = null;
                    createMapLayout();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Main.active = true;
            ships.clear();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}


class AdapterMaps extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Sector> list = new ArrayList<>();
    private final OnItemClickListener listener;
    private ImageView lastThumb;

    interface OnItemClickListener {
        void onItemClick(Sector map);
    }

    public class MapViewHolder extends RecyclerView.ViewHolder {

        TextView mapName, mapScale;
        ImageView mapThumbnail;

        MapViewHolder(View itemView) {
            super(itemView);
            mapName = itemView.findViewById(R.id.textName);
            mapThumbnail = itemView.findViewById(R.id.imageMap);
            mapScale = itemView.findViewById(R.id.textScale);
        }

        void bind(final Sector sector, final OnItemClickListener listener) {
            itemView.findViewById(R.id.imageMap).setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {

                    StartGame.selectedMap = sector;
                    listener.onItemClick(sector);

                    if (lastThumb != null)
                        lastThumb.setEnabled(true);

                    mapThumbnail.setEnabled(false);
                    lastThumb = mapThumbnail;
                }
            });
        }
    }

    AdapterMaps(List<Sector> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_map_entry, parent, false);
        return new MapViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MapViewHolder viewHolder = (MapViewHolder) holder;
        viewHolder.mapName.setText(list.get(position).name);
        viewHolder.mapThumbnail.setImageResource(list.get(position).thumb);
        String scale = list.get(position).width + " x " + list.get(position).height + " km";
        viewHolder.mapScale.setText(scale);
        viewHolder.bind(list.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}


class AdapterShips extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ShipClass> list = new ArrayList<>();
    private final OnItemClickListener listener;
    private ImageView lastThumb;

    interface OnItemClickListener {
        void onItemClick(ShipClass ship);
    }

    class ShipViewHolder extends RecyclerView.ViewHolder {

        TextView shipName;
        ImageView shipThumbnail;

        ShipViewHolder(View itemView) {
            super(itemView);
            shipName = itemView.findViewById(R.id.textName);
            shipThumbnail = itemView.findViewById(R.id.imageShip);
        }

        void bind(final ShipClass ship, final OnItemClickListener listener) {
            itemView.findViewById(R.id.imageShip).setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {

                    StartGame.selectedShip = ship;
                    listener.onItemClick(ship);

                    if (lastThumb != null)
                        lastThumb.setEnabled(true);

                    shipThumbnail.setEnabled(false);
                    lastThumb = shipThumbnail;
                }
            });
        }
    }

    AdapterShips(List<ShipClass> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_ship_entry, parent, false);
        return new ShipViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ShipViewHolder viewHolder = (ShipViewHolder) holder;
        viewHolder.shipName.setText(list.get(position).name);
        viewHolder.shipThumbnail.setImageResource(list.get(position).image);
        viewHolder.bind(list.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}