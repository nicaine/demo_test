package com.example.myapplication;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.dynamic.Dynamic;

import java.io.File;
import java.io.IOException;

import dalvik.system.DexClassLoader;

public class MainActivity extends Activity {
    private Dynamic dynamic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadDexClass();
    }

    /**
     * 加载dex文件中的class，并调用其中的sayHello方法
     */
    private void loadDexClass() {
        File cacheFile = FileUtils.getCacheDir(getApplicationContext());
        String internalPath = cacheFile.getAbsolutePath() + File.separator + "dynamic_dex.jar";
        File desFile = new File(internalPath);
        try {
            if (!desFile.exists()) {
                desFile.createNewFile();
                FileUtils.copyFiles(this, "dynamic_dex.jar", desFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        PackageManager packageManager = getPackageManager();
//        packageManager.getPackageArchiveInfo();
        //下面开始加载dex class
        DexClassLoader dexClassLoader = new DexClassLoader(internalPath, cacheFile.getAbsolutePath(), null, getClassLoader());
        try {
            Class libClazz = dexClassLoader.loadClass("com.example.myapplication.dynamic.impl.DynamicImpl");
            dynamic = (Dynamic) libClazz.newInstance();
            if (dynamic != null) {
                Toast.makeText(this, dynamic.sayHello(), Toast.LENGTH_LONG).show();
                Log.i("sssssssssssss", dynamic.sayHello());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
