package es.upm.miw.bantumi.ui.fragmentos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import es.upm.miw.bantumi.R;
import es.upm.miw.bantumi.ui.actividades.MainActivity;

public class RebootAlertDialog extends AppCompatDialogFragment {
    public interface Back {
        void onSuccess();
    }

    int title;
    int message;
    Back back;

    public RebootAlertDialog(int title, int message, Back back) {
        this.title = title;
        this.message = message;
        this.back = back;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MainActivity main = (MainActivity) getActivity();

        assert main != null;
        AlertDialog.Builder builder = new AlertDialog.Builder(main);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(
                        getString(R.string.txtOpcionDialogoSi),
                        (dialog, which) -> {
                            back.onSuccess();
                        }
                )
                .setNegativeButton(
                        getString(R.string.txtOpcionDialogoNo),
                        (dialog, which) -> {
                        }
                );
        return builder.create();
    }

}