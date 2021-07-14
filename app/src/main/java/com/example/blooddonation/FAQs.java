package com.example.blooddonation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FAQs extends AppCompatActivity {

    ExpandableListView expandableListView;
    List<String> listGroup;
    HashMap<String,List<String>> listHashMap;
    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);

        expandableListView = findViewById(R.id.expandable);
        listGroup = new ArrayList<>();
        listHashMap =new HashMap<>();
        adapter = new MainAdapter(this,listGroup,listHashMap);
        expandableListView.setAdapter(adapter);
        initListData();
    }

    private void initListData() {
        listGroup.add(getString(R.string.faq));
        listGroup.add(getString(R.string.faq2));
        listGroup.add(getString(R.string.faq1));
        listGroup.add(getString(R.string.faq3));
        listGroup.add(getString(R.string.faq4));
        listGroup.add(getString(R.string.faq5));
        listGroup.add(getString(R.string.faq6));
        listGroup.add(getString(R.string.faq7));
        listGroup.add(getString(R.string.faq8));

        String[] array;

        List<String> list = new ArrayList<>();
        array = getResources().getStringArray(R.array.faq);
        for(String item : array){
            list.add(item);
        }

        List<String> list1 = new ArrayList<>();
        array = getResources().getStringArray(R.array.faq2);
        for(String item : array){
            list1.add(item);
        }

        List<String> list2 = new ArrayList<>();
        array = getResources().getStringArray(R.array.faq1);
        for(String item : array){
            list2.add(item);
        }

        List<String> list3 = new ArrayList<>();
        array = getResources().getStringArray(R.array.faq3);
        for(String item : array){
            list3.add(item);
        }

        List<String> list4 = new ArrayList<>();
        array = getResources().getStringArray(R.array.faq4);
        for(String item : array){
            list4.add(item);
        }

        List<String> list5 = new ArrayList<>();
        array = getResources().getStringArray(R.array.faq5);
        for(String item : array){
            list5.add(item);
        }

        List<String> list6 = new ArrayList<>();
        array = getResources().getStringArray(R.array.faq6);
        for(String item : array){
            list6.add(item);
        }

        List<String> list7 = new ArrayList<>();
        array = getResources().getStringArray(R.array.faq7);
        for(String item : array){
            list7.add(item);
        }

        List<String> list8 = new ArrayList<>();
        array = getResources().getStringArray(R.array.faq8);
        for(String item : array){
            list8.add(item);
        }



        listHashMap.put(listGroup.get(0),list);
        listHashMap.put(listGroup.get(1),list1);
        listHashMap.put(listGroup.get(2),list2);
        listHashMap.put(listGroup.get(3),list3);
        listHashMap.put(listGroup.get(4),list4);
        listHashMap.put(listGroup.get(5),list5);
        listHashMap.put(listGroup.get(6),list6);
        listHashMap.put(listGroup.get(7),list7);
        listHashMap.put(listGroup.get(8),list8);


        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        Intent myIntent = new Intent(FAQs.this, HomeNavigation.class);
        startActivity(myIntent);
        this.finish();
    }
}
