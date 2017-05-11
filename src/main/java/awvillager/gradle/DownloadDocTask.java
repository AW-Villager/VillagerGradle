package awvillager.gradle;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.api.file.CopySpec;
import org.gradle.api.tasks.bundling.Zip;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;

import awvillager.gradle.util.ZipUtil;

public class DownloadDocTask extends DownloadTask{

	//どこからダウンロードするか
    private String BASE_URL = "https://github.com/aiwolf/AIWolfCommon/archive/v";

    protected boolean hasCache(Task task){

    	AWExtension exe = getVersion(task);

    	File versionFile = new File(binFile,exe.getVersion());
    	File comon = new File(versionFile,"aiwolf_common-0.4.5-sources.jar");

    	return comon.exists();

    }

    protected void downloadAIWolf(AWExtension exe) throws IOException{

    	File tmpFile = new File(getProject().getProjectDir(),"tmp");
    	File outFile = new File(tmpFile,"aiwolf_sources_"+exe.getVersion()+".zip");

    	URL url =new URL(this.getBaseURL() + exe.getVersion() +".zip");

    	System.out.println(url);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setInstanceFollowRedirects(true);
        con.setRequestProperty("User-Agent", USER_AGENT);

        con.connect();

        switch (con.getResponseCode()){

        case 200:
        	InputStream stream = con.getInputStream();
            Files.write(ByteStreams.toByteArray(stream), outFile);
            stream.close();

            break;

        default :;
        }

        con.disconnect();

    }

    protected void copyAIWolf(AWExtension exe) throws IOException{

    	//とりあえずの場所に奥
    	File tmpFile = new File(getProject().getProjectDir(),"tmp");
    	File outFile = new File(tmpFile,"aiwolf_sources_"+exe.getVersion()+".zip");

    	File repo = new File(getProject().getProjectDir(),VillagerGradlePlugin.DIR_AIWOLF+"/"+exe.getVersion());
    	if(!repo.exists())repo.mkdirs();

    	File versionFile = tmpFile;//binFile;//new File(binFile);

    	getProject().copy(new Action<CopySpec>() {
            @Override
			public void execute(CopySpec copySpec) {
            	copySpec.from(getProject().zipTree(outFile));
            	copySpec.into(new File(versionFile,"test.zip"));
            }
        });

    	//Zip化
    	try {
			ZipUtil.copyToZip(
					new File(versionFile,"AIWolfCommon-0.4.5/src/org"),
					new File(repo,"aiwolf_common-0.4.5-sources.jar"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}




    }


	protected String getBaseURL(){
    	return this.BASE_URL;
    }

    protected String getPreFix(){
    	return VillagerGradlePlugin.DIR_AIWOLF_JAVADOC_PREFIX;
    }

}
