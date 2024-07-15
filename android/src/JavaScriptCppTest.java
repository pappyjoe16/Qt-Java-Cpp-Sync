package org.qtproject.example;

//import com.google.gson.Gson;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.Map;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import android.app.Activity;
import android.content.Context;
import android.bluetooth.BluetoothDevice;
import com.onecoder.fitblekit.API.Boxing.FBKApiBoxing;
import com.onecoder.fitblekit.API.Boxing.FBKApiBoxingCallBack;
import com.onecoder.fitblekit.API.ScanDevices.FBKApiScan;
import com.onecoder.fitblekit.API.Base.FBKApiBsaeMethod;
import com.onecoder.fitblekit.API.ScanDevices.FBKApiScanCallBack;
import com.onecoder.fitblekit.Ble.FBKBleDevice.FBKBleDevice;
import com.onecoder.fitblekit.Ble.FBKBleDevice.FBKBleDeviceStatus;
import com.onecoder.fitblekit.API.Base.FBKBleBaseInfo;
import com.onecoder.fitblekit.Protocol.Boxing.BoxingAxisType;
import com.onecoder.fitblekit.Protocol.Boxing.FBKBoxingAxis;
import com.onecoder.fitblekit.Protocol.Boxing.FBKBoxingSet;
import com.onecoder.fitblekit.Tools.FBKSpliceBle;

public class JavaScriptCppTest {

    private static FBKApiScan bleScanner = new FBKApiScan(); // Initialize here
    //private static FBKApiBoxing bleDeviceApi = new FBKApiBoxing(); // Initialize here

    private static Set<String> seenDevices = new HashSet<>();

    // Native method declaration
    private static native void onDeviceFound(String deviceName, String deviceMac);

    public JavaScriptCppTest() {
       //bleScanner = new FBKApiScan();
    }

    public static String getJavaMessage() {
        System.out.println("Output from java: Hello, Yemi Omo Boda Fagbohun!!! ");
        //stopScan();
        return "Hello, Yemi Omo Boda Fagbohun!!!";

    }

    public static void startScan(Activity activity) {

        bleScanner.setScanRssi(-100);
        bleScanner.setRealTimeMode(false);
        bleScanner.setAddSystemDevice(false);

        seenDevices.clear(); // Clear the set of seen devices at the start of a new scan

        bleScanner.startScan(activity, new FBKApiScanCallBack() {
           @Override
           public void bleScanResult(List<FBKBleDevice> deviceArray, FBKApiScan apiScan) {
               for (FBKBleDevice device : deviceArray) {
                   String deviceName = device.getDeviceName();
                   String deviceMac = device.getMacAddress();
                    if ((deviceName != null && (deviceName.startsWith("BX100-L") || deviceName.startsWith("BX100-R"))) &&
                        !seenDevices.contains(deviceMac)) {
                        System.out.println("Device found: " + deviceName + " - " + deviceMac);
                        seenDevices.add(deviceMac);

                        // Call native method to pass device info to C++
                        onDeviceFound(deviceName, deviceMac);
                        connectToDevice(deviceMac, activity);
                    }
               }

           }

           @Override
           public void bleScanAvailable(Boolean isAvailable, FBKApiScan apiScan) {
               if (isAvailable) {
                   System.out.println("BLE scan is available.");
               } else {
                   System.out.println("BLE scan is not available.");
               }
           }
        });
    }

    public static void stopScan() {
        System.out.println("Stop scanning method called.");
        bleScanner.stopScan();
        seenDevices.clear();

    }

