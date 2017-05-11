package awvillager.gradle;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.gradle.api.DefaultTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.DependencyResolutionListener;
import org.gradle.api.artifacts.DependencySet;
import org.gradle.api.artifacts.ResolvableDependencies;
import org.gradle.api.artifacts.repositories.ArtifactRepository;
import org.gradle.api.internal.artifacts.dsl.DefaultRepositoryHandler;
import org.gradle.api.internal.artifacts.repositories.DefaultBaseRepositoryFactory;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.javadoc.Javadoc;

public class VillagerGradlePlugin implements Plugin<Project> {

	static final String EXTENSIONS_AIWOLF = "aiwolf";

	static final String TASK_TEST = "testHoge";
	static final String TASK_SET_UP_WORKSPACE = "setupWorkspace";
	static final String TASK_DOWNLOAD_AIWOLF = "downloadAIWolf";
	static final String TASK_DOWNLOAD_AIWOLF_DOC = "downloadAIWolfDoc";

	//AIWolfをjarを管理するDir
	static final String DIR_AIWOLF = "aw_bin";

	static final String DIR_AIWOLF_JAR_PREFIX = "AIWolf-ver";
	static final String DIR_AIWOLF_JAVADOC_PREFIX = "AIWolf-docs-ver";

	private Project project;

	@Override
	public void apply(Project arg0) {

		this.project = arg0;

		//ext = project.getExtensions().create(EXTENSIONS_AIWOLF, AWExtension.class,project);

		//project.getExtensions().add(EXTENSIONS_AIWOLF, AWExtension.class);

		//AWExtension ext = (AWExtension) project.getExtensions().getByName(EXTENSIONS_AIWOLF);

		project.getExtensions().add(EXTENSIONS_AIWOLF, new AWExtension());

		makeTask(TASK_TEST, VillagerTestTask.class);

		makeTask(TASK_DOWNLOAD_AIWOLF, DownloadTask.class);
		//makeTask(TASK_DOWNLOAD_AIWOLF_DOC, DownloadDocTask.class);

		makeTask(TASK_SET_UP_WORKSPACE, DefaultTask.class)
		.dependsOn(this.project.getTasks().getByName(TASK_DOWNLOAD_AIWOLF));


		//ローカルにリポジトリを作成
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("name", "localRepo");
		map.put("dirs", new File(this.project.getProjectDir(),"test"));

		//Class class =DefaultBaseRepositoryFactory.class;
		//DefaultBaseRepositoryFactory
		//this.project.
		//((DefaultRepositoryHandler)this.project.getRepositories()).flatDir(map);

		//依存関係の解決
		DependencySet compileDeps = project.getConfigurations().getByName("compile").getDependencies();

				project.getGradle().addListener(new DependencyResolutionListener() {
				    @Override
					public
				    void beforeResolve(ResolvableDependencies resolvableDependencies) {

				    	String version = getVersion();
				        compileDeps.add(
				        		project.getDependencies().add(
				        				"compile", project.fileTree("aw_bin/"+DIR_AIWOLF_JAR_PREFIX+version)));

				        project.getGradle().removeListener(this);
				    }

				    @Override
					public
				    void afterResolve(ResolvableDependencies resolvableDependencies) {}
				});

				//javadoc
				//Javadoc javadoc = (Javadoc) project.getTasks().getByName(JavaPlugin.JAVADOC_TASK_NAME);

	}

	public DefaultTask makeTask(String name) {
		return makeTask(name, DefaultTask.class);
	}

	public <T extends Task> T makeTask(String name, Class<T> type) {
		return makeTask(project, name, type);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Task> T makeTask(Project proj, String name, Class<T> type) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		map.put("type", type);
		map.put("group", "Villager Gradle");
		return (T) proj.task(map, name);
	}

	public String getVersion(){
		return
				((AWExtension)this.project.getExtensions()
				.findByName(VillagerGradlePlugin.EXTENSIONS_AIWOLF)).getVersion();
	}

}
