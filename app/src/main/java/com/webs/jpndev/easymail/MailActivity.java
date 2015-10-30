package com.webs.jpndev.easymail;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import org.roboguice.shaded.goole.common.base.Predicates;
import org.roboguice.shaded.goole.common.collect.Collections2;
import org.roboguice.shaded.goole.common.collect.Lists;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
@ContentView(R.layout.activity_mail)
public class MailActivity extends RoboActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>
{
    @InjectView(R.id.send_btn)
    Button send_btn;
    @InjectView(R.id.editTextSubject)
    EditText editTextSubject;
    @InjectView(R.id.editTextMessage)
    EditText editTextMessage;
    @InjectView(R.id.select_email_atxv)
    MultiAutoCompleteTextView select_email_atxv;
    SharedPreferences pref;
    private static final int	REQUEST_PICK_FILE	= 1;
    private TextView filePath;
    private Button				Browse;
    private File selectedFile;
    String						filename;
    File						file;
    DataHelper dataHelper;

    //MailShowArrayAdapter mailShowArrayAdapter;
    ArrayList<String> maillist;
    ArrayList<String> automaillist;
    ArrayList<String> filter_automaillist;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        populateAutoComplete();
        setUpActionBar();
        //  setContentView(R.layout.activity_mail);
        initConfig();
     /*   send_btn = (Button) findViewById(R.id.send_btn);
        select_email_atxv = (AutoCompleteTextView) findViewById(R.id.select_email_atxv);
        textSubject = (EditText) findViewById(R.id.editTextSubject);
        textMessage = (EditText) findViewById(R.id.editTextMessage);*/
        select_email_atxv.setRawInputType(InputType.TYPE_CLASS_TEXT
                |InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                |InputType.TYPE_TEXT_FLAG_AUTO_CORRECT
                |InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        pref = getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        select_email_atxv.setText(pref.getString(Constants.MAILID, "") + "");
        editTextSubject.setText(pref.getString(Constants.SUBJECT, "") + "");
        editTextMessage.setText(pref.getString(Constants.MESSAGE, "") + "");
        filename = pref.getString(Constants.FILEPATH, "");
        filePath = (TextView) findViewById(R.id.file_path);
        Browse = (Button) findViewById(R.id.browse);

        if(filename!=null)
            if(!filename.trim().equalsIgnoreCase(""))
            {
                File file = new File(filename);
                if(file.exists())
                 filePath.setText(filename);
                else
                    filename="";
            }
        filePath.setText(filename);
        Browse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MailActivity.this, FilePicker.class);
                startActivityForResult(intent, REQUEST_PICK_FILE);
            }
        });
        send_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String to = select_email_atxv.getText().toString();
                String subject = editTextSubject.getText().toString();
                String message = editTextMessage.getText().toString();
             //   String pathname = Environment.getExternalStorageDirectory().getAbsolutePath();
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
                // email.putExtra(Intent.EXTRA_CC, new String[]{ to});
                // email.putExtra(Intent.EXTRA_BCC, new String[]{to});
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, message);
                if (selectedFile != null) {
                    filename = selectedFile.getPath();
                    file = new File(filename);
                    email.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    saveConfig(filename);
                }
                else if (filename != null && !filename.trim().equalsIgnoreCase("")) {
                    try {
                        file = new File(filename);
                        email.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                        saveConfig(filename);
                    }
                    catch (Exception e) {
                    }
                }
                saveConfig(to, subject, message);
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });
    }

    void initConfig()
    {
        dataHelper=DataHelper.newInstance(this);
        pref = getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if (pref.getString(Constants.SUBJECT, "").equals("")) {
            editor.putString(Constants.SUBJECT, "");
        }
        if (pref.getString(Constants.MESSAGE, "").equals("")) {
            editor.putString(Constants.MESSAGE, "");
        }
        if (pref.getString(Constants.MAILID, "").equals("")) {
            editor.putString(Constants.MAILID, "");
        }
        if (pref.getString(Constants.FILEPATH, "").equals("")) {
            editor.putString(Constants.FILEPATH, "");
        }
        editor.commit();
        maillist = new ArrayList<String>();
        automaillist = getAllSavedMails();
        filter_automaillist = automaillist;

    }

    void saveConfig(String mailid, String subject, String message)
    {
        pref = getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constants.SUBJECT, subject);
        editor.putString(Constants.MESSAGE, message);
        editor.putString(Constants.MAILID, mailid);
        editor.commit();
    }

    void saveConfig(String fp)
    {
        pref = getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constants.FILEPATH, fp);
        editor.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK) {
            switch (requestCode)
            {
                case REQUEST_PICK_FILE:
                    if (data.hasExtra(FilePicker.EXTRA_FILE_PATH)) {
                        selectedFile = new File(data.getStringExtra(FilePicker.EXTRA_FILE_PATH));
                        filePath.setText(selectedFile.getPath());
                    }
                    break;
            }
        }
    }




    public ArrayList<String> saveAndGetMails(ArrayList<String>newmailist)
    {
        dataHelper.addEmailListDb(newmailist);
        return dataHelper.getAllSavedMails();
    }
    public ArrayList<String> saveAndGetMails(String newmail)
    {
        dataHelper.addEmail(newmail);
        return dataHelper.getAllSavedMails();
    }

    public ArrayList<String> getAllSavedMails()
    {
        return dataHelper.getAllSavedMails();

    }


    public  static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
       /* if(TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }*/
    }





    void setupEmails() {
        //mailShowArrayAdapter = new MailShowArrayAdapter(getActivity(), maillist);
        // email_lv.setAdapter(mailShowArrayAdapter);
        //  mailShowArrayAdapter.notifyDataSetChanged();
        select_email_atxv.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length() > 3) {
                    String lastChar = s.toString().substring(s.length() - 1);
                    if(lastChar.equals(" ")) {
                        s = s.toString().substring(0, s.length() - 1);
                        if(isValidEmail(s)) {
                            boolean flag = true;
                            for (String mail : maillist) {
                                if(s.toString().equalsIgnoreCase(mail)) {
                                    flag = false;
                                    break;
                                }
                            }
                            if(flag) {
                                maillist.add(s + "");
                                automaillist =saveAndGetMails(s + "");
                                filter_automaillist = (ArrayList<String>) Lists.newArrayList(Collections2.filter(automaillist, Predicates.not(Predicates.in(maillist))));
                                //    addEmailsToAutoComplete(automaillist);
                                addEmailsToAutoComplete(filter_automaillist);
                                //  List<String> filteredList = Lists.newArrayList(Collections2.filter(automaillist, Predicates.containsPattern("How")));
                                // mailShowArrayAdapter.notifyDataSetChanged();
                            }
                            select_email_atxv.setText("");
                        }

                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
      /*  addicon_imv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_email_atxv.showDropDown();
                select_email_atxv.requestFocus();

            }
        });*/
        select_email_atxv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                maillist.add(select_email_atxv.getText().toString());
                //maillist.add(filter_automaillist.get(position));
                filter_automaillist = (ArrayList<String>) Lists.newArrayList(Collections2.filter(automaillist, Predicates.not(Predicates.in(maillist))));
                addEmailsToAutoComplete(filter_automaillist);
                select_email_atxv.setText("");
                //  mailShowArrayAdapter.notifyDataSetChanged();
            }
        });
   /* if(select_email_atxv.isPerformingCompletion()) {
        select_email_atxv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                maillist.add(select_email_atxv.getText().toString());
                filter_automaillist = (ArrayList<String>) Lists.newArrayList(Collections2.filter(automaillist, Predicates.not(Predicates.in(maillist))));
                addEmailsToAutoComplete(filter_automaillist);
                select_email_atxv.setText("");
                mailShowArrayAdapter.notifyDataSetChanged();
            }
        });
    }*/

    }

    private void populateAutoComplete() {

        if(Build.VERSION.SDK_INT >= 14) {
            // Use ContactsContract.Profile (API 14+)
            getSupportLoaderManager().initLoader(0,null,this);

        } else if(Build.VERSION.SDK_INT >= 8) {
            // Use AccountManager (API 8+)
            new SetupEmailAutoCompleteTask().execute(null, null);
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device userbundle's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI, ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE + " = ?", new String[]{ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE}, null

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                //  ContactsContract.Contacts.Data.IS_PRIMARY + " DESC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        ArrayList<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
        automaillist =saveAndGetMails(emails);
        filter_automaillist = (ArrayList<String>) Lists.newArrayList(Collections2.filter(automaillist, Predicates.not(Predicates.in(maillist))));
        //  automaillist.addAll(emails);
        addEmailsToAutoComplete(filter_automaillist);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        select_email_atxv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        select_email_atxv.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        select_email_atxv.setThreshold(1);

        select_email_atxv.setAdapter(adapter);

        select_email_atxv.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                select_email_atxv.showDropDown();
                return false;
            }
        });
    }


    private interface ProfileQuery {
        String[] PROJECTION = {ContactsContract.CommonDataKinds.Email.ADDRESS, ContactsContract.CommonDataKinds.Email.IS_PRIMARY,};

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    class SetupEmailAutoCompleteTask extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            ArrayList<String> emailAddressCollection = new ArrayList<String>();

            // Get all emails from the user's contacts and copy them to a list.
            ContentResolver cr = getContentResolver();
            Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, null, null, null);
            while (emailCur.moveToNext()) {
                String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                emailAddressCollection.add(email);
            }
            emailCur.close();

            return emailAddressCollection;
        }


        protected void onPostExecute(final ArrayList<String> emailAddressCollection) {
            ArrayList<String> mails = emailAddressCollection;
            // automaillist.addAll(emailAddressCollection);
            automaillist =saveAndGetMails(mails);
            filter_automaillist = (ArrayList<String>) Lists.newArrayList(Collections2.filter(automaillist, Predicates.not(Predicates.in(maillist))));
            //  automaillist.addAll(emails);
            addEmailsToAutoComplete(filter_automaillist);
        }
    }

    void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.actionbar_layout);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        TextView title_txv = (TextView) actionBar.getCustomView().findViewById(R.id.title_txv);
        title_txv.setText("EasyMail");
        actionBar.show();

    }
















}
