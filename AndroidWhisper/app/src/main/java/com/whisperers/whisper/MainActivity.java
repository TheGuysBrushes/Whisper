package com.whisperers.whisper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import MessageExchange.Whisper;

public class MainActivity extends ListActivity implements BackgroundFragment.TaskCallBacks {
    private ChatAdapter chatAdapter;

    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mUsernameView;
    private EditText mAddressView;
    private View mProgressView;
    private View mLoginFormView;

    private BackgroundFragment mTaskFragment;
    private static final String TAG_TASKS_FRAGMENT = "TASK_FRAGMENT";

    private final static String TAG = "MAIN_ACTIVITY";

    private String username;
    private boolean isParameterView = true;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isParameterView",isParameterView);
        outState.putString("username",username);
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getFragmentManager().putFragment(outState, TAG_TASKS_FRAGMENT, mTaskFragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            isParameterView = savedInstanceState.getBoolean("isParameterView");
            username = savedInstanceState.getString("username");
        }

        Log.i(TAG, "onCreate: creation de l'activité : " + isParameterView);

        if (!isParameterView) {
            setNewView(savedInstanceState);
        } else {
            setContentView(R.layout.activity_parameter);

            mUsernameView = (EditText) findViewById(R.id.username);

            mAddressView = (EditText) findViewById(R.id.address);
            mAddressView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == R.id.login || id == EditorInfo.IME_NULL) {
                        attemptConnection();
                        return true;
                    }
                    return false;
                }
            });

            Button mEmailSignInButton = (Button) findViewById(R.id.connection_button);
            mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptConnection();
                }
            });

            mLoginFormView = findViewById(R.id.login_form);
            mProgressView = findViewById(R.id.login_progress);
        }
    }

    private void attemptConnection() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mAddressView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String address = mAddressView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(address)) {
            mAddressView.setError(getString(R.string.error_invalid_address));
            focusView = mAddressView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(username, address, this);
            mAuthTask.execute((Void) null);
        }
    }

    public void setNewView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        FragmentManager fm = getFragmentManager();
        if (savedInstanceState != null) {
            mTaskFragment = (BackgroundFragment) fm.getFragment(savedInstanceState, TAG_TASKS_FRAGMENT);
        }
        chatAdapter = new ChatAdapter(this);

        if (mTaskFragment.getAdapter() != null) {
            populate();
        } else {
            mTaskFragment.setAdapter(new ChatAdapter(this));
        }
        mTaskFragment.setmMainActivityListener(this);

        Whisper.setMyName(username);

        setListAdapter(chatAdapter);
        TextView texte = (TextView) findViewById(R.id.messageText);

        ImageView imageSend = (ImageView) findViewById(R.id.imageSend);
        imageSend.setOnClickListener((View v) -> {
            Log.d("WHISPER", "Click send button");
            String message = texte.getText().toString();
            texte.setText("");
            Whisper whisper = new Whisper(message);
            chatAdapter.add(new Whisper(whisper));
            chatAdapter.notifyDataSetChanged();
            mTaskFragment.sendNewMessage(whisper);
        });
    }

    private void populate() {
        int i;
        for (i = 0; i < mTaskFragment.getAdapter().getCount(); ++i) {
            chatAdapter.add(mTaskFragment.getAdapter().getItem(i));
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        TextView info = (TextView) v.findViewById(R.id.infoMsg);
        int visibility = info.getVisibility();
        if (visibility == View.VISIBLE)
            info.setVisibility(View.GONE);
        else
            info.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMessageReceived(Whisper whisper) {
        this.runOnUiThread(() -> {
            chatAdapter.add(new Whisper(whisper));
            chatAdapter.notifyDataSetChanged();
        });
        Log.d("WHISPER", "Message complete");
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setParameterView(boolean parameterView) {
        isParameterView = parameterView;
    }

    public void onConnectionDone(boolean success, String username) {
        if (success) {
            this.setUsername(username);
            this.setParameterView(false);
            this.recreate();
        } else {
            mAddressView.setError(getString(R.string.error_invalid_address));
            mAddressView.requestFocus();
        }
    }

    public void registerFragment(String mAddress){
        FragmentManager fm = getFragmentManager();
        mTaskFragment = new BackgroundFragment();
        mTaskFragment.setAddress(mAddress);
        mTaskFragment.runFragment();
        fm.beginTransaction().add(mTaskFragment, TAG_TASKS_FRAGMENT).commit();
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mAddress;
        private MainActivity activity;

        UserLoginTask(String username, String address, MainActivity parent) {
            mUsername = username;
            mAddress = address;
            activity = parent;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            activity.registerFragment(mAddress);

            do {
                Log.i(TAG, "doInBackground: " + mTaskFragment.isConnected());
            } while (!mTaskFragment.isConnected());

            Log.i(TAG, "doInBackground: tache terminée : " + mTaskFragment.isConnected());
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            Log.i(TAG, "onPostExecute: ");

            activity.onConnectionDone(success,mUsername);
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

