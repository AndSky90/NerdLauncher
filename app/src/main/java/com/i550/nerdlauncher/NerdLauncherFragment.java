package com.i550.nerdlauncher;


import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
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

    public void setupAdapter() { //нам не надо нихрена запускать, поэтому не вызываем шузер и старт, а делаем вот так
        Intent startupIntent = new Intent(Intent.ACTION_MAIN); //создаем интент с запросом =действия мэйн (реагирует как обычный запуск)
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);    // добавляем запрос =категории ланчер
        PackageManager pm = getActivity().getPackageManager();  //достаем ПМ
        List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0); //находим список подходящих активити (пакедж мэн возвращает ResolveInfo)

        Collections.sort(activities, new Comparator<ResolveInfo>() {    //ща как отсортируем компаратором
                    @Override
                    public int compare(ResolveInfo o1, ResolveInfo o2) {
                        PackageManager pm = getActivity().getPackageManager();
                        return String.CASE_INSENSITIVE_ORDER.compare(
                                o1.loadLabel(pm).toString(),
                                o2.loadLabel(pm).toString());
                    }
                }
        );

        Log.i(TAG, "Found " + activities.size() + " activities."); //ResolveInfo содержит всю инфу, метки, лейблы активностей
        mRecyclerView.setAdapter(new ActivityAdapter(activities)); //setupAdapter создает экземпляр ActivityAdapter'a и назначает его адаптером RecycleView
    }

    //_______________________________________________________________________________________________________________________

     private class ActivityHolder extends RecyclerView.ViewHolder implements View.OnClickListener{          //реализация ViewHolder у recycleView
        private ResolveInfo mResolveInfo;
        private TextView mNameTextView;

        public ActivityHolder(View itemView){
            super(itemView);
            mNameTextView=(TextView)itemView;
            mNameTextView.setOnClickListener(this); //назначаем листенер (при нажатии на пункт в списке активитей)
        }
        public void bindActivity(ResolveInfo resolveInfo){
            mResolveInfo=resolveInfo;
            PackageManager pm = getActivity().getPackageManager();
            String appName = mResolveInfo.loadLabel(pm).toString();
            mNameTextView.setText(appName);
        }

         @Override
         public void onClick(View v) {      //интент при нажатии на имя активити в списке активитей
             ActivityInfo activityInfo = mResolveInfo.activityInfo;     //ActivityInfo это часть ResolveInfo
             Intent i = new Intent(Intent.ACTION_MAIN).setClassName(activityInfo.applicationInfo.packageName, activityInfo.name);   //имя пакета и имя класса активности
             startActivity(i);      //создается интент (pkgName,clsName) - получает ComponentName и по нему находит полное имя пакета вызываемой активити
                                        //можно например public Intent setComponent(ComponentName c)
         }
     }

    //_______________________________________________________________________________________________________________________

    private class ActivityAdapter extends RecyclerView.Adapter<ActivityHolder>{     //реализация адаптера у recycleView
        private final List<ResolveInfo> mActivities;

        public ActivityAdapter(List<ResolveInfo> activities){
            mActivities=activities;
        }


        @Override
        public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1,parent,false);
            return new ActivityHolder(view);
        }

        @Override
        public void onBindViewHolder(ActivityHolder holder, int position) {
            ResolveInfo resolveInfo = mActivities.get(position);
            holder.bindActivity(resolveInfo);
        }

        @Override
        public int getItemCount() {
            return mActivities.size();
        }
    }



}
