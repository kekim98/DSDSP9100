package com.dignsys.dsdsp.dsdsp_9100.db.entity;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by bawoori on 17. 11. 16.
 */

@Entity(tableName = "config_info")
public class ConfigEntity  {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String configVersion;        // Config version.

    /* General Setting */
    private String firmwareVersion;    // Firmware version.
    private String deviceId;            // Device ID
    private int DSPMode;                // DSP Mode. 0: Standard Type, 1: private interactive Mode

    /* Video/Audio Setting */
    private int backlight;                // Back-light value (for only LVDS)
    private int TVMode;                // TV mode. 0:NTSC,
    private int videoOutput;            // Video output. 0: HDMI 1024x768...
    private int audioMode;                // Audio mode. 0: Stereo, 1: Surround
    private int audioVolume;            // Audio volume.

    /* Server Setting */
    private int serverMode;            // Server mode. 0: Stand-alone, 1: private internet
    private String serverAddress;    // Server address.
    private String serverFolder;        // Server folder.
    private String serverPort;            // Server port.
    private String serverId;            // Server ID.
    private String serverPwd;        // Server password.
    private int serverSyncInterval;    // Server private interval for sync.

    /* LAN Setting */
    private int useLAN;                // 0: Use, 1: Not used
    private int useLAN_DHCP;            // 0: Use, 1: Not used
    private int LAN_AutoDNS;            // 0: Auto, 1: Fix
    private String LAN_IP;            // IP for LAN
    private String LAN_Subnet;        // Sub-net mask for LAN
    private String LAN_Gateway;        // Gateway for LAN
    private String LAN_DNS;            // DNS for LAN

    /* Wifi Setting */
    private int useWifi;                // 0: Use, 1: Not used
    private int useWifi_DHCP;            // 0: Use, 1: Not used
    private int Wifi_AutoDNS;            // 0: Auto, 1: Fix
    private int Wifi_AuthMode;            // AuthMode. 0:None, WEP, WPAPSK/TKIP, WPAPSK/AES, WPA2/TKIP, WPA2/AES, WPA2PSK/TKIP, WPA2PSK/AES
    private String Wifi_IP;            // IP for Wifi
    private String Wifi_Subnet;        // Sub-net mask for Wifi
    private String Wifi_Gateway;        // Gateway for Wifi
    private String Wifi_DNS;            // DNS for LAN
    private String Wifi_ESSID;        // ESSID for Wifi
    private String Wifi_PWD;            // Password of ESSID

    /* Screen Setting */
    private int ImageChangeEffect;        // Image change effect.
    private int ImageChangeInterval;    // Image change private interval.
    private int UseAutoResizeMovie;    // 0: Not used, 1: Use
    private int UseAutoResizeImage;    // 0: Not used, 1: Use
    private int CapMode;                // Caption mode.
    private int CapPosition;            // Caption position.
    private int CapColor;                // Caption color.
    private int CapBackColor;            // Caption background color.
    private int CapSpeed;                // Caption speed.
    private int CapFont;                // Caption font.
    private int CapSize;                // Caption font size.
    private int RSSMode;                // RSS mode.
    private int RSSUpdateInterval;        // RSS update private interval.
    private String RSSAddress;            // RSS address;

    /* Time Setting */
    private int NTPServer;                // NTP server address. 0: Not used.
    private String Timezone;                // Time zone
    private int TimeDisplayPosition;    // Time display position. 0: Not used.
    private int TimeDisplayColor;        // Time display color
    private int TimeDisplayBackColor;    // Time display background color
    private int TimeDisplayFont;        // Time display font
    private int TimeDisplayFontSize;    // Time display font size
    private int AutoOnOffMode;            // Auto on/off mode.
    private String OnTime;            // On time.
    private String OffTime;            // Off time.

