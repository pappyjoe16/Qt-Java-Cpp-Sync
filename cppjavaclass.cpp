#include "cppjavaclass.h"
#include <QCoreApplication>
#include <QDebug>
#include <QJniObject>
#include <QMap>
#include <QString>
#include <QTimer>
#include <jni.h>

#if QT_CONFIG(permissions)
#include <QtCore/qcoreapplication.h>
#include <QtCore/qpermissions.h>
#endif

cppJavaClass::cppJavaClass(QObject *parent)
    : QObject{parent}
{}

void cppJavaClass::javaCall()
{
    QString javaMessage
        = QJniObject::callStaticMethod<jstring>("org/qtproject/example/JavaScriptCppTest",
                                                "getJavaMessage")
              .toString();
    qWarning() << "Output from qt C++: " << javaMessage;
    scanDevice();
}

void cppJavaClass::scanDevice()
{
#if QT_CONFIG(permissions)
    //! [permissions]
    QBluetoothPermission permission{};
    permission.setCommunicationModes(QBluetoothPermission::Access);
    switch (qApp->checkPermission(permission)) {
    case Qt::PermissionStatus::Undetermined:
        qApp->requestPermission(permission, this, &cppJavaClass::scanDevice);
        return;
    case Qt::PermissionStatus::Denied:
        qDebug() << "Error Bluetooth permissions not granted!";
        return;
    case Qt::PermissionStatus::Granted:
        break; // proceed to search
    }
    //! [permissions]
#endif // QT_CONFIG(permissions)

    //QAndroidJniObject activity = QtAndroid::androidActivity();
    //auto activity = QJniObject(QNativeInterface::QAndroidApplication::context());
    QJniObject activity = QNativeInterface::QAndroidApplication::context();
    QJniObject::callStaticMethod<void>("org/qtproject/example/JavaScriptCppTest",
                                       "startScan",
                                       "(Landroid/app/Activity;)V",
                                       activity.object());

    QTimer::singleShot(30000, [this]() {
        // Code to execute after 2 minutes (120,000 milliseconds)
        qWarning() << "1 minutes have passed!";
        stopBleScan();
    });

    // QTimer::singleShot(60000, [this]() {
    //     // Code to execute after 2 minutes (120,000 milliseconds)
    //     qWarning() << "Calling Disconnect method";
    //     disconnectBleDevice();
    // });
}

void cppJavaClass::stopBleScan()
{
    QJniObject::callStaticMethod<void>("org/qtproject/example/JavaScriptCppTest", "stopScan", "()V");
}

// void cppJavaClass::disconnectBleDevice()
// {
//     QJniObject::callStaticMethod<void>("org/qtproject/example/JavaScriptCppTest",
//                                        "disconnectDevice",
//                                        "()V");
// }

// Native method implementation
void cppJavaClass::onDeviceFound(JNIEnv *env, jobject thiz, jstring deviceName, jstring deviceMac)
{
    Q_UNUSED(env);
    Q_UNUSED(thiz);

    QString qDeviceName = QJniObject(deviceName).toString();
    QString qDeviceMac = QJniObject(deviceMac).toString();
    qWarning() << "Device found: " << qDeviceName << " - " << qDeviceMac;
}

// JNI OnLoad function to register native methods
extern "C" jint JNI_OnLoad(JavaVM *vm, void *reserved)
{
    Q_UNUSED(reserved);
    JNIEnv *env;
    if (vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return -1;
    }

    jclass clazz = env->FindClass("org/qtproject/example/JavaScriptCppTest");
    if (clazz == nullptr) {
        return -1;
    }

    static const JNINativeMethod methods[] = {
        {"onDeviceFound",
         "(Ljava/lang/String;Ljava/lang/String;)V",
         reinterpret_cast<void *>(cppJavaClass::onDeviceFound)},
    };

    if (env->RegisterNatives(clazz, methods, sizeof(methods) / sizeof(methods[0])) != JNI_OK) {
        return -1;
    }

    return JNI_VERSION_1_6;
}
