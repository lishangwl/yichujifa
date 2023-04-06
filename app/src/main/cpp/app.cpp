//
// Created by 大明 on 2020/8/18.
//

//
// Created by StephenLau on 2019/4/11.
//
#include <jni.h>
#include <string.h>
#include <malloc.h>


static jobject AppContext = nullptr;


static void callVoidStatic(JNIEnv *env, const char* className,const char* methodName,const char* sign,...){
    va_list args;
    va_start(args, sign);
    jclass utilsClass = env->FindClass(className);
    env->CallStaticVoidMethodV(utilsClass,env->GetStaticMethodID(utilsClass,methodName,sign),args);
    va_end(args);
}


static jobject callObjMethod(JNIEnv *env, jobject object,const char* methodName,const char* sign,...){
    va_list args;
    va_start(args, sign);
    jclass objClass = env->GetObjectClass(object);
    jobject result = env->CallObjectMethodV(object,env->GetMethodID(objClass,methodName,sign),args);
    va_end(args);
    return result;
}


void resetPackageManager(JNIEnv *env){
    jclass activityThreadClass = env->FindClass("android/app/ActivityThread");
    jfieldID sPackageManagerField = env->GetStaticFieldID(activityThreadClass,"sPackageManager","Landroid/content/pm/IPackageManager;");
    env->SetStaticObjectField(activityThreadClass,sPackageManagerField,NULL);
    jmethodID getPackageManagerMethod = env->GetStaticMethodID(activityThreadClass,"getPackageManager","()Landroid/content/pm/IPackageManager;");
    env->CallStaticObjectMethod(activityThreadClass,getPackageManagerMethod);
}

char* GetAppSignture(JNIEnv *env){
    jobject packageManager = callObjMethod(env,AppContext,"getPackageManager","()Landroid/content/pm/PackageManager;");

    jstring packageName = static_cast<jstring>(callObjMethod(env, AppContext, "getPackageName","()Ljava/lang/String;"));
    jobject packageInfo = callObjMethod(env,packageManager,"getPackageInfo","(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;",packageName,64);

    jobjectArray signaturesArray = static_cast<_jobjectArray *>(env->GetObjectField(packageInfo,
                                                                                    env->GetFieldID(
                                                                                            env->GetObjectClass(
                                                                                                    packageInfo),
                                                                                            "signatures",
                                                                                            "[Landroid/content/pm/Signature;")));
    jsize size = env->GetArrayLength(signaturesArray);
    jobject signature_obj = env->GetObjectArrayElement(signaturesArray, 0);
    jclass signature_clazz = env->GetObjectClass(signature_obj);
    jmethodID string_id = env->GetMethodID(signature_clazz, "toCharsString", "()Ljava/lang/String;");
    jstring str = static_cast<jstring>(env->CallObjectMethod(signature_obj, string_id));
    char *c_msg = (char*)env->GetStringUTFChars(str,nullptr);
    return c_msg;
}
void checkAppIsHackedFromApplication(JNIEnv *env,jobject context){
    resetPackageManager(env);
    char* className = "esqeee.xieqing.com.eeeeee.MyApp";
    char* sign = "3082033d30820225a00302010202047acf8d34300d06092a864886f70d01010b0500304f310b3009060355040613026461310b3009060355040813026173310a30080603550407130161310c300a060355040a1303617364310c300a060355040b1303617364310b3009060355040313027771301e170d3139303332313032313831335a170d3434303331343032313831335a304f310b3009060355040613026461310b3009060355040813026173310a30080603550407130161310c300a060355040a1303617364310c300a060355040b1303617364310b300906035504031302777130820122300d06092a864886f70d01010105000382010f003082010a02820101009ed0154764f6bd536df633c27d55783407330299f6b5e2f496cd5f1b8a094439cee2ea3c12b1d7a13122fb22974f47d1f5390c4ef06c40405231ad3c0723896939286c1a21ea61c4e7b14792146977041d545d17f908340b53f310424cecd1413937b76c79703cf2872342ae86982d7e26478f93760fd02715f4bb7c85a5cb159cf5be3ff9ce969276970128cdd664897c45d9ab7d3481ff6320bea66333dcfd4e7576c427db26520c163e8ea2d9d40fe0674be47a6b484b181c13b85373e35f39157b880f0bc4c666108863e965f425b44cb4794daf6ecf79c4cf3612e24a8e465a055f75e7f2f275042836c728e532e64c2afd1adf210136f0ef24fdae45750203010001a321301f301d0603551d0e04160414db77867476bfbe934828f55da48dd3fc339d169c300d06092a864886f70d01010b050003820101008d8320c328e2c0874c16cc7ab0ad4f5b9b6758f5fa00cfbe3acfebe27085a8a774aa623cd10c76fb2036a5a0891988293653ffd4061d3048b5fef93eaafaef0658054fa40893085362a016efd964ead4b7e2e53fab7e3b3a3f609e1bb2f0daa3c061c30fe385fd7a3d94307109e94c260b5fa7d5079454dbcae6327ec8ec797dccac2b42abe22f974544ecbf2e5c180db3a26c5d436c6f29ddf10733e96a8708ba9ef31d9f6543e6ca558665aae02db72e1cd6b50a57c4a0e96e3cac770e4139cd6d028ca2e4dfa211f1f7a919c8d99fea69f5a471c129d80da2b1dbdcf42336e23479547d71d403da4efe9b9e7c8e1149cf1565c14129d94e029d58b6966886";
    jobject applicationInfo = callObjMethod(env,context,"getApplicationInfo","()Landroid/content/pm/ApplicationInfo;");

    jstring applicationClassName = static_cast<jstring>(env->GetObjectField(applicationInfo,
                                                                            env->GetFieldID(
                                                                                    env->GetObjectClass(
                                                                                            applicationInfo),
                                                                                    "className",
                                                                                    "Ljava/lang/String;")));
    if  (applicationClassName == NULL){
        //失败
        env->ThrowNew(env->FindClass("java/lang/RuntimeException"),"Error");
        return;
    }
    jboolean isCopy = false;
    const char* charApplicationClassName = env->GetStringUTFChars(applicationClassName,
                                                                  reinterpret_cast<jboolean *>(isCopy));

    if (strcmp(charApplicationClassName, className) != 0) {
        //失败
        env->ThrowNew(env->FindClass("java/lang/RuntimeException"),"Error");
    }

    char* now = GetAppSignture(env);
    if (strcmp(sign, now) != 0) {
        //失败
        env->ThrowNew(env->FindClass("java/lang/RuntimeException"),"Error");
    }
    //释放
    env->ReleaseStringUTFChars(applicationClassName, charApplicationClassName);
}



