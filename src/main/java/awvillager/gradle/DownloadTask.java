package awvillager.gradle;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.regex.Pattern;

import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.Task;
import org.gradle.api.file.CopySpec;
import org.gradle.api.plugins.BasePlugin;
import org.gradle.api.specs.Spec;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;

/**
 * AIWolfのソースをダウンロードするタスク
 * @author k14041kk
 */
public class DownloadTask extends DefaultTask
{

	@Input
    private Object version;

    @OutputFile
    private Object output;

    static File binFile;

    //ユーザーエージェント
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";

    //どこからダウンロードするか
    private String BASE_URL = "http://aiwolf.org/control-panel/wp-content/uploads/2014/03/aiwolf-ver";

    public DownloadTask(){

    	//AIWolfのデータを置く場所を作成
    	createAIWolfCache();

    	//最新化どうかを返す
    	getOutputs().upToDateWhen(new Spec<Task>() {
            @Override
            public boolean isSatisfiedBy(Task task) {

            	AWExtension exe = DownloadTask.getVersion(task);
            	//System.out.println("isSatisfiedBy---" + exe.getVersion());

            	//System.out.println("aac");
                return hasCache(task);

            }
        });
    }

    public String getDescription(){
		return "Download aiwolf jar.";
	}

    private void createAIWolfCache(){

    	binFile = new File(getProject().getProjectDir(),VillagerGradlePlugin.DIR_AIWOLF);
    	if(!binFile.exists())binFile.mkdirs();

    }

    private boolean hasCache(Task task){

    	AWExtension exe = getVersion(task);

    	File versionFile = new File(binFile,this.getPreFix()+exe.getVersion());

    	return versionFile.exists();

    }

    @TaskAction
    public void doTask() throws IOException
    {
    	AWExtension exe = getVersion(this);

    	if(hasCache(this))return;

    	//Tmpの作成
    	File tmpFile = new File(getProject().getProjectDir(),"tmp");
    	if(!tmpFile.exists())tmpFile.mkdirs();

    	this.downloadAIWolf(exe);

    	//管理用フォルダの作成
    	//File versionFile = new File(binFile,exe.getVersion());
    	//if(!versionFile.exists())versionFile.mkdirs();

    	this.copyAIWolf(exe);

    	//System.out.println("test---" + exe.getVersion());
    }

    private void downloadAIWolf(AWExtension exe) throws IOException{

    	File tmpFile = new File(getProject().getProjectDir(),"tmp");
    	File outFile = new File(tmpFile,"aiwolf.zip");

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

    private void copyAIWolf(AWExtension exe) throws IOException{

    	File tmpFile = new File(getProject().getProjectDir(),"tmp");
    	File outFile = new File(tmpFile,"aiwolf.zip");

    	File versionFile = binFile;//new File(binFile);

    	String version = getVersion(this).getVersion();

    	getProject().copy(new Action<CopySpec>() {
            @Override
			public void execute(CopySpec copySpec) {
            	copySpec.from(getProject().zipTree(outFile));
            	copySpec.into(versionFile);
            }
        });


    }

    protected String getBaseURL(){
    	return this.BASE_URL;
    }

    protected String getPreFix(){
    	return VillagerGradlePlugin.DIR_AIWOLF_JAR_PREFIX;
    }


    public static AWExtension getVersion(Task tack){

    	AWExtension exe = (AWExtension) tack.getProject()
                .getExtensions().findByName(VillagerGradlePlugin.EXTENSIONS_AIWOLF);

    	return exe;

    }



}
