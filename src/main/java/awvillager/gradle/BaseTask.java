package awvillager.gradle;

import java.io.File;

import org.gradle.api.DefaultTask;
import org.gradle.api.Task;

/**
 * 全てのTaskのベースとなるクラス
 * @author kamiya
 *
 */
public class BaseTask extends DefaultTask{

	protected VillagerGradlePlugin plugin;

	public BaseTask(){
	}

	public VillagerGradlePlugin getPlugin() {
		return plugin;
	}

	@SuppressWarnings("unchecked")
	public <E extends BaseTask> E setPlugin(VillagerGradlePlugin plugin){
		this.plugin = plugin;
		/*if(this.){
			return (E) this;
		}*/
		return (E) this;
	}

	public File getBinFile(){
		//if(VillagerGradlePlugin.binFile==null){
		//	VillagerGradlePlugin.binFile =new File(getProject().getProjectDir(),VillagerGradlePlugin.DIR_AIWOLF);
		//}
		return VillagerGradlePlugin.binFile;
	}

	public AWExtension getAWExtension(){

    	AWExtension exe = (AWExtension) this.getProject()
                .getExtensions().findByName(VillagerGradlePlugin.EXTENSIONS_AIWOLF);

    	return exe;

    }


}
