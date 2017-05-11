package awvillager.gradle;

public class DownloadDocTask extends DownloadTask{

	//どこからダウンロードするか
    private String BASE_URL = "https://github.com/aiwolf/AIWolfCommon/archive/v";

	protected String getBaseURL(){
    	return this.BASE_URL;
    }

    protected String getPreFix(){
    	return VillagerGradlePlugin.DIR_AIWOLF_JAVADOC_PREFIX;
    }

}
