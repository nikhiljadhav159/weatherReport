package com.weatherreport.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


import com.weatherreport.R;

import java.util.List;

/**
 * Created by Ajinkya.Pote on 05/01/2016.
 */
public class MultiSelectSpinner extends Spinner implements DialogInterface.OnMultiChoiceClickListener, DialogInterface.OnCancelListener {

    private List<String> items;
    private boolean[] selected;
    private String defaultText;
    private MultiSpinnerListener listener;
    private static int counter = 0;
    private Context mContext;

    public MultiSelectSpinner(Context context) {
        super(context);
    }

    public MultiSelectSpinner(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
    }

    public MultiSelectSpinner(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

        if (isChecked) {
            counter++;
            selected[which] = true;
            /*if(counter<=3) {
                selected[which] = true;
            }else{
                Toast.makeText(mContext, "Select one city.", Toast.LENGTH_SHORT).show();
            }*/
        }
        else {
            counter--;
            selected[which] = false;
        }

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // refresh text on spinner

        StringBuffer spinnerBuffer = new StringBuffer();

        for (int i = 0; i < items.size(); i++) {
            if (selected[i] == true) {
                spinnerBuffer.append(items.get(i));
                spinnerBuffer.append(", ");
            }
        }

        String spinnerText = "";
        spinnerText = spinnerBuffer.toString();
        if (spinnerText.length() > 2)
            spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
        else
            spinnerText = defaultText;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_center_item,
                new String[] { spinnerText});
        setAdapter(adapter);
        if(selected.length > 0)
            listener.onItemsSelected(selected);

    }

    @Override
    public boolean performClick() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(defaultText);
        builder.setMultiChoiceItems(
                items.toArray(new CharSequence[items.size()]), selected, this);

            builder.setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if ( counter > 0) {
                                dialog.cancel();
                            }
                            else if(counter==0){
                                builder.show();
                                Toast.makeText(mContext, "Select one city.", Toast.LENGTH_SHORT).show();
                            }else {
                                builder.show();
                                Toast.makeText(mContext,"Select one city.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        builder.setCancelable(false);
        builder.setOnCancelListener(this);
        builder.show();
        return true;
    }

    public void setItems(Context context, List<String> items, String allText, int position,
                         MultiSpinnerListener listener) {
        mContext = context;
        this.items = items;
        this.defaultText = allText;
        this.listener = listener;

        // all selected by default
        selected = new boolean[items.size()];
        for (int i = 0; i < selected.length; i++)
            selected[i] = false;

        // all text on the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_center_item, new String[] { allText });
        setAdapter(adapter);

        if(position != -1)
        {
            selected[position] = true;
            listener.onItemsSelected(selected);

            onCancel(null);
        }

    }

    public interface MultiSpinnerListener {
        public void onItemsSelected(boolean[] selected);
    }
}
