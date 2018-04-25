package com.aqualein.fancymovies.Utilities;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class MyApplication extends Application {

    public void onCreate() {
        super.onCreate();


// Enable command line interface initializerBuilder.enableDumpapp( Stetho.defaultDumperPluginsProvider(context) );
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(getApplicationContext()))
                        .build());

    }
}
