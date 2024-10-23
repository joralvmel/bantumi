package es.upm.miw.bantumi.ui.fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.DialogFragment;

import es.upm.miw.bantumi.R;
import es.upm.miw.bantumi.dominio.logica.JuegoBantumi;
import es.upm.miw.bantumi.ui.actividades.MainActivity;

public class FinalAlertDialog extends DialogFragment {

    private final String tituloAlertDialog;
    private final Runnable onConfirm;

    public FinalAlertDialog(String tituloAlertDialog, Runnable onConfirm) {
        this.tituloAlertDialog = tituloAlertDialog;
        this.onConfirm = onConfirm;
    }

    @NonNull
    @Override
    public AppCompatDialog onCreateDialog(Bundle savedInstanceState) {
        final MainActivity main = (MainActivity) requireActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(main);
        builder
                .setTitle(tituloAlertDialog)
                .setMessage(R.string.txtDialogoFinalPregunta)
                .setPositiveButton(
                        getString(android.R.string.ok),
                        (dialog, which) -> {
                            main.juegoBantumi.inicializar(JuegoBantumi.Turno.turnoJ1);
                            if (onConfirm != null) {
                                onConfirm.run();
                            }
                        }
                )
                .setNegativeButton(
                        getString(android.R.string.cancel),
                        (dialog, which) -> main.finish()
                );

        return builder.create();
    }
}