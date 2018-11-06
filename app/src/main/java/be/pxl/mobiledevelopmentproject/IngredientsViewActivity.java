package be.pxl.mobiledevelopmentproject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import be.pxl.mobiledevelopmentproject.data.Ingredient;

public class IngredientsViewActivity extends AppCompatActivity implements IActionBar {
    private SQLiteDatabase mDatabase;
    private ConstraintLayout constraintLayoutIng;
    private DatabaseHelper databaseHelper;
    private EditText mEditTextName;



    /**
     * Clear any existing layout, add the current fragment
     * to the back stack, and load the new fragment
     * @param fragment fragment to load
     */

    public void loadFragment(Fragment fragment){
        //Clear any existing layout
        constraintLayoutIng.removeAllViews();


        //Load the new fragment to the layout
        getFragmentManager().beginTransaction()
                .addToBackStack(null) // Go to the previous fragment when clicking back button
                .replace(R.id.constraintLayoutIng, fragment)
                .commit();

    }

    @Override
    public void onBackPressed() {
        //clear any existing layouts before popping the stack
        if (constraintLayoutIng != null){
            constraintLayoutIng.removeAllViews();
        }

        //create fragment manager to load previous fragment
        FragmentManager fragmentManager = getFragmentManager();

        //if there are fragments left in the stack, load the previous fragment
        // this may be needed when calling addToBackStack(null) to load fragments
        if (fragmentManager.getBackStackEntryCount() >1){
            fragmentManager.popBackStack();
            return;
        }

        //exit app if there are no more fragments
        super.onBackPressed();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_view);

        databaseHelper = new DatabaseHelper(this);
        mDatabase = databaseHelper.getWritableDatabase();


        //attach the layout to a handle
        constraintLayoutIng = (ConstraintLayout) findViewById(R.id.constraintLayoutIng);

        //load the fragment into the layout handle
        getFragmentManager().beginTransaction()
                .replace(R.id.constraintLayoutIng, new IngredientsViewActivityFragment())
                .commit();

        toolbar();

        mEditTextName = findViewById(R.id.edittext_name);

        Button findRecipeButton = (Button)findViewById(R.id.findRecipeButton);
        Button buttonAdd = findViewById(R.id.button_add);


        buttonAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addItem();
            }
        });


        findRecipeButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(IngredientsViewActivity.this, RecipesActivity.class));
            }
        });

    }

    private void addItem(){

        if (mEditTextName.getText().toString().trim().length() == 0){
            return;
        }

        String name = mEditTextName.getText().toString();
        ContentValues cv = new ContentValues();
        cv.put(IngredientContract.IngredientEntry.COLUMN_NAME, name);

        mDatabase.insert(IngredientContract.IngredientEntry.TABLE_NAME, null, cv);

        mEditTextName.getText().clear();
    }

    public void toolbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Welcome");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }



    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}