#ifndef CPPJAVACLASS_H
#define CPPJAVACLASS_H

#include <QObject>
#include <jni.h>

class cppJavaClass : public QObject
{
    Q_OBJECT
public:
    explicit cppJavaClass(QObject *parent = nullptr);

    Q_INVOKABLE void javaCall();
    Q_INVOKABLE void scanDevice();
    Q_INVOKABLE void stopBleScan();
    //Q_INVOKABLE void disconnectBleDevice();

    // Native method declaration
    static void onDeviceFound(JNIEnv *env, jobject thiz, jstring deviceName, jstring deviceMac);

signals:
};

#endif // CPPJAVACLASS_H
