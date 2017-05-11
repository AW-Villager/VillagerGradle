package awvillager.gradle;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.gradle.api.Action;
import org.gradle.api.file.CopySpec;
import org.gradle.api.tasks.bundling.Zip;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;

public class DownloadDocTask extends DownloadTask{

	//どこからダウンロードするか
    private String BASE_URL = "https://github.com/aiwolf/AIWolfCommon/archive/v";

    protected void downloadAIWolf(AWExtension exe) throws IOException{

    	return;
    	/*
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
        */
    }

    protected void copyAIWolf(AWExtension exe) throws IOException{

    	//とりあえずの場所に奥
    	File tmpFile = new File(getProject().getProjectDir(),"tmp");
    	File outFile = new File(tmpFile,"aiwolf_sources_"+exe.getVersion()+".zip");

    	File versionFile = tmpFile;//binFile;//new File(binFile);

    	getProject().copy(new Action<CopySpec>() {
            @Override
			public void execute(CopySpec copySpec) {
            	copySpec.from(getProject().zipTree(outFile));
            	copySpec.into(new File(versionFile,"test.zip"));
            }
        });

    	/*Zip zip = new Zip();
    	zip.from(new File(versionFile,"AIWolfCommon-0.4.5/src"));
    	zip.setArchiveName("test1.zip");*/



    	/*
    	getProject().copy(new Action<CopySpec>() {
            @Override
			public void execute(CopySpec copySpec) {
            	copySpec.from(new File(versionFile,"AIWolf-ver"+exe.getVersion()));
            	copySpec.into(new File(binFile,exe.getVersion()));

            	//TODO 頭悪い方法. Hoge-Foo.jar -> Hoge_Foo-{version}.jarの正規表現ください
            	copySpec.rename("aiwolf-common.jar", "aiwolf_common-"+exe.getVersion()+".jar");
            	copySpec.rename("aiwolf-client.jar", "aiwolf_client-"+exe.getVersion()+".jar");
            	copySpec.rename("aiwolf-server.jar", "aiwolf_server-"+exe.getVersion()+".jar");
            	copySpec.rename("aiwolf-viewer.jar", "aiwolf_viewer-"+exe.getVersion()+".jar");
            }
        });*/


    }


	protected String getBaseURL(){
    	return this.BASE_URL;
    }

    protected String getPreFix(){
    	return VillagerGradlePlugin.DIR_AIWOLF_JAVADOC_PREFIX;
    }

}
