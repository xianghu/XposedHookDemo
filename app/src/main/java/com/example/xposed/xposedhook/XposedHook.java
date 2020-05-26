package com.example.xposed.xposedhook;


import java.io.File;
import java.io.FileOutputStream;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

/**
 * using Xposed to decrypt autojs main.js
 *
 */

public class XposedHook implements IXposedHookLoadPackage {

    public void handleLoadPackage(final LoadPackageParam loadPackageParam) throws Throwable {

        XposedBridge.log("Loaded app: " + loadPackageParam.packageName);


        // this one works!
//        String pkgName = "weesun.qinghuaci"; // pkg name to hook -- 青花瓷
//        String pkgName = "com.Wujiqiangdan"; // pkg name to hook -- 无极
        String pkgName = "weesun.longjuanfeng"; // pkg name to hook -- 龙卷风


        if (loadPackageParam.packageName.equals(pkgName)) {
            XposedBridge.log("xposed hooking: " + pkgName + " has been Hooked!");

//            XposedHelpers.findAndHookMethod("com.stardust.autojs.engine.encryption.ScriptEncryption",
//                    loadPackageParam.classLoader, "decrypt", new XC_MethodHook() {
//
//            });

            XposedBridge.hookAllMethods(
                    XposedHelpers.findClass("com.stardust.autojs.engine.encryption.ScriptEncryption",
                            loadPackageParam.classLoader),
                    "decrypt", new XC_MethodHook() {

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);

                            if (param.args.length >= 1) {

                                XposedBridge.log("Data: \n");
                                XposedBridge.log(new String((byte[]) param.args[0]));

                                XposedBridge.log("i: \n");
                                XposedBridge.log(param.args[1].toString());
                                XposedBridge.log("i2: \n");
                                XposedBridge.log(param.args[2].toString());


                                XposedBridge.log("result: \n");
                                XposedBridge.log(new String((byte[]) param.getResult()));

                                File decryptedFile = new File("/sdcard/" + "decrypted.js");
                                decryptedFile.createNewFile();
                                // then using from pc: adb pull /sdcard/decrypted.js
                                try (FileOutputStream stream = new FileOutputStream(decryptedFile)) {
                                    stream.write((byte[]) param.getResult());
                                }
                            }
                        }
                    });
        }

    }
}
