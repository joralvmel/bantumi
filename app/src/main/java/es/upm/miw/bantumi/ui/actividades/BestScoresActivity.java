package es.upm.miw.bantumi.ui.actividades;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import es.upm.miw.bantumi.R;
import es.upm.miw.bantumi.dominio.logica.ScoreDatabaseHelper;

public class BestScoresActivity extends AppCompatActivity {

    private ScoreDatabaseHelper dbHelper;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_scores);

        tableLayout = findViewById(R.id.tableLayoutBestScores);
        dbHelper = new ScoreDatabaseHelper(this);

        loadBestScores();

        Button buttonDeleteAllScores = findViewById(R.id.buttonDeleteAllScores);
        buttonDeleteAllScores.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    private void loadBestScores() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT player_name, seeds_player1, seeds_player2, date_time, " +
                "CASE WHEN seeds_player1 > seeds_player2 THEN seeds_player1 ELSE seeds_player2 END AS max_seeds " +
                "FROM scores " +
                "ORDER BY max_seeds DESC " +
                "LIMIT 10";

        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            String playerName = cursor.getString(cursor.getColumnIndexOrThrow("player_name"));
            int maxSeeds = cursor.getInt(cursor.getColumnIndexOrThrow("max_seeds"));
            String dateTime = cursor.getString(cursor.getColumnIndexOrThrow("date_time"));

            TableRow row = new TableRow(this);
            TextView playerNameView = new TextView(this);
            TextView maxSeedsView = new TextView(this);
            TextView dateTimeView = new TextView(this);

            playerNameView.setText(playerName);
            maxSeedsView.setText(String.valueOf(maxSeeds));
            maxSeedsView.setGravity(android.view.Gravity.CENTER);
            dateTimeView.setText(dateTime);

            row.addView(playerNameView);
            row.addView(maxSeedsView);
            row.addView(dateTimeView);

            tableLayout.addView(row);
        }
        cursor.close();
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.txtDialogoEliminarResultadosTitulo)
                .setMessage(R.string.txtDialogoEliminarResultadosPregunta)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> deleteAllScores())
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void deleteAllScores() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(ScoreDatabaseHelper.TABLE_SCORES, null, null);
        tableLayout.removeViews(1, tableLayout.getChildCount() - 1);
        loadBestScores();
    }
}