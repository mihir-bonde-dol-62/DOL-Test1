package com.example.android.books;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{

    private EditText mUserSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // UI to hide soft keyboard when clicked outside the box
        setupUI(findViewById(R.id.main_parent));

        // Get ref to the user input
        mUserSearch = findViewById(R.id.user_input_edit_text_view);

        final ImageButton search = findViewById(R.id.search_button);

        // Implement search button click when user presses the done button on the keyboard
        mUserSearch.setOnEditorActionListener(new EditText.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    search.performClick();
                    return true;
                }

                return false;
            }
        });
    }

    /**
     * method is called when user hits the search button
     * It connects the users search text to the query methods
     */
    public void searchFor(View view) {
        EditText userInput = findViewById(R.id.user_input_edit_text_view);
        // Get the characters and convert it to string
        String input = userInput.getText().toString();

        // Search filter for matching book titles
        RadioButton mTitleChecked = findViewById(R.id.title_radio);
        // for matching authors
        RadioButton mAuthorChecked = findViewById(R.id.author_radio);
        // for matching ISBN no
        RadioButton mIsbnChecked = findViewById(R.id.isbn_radio);

        if (!input.isEmpty()) {

            Intent results = new Intent(MainActivity.this, QueryResultsActivity.class);

            results.putExtra("topic", mUserSearch.getText().toString().toLowerCase());

            // search filter
            if (mTitleChecked.isChecked()) {
                results.putExtra("title", "intitle=");
            } else if (mAuthorChecked.isChecked()) {
                results.putExtra("author", "inauthor=");
            } else if (mIsbnChecked.isChecked()) {
                results.putExtra("isbn", "isbn=");
            }

            startActivity(results);

        } else {
            // User has not entered any search text
            Toast.makeText(
                    MainActivity.this,
                    getString(R.string.enter_text),
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * to hide the keypad and choose the filter radio box
     */
    public void setupUI(View view)
    {
        if (!(view instanceof EditText))
        {
            view.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Hide keypad
                    v.performClick();
                    hideSoftKeyboard(MainActivity.this);
                    return false;
                }
            });
        }

        if (view instanceof ViewGroup)
        {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
            {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    /**
     * hides the soft keypad
     */
    public static void hideSoftKeyboard(Activity activity)
        {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);

        // Hide the soft keypad
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
        }
}
