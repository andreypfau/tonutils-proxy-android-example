#include <jni.h>

extern char *StartProxy(unsigned short port);

extern char *StartProxyWithConfig(unsigned short port, const char *configTextJSON);

extern char *StopProxy();

JNIEXPORT jstring JNICALL
Java_io_github_andreypfau_tonutilsproxy_example_TonUtilsProxy_startProxy(JNIEnv *env,
                                                                         jobject thiz,
                                                                         jshort port) {
    char *res = StartProxy(port);
    return (*env)->NewStringUTF(env, res);
}


JNIEXPORT void JNICALL
Java_io_github_andreypfau_tonutilsproxy_example_TonUtilsProxy_startProxyWithConfig(JNIEnv *env,
                                                                                   jobject thiz,
                                                                                   jshort port,
                                                                                   jstring config) {
    const char *chars = (*env)->GetStringUTFChars(env, config, 0);
    StartProxyWithConfig(port, chars);
    (*env)->ReleaseStringUTFChars(env, config, chars);
}


JNIEXPORT void JNICALL
Java_io_github_andreypfau_tonutilsproxy_example_TonUtilsProxy_stopProxy(JNIEnv *env, jobject thiz) {
    StopProxy();
}
