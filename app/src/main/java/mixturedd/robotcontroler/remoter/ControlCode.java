package mixturedd.robotcontroler.remoter;

/**
 * ControlCode.java
 * Description :
 * <p>
 * Created by hp on 2016/11/21 20:18.
 * Copyright © 2016 hp. All rights reserved.
 */

public class ControlCode {
    //服务器
    public static String SEVER_IP = "192.168.1.1";
    public static String SEVER_URL = "http://" + SEVER_IP;
    public static int SEVER_PORT = 2001;
    //Socket方式
    public static String ORDER_SAE = "FF";
    //  电机
    public static String ORDER_MOVE = "00";
    public static String ORDER_MOVE_FORWARD = "01";
    public static String ORDER_MOVE_BACKWARD = "02";
    public static String ORDER_MOVE_RIGHT = "04";
    public static String ORDER_MOVE_LEFT = "03";
    public static String ORDER_MOVE_STOP = "0000";
    public static String ORDER_MOVE_SPEED_1 = "01";
    public static String ORDER_MOVE_SPEED_2 = "02";
    //  云台
    public static String ORDER_HEAD = "01";
    public static String ORDER_HEAD_VERTICAL = "01";
    public static String ORDER_HEAD_HORIZONTAL = "02";
    //传感器
    public static String ORDER_SENSOR = "02";
    public static String ORDER_SENSOR_DISTANCE = "01";
    public static String ORDER_SENSOR_TEMPERATURE = "02";
    public static String ORDER_SENSOR_GAS = "03";
    public static String ORDER_SENSOR_END = "FF";
    public static String ORDER_SENSOR_ALL = "FFFF";
    //机械手
    public static String ORDER_HAND = "03";
    public static String ORDER_HAND_ONE = "01";
    public static String ORDER_HAND_TOW = "02";
    public static String ORDER_HAND_THREE = "03";
    public static String ORDER_HAND_FOUR = "04";
    public static String ORDER_HAND_FIVE = "05";
    public static String ORDER_HAND_INITIALIZATION = ORDER_SAE + ORDER_HAND + "0000" + ORDER_SAE;
    //视频
    public static int VIDEO_PORT = 8080;
    public static final String VIDEO_MODEL_STREAM = "stream";
    public static final String VIDEO_MODEL_SNAPSHOT = "snapshot";
}
