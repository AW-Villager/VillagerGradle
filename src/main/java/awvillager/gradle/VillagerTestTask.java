package awvillager.gradle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.OutputFiles;
import org.gradle.api.tasks.TaskAction;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;

public class VillagerTestTask extends BaseTask{

	public static final String     USER_AGENT       = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";

	@Input
    private Object url;

    @OutputFile
    private Object output;

	@TaskAction
    public void doTask()
    {

		File outFile = new File(getProject().getProjectDir(),"test.zip");
		File outFile2 = getProject().getProjectDir();

		System.out.println("test---");

		try{
			  File file = new File(outFile2,"hoge.txt");
			  FileWriter filewriter = new FileWriter(file);

			  filewriter.write("テスト");

			  filewriter.close();
			}catch(IOException e){
			  System.out.println(e);
			}

		getLogger().info("Downloading aiwolf.");

		try
        {

			URL url =new URL("http://aiwolf.org/control-panel/wp-content/uploads/2014/03/aiwolf-ver0.4.5.zip");

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setInstanceFollowRedirects(true);
            con.setRequestProperty("User-Agent", USER_AGENT);

            con.connect();

            switch (con.getResponseCode()){

            case 200:
            	InputStream stream = con.getInputStream();
                Files.write(ByteStreams.toByteArray(stream), outFile);
                stream.close();

                // write etag
                /*
                etag = con.getHeaderField("ETag");
                if (!Strings.isNullOrEmpty(etag))
                {
                    Files.write(etag, etagFile, Charsets.UTF_8);
                }*/

                break;

            default :;
            }

            con.disconnect();

        }catch (Throwable e)
        {

        }

    }

	public String getDescription(){
		return "Hoge";
	}



}
