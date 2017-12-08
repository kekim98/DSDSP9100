package com.dignsys.dsdsp.dsdsp_9100;

public class Definer {

	
	/*
	 * Common.
	 */
	public static final int	SCHEDULE_PERIOD = 2*1000; //2s
	public static final int DEF_FALSE					= 0;
	public static final int DEF_TRUE					= 1;	
	public static final int DEF_USE						= 0;
	public static final int DEF_NOT_USED				= 1;
	
	public static final int DEF_PANEL_TYPE_LANDSCAPE	= 1;
	public static final int DEF_PANEL_TYPE_PORTRAIT		= 2;
	
	public static final int DEF_THREAD_PRIORITY_MIN 	= 1;
	public static final int DEF_THREAD_PRIORITY_MAX 	= 10;
	
	public static final String DEF_SD_PATH				= "/mnt/external_sd";
	public static final String DEF_CAPTURE_PATH			= "/mnt/external_sd/";
	public static final String DEF_FILENAME_PLAYLIST	= "playlist.txt";
	public static final String DEF_FILENAME_FORMAT		= "format.txt";
	public static final String DEF_FILENAME_DSPCONFIG	= "dspconfig.txt";
	public static final String DEF_FILENAME_COMMAND		= "command.txt";
	public static final String DEF_FILENAME_CONTROL		= "control.txt";
	
	/*
	 * Company Code.
	 */
	public static final String DEF_COMPANY_ID_CSGD = "001";    // Cheonan Sports Gym for the Disabled
	public static final String DEF_BOARD_ID = "301";
	public static final String DEF_MODEL_ID = "001";
	public static final String DEF_COMPANY_ID = "001";
	public static final String DEF_VERSION = "032";
	public static final String DEF_CONFIG_VERSION=	"001";

	public static final String DEF_PRODUCT_NAME	="ds7000";
	
	/*
	 * Activity Request Code.
	 */
	public static final int DEF_ACTIVITY_REQUEST_CODE_PLAYER		= 0x01;
	public static final int DEF_ACTIVITY_REQUEST_CODE_CONFIG		= 0x02;
	
	/*
	 */
	/*public static final String DEF_IDN_FINISH_CODE							= "FC";		// Intent Data Name. Activity finish code.
	
	public static final int 	DEF_ID_FINISH_CODE_PRESSED_MENU_KEY			= 1;		// Intent Data. Pressed Menu key.
	public static final int 	DEF_ID_FINISH_CODE_RECEIVED_FINISH_MSG		= 2;		// Intent Data. Received finish message.
	public static final int 	DEF_ID_FINISH_CODE_RECEIVED_CS_MSG			= 3;		// Intent Data. Received changed schedule message.
	public static final int 	DEF_ID_FINISH_CODE_END_SCENE				= 4;		// Intent Data. End scene.
	public static final int 	DEF_ID_FINISH_CODE_UMS_COMMAND				= 5;		// Intent Data. command.txt in UMS.
	public static final int 	DEF_ID_FINISH_CODE_UMS_SYNC					= 6;		// Intent Data. confirm sync UMS.
	public static final int 	DEF_ID_FINISH_CODE_PRESSED_BACK_KEY			= 7;		// Intent Data. Pressed Back key.
	public static final int 	DEF_ID_FINISH_CODE_RECEIVED_CD_COMMAND_MSG 	= 8;		// Intent Data. Received completed download commnad.txt message.
	public static final int 	DEF_ID_FINISH_CODE_RECEIVED_CD_CONFIG_MSG 	= 9;		// Intent Data. Received completed download dspconfig.txt message.
	public static final int 	DEF_ID_FINISH_CODE_RECEIVED_CD_SCHEDULE_MSG = 10;		// Intent Data. Received completed download playlist.txt message.*/
	
	
	/*
	 * Key code of Remote controller.
	 */
	public static final int DEF_KEY_CODE_MENU			= 0x52;
	
	/*
	 * Service Handler Message ID.
	 */
	/*public static final int DEF_SH_MSG_ID_START_DOWNLOAD				= 1;
	public static final int DEF_SH_MSG_ID_DOWNLOADING					= 2;
	public static final int DEF_SH_MSG_ID_COMPLETED_DOWNLOAD_COMMAND	= 3;
	public static final int DEF_SH_MSG_ID_COMPLETED_DOWNLOAD_CONFIG		= 4;
	public static final int DEF_SH_MSG_ID_COMPLETED_DOWNLOAD_SCHEDULE	= 5;
	public static final int DEF_SH_MSG_ID_COMPLETED_DOWNLOAD_RSS		= 6;
	public static final int DEF_SH_MSG_ID_COMPLETED_DOWNLOAD_CONTROL	= 7;*/
	
	/*
	 * WEB Interface path.
	 */
	public static final String DEF_WEB_IF_LIVE_PATH			= "process/interface/if_live.php";
	public static final String DEF_WEB_IF_LOG_PATH			= "process/interface/if_log.php";
	public static final String DEF_WEB_IF_STATUS_LOG_PATH	= "process/interface/if_log_status.php";
	public static final String DEF_WEB_IF_CAPTURE_PATH		= "process/interface/if_capture.php";
	public static final String DEF_WEB_IF_CONTROL_PATH		= "process/interface/if_control.php";	
	public static final String DEF_WEB_CONTENTS_URL			= "/contents/_weather_";