JNICALL void init(JNIEnv *env, jclass obj,jobject context) {
    AppContext = context;
    //首先判断Application Class Name 是否被替换掉了，这是爆破签名(MT管理器) 常用手段
    checkAppIsHackedFromApplication(env,context);
    callVoidStatic(env,"com/xieqing/codeutils/util/Utils","init", "(Landroid/content/Context;)V",context);
}

static char app_class[] = "com/yicu/yichujifa/pro/App";//指定要注册的类
static JNINativeMethod app_methods[] = {{"init","(Landroid/content/Context;)V",(void*)init}};




static int registerNativeMethods(JNIEnv* env, const char* className,JNINativeMethod* gMethods, int numMethods){
    jclass clazz;
    clazz = (env)->FindClass(className);
    if (clazz == NULL) {
        return JNI_FALSE;
    }
    if ((env)->RegisterNatives(clazz, gMethods, numMethods) < 0) {
        return JNI_FALSE;
    }
    return JNI_TRUE;
}

static int registerNatives(JNIEnv* env){
    //注册APP类
    if (!registerNativeMethods(env, app_class, app_methods,sizeof(app_methods) / sizeof(app_methods[0])))
        return JNI_FALSE;
    return JNI_TRUE;
}


/*
* Set some test stuff up.
*
* Returns the JNI version on success, -1 on failure.
*/

jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
    JNIEnv* env = NULL;
    jint result = -1;
    if ((vm)->GetEnv( (void**) &env, JNI_VERSION_1_4) != JNI_OK) {
        return -1;
    }
    if (!registerNatives(env)) {//注册
        return -1;
    }
    result = JNI_VERSION_1_6;
    return result;
}

