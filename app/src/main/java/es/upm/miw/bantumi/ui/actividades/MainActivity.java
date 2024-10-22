package es.upm.miw.bantumi.ui.actividades;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import es.upm.miw.bantumi.dominio.logica.ScoreDatabaseHelper;
import es.upm.miw.bantumi.ui.fragmentos.FinalAlertDialog;
import es.upm.miw.bantumi.R;
import es.upm.miw.bantumi.dominio.logica.JuegoBantumi;
import es.upm.miw.bantumi.ui.fragmentos.ConfirmationAlertDialog;
import es.upm.miw.bantumi.ui.viewmodel.BantumiViewModel;

public class MainActivity extends AppCompatActivity {

    protected final String LOG_TAG = "MiW";
    public JuegoBantumi juegoBantumi;
    private BantumiViewModel bantumiVM;
    int numInicialSemillas;
    boolean changed = false;
    boolean began = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Instancia el ViewModel y el juego, y asigna observadores a los huecos
        numInicialSemillas = getResources().getInteger(R.integer.intNumInicialSemillas);
        bantumiVM = new ViewModelProvider(this).get(BantumiViewModel.class);
        juegoBantumi = new JuegoBantumi(bantumiVM, JuegoBantumi.Turno.turnoJ1, numInicialSemillas);
        crearObservadores();
    }

    /**
     * Crea y subscribe los observadores asignados a las posiciones del tablero.
     * Si se modifica el contenido del tablero -> se actualiza la vista.
     */
    private void crearObservadores() {
        for (int i = 0; i < JuegoBantumi.NUM_POSICIONES; i++) {
            int finalI = i;
            bantumiVM.getNumSemillas(i).observe(    // Huecos y almacenes
                    this,
                    new Observer<Integer>() {
                        @Override
                        public void onChanged(Integer integer) {
                            mostrarValor(finalI, juegoBantumi.getSemillas(finalI));
                            if(began) changed = true;
                        }
                    });
        }
        bantumiVM.getTurno().observe(   // Turno
                this,
                new Observer<JuegoBantumi.Turno>() {
                    @Override
                    public void onChanged(JuegoBantumi.Turno turno) {
                        marcarTurno(juegoBantumi.turnoActual());
                        if(!began) began = true;
                    }
                }
        );
    }

    /**
     * Indica el turno actual cambiando el color del texto
     *
     * @param turnoActual turno actual
     */
    private void marcarTurno(@NonNull JuegoBantumi.Turno turnoActual) {
        TextView tvJugador1 = findViewById(R.id.tvPlayer1);
        TextView tvJugador2 = findViewById(R.id.tvPlayer2);
        switch (turnoActual) {
            case turnoJ1:
                tvJugador1.setTextColor(getColor(R.color.white));
                tvJugador1.setBackgroundColor(getColor(android.R.color.holo_blue_light));
                tvJugador2.setTextColor(getColor(R.color.black));
                tvJugador2.setBackgroundColor(getColor(R.color.white));
                break;
            case turnoJ2:
                tvJugador1.setTextColor(getColor(R.color.black));
                tvJugador1.setBackgroundColor(getColor(R.color.white));
                tvJugador2.setTextColor(getColor(R.color.white));
                tvJugador2.setBackgroundColor(getColor(android.R.color.holo_blue_light));
                break;
            default:
                tvJugador1.setTextColor(getColor(R.color.black));
                tvJugador2.setTextColor(getColor(R.color.black));
        }
    }

    /**
     * Muestra el valor <i>valor</i> en la posición <i>pos</i>
     *
     * @param pos posición a actualizar
     * @param valor valor a mostrar
     */
    private void mostrarValor(int pos, int valor) {
        String num2digitos = String.format(Locale.getDefault(), "%02d", pos);
        // Los identificadores de los huecos tienen el formato casilla_XX
        int idBoton = getResources().getIdentifier("casilla_" + num2digitos, "id", getPackageName());
        if (0 != idBoton) {
            TextView viewHueco = findViewById(idBoton);
            viewHueco.setText(String.valueOf(valor));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.opciones_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.opcAjustes: // @todo Preferencias
//                startActivity(new Intent(this, BantumiPrefs.class));
//                return true;
            case R.id.opcAcercaDe:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.aboutTitle)
                        .setMessage(R.string.aboutMessage)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
                return true;

            // @TODO!!! resto opciones

            case R.id.opcReiniciarPartida:
                reiniciarPartida();
                return true;

            case R.id.opcGuardarPartida:
                guardarPartida();
                return true;

            case R.id.opcRecuperarPartida:
                recuperarPartida();
                return true;

            default:
                Snackbar.make(
                        findViewById(android.R.id.content),
                        getString(R.string.txtSinImplementar),
                        Snackbar.LENGTH_LONG
                ).show();
        }
        return true;
    }

    private void reiniciarPartida() {
        ConfirmationAlertDialog.Back callBack = () -> {
            changed = false;
            began = false;
            bantumiVM.clear();
            juegoBantumi.clear(JuegoBantumi.Turno.turnoJ1);
            crearObservadores();
        };
        if (changed) {
            new ConfirmationAlertDialog(
                    R.string.txtDialogoReiniciarTitulo,
                    R.string.txtDialogoReiniciarPregunta,
                    callBack
            ).show(getSupportFragmentManager(),"DIALOGO_REINICIAR");
        } else {
            callBack.onSuccess();
        }
    }

    private void guardarPartida() {
        String fileName = "bantumi_save.txt";
        String gameState = juegoBantumi.serializa();

        try (FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE)) {
            fos.write(gameState.getBytes());
            Snackbar.make(
                    findViewById(android.R.id.content),
                    getString(R.string.txtGuardarTitulo),
                    Snackbar.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG, getString(R.string.txtErrorNotFound), e);
            Snackbar.make(
                    findViewById(android.R.id.content),
                    getString(R.string.txtErrorGuardar),
                    Snackbar.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e(LOG_TAG, getString(R.string.txtErrorIO), e);
            Snackbar.make(
                    findViewById(android.R.id.content),
                    getString(R.string.txtErrorGuardar),
                    Snackbar.LENGTH_LONG).show();
        }
    }

    private void recuperarPartida() {
        ConfirmationAlertDialog.Back callBack = () -> {
            String fileName = "bantumi_save.txt";
            try (FileInputStream fis = openFileInput(fileName)) {
                byte[] data = new byte[fis.available()];
                fis.read(data);
                String gameState = new String(data);
                juegoBantumi.deserializa(gameState);
                crearObservadores();
                Snackbar.make(
                        findViewById(android.R.id.content),
                        getString(R.string.txtRecuperarTitulo),
                        Snackbar.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                Log.e(LOG_TAG, getString(R.string.txtErrorNotFound), e);
                Snackbar.make(
                        findViewById(android.R.id.content),
                        getString(R.string.txtErrorRecuperar),
                        Snackbar.LENGTH_LONG).show();
            } catch (IOException e) {
                Log.e(LOG_TAG, getString(R.string.txtErrorIO), e);
                Snackbar.make(
                        findViewById(android.R.id.content),
                        getString(R.string.txtErrorRecuperar),
                        Snackbar.LENGTH_LONG).show();
            }
        };

        if (changed) {
            new ConfirmationAlertDialog(
                    R.string.txtDialogoRecuperarTitulo,
                    R.string.txtDialogoRecuperarPregunta,
                    callBack
            ).show(getSupportFragmentManager(), "DIALOGO_RECUPERAR");
        } else {
            callBack.onSuccess();
        }
    }

    /**
     * Acción que se ejecuta al pulsar sobre cualquier hueco
     *
     * @param v Vista pulsada (hueco)
     */
    public void huecoPulsado(@NonNull View v) {
        String resourceName = getResources().getResourceEntryName(v.getId()); // pXY
        int num = Integer.parseInt(resourceName.substring(resourceName.length() - 2));
        Log.i(LOG_TAG, "huecoPulsado(" + resourceName + ") num=" + num);
        switch (juegoBantumi.turnoActual()) {
            case turnoJ1:
                Log.i(LOG_TAG, "* Juega Jugador");
                juegoBantumi.jugar(num);
                break;
            case turnoJ2:
                Log.i(LOG_TAG, "* Juega Computador");
                juegoBantumi.juegaComputador();
                break;
            default:    // JUEGO TERMINADO
                finJuego();
        }
        if (juegoBantumi.juegoTerminado()) {
            finJuego();
        }
    }

    private void guardarPuntuacion() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String playerName = prefs.getString("player_name", "Unknown Player");

        int seedsPlayer1 = juegoBantumi.getSemillas(6);
        int seedsPlayer2 = juegoBantumi.getSemillas(13);

        String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        ScoreDatabaseHelper dbHelper = new ScoreDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ScoreDatabaseHelper.COLUMN_PLAYER_NAME, playerName);
        values.put(ScoreDatabaseHelper.COLUMN_DATE_TIME, dateTime);
        values.put(ScoreDatabaseHelper.COLUMN_SEEDS_PLAYER1, seedsPlayer1);
        values.put(ScoreDatabaseHelper.COLUMN_SEEDS_PLAYER2, seedsPlayer2);

        db.insert(ScoreDatabaseHelper.TABLE_SCORES, null, values);
        db.close();
    }

    /**
     * El juego ha terminado. Volver a jugar?
     */
    private void finJuego() {
        String texto = (juegoBantumi.getSemillas(6) > 6 * numInicialSemillas)
                ? "Gana Jugador 1"
                : "Gana Jugador 2";
        if (juegoBantumi.getSemillas(6) == 6 * numInicialSemillas) {
            texto = "¡¡¡ EMPATE !!!";
        }

        // @TODO guardar puntuación
        guardarPuntuacion();

        // terminar
        new FinalAlertDialog(texto).show(getSupportFragmentManager(), "ALERT_DIALOG");
    }
}