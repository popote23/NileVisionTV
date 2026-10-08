package com.nilevision.tv;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import java.util.*;

public class MainActivity extends Activity {
    private ListView catList, chList;
    private TextView status, now;
    private ExoPlayer player;
    private final List<Channel> channels = new ArrayList<>();
    private final Map<Integer, List<Channel>> cache = new HashMap<>();
    private boolean fullscreen = false;
    private int playing = -1, loadingCat = -1;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.main);
        catList = findViewById(R.id.cats); chList = findViewById(R.id.chans);
        status = findViewById(R.id.status); now = findViewById(R.id.now);
        StyledPlayerView pv = findViewById(R.id.player);
        player = new ExoPlayer.Builder(this).build();
        pv.setPlayer(player);
        player.addListener(new Player.Listener() {
            @Override public void onPlaybackStateChanged(int s) {
                if (s == Player.STATE_BUFFERING) status.setText("Chargement...");
                if (s == Player.STATE_READY) status.setText("");
            }
            @Override public void onPlayerError(PlaybackException e) { status.setText("Flux indisponible - essayez une autre chaîne"); }
        });

        List<String> titles = new ArrayList<>();
        for (Catalog.Cat c : Catalog.CATS) titles.add(c.title);
        catList.setAdapter(new ArrayAdapter<>(this, R.layout.item, titles));
        catList.setOnItemClickListener((p, v, pos, id) -> { openCategory(pos); chList.requestFocus(); });
        catList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> p, View v, int pos, long id) { openCategory(pos); }
            public void onNothingSelected(AdapterView<?> p) {}
        });
        chList.setOnItemClickListener((p, v, pos, id) -> onChannelClick(pos));
        catList.requestFocus();
    }

    private void openCategory(final int pos) {
        if (pos == loadingCat) return;
        loadingCat = pos;
        final Catalog.Cat cat = Catalog.CATS[pos];
        channels.clear();
        if (cat.sat) {
            List<String> l = new ArrayList<>();
            for (String[] s : Catalog.SATS) l.add("📡 " + s[0] + "  —  " + s[1]);
            chList.setAdapter(new ArrayAdapter<>(this, R.layout.item, l));
            return;
        }
        if (cache.containsKey(pos)) { showChannels(cache.get(pos)); return; }
        chList.setAdapter(new ArrayAdapter<>(this, R.layout.item, new String[]{"Chargement..."}));
        new Thread(() -> {
            try {
                final List<Channel> res = M3uParser.load(cat.url, cat.filter);
                runOnUiThread(() -> { cache.put(pos, res); if (loadingCat == pos) showChannels(res); });
            } catch (Exception e) {
                runOnUiThread(() -> { if (loadingCat == pos) chList.setAdapter(new ArrayAdapter<>(this, R.layout.item, new String[]{"Erreur réseau : vérifiez Internet"})); });
            }
        }).start();
    }

    private void showChannels(List<Channel> res) {
        channels.clear(); channels.addAll(res);
        List<String> names = new ArrayList<>();
        int n = 1;
        for (Channel c : res) names.add(String.format("%03d   %s", n++, c.name));
        if (names.isEmpty()) names.add("Aucune chaîne");
        chList.setAdapter(new ArrayAdapter<>(this, R.layout.item, names));
    }

    private void onChannelClick(int pos) {
        if (Catalog.CATS[loadingCat].sat) {
            String[] s = Catalog.SATS[pos];
            new AlertDialog.Builder(this).setTitle(s[0] + " (" + s[1] + ")").setMessage(s[2]).setPositiveButton("OK", null).show();
            return;
        }
        if (pos >= channels.size()) return;
        if (pos == playing) { setFullscreen(true); return; }
        play(pos);
    }

    private void play(int pos) {
        if (channels.isEmpty()) return;
        playing = (pos + channels.size()) % channels.size();
        Channel c = channels.get(playing);
        now.setText(String.format("%03d  %s", playing + 1, c.name));
        status.setText("Chargement...");
        player.setMediaItem(MediaItem.fromUri(c.url));
        player.prepare(); player.setPlayWhenReady(true);
    }

    private void setFullscreen(boolean f) {
        fullscreen = f;
        int v = f ? View.GONE : View.VISIBLE;
        catList.setVisibility(v); chList.setVisibility(v);
    }

    @Override public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getAction() == KeyEvent.ACTION_DOWN && fullscreen) {
            int k = e.getKeyCode();
            if (k == KeyEvent.KEYCODE_DPAD_UP || k == KeyEvent.KEYCODE_CHANNEL_UP) { play(playing + 1); return true; }
            if (k == KeyEvent.KEYCODE_DPAD_DOWN || k == KeyEvent.KEYCODE_CHANNEL_DOWN) { play(playing - 1); return true; }
        }
        return super.dispatchKeyEvent(e);
    }

    @Override public void onBackPressed() { if (fullscreen) setFullscreen(false); else super.onBackPressed(); }
    @Override protected void onStop() { super.onStop(); player.pause(); }
    @Override protected void onDestroy() { super.onDestroy(); player.release(); }
}
