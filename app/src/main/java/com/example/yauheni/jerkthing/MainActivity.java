package com.example.yauheni.jerkthing;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

  private static final String UNDO_FIELD = "undo_field";

  private Game game;
  private GameView gameView;
  private SharedPreferences sharedPreferences;

  private static final String TAG = "myLog";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.d(TAG, "onCreate()");
    super.onCreate(savedInstanceState);
    game = new Game();
    gameView = new GameView(this, game);
    setContentView(gameView);
    game.attachView(gameView);
    sharedPreferences = getPreferences(MODE_PRIVATE);
  }

  @Override
  protected void onStart() {
    Log.d(TAG, "onStart()");
    if(sharedPreferences.getBoolean("hasSavedState",false)){
      load();
    } else {
      game.start();
    }
    super.onStart();
  }

  @Override
  protected void onPause() {
    Log.d(TAG, "onPause()");
    super.onPause();
  }

  @Override
  protected void onResume() {
    Log.d(TAG, "onResume()");
    super.onResume();
  }

  @Override
  public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
    super.onSaveInstanceState(outState, outPersistentState);
    Log.d(TAG, "onSaveInstanceState()");
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    Log.d(TAG, "onRestoreInstanceState()");
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
  }

  @Override
  protected void onStop() {
    Log.d(TAG, "onStop()");
    save();
    super.onStop();
  }


  @Override
  protected void onDestroy() {
    Log.d(TAG, "onDestroy()");
    super.onDestroy();
  }

  private void save(){
    //SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
    Editor editor = sharedPreferences.edit();
    for (int i = 0; i < PlayGround.SIZE_OF_ARRAY; i++) {
      for (int j = 0; j < PlayGround.SIZE_OF_ARRAY; j++) {
        editor.putInt(i+":"+j, game.getBoardValueAt(i,j));
      }
    }

    for (int i = 0; i < PlayGround.SIZE_OF_ARRAY; i++) {
      for (int j = 0; j < PlayGround.SIZE_OF_ARRAY; j++) {
        editor.putInt(UNDO_FIELD + i + ":" + j, game.getStebBackValueAt(i,j));
      }
    }

    editor.putBoolean("hasSavedState", true);
    editor.apply();
  }

  private void load(){
    //SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
    int[][] array = new int[PlayGround.SIZE_OF_ARRAY][PlayGround.SIZE_OF_ARRAY];
    for (int i = 0; i < PlayGround.SIZE_OF_ARRAY; i++) {
      for (int j = 0; j < PlayGround.SIZE_OF_ARRAY; j++) {
        array[i][j] = sharedPreferences.getInt(i+":"+j,0);
      }
    }

    int[][] backArray = new int[PlayGround.SIZE_OF_ARRAY][PlayGround.SIZE_OF_ARRAY];
    for (int i = 0; i < PlayGround.SIZE_OF_ARRAY; i++) {
      for (int j = 0; j < PlayGround.SIZE_OF_ARRAY; j++) {
        backArray[i][j] = sharedPreferences.getInt(UNDO_FIELD + i + ":" + j,0);
      }
    }

    game.load(array, backArray);
  }
}