    private static void connectToDevice(String macAdd, Activity activity) {
        FBKApiBoxing bleDeviceApi = new FBKApiBoxing(activity, new FBKApiBoxingCallBack() {
            @Override
            public void bleConnectError(String error, FBKApiBsaeMethod apiBsaeMethod) {
                // Handle connection error here
                System.out.println("Connection Error: " + error);
            }

            @Override
            public void bleConnectStatus(FBKBleDeviceStatus connectStatus, FBKApiBsaeMethod apiBsaeMethod) {
                // Handle connection status here
                System.out.println("Connection Status: " + connectStatus);

            }
            @Override
            public void realtimeAxis(List<FBKBoxingAxis> axisData, int dataType, int modeType, FBKApiBoxing apiBoxing) {
                // Handle real-time boxing axis data here
                System.out.println("Real-time Axis Data: " + axisData);
            }
            @Override
            public void boxingAxisSwitchResult(boolean isSuccess, FBKApiBoxing apiBoxing) {
                // Handle boxing axis switch result here
                System.out.println("Boxing Axis Switch Result: " + isSuccess);
            }
            @Override
            public void boxingRecord(Object recordData, FBKApiBoxing apiBoxing) {
                // Handle boxing record data here
                System.out.println("Boxing Record Data: " + recordData);
            }
            @Override
             public void realTimeBoxing(Object data, FBKApiBoxing apiBoxing) {
                // Handle real-time boxing data here
                System.out.println("Real-time Boxing Data: " + data);
            }
            @Override
            public void bleConnectStatusLog(String infoString, FBKApiBsaeMethod apiBsaeMethod) {

            }

            @Override
            public void batteryPower(final int power, FBKApiBsaeMethod apiBsaeMethod) {

                System.out.println("Read Battery Power"+"   ("+String.valueOf(power)+"%"+")");

            }

            @Override
            public void protocolVersion(String version, FBKApiBsaeMethod apiBsaeMethod) {
            }

            @Override
            public void firmwareVersion(String version, FBKApiBsaeMethod apiBsaeMethod) {

            }

            @Override
            public void hardwareVersion(String version, FBKApiBsaeMethod apiBsaeMethod) {

            }

            @Override
            public void softwareVersion(String version, FBKApiBsaeMethod apiBsaeMethod) {

            }

            @Override
            public void privateVersion(Map<String, String> versionMap, FBKApiBsaeMethod apiBsaeMethod) {

            }

            @Override
            public void privateMacAddress(Map<String, String> macMap, FBKApiBsaeMethod apiBsaeMethod) {

            }


            @Override
            public void bleConnectInfo(String infoString, FBKApiBsaeMethod apiBsaeMethod) {

            }

            @Override
            public void deviceSystemData(byte[] systemData, FBKApiBsaeMethod apiBsaeMethod) {

            }

            @Override
            public void deviceModelString(String modelString, FBKApiBsaeMethod apiBsaeMethod) {

            }

            @Override
            public void deviceSerialNumber(String serialNumber, FBKApiBsaeMethod apiBsaeMethod) {

            }

            @Override
            public void deviceManufacturerName(String manufacturerName, FBKApiBsaeMethod apiBsaeMethod) {

            }

            @Override
            public void deviceBaseInfo(FBKBleBaseInfo baseInfo, FBKApiBsaeMethod apiBsaeMethod) {

               System.out.println("macAddress: "+baseInfo.getDeviceMac());
            }
        });

        bleDeviceApi.registerBleListenerReceiver();
        bleDeviceApi.connectBluetooth(macAdd);

        /* Schedule the disconnection after 3 minutes
               Handler handler1 = new Handler(Looper.getMainLooper());
               handler1.postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       System.out.println("Here is Boxing record:");
                       bleDeviceApi.getBoxingRecord();

                   }
               }, 1 * 60 * 1000); // 3 minutes in millisecon
           */
           // Schedule the disconnection after 3 minutes
                  Handler handler = new Handler(Looper.getMainLooper());
                  handler.postDelayed(new Runnable() {
                      @Override
                      public void run() {
                          bleDeviceApi.disconnectBle();
                          bleDeviceApi.unregisterBleListenerReceiver();
                          System.out.println("All Device Disconnected.");


                      }
                  }, 3 * 60 * 1000); // 3 minutes in millisecon
    }


    }