    /* Advanced Setting */
    private int IA_Mode;                // private interactive mode.
    private int IA_ReqMeans;            // private interactive request means.
    private int IA_SerialBaud;            // private interactive serial baud-rate.
    private int IA_SerialData;            // private interactive serial data bit.
    private int IA_SerialParity;        // private interactive serial parity bit.
    private int IA_SerialStop;            // private interactive serial stop bit.
    private String IA_NetIP;            // private interactive network ip address.
    private int IA_NetPort;            // private interactive network port number.
    private int MS_Mode;                // Multi-sync mode.
    private String MS_ServerIP;        // Multi-sync server ip.
    private int MS_UserCount;            // Multi-sync user count.

    /* Optional Setting */
    private int DTV_Modulation;        // DTV Modulation means.
    private int DTV_AutoSearchRange;    // DTV Auto search range.
    private String DTV_Channel;        // DTV Channel.
    private int UseDefaultContents;    // Use Default Contents when SD empty.
    private int UseSDLogSave;            // Use log save in SD
    private int LogFileSaveDay;        // Log file save day.
    private int EnableLiveUpload;
    private int EnableLogUpload;
    private int EnableCaptureUpload;
    private int IntervalLive;
    private int IntervalLog;
    private int IntervalCapture;

    private int CaptureScale;


    private int Language;                // Language.

/*

    Append by bawoori

*/
    private int offDay;
    private String offWeek;
    private int rebootTime;
    private int downloadStartTime;
    private int downloadEndTime;
    private int rssCaptionMode;
    private int rssContinueTime;

    public int getRssCaptionMode() {
        return rssCaptionMode;
    }

    public void setRssCaptionMode(int rssCaptionMode) {
        this.rssCaptionMode = rssCaptionMode;
    }

    public int getRssContinueTime() {
        return rssContinueTime;
    }

    public void setRssContinueTime(int rssContinueTime) {
        this.rssContinueTime = rssContinueTime;
    }

    public int getDownloadStartTime() {
        return downloadStartTime;
    }

    public void setDownloadStartTime(int downloadStartTime) {
        this.downloadStartTime = downloadStartTime;
    }

    public int getDownloadEndTime() {
        return downloadEndTime;
    }

    public void setDownloadEndTime(int downloadEndTime) {
        this.downloadEndTime = downloadEndTime;
    }





    public int getOffDay() {
        return offDay;
    }

    public void setOffDay(int offDay) {
        this.offDay = offDay;
    }

    public String getOffWeek() {
        return offWeek;
    }

    public void setOffWeek(String offWeek) {
        this.offWeek = offWeek;
    }

    public int getRebootTime() {
        return rebootTime;
    }

