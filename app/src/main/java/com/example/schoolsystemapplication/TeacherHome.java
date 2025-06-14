package com.example.schoolsystemapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TeacherHome extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private NavigationView navigationView;

    private TextView newsText;
    private final String NEWS_API_KEY = "4e69b333d7e74f40833ba8df51d7f660";
    private final String NEWS_API_URL = "https://newsapi.org/v2/everything?q=gaza&sortBy=publishedAt&language=en&apiKey=" + NEWS_API_KEY;
    private final Handler handler = new Handler();
    private final StringBuilder newsBuilder = new StringBuilder();
    private Runnable newsRunnable;
    private TextView quranVerseText, quranSurahText;
    private Handler handler1 = new Handler();
    private Runnable verseUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);

        quranVerseText = findViewById(R.id.quranVerseText);
        quranSurahText = findViewById(R.id.quranSurahText);

        fetchRandomVerse();

        verseUpdater = new Runnable() {
            @Override
            public void run() {
                fetchRandomVerse();
                handler.postDelayed(this, 20000);
            }
        };
        handler.postDelayed(verseUpdater, 20000);

        newsText = findViewById(R.id.newsText);
        fetchLatestNews();
        startNewsAutoUpdate();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        // Add the toggle to the drawer
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Setup NavigationView and its item listener
        navigationView = findViewById(R.id.nav_view);

        SharedPreferences sharedPreferences = getSharedPreferences("Mode", MODE_PRIVATE);
        String modeNow = sharedPreferences.getString("mode", "night");
        if (modeNow.equals("night")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Menu menu = navigationView.getMenu();
            MenuItem item_dark = menu.findItem(R.id.nav_dark_mode);
            item_dark.setTitle("Dark Mode");
            item_dark.setIcon(R.drawable.ic_dark_mode);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            Menu menu = navigationView.getMenu();
            MenuItem item_dark = menu.findItem(R.id.nav_dark_mode);
            item_dark.setTitle("Light Mode");
            item_dark.setIcon(R.drawable.ic_light_mode);
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                Intent intent1 = new Intent(this, TeacherHome.class);
                startActivity(intent1);
            } else if (id == R.id.nav_dark_mode) {
                toggleDark();
            } else if (id == R.id.nav_schedule) {
                Intent intent1 = new Intent(this, teacherSchedule.class);
                startActivity(intent1);
            } else if (id == R.id.nav_assignments) {
                Intent intent1 = new Intent(this, SendAssignmentActivity.class);
                intent1.putExtra("nav", "assignments");
                startActivity(intent1);
            } else if (id == R.id.nav_marks) {
                Intent intent1 = new Intent(this, ClassList_Activity.class);
                intent1.putExtra("nav", "marks");
                startActivity(intent1);
            } else if (id == R.id.nav_addMarks) {
                Intent intent1 = new Intent(this, ClassList_Activity.class);
                intent1.putExtra("nav", "addmarks");
                startActivity(intent1);
            } else if (id == R.id.nav_logout) {
                Intent intent1 = new Intent(this, LogIn.class);
                startActivity(intent1);
            }
            drawerLayout.closeDrawers();
            return true;
        });

        TextView welcomeText = findViewById(R.id.welcomeText);
        TextView dateText = findViewById(R.id.dateText);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        String dayName = dayFormat.format(calendar.getTime());
        String dateString = dateFormat.format(calendar.getTime());
        dateText.setText(dayName + ", " + dateString);

        SharedPreferences sp = getSharedPreferences("teacher_session", MODE_PRIVATE);
        String idNumber = sp.getString("id_number", "0");

        getTeacherName(idNumber, welcomeText);

    }

    private void toggleDark() {
        SharedPreferences sharedPreferences = getSharedPreferences("Mode", MODE_PRIVATE);
        Menu menu = navigationView.getMenu();
        MenuItem item_dark = menu.findItem(R.id.nav_dark_mode);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int currentNightMode = AppCompatDelegate.getDefaultNightMode();
        if (currentNightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            editor.putString("mode", "night");
            item_dark.setTitle("Dark Mode");
            item_dark.setIcon(R.drawable.ic_dark_mode);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            editor.putString("mode", "dark");
            item_dark.setTitle("Light Mode");
            item_dark.setIcon(R.drawable.ic_light_mode);
        }
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        // If the drawer is open, close it; otherwise, exit the activity
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void fetchLatestNews() {
        StringRequest request = new StringRequest(Request.Method.GET, NEWS_API_URL,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.getString("status").equals("ok")) {
                            JSONObject firstArticle = json.getJSONArray("articles").getJSONObject(0);
                            String title = firstArticle.getString("title");
                            String time = firstArticle.getString("publishedAt");

                            String newEntry = "• " + title + " \n(" + time + ")\n\n";
                            newsBuilder.insert(0, newEntry);
                            newsText.setText(newsBuilder.toString());
                        }
                    } catch (JSONException e) {
                        newsText.setText("Error loading news.");
                    }
                },
                error -> newsText.setText("Failed to load news.")) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "Mozilla/5.0");
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void fetchRandomVerse() {
        String url = "https://api.alquran.cloud/v1/ayah/random";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        JSONObject data = obj.getJSONObject("data");

                        String text = data.getString("text");
                        JSONObject surah = data.getJSONObject("surah");
                        int number = data.getInt("numberInSurah");
                        String surahName = surah.getString("englishName");

                        quranVerseText.setText("“" + text + "”");
                        quranSurahText.setText("Surah " + surahName + " - Ayah " + number);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        quranVerseText.setText("Failed to load verse.");
                        quranSurahText.setText("");
                    }
                },
                error -> {
                    quranVerseText.setText("Network error. Try again later.");
                    quranSurahText.setText("");
                });

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void startNewsAutoUpdate() {
        newsRunnable = new Runnable() {
            @Override
            public void run() {
                fetchLatestNews();
                handler.postDelayed(this, 30000);
            }
        };
        handler.post(newsRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(newsRunnable);
        handler1.removeCallbacks(verseUpdater);
    }


    private void getTeacherName(String idNumber, TextView welcomeText) {
        String url = "http://10.0.2.2:80/php_project/teacher_name.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getBoolean("success")) {
                            String name = obj.getString("name");
                            welcomeText.setText("Welcome, " + name);
                        } else {
                            welcomeText.setText("Welcome, Teacher");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        welcomeText.setText("Welcome, Teacher");
                    }
                },
                error -> {
                    welcomeText.setText("Welcome, Teacher");
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("id_number", idNumber);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

}




