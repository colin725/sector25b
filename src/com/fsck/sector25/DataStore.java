package com.fsck.sector25;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

public class DataStore implements Serializable {

    ArrayList<ArrayList<LevelScore>> levels;
    private final String FILENAME = "Sector25DataStore";

    public DataStore(int numLevels, Context context) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = context.openFileInput(FILENAME);
            ois = new ObjectInputStream(fis);
            levels = (ArrayList<ArrayList<LevelScore>>) ois.readObject();
            if (levels != null) {
                for (List<LevelScore> l : levels) {
                    for (LevelScore s : l) {
                        Log.e("MATT", "Name: " + s.name + "; mScore: " + s.score);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        if (levels == null) {
            levels = new ArrayList<ArrayList<LevelScore>>();
            for (int i = 0; i < numLevels; i++) {
                levels.add(new ArrayList<LevelScore>());
            }
        }
    }

    public void addScore(int level, int score, String name) {
        levels.get(level).add(new LevelScore(score, name));
    }

    public ArrayList<LevelScore> getScores(int level) {
        return (ArrayList<LevelScore>) levels.get(level);
    }

    public void saveToDisk(Context context) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(levels);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    public void deleteData(Context context) {
        context.deleteFile(FILENAME);
    }

    class LevelScore implements Serializable {

        /**
         * 
         */
        private static final long serialVersionUID = 8056524397105704746L;
        public int score;
        public String name;

        public LevelScore(int score, String name) {
            this.score = score;
            this.name = name;
        }
    }

}
