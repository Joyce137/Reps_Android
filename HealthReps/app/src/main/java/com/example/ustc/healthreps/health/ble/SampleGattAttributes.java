/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.ustc.healthreps.health.ble;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Pattern;

//import com.iwit.bluetoothcommunication.util.encodeUtil;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
public class SampleGattAttributes {
    // 注：下面的UUID是通过连接设备后打印所得
    /**
     * 主服务uuid，gatt服务由BluetoothGattService类代表
     */
    public static boolean checkflag=false;
    public static boolean flag=false;
    public static boolean connectedState=false;
    public static boolean whichactivityconnect=false;
    public static boolean isinsetactivity=false;
    public static Calendar lastrecall=Calendar.getInstance();
    public static Calendar thisrecall=Calendar.getInstance();
    public static Calendar checkifrecall=Calendar.getInstance();
    public  static int controlbroadcast=0;



    public static String GATT_SERVICE_PRIMARY = "00008000-0000-1000-8000-00805f9b34fb";//服务号
    /**
     * notify特性uuid  暂时理解为对给定characteristic进行异步传输
     */
    public static String CHARACTERISTIC_NOTIFY = "00008001-0000-1000-8000-00805f9b34fb";//主要数据
    /**
     * 主动读取数据的特性uuid
     */
    public static String broadcastperiodRW = "00008002-0000-1000-8000-00805f9b34fb";//两字节的广播周期20~300
    /**
     * 特性句柄由BluetoothGattCharacteristic类代表
     * 发送数据的特性uuid
     */
    public static String WAKEUPRW = "00008003-0000-1000-8000-00805f9b34fb";//2byte每次唤醒起来广播间隔，参数范围为：8000-30000ms，默认为 8000
    public static String BROADCASTLASTRW = "00008004-0000-1000-8000-00805f9b34fb";//2byte单次广播持续时间，参数范围为：100ms-300ms，默认为 200ms
    public static String POWER= "00008005-0000-1000-8000-00805f9b34fb";//1字节电量
    public static String HEARTRATEPERIODRW = "00008006-0000-1000-8000-00805f9b34fb";//2byte设置每天心率采集的次数，参数范围为：1-480 次，默认为 5
    public static String SOSRW = "00008007-0000-1000-8000-00805f9b34fb";//2byte用于设置 SOS 警报的持续时间，参数范围为：1000ms-20000ms，默认为 5000ms,
    public static String TIMEADJUSTRW = "00008008-0000-1000-8000-00805f9b34fb";//蓝牙数据手环连接3S，手机端的时间发送到蓝牙手环进行同步，
                                                                    // 格式为：秒（1Byte）+分(1Byte)+ 时(1Byte)+ 日(1Byte)+月(Byte)+年(2Bytes)
    public static String emitPowerRW = "00008009-0000-1000-8000-00805f9b34fb";//功率，x db=10log（10的x次幂）mw 所以0db为1mw那个log以10为底
    /**
         * @return, service 字段很可能上来时各characteristic都是空的


     */

    public static boolean sendMessage(BluetoothGatt gatt,byte[] message,UUID writeable){
        if(gatt == null || message == null || message.length == 0)return false;
        System.out.println("--------try to write in sample ----- " );
        // 未知uuid的情况下要通过gatt.getServices()循环遍历
        BluetoothGattService service = gatt.getService(UUID.fromString(GATT_SERVICE_PRIMARY));
        if(service == null)return false;

        // 获取writeable特性，未知uuid的情况下要通过service.getCharacteristics()循环遍历
        BluetoothGattCharacteristic characteristic = service.getCharacteristic(writeable);

        if(characteristic == null)return false;

        characteristic.setValue(message);

        characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
        return gatt.writeCharacteristic(characteristic);
    }

