package com.mediatek.elian;

/**
 */
public class ElianNative
{
    static 
    {
        System.loadLibrary("elianjni");
    }

    public native static int GetProtoVersion();

    public native static int GetLibVersion();
    
	/*
     * Init SmartConnection
     */
    public native static int InitSmartConnection(String Target, int sendV1, int sendV4);

    /**
     * Start SmartConnection with Home AP
     *
     * @SSID : SSID of Home AP
     * @Password : Password of Home AP
     * @Auth : Auth of Home AP
     */
    public native static int StartSmartConnection(String SSID, String Password, String Custom);

    /**
     * Stop SmartConnection by user
     *
     */

    public native static int StopSmartConnection();

}