	/*
	 * Confirm type
	 */
	public static final int DEF_CONFIRM_ID_DELETE_ALL		= 1;
	public static final int DEF_CONFIRM_ID_USB_SYNC			= 2;
	public static final int DEF_CONFIRM_ID_USB_COPY			= 3;
	public static final int DEF_CONFIRM_ID_RESET_CONFIG		= 4;
	public static final int DEF_CONFIRM_ID_FORMAT_SDCARD	= 5;
	
	/*
	 * UMS Operation.
	 */
	public static final int DEF_USM_SYNC_OP_ID_SYNC			= 1;
	public static final int DEF_USM_SYNC_OP_ID_COPY			= 2;
	
	public static final int DEF_MSG_ID_UMS_SYNC_DELETE		= 1;
	public static final int DEF_MSG_ID_UMS_SYNC_RUNNING		= 2;
	public static final int DEF_MSG_ID_UMS_SYNC_COMPLETED	= 3;

	
	/*
	 * Caption Message Type.
	 */
    public static final int DEF_MESSAGE_TYPE_SCROLL			= 0;
    public static final int DEF_MESSAGE_TYPE_STATIC_LEFT	= 1;
    public static final int DEF_MESSAGE_TYPE_STATIC_RIGHT	= 2;
    public static final int DEF_MESSAGE_TYPE_STATIC_MIDDLE	= 5;
    public static final int DEF_MESSAGE_TYPE_WRAP_UP		= 8;
    public static final int DEF_MESSAGE_TYPE_WRAP_DOWN		= 9;
    public static final int DEF_MESSAGE_TYPE_WRAP_STOP_UP	= 12;
    public static final int DEF_MESSAGE_TYPE_WRAP_STOP_DOWN	= 13;
	
    /*
     * Configuration Items
     */
    public static final int DEF_CFG_SERVER_MODE_INTERNET	= 0x01;
	public static final int DEF_CFG_SCREEN_LANDSCAPE		= 0x00;
	public static final int DEF_CFG_SCREEN_PORTRAIT			= 0x01;
	
	public static final int DEF_CFG_ITEM_VALUE_USE			= 0x00;
	public static final int DEF_CFG_ITEM_VALUE_NOT_USE		= 0x01;
	
	public static final int DEF_CFG_ITEM_VALUE_AUTO			= 0x00;
	public static final int DEF_CFG_ITEM_VALUE_FIX			= 0x01;

	public static final int DEF_CFG_ITEM_VALUE_STANDARD		= 0x00;
	public static final int DEF_CFG_ITEM_VALUE_INTERACTIVE	= 0x01;

	public static final int DEF_CFG_ITEM_VALUE_NTSC			= 0x00;
	public static final int DEF_CFG_ITEM_VALUE_PAL			= 0x01;
	
	public static final int DEF_CFG_ITEM_VALUE_720P			= 0x02;
	public static final int DEF_CFG_ITEM_VALUE_1080P		= 0x04;

	public static final int DEF_CFG_ITEM_VALUE_STEREO		= 0x00;
	public static final int DEF_CFG_ITEM_VALUE_SURROUN		= 0x01;
	
	public static final int DEF_CFG_ITEM_VALUE_STANDALONE	= 0x00;
	public static final int DEF_CFG_ITEM_VALUE_INTERNET		= 0x01;
	
	public static final int DEF_CFG_ITEM_VALUE_CAP_TEXT		= 0x00;
	public static final int DEF_CFG_ITEM_VALUE_CAP_TEXT_RSS	= 0x01;
	
	public static final int DEF_CFG_MESSAGE_DEFAULT_HEIGHT		= 80;
	public static final int DEF_CFG_MESSAGE_POSITION_TOP		= 0;
	public static final int DEF_CFG_MESSAGE_POSITION_BOTTOM		= 1;
	public static final int DEF_CFG_MESSAGE_POSITION_LEFT		= 2;
	public static final int DEF_CFG_MESSAGE_POSITION_RIGHT		= 3;
	
    public static final int DEF_CFG_MESSAGE_MODE_TEXT_BMP		= 0;
    public static final int DEF_CFG_MESSAGE_MODE_TEXT_BMP_RSS	= 1;
    
    public static final int DEF_CFG_MESSAGE_TYPE_SCROLL			= 0;
    public static final int DEF_CFG_MESSAGE_TYPE_STATIC_LEFT	= 1;
    public static final int DEF_CFG_MESSAGE_TYPE_STATIC_RIGHT	= 2;
    public static final int DEF_CFG_MESSAGE_TYPE_STATIC_MIDDLE	= 5;
    public static final int DEF_CFG_MESSAGE_TYPE_WRAP_UP		= 8;
    public static final int DEF_CFG_MESSAGE_TYPE_WRAP_DOWN		= 9;
    public static final int DEF_CFG_MESSAGE_TYPE_WRAP_STOP_UP	= 12;
    public static final int DEF_CFG_MESSAGE_TYPE_WRAP_STOP_DOWN	= 13;

