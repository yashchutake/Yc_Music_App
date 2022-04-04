package com.example.ycmusic;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    ListView listView;
    ArrayList<File> mySongs;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //ListView listView;//Declaration Part
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        editText=findViewById(R.id.etSearch);


        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        //Toast.makeText(MainActivity.this, "Runtime Permission Given..", Toast.LENGTH_SHORT).show();
                        mySongs = fetchSongs(Environment.getExternalStorageDirectory());//Store All Songs that we fetch to a array list
                        String[] items = new String[mySongs.size()];//create array to names of songs


                        for (int i = 0; i < mySongs.size(); i++) {
                            items[i] = mySongs.get(i).getName().replace(".mp3", "");//replace name ends with mp3 by the only Name
                        }

                         adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_selectable_list_item, items); //to  display
                        listView.setAdapter(adapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(MainActivity.this, PlaySong.class);
                                String currentSong = listView.getItemAtPosition(position).toString();
                                intent.putExtra("songList", mySongs);
                                intent.putExtra("currentSong", currentSong);
                                intent.putExtra("position", position);
                                startActivity(intent);
                            }
                        });
                        //search logic
                        editText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                adapter.getFilter().filter(charSequence);
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });
                        //
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(MainActivity.this, "Please Give Permission To Use Our App..", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();//ask permission again if not given in past time
                    }
                })

                .check();

    }
    public ArrayList<File> fetchSongs(File file){// fetch The all songs in the Files
        ArrayList<File> arrayList=new ArrayList<>();
        File [] songs=file.listFiles();//list all files present in that directory
        //Recursive implementation
        //Take All Files Add to array list
        if (songs!=null){
          //  Toast.makeText(this, "Loading....", Toast.LENGTH_SHORT).show();
                for(File myFile: songs){// songs which we fetch give me to in directory
                    if (!myFile.isHidden() && myFile.isDirectory()){//hiden nahi he and Directory he
                        arrayList.addAll(fetchSongs(myFile));// add all songs from directory to Array list
                    }
                    else {
                        if(myFile.getName().endsWith(".mp3") && !myFile.getName().startsWith(".")){//all file have of type mp3 amd Not appliction sonngs
                            arrayList.add(myFile);
                        }
                    }
                }
        }
         return arrayList;
    }
}