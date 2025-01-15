package com.example.finalprojectgrizzbot_group8;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "grizzbot.db";
    private static final String DATABASE_PATH = "/data/data/com.example.finalprojectgrizzbot_group8/databases/";
    private static final int DATABASE_VERSION = 1;
    private final Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void initializeDatabase() {
        if (!checkDatabaseExists()) {
            copyDatabase();
        }
    }

    private boolean checkDatabaseExists() {
        File databaseFile = new File(DATABASE_PATH + DATABASE_NAME);
        return databaseFile.exists();
    }

    private void copyDatabase() {
        try {
            InputStream input = context.getAssets().open(DATABASE_NAME);
            File databaseFolder = new File(DATABASE_PATH);
            if (!databaseFolder.exists()) {
                databaseFolder.mkdirs();
            }
            OutputStream output = new FileOutputStream(DATABASE_PATH + DATABASE_NAME);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SQLiteDatabase getDatabase() {
        return SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
    }

    // Question Table Name and Columns
    public static final String QUESTION_TABLE = "questions";
    public static final String QUESTION_ID = "idQuestion";
    public static final String QUESTION_QUESTION = "question";
    public static final String QUESTION_ANSWER = "answer";
    public static final String QUESTION_CATEGORYID = "category_id";
    public static final String QUESTION_CATEGORY_FK_NAME = "FK_category_categoryid_idcategory";

    // Category Table Name and Columns
    public static final String CATEGORY_TABLE = "category";
    public static final String CATEGORY_ID = "idcategory";
    public static final String CATEGORY_CATEGORY = "category";

    // Question Table Name and Columns
    public static final String QUESTIONHISTORY_TABLE = "question_history";
    public static final String QUESTIONHISTORY_ID = "id_history";
    public static final String QUESTIONHISTORY_QUESTION = "question";
    public static final String QUESTIONHISTORY_ANSWER = "answer";
    public static final String QUESTIONHISTORY_USER = "user";
//
//    private static final String QUESTION_TABLE_CREATE =
//            "CREATE TABLE " + QUESTION_TABLE + " (" +
//                    QUESTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                    QUESTION_QUESTION + " TEXT, " +
//                    QUESTION_ANSWER + " TEXT, " +
//                    QUESTION_CATEGORYID + " INTEGER," +
//                    "CONSTRAINT " + QUESTION_CATEGORY_FK_NAME +" FOREIGN KEY("+QUESTION_CATEGORYID+") REFERENCES "+CATEGORY_TABLE+"("+CATEGORY_ID+"))";
//
//    private static final String SCRAPED_DATA_TABLE_CREATE =
//            "CREATE TABLE " + SCRAPED_TABLE + " ("+
//                    SCRAPED_URL + " TEXT, " +
//                    SCRAPED_CONTENT + " TEXT);";
//
//    private static final String CATEGORY_TABLE_CREATE =
//            "CREATE TABLE " + CATEGORY_TABLE + " ("+
//                    CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                    CATEGORY_CATEGORY + " TEXT);";
//
//    // Constructor
//    public DBHelper(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the table when the database is created
        if (!checkDatabaseExists()) {
            copyDatabase();
        }
        new FetchCategoriesTask().execute("https://oakland.edu"); // Replace with your target URL

        //onDatabaseCreated();
    }
//
//    public void onDatabaseCreated() {
//        // Pre-load some responses into the database
//        insertQuestion("Hi", "Hello!",null);
//        insertQuestion("How are you?", "I'm doing great, thank you!",null);
//    }
//
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If the database version is updated, drop the old table and create a new one
        db.execSQL("DROP TABLE IF EXISTS " + QUESTION_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORY_TABLE);
        onCreate(db);
    }

    // Get a response for a given question
    public String getResponse(String question) {
        SQLiteDatabase db = this.getReadableDatabase();
        question = "%" + question + "%";

        String query = "SELECT " + QUESTION_ANSWER + " FROM " + QUESTION_TABLE + " WHERE " + QUESTION_QUESTION + " LIKE ?";
        Cursor cursor = db.rawQuery(query, new String[]{question});

        String response = "Sorry, I don't understand that.";  // Default response if no answer is found

        // Check if cursor is not null and contains data
        if (cursor != null && cursor.moveToFirst()) {
            // Get column index for COL_ANSWER
            int answerColumnIndex = cursor.getColumnIndex(QUESTION_ANSWER);

            // Ensure the column index is valid (≥ 0)
            if (answerColumnIndex >= 0) {
                response = cursor.getString(answerColumnIndex);
            }
        }

        // Close the cursor to avoid memory leaks
        if (cursor != null) {
            cursor.close();
        }

        return response;
    }

    public long insertQuestionHistory(String question, String answer, String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(QUESTIONHISTORY_QUESTION, question);
        contentValues.put(QUESTIONHISTORY_ANSWER, answer);
        contentValues.put(QUESTIONHISTORY_USER, user);

        return db.insert(QUESTIONHISTORY_TABLE, null, contentValues);
    }

    public Cursor getQuestionHistory(String user) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + QUESTIONHISTORY_TABLE + " WHERE " + QUESTIONHISTORY_USER + " = '"+ user +"' ORDER BY " +QUESTIONHISTORY_ID;
        return db.rawQuery(query, null);
    }

    public long insertQuestion(String question, String answer, Integer category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(QUESTION_QUESTION, question);
        contentValues.put(QUESTION_ANSWER, answer);
        contentValues.put(QUESTION_CATEGORYID, category);

        return db.insert(QUESTION_TABLE, null, contentValues);
    }

    public Cursor getAllCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + CATEGORY_TABLE + " ORDER BY " +CATEGORY_CATEGORY, null);
    }

    public String getCategory(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String category = "";
        String query = "SELECT " + CATEGORY_CATEGORY + " FROM " + CATEGORY_TABLE +" WHERE " + CATEGORY_ID + " = " + String.valueOf(id);

        Cursor cursor = db.rawQuery(query,null);

        if (cursor != null && cursor.moveToFirst()){
            category = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY_CATEGORY));
            cursor.close();
        } else {
            category = "No Category found.";
        }
        return category;
    }

    public long insertCategory(String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_CATEGORY, category);

        return db.insert(CATEGORY_TABLE, null, contentValues);
    }

    // AsyncTask to perform web scraping
    private class FetchCategoriesTask extends AsyncTask<String, Void, List<String>> {
        StringBuilder textData = new StringBuilder();
        @Override
        protected List<String> doInBackground(String... urls) {
            List<String> categories = new ArrayList<>();
            try {
                // Connect to the website and fetch the HTML document
                Document doc = Jsoup.connect(urls[0]).get();

                // Select elements that represent categories (modify the CSS selector to match your target site)
                Elements categoryElements = doc.select(".globalNavBtn"); // Replace with actual CSS selector

                for (org.jsoup.nodes.Element element : categoryElements) {
                    insertCategory(element.text());
                    textData.append(element.text()).append("\n\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //insertScrapedData(textData);
            return categories;
        }

        @Override
        protected void onPostExecute(List<String> categories) {
            super.onPostExecute(categories);

            // Display categories (or integrate with the chatbot)
            if (!categories.isEmpty()) {
                StringBuilder builder = new StringBuilder();
                for (String category : categories) {
                    builder.append(category).append("\n");
                }
                //textView.setText(builder.toString());
            }
        }
    }

}