    public static final int DEF_CFG_MESSAGE_RSS_MODE_DESC		= 0;
    public static final int DEF_CFG_MESSAGE_RSS_MODE_TITLE		= 1;
    public static final int DEF_CFG_MESSAGE_RSS_MODE_TITLE_DESC	= 2;
    
    
    /*
     * Contents type.
     */
	public static final int DEF_CONTENTS_TYPE_UNKNOWN		= 0;
	public static final int DEF_CONTENTS_TYPE_VIDEO			= 1;
	public static final int DEF_CONTENTS_TYPE_IMAGE			= 2;
	public static final int DEF_CONTENTS_TYPE_TEXT			= 3;
	public static final int DEF_CONTENTS_TYPE_BGM			= 4;
	public static final int DEF_CONTENTS_TYPE_RSS			= 5;
	
	/*
	 * Player message 
	 */
	public static final int DEF_PLAYER_MSG_ID_PLAYER_END_VIDEO		= 1;
	public static final int DEF_PLAYER_MSG_ID_PLAYER_END_IMAGE		= 2;
	public static final int DEF_PLAYER_MSG_ID_PLAYER_END_INPUT		= 3;
	
	/*
	 * Player Attributes.
	 */
    public static final int DEF_MESSAGE_MODE_TEXT_BMP		= 0;
    public static final int DEF_MESSAGE_MODE_TEXT_BMP_RSS	= 1;
    
	public static final int DEF_MESSAGE_DEFAULT_HEIGHT		= 80;
	public static final int DEF_MESSAGE_POSITION_TOP		= 0;
	public static final int DEF_MESSAGE_POSITION_BOTTOM		= 1;
	public static final int DEF_MESSAGE_POSITION_LEFT		= 2;
	public static final int DEF_MESSAGE_POSITION_RIGHT		= 3;
	
    public static final int DEF_MESSAGE_RSS_MODE_DESC		= 0;
    public static final int DEF_MESSAGE_RSS_MODE_TITLE		= 1;
    public static final int DEF_MESSAGE_RSS_MODE_TITLE_DESC	= 2;
    
	public static final int DEF_MESSAGE_DIR_MODE_LANDSCAPE	= 0;
	public static final int DEF_MESSAGE_DIR_MODE_PORTRAIT	= 1;
	
	public static final int DEF_MESSAGE_DIR_LEFT			= 0;
	public static final int DEF_MESSAGE_DIR_RIGHT			= 1;
	public static final int DEF_MESSAGE_DIR_UP				= 2;
	public static final int DEF_MESSAGE_DIR_DOWN			= 3;
	
	public static final	int DEF_PIC_EFFECT_NONE				= 0;
	public static final	int DEF_PIC_EFFECT_RANDOM			= 1;
	public static final	int DEF_PIC_EFFECT_CROSS_FADE		= 2;
	public static final	int DEF_PIC_EFFECT_RIGHT			= 3;
	public static final	int DEF_PIC_EFFECT_LEFT				= 4;
	public static final	int DEF_PIC_EFFECT_UP				= 5;
	public static final	int DEF_PIC_EFFECT_GROWING			= 6;
	public static final	int DEF_PIC_EFFECT_DOWN				= 7;
	
	public static final int DEF_CLOCK_DEFAULT_CAP_WIDTH		= 12;
	public static final int DEF_CLOCK_DEFAULT_CAP_HEIGHT	= 6;
	
	public static final int DEF_CLOCK_POS_LEFT_TOP			= 1;
	public static final int DEF_CLOCK_POS_RIGHT_TOP			= 2;
	public static final int DEF_CLOCK_POS_LEFT_BOTTOM		= 3;
	public static final int DEF_CLOCK_POS_RIGHT_BOTTOM		= 4;


	public static final String EXTRA_SYNC_TYPE = "com.dignsys.dsdsp.EXTRA_SYNC_TYPE";
	public static final int SYNC_ALL					= 0;
	public static final int SYNC_PLAY_ONLY				= 1;
	public static final int SYNC_COMMAND_ONLY 			= 2;
	public static final int SYNC_CONFIG_COMMAND_ONLY = 3;
	public static final int SYNC_RSS_ONLY				= 4;

	public static final int UPLOAD_LOG					= 5;
	public static final int UPLOAD_LIVE_SCREEN			= 6;

	public static final String TEST_PLAYLIST_URL = "http://192.168.1.132/site/bawooriTest/test/playlist.txt" ;
	public static final String TEST_FORMAT_URL = "http://192.168.1.132/site/bawooriTest/test/format.txt";
	public static final String TEST_CONFIG_URL = "http://192.168.1.132/site/bawooriTest/test/dspconfig.txt";
	public static final String TEST_COMMAND_URL = "http://192.168.1.132/site/bawooriTest/test/command.txt";
	public static final String TEST_RSS_URL = "http://open.moleg.go.kr/data/xml/lh_rssSH03.xml";



}
