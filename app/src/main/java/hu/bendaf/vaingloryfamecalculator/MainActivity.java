package hu.bendaf.vaingloryfamecalculator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        try {
            MainActivityFragment myFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);

            myFragment.onFocusChanged(hasFocus);
        } catch(ClassCastException ignored) {
        }
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            case R.id.action_feedback:
                showFeedBack();
                return true;
            case R.id.action_about:
                showAboutMe();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showFeedBack() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogBody = getLayoutInflater().inflate(R.layout.dialog_body_feedback, null, false);
        final RadioGroup rgFeedbackSubject = (RadioGroup) dialogBody.findViewById(R.id.rgFeedback);
        rgFeedbackSubject.check(R.id.rbIdea);
        final EditText etMessage = (EditText) dialogBody.findViewById(R.id.etMessage);
        builder.setTitle(getString(R.string.text_feedback))
                .setNeutralButton(R.string.text_send, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int radioButtonID = rgFeedbackSubject.getCheckedRadioButtonId();

                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
                                Uri.fromParts("mailto",getString(R.string.email_feedback), null));
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.email_feedback)});
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, rgFeedbackSubject.findViewById(radioButtonID).getContentDescription());
                        emailIntent.putExtra(Intent.EXTRA_TEXT, etMessage.getText());
                        startActivity(Intent.createChooser(emailIntent, getString(R.string.text_chooser_feedback)));
                    }
                })
                .setPositiveButton(R.string.text_cancel, null);
        builder.setView(dialogBody);
        setUpDialog(builder.show());
    }

    private void showAboutMe() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setMessage("This app is made by Felician Benda, android app developer." + "\n" +
                "app version: " + BuildConfig.VERSION_NAME);
        setUpDialog(builder.show());

    }

    private void setUpDialog(AlertDialog dialog) {
        dialog.setCanceledOnTouchOutside(true);
        // Set title divider color
        int titleDividerId = getResources().getIdentifier("titleDivider", "id", "android");
        View titleDivider = dialog.findViewById(titleDividerId);
        if(titleDivider != null)
            titleDivider.setBackgroundColor(getResources().getColor(R.color.colorAccent));
    }
}
