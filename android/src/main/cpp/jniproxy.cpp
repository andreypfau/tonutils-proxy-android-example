#include <jni.h>
#include "tonutils-proxy.h"

extern "C"
JNIEXPORT jstring JNICALL
Java_io_github_andreypfau_tonutilsproxy_example_TonUtilsProxy_startProxy(JNIEnv *env,
                                                                         jobject thiz,
                                                                         jshort port) {
    auto res = StartProxy(port);
    return env->NewStringUTF(res);
}

extern "C"
JNIEXPORT void JNICALL
Java_io_github_andreypfau_tonutilsproxy_example_TonUtilsProxy_startProxyWithConfig(JNIEnv *env,
                                                                                   jobject thiz,
                                                                                   jshort port,
                                                                                   jstring config) {
    auto config_chars = env->GetStringUTFChars(config, 0);
    StartProxyWithConfig(port, (char *) config_chars);
}

extern "C"
JNIEXPORT void JNICALL
Java_io_github_andreypfau_tonutilsproxy_example_TonUtilsProxy_stopProxy(JNIEnv *env, jobject thiz) {
    StopProxy();
}