    /**
     * notify特性即characteristic.getProperties() == BluetoothGattCharacteristic.PROPERTY_NOTIFY
     * 拿到notify特性，使能notify，（被动）实时接收从机数据
     */
    public static boolean notify(BluetoothGatt gatt,UUID notifieduuid){
        if(gatt == null)return false;

        BluetoothGattService service = gatt.getService(UUID.fromString(GATT_SERVICE_PRIMARY));//

        if(service == null)return false;

        BluetoothGattCharacteristic characteristic = service.getCharacteristic(notifieduuid);//这个可能是写uuid
        characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);//这句话实际写的是descriptor字段
        return gatt.setCharacteristicNotification(characteristic, true);//理解为设立某characteristic，对其字段改变进行通知，连接开始时为空
    }

    /**
     * 拿到readable特性，（主动）读取从机数据
     */
    public static boolean readMessage(BluetoothGatt gatt,UUID uuidcharactername){
        if(gatt == null)return false;

        BluetoothGattService service = gatt.getService(UUID.fromString(GATT_SERVICE_PRIMARY));

        if(service == null)return false;

        BluetoothGattCharacteristic characteristic = service.getCharacteristic(uuidcharactername);

        return gatt.readCharacteristic(characteristic);
    }
    public static void trim(byte[]  p,int k)//int转byte数组写入时会用到
    {
        int i;
        for(i=0;i<=3;i++)
        {
            p[i] = (byte)((k>> 8*(3-i)));
        }

    }

    public static boolean isNumer(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public static int dataGetter(byte input[],int offset,int format)//offset is the  offset of your first byte
    {
        switch (format){//format是指几个byte弄成一个int
            case 1:{return input[offset] & 0xff;//


            }
            case 2:{
                return (input[offset+1]& 0xff)*256+(input[offset]& 0xff);
            }
            case 4:{}//只有解析数据头才用到

        }return  -1;

    }
    /**数据头//UINT几 是INT型占几位的新结构类型
     0x336699DB(4Bytes)+年（2Bytes）+月（1Byte）+日（1Byte）
     +步数（2Bytes）+心率数据（1Bytes）           */
    public static int dataDivider(byte input[],int whichDay,int itemOffset)
    /**这里itemOffset 为方便人为对照吧：4对应年  6对应月  7对应日  8对应步数 10~~19对应心率数据*/
    {
        int baseOffset = (whichDay - 1) * 20;//
        switch (itemOffset) {
            case 4:
               return dataGetter(input, baseOffset + itemOffset, 2);
            case 6:
               return dataGetter(input, baseOffset + itemOffset, 1);
            case 7:
                return dataGetter(input, baseOffset + itemOffset, 1);
            case 8:
                return dataGetter(input, baseOffset + itemOffset, 2);
            case 10:
                return dataGetter(input, baseOffset + itemOffset, 1);
            case 11:
                return dataGetter(input, baseOffset + itemOffset, 1);
            case 12:
                return dataGetter(input, baseOffset + itemOffset, 1);
            case 13:
                return dataGetter(input, baseOffset + itemOffset, 1);
            case 14:
                return  dataGetter(input, baseOffset + itemOffset, 1);
            case 15:
                return  dataGetter(input, baseOffset + itemOffset, 1);
            case 16:
                return  dataGetter(input, baseOffset + itemOffset, 1);
            case 17:
                return dataGetter(input, baseOffset + itemOffset, 1);
            case 18:
                return  dataGetter(input, baseOffset + itemOffset, 1);
            case 19:
                return dataGetter(input, baseOffset + itemOffset, 1);
        }
    return -1;//出错的返回值
    }
    public static String adjustDatafunc(byte[] input){//7byte 秒分时日月 年年
        return  String.valueOf(dataGetter(input, 5, 2))+
        String.valueOf(dataGetter(input, 4, 1))+
                        String.valueOf(dataGetter(input, 3, 1))+
                String.valueOf(dataGetter(input, 2, 1))+
        String.valueOf(dataGetter(input, 1, 1))+
                String.valueOf(dataGetter(input, 0, 1))
        ;

    }


}