    public void setRebootTime(int rebootTime) {
        this.rebootTime = rebootTime;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(String configVersion) {
        this.configVersion = configVersion;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getDSPMode() {
        return DSPMode;
    }

    public void setDSPMode(int DSPMode) {
        this.DSPMode = DSPMode;
    }

    public int getBacklight() {
        return backlight;
    }

    public void setBacklight(int backlight) {
        this.backlight = backlight;
    }

    public int getTVMode() {
        return TVMode;
    }

    public void setTVMode(int TVMode) {
        this.TVMode = TVMode;
    }

    public int getVideoOutput() {
        return videoOutput;
    }

    public void setVideoOutput(int videoOutput) {
        this.videoOutput = videoOutput;
    }

    public int getAudioMode() {
        return audioMode;
    }

    public void setAudioMode(int audioMode) {
        this.audioMode = audioMode;
    }

    public int getAudioVolume() {
        return audioVolume;
    }

    public void setAudioVolume(int audioVolume) {
        this.audioVolume = audioVolume;
    }

    public int getServerMode() {
        return serverMode;
    }

    public void setServerMode(int serverMode) {
        this.serverMode = serverMode;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getServerFolder() {
        return serverFolder;
    }

    public void setServerFolder(String serverFolder) {
        this.serverFolder = serverFolder;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getServerPwd() {
        return serverPwd;
    }

    public void setServerPwd(String serverPwd) {
        this.serverPwd = serverPwd;
    }

    public int getServerSyncInterval() {
        return serverSyncInterval;
    }

    public void setServerSyncInterval(int serverSyncInterval) {
        this.serverSyncInterval = serverSyncInterval;
    }

    public int getUseLAN() {
        return useLAN;
    }

    public void setUseLAN(int useLAN) {
        this.useLAN = useLAN;
    }

    public int getUseLAN_DHCP() {
        return useLAN_DHCP;
    }

    public void setUseLAN_DHCP(int useLAN_DHCP) {
        this.useLAN_DHCP = useLAN_DHCP;
    }

    public int getLAN_AutoDNS() {
        return LAN_AutoDNS;
    }

    public void setLAN_AutoDNS(int LAN_AutoDNS) {
        this.LAN_AutoDNS = LAN_AutoDNS;
    }

    public String getLAN_IP() {
        return LAN_IP;
    }

    public void setLAN_IP(String LAN_IP) {
        this.LAN_IP = LAN_IP;
    }

    public String getLAN_Subnet() {
        return LAN_Subnet;
    }

    public void setLAN_Subnet(String LAN_Subnet) {
        this.LAN_Subnet = LAN_Subnet;
    }

    public String getLAN_Gateway() {
        return LAN_Gateway;
    }

    public void setLAN_Gateway(String LAN_Gateway) {
        this.LAN_Gateway = LAN_Gateway;
    }

    public String getLAN_DNS() {
        return LAN_DNS;
    }

    public void setLAN_DNS(String LAN_DNS) {
        this.LAN_DNS = LAN_DNS;
    }

    public int getUseWifi() {
        return useWifi;
    }

    public void setUseWifi(int useWifi) {
        this.useWifi = useWifi;
    }

    public int getUseWifi_DHCP() {
        return useWifi_DHCP;
    }

    public void setUseWifi_DHCP(int useWifi_DHCP) {
        this.useWifi_DHCP = useWifi_DHCP;
    }

    public int getWifi_AutoDNS() {
        return Wifi_AutoDNS;
    }

    public void setWifi_AutoDNS(int wifi_AutoDNS) {
        Wifi_AutoDNS = wifi_AutoDNS;
    }

    public int getWifi_AuthMode() {
        return Wifi_AuthMode;
    }

    public void setWifi_AuthMode(int wifi_AuthMode) {
        Wifi_AuthMode = wifi_AuthMode;
    }

    public String getWifi_IP() {
        return Wifi_IP;
    }

    public void setWifi_IP(String wifi_IP) {
        Wifi_IP = wifi_IP;
    }

    public String getWifi_Subnet() {
        return Wifi_Subnet;
    }

    public void setWifi_Subnet(String wifi_Subnet) {
        Wifi_Subnet = wifi_Subnet;
    }

    public String getWifi_Gateway() {
        return Wifi_Gateway;
    }

    public void setWifi_Gateway(String wifi_Gateway) {
        Wifi_Gateway = wifi_Gateway;
    }

    public String getWifi_DNS() {
        return Wifi_DNS;
    }

    public void setWifi_DNS(String wifi_DNS) {
        Wifi_DNS = wifi_DNS;
    }

    public String getWifi_ESSID() {
        return Wifi_ESSID;
    }

    public void setWifi_ESSID(String wifi_ESSID) {
        Wifi_ESSID = wifi_ESSID;
    }

    public String getWifi_PWD() {
        return Wifi_PWD;
    }

    public void setWifi_PWD(String wifi_PWD) {
        Wifi_PWD = wifi_PWD;
    }

    public int getImageChangeEffect() {
        return ImageChangeEffect;
    }

    public void setImageChangeEffect(int imageChangeEffect) {
        ImageChangeEffect = imageChangeEffect;
    }

    public int getImageChangeInterval() {
        return ImageChangeInterval;
    }

    public void setImageChangeInterval(int imageChangeInterval) {
        ImageChangeInterval = imageChangeInterval;
    }

    public int getUseAutoResizeMovie() {
        return UseAutoResizeMovie;
    }

    public void setUseAutoResizeMovie(int useAutoResizeMovie) {
        UseAutoResizeMovie = useAutoResizeMovie;
    }

    public int getUseAutoResizeImage() {
        return UseAutoResizeImage;
    }

    public void setUseAutoResizeImage(int useAutoResizeImage) {
        UseAutoResizeImage = useAutoResizeImage;
    }

    public int getCapMode() {
        return CapMode;
    }

    public void setCapMode(int capMode) {
        CapMode = capMode;
    }

    public int getCapPosition() {
        return CapPosition;
    }

    public void setCapPosition(int capPosition) {
        CapPosition = capPosition;
    }

    public int getCapColor() {
        return CapColor;
    }

    public void setCapColor(int capColor) {
        CapColor = capColor;
    }

    public int getCapBackColor() {
        return CapBackColor;
    }

    public void setCapBackColor(int capBackColor) {
        CapBackColor = capBackColor;
    }

    public int getCapSpeed() {
        return CapSpeed;
    }

    public void setCapSpeed(int capSpeed) {
        CapSpeed = capSpeed;
    }

    public int getCapFont() {
        return CapFont;
    }

    public void setCapFont(int capFont) {
        CapFont = capFont;
    }

    public int getCapSize() {
        return CapSize;
    }

    public void setCapSize(int capSize) {
        CapSize = capSize;
    }

    public int getRSSMode() {
        return RSSMode;
    }

    public void setRSSMode(int RSSMode) {
        this.RSSMode = RSSMode;
    }

    public int getRSSUpdateInterval() {
        return RSSUpdateInterval;
    }

    public void setRSSUpdateInterval(int RSSUpdateInterval) {
        this.RSSUpdateInterval = RSSUpdateInterval;
    }

    public String getRSSAddress() {
        return RSSAddress;
    }

    public void setRSSAddress(String RSSAddress) {
        this.RSSAddress = RSSAddress;
    }

    public int getNTPServer() {
        return NTPServer;
    }

    public void setNTPServer(int NTPServer) {
        this.NTPServer = NTPServer;
    }

    public String getTimezone() {
        return Timezone;
    }

    public void setTimezone(String timezone) {
        Timezone = timezone;
    }

    public int getTimeDisplayPosition() {
        return TimeDisplayPosition;
    }

    public void setTimeDisplayPosition(int timeDisplayPosition) {
        TimeDisplayPosition = timeDisplayPosition;
    }

    public int getTimeDisplayColor() {
        return TimeDisplayColor;
    }

    public void setTimeDisplayColor(int timeDisplayColor) {
        TimeDisplayColor = timeDisplayColor;
    }

    public int getTimeDisplayBackColor() {
        return TimeDisplayBackColor;
    }

    public void setTimeDisplayBackColor(int timeDisplayBackColor) {
        TimeDisplayBackColor = timeDisplayBackColor;
    }

    public int getTimeDisplayFont() {
        return TimeDisplayFont;
    }

    public void setTimeDisplayFont(int timeDisplayFont) {
        TimeDisplayFont = timeDisplayFont;
    }

    public int getTimeDisplayFontSize() {
        return TimeDisplayFontSize;
    }

    public void setTimeDisplayFontSize(int timeDisplayFontSize) {
        TimeDisplayFontSize = timeDisplayFontSize;
    }

    public int getAutoOnOffMode() {
        return AutoOnOffMode;
    }

    public void setAutoOnOffMode(int autoOnOffMode) {
        AutoOnOffMode = autoOnOffMode;
    }

    public String getOnTime() {
        return OnTime;
    }

    public void setOnTime(String onTime) {
        OnTime = onTime;
    }

    public String getOffTime() {
        return OffTime;
    }

    public void setOffTime(String offTime) {
        OffTime = offTime;
    }

    public int getIA_Mode() {
        return IA_Mode;
    }

    public void setIA_Mode(int IA_Mode) {
        this.IA_Mode = IA_Mode;
    }

    public int getIA_ReqMeans() {
        return IA_ReqMeans;
    }

    public void setIA_ReqMeans(int IA_ReqMeans) {
        this.IA_ReqMeans = IA_ReqMeans;
    }

    public int getIA_SerialBaud() {
        return IA_SerialBaud;
    }

    public void setIA_SerialBaud(int IA_SerialBaud) {
        this.IA_SerialBaud = IA_SerialBaud;
    }

    public int getIA_SerialData() {
        return IA_SerialData;
    }

    public void setIA_SerialData(int IA_SerialData) {
        this.IA_SerialData = IA_SerialData;
    }

    public int getIA_SerialParity() {
        return IA_SerialParity;
    }

    public void setIA_SerialParity(int IA_SerialParity) {
        this.IA_SerialParity = IA_SerialParity;
    }

    public int getIA_SerialStop() {
        return IA_SerialStop;
    }

    public void setIA_SerialStop(int IA_SerialStop) {
        this.IA_SerialStop = IA_SerialStop;
    }

    public String getIA_NetIP() {
        return IA_NetIP;
    }

    public void setIA_NetIP(String IA_NetIP) {
        this.IA_NetIP = IA_NetIP;
    }

    public int getIA_NetPort() {
        return IA_NetPort;
    }

    public void setIA_NetPort(int IA_NetPort) {
        this.IA_NetPort = IA_NetPort;
    }

    public int getMS_Mode() {
        return MS_Mode;
    }

    public void setMS_Mode(int MS_Mode) {
        this.MS_Mode = MS_Mode;
    }

    public String getMS_ServerIP() {
        return MS_ServerIP;
    }

    public void setMS_ServerIP(String MS_ServerIP) {
        this.MS_ServerIP = MS_ServerIP;
    }

    public int getMS_UserCount() {
        return MS_UserCount;
    }

    public void setMS_UserCount(int MS_UserCount) {
        this.MS_UserCount = MS_UserCount;
    }

    public int getDTV_Modulation() {
        return DTV_Modulation;
    }

    public void setDTV_Modulation(int DTV_Modulation) {
        this.DTV_Modulation = DTV_Modulation;
    }

    public int getDTV_AutoSearchRange() {
        return DTV_AutoSearchRange;
    }

    public void setDTV_AutoSearchRange(int DTV_AutoSearchRange) {
        this.DTV_AutoSearchRange = DTV_AutoSearchRange;
    }

    public String getDTV_Channel() {
        return DTV_Channel;
    }

    public void setDTV_Channel(String DTV_Channel) {
        this.DTV_Channel = DTV_Channel;
    }

    public int getUseDefaultContents() {
        return UseDefaultContents;
    }

    public void setUseDefaultContents(int useDefaultContents) {
        UseDefaultContents = useDefaultContents;
    }

    public int getUseSDLogSave() {
        return UseSDLogSave;
    }

    public void setUseSDLogSave(int useSDLogSave) {
        UseSDLogSave = useSDLogSave;
    }

    public int getLogFileSaveDay() {
        return LogFileSaveDay;
    }

    public void setLogFileSaveDay(int logFileSaveDay) {
        LogFileSaveDay = logFileSaveDay;
    }

    public int getEnableLiveUpload() {
        return EnableLiveUpload;
    }

    public void setEnableLiveUpload(int enableLiveUpload) {
        EnableLiveUpload = enableLiveUpload;
    }

    public int getEnableLogUpload() {
        return EnableLogUpload;
    }

    public void setEnableLogUpload(int enableLogUpload) {
        EnableLogUpload = enableLogUpload;
    }

    public int getEnableCaptureUpload() {
        return EnableCaptureUpload;
    }

    public void setEnableCaptureUpload(int enableCaptureUpload) {
        EnableCaptureUpload = enableCaptureUpload;
    }

    public int getIntervalLive() {
        return IntervalLive;
    }

    public void setIntervalLive(int intervalLive) {
        IntervalLive = intervalLive;
    }

    public int getIntervalLog() {
        return IntervalLog;
    }

    public void setIntervalLog(int intervalLog) {
        IntervalLog = intervalLog;
    }

    public int getIntervalCapture() {
        return IntervalCapture;
    }

    public void setIntervalCapture(int intervalCapture) {
        IntervalCapture = intervalCapture;
    }

    public int getCaptureScale() {
        return CaptureScale;
    }

    public void setCaptureScale(int captureScale) {
        CaptureScale = captureScale;
    }

    public int getLanguage() {
        return Language;
    }

    public void setLanguage(int language) {
        Language = language;
    }
}
