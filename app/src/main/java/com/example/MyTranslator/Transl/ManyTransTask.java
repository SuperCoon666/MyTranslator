package com.example.MyTranslator.Transl;

import android.os.AsyncTask;
import android.util.Log;

import com.example.MyTranslator.MainActivity;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import static com.example.MyTranslator.MainActivity.et;
import static com.example.MyTranslator.MainActivity.langf;
import static com.example.MyTranslator.MainActivity.langs;
import static com.example.MyTranslator.MainActivity.langt;

public class ManyTransTask extends AsyncTask<Integer, Void, String>
{

    final MainActivity activity;
    public static String text;
    String from, to;

    // передаём ссылку на активность, чтобы отобразить результат перевода
    public ManyTransTask(MainActivity activity) {
        this.activity = activity;
    }

    public Response requestToServer(Request req)
    {
        Gson gson = new Gson();

        String API_URL = "https://translate.yandex.net/api/v1.5/tr.json/translate";
        try {
            URL url = new URL(API_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true); // setting POST method
            OutputStream out = urlConnection.getOutputStream();

            // сериализованный объект-запрос пишем в поток
            out.write(req.toByteArray());
            InputStream stream = urlConnection.getInputStream();
            Response response = gson.fromJson(new InputStreamReader(stream), Response.class);
            return response;

        }
        catch (IOException e) { return null; }

    }

    @Override
    protected void onPreExecute()
    {
        from = langf.getSelectedItem().toString();
        to = langt.getSelectedItem().toString();
    }

    public String transForManyLangs(int c)
    {
        String last_lang = "";
        String lastl_lang = "";
        ArrayList<String> langs_now = new ArrayList<>(langs.keySet());
        if(c > 0 && c < 25)
        {
            for (int i = 0; i < c; i++)
            {
                if(i == 0)
                {
                    Log.d("TransResult", "Start");
                    String txt = et.getText().toString();
                    last_lang = langs_now.get(new Random().nextInt(langs_now.size()));
                    Request req = new Request(txt, langs.get(from) + "-" + langs.get(last_lang));
                    Response response = requestToServer(req);
                    text = response.toString();
                } else if(i == c-1)
                {
                    Request req = new Request(text,  langs.get(lastl_lang) + "-" + langs.get(to));
                    Response response = requestToServer(req);
                    text = response.toString();
                    Log.d("TransResult", "End");
                } else
                    {
                    lastl_lang = langs_now.get(new Random().nextInt(langs_now.size()));
                    Request req = new Request(text,  langs.get(last_lang) + "-" + langs.get(lastl_lang));
                    Response response = requestToServer(req);
                    text = response.toString();
                    last_lang = lastl_lang;
                    Log.d("TransResult", "Mid");
                }
            }
        }
        return text;
    }

    @Override
    protected String doInBackground(Integer... integers)
    {
        int h = integers[0];
        try
        {
            return transForManyLangs(h);
        }
        catch (Exception ex)
        {

        }
        return "BANNED";
    }

    @Override
    protected void onPostExecute(String s) {
        activity.displayResult(s);
    }
}
