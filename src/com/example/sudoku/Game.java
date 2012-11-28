package com.example.sudoku;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class Game extends Activity {
	private static final String TAG = "Sudoku";
	
	private static final String PREF_PUZZLE = "puzzle";
	protected static final int DIFFICULTY_CONTINUE = -1;
	
	public static final String KEY_DIFFICULTY = "com.example.sudoku.difficulty";
	public static final int DIFFICULTY_EASY = 0;
	public static final int DIFFICULTY_MEDIUM = 1;
	public static final int DIFFICULTY_HARD = 2;
	
	private int puzzle[] = new int[9 * 9];
	
	private PuzzleView puzzleView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		
		// If the activity is restarted, do a continue next time
		getIntent().putExtra(KEY_DIFFICULTY, DIFFICULTY_CONTINUE);
		int diff = getIntent().getIntExtra(KEY_DIFFICULTY, DIFFICULTY_EASY);
		puzzle = getPuzzle(diff);
		calculateUsedTiles();
		
		puzzleView = new PuzzleView(this);
		setContentView(puzzleView);
		puzzleView.requestFocus();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Music.play(this, R.raw.game);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
		Music.stop(this);
		
		// Save the current puzzle
		getPreferences(MODE_PRIVATE).edit()
			.putString(PREF_PUZZLE, toPuzzleString(puzzle)).commit();
	}
	
	protected void showKeypadOnError(int x, int y) {
		int tiles[] = getUsedTiles(x, y);
		if (tiles.length == 9) {
			Toast toast = Toast.makeText(this, R.string.no_moves_label, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		} else {
			Log.d(TAG, "showKeypad; used=" + toPuzzleString(tiles));
			Dialog dialog = new Keypad(this, tiles, puzzleView);
			dialog.show();
		}
	}
	
	protected boolean setTileIfValid(int x, int y, int value) {
		int tiles[] = getUsedTiles(x, y);
		if (value != 0) {
			for (int tile : tiles) {
				if (tile == value)
					return false;
			}
		}
		setTile(x, y, value);
		calculateUsedTiles();
		
		return true;
	}
	
	// Cache of used tiles
	private final int used[][][] = new int[9][9][];
	// Return cached used tiles visible from the given coords
	protected int[] getUsedTiles(int x, int y) {
		return used[x][y];
	}
	
	private void calculateUsedTiles() {
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				used[x][y] = calculateUsedTiles(x, y);
			}
		}
	}
	
	private int[] calculateUsedTiles(int x, int y) {
		int c[] = new int[9];
		// horizontal
		for (int i = 0; i < 9; i++) {
			if (i == y)
				continue;
			
			int t = getTile(x, i);
			if (t != 0)
				c[t - 1] = t;
		}
		
		// vertical
		for (int i = 0; i < 9; i++) {
			if (i == x)
				continue;
			
			int t = getTile(i, y);
			if (t != 0)
				c[t - 1] = t;
		}
		
		// compress
		int nused = 0;
		for (int t : c) {
			if (t != 0)
				nused++;
		}
		int c1[] = new int[nused];
		nused = 0;
		for (int t : c) {
			if (t != 0)
				c1[nused++] = t;
		}
		
		return c1;
	}
	
	private final String easyPuzzle =
			"360000000004230800000004200" +
		    "070460003820000014500013020" +
			"001900000007048300000000045";
	private final String mediumPuzzle =
			"650000070000506000014000005" +
			"007009000002314700000700800" +
			"500000630000201000030000097";
	private final String hardPuzzle =
			"009000000080605020501078000" +
			"000000700706040102004000000" +
			"000720903090301080000000600";
	
	private int[] getPuzzle(int diff) {
		String puzz;
		switch(diff) {
			case DIFFICULTY_CONTINUE:
				puzz = getPreferences(MODE_PRIVATE).getString(PREF_PUZZLE, mediumPuzzle);
			case DIFFICULTY_HARD:
				puzz = hardPuzzle;
				break;
			case DIFFICULTY_MEDIUM:
				puzz = mediumPuzzle;
				break;
			case DIFFICULTY_EASY:
				puzz = easyPuzzle;
				break;
			default:
				puzz = mediumPuzzle;
				break;
		}
		
		return fromPuzzleString(puzz);
	}
	
	static private String toPuzzleString(int[] puzz) {
		StringBuilder buffer = new StringBuilder();
		for (int el : puzz) {
			buffer.append(el);
		}
		
		return buffer.toString();
	}
	
	static protected int[] fromPuzzleString(String string) {
		int[] puzz = new int[string.length()];
		for (int i = 0; i < puzz.length; i++) {
			puzz[i] = string.charAt(i) - '0';
		}
		
		return puzz;
	}
	
	private int getTile(int x, int y) {
		return puzzle[y * 9 + x];
	}
	
	private void setTile(int x, int y, int value) {
		puzzle[y * 9 + x] = value;
	}
	
	protected String getTileString(int x, int y) {
		int v = getTile(x, y);
		if (v == 0)
			return "";
		else
			return String.valueOf(v);
	}
}
