package awvillager.gradle;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.api.file.CopySpec;
import org.gradle.api.specs.Spec;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;

/**
 * AIWolfのソースをダウンロードするタスク
 * 
 * @author k14041kk
 */
public class DownloadTask extends BaseTask {

    @Input
    private Object version;

    @OutputFile
    private Object output;

    // static File binFile;

    File versionFile;

    File tmpFile;

    // ユーザーエージェント
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";

    // どこからダウンロードするか
    private String BASE_URL = "http://aiwolf.org/control-panel/wp-content/uploads/2014/03/aiwolf-ver";

    public DownloadTask() {

        // 最新化どうかを返す
        getOutputs().upToDateWhen(new Spec<Task>() {
            @Override
            public boolean isSatisfiedBy(Task task) {

                AWExtension exe = DownloadTask.getVersion(task);
                versionFile = new File(getBinFile(), exe.getAIWolfVersion());
                // System.out.println("isSatisfiedBy---" + exe.getVersion());

                // System.out.println("aac");
                return hasCache(task);

            }
        });
    }

    @Override
    public String getDescription() {
        return "Download aiwolf jar.";
    }

    protected boolean hasCache(Task task) {

        AWExtension exe = getVersion(task);

        // File versionFile = new File(getBinFile(),exe.getVersion());
        File commonFile = new File(versionFile, "aiwolf_common-" + exe.getAIWolfVersion() + ".jar");

        return commonFile.exists();

    }

    @TaskAction
    public void doTask() throws IOException {
        AWExtension exe = getVersion(this);

        // AIWolfのデータを置く場所を作成
        this.createAIWolfVersionCache();

        // キャッシュがある場合はスキップ
        if (hasCache(this)) return;

        // Tmpの作成
        this.createTmp();

        this.downloadAIWolf(exe);

        // 管理用フォルダの作成
        // File versionFile = new File(binFile,exe.getVersion());
        // if(!versionFile.exists())versionFile.mkdirs();

        this.copyAIWolf(exe);

        // System.out.println("test---" + exe.getVersion());
    }

    /**
     * バージョン別のキャッシュフォルダを作成
     */
    private void createAIWolfVersionCache() {

        AWExtension exe = this.getAWExtension();

        versionFile = new File(getBinFile(), exe.getAIWolfVersion());

        if (!versionFile.exists()) versionFile.mkdirs();

    }

    /** 仮置きの場所を作成 */
    private void createTmp() {

        tmpFile = new File(getProject().getProjectDir(), "tmp");
        if (!tmpFile.exists()) tmpFile.mkdirs();

    }

    protected void downloadAIWolf(AWExtension exe) throws IOException {

        File tmpFile = new File(getProject().getProjectDir(), "tmp");
        File outFile = new File(tmpFile, "aiwolf_" + exe.getAIWolfVersion() + ".zip");

        URL url = new URL(this.getBaseURL() + exe.getAIWolfVersion() + ".zip");

        System.out.println(url);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setInstanceFollowRedirects(true);
        con.setRequestProperty("User-Agent", USER_AGENT);

        con.connect();

        switch (con.getResponseCode()) {

        // TODO 404とかの処理
        case 200:
            InputStream stream = con.getInputStream();
            Files.write(ByteStreams.toByteArray(stream), outFile);
            stream.close();

            break;

        default:
            ;
        }

        con.disconnect();
    }

    protected void copyAIWolf(AWExtension exe) throws IOException {

        // とりあえずの場所に奥
        File tmpFile = new File(getProject().getProjectDir(), "tmp");
        File outFile = new File(tmpFile, "aiwolf_" + exe.getAIWolfVersion() + ".zip");

        File versionFile = tmpFile;// binFile;//new File(binFile);

        getProject().copy(new Action<CopySpec>() {
            @Override
            public void execute(CopySpec copySpec) {
                copySpec.from(getProject().zipTree(outFile));
                copySpec.into(versionFile);
            }
        });

        getProject().copy(new Action<CopySpec>() {
            @Override
            public void execute(CopySpec copySpec) {
                copySpec.from(new File(versionFile, "AIWolf-ver" + exe.getAIWolfVersion()));
                copySpec.into(new File(getBinFile(), exe.getAIWolfVersion()));

                // TODO 頭悪い方法. Hoge-Foo.jar -> Hoge_Foo-{version}.jarの正規表現ください
                copySpec.rename("aiwolf-common.jar", "aiwolf_common-" + exe.getAIWolfVersion() + ".jar");
                copySpec.rename("aiwolf-client.jar", "aiwolf_client-" + exe.getAIWolfVersion() + ".jar");
                copySpec.rename("aiwolf-server.jar", "aiwolf_server-" + exe.getAIWolfVersion() + ".jar");
                copySpec.rename("aiwolf-viewer.jar", "aiwolf_viewer-" + exe.getAIWolfVersion() + ".jar");
            }
        });

    }

    protected String getBaseURL() {
        return this.BASE_URL;
    }

    protected String getPreFix() {
        return VillagerGradlePlugin.DIR_AIWOLF_JAR_PREFIX;
    }

    public static AWExtension getVersion(Task tack) {

        AWExtension exe = (AWExtension) tack.getProject().getExtensions().findByName(VillagerGradlePlugin.EXTENSIONS_AIWOLF);

        return exe;

    }

}
