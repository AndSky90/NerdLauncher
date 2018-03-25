package com.i550.nerdlauncher;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class NerdLauncherFragment extends Fragment{

    private RecyclerView mRecyclerView; //- ссылка на ресайклВью
    private static final String TAG = "NerdLauncherFragment";   //тэг для лога

    public static NerdLauncherFragment newInstance() {
        return new NerdLauncherFragment();
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nerd_launcher,container,false); //вдуваем наш лэйаут fragment_nerd_launcher
        mRecyclerView = v.findViewById(R.id.app_recycle_view);      // в нем находим ресайклВью
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); //-так надо для ресайклВью
        setupAdapter(); //вызываем адаптер
        return v;
    }

    public void setupAdapter(){
        Intent startupIntent = new Intent(Intent.ACTION_MAIN); //создаем интент
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);    // добавляем категорию лаунчер
        PackageManager pm = getActivity().getPackageManager();  //достаем
        List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent,0);
        Log.i(TAG,"Found " + activities.size() + " activities.");
    }
}
